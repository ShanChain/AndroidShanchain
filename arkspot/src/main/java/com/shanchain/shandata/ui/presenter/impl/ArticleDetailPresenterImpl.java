package com.shanchain.shandata.ui.presenter.impl;

import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.ui.presenter.ArticleDetailPresenter;
import com.shanchain.shandata.ui.view.activity.article.view.ArticleDetailView;

import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/7/30
 * Describe :
 */
public class ArticleDetailPresenterImpl implements ArticleDetailPresenter {
    private ArticleDetailView mArticleDetailView;
    public ArticleDetailPresenterImpl(ArticleDetailView view){
        this.mArticleDetailView = view;
    }
    @Override
    public void getAllArticleComment(int invitationId, int currentPage, int pagesize) {
        mArticleDetailView.showProgressStart();
        SCHttpUtils.getAndToken()
                .url(HttpApi.TITLE_COMMENT_LIST)
                .addParams("invitationId", invitationId+"")
                .addParams("currentPage", currentPage+"")
                .addParams("pagesize", pagesize + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("TaskPresenterImpl", "查询任务失败");
                        mArticleDetailView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mArticleDetailView.showProgressEnd();
                        mArticleDetailView.setCommentList(response);
                    }
                });
    }

    @Override
    public void addComment(int invitationId, String content, int sendUserId, int toUserId) {
        mArticleDetailView.showProgressStart();
        SCHttpUtils.getAndToken()
                .url(HttpApi.ADD_COMMENT)
                .addParams("invitationId", invitationId+"")
                .addParams("content", content+"")
                .addParams("sendUserId", sendUserId + "")
                .addParams("toUserId", toUserId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("TaskPresenterImpl", "添加评论失败");
                        mArticleDetailView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mArticleDetailView.showProgressEnd();
                        mArticleDetailView.addCommentResponse(response);
                    }
                });
    }
}
