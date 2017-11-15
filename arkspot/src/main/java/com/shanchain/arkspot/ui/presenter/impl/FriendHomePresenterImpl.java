package com.shanchain.arkspot.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.arkspot.ui.model.CharacterInfo;
import com.shanchain.arkspot.ui.model.StoryBeanModel;
import com.shanchain.arkspot.ui.model.StoryModel;
import com.shanchain.arkspot.ui.model.StoryModelBean;
import com.shanchain.arkspot.ui.model.StoryModelInfo;
import com.shanchain.arkspot.ui.presenter.FriendHomePresenter;
import com.shanchain.arkspot.ui.view.activity.mine.view.FriendHomeView;
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
 * Created by zhoujian on 2017/11/14.
 */

public class FriendHomePresenterImpl implements FriendHomePresenter {
    private FriendHomeView mView;
    //自定义的故事数据模型
    List<StoryModel> datas = new ArrayList<>();

    public FriendHomePresenterImpl(FriendHomeView view) {
        mView = view;
    }

    @Override
    public void initCharacterInfo(final int characterId) {
        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_QUERY)
                .addParams("characterId", characterId + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取好友信息失败 ");
                        e.printStackTrace();
                        mView.initFriendSuc(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到好友信息 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                CharacterInfo characterInfo = JSONObject.parseObject(data, CharacterInfo.class);
                                mView.initFriendSuc(characterInfo);
                                //checkIsFocus(characterInfo);

                            } else {
                                //
                                mView.initFriendSuc(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initFriendSuc(null);
                        }

                    }
                });
    }


    private void checkIsFocus(CharacterInfo characterInfo) {
        int characterId = characterInfo.getCharacterId();
        SCHttpUtils.postWithChaId()
                .url(HttpApi.FOCUS_IS_FAV)
                .addParams("checkId", characterId + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取是否关注角色失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到是否关注结果 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {

                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                });
    }

    @Override
    public void focus(int characterId) {
        String cacheCharacterId = SCCacheUtils.getCacheCharacterId();
        SCHttpUtils.post()
                .url(HttpApi.FOCUS_FOCUS)
                .addParams("funsId", cacheCharacterId)
                .addParams("characterId", characterId + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("关注角色失败");
                        e.printStackTrace();
                        mView.focusSuc(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("关注角色成功 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                CharacterInfo characterInfo = JSONObject.parseObject(data, CharacterInfo.class);
                                if (characterInfo != null) {
                                    //关注成功
                                    mView.focusSuc(true);
                                } else {
                                    mView.focusSuc(false);
                                }
                            } else {
                                mView.focusSuc(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.focusSuc(false);
                        }
                    }
                });
    }

    @Override
    public void initStory(int characterId, int page, int size) {
        datas.clear();
        SCHttpUtils.postWithUidSpaceIdAndCharId()
                .url(HttpApi.DYNAMIC_CHARACTER)
                .addParams("page", page + "")
                .addParams("size", size + "")
                .addParams("targetId", characterId + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取角色动态失败");
                        e.printStackTrace();
                        mView.getStoryInfoSuc(null,false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到角色动态 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                Boolean last = JSONObject.parseObject(data).getBoolean("last");
                                String content = JSONObject.parseObject(data).getString("content");
                                List<String> detailIds = JSONObject.parseArray(content, String.class);
                                if (detailIds.size() == 0) {
                                    mView.getStoryInfoSuc(null,last);
                                } else {
                                    List<String> ids = new ArrayList<>();
                                    for (int i = 0; i < detailIds.size(); i++) {
                                        StoryModel storyModel = new StoryModel();
                                        StoryModelInfo storyModelInfo = new StoryModelInfo();
                                        storyModel.setStoryChain(null);
                                        String detailData = detailIds.get(i);
                                        String detailId = JSONObject.parseObject(detailData).getString("detailId");
                                        ids.add(detailId);
                                        storyModelInfo.setStoryId(detailId);
                                        storyModel.setModelInfo(storyModelInfo);
                                        datas.add(storyModel);
                                    }
                                    obtainStoryInfo(last, ids);
                                }

                            } else {
                                    mView.getStoryInfoSuc(null,false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.getStoryInfoSuc(null,false);
                        }
                    }
                });
    }

    @Override
    public void loadMore(int characterId, int page, int size) {
        initStory(characterId, page, size);
    }

    @Override
    public void obtainHxInfo(int characterId) {
        SCHttpUtils.post()
                .url(HttpApi.HX_USER_REGIST)
                .addParams("characterId",characterId + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取好友环信信息失败");
                        e.printStackTrace();
                        mView.getHxSuc(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到好友环信信息 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                                String data = JSONObject.parseObject(response).getString("data");
                                String hxUserName = JSONObject.parseObject(data).getString("hxUserName");
                                mView.getHxSuc(hxUserName);
                            }else {
                                mView.getHxSuc(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.getHxSuc(null);
                        }
                    }
                });
    }

    @Override
    public void storyCancelSupport(final int position, String storyId) {
        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_SUPPORT_CANCEL)
                .addParams("storyId",storyId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("取消点赞失败");
                        e.printStackTrace();
                        mView.supportCancelSuccess(false,position);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("取消点赞成功 = " + response);
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                            mView.supportCancelSuccess(true,position);
                        }else {
                            mView.supportCancelSuccess(false,position);
                        }
                    }
                });
    }

    @Override
    public void storySupport(final int position, String storyId) {
        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_SUPPORT_ADD)
                .addParams("storyId", storyId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("点赞失败");
                        e.printStackTrace();
                        mView.supportSuccess(false,position);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("点赞结果 = " + response);
                        String code = JSONObject.parseObject(response).getString("code");

                        if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                            mView.supportSuccess(true,position);
                        }else {
                            mView.supportSuccess(false,position);
                        }
                    }
                });
    }

    private void obtainStoryInfo(final Boolean last, List<String> detailIds) {
        String dataArray = JSONObject.toJSONString(detailIds);
        LogUtils.i("dataArray = " + dataArray);
        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_RECOMMEND_DETAIL)
                .addParams("dataArray", dataArray)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取动态详情信息失败");
                        e.printStackTrace();
                        mView.getStoryInfoSuc(null,last);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取动态详情成功 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                List<StoryModelBean> list = JSONObject.parseArray(data, StoryModelBean.class);
                                buildData(list, last);
                            } else {
                                mView.getStoryInfoSuc(null,last);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.getStoryInfoSuc(null,last);
                        }
                    }
                });
    }

    private void buildData(List<StoryModelBean> data, Boolean last) {
        List<StoryBeanModel> list = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            StoryModel storyModel = datas.get(i);
            StoryModelInfo modelInfo = storyModel.getModelInfo();
            String storyId = modelInfo.getStoryId();
            for (StoryModelBean bean : data) {
                String detailId = bean.getDetailId();
                if (TextUtils.equals(storyId, detailId)) {
                    LogUtils.i("外层添加数据 i = " + i);
                    modelInfo.setBean(bean);
                }
            }
        }

        for (int i = 0; i < datas.size(); i++) {
            StoryBeanModel beanModel = new StoryBeanModel();
            StoryModel storyModel = datas.get(i);
            beanModel.setStoryModel(storyModel);
            StoryModelInfo modelInfo = storyModel.getModelInfo();
            StoryModelBean bean = modelInfo.getBean();
            int type = bean.getType();
            beanModel.setItemType(type);
            list.add(beanModel);
            LogUtils.i("构建数据结果 = " + beanModel);
        }

        mView.getStoryInfoSuc(list, last);
    }

}
