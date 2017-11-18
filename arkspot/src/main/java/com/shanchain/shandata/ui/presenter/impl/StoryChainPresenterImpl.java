package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.presenter.StoryChainPresenter;
import com.shanchain.shandata.ui.view.activity.story.stroyView.StoryChainView;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/11/17.
 */

public class StoryChainPresenterImpl implements StoryChainPresenter {
    private StoryChainView mView;

    public StoryChainPresenterImpl(StoryChainView view) {
        mView = view;
    }

    @Override
    public void initStoryList(int start, int end, String storyId) {
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
                                List<StoryModelBean> modelBeanList = JSONObject.parseArray(data, StoryModelBean.class);
                                if (modelBeanList!=null){
                                    int size = modelBeanList.size();
                                    int genNum = modelBeanList.get(size - 1).getGenNum();
                                    int startNum = modelBeanList.get(0).getGenNum();
                                    LogUtils.i("最后一个的gennun = " + genNum);
                                    LogUtils.i("第一条的gennum = " + startNum);
                                    boolean isLast;
                                    if (genNum == 1){
                                        isLast = true;
                                    }else {
                                        isLast = false;
                                    }
                                    mView.getStoryListSuc(modelBeanList,isLast);
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
}
