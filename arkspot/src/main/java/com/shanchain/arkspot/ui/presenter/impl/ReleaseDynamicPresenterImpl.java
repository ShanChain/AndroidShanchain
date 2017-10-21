package com.shanchain.arkspot.ui.presenter.impl;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.shanchain.arkspot.global.Constants;
import com.shanchain.arkspot.ui.model.ReleaseContentInfo;
import com.shanchain.arkspot.ui.model.ReleaseStoryContentInfo;
import com.shanchain.arkspot.ui.model.UpLoadImgBean;
import com.shanchain.arkspot.ui.presenter.ReleaseDynamicPresenter;
import com.shanchain.arkspot.ui.view.activity.story.stroyView.ReleaseDynamicView;
import com.shanchain.arkspot.utils.OssHelper;
import com.shanchain.arkspot.utils.SCImageUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/10/17.
 */

public class ReleaseDynamicPresenterImpl implements ReleaseDynamicPresenter {

    private ReleaseDynamicView mReleaseDynamicView;

    public ReleaseDynamicPresenterImpl(ReleaseDynamicView releaseDynamicView) {
        mReleaseDynamicView = releaseDynamicView;
    }

    @Override
    public void releaseDynamic(String word, List<String> imgUrls, String title, String tailId, List<Integer> topicIds, int type) {

        //{"content":"","imgs":["",""]}
        ReleaseContentInfo releaseContentInfo = new ReleaseContentInfo();
        releaseContentInfo.setContent(word);

        releaseContentInfo.setImgs(imgUrls);

        String content = new Gson().toJson(releaseContentInfo);

        ReleaseStoryContentInfo contentInfo = new ReleaseStoryContentInfo();
        if (type == Constants.TYPE_STORY_SHORT) {
            contentInfo.setIntro(content);
            contentInfo.setTitle("");
            contentInfo.setContent("");
        } else if (type == Constants.TYPE_STORY_LONG) {
            contentInfo.setContent(content);
            contentInfo.setIntro("");
            contentInfo.setTitle(title);
        }
        contentInfo.setTailId(tailId);
        Gson gson = new Gson();
        String dataString = gson.toJson(contentInfo);
        String topicArr = gson.toJson(topicIds);
        SCHttpUtils.postWhitSpaceAndChaId()
                .url(HttpApi.STORY_ADD)
                .addParams("dataString", dataString)
                .addParams("topicIds", topicArr)
                .addParams("type", type + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("发布动态失败");
                        e.printStackTrace();
                        mReleaseDynamicView.releaseFailed("发布动态失败", e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("发布故事返回的结果 = " + response);
                        mReleaseDynamicView.releaseSuccess();
                    }
                });
    }

    @Override
    public void upLoadImgs(final Context context, final String word, final List<String> imgPaths, final String title, final String tailId, final List<Integer> topics, final int type) {

        final List<String> compressImages = compressImages(context, imgPaths);

        SCHttpUtils.post()
                .url(HttpApi.UP_LOAD_FILE)
                .addParams("num", compressImages.size() + "")
                .build()
                .execute(new SCHttpCallBack<UpLoadImgBean>(UpLoadImgBean.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("获取图片地址失败");
                        e.printStackTrace();
                        mReleaseDynamicView.releaseFailed("获取图片失败", e);
                    }

                    @Override
                    public void onResponse(UpLoadImgBean response, int id) {
                        if (response == null) {
                            LogUtils.d("返回的图片信息为空");
                            Exception e = new Exception("返回的图片信息为空");
                            mReleaseDynamicView.releaseFailed("返回的图片信息为空", e);
                            return;
                        }

                        final List<String> imgUrls = response.getUuidList();
                        final List<String> saveUrls = new ArrayList<>();
                        saveUrls.addAll(imgUrls);
                        String accessKeyId = response.getAccessKeyId();
                        String accessKeySecret = response.getAccessKeySecret();
                        String securityToken = response.getSecurityToken();
                        LogUtils.d("图片名字集合 = " + imgUrls.toString());
                        final OssHelper ossHelper = new OssHelper(context, accessKeyId, accessKeySecret, securityToken);
                        ossHelper.setOnUploadListener(new OssHelper.OnUploadListener() {
                            @Override
                            public void upLoadSuccess(boolean isSuccess) {
                                if (isSuccess) {
                                    LogUtils.d("上传阿里云成功");
                                    List<String> urls = new ArrayList<>();

                                    for (int i = 0; i < saveUrls.size(); i ++) {
                                        String url = saveUrls.get(i);
                                        String imgUrl = ossHelper.getImgUrl(url);
                                        urls.add(imgUrl);
                                    }

                                    LogUtils.d("图片urls = "+urls.toString());

                                    releaseDynamic(word, urls, title, tailId, topics, type);
                                } else {
                                    LogUtils.d("上传阿里云失败");
                                    Exception e = new Exception("上传阿里云失败");
                                    mReleaseDynamicView.releaseFailed("", e);
                                }
                            }
                        });

                        ossHelper.ossUpload(compressImages, imgUrls);

                    }
                });

    }

    /**
     *  描述： 压缩图片
     */
    private List<String> compressImages(Context context, List<String> imgPaths)  {
        List<String> compressImgPaths = new ArrayList<>();

        for (int i = 0; i < imgPaths.size(); i ++) {

            String imgPath = imgPaths.get(i);
            File file = new File(imgPath);
            long length = file.length();
            LogUtils.d(i + "压缩前图片大小 = " + length);

            Bitmap bitmap = SCImageUtils.getimage(imgPath);

            File filesDir = context.getFilesDir();
            long curTime = System.currentTimeMillis();
            String path = filesDir.getPath() + curTime + i;
            try {
                SCImageUtils.storeImage(bitmap,path);
                compressImgPaths.add(path);
                File file1 = new File(path);
                LogUtils.d(i + "压缩后图片大小 = " + file1.length());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                LogUtils.d("压缩图片错误");
            }

        }
        return compressImgPaths;

    }

}
