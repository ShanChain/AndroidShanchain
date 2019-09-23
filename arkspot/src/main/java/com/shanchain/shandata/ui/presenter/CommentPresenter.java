package com.shanchain.shandata.ui.presenter;

/**
 * Created by WealChen
 * Date : 2019/9/21
 * Describe :
 */
public interface CommentPresenter {
    void getCommentSecontList(int invitationId,int parentId,int currentPage,int pagesize);
    void addCommentUser(int sendUserId,int toUserId,int invitationId,int parentId,int replyId,String content);
}
