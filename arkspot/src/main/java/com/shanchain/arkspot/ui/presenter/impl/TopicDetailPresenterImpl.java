package com.shanchain.arkspot.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.arkspot.ui.model.ResponseStoryChainBean;
import com.shanchain.arkspot.ui.model.ResponseStoryIdBean;
import com.shanchain.arkspot.ui.model.ResponseStoryListInfo;
import com.shanchain.arkspot.ui.model.StoryBeanModel;
import com.shanchain.arkspot.ui.model.StoryModel;
import com.shanchain.arkspot.ui.model.StoryModelBean;
import com.shanchain.arkspot.ui.model.StoryModelInfo;
import com.shanchain.arkspot.ui.presenter.TopicDetailPresenter;
import com.shanchain.arkspot.ui.view.activity.story.stroyView.TopicDetailView;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/11/6.
 */

public class TopicDetailPresenterImpl implements TopicDetailPresenter {

    TopicDetailView mDetailView;
    List<StoryModel> datas = new ArrayList<>();
    public TopicDetailPresenterImpl(TopicDetailView detailView) {
        mDetailView = detailView;
    }

    @Override
    public void initTopicInfo(String topicId) {

    }

    @Override
    public void initStoryInfo(String topicId, int page, int size) {
        datas.clear();
        SCHttpUtils.postWithChaId()
                .url(HttpApi.DYNAMIC_TOPIC)
                .addParams("page", page + "")
                .addParams("size", size + "")
                .addParams("topicId", topicId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取话题故事失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到话题故事列表 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                                String data = JSONObject.parseObject(response).getString("data");
                                String content = JSONObject.parseObject(data).getString("content");
                                Boolean last = JSONObject.parseObject(data).getBoolean("last");
                                List<ResponseStoryIdBean> storyBeanList = JSONObject.parseArray(content,ResponseStoryIdBean.class);
                                if (storyBeanList == null || storyBeanList.size() == 0){
                                    mDetailView.initSuccess(null,last);
                                    return;
                                }

                                List<String> ids = new ArrayList<>();
                                for (int i = 0; i < storyBeanList.size(); i++) {
                                    StoryModel storyModel = new StoryModel();
                                    StoryModelInfo storyModelInfo = new StoryModelInfo();
                                    List<StoryModelInfo> modelInfoList = new ArrayList<>();
                                    ResponseStoryIdBean responseStoryBean = storyBeanList.get(i);
                                    String detailId = responseStoryBean.getDetailId();
                                    storyModelInfo.setStoryId(detailId);
                                    ids.add(detailId);
                                    ResponseStoryChainBean storyChainBean = responseStoryBean.getChain();
                                    if (storyChainBean != null) {
                                        List<String> detailIds = storyChainBean.getDetailIds();
                                        if (detailIds != null) {
                                            if (storyChainBean.getDetailIds().size() == 0) {

                                            } else {
                                                for (int j = 0; j < detailIds.size(); j++) {
                                                    String dId = detailIds.get(j);
                                                    ids.add(dId);
                                                    StoryModelInfo modelInfo = new StoryModelInfo();
                                                    modelInfo.setStoryId(dId);
                                                    modelInfoList.add(modelInfo);
                                                }
                                                storyModel.setStoryChain(modelInfoList);
                                            }
                                        }
                                    } else {
                                        modelInfoList = null;
                                        storyModel.setStoryChain(modelInfoList);
                                    }
                                    storyModel.setModelInfo(storyModelInfo);
                                    datas.add(storyModel);
                                }
                                LogUtils.i("datas长度 = " + datas.size());
                                obtainStoryList(ids,last);
                            }else {
                                mDetailView.initSuccess(null,false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mDetailView.initSuccess(null,false);
                        }
                    }
                });
    }



    private void obtainStoryList(List<String> list, final boolean isLast) {

        String dataArray = JSONObject.toJSONString(list);
        LogUtils.i("dataArray = " + dataArray);
        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_RECOMMEND_DETAIL)
                .addParams("dataArray", dataArray)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mDetailView.initSuccess(null,isLast);
                        LogUtils.e("获取故事详情列表失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("故事列表数据 = " + response);
                            ResponseStoryListInfo storyListInfo = JSONObject.parseObject(response, ResponseStoryListInfo.class);
                            if (!TextUtils.equals(storyListInfo.getCode(), NetErrCode.COMMON_SUC_CODE)) {
                                mDetailView.initSuccess(null,isLast);
                                return;
                            }
                            List<StoryModelBean> data = storyListInfo.getData();
                            if (data != null && data.size() >0) {
                                builderData(data,isLast);
                            }else {
                                mDetailView.initSuccess(null,isLast);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mDetailView.initSuccess(null,isLast);
                        }
                    }
                });
    }


    private void builderData(List<StoryModelBean> data,boolean isLast) {
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
            List<StoryModelInfo> storyChain = storyModel.getStoryChain();
            if (storyChain!=null && storyChain.size()>0){
                LogUtils.i("构建数据时获取的楼层数 = " + storyChain.size());
                for (int j = 0 ; j < storyChain.size();j ++){
                    StoryModelInfo storyModelInfo = storyChain.get(j);
                    String storyId1 = storyModelInfo.getStoryId();
                    LogUtils.i("楼层中的故事id = " + storyId1);
                    for (StoryModelBean bean : data){
                        String detailId = bean.getDetailId();
                        LogUtils.i("所有故事表中的id = " + detailId);
                        if (TextUtils.equals(storyId1,detailId)){
                            LogUtils.i("内层添加数据 j = " + j);
                            storyModelInfo.setBean(bean);
                        }
                    }
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
        mDetailView.initSuccess(list,isLast);
    }

    @Override
    public void loadMore(String topicId, int page, int size) {
        initStoryInfo(topicId,page,size);
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
                        mDetailView.supportCancelSuccess(false,position);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("取消点赞成功 = " + response);
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                            mDetailView.supportCancelSuccess(true,position);
                        }else {
                            mDetailView.supportCancelSuccess(false,position);
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
                        mDetailView.supportSuccess(false,position);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("点赞结果 = " + response);
                        String code = JSONObject.parseObject(response).getString("code");

                        if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                            mDetailView.supportSuccess(true,position);
                        }else {
                            mDetailView.supportSuccess(false,position);
                        }
                    }
                });
    }

}
