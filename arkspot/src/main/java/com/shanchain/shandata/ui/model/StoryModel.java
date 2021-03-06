package com.shanchain.shandata.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhoujian on 2017/10/18.
 */

public class StoryModel implements Serializable{

    private StoryModelInfo modelInfo;
    private List<StoryModelInfo> storyChain;
    private int chainCount;

    public int getChainCount() {
        return chainCount;
    }

    public void setChainCount(int chainCount) {
        this.chainCount = chainCount;
    }

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
