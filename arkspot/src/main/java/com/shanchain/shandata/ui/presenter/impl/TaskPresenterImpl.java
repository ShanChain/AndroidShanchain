package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.ui.model.TaskMode;
import com.shanchain.shandata.ui.presenter.TaskPresenter;
import com.shanchain.shandata.ui.view.fragment.view.TaskView;

import java.util.ArrayList;
import java.util.List;

import cn.jiguang.imui.model.ChatEventMessage;
import okhttp3.Call;

import static com.alibaba.fastjson.JSON.parseObject;

public class TaskPresenterImpl implements TaskPresenter {

    private TaskView taskView;
    public TaskPresenterImpl taskPresenter;
    private List<ChatEventMessage> taskList = new ArrayList<>();
    List<ChatEventMessage> chatEventMessageList = new ArrayList<>();

    public TaskPresenterImpl(TaskView taskView) {
        this.taskView = taskView;
    }

    @Override
    public void initTask(String characterId, String roomId,int page,int size) {
        taskList.clear();
        SCHttpUtils.postWithUserId()
                .url(HttpApi.GROUP_TASK_LIST)
                .addParams("characterId", characterId)
                .addParams("roomId", roomId)
                .addParams("page", ""+page)
                .addParams("size", ""+size)
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
                            taskList = JSONObject.parseArray(content, ChatEventMessage.class);

                            taskView.initTask(taskList, true);
                        } else {
                            taskView.initTask(null, false);
                        }
                    }
                });

    }

    @Override
    public void initUserTaskList(String characterId, int page, int size) {
        chatEventMessageList.clear();
        SCHttpUtils.postWithUserId()
                .url(HttpApi.USER_TASK_LIST)
                .addParams("characterId", characterId + "")
                .addParams("page", page + "")
                .addParams("size", size + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("TaskPresenterImpl", "查询任务失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            LogUtils.d("TaskPresenterImpl", "添加任务成功");
                            String data = JSONObject.parseObject(response).getString("data");
                            TaskMode taskMode = JSONObject.parseObject(data, TaskMode.class);

                            String content = JSONObject.parseObject(data).getString("content");
                            chatEventMessageList = JSONObject.parseArray(content, ChatEventMessage.class);
                            taskView.initUserTaskList(chatEventMessageList,true);
                        }else {
                            taskView.initUserTaskList(null,false);
                        }
                    }
                });
    }

    @Override
    public void notFinishTask(int characterId, String squareId) {

    }

    /*
    * 发布任务
    *
    * */
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
                            taskView.releaseTaskView(true);
                        }else {
                            taskView.releaseTaskView(false);
                        }
                    }
                });

    }

    /*
    * 删除任务
    * */
    @Override
    public void deleteTask(int taskId) {

    }

    /*
    * 领取任务
    * */
    @Override
    public void receiveTask(int characterId, String squareId, int taskId) {

    }

    /*
    * 确认完成任务
    * */
    @Override
    public void confirmTaskProgress(int status, int characterId, int taskId) {

    }

    /*
    * */
    @Override
    public void taskProgressNotice(int targetId, int taskId, int type) {

    }

    @Override
    public void queryReleaseTaskByUserId(int characterId) {

    }



    @Override
    public void queryAllFinishTaskByUser(int characterId) {

    }

    @Override
    public void cancelTask(String characterId, String taskId) {

    }

    /*
    * 添加任务评论
    * */
    @Override
    public void addTaskComment(String characterId,String taskId, String comment) {

        String dataString = "{\"content\": \"" + comment + "\",\"isAnon\":0}";
        SCHttpUtils.postWithUserId()
                .url(HttpApi.TASK_COMMENT_ADD)
                .addParams("characterId",characterId)
                .addParams("taskId",taskId)
                .addParams("dataString",dataString)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        taskView.addSuccess(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        String data = JSONObject.parseObject(response).getString("data");
                       if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            taskView.addSuccess(true);
                        } else {
                           taskView.addSuccess(false);
                        }
                    }
                });
    }
}