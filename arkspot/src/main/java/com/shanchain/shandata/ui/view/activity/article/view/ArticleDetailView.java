package com.shanchain.shandata.ui.view.activity.article.view;

/**
 * Created by WealChen
 * Date : 2019/7/25
 * Describe :
 */
public interface ArticleDetailView {
    void showProgressStart();
    void showProgressEnd();
    void setCommentList(String response);
    void addCommentResponse(String response);
    void setAttentionResponse(String response,int type);//0加关注，1删除关注
    void setPraiseResponse(String response,int type);//0点赞，1删除点赞
    void setAttentionCommentResponse(String response,int type);//0加关注，1删除关注
}
