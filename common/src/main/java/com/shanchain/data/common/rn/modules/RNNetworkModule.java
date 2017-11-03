package com.shanchain.data.common.rn.modules;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;
import com.shanchain.common.R;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.bean.UpLoadImgBean;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetDataFormatUtils;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.NetworkUtils;
import com.shanchain.data.common.net.SCHttpCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.OssHelper;
import com.shanchain.data.common.utils.SCImageUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        Log.i("fetch",options.toString());
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
                            .url(HttpApi.BASE_URL + urlPath)
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
                            .url(HttpApi.BASE_URL + urlPath)
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
                                    Log.i("fetch_response",response.toString());
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

    @ReactMethod
    public void uploadFile(ReadableMap options , final Callback callback,final Callback errorCallBack){
        ReadableArray filePaths = options.getArray("filePaths");
        Log.i("flyye",filePaths.toString());
        final List<String> files = new ArrayList<>();

        for (int i = 0; i < filePaths.size(); i ++) {
            files.add(filePaths.getString(i));
        }

        SCHttpUtils.post()
                .url(HttpApi.UP_LOAD_FILE)
                .addParams("num",files.size()+"")
                .build()
                .execute(new SCHttpCallBack<UpLoadImgBean>(UpLoadImgBean.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取图片地址失败");
                        e.printStackTrace();
                        errorCallBack.invoke("网络访问");
                    }

                    @Override
                    public void onResponse(UpLoadImgBean response, int id) {
                        try {
                            LogUtils.i("获取图片地址成功 " );
                            String accessKeyId = response.getAccessKeyId();
                            String accessKeySecret = response.getAccessKeySecret();
                            String securityToken = response.getSecurityToken();
                            List<String> uuidList = response.getUuidList();
                            upLoadOss(accessKeyId,accessKeySecret,securityToken,uuidList,files,callback,errorCallBack);
                        } catch (Exception e) {
                            LogUtils.i("获取图片地址失败");
                            e.printStackTrace();
                            errorCallBack.invoke("数据解析错误");
                        }

                    }
                });

    }

    private void upLoadOss(String accessKeyId, String accessKeySecret, String securityToken, List<String> files, final List<String> uuidList, final Callback callback,final Callback errorCallBack) {

        Activity topActivity =
                ActivityStackManager.getInstance().getTopActivity();
        final List<String> uuidListBack = new ArrayList<>(uuidList);

        List<String> compressImages = SCImageUtils.compressImages(topActivity, files);

        final OssHelper helper = new OssHelper(topActivity,accessKeyId,accessKeySecret,securityToken);
        helper.setOnUploadListener(new OssHelper.OnUploadListener() {
            @Override
            public void upLoadSuccess(boolean isSuccess) {
                if (isSuccess){
                    LogUtils.i("文件上传阿里云成功");
                    WritableArray urls = new WritableNativeArray();
                    for (int i = 0; i < uuidListBack.size(); i ++) {
                        String url = uuidListBack.get(i);
                        String imgUrl = helper.getImgUrl(url);
                        urls.pushString(imgUrl);
                    }
                    callback.invoke(urls);

                }else {
                    errorCallBack.invoke("阿里云文件上传失败");
                }
            }
        });
        helper.ossUpload(compressImages,uuidList);
    }

}
