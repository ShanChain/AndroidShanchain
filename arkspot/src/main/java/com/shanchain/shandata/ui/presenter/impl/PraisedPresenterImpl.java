package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.encryption.SCJsonUtils;
import com.shanchain.shandata.ui.model.ContactBean;
import com.shanchain.shandata.ui.model.StoryContentBean;
import com.shanchain.shandata.ui.model.StoryInfo;
import com.shanchain.shandata.ui.presenter.PraisedPresenter;
import com.shanchain.shandata.ui.view.activity.mine.view.PraisedView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/11/21.
 */

public class PraisedPresenterImpl implements PraisedPresenter {
    private PraisedView mView;
    private List<StoryContentBean> mContentBeanList = new ArrayList<>();

    public PraisedPresenterImpl(PraisedView view) {
        mView = view;
    }


    @Override
    public void initPraiseData(int page, int size) {
        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_SUPPORT_LIST)
                .addParams("page", "" + page)
                .addParams("size", "" + size)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取我赞过的失败");
                        e.printStackTrace();
                        mView.initPraisedSuc(null, false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到我赞过的数据 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                boolean last = SCJsonUtils.parseBoolean(data, "last");
                                String content = SCJsonUtils.parseString(data, "content");
                                List<StoryContentBean> contentBeanList = JSONObject.parseArray(content, StoryContentBean.class);
                                if (contentBeanList != null && contentBeanList.size() > 0) {



                                    mView.initPraisedSuc(contentBeanList, last);
                                } else {
                                    mView.initPraisedSuc(null, last);
                                }
                            } else {
                                mView.initPraisedSuc(null, false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initPraisedSuc(null,false);
                        }


                    }
                });
    }

    @Override
    public void initStoryData(int page, int size) {
        mContentBeanList.clear();
        String spaceId = SCCacheUtils.getCacheSpaceId();
        SCHttpUtils.postWithUserId()
                .url(HttpApi.STORY_QUERY_MINE)
                .addParams("page", "" + page)
                .addParams("size", "" + size)
                .addParams("type", StoryInfo.type1 + "")
                .addParams("spaceId", spaceId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取故事我的故事列表失败");
                        e.printStackTrace();
                        mView.initStorySuc(null,false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到我的故事列表 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                boolean last = SCJsonUtils.parseBoolean(data, "last");
                                String content = SCJsonUtils.parseString(data, "content");
                                mContentBeanList = JSONObject.parseArray(content, StoryContentBean.class);
                                if (mContentBeanList != null && mContentBeanList.size() > 0) {
                                    List<Integer> ids = new ArrayList<>();
                                    for (int i = 0; i < mContentBeanList.size(); i ++) {
                                        int characterId = mContentBeanList.get(i).getCharacterId();
                                        ids.add(characterId);
                                    }
                                    obtainCharacterInfo(ids,last);
                                }else {
                                    mView.initStorySuc(null,last);
                                }
                            } else {
                                mView.initStorySuc(null,false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initStorySuc(null,false);
                        }
                    }
                });
    }

    private void obtainCharacterInfo(List<Integer> ids, final boolean last) {
        String jArr = JSONObject.toJSONString(ids);
        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_BRIEF)
                .addParams("dataArray",jArr)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取角色简要信息失败");
                        e.printStackTrace();
                        mView.initStorySuc(null,last);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到角色简要信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                                String data = SCJsonUtils.parseData(response);
                                List<ContactBean> contactBeanList = JSONObject.parseArray(data, ContactBean.class);
                                for (int i = 0; i < mContentBeanList.size(); i ++) {
                                    StoryContentBean storyContentBean = mContentBeanList.get(i);
                                    for (ContactBean bean : contactBeanList){
                                        if (bean.getCharacterId() == storyContentBean.getCharacterId()){
                                            storyContentBean.setContactBean(bean);
                                        }
                                    }
                                }
                                mView.initStorySuc(mContentBeanList,last);
                            }else{
                                mView.initStorySuc(null,last);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initStorySuc(null,last);
                        }
                    }
                });
    }
}
