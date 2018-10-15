package com.shanchain.shandata.event;

import com.shanchain.shandata.ui.model.StoryModelBean;

public class DynamicCommentEvent {
    private int commentCount;
    private boolean isAdd;
    private StoryModelBean bean;


    public DynamicCommentEvent() {
    }
    public DynamicCommentEvent(int count) {
        this.commentCount = count;
    }

    public StoryModelBean getBean() {
        return bean;
    }

    public void setBean(StoryModelBean bean) {
        this.bean = bean;
    }
    public DynamicCommentEvent(boolean isAdd) {
        this.isAdd = isAdd;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int count) {
        this.commentCount = count;
    }

    public void setAddOrDelete(Boolean isRefresh){
        this.isAdd = isRefresh;
    }

    public Boolean isAdd(){
        return this.isAdd;
    }
}
