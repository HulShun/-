package com.example.newsclient.Model.utils;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016-04-12.
 */
public class RetrofitUtil {
    public static OkHttpClient createClinet(final String header, final String value) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader(header, value).build();
                return chain.proceed(request);
            }
        };
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        client.interceptors().add(interceptor);
        return client;
    }
}
