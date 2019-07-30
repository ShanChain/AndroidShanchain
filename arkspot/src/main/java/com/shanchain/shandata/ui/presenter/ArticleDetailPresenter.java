package com.shanchain.shandata.ui.presenter;

/**
 * Created by WealChen
 * Date : 2019/7/30
 * Describe :
 */
public interface ArticleDetailPresenter {
    void getAllArticleComment(int invitationId,int currentPage,int pagesize);
    void addComment(int invitationId, String content,int sendUserId,int toUserId);
}
