package com.shanchain.data.common.rn.modules;

import android.content.Context;
import android.text.TextUtils;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetDataFormatUtils;
import com.shanchain.common.R;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.NetworkUtils;

import com.shanchain.data.common.net.SCHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by flyye on 2017/10/11.
 */

public class RNNetworkModule extends ReactContextBaseJavaModule {

    private Context mContext;
    private final static String REACT_CLASS = "SCNetwork";

    public RNNetworkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = getReactApplicationContext();
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void fetch(ReadableMap options,final Callback successCallback,final Callback failCallback ){
        if(!NetworkUtils.isNetworkConnected(mContext)){
            failCallback.invoke(NetDataFormatUtils.getNetErrWritableMap(mContext.getString(R.string.request_no_network_connect), NetErrCode.REQUEST_NO_NETWORK,null));
            return;
        }
        if (options != null && options.keySetIterator().toString().length() > 0) {
            String urlPath = options.getString("path");
            String method = options.getString("method");
            ReadableMap params = options.getMap("params");

            if (method != null && method.trim().length() > 0) {
                Map<String, String> endParams = new HashMap<String, String>();
                if (params != null && params.keySetIterator().toString().length() > 0) {
                    ReadableMapKeySetIterator iterator = params.keySetIterator();
                    while (iterator.hasNextKey()) {
                        String key = iterator.nextKey();
                        String value = params.getString(key);
                        endParams.put(key, value);
                    }
                }

                if (method.equalsIgnoreCase("get")) {
                    SCHttpUtils.get()
                            .url(HttpApi.BASE_URL_ARTSPOT + urlPath)
                            .params(endParams)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    e.printStackTrace();
                                    failCallback.invoke(NetDataFormatUtils.getNetErrWritableMap(mContext.getString(R.string.request_no_network_connect), NetErrCode.REQUEST_NO_NETWORK,null));
                                    return;
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if(TextUtils.isEmpty(jsonObject.getString("code"))){
                                            failCallback.invoke(NetDataFormatUtils.getNetErrWritableMap(mContext.getString(R.string.server_err), NetErrCode.REQUEST_NO_NETWORK,null));
                                        }
                                        if(!TextUtils.isEmpty(jsonObject.getString("code")) && jsonObject.getString("code").equalsIgnoreCase("000000")){
                                            successCallback.invoke(NetDataFormatUtils.parseJSON2WritableMap(response));
                                        }else {

                                            failCallback.invoke(NetDataFormatUtils.parseJSON2WritableMap(response));
                                        }

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }


                                }
                            });
                } else if (method.equalsIgnoreCase("post")) {
                    SCHttpUtils.post()
                            .url(HttpApi.BASE_URL_ARTSPOT + urlPath)
                            .params(endParams)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    e.printStackTrace();
                                    failCallback.invoke(NetDataFormatUtils.getNetErrWritableMap(mContext.getString(R.string.request_no_network_connect), NetErrCode.REQUEST_NO_NETWORK,null));
                                    return;
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if(TextUtils.isEmpty(jsonObject.getString("code"))){
                                            failCallback.invoke(NetDataFormatUtils.getNetErrWritableMap(mContext.getString(R.string.server_err), NetErrCode.REQUEST_NO_NETWORK,null));
                                        }
                                        if(!TextUtils.isEmpty(jsonObject.getString("code")) && jsonObject.getString("code").equalsIgnoreCase("000000")){
                                            successCallback.invoke(NetDataFormatUtils.parseJSON2WritableMap(response));
                                        }else {

                                            failCallback.invoke(NetDataFormatUtils.parseJSON2WritableMap(response));
                                        }

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }


                                }
                            });
                } else if (method.equalsIgnoreCase("upload")){
//                    uploadImage(params, successCallback, errorCallback);
                } else {
                    failCallback.invoke(NetDataFormatUtils.getNetErrWritableMap(mContext.getString(R.string.request_wrong_method), NetErrCode.REQUEST_METHOD_ERROR, null));
                }
            } else {
                failCallback.invoke(NetDataFormatUtils.getNetErrWritableMap(mContext.getString(R.string.request_no_method_error),NetErrCode.REQUEST_NO_PARAMS, null));
            }
        }else {
            failCallback.invoke(NetDataFormatUtils.getNetErrWritableMap(mContext.getString(R.string.request_no_param_error), NetErrCode.REQUEST_NO_PARAMS,null));

        }




    }
}