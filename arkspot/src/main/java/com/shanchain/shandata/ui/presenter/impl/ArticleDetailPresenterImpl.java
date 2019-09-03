package com.shanchain.shandata.ui.presenter.impl;

import com.shanchain.data.common.cache.SCCacheUtils;
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
    public void getAllArticleComment(int userId,int invitationId, int currentPage, int pagesize) {
        mArticleDetailView.showProgressStart();
        SCHttpUtils.getAndToken()
                .url(HttpApi.TITLE_COMMENT_LIST)
                .addParams("userId", userId+"")
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

    @Override
    public void attentionUser(int userId, int attentionUserId) {
        mArticleDetailView.showProgressStart();
        SCHttpUtils.post()
                .url(HttpApi.ADD_ATTENTION)
                .addParams("userId", userId + "")
                .addParams("attentionUserId", attentionUserId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mArticleDetailView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mArticleDetailView.showProgressEnd();
                        mArticleDetailView.setAttentionResponse(response,0);
                    }
                });
    }

    @Override
    public void deleteAttentionUser(int userId, int attentionUserId) {
        mArticleDetailView.showProgressStart();
        SCHttpUtils.post()
                .url(HttpApi.DELETE_ATTENTION)
                .addParams("userId", userId + "")
                .addParams("attentionUserId", attentionUserId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mArticleDetailView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mArticleDetailView.showProgressEnd();
                        mArticleDetailView.setAttentionResponse(response,1);
                    }
                });
    }

    @Override
    public void addPraiseToArticle(int userId, int invitationId) {
        mArticleDetailView.showProgressStart();
        SCHttpUtils.post()
                .url(HttpApi.ADD_PRAISE_TITLE)
                .addParams("userId", userId + "")
                .addParams("invitationId", invitationId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mArticleDetailView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mArticleDetailView.showProgressEnd();
                        mArticleDetailView.setPraiseResponse(response,0);
                    }
                });
    }

    @Override
    public void deletePraiseToArticle(int userId, int invitationId) {
        mArticleDetailView.showProgressStart();
        SCHttpUtils.post()
                .url(HttpApi.DELETE_PRAISE_TITLE)
                .addParams("userId", userId + "")
                .addParams("invitationId", invitationId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mArticleDetailView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mArticleDetailView.showProgressEnd();
                        mArticleDetailView.setPraiseResponse(response,1);
                    }
                });
    }

    @Override
    public void addAttentToCommentUser(int userId, int attentionUserId) {
        SCHttpUtils.post()
                .url(HttpApi.ADD_ATTENTION)
                .addParams("userId", userId + "")
                .addParams("attentionUserId", attentionUserId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mArticleDetailView.setAttentionCommentResponse(response,0);
                    }
                });
    }

    @Override
    public void deleteAttentionCommentUser(int userId, int attentionUserId) {
        SCHttpUtils.post()
                .url(HttpApi.DELETE_ATTENTION)
                .addParams("userId", userId + "")
                .addParams("attentionUserId", attentionUserId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mArticleDetailView.setAttentionCommentResponse(response,1);
                    }
                });
    }

    @Override
    public void queryArticleDetail(String articleId) {
        SCHttpUtils.post()
                .url(HttpApi.QUERY_DETAIL_TITLE)
                .addParams("id",articleId)
                .addParams("userId", SCCacheUtils.getCacheUserId())
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mArticleDetailView.setQueryArticleDetailResponse(response);
                    }
                });

    }

    @Override
    public void deleteComment(String commentId) {
        SCHttpUtils.post()
                .url(HttpApi.DELETE_COMMENT_ARTICLE)
                .addParams("id", commentId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mArticleDetailView.setDeleteCommentResponse(response);
                    }
                });
    }

    @Override
    public void deleteEssay(String commentId) {
        SCHttpUtils.post()
                .url(HttpApi.DELETE_COMMENT_ESSAY)
                .addParams("id", commentId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mArticleDetailView.setDeleteEssayResponse(response);
                    }
                });
    }
}
