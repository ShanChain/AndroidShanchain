package com.shanchain.shandata.base.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.jkyeo.basicparamsinterceptor.BasicParamsInterceptor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 请求的基类
 * Created by xiongyikai on 2017/2/16.
 */

public class RequestBase {

    private static final String TAG = RequestBase.class.getSimpleName();

    private RequestBase(){}

    private static class SingletonHolder{
        private static final RequestBase INSTANCE = new RequestBase();
    }

    public static RequestBase getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private Retrofit mRetrofit;

    public Retrofit getRetrofitInstance(Context context , String baseUrl , Interceptor basicParamInterceptor , Interceptor responseIntercepter){
        initRetrofitInstance(context , baseUrl , basicParamInterceptor , responseIntercepter);
        return mRetrofit;
    }

    private void initRetrofitInstance(Context context , String baseUrl , Interceptor paramInterceptor , Interceptor responseIntercepter){
        //参数打印拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //公共参数拦截器
        BasicParamsInterceptor basicParamsInterceptor = new BasicParamsInterceptor.Builder()
                .build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60*30 , TimeUnit.SECONDS)
                .addInterceptor(basicParamsInterceptor)
                .addInterceptor(paramInterceptor)
                .addInterceptor(responseIntercepter)
                .addInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)
                .build();
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();
        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    class RequestBodyInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            RequestBody requestBody = request.body();

            return null;
        }
    }

    private static final Charset UTF8 = Charset.forName("UTF-8");
    class ResponseBodyInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            Charset charset = UTF8;
            ResponseBody responseBody = response.body();
            MediaType contentType = responseBody.contentType();
            charset = contentType.charset(UTF8);
            Buffer buffer = responseBody.source().buffer();
            String json = buffer.clone().readString(charset);
            return response;
        }
    }
}
