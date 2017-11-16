package com.shanchain.arkspot.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shanchain.arkspot.ui.model.SpaceInfo;
import com.shanchain.arkspot.ui.presenter.StoryTitlePresenter;
import com.shanchain.arkspot.ui.view.activity.story.stroyView.StoryTitleView;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
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
    public void initFavData(int page,int size) {
        //收藏的时空
        SCHttpUtils.postWithUserId()
                .url(HttpApi.SPACE_LIST_FAVORITE)
                .addParams("page",page+"")
                .addParams("size",size + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("获取我收藏的时空数据失败");
                        e.printStackTrace();
                        mStoryTitleView.getMyFavoriteSuccess(null,false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.d("获取的我收藏的时空数据" + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                                String data = JSONObject.parseObject(response).getString("data");
                                String content = JSONObject.parseObject(data).getString("content");
                                Boolean last = JSONObject.parseObject(data).getBoolean("last");
                                List<SpaceInfo> favoriteSpaceList = JSONObject.parseArray(content, SpaceInfo.class);
                                List<String> favoriteSpace = new ArrayList<>();
                                for (int i = 0; i < favoriteSpaceList.size(); i++) {
                                    String spaceId = favoriteSpaceList.get(i).getSpaceId() + "";
                                    favoriteSpace.add(spaceId);
                                }
                                String favoriteSpaceIds = JSON.toJSONString(favoriteSpace);
                                String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
                                SCCacheUtils.setCache(userId, Constants.CACHE_SPACE_COLLECTION, favoriteSpaceIds);
                            mStoryTitleView.getMyFavoriteSuccess(favoriteSpaceList,last);
                            }else {
                                mStoryTitleView.getMyFavoriteSuccess(null,false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mStoryTitleView.getMyFavoriteSuccess(null,false);
                        }
                    }
                });

    }

    @Override
    public void loadMoreData(int page, int size) {
        initSpace(page,size);
    }

    @Override
    public void initSpace(int page, int size) {
        SCHttpUtils.post()
                .url(HttpApi.SPACE_LIST_ALL)
                .addParams("page", "" + page)
                .addParams("size", "" + size)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取时空列表失败");
                        e.printStackTrace();
                        mStoryTitleView.getSpaceListSuccess(null,false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("时空列表 = " + response);
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            String spaceStr = JSONObject.parseObject(data).getString("content");
                            List<SpaceInfo> spaceInfos = JSONObject.parseArray(spaceStr, SpaceInfo.class);
                            Boolean isLast = JSONObject.parseObject(data).getBoolean("last");

                            mStoryTitleView.getSpaceListSuccess(spaceInfos,isLast);

                        } else {
                            mStoryTitleView.getSpaceListSuccess(null,false);
                        }
                    }
                });
    }

    @Override
    public void loadMoreLike(int likePage, int likeSize) {
        initFavData(likePage,likeSize);
    }
}
