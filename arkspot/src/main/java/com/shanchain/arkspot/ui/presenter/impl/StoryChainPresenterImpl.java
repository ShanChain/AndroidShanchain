package com.shanchain.arkspot.ui.presenter.impl;

import com.shanchain.arkspot.ui.presenter.StoryChainPresenter;
import com.shanchain.arkspot.ui.view.activity.story.stroyView.StoryChainView;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/11/17.
 */

public class StoryChainPresenterImpl implements StoryChainPresenter {
    private StoryChainView mView;

    public StoryChainPresenterImpl(StoryChainView view) {
        mView = view;
    }

    @Override
    public void initStoryList(int start, int end, String storyId) {
        SCHttpUtils.post()
                .url(HttpApi.STORY_CHAIN_ID)
                .addParams("","")
                .addParams("","")
                .addParams("","")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });
    }
}
