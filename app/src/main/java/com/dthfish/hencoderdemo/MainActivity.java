package com.dthfish.hencoderdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dthfish.hencoderdemo.activity.AvatarActivity;
import com.dthfish.hencoderdemo.activity.CameraActivity;
import com.dthfish.hencoderdemo.activity.CustomViewActivity;
import com.dthfish.hencoderdemo.activity.DashActivity;
import com.dthfish.hencoderdemo.activity.MaterialEditTextActivity;
import com.dthfish.hencoderdemo.activity.PagerViewActivity;
import com.dthfish.hencoderdemo.activity.ParagraphActivity;
import com.dthfish.hencoderdemo.activity.PieChartActivity;
import com.dthfish.hencoderdemo.activity.ScalableImageActivity;
import com.dthfish.hencoderdemo.activity.SportActivity;
import com.dthfish.hencoderdemo.activity.TagLayoutActivity;
import com.dthfish.hencoderdemo.jbox2d.Box2DActivity;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_to_dash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DashActivity.class));
            }
        });
        findViewById(R.id.btn_to_pie_chart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PieChartActivity.class));
            }
        });
        findViewById(R.id.btn_to_sport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SportActivity.class));
            }
        });
        findViewById(R.id.btn_to_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AvatarActivity.class));
            }
        });
        findViewById(R.id.btn_to_paragraph).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ParagraphActivity.class));
            }
        });
        findViewById(R.id.btn_to_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
            }
        });
        findViewById(R.id.btn_to_box2d).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Box2DActivity.class));
            }
        });
        findViewById(R.id.btn_to_material).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MaterialEditTextActivity.class));
            }
        });
        findViewById(R.id.btn_to_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CustomViewActivity.class));
            }
        });
        findViewById(R.id.btn_to_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TagLayoutActivity.class));
            }
        });
        findViewById(R.id.btn_to_scalable_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScalableImageActivity.class));
            }
        });
        findViewById(R.id.btn_to_pager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PagerViewActivity.class));
            }
        });


//        httpMethod();


    }

    private void httpMethod() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<String> call = apiService.getText("");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        apiService.getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("").build();
        okhttp3.Call okCall = okHttpClient.newCall(request);
        okCall.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

            }
        });
    }
}
