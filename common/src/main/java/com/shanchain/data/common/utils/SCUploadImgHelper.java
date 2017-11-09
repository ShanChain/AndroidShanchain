package com.shanchain.data.common.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/11/6.
 */

public class SCUploadImgHelper {

    public void upLoadImg(final Context context,final List<String> srcPaths){
        int size = srcPaths.size();
        SCHttpUtils.post()
                .url(HttpApi.UP_LOAD_FILE)
                .addParams("num",size+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取图片地址失败 onError");
                        e.printStackTrace();
                        mListener.error();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                                String dataStr = JSONObject.parseObject(response).getString("data");
                                String accessKeyId = JSONObject.parseObject(dataStr).getString("accessKeyId");
                                String accessKeySecret = JSONObject.parseObject(dataStr).getString("accessKeySecret");
                                String securityToken = JSONObject.parseObject(dataStr).getString("securityToken");
                                String arr = JSONObject.parseObject(dataStr).getString("uuidList");
                                List<String> uuidList = JSONObject.parseArray(arr, String.class);
                                LogUtils.i("accessKeyId = " + accessKeyId + ";\n accessKeySecret = " + accessKeySecret + ";\n securityToken = "+securityToken
                                        + ";\n uuidList " + uuidList);
                                upLoadOss(context,srcPaths,accessKeyId,accessKeySecret,securityToken,uuidList);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.i("获取图片地址数据失败 onResponse");
                            mListener.error();
                        }

                    }
                });

    }

    private void upLoadOss(Context context, List<String> srcPaths, String accessKeyId, String accessKeySecret, String securityToken, List<String> uuidList) {
        final OssHelper helper = new OssHelper(context,accessKeyId,accessKeySecret,securityToken);

        final List<String> tempUrls = new ArrayList<>(uuidList);

        helper.setOnUploadListener(new OssHelper.OnUploadListener() {
            @Override
            public void upLoadSuccess(boolean isSuccess) {
                if (isSuccess){
                    LogUtils.i("阿里云上传成功");
                    ArrayList<String> urls = new ArrayList<>();
                    for (int i = 0; i < tempUrls.size(); i ++) {
                        String tempUrl = tempUrls.get(i);
                        String imgUrl = helper.getImgUrl(tempUrl);
                        urls.add(imgUrl);
                    }
                    mListener.onUploadSuc(urls);
                }else {
                    LogUtils.i("阿里云上传失败");
                    mListener.error();
                }
            }
        });

        List<String> compressImages = SCImageUtils.compressImages(context, srcPaths);

        helper.ossUpload(compressImages,uuidList);
    }


    public interface UploadListener{
        void onUploadSuc(List<String> urls);
        void error();
    }

    private UploadListener mListener;

    public void setUploadListener(UploadListener listener){
        this.mListener = listener;
    }

}
