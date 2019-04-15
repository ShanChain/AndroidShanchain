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
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MultiMyTaskAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.event.EventMessage;
import com.shanchain.shandata.ui.model.TaskMode;
import com.shanchain.shandata.ui.presenter.TaskPresenter;
import com.shanchain.shandata.ui.presenter.impl.TaskPresenterImpl;
import com.shanchain.shandata.ui.view.fragment.view.TaskView;
import com.shanchain.shandata.utils.ViewAnimUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jpush.im.android.eventbus.EventBus;
import okhttp3.Call;


public class FragmentTaskList extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, TaskView {
    //    private String roomId = "15198852";

    @Bind(R.id.rv_task_list)
    RecyclerView rvTaskList;
    @Bind(R.id.srl_task_list)
    SwipeRefreshLayout srlTaskList;
    private MultiMyTaskAdapter adapter;
    private ProgressDialog mDialog;
    private TaskPresenter taskPresenter;
    private String roomId;
    String characterId = SCCacheUtils.getCacheCharacterId();
    String userId = SCCacheUtils.getCacheUserId();
    private int page = 0;
    private int size = 20;
    private boolean isLoadMore = false;
    private List<ChatEventMessage> taskList = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        roomId = intent.getStringExtra("roomId") != null ?
                intent.getStringExtra("roomId") : SCCacheUtils.getCacheRoomId();
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
        //5未领取 10已领取/正在完成， //15领取方已确认完成
        //20发布方确认完成，21发布方确认任务未完成 22 任务超时 25领取方任务取消
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new MultiMyTaskAdapter(getContext(), taskList, new int[]{
                R.layout.item_task_type_two,//0我发布的，5:未领取
                R.layout.item_task_type_one,//1、我发布的，10.对方正在完成
                R.layout.item_task_type_three, //2、我发布的，15:去确认

                R.layout.item_task_type_four,//3、我领取的，10:正在完成
                R.layout.item_task_type_five,//4、我领取的，15:待赏主确认
                R.layout.item_task_type_donging,//5、我领取的,20发布方确认任务完成
                R.layout.item_task_type_donging2,//6、我领取的,21发布方确认任务未完成
                R.layout.item_task_type_over_time,//7、22:过期的
                R.layout.item_task_type_recevice,//8、25：任务已被取消
//                R.layout.item_task_type_donging_fnish, //对方收到赏金
//                R.layout.item_task_type_donging_unfnish, //退回赏金
        });

        SCHttpUtils.postWithUserId()
                .url(HttpApi.MY_TASK_PUBLISH_LIST)
                .addParams("characterId", characterId + "")
                .addParams("page", page + "")
                .addParams("pageSize", size + "")
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
                        closeLoadingDialog();
                        closeProgress();
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            LogUtils.d("TaskPresenterImpl", "添加任务成功");
                            String data = JSONObject.parseObject(response).getString("data");
                            TaskMode taskMode = JSONObject.parseObject(data, TaskMode.class);

                            String content = JSONObject.parseObject(data).getString("content");
                            List<ChatEventMessage> chatEventMessageList = JSONObject.parseArray(content, ChatEventMessage.class);

                            for (int i = 0; i < chatEventMessageList.size(); i++) {
                                taskList.add(chatEventMessageList.get(i));
                            }
                        }

                        rvTaskList.setLayoutManager(linearLayoutManager);
//                        adapter.setHasStableIds(true);
                        rvTaskList.setAdapter(adapter);
                        adapter.setOnClickListener(new MultiMyTaskAdapter.OnClickListener() {

                            @Override
                            public void OnClick(ChatEventMessage item, View view, com.shanchain.shandata.adapter.BaseViewHolder holder, int position) {
                                final LinearLayout front = view.findViewById(R.id.item_task_front);
                                final LinearLayout back = view.findViewById(R.id.item_task_back);
//                                ToastUtils.showToast(getContext(), taskList.get(position).getTaskId() + "内容："+taskList.get(position).getIntro());
                                FrameLayout frame = view.findViewById(R.id.frame);
                                int direction = 1;
                                if (back.isShown()) {
                                    direction = -1;
                                }
                                ViewAnimUtils.flip(frame, 500, direction);
                                frame.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        switchViewVisibility(back, front);
                                    }
                                }, 500);
                            }
                        });
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
        if (taskList.size() < 0) {
            return;
        }
        initData();
        adapter.upData(taskList);
        adapter.notifyDataSetChanged();
        rvTaskList.setAdapter(adapter);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void taskListEventBus(EventMessage event) {
        if (event.getCode() == NetErrCode.REFRESH_MY_TASK) {
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
