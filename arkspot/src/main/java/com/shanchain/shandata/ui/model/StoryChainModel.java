package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/11/23.
 */

public class StoryChainModel {

    private StoryChainBean storyBean;
    private int storyId;
    private boolean beFav;
    private ContactBean characterBean;

    public StoryChainBean getStoryBean() {
        return storyBean;
    }

    public void setStoryBean(StoryChainBean storyBean) {
        this.storyBean = storyBean;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public boolean isBeFav() {
        return beFav;
    }

    public void setBeFav(boolean beFav) {
        this.beFav = beFav;
    }

    public ContactBean getCharacterBean() {
        return characterBean;
    }

    public void setCharacterBean(ContactBean characterBean) {
        this.characterBean = characterBean;
    }
}
