package com.shanchain.shandata.ui.presenter;

/**
 * Created by zhoujian on 2017/11/17.
 */

public interface StoryChainPresenter {
    void initStoryList(int start, int end, String storyId);

    void supportCancel(int storyId,int position);

    void support(int storyId,int position);
}