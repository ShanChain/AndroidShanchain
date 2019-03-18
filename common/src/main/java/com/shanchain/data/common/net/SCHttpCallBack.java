package com.shanchain.data.common.net;

import android.util.Log;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import okhttp3.Response;

public abstract class SCHttpCallBack<T> extends Callback<T>{

    private Class<T> clazz;

    public SCHttpCallBack(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        String result = response.body().string();
        JSONObject jsonObject = new JSONObject(result);
        String data = jsonObject.optString("data");
        String code = jsonObject.optString("code");
        String msg = jsonObject.optString("message");
        Log.d("httpResult_SC",result);
        if (SCHttpApi.COMMON_SUC_CODE.equals(code)) {
            return new Gson().fromJson(data, clazz);
        } else if (SCHttpApi.COMMON_ERR_CODE.equals(code)) {
            Log.e("httpResult_SC","通用异常");
        } else if (SCHttpApi.SMSVC_ERR_CODE.equals(code)) {
            Log.e("httpResult_SC","服务器异常");
        } else if (SCHttpApi.LOGIN_ERR_CODE.equals(code)) {
            Log.e("httpResult_SC","账号或密码错误");
        } else if (SCHttpApi.USER_REPEAT_ERR_CODE.equals(code)) {
            Log.e("httpResult_SC","账号已存在");
        }
        return null;
    }



}
