package com.shanchain.arkspot.ui.presenter.impl;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.shanchain.arkspot.ui.model.ResponseCharacterBrief;
import com.shanchain.arkspot.ui.model.ResponseCharacterList;
import com.shanchain.arkspot.ui.model.ResponseStoryChainBean;
import com.shanchain.arkspot.ui.model.ResponseStoryIdBean;
import com.shanchain.arkspot.ui.model.ResponseStoryListInfo;
import com.shanchain.arkspot.ui.model.StoryBeanModel;
import com.shanchain.arkspot.ui.model.StoryModel;
import com.shanchain.arkspot.ui.model.StoryModelBean;
import com.shanchain.arkspot.ui.model.StoryModelInfo;
import com.shanchain.arkspot.ui.model.StoryResponseInfo;
import com.shanchain.arkspot.ui.presenter.CurrentPresenter;
import com.shanchain.arkspot.ui.view.fragment.view.CurrentView;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

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
    public void initData(int page, int size) {
        SCHttpUtils.postWhitSpaceAndChaId()
                .url(HttpApi.STORY_RECOMMEND_HOT)
                .addParams("page", page + "")
                .addParams("size", size + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取实时数据失败");
                        e.printStackTrace();
                        mCurrentView.initSuccess(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        datas.clear();
                        LogUtils.i("实时数据 = " + response);
                        StoryResponseInfo storyResponseInfo = new Gson().fromJson(response, StoryResponseInfo.class);
                        List<ResponseStoryIdBean> storyBeanList = storyResponseInfo.getData();
                        if (!TextUtils.equals(storyResponseInfo.getCode(), NetErrCode.COMMON_SUC_CODE)) {
                            mCurrentView.initSuccess(null);
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


                        obtainStoryList(ids);

                    }
                });
    }

    @Override
    public void storySupport(String storyId) {
        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_SUPPORT_ADD)
                .addParams("storyId", storyId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("点赞失败");
                        e.printStackTrace();
                        mCurrentView.supportSuccess(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("点赞结果 = " + response);
                        mCurrentView.supportSuccess(true);
                    }
                });
    }

    @Override
    public void refreshData(int page, int size) {
        initData(page, size);
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
                        mCurrentView.initSuccess(null);
                        LogUtils.e("获取故事详情列表失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("故事列表数据 = " + response);

                        ResponseStoryListInfo storyListInfo = new Gson().fromJson(response, ResponseStoryListInfo.class);

                        if (!TextUtils.equals(storyListInfo.getCode(), NetErrCode.COMMON_SUC_CODE)) {
                            mCurrentView.initSuccess(null);
                            return;
                        }

                        List<StoryModelBean> data = storyListInfo.getData();
                        if (data != null) {

                            obtainCharacterBrief(data);

                        }

                    }
                });
    }

    private void builderData() {
        List<StoryBeanModel> list = new ArrayList<>();
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


        mCurrentView.initSuccess(list);
    }

    private void obtainCharacterBrief(final List<StoryModelBean> data) {

        List<Integer> characterIds = new ArrayList<>();
        for (StoryModelBean storyBean : data){
            characterIds.add(storyBean.getCharacterId());
        }


        LogUtils.i("角色一共有 " + characterIds.size());
        for (int i = 0; i < characterIds.size(); i ++) {

            LogUtils.i("角色id 有 = " + characterIds.get(i));

        }

        String jArr = new Gson().toJson(characterIds);

        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_BRIEF)
                .addParams("dataArray",jArr)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取角色");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取角色简要信息 = " + response);
                        ResponseCharacterList responseCharacterList = new Gson().fromJson(response, ResponseCharacterList.class);
                        List<ResponseCharacterBrief> characterBriefList = responseCharacterList.getData();
                        setData(data,characterBriefList);
                    }
                });

    }

    private void setData(List<StoryModelBean> data,List<ResponseCharacterBrief> characterBriefList){

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

            for (ResponseCharacterBrief characterBrief : characterBriefList){
                int characterId = characterBrief.getCharacterId();
                if (characterId == modelInfo.getBean().getCharacterId()){
                    modelInfo.setCharacterBrief(characterBrief);
                }
            }

            LogUtils.i("外层id" + storyId);
            List<StoryModelInfo> modelInfoList = storyModel.getStoryChain();
            if (modelInfoList != null) {
                for (int j = 0; j < modelInfoList.size(); j++) {
                    StoryModelInfo storyModelInfo = modelInfoList.get(j);
                    String storyId1 = storyModelInfo.getStoryId();
                    for (StoryModelBean bean : data) {
                        if (TextUtils.equals(storyId1, bean.getDetailId())) {
                            LogUtils.i("内层添加数据 j = " + j);
                            storyModelInfo.setBean(bean);
                        }
                    }

                    for (ResponseCharacterBrief characterBrief : characterBriefList){
                        int characterId = characterBrief.getCharacterId();
                        if (characterId == storyModelInfo.getBean().getCharacterId()){
                            storyModelInfo.setCharacterBrief(characterBrief);
                        }
                    }
                    LogUtils.i("内层id" + storyId1);
                }
            }
        }


        builderData();
    }

}
