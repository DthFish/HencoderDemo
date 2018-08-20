package com.dthfish.hencoderdemo;

import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Description
 * Author DthFish
 * Date  2018/7/10.
 */

public interface ApiService {
    @GET("ddd/dd")
    Call<String> getText(@Nullable String s);

    @GET("sss/ss")
    Observable<String> getObservable();
}
