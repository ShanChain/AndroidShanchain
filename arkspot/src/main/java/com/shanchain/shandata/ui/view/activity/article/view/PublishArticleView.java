package com.shanchain.shandata.ui.view.activity.article.view;

/**
 * Created by WealChen
 * Date : 2019/7/25
 * Describe :
 */
public interface PublishArticleView {
    void showProgressStart();
    void showProgressEnd();
    void addArticleResponse(String response);
    void setPhotoListSuccess(String response);

}
