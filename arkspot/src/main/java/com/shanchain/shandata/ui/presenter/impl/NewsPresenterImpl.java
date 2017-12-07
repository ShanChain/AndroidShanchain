package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.encryption.SCJsonUtils;
import com.shanchain.shandata.manager.ConversationManager;
import com.shanchain.shandata.ui.model.ConversationInfo;
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
    private List<ConversationInfo> mConversationCache;

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

        if (hxGroup.size() > 0) {
            obtainHxGroupInfo(hxGroup);
        }

        if (hxUser.size() > 0) {
            obtainHxUserInfo(hxUser);
        }
    }

    @Override
    public void initConversationCache(List<MessageHomeInfo> sourceDatas) {
        if (sourceDatas == null || sourceDatas.size() == 0) {
            return;
        }

        List<ConversationInfo> conversationCache = ConversationManager.getInstance().getConversationCache(EMClient.getInstance().getCurrentUser());
        if (conversationCache == null || conversationCache.size() == 0) {   //没有缓存
            ConversationManager.getInstance().obtainConversationInfoFromServer();   //获取服务器的信息
            //轮询检测是否缓存完成
            LogUtils.i("没有缓存");
            checkConversationCache();
        } else {
            LogUtils.i("有缓存");
            if (sourceDatas.size() == conversationCache.size()) {
                for (int i = 0; i < sourceDatas.size(); i++) {
                    boolean has = ConversationManager.getInstance().hasHxUser(sourceDatas.get(i).getHxUser());
                    if (!has) {
                        ConversationManager.getInstance().obtainConversationInfoFromServer();
                        checkConversationCache();
                        break;
                    }
                }
                LogUtils.i("循环结束");
                mView.initCacheSuc(conversationCache);
            } else { //缓存的数据不同
                ConversationManager.getInstance().obtainConversationInfoFromServer();
                //轮询检测是否缓存完成
                checkConversationCache();
            }
        }
    }

    @Override
    public void updateCache(EMMessage message) {
        String userName = message.getUserName();
        String from = message.getFrom();
        String to = message.getTo();
        String img = "";
        String hxUser = "";
        LogUtils.i("接收消息  username = " + userName + "; from = " + from + "; to = " + to);
        if (message.getChatType() == EMMessage.ChatType.GroupChat){
            hxUser = to;
            img = message.getStringAttribute(Constants.MSG_GROUP_IMG,"");
        }else {
            hxUser = userName;
            img = message.getStringAttribute(Constants.MSG_HEAD_IMG,"");
        }

        boolean hasHxUser = ConversationManager.getInstance().hasHxUser(hxUser);
        if (hasHxUser){

        }else {

        }


    }

    private void checkConversationCache() {
        LogUtils.i("更新缓存");
        final String currentUser = EMClient.getInstance().getCurrentUser();
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 20; i++) {
                    try {
                        Thread.currentThread().sleep(500);
                        LogUtils.i("睡眠 = ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mConversationCache = ConversationManager.getInstance().getConversationCache(currentUser);
                    if (mConversationCache != null && mConversationCache.size() > 0) {
                        LogUtils.i("取到缓存数据");
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                LogUtils.i("去主线程更新");
                                mView.initCacheSuc(mConversationCache);
                            }
                        });

                        break;
                    }
                }
            }
        }).start();


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
