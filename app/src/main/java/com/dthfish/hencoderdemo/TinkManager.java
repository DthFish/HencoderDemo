package com.dthfish.hencoderdemo;

import android.content.Context;
import android.util.Base64;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.integration.android.AndroidKeysetManager;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Description 加密解密
 * Author DthFish
 * Date  2018/9/5.
 */
public class TinkManager {
    private static final String ASSOCIATED_DATA = "DTHFISH";
    private static TinkManager sInstance;

    private AndroidKeysetManager mManager;

    private TinkManager(Context context) {

        try {
            AeadConfig.register();
            String masterKeyUri = "android-keystore://dthfish_key_id";
            mManager = new AndroidKeysetManager.Builder()
                    .withSharedPref(context.getApplicationContext(), "password", "dthfish_keyset")
                    .withKeyTemplate(AeadKeyTemplates.AES128_GCM)
                    .withMasterKeyUri(masterKeyUri)
                    .build();

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

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

    public String encrypt(final String plaintext) {

        try {
            Aead aead = AeadFactory.getPrimitive(mManager.getKeysetHandle());

            byte[] ciphertext = aead.encrypt(plaintext.getBytes(), ASSOCIATED_DATA.getBytes());
            return Base64.encodeToString(ciphertext, Base64.DEFAULT);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(final String ciphertext) {
        try {
            Aead aead = AeadFactory.getPrimitive(mManager.getKeysetHandle());

            byte[] plaintext = aead.decrypt(Base64.decode(ciphertext, Base64.DEFAULT), ASSOCIATED_DATA.getBytes());
            return new String(plaintext);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;

    }
}
