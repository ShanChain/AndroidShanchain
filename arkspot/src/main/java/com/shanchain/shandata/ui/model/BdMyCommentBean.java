package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/10/27.
 */

public class BdMyCommentBean {

    private int storyId;
    private CommentStoryInfo storyInfo;
    private CommentBean mCommentBean;
    private ContactBean mContactBean;

    public CommentStoryInfo getStoryInfo() {
        return storyInfo;
    }

    public void setStoryInfo(CommentStoryInfo storyInfo) {
        this.storyInfo = storyInfo;
    }

    public CommentBean getCommentBean() {
        return mCommentBean;
    }

    public void setCommentBean(CommentBean commentBean) {
        mCommentBean = commentBean;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public ContactBean getContactBean() {
        return mContactBean;
    }

    public void setContactBean(ContactBean contactBean) {
        mContactBean = contactBean;
    }
}
