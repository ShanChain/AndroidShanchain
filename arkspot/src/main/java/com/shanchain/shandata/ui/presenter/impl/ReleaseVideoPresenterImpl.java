package com.shanchain.shandata.ui.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadClient;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.SvideoInfo;
import com.alibaba.sdk.android.vod.upload.session.VodHttpClientConfig;
import com.alibaba.sdk.android.vod.upload.session.VodSessionCreateInfo;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.shandata.ui.presenter.ReleaseVideoPresenter;
import com.shanchain.shandata.ui.view.activity.story.stroyView.ReleaseVideoView;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/12/21.
 */

public class ReleaseVideoPresenterImpl implements ReleaseVideoPresenter {

    private ReleaseVideoView mView;
    private VODSVideoUploadClient mVodsVideoUploadClient;

    public ReleaseVideoPresenterImpl(ReleaseVideoView view) {
        mView = view;
    }

    @Override
    public void releaseVideoDynamic(Context context, final String videoTitle, final String videoDes, final String videoPath, final String imgPath, String type) {
        mVodsVideoUploadClient = new VODSVideoUploadClientImpl(context);
        mVodsVideoUploadClient.init();
        SCHttpUtils.post()
                .url(HttpApi.OSS_VIDEO_UPLOAD)
                .addParams("type", type)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("获取上传凭证失败");
                        e.printStackTrace();
                        mView.error("获取上传凭证失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到视频上传凭证 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                uploadVideo(videoTitle, videoDes, videoPath, imgPath, data);
                            } else {
                                mView.error("获取上传凭证返回码错误 code = " + code);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.error("数据解析异常");
                        }
                    }
                });


        VodHttpClientConfig vodHttpClientConfig = new VodHttpClientConfig.Builder()
                .setMaxRetryCount(2)//重试次数
                .setConnectionTimeout(30 * 1000)//连接超时
                .setSocketTimeout(30 * 1000)//socket超时
                .build();
        SvideoInfo svideoInfo = new SvideoInfo();
        svideoInfo.setTitle(videoTitle);
        svideoInfo.setDesc(videoDes);
        //svideoInfo.setCateId();
    }

    private void uploadVideo(final String videoTitle, final String videoDes, String videoPath, String imgPath, String data) {
        final String accessKeyId = SCJsonUtils.parseString(data, "accessKeyId");
        final String accessKeySecret = SCJsonUtils.parseString(data, "accessKeySecret");
        String cateId = SCJsonUtils.parseString(data, "cateId");
        final String securityToken = SCJsonUtils.parseString(data, "securityToken");
        final String expiration = SCJsonUtils.parseString(data, "expiration");
        String requestId = SCJsonUtils.parseString(data, "requestId");

        VodHttpClientConfig vodHttpClientConfig = new VodHttpClientConfig.Builder()
                .setMaxRetryCount(2)//重试次数
                .setConnectionTimeout(30 * 1000)//连接超时
                .setSocketTimeout(30 * 1000)//socket超时
                .build();
        SvideoInfo svideoInfo = new SvideoInfo();
        svideoInfo.setTitle(videoTitle);
        svideoInfo.setDesc(videoDes);
        svideoInfo.setCateId(Integer.parseInt(cateId));

        VodSessionCreateInfo vodSessionCreateInfo = new VodSessionCreateInfo.Builder()
                .setVideoPath(videoPath)
                .setImagePath(imgPath)
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setSecurityToken(securityToken)
                .setExpriedTime(expiration)
                .setRequestID(requestId)
                .setIsTranscode(true)
                .setSvideoInfo(svideoInfo)
                .setVodHttpClientConfig(vodHttpClientConfig).build();

        mVodsVideoUploadClient.uploadWithVideoAndImg(vodSessionCreateInfo, new VODSVideoUploadCallback() {
            @Override
            public void onUploadSucceed(String videoId, String imageUrl) {
                //上传成功
                LogUtils.i("视频上传成功 vid = " + videoId + "; imageUrl = " + imageUrl);
                releaseDynamic(videoTitle, videoDes, videoId, imageUrl);
            }

            @Override
            public void onUploadFailed(String code, String message) {
                LogUtils.i("视频上传失败 : code = " + code + "; message = " + message);
                mView.error("阿里云上传视频失败 code = " + code + "; message = " + message);
            }

            @Override
            public void onUploadProgress(long uploadedSize, long totalSize) {
                int progress = (int) (uploadedSize * 100 / totalSize);
                LogUtils.i("视频上传进度 = " + progress + "%");
            }

            @Override
            public void onSTSTokenExpried() {
                LogUtils.i("token过期需刷新token");
                mVodsVideoUploadClient.refreshSTSToken(accessKeyId, accessKeySecret, securityToken, expiration);
            }

            @Override
            public void onUploadRetry(String code, String message) {
                LogUtils.i("视频上传重试 : code = " + code + "; message = " + message);
            }

            @Override
            public void onUploadRetryResume() {
                LogUtils.i("视频上传重试成功");
            }
        });


    }

    private void releaseDynamic(final String videoTitle, final String videoDes, final String videoId, final String imageUrl) {
        JSONObject object = new JSONObject();
        object.put("title", videoTitle);
        object.put("intro", videoDes);
        object.put("background", imageUrl);
        object.put("vidId", videoId);
        String dataString = object.toJSONString();

        SCHttpUtils.postWithUidSpaceIdAndCharId()
                .url(HttpApi.PLAY_ADD)
                .addParams("dataString", dataString)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("发布演绎动态失败");
                        e.printStackTrace();
                        mView.error("发布演绎动态失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("发布演绎动态成功 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                if (TextUtils.isEmpty(data)) {
                                    LogUtils.i("发布演绎失败");
                                    mView.error("发布演绎失败");
                                } else {
                                    LogUtils.i("发布演绎成功");
                                    mView.suc(data);
                                }
                            } else if (TextUtils.equals(code, NetErrCode.VOD_UNCOMPLETED_TRANSCODING)) {
                                //视频转码中。。。
                                LogUtils.i("转码中");
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            sleep(1000);
                                            LogUtils.i("睡1秒");
                                            releaseDynamic(videoTitle, videoDes, videoId, imageUrl);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                            } else {
                                mView.error("发布演绎返回码错误 code = " + code);
                            }
                        } catch (Exception e) {
                            LogUtils.e("发布演绎动态异常");
                            e.printStackTrace();
                            mView.error("发布演绎数据解析异常");
                        }
                    }
                });
    }


}
