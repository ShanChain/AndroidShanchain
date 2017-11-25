package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.encryption.SCJsonUtils;
import com.shanchain.shandata.ui.model.MessageHomeInfo;
import com.shanchain.shandata.ui.model.NewsCharacterBean;
import com.shanchain.shandata.ui.model.NewsGroupBean;
import com.shanchain.shandata.ui.presenter.NewsPresenter;
import com.shanchain.shandata.ui.view.fragment.view.NewsView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/11/24.
 */

public class NewsPresenterImpl implements NewsPresenter {
    private NewsView mView;

    public NewsPresenterImpl(NewsView view) {
        mView = view;
    }

    private List<MessageHomeInfo> datas;

    @Override
    public void initConversationInfo(List<MessageHomeInfo> sourceDatas) {
        datas = sourceDatas;
        List<String> hxGroup = new ArrayList<>();
        List<String> hxUser = new ArrayList<>();
        for (int i = 0; i < sourceDatas.size(); i++) {
            MessageHomeInfo messageHomeInfo = sourceDatas.get(i);
            String hxUserName = messageHomeInfo.getHxUser();
            boolean group = messageHomeInfo.getEMConversation().isGroup();
            if (group) {
                hxGroup.add(hxUserName);
            } else {
                hxUser.add(hxUserName);
            }
        }

        if (hxGroup.size() > 0){
            obtainHxGroupInfo(hxGroup);
        }

        if (hxUser.size()>0){
            obtainHxUserInfo(hxUser);
        }
    }

    private void obtainHxUserInfo(List<String> hxUser) {

        String jArr = JSONObject.toJSONString(hxUser);

        SCHttpUtils.post()
                .url(HttpApi.HX_USER_USERNAME_LIST)
                .addParams("jArray", jArr)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取环信用户角色信息失败");
                        e.printStackTrace();
                        mView.initCharacterSuc(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到环信用户角色信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                List<NewsCharacterBean> characterBeanList = JSONObject.parseArray(data, NewsCharacterBean.class);
                                if (characterBeanList != null && characterBeanList.size() > 0) {
                                    mView.initCharacterSuc(characterBeanList);
                                } else {
                                    mView.initCharacterSuc(null);
                                }


                            } else {
                                mView.initCharacterSuc(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initCharacterSuc(null);
                        }


                    }
                });
    }

    private void obtainHxGroupInfo(List<String> hxGroup) {

        String jArr = JSONObject.toJSONString(hxGroup);

        SCHttpUtils.post()
                .url(HttpApi.HX_GROUP_LIST)
                .addParams("jArray", jArr)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("获取环信群组信息失败");
                        e.printStackTrace();
                        mView.initGroupInfoSuc(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取环信群组信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                List<NewsGroupBean> newsGroupBeanList = JSONObject.parseArray(data, NewsGroupBean.class);
                                if (newsGroupBeanList != null && newsGroupBeanList.size() > 0) {
                                    mView.initGroupInfoSuc(newsGroupBeanList);
                                } else {
                                    mView.initGroupInfoSuc(null);
                                }
                            } else {
                                mView.initGroupInfoSuc(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initGroupInfoSuc(null);
                        }
                    }
                });

    }
}
