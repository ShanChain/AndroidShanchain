package com.shanchain.arkspot.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.shanchain.arkspot.ui.model.ResponseStoryChainBean;
import com.shanchain.arkspot.ui.model.ResponseStoryIdBean;
import com.shanchain.arkspot.ui.model.ResponseStoryListInfo;
import com.shanchain.arkspot.ui.model.StoryBeanModel;
import com.shanchain.arkspot.ui.model.StoryInfo;
import com.shanchain.arkspot.ui.model.StoryModel;
import com.shanchain.arkspot.ui.model.StoryModelBean;
import com.shanchain.arkspot.ui.model.StoryModelInfo;
import com.shanchain.arkspot.ui.model.StoryResponseInfo;
import com.shanchain.arkspot.ui.presenter.CurrentPresenter;
import com.shanchain.arkspot.ui.view.fragment.view.CurrentView;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.shanchain.data.common.net.NetErrCode.COMMON_SUC_CODE;

/**
 * Created by zhoujian on 2017/10/17.
 */

public class CurrentPresenterImpl implements CurrentPresenter {

    private CurrentView mCurrentView;
    //自定义的故事数据模型
    List<StoryModel> datas = new ArrayList<>();
    public CurrentPresenterImpl(CurrentView currentView) {
        mCurrentView = currentView;
    }


    @Override
    public void initData(String characterId, int page, int size, final String spaceId) {
        SCHttpUtils.postFWhitSpceAndChaId()
                .url(HttpApi.STORY_RECOMMEND_HOT)
                //.addParams("characterId", characterId)
                .addParams("page", page + "")
                .addParams("size", size + "")
                //.addParams("spaceId", spaceId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("获取实时数据失败");
                        e.printStackTrace();
                        mCurrentView.initSuccess(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("实时数据 = " + response);

                        StoryResponseInfo storyResponseInfo = new Gson().fromJson(response, StoryResponseInfo.class);
                        List<ResponseStoryIdBean> storyBeanList = storyResponseInfo.getData();

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
                            if (storyChainBean != null ){
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
                                        storyModel.setModelInfoList(modelInfoList);
                                    }
                                }
                            }else {
                                modelInfoList = null;
                                storyModel.setModelInfoList(modelInfoList);
                            }

                            storyModel.setModelInfo(storyModelInfo);

                            datas.add(storyModel);
                        }

                        LogUtils.d("datas长度 = " + datas.size());



                        obtainStoryList(ids);

                    }
                });
    }


    private void obtainStoryList(List<String> list) {
        String dataArray = new Gson().toJson(list);
        LogUtils.d("dataArray == " + dataArray);
        SCHttpUtils.post()
                .url(HttpApi.STORY_RECOMMEND_DETAIL)
                .addParams("dataArray", dataArray)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if(JSONObject.parseObject(response).getString("code").equalsIgnoreCase(COMMON_SUC_CODE)){
                            LogUtils.d("故事列表数据 = " + response);

                            ResponseStoryListInfo storyListInfo = new Gson().fromJson(response, ResponseStoryListInfo.class);
                            List<StoryModelBean> data = storyListInfo.getData();
                            for (int i = 0; i < data.size(); i ++) {
                                StoryModelBean storyBean = data.get(i);
                                StoryInfo storyInfo = new StoryInfo();
                                storyInfo.setItemType(storyBean.getType());
                            }

                            setData(data);
                            builderData();
                        }


                    }
                });
    }

    private void builderData() {
        List<StoryBeanModel> list = new ArrayList<>();

        for (int i = 0; i < datas.size(); i ++) {
            StoryBeanModel beanModel = new StoryBeanModel();
            StoryModel storyModel = datas.get(i);
            beanModel.setStoryModel(storyModel);
            StoryModelInfo modelInfo = storyModel.getModelInfo();
            StoryModelBean bean = modelInfo.getBean();
            int type = bean.getType();
            beanModel.setItemType(type);
            list.add(beanModel);
        }

        mCurrentView.initSuccess(list);
    }

    private void setData(List<StoryModelBean> data) {
        for (int i = 0; i < datas.size(); i ++) {
            StoryModel storyModel = datas.get(i);
            StoryModelInfo modelInfo = storyModel.getModelInfo();
            String storyId = modelInfo.getStoryId();
            for(StoryModelBean bean : data) {
                String detailId = bean.getDetailId();
                if (TextUtils.equals(storyId,detailId)) {
                    LogUtils.d("外层添加数据");
                    modelInfo.setBean(bean);
                }
            }

            LogUtils.d("外层id" + storyId);
            List<StoryModelInfo> modelInfoList = storyModel.getModelInfoList();
            if (modelInfoList != null) {
                for (int j = 0; j < modelInfoList.size(); j ++) {
                    StoryModelInfo storyModelInfo = modelInfoList.get(j);
                    String storyId1 = storyModelInfo.getStoryId();
                    for (StoryModelBean bean : data) {
                        if (TextUtils.equals(storyId1,bean.getDetailId())) {
                            LogUtils.d("内层添加数据");
                            storyModelInfo.setBean(bean);
                        }
                    }
                    LogUtils.d("内层id" + storyId1);
                }
            }
        }
    }

}
