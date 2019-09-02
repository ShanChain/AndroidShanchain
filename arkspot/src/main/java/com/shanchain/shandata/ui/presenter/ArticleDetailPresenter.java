package com.shanchain.shandata.ui.presenter;

/**
 * Created by WealChen
 * Date : 2019/7/30
 * Describe :
 */
public interface ArticleDetailPresenter {
    void getAllArticleComment(int userId,int invitationId,int currentPage,int pagesize);
    void addComment(int invitationId, String content,int sendUserId,int toUserId);
    void attentionUser(int userId,int attentionUserId);
    void deleteAttentionUser(int userId,int attentionUserId);
    void addPraiseToArticle(int userId,int invitationId);
    void deletePraiseToArticle(int userId,int invitationId);
    void addAttentToCommentUser(int userId,int attentionUserId);
    void deleteAttentionCommentUser(int userId,int attentionUserId);
    void queryArticleDetail(String articleId);
    void deleteComment(String commentId);
}
