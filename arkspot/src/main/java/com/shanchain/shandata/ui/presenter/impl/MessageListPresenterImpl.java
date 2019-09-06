package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.shandata.ui.model.SearchGroupTeamBeam;
import com.shanchain.shandata.ui.presenter.MessageListPresenter;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.view.MessageListView;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/9/6
 * Describe :
 */
public class MessageListPresenterImpl implements MessageListPresenter {
    private MessageListView mView;

    public MessageListPresenterImpl(MessageListView view){
        this.mView = view;
    }

    @Override
    public void queryMineRoomNums(String roomId) {
        SCHttpUtils.post()
                .url(HttpApi.CHECK_MMINING_ROOM_NUMS)
                .addParams("roomId",roomId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mView.setQueryMineRoomNumsResponse(response);
                    }
                });
    }

    @Override
    public void checkUserIsSuper() {
        SCHttpUtils.get()
                .url(HttpApi.SUPER_USER + "?token=" + SCCacheUtils.getCacheToken())
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("####### USER_COORDINATE 请求失败 #######");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mView.setCheckUserIsSuperResponse(response);
                    }
                });
    }
}
