package com.shanchain.arkspot.ui.presenter.impl;

import com.google.gson.Gson;

import com.shanchain.arkspot.ui.model.StoryInfo;
import com.shanchain.arkspot.ui.model.StoryListDataBean;
import com.shanchain.arkspot.ui.model.StoryListInfo;
import com.shanchain.arkspot.ui.presenter.AttentionPresenter;
import com.shanchain.arkspot.ui.view.fragment.view.AttentionView;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/10/13.
 */

public class AttentionPresenterImpl implements AttentionPresenter {

    private AttentionView mAttentionView;

    public AttentionPresenterImpl(AttentionView attentionView) {
        mAttentionView = attentionView;
    }



    @Override
    public void initData(String characterId) {
        SCHttpUtils.post()
                .url(HttpApi.STORY_FOCUS_GET)
                .addParams("characterId",characterId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("获取故事列表失败");
                        e.printStackTrace();
                        mAttentionView.initError(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLog("故事列表数据 = "+response);
                        LogUtils.d("======"+response);
                        StoryListInfo storyListInfo = new Gson().fromJson(response, StoryListInfo.class);
                        List<StoryListDataBean> storyList = storyListInfo.getData();
                        List<StoryInfo> storyInfoList = new ArrayList<>();
                        for (int i = 0; i < storyList.size(); i ++) {
                            StoryInfo info = new StoryInfo();
                            info.setStoryListDataBean(storyList.get(i));
                            int type = storyList.get(i).getType();
                            info.setItemType(type);
                            storyInfoList.add(info);
                        }


                        mAttentionView.initSuccess(storyInfoList);
                    }
                });

    }
}
