package com.shanchain.shandata.ui.presenter;

/**
 * Created by WealChen
 * Date : 2019/7/23
 * Describe :
 */
public interface SquarePresenter {
    void getListData(String title,String userId,int page,int size,int pullType);
    void attentionUser(int userId,int attentionUserId);
    void deleteAttentionUser(int userId,int attentionUserId);
    void addPraiseToArticle(int userId,int invitationId);
    void deletePraiseToArticle(int userId,int invitationId);
    void getUserHxid(String userId);
}
