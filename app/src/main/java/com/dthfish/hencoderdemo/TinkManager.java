package com.dthfish.hencoderdemo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.JsonKeysetWriter;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.aead.AeadKeyTemplates;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Description 加密解密
 * Author DthFish
 * Date  2018/9/5.
 */
public class TinkManager {
    private static TinkManager sInstance;
    private Context mContext;
    private KeysetHandle mKeysetHandle;
    private static final String ASSOCIATED_DATA = "DTHFISH";
    private static final String TAG = "TinkManager";

    private static final String FILE_NAME = "dthfish_keyset.json";
    private AtomicInteger mTry;

    private TinkManager(Context context) {
        try {
            AeadConfig.register();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        mTry = new AtomicInteger();
        mContext = context.getApplicationContext();
        Observable.create(new ObservableOnSubscribe<KeysetHandle>() {
            @Override
            public void subscribe(ObservableEmitter<KeysetHandle> emitter) throws Exception {
                mTry.getAndIncrement();
                File keysetFile = new File(mContext.getFilesDir(), FILE_NAME);
                KeysetHandle keysetHandle = null;
                // read
                if (keysetFile.exists()) {
                    Log.d(TAG, "TinkManager: " + "文件存在读取文件！");
                    try {

                        keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withFile(keysetFile));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                // generate
                if (keysetHandle == null) {
                    Log.d(TAG, "TinkManager: " + "生成 KeysetHandle！");
                    keysetHandle = KeysetHandle.generateNew(AeadKeyTemplates.AES128_GCM);
                }
                // write
                if (!keysetFile.exists()) {
                    Log.d(TAG, "TinkManager: " + "保存 KeysetHandle！");
                    CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(keysetFile));
                }
                emitter.onNext(keysetHandle);
                emitter.onComplete();

            }
        })
                .retry(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<KeysetHandle>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(KeysetHandle keysetHandle) {
                        Log.d(TAG, "TinkManager: " + "KeysetHandle 准备完成！");
                        mKeysetHandle = keysetHandle;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public static TinkManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (TinkManager.class) {
                if (sInstance == null) {
                    sInstance = new TinkManager(context);
                }
            }
        }
        return sInstance;
    }

    public interface Callback {

        void onSuccess(String text);
    }

    public void encrypt(final String plaintext, final Callback callback) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                while (mKeysetHandle == null) {
                    if (mTry.get() == 4) {
                        break;
                    }
                }
                Aead aead = AeadFactory.getPrimitive(mKeysetHandle);

                byte[] ciphertext = aead.encrypt(plaintext.getBytes(), ASSOCIATED_DATA.getBytes());
                emitter.onNext(toHex(ciphertext));
                emitter.onComplete();

            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "TinkManager: " + "开始加密：" + plaintext);

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "TinkManager: " + "加密完成：" + s);
                        if (callback != null) {
                            callback.onSuccess(s);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "TinkManager: " + "加密失败：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void decrypt(final String ciphertext, final Callback callback) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                while (mKeysetHandle == null) {
                    if (mTry.get() == 4) {
                        break;
                    }
                }
                Aead aead = AeadFactory.getPrimitive(mKeysetHandle);

                byte[] plaintext = aead.decrypt(toByte(ciphertext), ASSOCIATED_DATA.getBytes());
                emitter.onNext(new String(plaintext));
                emitter.onComplete();

            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "TinkManager: " + "开始解密：" + ciphertext);

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "TinkManager: " + "解密完成：" + s);
                        if (callback != null) {
                            callback.onSuccess(s);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "TinkManager: " + "解密失败：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static byte[] toByte(String hexString) {
        if (TextUtils.isEmpty(hexString))
            return null;
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() >> 1];
        int index = 0;
        for (int i = 0; i < hexString.length(); i++) {
            if (index > hexString.length() - 1)
                return byteArray;
            byte highDit = (byte) (Character.digit(hexString.charAt(index), 16) & 0xFF);
            byte lowDit = (byte) (Character.digit(hexString.charAt(index + 1), 16) & 0xFF);
            byteArray[i] = (byte) (highDit << 4 | lowDit);
            index += 2;
        }
        return byteArray;
    }


    /**
     * byte[] to Hex string.
     *
     * @param byteArray the byte array
     * @return the string
     */
    public static String toHex(byte[] byteArray) {
        final StringBuilder hexString = new StringBuilder("");
        if (byteArray == null || byteArray.length <= 0)
            return null;
        for (int i = 0; i < byteArray.length; i++) {
            int v = byteArray[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                hexString.append(0);
            }
            hexString.append(hv);
        }
        return hexString.toString().toLowerCase();
    }
}
