package com.shanchain.arkspot.ui.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/10/18.
 */

public class StoryModelInfo implements Serializable{

    private String storyId;
    private StoryModelBean mBean;
    private ResponseCharacterBrief mCharacterBrief;

    public ResponseCharacterBrief getCharacterBrief() {
        return mCharacterBrief;
    }

    public void setCharacterBrief(ResponseCharacterBrief characterBrief) {
        mCharacterBrief = characterBrief;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public StoryModelBean getBean() {
        return mBean;
    }

    public void setBean(StoryModelBean bean) {
        mBean = bean;
    }

    @Override
    public String toString() {
        return "StoryModelInfo{" +
                "storyId='" + storyId + '\'' +
                ", mBean=" + mBean +
                '}';
    }
}
