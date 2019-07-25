package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.ui.presenter.PublishArticlePresenter;
import com.shanchain.shandata.ui.view.activity.article.view.PublishArticleView;

import cn.jiguang.imui.model.ChatEventMessage;
import okhttp3.Call;

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
    public void addArticleNoPictrue(int userId, String title, String content, String[] photos) {
        mPublishArticleView.showProgressStart();
        SCHttpUtils.postWithUserId()
                .url(HttpApi.PUBLISH_ARTICLE)
                .addParams("title", title)
                .addParams("content", content)
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
}
