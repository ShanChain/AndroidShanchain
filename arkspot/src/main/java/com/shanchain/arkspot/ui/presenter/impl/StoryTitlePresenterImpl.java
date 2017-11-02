package com.shanchain.arkspot.ui.presenter.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.shanchain.data.common.base.Constants;
import com.shanchain.arkspot.ui.model.SpaceInfo;
import com.shanchain.arkspot.ui.model.SpaceListInfo;
import com.shanchain.arkspot.ui.model.TagContentBean;
import com.shanchain.arkspot.ui.model.TagInfo;
import com.shanchain.arkspot.ui.presenter.StoryTitlePresenter;
import com.shanchain.arkspot.ui.view.activity.story.stroyView.StoryTitleView;
import com.shanchain.data.common.cache.SCCacheUtils;
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

public class StoryTitlePresenterImpl implements StoryTitlePresenter {

    private StoryTitleView mStoryTitleView;

    public StoryTitlePresenterImpl(StoryTitleView storyTitleView) {
        mStoryTitleView = storyTitleView;
    }


    @Override
    public void initData() {
        //获取标签列表
        SCHttpUtils.post()
                .url(HttpApi.TAG_QUERY)
                .addParams("type", "space")
                .addParams("page","0")
                .addParams("size","20")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("获取热门标签数据失败");
                        e.printStackTrace();
                        mStoryTitleView.getTagSuccess(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("获取的热门标签数据" + response);
                        TagInfo tagInfo = new Gson().fromJson(response, TagInfo.class);
                        LogUtils.d("=========" + tagInfo.toString());

                        List<TagContentBean> tagList = tagInfo.getData().getContent();
                        mStoryTitleView.getTagSuccess(tagList);
                    }
                });

        //获取瀑布流数据
        SCHttpUtils.post()
                .url(HttpApi.SPACE_LIST)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("获取时空列表数据失败");
                        e.printStackTrace();
                        mStoryTitleView.getSpaceListSuccess(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLog("时空列表数据 " + response);
                        SpaceListInfo spaceListInfo = new Gson().fromJson(response, SpaceListInfo.class);
                        List<SpaceInfo> spaceInfoList = spaceListInfo.getData();
                        mStoryTitleView.getSpaceListSuccess(spaceInfoList);
                    }
                });

        //收藏的时空
        SCHttpUtils.postWithUserId()
                .url(HttpApi.SPACE_LIST_FAVORITE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("获取我收藏的时空数据失败");
                        e.printStackTrace();
                        mStoryTitleView.getMyFavoriteSuccess(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("获取的我收藏的时空数据" + response);
                        SpaceListInfo spaceListInfo = new Gson().fromJson(response, SpaceListInfo.class);
                        LogUtils.d("我收藏的 = " + spaceListInfo.toString());
                        List<SpaceInfo> favoriteSpaceList = spaceListInfo.getData();
                        if (favoriteSpaceList != null){
                            List<String> favoriteSpace = new ArrayList<>();
                            for (int i = 0; i < favoriteSpaceList.size(); i ++) {
                                String spaceId = favoriteSpaceList.get(i).getSpaceId() + "";
                                favoriteSpace.add(spaceId);
                            }
                            String favoriteSpaceIds = JSON.toJSONString(favoriteSpace);
                            String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
                            SCCacheUtils.setCache(userId, Constants.CACHE_SPACE_COLLECTION,favoriteSpaceIds);
                        }
                        mStoryTitleView.getMyFavoriteSuccess(favoriteSpaceList);
                    }
                });

    }

    @Override
    public void loadMoreData(String userId) {
        mStoryTitleView.loadMoreResult();
    }
}
