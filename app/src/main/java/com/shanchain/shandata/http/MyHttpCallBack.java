package com.shanchain.shandata.http;

import com.google.gson.Gson;
import com.shanchain.shandata.utils.LogUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import okhttp3.Response;

/**
 * 网络请求回调接口基础封装
 */

public abstract class MyHttpCallBack<T> extends Callback<T> {

    private Class<T> clazz;

    public MyHttpCallBack(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        String result = response.body().string();
        JSONObject jsonObject = new JSONObject(result);
        String data = jsonObject.optString("data");
        String code = jsonObject.optString("code");
        String msg = jsonObject.optString("message");
        LogUtils.showLog(result);
        if (code.equals(HttpApi.COMMON_SUC_CODE)) {
            return new Gson().fromJson(data, clazz);
        } else if (code.equals(HttpApi.COMMON_ERR_CODE)) {
            LogUtils.e("通用异常");
        } else if (code.equals(HttpApi.SMSVC_ERR_CODE)) {
            LogUtils.e("服务器异常");
        } else if (code.equals(HttpApi.LOGIN_ERR_CODE)) {
            LogUtils.e("账号或密码错误");
        } else if (code.equals(HttpApi.USER_REPEAT_ERR_CODE)) {
            LogUtils.e("账号已存在");
        }
        return null;
    }
}
