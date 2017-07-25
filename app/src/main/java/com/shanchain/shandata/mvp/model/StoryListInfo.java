package com.shanchain.shandata.mvp.model;

/**
 * Created by zhoujian on 2017/7/17.
 */

public class StoryListInfo {
    private String storyName;
    private String storyDes;
    private String storyResult;
    private boolean isFinish;
    private String updateInfo;

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public String getStoryDes() {
        return storyDes;
    }

    public void setStoryDes(String storyDes) {
        this.storyDes = storyDes;
    }

    public String getStoryResult() {
        return storyResult;
    }

    public void setStoryResult(String storyResult) {
        this.storyResult = storyResult;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }
}
