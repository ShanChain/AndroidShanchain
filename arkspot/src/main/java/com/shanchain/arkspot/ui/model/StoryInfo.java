package com.shanchain.arkspot.ui.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/8/23.
 */

public class StoryInfo implements MultiItemEntity,Serializable{
    public static final int type1 = 1;  //短故事
    public static final int type2 = 2;  //长文
    public static final int type3 = 3;
    public static final int type4 = 4;

    private String time;

    private int itemType;

    private StoryListDataBean mStoryListDataBean;

    public StoryListDataBean getStoryListDataBean() {
        return mStoryListDataBean;
    }

    public void setStoryListDataBean(StoryListDataBean storyListDataBean) {
        mStoryListDataBean = storyListDataBean;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
