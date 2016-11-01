package com.robot.simplenews.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2016/10/27.
 */
public class OkHttpClientHelper {
    public OkHttpClient provideApiOkHttpClient(
            CookieInterceptor cookieInterceptor) {
        OkHttpClient.Builder builder =
                new OkHttpClient.Builder().connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                        .readTimeout(20 * 1000, TimeUnit.MILLISECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
        builder.addInterceptor(cookieInterceptor);
        return builder.build();
    }
}
