package com.shanchain.shandata.ui.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
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

public class FragmentMyTask extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private String roomId;
//    private String roomId = "15198852";

    @Bind(R.id.rv_task_list)
    RecyclerView rvTaskList;
    @Bind(R.id.srl_task_list)
    SwipeRefreshLayout srlTaskList;

    private List<ChatEventMessage> taskList = new ArrayList();;
    private MultiMyTaskAdapter adapter;
    private View.OnClickListener taskStatusListener;
    private ProgressDialog mDialog;


    @Override
    public View initView() {
        return View.inflate(getContext(), R.layout.fragment_task_list, null);
    }

    public void refreshUseTask(String characterId){
        SCHttpUtils.postWithUserId()
                .url(HttpApi.USER_TASK_LIST)
                .addParams("characterId", characterId + "")
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
                            LogUtils.d("TaskPresenterImpl", "添加任务成功");
                            String data = JSONObject.parseObject(response).getString("data");
                            TaskMode taskMode = JSONObject.parseObject(data, TaskMode.class);

                            String content = JSONObject.parseObject(data).getString("content");
                            List<ChatEventMessage> chatEventMessageList = JSONObject.parseArray(content, ChatEventMessage.class);

                            for (int i = 0; i < chatEventMessageList.size(); i++) {
                                taskList.add(chatEventMessageList.get(i));
                            }
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        adapter = new MultiMyTaskAdapter(getContext(), taskList, new int[]{
                                R.layout.item_task_type_two,//我的任务，未领取
                                R.layout.item_task_type_four, //我的任务，领取任务去确认完成
                                R.layout.item_task_type_donging,//我的任务，对方正在完成
                        });
                        rvTaskList.setLayoutManager(linearLayoutManager);
                        adapter.setHasStableIds(true);
                        rvTaskList.setAdapter(adapter);
                        closeLoadingDialog();

                       /* adapter.setOnClickListener(new MultiMyTaskAdapter.OnClickListener() {

                            @Override
                            public void OnClick(ChatEventMessage item, View view, com.shanchain.shandata.adapter.BaseViewHolder holder,int position) {
                                final LinearLayout front = view.findViewById(R.id.item_task_front);
                                final LinearLayout back = view.findViewById(R.id.item_task_back);
                                ToastUtils.showToast(getContext(),""+item.getIntro());
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

                                TextView taskReleaseTime = (TextView) view.findViewById(R.id.task_release_time);
                                TextView taskReceiveTime = (TextView) view.findViewById(R.id.task_receive_time);
                                TextView taskFinishTime = (TextView) view.findViewById(R.id.task_finish_time);
                                Button undone = (Button) view.findViewById(R.id.item_task_undone);
                                Button done = (Button) view.findViewById(R.id.item_task_done);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                switch (taskList.get(position).getStatus()) {
                                    case 5:
                                        String releaseTime = sdf.format(new Date(item.getCreateTime()));
                                        taskReleaseTime.setText("发布时间：" + releaseTime);
                                        break;
                                    case 10:
                                        String receiveTime = sdf.format(new Date(item.getReceiveTime()));
                                        taskReceiveTime.setVisibility(View.VISIBLE);
                                        taskReceiveTime.setText("领取时间：" + receiveTime);
                                        break;
                                    case 15:
                                        done.setVisibility(View.VISIBLE);
                                        undone.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                        });*/

                        adapter.setOnItemClickListener(new MultiMyTaskAdapter.OnItemClickListener() {
                            @Override
                            public void OnItemClick(ChatEventMessage item, final View view, final com.shanchain.shandata.adapter.BaseViewHolder holder, int position) {

                                ToastUtils.showToast(getContext(),"点击了查看按钮"+item.getIntro());
                                switch (view.getId()) {
                                    case R.id.item_task_urge:
                                        showProgress();
                                        SCHttpUtils.postWithUserId()
                                                .url(HttpApi.TASK_DETAIL_URGE)
                                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                                .addParams("targetId", taskList.get(position).getTaskId() + "")
                                                .build()
                                                .execute(new SCHttpStringCallBack() {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {

                                                    }

                                                    @Override
                                                    public void onResponse(String response, int id) {
                                                        TextView release = view.getRootView().findViewById(R.id.task_release_time);
                                                        TextView receive = view.getRootView().findViewById(R.id.task_receive_time);
                                                        TextView finish = view.getRootView().findViewById(R.id.task_finish_time);
//                                                        receive.setText("发布时间：");
                                                        adapter.notifyDataSetChanged();
                                                        closeProgress();
                                                    }
                                                });
                                        break;
                                    case R.id.btn_event_confirm:
                                        showProgress();
                                        SCHttpUtils.postWithUserId()
                                                .url(HttpApi.TASK_DETAIL_ACCOMPLISH)
                                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                                .addParams("targetId", taskList.get(position).getTaskId() + "")
                                                .build()
                                                .execute(new SCHttpStringCallBack() {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {
                                                        closeProgress();
                                                    }

                                                    @Override
                                                    public void onResponse(String response, int id) {
                                                        TextView release = view.getRootView().findViewById(R.id.task_release_time);
                                                        TextView receive = view.getRootView().findViewById(R.id.task_receive_time);
                                                        TextView finish = view.getRootView().findViewById(R.id.task_finish_time);
//                                                        receive.setText("发布时间：");
                                                        adapter.notifyDataSetChanged();
                                                        showProgress();
                                                    }
                                                });

                                        break;
                                    case R.id.item_task_done:
                                        showProgress();
                                        SCHttpUtils.postWithUserId()
                                                .url(HttpApi.TASK_DETAIL_DONE)
                                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                                .addParams("targetId", taskList.get(position).getTaskId() + "")
                                                .build()
                                                .execute(new SCHttpStringCallBack() {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {
                                                        closeProgress();
                                                    }

                                                    @Override
                                                    public void onResponse(String response, int id) {
                                                        TextView release = view.getRootView().findViewById(R.id.task_release_time);
                                                        TextView receive = view.getRootView().findViewById(R.id.task_receive_time);
                                                        TextView finish = view.getRootView().findViewById(R.id.task_finish_time);
//                                                        receive.setText("发布时间：");
                                                        adapter.notifyDataSetChanged();
                                                        closeProgress();
                                                    }
                                                });
                                        break;
                                    case R.id.item_task_undone:
                                        showProgress();
                                        SCHttpUtils.postWithUserId()
                                                .url(HttpApi.TASK_DETAIL_UNDONE)
                                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                                .addParams("targetId", item.getTaskId() + "")
                                                .build()
                                                .execute(new SCHttpStringCallBack() {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {
                                                        closeProgress();
                                                    }

                                                    @Override
                                                    public void onResponse(String response, int id) {
                                                        TextView release = view.getRootView().findViewById(R.id.task_release_time);
                                                        TextView receive = view.getRootView().findViewById(R.id.task_receive_time);
                                                        TextView finish = view.getRootView().findViewById(R.id.task_finish_time);
//                                                        receive.setText("发布时间：");
                                                        adapter.notifyDataSetChanged();
                                                        closeProgress();

                                                    }
                                                });
                                        break;
                                    case R.id.item_task_cancel:
                                        showProgress();
                                        SCHttpUtils.postWithUserId()
                                                .url(HttpApi.TASK_DETAIL_CANCEL)
                                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                                .addParams("targetId", taskList.get(position).getTaskId() + "")
                                                .build()
                                                .execute(new SCHttpStringCallBack() {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {
                                                        closeProgress();
                                                    }

                                                    @Override
                                                    public void onResponse(String response, int id) {
                                                        TextView release = view.getRootView().findViewById(R.id.task_release_time);
                                                        TextView receive = view.getRootView().findViewById(R.id.task_receive_time);
                                                        TextView finish = view.getRootView().findViewById(R.id.task_finish_time);
//                                                        receive.setText("发布时间：");
                                                        adapter.notifyDataSetChanged();
                                                        closeProgress();
                                                    }
                                                });
                                        break;
                                }
                            }
                        });

                    }
                });
    }

    @Override
    public void initData() {
        roomId = SCCacheUtils.getCacheRoomId();
        srlTaskList.setOnRefreshListener(this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new MultiMyTaskAdapter(getContext(), taskList, new int[]{
                R.layout.item_task_type_two,//我的任务，未领取
                R.layout.item_task_type_four, //我的任务，领取任务去确认完成/取消
                R.layout.item_task_type_five,//我的任务，对方正在完成
                R.layout.item_task_type_donging ,//确认对已完成/未完成
                R.layout.item_task_type_four_finish, //等待对方确认完成
                R.layout.item_task_type_recevice ,//任务已被领取
                R.layout.item_task_type_donging_fnish, //对方收到赏金
                R.layout.item_task_type_donging_unfnish, //退回赏金
                R.layout.item_task_type_three, //已过期

        });

        final String characterId = SCCacheUtils.getCacheCharacterId();
        SCHttpUtils.postWithUserId()
                .url(HttpApi.USER_TASK_LIST)
                .addParams("characterId", characterId + "")
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
                        adapter.setHasStableIds(true);
                        rvTaskList.setAdapter(adapter);

                        adapter.setOnClickListener(new MultiMyTaskAdapter.OnClickListener() {

                            @Override
                            public void OnClick(ChatEventMessage item, View view, com.shanchain.shandata.adapter.BaseViewHolder holder,int position) {
                                final LinearLayout front = view.findViewById(R.id.item_task_front);
                                final LinearLayout back = view.findViewById(R.id.item_task_back);
                                ToastUtils.showToast(getContext(),adapter.getPosition()+"");
//                                FrameLayout frame = view.findViewById(R.id.frame);
//                                int direction = 1;
//                                if (back.isShown()) {
//                                    direction = -1;
//                                }
//                                ViewAnimUtils.flip(frame, 500, direction);
//                                frame.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        switchViewVisibility(back, front);
//                                    }
//                                }, 500);


                            }
                        });


                    }
                });


//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, true);
//        TaskListAdapter adapter = new TaskListAdapter(getContext(), taskList);
//        rvTaskList.setLayoutManager(linearLayoutManager);
//        rvTaskList.setAdapter(adapter);

    }

    /*
     * 卡片监听状态
     * */
    private void taskStatusClickLisener(final ChatEventMessage chatEventMessage, final TextView completeTimeView) {
        taskStatusListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatEventMessage == null) {
                    return;
                }
                String characterId = SCCacheUtils.getCacheCharacterId();
                String userId = SCCacheUtils.getCacheUserId();
                String taskId = chatEventMessage.getTaskId() + "";

                switch (v.getId()) {
                    case R.id.item_task_undone:
                        SCHttpUtils.postWithUserId()
                                .url(HttpApi.TASK_DETAIL_UNDONE)
                                .addParams("characterId", characterId)
                                .addParams("taskId", taskId)
                                .addParams("userId", userId)
                                .build()
                                .execute(new SCHttpStringCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                    }
                                });
                        break;
                    case R.id.item_task_done:
                        SCHttpUtils.postWithUserId()
                                .url(HttpApi.TASK_DETAIL_DONE)
                                .addParams("characterId", characterId)
                                .addParams("taskId", taskId)
                                .addParams("userId", userId)
                                .build()
                                .execute(new SCHttpStringCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {

                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        String code = JSONObject.parseObject(response).getString("code");
                                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                            String data = JSONObject.parseObject(response).getString("data");
                                            String completeTime = JSONObject.parseObject(data).getString("CompleteTime");
                                            completeTimeView.setText(completeTime);
                                        }

                                    }
                                });
                        break;
                }
            }
        };
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

    @Override
    public void onRefresh() {
        initData();
        srlTaskList.setRefreshing(false);
    }

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
}
