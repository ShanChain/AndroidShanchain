package com.shanchain.data.common.utils;

import android.content.Context;

import com.shanchain.data.common.bean.UpLoadImgBean;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpCallBack;
import com.shanchain.data.common.net.SCHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/11/6.
 */

public class SCUploadImgHelper {

    public void upLoadImg(final Context context,int num, final List<String> srcPaths){

        SCHttpUtils.post()
                .url(HttpApi.UP_LOAD_FILE)
                .addParams("num",num+"")
                .build()
                .execute(new SCHttpCallBack<UpLoadImgBean>(UpLoadImgBean.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取图片地址失败");
                        e.printStackTrace();
                        mListener.error();
                    }

                    @Override
                    public void onResponse(UpLoadImgBean upLoadImgBean, int id) {
                        try {
                            String accessKeyId = upLoadImgBean.getAccessKeyId();
                            String accessKeySecret = upLoadImgBean.getAccessKeySecret();
                            String securityToken = upLoadImgBean.getSecurityToken();
                            List<String> uuidList = upLoadImgBean.getUuidList();
                            upLoadOss(context,srcPaths,accessKeyId,accessKeySecret,securityToken,uuidList);
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.i("获取图片地址数据失败");
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
