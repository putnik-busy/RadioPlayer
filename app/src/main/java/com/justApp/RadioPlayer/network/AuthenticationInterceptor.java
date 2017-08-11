package com.justApp.RadioPlayer.network;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Sergey Rodionov
 */

public class AuthenticationInterceptor implements Interceptor {

    private static AuthenticationInterceptor sAuthenticationInterceptor;
    private String mToken;

    public AuthenticationInterceptor(String token) {
        this.mToken = token;
    }

    public static AuthenticationInterceptor getAuthenticationInterceptor(String token) {
        if (sAuthenticationInterceptor == null) {
            sAuthenticationInterceptor = new AuthenticationInterceptor(token);
        }
        return sAuthenticationInterceptor;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();
        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("token", mToken)
                .build();

        Request.Builder requestBuilder = original.newBuilder()
                .url(url);
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}