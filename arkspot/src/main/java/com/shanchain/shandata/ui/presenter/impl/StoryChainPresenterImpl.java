package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.encryption.SCJsonUtils;
import com.shanchain.shandata.ui.model.ContactBean;
import com.shanchain.shandata.ui.model.IsFavBean;
import com.shanchain.shandata.ui.model.StoryChainBean;
import com.shanchain.shandata.ui.model.StoryChainModel;
import com.shanchain.shandata.ui.presenter.StoryChainPresenter;
import com.shanchain.shandata.ui.view.activity.story.stroyView.StoryChainView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/11/17.
 */

public class StoryChainPresenterImpl implements StoryChainPresenter {
    private StoryChainView mView;
    private List<StoryChainModel> mModelBeanList = new ArrayList<>();

    public StoryChainPresenterImpl(StoryChainView view) {
        mView = view;
    }

    @Override
    public void initStoryList(int start, int end, String storyId) {
        mModelBeanList.clear();
        SCHttpUtils.post()
                .url(HttpApi.STORY_CHAIN_ID)
                .addParams("start",""+start)
                .addParams("end",""+end)
                .addParams("storyId",storyId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取故事链条失败");
                        e.printStackTrace();
                        mView.getStoryListSuc(null, false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取故事链条成功 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                                String data = JSONObject.parseObject(response).getString("data");
                                List<StoryChainBean> chainBeanList = JSONObject.parseArray(data, StoryChainBean.class);
                                if (chainBeanList !=null&&chainBeanList.size()>0){
                                    int size = chainBeanList.size();
                                    int genNum = chainBeanList.get(size - 1).getGenNum();
                                    int startNum = chainBeanList.get(0).getGenNum();
                                    LogUtils.i("最后一个的gennun = " + genNum);
                                    LogUtils.i("第一条的gennum = " + startNum);
                                    boolean isLast;
                                    if (genNum == 1){
                                        isLast = true;
                                    }else {
                                        isLast = false;
                                    }
                                    for (int i = 0; i < chainBeanList.size(); i ++) {
                                        StoryChainModel model = new StoryChainModel();
                                        StoryChainBean storyChainBean = chainBeanList.get(i);
                                        model.setStoryBean(storyChainBean);
                                        model.setStoryId(storyChainBean.getStoryId());
                                        mModelBeanList.add(model);
                                    }
                                    obtainFav(isLast);
                                    //mView.getStoryListSuc(mModelBeanList,isLast);
                                }

                            }else { //返回码错误

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            //数据解析异常

                        }


                    }
                });
    }

    private void obtainFav(final boolean isLast) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < mModelBeanList.size(); i ++) {
            int id = mModelBeanList.get(i).getStoryId();
            ids.add(id);
        }

        String jArr = JSONObject.toJSONString(ids);

        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_ISFAV_LIST)
                .addParams("checkIdList",jArr)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取是否点赞失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取点赞成功 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                                String data = SCJsonUtils.parseData(response);
                                List<IsFavBean> favBeanList = JSONObject.parseArray(data, IsFavBean.class);
                                for (int i = 0; i < mModelBeanList.size(); i ++) {
                                    StoryChainModel model = mModelBeanList.get(i);
                                    for (IsFavBean bean : favBeanList){
                                       if (model.getStoryId() == bean.getStoryId()){
                                           model.setBeFav(bean.isCheck());
                                       }
                                    }
                                }
                                obtainCharacterInfo(isLast);
                            }else{

                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                });

    }

    private void obtainCharacterInfo(final boolean isLast) {
        List<Integer> characterIds = new ArrayList<>();
        for (int i = 0; i < mModelBeanList.size(); i ++) {
            StoryChainBean storyBean = mModelBeanList.get(i).getStoryBean();
            characterIds.add(storyBean.getCharacterId());
        }

        String jArr = JSONObject.toJSONString(characterIds);

        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_BRIEF)
                .addParams("dataArray",jArr)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取角色简要信息失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取到角色简要信息 = " + response);
                        String code = SCJsonUtils.parseCode(response);
                        if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                            String data = SCJsonUtils.parseData(response);
                            List<ContactBean> contactBeanList = JSONObject.parseArray(data, ContactBean.class);
                            for (int i = 0; i < mModelBeanList.size(); i ++) {
                                StoryChainModel model = mModelBeanList.get(i);
                                for (ContactBean bean : contactBeanList){
                                    if (model.getStoryBean().getCharacterId() == bean.getCharacterId()){
                                        model.setCharacterBean(bean);
                                    }
                                }
                            }

                            mView.getStoryListSuc(mModelBeanList,isLast);

                        }else{

                        }
                    }
                });


    }
}
