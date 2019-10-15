package com.shanchain.shandata.base.api;


import android.content.Context;
import android.text.TextUtils;

import com.jkyeo.basicparamsinterceptor.BasicParamsInterceptor;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.shandata.base.MyApplication;
import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by 胡茂柜 on 2017/2/16.
 */

public class ApiBase {
    public static final int TIP_REQUEST_FAIL_CODE = 0x91;

    public class ErrorCode{
        public static final int SUCCESS = 101;
        public static final int CODE_FAIL = 1002;
        public static final int CODE_LOGIN_FAIL = 400;
    }

    private ApiBase(){}

    private static class SingletonHolder{
        private static final ApiBase INSTANCE = new ApiBase();
    }

    public static ApiBase getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private static final String API_BASE_URL = "http://vlog.vkankr.com/vlog-rest/";
    public static final String API_APKPATH_URL = "http://vlog.vkankr.com/apkfiles/";

    private Retrofit mRetrofit;


    /**
     * Api的初始化，需要在Application.onCreate()方法中调用
     * 登录之后需要重新调用一次，更新用户相关的公共参数
     */
    public void init(Context context){
        mRetrofit = RequestBase.getInstance().getRetrofitInstance(MyApplication.getInstance() ,
                API_BASE_URL , createParamInterceptor() , new ResponseInterceptor(context));

    }

    //携带token的请求
    private BasicParamsInterceptor createParamInterceptor(){
        if (TextUtils.isEmpty(SCCacheUtils.getCacheToken())){
            return new BasicParamsInterceptor.Builder().build();
        }
        return new BasicParamsInterceptor.Builder()
                .addHeaderParam("token",SCCacheUtils.getCacheToken())
                .build();
    }


    /**
     * 请求都用这个方法
     * @param observable
     * @param <T>
     */
    public <T> Observable<T> toRequest(Observable<HttpResult<T>> observable){
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new HttpResultFunc<T>(MyApplication.getInstance()));
    }

    /**
     * 需要根据生命周期取消请求的用这个方法
     * @param observable
     * @param lifecycleTransformer
     * @param <T>
     * @return
     */
    public <T> Observable<T> toRequest(Observable<HttpResult<T>> observable , LifecycleTransformer lifecycleTransformer){
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer)
                .map(new HttpResultFunc<T>(MyApplication.getInstance()));
    }

}
