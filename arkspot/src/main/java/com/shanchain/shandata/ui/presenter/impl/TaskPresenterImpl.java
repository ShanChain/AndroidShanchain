package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.ui.presenter.TaskPresenter;
import com.shanchain.shandata.ui.view.fragment.view.TaskView;

import java.util.ArrayList;
import java.util.List;

import cn.jiguang.imui.model.ChatEventMessage;
import okhttp3.Call;

public class TaskPresenterImpl implements TaskPresenter {

    private TaskView taskView;
    public TaskPresenterImpl taskPresenter;

    public TaskPresenterImpl(TaskView taskView) {
        this.taskView = taskView;
    }

    @Override
    public void initTask(String characterId, String roomId) {
        SCHttpUtils.postWithUserId()
                .url(HttpApi.GROUP_TASK_LIST)
                .addParams("characterId", characterId)
                .addParams("roomId", roomId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            LogUtils.d("TaskPresenterIml", "请求任务列表i成功" +response);
                            String data = JSONObject.parseObject(response).getString("data");
                            String content = JSONObject.parseObject(data).getString("content");
                            List<ChatEventMessage> taskList = JSONObject.parseArray(content, ChatEventMessage.class);

                            taskView.initTask(taskList, true);
                        } else {
                            taskView.initTask(null, false);
                        }
                    }
                });

    }

    @Override
    public void notFinishTask(int characterId, String squareId) {

    }

    @Override
    public void releaseTask(String characterId, String roomId, String bounty, String dataString, String time) {

        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHAT_TASK_ADD)
                .addParams("characterId",characterId+"")
                .addParams("bounty",bounty+"")
                .addParams("roomId",roomId+"")
                .addParams("dataString",dataString+"") //任务内容
                .addParams("time",time+"")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("TaskPresenterImpl","添加任务失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                            LogUtils.d("TaskPresenterImpl","添加任务成功");
//                            taskView.releaseTaskView();
                        }
                    }
                });
//        SCHttpUtils.postNoToken()
//                .url(HttpApi.CHAT_TASK_ADD)
//                .addParams("token", "3_18de26a8218e4251b41932ef4de4ea491541670201558")
//                .addParams("bounty", bounty)
//                .addParams("currency","rmb")
//                .addParams("roomId", roomId)
//                .addParams("characterId", characterId)
//                .addParams("dataString", dataString) //任务内容
//                .addParams("time", time)
//                .build()
//                .execute(new SCHttpStringCallBack() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        LogUtils.d("TaskPresenterImpl", "添加任务失败");
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        String code = JSONObject.parseObject(response).getString("code");
//                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
////                            taskView.releaseTaskView();
//                            LogUtils.d("TaskPresenterImpl", "添加任务成功");
//                        }
//                    }
//                });
//        ;

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