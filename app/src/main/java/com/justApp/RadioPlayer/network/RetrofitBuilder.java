package com.justApp.RadioPlayer.network;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.justApp.RadioPlayer.BuildConfig.TOKEN;

/**
 * @author Sergey Rodionov
 */

public class RetrofitBuilder {

    public static final String BASE_URL = "http://api.dirble.com/v2/stations/";
    private static Retrofit mRetrofit;
    private static OkHttpClient mOkClient;

    @NonNull
    private static OkHttpClient getOkClient() {
        if (mOkClient == null) {
            mOkClient = new OkHttpClient.Builder()
                    .addInterceptor(LoggingInterceptor.getLoggingInterceptor())
                    .addInterceptor(AuthenticationInterceptor.getAuthenticationInterceptor(TOKEN))
                    .addNetworkInterceptor(LoggingInterceptor.getLoggingInterceptor())
                    .readTimeout(1, TimeUnit.MINUTES)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .build();
        }
        return mOkClient;
    }

    private static Retrofit getRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkClient())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public static <T> T createRetrofitService(Class<T> tClass) {
        return getRetrofit().create(tClass);
    }

}
