package com.shanchain.arkspot.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/18.
 */

public class StoryModel {

    private StoryModelInfo modelInfo;
    private List<StoryModelInfo> storyChain;

    public StoryModelInfo getModelInfo() {
        return modelInfo;
    }

    public void setModelInfo(StoryModelInfo modelInfo) {
        this.modelInfo = modelInfo;
    }

    public List<StoryModelInfo> getStoryChain() {
        return storyChain;
    }

    public void setStoryChain(List<StoryModelInfo> storyChain) {
        this.storyChain = storyChain;
    }

    @Override
    public String toString() {
        return "StoryModel{" +
                "modelInfo=" + modelInfo +
                ", storyChain=" + storyChain +
                '}';
    }
}
