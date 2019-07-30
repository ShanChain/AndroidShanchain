package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.ui.presenter.TaskDetailPresenter;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.TaskDetailListView;


import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/7/20
 * Describe :
 */
public class TaskDetailPresenterImpl implements TaskDetailPresenter {
    private TaskDetailListView mTaskDetailListView;

    public TaskDetailPresenterImpl(TaskDetailListView taskDetailListView){
        this.mTaskDetailListView = taskDetailListView;
    }
    @Override
    public void getTaskListData(String characterId, String roomId, int page, int size,final int pullType) {
        mTaskDetailListView.showProgressStart();
        SCHttpUtils.getAndToken()
                .url(HttpApi.ALL_TASK_LIST)
                .addParams("characterId", characterId + "")
                .addParams("roomId", roomId + "")
                .addParams("page", page + "")
                .addParams("size", size + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("TaskPresenterImpl", "查询任务失败");
                        mTaskDetailListView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mTaskDetailListView.showProgressEnd();
                        mTaskDetailListView.setTaskDetailList(response,pullType);
                    }
                });
    }

    @Override
    public void getCurrency() {
        SCHttpUtils.get()
                .url(HttpApi.GET_SEAT_CURRENCY)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mTaskDetailListView.setCurrencyInfo(response);
                    }
                });
    }
}
