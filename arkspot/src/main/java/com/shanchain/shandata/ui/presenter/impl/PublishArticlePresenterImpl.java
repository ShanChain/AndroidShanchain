package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.shandata.ui.model.PhotoBean;
import com.shanchain.shandata.ui.presenter.PublishArticlePresenter;
import com.shanchain.shandata.ui.view.activity.article.view.PublishArticleView;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.request.PostFormRequest;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.jiguang.imui.model.ChatEventMessage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by WealChen
 * Date : 2019/7/25
 * Describe :
 */
public class PublishArticlePresenterImpl implements PublishArticlePresenter {
    private PublishArticleView mPublishArticleView;
    public PublishArticlePresenterImpl(PublishArticleView view){
        this.mPublishArticleView = view;
    }
    @Override
    public void addArticleNoPictrue(int userId, String title,String content,String listImg) {
        mPublishArticleView.showProgressStart();
        SCHttpUtils.postWithUserId()
                .url(HttpApi.PUBLISH_ARTICLE)
                .addParams("title", title)
                .addParams("content", content)
                .addParams("listImg",listImg)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mPublishArticleView.showProgressEnd();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mPublishArticleView.showProgressEnd();
                        mPublishArticleView.addArticleResponse(response);
                    }
                });
    }

    @Override
    public void uploadPhotoListToServer(List<PhotoBean> mList) {
        OkHttpClient mOkHttpClient = OkHttpUtils.getInstance().getOkHttpClient();
        MultipartBody.Builder mbody=new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(PhotoBean p:mList){
            if(new File(p.getUrl()).exists()){
                mbody.addFormDataPart("files",p.getFileName(), RequestBody.create(SCHttpUtils.FORM_DATA,new File(p.getUrl())));
            }
        }
        RequestBody requestBody =mbody.build();
        Request request = new Request.Builder()
                .url(HttpApi.UPLOAD_IMAGE)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("Resonse IOException: ", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("Resonse: ", result);
                mPublishArticleView.setPhotoListSuccess(result);
            }
        });
    }
}
