package com.shanchain.shandata.ui.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MultiMyTaskAdapter;
import com.shanchain.shandata.adapter.MultiTaskListAdapter;
import com.shanchain.shandata.adapter.TaskListAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.model.TaskMode;
import com.shanchain.shandata.ui.presenter.TaskPresenter;
import com.shanchain.shandata.ui.presenter.impl.TaskPresenterImpl;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskListActivity;
import com.shanchain.shandata.ui.view.fragment.view.TaskView;
import com.shanchain.shandata.utils.ViewAnimUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.eventbus.EventBus;
import okhttp3.Call;


/**
 * Created by zhoujian on 2017/8/23.
 */

public class FragmentMyTask extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, TaskView {

//    private String roomId = "15198852";

    @Bind(R.id.rv_task_list)
    RecyclerView rvTaskList;
    @Bind(R.id.srl_task_list)
    SwipeRefreshLayout srlTaskList;

    private List<ChatEventMessage> taskList = new ArrayList();
    ;
    private MultiMyTaskAdapter adapter;
    private View.OnClickListener taskStatusListener;
    private ProgressDialog mDialog;
    private TaskPresenter taskPresenter;
    private String roomId;
    String characterId = SCCacheUtils.getCacheCharacterId();
    String userId = SCCacheUtils.getCacheUserId();
    private int page = 0;
    private int size = 10;
    private boolean isLoadMore = false;


    @Override
    public View initView() {
        return View.inflate(getContext(), R.layout.fragment_task_list, null);
    }

    @Override
    public void initData() {
        taskPresenter = new TaskPresenterImpl(this);
        roomId = SCCacheUtils.getCacheRoomId();
        taskList.clear();
        taskPresenter.initUserTaskList(characterId, page, size);
        srlTaskList.setOnRefreshListener(this);

//        ToastUtils.showToastLong(getContext(),SCCacheUtils.getCacheCharacterId());
        for (int i = 2; i < 20; i++) {
            ChatEventMessage chatEventMessage = new ChatEventMessage("测试", IMessage.MessageType.RECEIVE_TEXT.ordinal());
            chatEventMessage.setIntro("测试" + i);
            chatEventMessage.setTimeString("2018-12-10 12:12:12");
            chatEventMessage.setBounty("80");
            chatEventMessage.setName("测试" + i);
            chatEventMessage.setRoomName("1235");
            chatEventMessage.setExpiryTime(1542209938);
            if (i == 0) {
                chatEventMessage.setStatus(5);
            } else if (i == 1) {
                chatEventMessage.setStatus(15);
            } else if (i == 2) {
                chatEventMessage.setStatus(20);
            } else if (i == 3) {
                chatEventMessage.setStatus(21);
            } else if (i == 4) {
                chatEventMessage.setStatus(22);
            } else {
                chatEventMessage.setStatus(25);
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
                .url(HttpApi.USER_TASK_LIST)
                .addParams("characterId", characterId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("TaskPresenterImpl", "查询任务失败");
                        closeLoadingDialog();
                        closeProgress();
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

        adapter.setOnItemClickListener(new MultiMyTaskAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ChatEventMessage item, View view, com.shanchain.shandata.adapter.BaseViewHolder holder, int position) {
                switch (view.getId()){
                    case R.id.btn_event_task:
                        final LinearLayout front = holder.getConvertView().findViewById(R.id.item_task_front);
                        final LinearLayout back = holder.getConvertView().findViewById(R.id.item_task_back);
//              ToastUtils.showToast(getContext(), taskList.get(position).getTaskId() + "内容："+taskList.get(position).getIntro());
                        FrameLayout frame = holder.getConvertView().findViewById(R.id.frame);
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
                        break;
                }
            }
        });
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void myTaskEvent(Object event) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    /*
     * 下拉刷新
     * */
    @Override
    public void onRefresh() {
//        isLoadMore = false;
//        taskPresenter.initUserTaskList(characterId, page, size);
        showProgress();
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

    @Override
    public void initTask(List<ChatEventMessage> list, boolean isSuccess) {
//        taskList.addAll(list);

    }

    @Override
    public void initUserTaskList(List<ChatEventMessage> list, boolean isSuccess) {
//        taskList.addAll(list);
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
