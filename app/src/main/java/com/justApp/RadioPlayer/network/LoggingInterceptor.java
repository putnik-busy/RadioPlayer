package com.justApp.RadioPlayer.network;

import com.justApp.RadioPlayer.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author Sergey Rodionov
 */

public class LoggingInterceptor implements Interceptor {

    private static LoggingInterceptor sLoggingInterceptor;
    private HttpLoggingInterceptor mHttpLoggingInterceptor;

    public LoggingInterceptor() {
        mHttpLoggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY :
                        HttpLoggingInterceptor.Level.NONE);
    }

    public static LoggingInterceptor getLoggingInterceptor() {
        if (sLoggingInterceptor == null) {
            sLoggingInterceptor = new LoggingInterceptor();
        }
        return sLoggingInterceptor;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return mHttpLoggingInterceptor.intercept(chain);
    }
}
