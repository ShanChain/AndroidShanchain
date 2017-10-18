package com.shanchain.arkspot.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/18.
 */

public class StoryModel {

    private StoryModelInfo modelInfo;
    private List<StoryModelInfo> modelInfoList;

    public StoryModelInfo getModelInfo() {
        return modelInfo;
    }

    public void setModelInfo(StoryModelInfo modelInfo) {
        this.modelInfo = modelInfo;
    }

    public List<StoryModelInfo> getModelInfoList() {
        return modelInfoList;
    }

    public void setModelInfoList(List<StoryModelInfo> modelInfoList) {
        this.modelInfoList = modelInfoList;
    }
}
