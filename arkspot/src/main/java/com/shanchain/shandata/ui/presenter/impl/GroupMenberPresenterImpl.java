package com.shanchain.shandata.ui.presenter.impl;

import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.ui.presenter.GroupMenberPresenter;
import com.shanchain.shandata.ui.view.activity.jmessageui.MemberActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.view.GroupMenberView;

import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/9/4
 * Describe :
 */
public class GroupMenberPresenterImpl implements GroupMenberPresenter {
    private GroupMenberView mGroupMenberView;
    public GroupMenberPresenterImpl(GroupMenberView groupMenberView){
        this.mGroupMenberView = groupMenberView;
    }
    @Override
    public void getGroupMenberList(String roomId, String count, String page, String size, final int pullType) {
        mGroupMenberView.showProgressStart();
        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHAT_ROOM_MEMBER)
                .addParams("roomId", roomId)
                .addParams("count", count + "")
                .addParams("page", page + "")
                .addParams("size", size + "")
                .addParams("token", SCCacheUtils.getCacheToken() + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mGroupMenberView.showProgressEnd();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mGroupMenberView.showProgressEnd();
                        mGroupMenberView.setGroupMenberResponse(response,pullType);
                    }

                });
    }

    @Override
    public void checkIsGroupCreater(String roomId) {
        //判断是否为群主
        SCHttpUtils.get()
                .url(HttpApi.ROOM_OWNER)
                .addParams("roomId", roomId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mGroupMenberView.setCheckGroupCreateeResponse(response);
                    }
                });
    }

    @Override
    public void deleteGroupMenber(String rooId, String menbers) {
        mGroupMenberView.showProgressStart();
        SCHttpUtils.post()
                .url(HttpApi.DELETE_ROOM_MEMBERS)
                .addParams("roomId", rooId)
                .addParams("jArray", menbers)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mGroupMenberView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mGroupMenberView.showProgressEnd();
                        mGroupMenberView.setDeleteGroupMenberResponse(response);
                    }
                });
    }

    @Override
    public void getRoomMenbers(String roomId) {
        mGroupMenberView.showProgressStart();
        SCHttpUtils.postNoToken()
                .url(HttpApi.CHECK_ADD_MMINING_ROOM)
                .addParams("diggingsId", roomId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mGroupMenberView.showProgressEnd();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mGroupMenberView.showProgressEnd();
                        mGroupMenberView.setGroupMenberListResponse(response);
                    }

                });
    }
}
