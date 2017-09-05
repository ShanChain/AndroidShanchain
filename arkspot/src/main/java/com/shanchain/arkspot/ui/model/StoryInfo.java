package com.shanchain.arkspot.ui.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by zhoujian on 2017/8/23.
 */

public class StoryInfo implements MultiItemEntity{
    public static final int type1 = 1;
    public static final int type2 = 2;
    public static final int type3 = 3;
    public static final int type4 = 4;

    private String time;

    private int itemType;

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
