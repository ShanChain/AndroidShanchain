package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.encryption.SCJsonUtils;
import com.shanchain.shandata.ui.model.IsFavBean;
import com.shanchain.shandata.ui.model.StoryDetailInfo;
import com.shanchain.shandata.ui.model.StoryInfo;
import com.shanchain.shandata.ui.presenter.MyStoryPresenter;
import com.shanchain.shandata.ui.view.activity.mine.view.MyStoryView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/11/25.
 */

public class MyStoryPresenterImpl implements MyStoryPresenter {
    private MyStoryView mView;
    private List<StoryDetailInfo> mStoryDetailInfoList;

    public MyStoryPresenterImpl(MyStoryView view) {
        mView = view;
    }

    @Override
    public void initStory(int page, int size) {
        String spaceId = SCCacheUtils.getCacheSpaceId();
        SCHttpUtils.postWithUserId()
                .url(HttpApi.STORY_QUERY_MINE)
                .addParams("page", "" + page)
                .addParams("size", "" + size)
                .addParams("type", StoryInfo.type1 + "")
                .addParams("spaceId", spaceId)
                .build()
                .execute(new SCHttpStringCallBack() {

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
                                mStoryDetailInfoList = JSONObject.parseArray(content, StoryDetailInfo.class);
                                if (mStoryDetailInfoList != null && mStoryDetailInfoList.size()>0){
                                    checkFav(mStoryDetailInfoList,last);

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

    @Override
    public void supportCancel(int storyId, final int position) {
        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_SUPPORT_CANCEL)
                .addParams("storyId", storyId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("取消点赞失败");
                        e.printStackTrace();
                        mView.supportCancelSuc(false, position);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("取消点赞成功 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                mView.supportCancelSuc(true, position);
                            } else {
                                mView.supportCancelSuc(false, position);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                                 mView.supportCancelSuc(false, position);
                        }
                    }
                });
    }

    @Override
    public void support(int storyId, final int position) {
        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_SUPPORT_ADD)
                .addParams("storyId", storyId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("点赞失败");
                        e.printStackTrace();
                        mView.supportSuc(false, position);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("点赞结果 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");

                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                mView.supportSuc(true, position);
                            } else {
                                mView.supportSuc(false, position);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                                  mView.supportSuc(false, position);
                        }
                    }
                });
    }

    private void checkFav(List<StoryDetailInfo> storyDetailInfoList,final boolean last) {
        List<Integer> chechList = new ArrayList<>();
        for (int i = 0; i < storyDetailInfoList.size(); i ++) {
            int storyId = storyDetailInfoList.get(i).getStoryId();
            chechList.add(storyId);
        }
        String jArr = JSONObject.toJSONString(chechList);

        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_ISFAV_LIST)
                .addParams("checkIdList",jArr)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("判断故事是否点赞失败");
                        e.printStackTrace();
                        mView.initStorySuc(null,false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到故事点赞数据 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                                String data = SCJsonUtils.parseData(response);
                                List<IsFavBean> favBeanList = JSONObject.parseArray(data, IsFavBean.class);
                                for (int i = 0; i < mStoryDetailInfoList.size(); i ++) {
                                    int storyId = mStoryDetailInfoList.get(i).getStoryId();
                                    for (IsFavBean bean : favBeanList){
                                        if (storyId == bean.getStoryId()){
                                            mStoryDetailInfoList.get(i).setFav(bean.isCheck());
                                        }
                                    }
                                }
                                mView.initStorySuc(mStoryDetailInfoList,last);
                            }else{
                                mView.initStorySuc(mStoryDetailInfoList,last);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initStorySuc(mStoryDetailInfoList,last);
                        }
                    }
                });


    }
}
