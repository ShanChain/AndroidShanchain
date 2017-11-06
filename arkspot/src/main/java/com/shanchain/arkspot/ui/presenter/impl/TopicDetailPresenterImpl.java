package com.shanchain.arkspot.ui.presenter.impl;

import com.shanchain.arkspot.ui.presenter.TopicDetailPresenter;
import com.shanchain.arkspot.ui.view.activity.story.stroyView.TopicDetailView;

/**
 * Created by zhoujian on 2017/11/6.
 */

public class TopicDetailPresenterImpl implements TopicDetailPresenter {

    TopicDetailView mDetailView;

    public TopicDetailPresenterImpl(TopicDetailView detailView) {
        mDetailView = detailView;
    }

    @Override
    public void initTopicInfo(String topicId) {

    }

    @Override
    public void initStoryInfo() {

    }
}
