package com.shanchain.shandata.base.api;

import android.content.Context;

import io.reactivex.functions.Function;

/**
 * 响应结果的统一处理
 * Created by xiongyikai on 2017/2/16.
 */

public class HttpResultFunc<T> implements Function<HttpResult<T>, T> {

    private static final String TAG = HttpResultFunc.class.getSimpleName();

    private static final int CODE_NEED_LOGIN = 1003;
    private static final int CODE_ERROR = 1002;

    private Context mContext;

    public HttpResultFunc(Context context){
        mContext = context;
    }

    @Override
    public T apply(HttpResult<T> tHttpResult) throws Exception {
        if (tHttpResult.resultCode == CODE_NEED_LOGIN){
            throw new ApiException(ApiException.ERROR_CODE_NOLOGIN , "not login");
        }else if (tHttpResult.resultCode == 101){
            return tHttpResult.data;
        }else{
            throw new ApiException(ApiException.ERROR_FAIL , tHttpResult.getMsg());
        }
    }

}
