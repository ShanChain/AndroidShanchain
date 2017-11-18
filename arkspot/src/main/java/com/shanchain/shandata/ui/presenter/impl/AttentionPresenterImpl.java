package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.shandata.ui.model.ResponseStoryChainBean;
import com.shanchain.shandata.ui.model.ResponseStoryIdBean;
import com.shanchain.shandata.ui.model.ResponseStoryIdData;
import com.shanchain.shandata.ui.model.ResponseStoryListInfo;
import com.shanchain.shandata.ui.model.StoryBeanModel;
import com.shanchain.shandata.ui.model.StoryModel;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.model.StoryModelInfo;
import com.shanchain.shandata.ui.model.StoryResponseInfo;
import com.shanchain.shandata.ui.presenter.AttentionPresenter;
import com.shanchain.shandata.ui.view.fragment.view.AttentionView;
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

public class AttentionPresenterImpl implements AttentionPresenter {

    private AttentionView mAttentionView;
    //自定义的故事数据模型
    List<StoryModel> datas = new ArrayList<>();

    public AttentionPresenterImpl(AttentionView attentionView) {
        mAttentionView = attentionView;
    }


    @Override
    public void initData(int page, int size) {
        datas.clear();
        SCHttpUtils.postWithUidSpaceIdAndCharId()
                .url(HttpApi.STORY_RECOMMEND_FOCUS)
                .addParams("page", page + "")
                .addParams("size", size + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取实时数据失败");
                        e.printStackTrace();
                        mAttentionView.initSuccess(null, false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("关注数据 = " + response);
                            StoryResponseInfo storyResponseInfo = JSONObject.parseObject(response, StoryResponseInfo.class);

                            if (!TextUtils.equals(storyResponseInfo.getCode(), NetErrCode.COMMON_SUC_CODE)) {
                                mAttentionView.initSuccess(null, false);
                                return;
                            }
                            ResponseStoryIdData idData = storyResponseInfo.getData();
                            if (idData == null){
                                mAttentionView.initSuccess(null,false);
                                return;
                            }
                            List<ResponseStoryIdBean> storyBeanList = idData.getContent();
                            boolean last = idData.isLast();
                            if (storyBeanList.size() == 0){
                                mAttentionView.initSuccess(null,last);
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
                            obtainStoryList(ids, last);
                        } catch (Exception e) {
                            e.printStackTrace();
                            mAttentionView.initSuccess(null,false);
                        }
                    }
                });
    }

    @Override
    public void refresh(int page, int size) {
        initData(page, size);
    }

    @Override
    public void loadMore(int page, int size) {
        initData(page, size);
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
                        mAttentionView.supportCancelSuccess(false,position);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("取消点赞成功 = " + response);
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                            mAttentionView.supportCancelSuccess(true,position);
                        }else {
                            mAttentionView.supportCancelSuccess(false,position);
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
                        mAttentionView.supportSuccess(false,position);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("点赞结果 = " + response);
                        String code = JSONObject.parseObject(response).getString("code");

                        if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                            mAttentionView.supportSuccess(true,position);
                        }else {
                            mAttentionView.supportSuccess(false,position);
                        }
                    }
                });
    }


    private void obtainStoryList(List<String> list, final boolean isLast) {
        String dataArray = JSONObject.toJSONString(list);
        LogUtils.d("dataArray = " + dataArray);
        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_RECOMMEND_DETAIL)
                .addParams("dataArray", dataArray)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mAttentionView.initSuccess(null, isLast);
                        LogUtils.e("获取故事详情列表失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("故事列表数据 = " + response);

                        ResponseStoryListInfo storyListInfo = JSONObject.parseObject(response, ResponseStoryListInfo.class);

                        if (!TextUtils.equals(storyListInfo.getCode(), NetErrCode.COMMON_SUC_CODE)) {
                            mAttentionView.initSuccess(null, isLast);
                            return;
                        }

                        List<StoryModelBean> data = storyListInfo.getData();
                        if (data != null && data.size() > 0) {
                            // obtainCharacterBrief(data, isLast);
                            builderData(data, isLast);
                        } else {
                            mAttentionView.initSuccess(null, isLast);
                            return;
                        }

                    }
                });
    }

   /* private void obtainCharacterBrief(final List<StoryModelBean> data, final boolean isLast) {

        List<Integer> characterIds = new ArrayList<>();
        for (StoryModelBean storyBean : data) {
            characterIds.add(storyBean.getCharacterId());
        }

        LogUtils.i("角色一共有 " + characterIds.size());
        for (int i = 0; i < characterIds.size(); i++) {
            LogUtils.i("角色id 有 = " + characterIds.get(i));

        }

        String jArr = JSONObject.toJSONString(characterIds);
        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_BRIEF)
                .addParams("dataArray", jArr)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取角色简要信息失败");
                        mAttentionView.initSuccess(null, isLast);
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取角色简要信息 = " + response);
                        ResponseCharacterList responseCharacterList = JSONObject.parseObject(response, ResponseCharacterList.class);
                        List<ResponseCharacterBrief> characterBriefList = responseCharacterList.getData();
                        setData(data, characterBriefList, isLast);
                    }
                });

    }*/

    private void builderData(List<StoryModelBean> data, boolean isLast) {

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
        mAttentionView.initSuccess(list, isLast);
    }
}
