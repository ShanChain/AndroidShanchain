package com.shanchain.data.common.net;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.shanchain.data.common.utils.ToastUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import okhttp3.Response;

public abstract class SCHttpCallBack<T> extends Callback<T> {

    private Class<T> clazz;
    private Context mContext;

    public SCHttpCallBack(Class<T> clazz) {
        this.clazz = clazz;
    }

    public SCHttpCallBack(Class<T> clazz, Context context) {
        this.clazz = clazz;
        this.mContext = context;
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        String result = response.body().string();
        JSONObject jsonObject = new JSONObject(result);
        String data = jsonObject.optString("data");
        String code = jsonObject.optString("code");
        String msg = jsonObject.optString("message");
        Log.d("httpResult_SC", result);
        if (SCHttpApi.COMMON_SUC_CODE.equals(code)) {
            return new Gson().fromJson(data, clazz);
        } else if (SCHttpApi.COMMON_ERR_CODE.equals(code)) {
            Log.e("httpResult_SC", "通用异常");
            if (mContext != null) {
                ToastUtils.showToast(mContext,"未知异常:"+code);
            }
        } else if (SCHttpApi.SMSVC_ERR_CODE.equals(code)) {
            Log.e("httpResult_SC", "服务器异常");
            if (mContext != null) {
                ToastUtils.showToast(mContext,"服务器异常");
            }
        } else if (SCHttpApi.LOGIN_ERR_CODE.equals(code)) {
            Log.e("httpResult_SC", "账号或密码错误");
            if (mContext != null) {
                ToastUtils.showToast(mContext,"账号或密码错误");
            }
        } else if (SCHttpApi.USER_REPEAT_ERR_CODE.equals(code)) {
            Log.e("httpResult_SC", "账号已存在");
            if (mContext != null) {
                ToastUtils.showToast(mContext,"账号已存在");
            }
        }
        return null;
    }


}
