package com.shanchain.shandata.ui.presenter.impl;

import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.ui.presenter.CommentPresenter;
import com.shanchain.shandata.ui.view.activity.article.view.CommentView;

import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/9/21
 * Describe :
 */
public class CommentPresenterImpl implements CommentPresenter {
    private CommentView mCommentView;
    public CommentPresenterImpl(CommentView view){
        this.mCommentView = view;
    }
    @Override
    public void getCommentSecontList(int invitationId, int parentId, int currentPage, int pagesize) {
        SCHttpUtils.getAndToken()
                .url(HttpApi.TITLE_COMMENT_LIST)
                .addParams("invitationId", invitationId+"")
                .addParams("parentId", parentId+"")
                .addParams("currentPage", currentPage+"")
                .addParams("pagesize", pagesize + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mCommentView.setCommentSencondListResponse(response);
                    }
                });
    }

    @Override
    public void addCommentUser(int sendUserId, int toUserId,int invitationId, int parentId, int replyId, String content) {
        SCHttpUtils.getAndToken()
                .url(HttpApi.ADD_COMMENT)
                .addParams("invitationId", invitationId+"")
                .addParams("parentId", parentId+"")
                .addParams("replyId", replyId+"")
                .addParams("content", content+"")
                .addParams("sendUserId", sendUserId + "")
                .addParams("toUserId", toUserId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("TaskPresenterImpl", "添加评论失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mCommentView.addCommentResponse(response);
                    }
                });
    }
}
