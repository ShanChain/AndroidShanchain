package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/8/25.
 */

public class StoryTagInfo {
    private boolean isSelected;
    private String tag;

    private TagContentBean tagBean;

    public TagContentBean getTagBean() {
        return tagBean;
    }

    public void setTagBean(TagContentBean tagBean) {
        this.tagBean = tagBean;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
