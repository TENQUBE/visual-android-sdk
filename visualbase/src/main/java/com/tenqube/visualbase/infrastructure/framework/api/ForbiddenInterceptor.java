package com.tenqube.visualbase.infrastructure.framework.api;

import com.tenqube.visualbase.domain.auth.AuthService;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ForbiddenInterceptor implements Interceptor {
    private final AuthService authService;

    public ForbiddenInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);
        if(response.code() == 403) {
            authService.signOut();
        }

        return response;
    }

}
