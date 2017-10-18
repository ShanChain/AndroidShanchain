package com.shanchain.arkspot.ui.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by zhoujian on 2017/10/18.
 */

public class StoryBeanModel implements MultiItemEntity {

    public static final int type1 = 1;  //短故事
    public static final int type2 = 2;  //长文
    public static final int type3 = 3;
    public static final int type4 = 4;

    private int itemType;
    private StoryModel storyModel;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public StoryModel getStoryModel() {
        return storyModel;
    }

    public void setStoryModel(StoryModel storyModel) {
        this.storyModel = storyModel;
    }
}
