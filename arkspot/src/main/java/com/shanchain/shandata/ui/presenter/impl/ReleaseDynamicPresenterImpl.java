package com.shanchain.shandata.ui.presenter.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpCallBack;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.push.PushFilterBuilder;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.OssHelper;
import com.shanchain.data.common.utils.SCImageUtils;
import com.shanchain.data.common.utils.SCUploadImgHelper;
import com.shanchain.shandata.ui.model.ReleaseContentInfo;
import com.shanchain.shandata.ui.model.ReleaseStoryContentInfo;
import com.shanchain.shandata.ui.model.RichTextModel;
import com.shanchain.shandata.ui.model.UpLoadImgBean;
import com.shanchain.shandata.ui.presenter.ReleaseDynamicPresenter;
import com.shanchain.shandata.ui.view.activity.story.stroyView.ReleaseDynamicView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/10/17.
 */

public class ReleaseDynamicPresenterImpl implements ReleaseDynamicPresenter {

    private ReleaseDynamicView mReleaseDynamicView;
    private List<RichTextModel> mImgModels;
    private List<RichTextModel> mData;

    public ReleaseDynamicPresenterImpl(ReleaseDynamicView releaseDynamicView) {
        mReleaseDynamicView = releaseDynamicView;
    }

    @Override
    public void releaseDynamic(String word, List<String> imgUrls, String tailId, List<Integer> atList, List<Integer> topicIds) {

        //{"content":"","imgs":["",""]}
        ReleaseContentInfo releaseContentInfo = new ReleaseContentInfo();
        releaseContentInfo.setContent(word);

        releaseContentInfo.setImgs(imgUrls);

        String content = new Gson().toJson(releaseContentInfo);

        ReleaseStoryContentInfo contentInfo = new ReleaseStoryContentInfo();
        contentInfo.setIntro(content);
        contentInfo.setTailId(tailId);
        Gson gson = new Gson();
        String dataString = gson.toJson(contentInfo);
        String topicArr = gson.toJson(topicIds);
        String referedModel = gson.toJson(atList);
        JSONArray jsonArray = new JSONArray();
        for (Integer val:atList) {
            JSONObject tagJson = new JSONObject();
            tagJson.put("tag","MODEL_"+ val);
            jsonArray.add(tagJson);
        }
        PushFilterBuilder builder = new PushFilterBuilder();
        if(jsonArray.size() > 0){
            builder.addOrFilter(jsonArray);
        }
        SCHttpUtils.postWithSpaceAndChaId()
                .url(HttpApi.STORY_ADD)
                .addParams("dataString", dataString)
                .addParams("topicIds", topicArr)
                .addParams("type", Constants.TYPE_STORY_SHORT + "")
                .addParams("filter",builder.getFilter())
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("发布动态失败");
                        e.printStackTrace();
                        mReleaseDynamicView.releaseFailed("发布动态失败", e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("发布故事返回的结果 = " + response);
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            mReleaseDynamicView.releaseSuccess();
                        } else {
                            Exception e = new Exception("返回码异常");
                            mReleaseDynamicView.releaseFailed("返回码异常", e);
                        }

                    }
                });
    }

    @Override
    public void upLoadImgs(final Context context, final String word, final List<String> imgPaths, final String tailId, final List<Integer> atList, final List<Integer> topics) {

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

                                    for (int i = 0; i < saveUrls.size(); i++) {
                                        String url = saveUrls.get(i);
                                        String imgUrl = ossHelper.getImgUrl(url);
                                        urls.add(imgUrl);
                                    }

                                    LogUtils.d("图片urls = " + urls.toString());

                                    releaseDynamic(word, urls, tailId, atList, topics);
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

    @Override
    public void ReleaseLongText(Context context, final String title, List<RichTextModel> editData) {
        mImgModels = new ArrayList<>();
        mData = new ArrayList<>(editData);
        RichTextModel titleModel = new RichTextModel();
        titleModel.setImg(false);
        titleModel.setText(title);
        mData.add(0,titleModel);

        List<String> imgPaths = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            RichTextModel model = mData.get(i);
            model.setIndex(i);
            if (model.isImg()) {
                mImgModels.add(model);
                imgPaths.add(model.getImgPath());
            }
        }

        if (imgPaths.size() == 0) {
            publishData(title);
        } else {
            SCUploadImgHelper helper = new SCUploadImgHelper();
            helper.setUploadListener(new SCUploadImgHelper.UploadListener() {
                @Override
                public void onUploadSuc(List<String> urls) {
                    for (int i = 0; i < urls.size(); i++) {
                        RichTextModel model = mImgModels.get(i);
                        String url = urls.get(i);
                        model.setImgPath(url);
                    }
                    publishData(title);

                }

                @Override
                public void error() {
                    mReleaseDynamicView.releaseFailed("上传阿里云失败", new Exception("阿里云失败"));
                }
            });
            helper.upLoadImg(context, imgPaths);
        }

    }

    private void publishData(String title) {

        List<String> contents = new ArrayList<>();


        for (int i = 0; i < mData.size(); i++) {
            RichTextModel model = mData.get(i);
            for (RichTextModel imgModel : mImgModels) {
                if (model.getIndex() == imgModel.getIndex()) {
                    model.setImgPath(imgModel.getImgPath());
                }
            }

            if (!model.isImg()) {
                if (i != mData.size() - 1) {
                    contents.add(model.getText());
                }
            }
        }
        StringBuilder intro = new StringBuilder();
        for (int i = 0; i < contents.size(); i++) {
            if ( i == 0 ) {
                continue;
            }
            if (i == contents.size() - 1) {
                intro.append(contents.get(i));
            } else {
                intro.append(contents.get(i) + "\n");
            }

        }

        String content = JSONObject.toJSONString(mData);

        LogUtils.i("小说内容 = " + content);

        ReleaseStoryContentInfo contentInfo = new ReleaseStoryContentInfo();
        contentInfo.setContent(content);
        contentInfo.setIntro(intro.toString());
        contentInfo.setTitle(title);
        contentInfo.setTailId("");

        String dataString = JSONObject.toJSONString(contentInfo);
        SCHttpUtils.postWithSpaceAndChaId()
                .url(HttpApi.STORY_ADD)
                .addParams("dataString", dataString)
                .addParams("type", Constants.TYPE_STORY_LONG + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("发布小说失败");
                        e.printStackTrace();
                        mReleaseDynamicView.releaseFailed("发布小说失败", e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("发布小说成功 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                mReleaseDynamicView.releaseSuccess();
                            } else {
                                mReleaseDynamicView.releaseFailed("发布小说失败", new Exception("发布小说返回码异常"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mReleaseDynamicView.releaseFailed("发布小说失败", e);
                        }
                    }
                });

    }


    /**
     * 描述： 压缩图片
     */
    private List<String> compressImages(Context context, List<String> imgPaths) {
        List<String> compressImgPaths = new ArrayList<>();

        for (int i = 0; i < imgPaths.size(); i++) {

            String imgPath = imgPaths.get(i);
            File file = new File(imgPath);
            long length = file.length();
            LogUtils.d(i + "压缩前图片大小 = " + length);

            Bitmap bitmap = SCImageUtils.getimage(imgPath);

            File filesDir = context.getFilesDir();
            long curTime = System.currentTimeMillis();
            String path = filesDir.getPath() + curTime + i;
            try {
                SCImageUtils.storeImage(bitmap, path);
                compressImgPaths.add(path);
                File file1 = new File(path);
                LogUtils.d(i + "压缩后图片大小 = " + file1.length());
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.d("压缩图片错误");
            }

        }
        return compressImgPaths;

    }

}
