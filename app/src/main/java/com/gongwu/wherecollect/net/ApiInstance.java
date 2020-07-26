package com.gongwu.wherecollect.net;


import android.text.TextUtils;

import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.contract.AppConstant;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author pengqm
 * @name FilmApplication
 * @class name：com.baidu.iov.dueros.film.net
 * @time 2018/10/10 8:52
 * @change
 * @class describe
 */

public class ApiInstance {

    private static final String TAG = "ApiInstance";
//    public static final String TV_Host_Url = "http://tv.ichunsun.com";

    Retrofit mRetrofit;

    private ApiInstance() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build();
    }

    public static ApiInstance getApiInstance() {
        return SingletonHandler.sApiInstance;
    }

    private TakeawayApi mWaimaiApi;

    public static TakeawayApi getApi() {
        ApiInstance instance = getApiInstance();
        if (instance.mWaimaiApi == null) {
            instance.mWaimaiApi = instance.mRetrofit.create(TakeawayApi.class);
        }
        return instance.mWaimaiApi;
    }


    private static class SingletonHandler {
        private static final ApiInstance sApiInstance = new ApiInstance();
    }

    private OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //set log Level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //set Cookie and Content-Type
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader(Config.CONTENT_TYPE_KEY, Config.CONTENT_TYPE_VALUE)
//                        .addHeader(Config.COOKIE_KEY, Config.COOKIE_VALUE)
                        .build();
                return chain.proceed(request);
            }
        });
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(new AddCookiesInterceptor());
        httpClient.addInterceptor(new ReceivedCookiesInterceptor());
        httpClient.connectTimeout(10, TimeUnit.SECONDS); //连接超时
        return httpClient.build();
    }

    public class ReceivedCookiesInterceptor implements Interceptor {

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {

            Response originalResponse = chain.proceed(chain.request());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {

                //解析Cookie
                for (String header : originalResponse.headers("Set-Cookie")) {
                    if(header.contains("shounaer_app_cookie_session")){
                        AppConstant.COOKIE = header.substring(header.indexOf("shounaer_app_cookie_session"), header.indexOf(";"));
                    }
                }
            }
            return originalResponse;
        }

    }

    public class AddCookiesInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            final Request.Builder builder = chain.request().newBuilder();
            //添加Cookie
            if(!TextUtils.isEmpty(AppConstant.COOKIE)){
                builder.addHeader("Cookie", AppConstant.COOKIE);
            }
            return chain.proceed(builder.build());
        }
    }
}
