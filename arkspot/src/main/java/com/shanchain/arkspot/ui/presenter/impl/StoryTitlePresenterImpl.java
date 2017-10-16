package com.shanchain.arkspot.ui.presenter.impl;

import com.google.gson.Gson;
import com.shanchain.arkspot.http.HttpApi;
import com.shanchain.arkspot.ui.model.FavoriteSpaceBean;
import com.shanchain.arkspot.ui.model.FavoriteSpaceInfo;
import com.shanchain.arkspot.ui.model.SpaceBean;
import com.shanchain.arkspot.ui.model.SpaceListInfo;
import com.shanchain.arkspot.ui.model.TagContentBean;
import com.shanchain.arkspot.ui.model.TagInfo;
import com.shanchain.arkspot.ui.presenter.StoryTitlePresenter;
import com.shanchain.arkspot.ui.view.activity.story.stroyView.StoryTitleView;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.netrequest.SCHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
    public void initData(String userId) {
        //获取标签列表
        SCHttpUtils.post()
                .url(HttpApi.TAG_QUERY)
                .addParams("type", "space")
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
                        List<SpaceBean> spaceBeanList = spaceListInfo.getData();
                        mStoryTitleView.getSpaceListSuccess(spaceBeanList);
                    }
                });

        SCHttpUtils.post()
                .url(HttpApi.SPACE_LIST_FAVORITE)
                .addParams("userId", userId)
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
                        FavoriteSpaceInfo favoriteSpaceInfo = new Gson().fromJson(response, FavoriteSpaceInfo.class);
                        LogUtils.d("我收藏的 = " + favoriteSpaceInfo.toString());
                        List<FavoriteSpaceBean> favoriteSpaceList = favoriteSpaceInfo.getData();

                        mStoryTitleView.getMyFavoriteSuccess(favoriteSpaceList);
                    }
                });

    }

    @Override
    public void loadMoreData(String userId) {

    }
}
