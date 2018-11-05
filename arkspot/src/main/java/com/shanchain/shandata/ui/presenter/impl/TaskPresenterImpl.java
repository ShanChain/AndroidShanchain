package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.ui.presenter.TaskPresenter;

import okhttp3.Call;

public class TaskPresenterImpl implements TaskPresenter{

    @Override
    public void initTask(int characterId, String squareId) {
        SCHttpUtils.postWithUserId()
                .url(HttpApi.GROUP_TASK_LIST)
                .addParams("squareId",squareId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });

    }

    @Override
    public void notFinishTask(int characterId, String squareId) {

    }

    @Override
    public void releaseTask(String squareId, String bounty, String dataString, String time) {
        SCHttpUtils.postWithUserId()
                .url(HttpApi.GROUP_TASK_add)
                .addParams("bounty",bounty)
                .addParams("squareId",squareId)
                .addParams("dataString",dataString) //任务内容
                .addParams("time",time)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("TaskPresenterImpl","添加任务失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("TaskPresenterImpl","添加任务成功");
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){


                        }
                    }
                });

    }

    @Override
    public void deleteTask(int taskId) {

    }

    @Override
    public void receiveTask(int characterId, String squareId, int taskId) {

    }

    @Override
    public void confirmTaskProgress(int status, int characterId, int taskId) {

    }

    @Override
    public void taskProgressNotice(int targetId, int taskId, int type) {

    }

    @Override
    public void queryUserReleaseTask(int characterId) {

    }

    @Override
    public void queryAllTaskByUser(int characterId) {

    }

    @Override
    public void queryAllFinishTaskByUser(int characterId) {

    }

    @Override
    public void cancelTask(int characterId, int taskId) {

    }
}