package com.shanchain.shandata.ui.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.BaseViewHolder;
import com.shanchain.shandata.adapter.MultiMyTaskAdapter;
import com.shanchain.shandata.adapter.MultiTaskListAdapter;
import com.shanchain.shandata.adapter.TaskListAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.event.EventMessage;
import com.shanchain.shandata.ui.model.TaskMode;
import com.shanchain.shandata.ui.presenter.TaskPresenter;
import com.shanchain.shandata.ui.presenter.impl.TaskPresenterImpl;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskDetailActivity;
import com.shanchain.shandata.ui.view.fragment.view.TaskView;
import com.shanchain.shandata.utils.ViewAnimUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.eventbus.EventBus;
import okhttp3.Call;


public class FragmentTaskList extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, TaskView {
    //    private String roomId = "15198852";

    @Bind(R.id.rv_task_list)
    RecyclerView rvTaskList;
    @Bind(R.id.srl_task_list)
    SwipeRefreshLayout srlTaskList;
    private MultiTaskListAdapter adapter;
    private ProgressDialog mDialog;
    private TaskPresenter taskPresenter;
    private String roomId = SCCacheUtils.getCacheRoomId();
    ;
    String characterId = SCCacheUtils.getCacheCharacterId();
    String userId = SCCacheUtils.getCacheUserId();
    private int page = 0;
    private int size = 10;
    private boolean isLoadMore = false;
    private List<ChatEventMessage> taskList = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public View initView() {
        return View.inflate(getContext(), R.layout.fragment_task_list, null);
    }

    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        taskList.clear();
        taskPresenter = new TaskPresenterImpl(this);
        taskPresenter.initTask(characterId, roomId, page, size);
        srlTaskList.setOnRefreshListener(this);//下拉刷新
        initRecyclerView();
    }

    private void initRecyclerView() {

        for (int i = 2; i < 20; i++) {
            ChatEventMessage chatEventMessage = new ChatEventMessage("测试", IMessage.MessageType.RECEIVE_TEXT.ordinal());
            chatEventMessage.setIntro("测试" + i);
            chatEventMessage.setTimeString("2018-12-10 12:12:12");
            chatEventMessage.setBounty("80");
            chatEventMessage.setName("测试" + i);
            chatEventMessage.setRoomName("1235");
            chatEventMessage.setExpiryTime(1542209938);
            if (i % 2 == 0) {
                chatEventMessage.setStatus(5);
            } else if (i == 7) {
                chatEventMessage.setStatus(10);
            } else {
                chatEventMessage.setStatus(20);
            }
//            taskList.add(chatEventMessage);
        }

        SCHttpUtils.postWithUserId()
                .url(HttpApi.GROUP_TASK_LIST)
                .addParams("characterId", characterId + " ")
                .addParams("roomId", roomId + " ")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("TaskPresenterImpl", "查询任务失败");
                        closeLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            LogUtils.d("TaskPresenterImpl", "查询任务成功");
                            String data = JSONObject.parseObject(response).getString("data");
                            String content = JSONObject.parseObject(data).getString("content");

                            List<ChatEventMessage> chatEventMessageList = JSONObject.parseArray(content
                                    , ChatEventMessage.class);
                            for (int i = 0; i < chatEventMessageList.size(); i++) {
                                taskList.add(chatEventMessageList.get(i));
                            }
                            //构造Adapter
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            adapter = new MultiTaskListAdapter(getContext(), taskList, new int[]{
                                    R.layout.item_task_list_type1, //所有任务，已被领取
                                    R.layout.item_task_list_type2,//所有任务，查看/领取任务
                                    R.layout.item_task_type_two,//我的任务，未领取
//                                    R.layout.item_task_type_donging,//我的任务，对方正在完成
//                                    R.layout.item_task_type_four, //我的任务，去确认
                            });
                            rvTaskList.setLayoutManager(linearLayoutManager);
                            adapter.setHasStableIds(true);
                            rvTaskList.setAdapter(adapter);
                            closeLoadingDialog();
                        }
                    }
                });
    }

    /*
     * 下拉刷新
     * */
    @Override
    public void onRefresh() {
//        taskPresenter.initTask(characterId, roomId, page, size);
//        taskList.clear();
        initData();
        srlTaskList.setRefreshing(false);
    }

    /*
     * 上拉加载
     * */


    public void showProgress() {
        mDialog = new ProgressDialog(getContext());
        mDialog.setMax(100);
        mDialog.setMessage("数据请求中..");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public void closeProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
        }

    }

    private void switchViewVisibility(LinearLayout back, LinearLayout front) {
        if (back.isShown()) {
            back.setVisibility(View.GONE);
            front.setVisibility(View.VISIBLE);
        } else {
            back.setVisibility(View.VISIBLE);
            front.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void taskListEventBus(EventMessage event) {
        if (event.getCode()==1){
            showProgress();
            onRefresh();
//            ToastUtils.showToast(getContext(),"Evenbus执行");
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void initTask(List<ChatEventMessage> list, boolean isSuccess) {
        closeProgress();
//        taskList.addAll(list);
    }

    @Override
    public void initUserTaskList(List<ChatEventMessage> list, boolean isSuccess) {

    }

    @Override
    public void addSuccess(boolean success) {

    }

    @Override
    public void releaseTaskView(boolean isSuccess) {

    }

    @Override
    public void supportSuccess(boolean isSuccess, int position) {

    }

    @Override
    public void supportCancelSuccess(boolean isSuccess, int position) {

    }

    @Override
    public void deleteTaskView(boolean isSuccess, int position) {

    }
}
