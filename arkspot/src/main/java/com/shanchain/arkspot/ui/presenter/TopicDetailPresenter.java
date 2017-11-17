package com.shanchain.arkspot.ui.presenter;

/**
 * Created by zhoujian on 2017/11/6.
 */

public interface TopicDetailPresenter {
    void initTopicInfo(String topicId);
    void initStoryInfo(String topicId , int page , int size);

    void loadMore(String topicId, int page, int size);

    void storyCancelSupport(int position, String storyId);

    void storySupport(int position, String storyId);
}
