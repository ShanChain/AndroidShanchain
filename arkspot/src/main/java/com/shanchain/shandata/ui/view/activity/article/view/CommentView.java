package com.shanchain.shandata.ui.view.activity.article.view;

/**
 * Created by WealChen
 * Date : 2019/9/21
 * Describe :
 */
public interface CommentView {
    void showProgressStart();
    void showProgressEnd();
    void setCommentSencondListResponse(String response);
    void addCommentResponse(String response);
}
