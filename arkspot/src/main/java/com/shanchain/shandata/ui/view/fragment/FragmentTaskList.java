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
import com.shanchain.shandata.ui.model.TaskMode;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskDetailActivity;
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


public class FragmentTaskList extends BaseFragment implements  SwipeRefreshLayout.OnRefreshListener {

    //    private String roomId = "15198852";
    private String roomId;

    @Bind(R.id.rv_task_list)
    RecyclerView rvTaskList;
    @Bind(R.id.srl_task_list)
    SwipeRefreshLayout srlTaskList;
    private MultiTaskListAdapter adapter;
    private ProgressDialog mDialog;
    private String characterId;
    private List<ChatEventMessage> taskList = new ArrayList();;

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
        srlTaskList.setOnRefreshListener(this);
        roomId = SCCacheUtils.getCacheRoomId();
        initRecyclerView();
    }

    private void initRecyclerView() {
        showLoadingDialog();

        for (int i = 2; i <20; i++) {
            ChatEventMessage chatEventMessage = new ChatEventMessage("测试", IMessage.MessageType.RECEIVE_TEXT.ordinal());
            chatEventMessage.setIntro("测试"+i);
            chatEventMessage.setTimeString("2018-12-10 12:12:12");
            chatEventMessage.setBounty("80");
            chatEventMessage.setName("测试"+i);
            chatEventMessage.setRoomName("1235");
            chatEventMessage.setExpiryTime(1542209938);
            if (i%2==0){
                chatEventMessage.setStatus(5);
            }else if(i==7){
                chatEventMessage.setStatus(10);
            }else {
                chatEventMessage.setStatus(20);
            }
//            taskList.add(chatEventMessage);
        }

        characterId = SCCacheUtils.getCacheCharacterId();
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
                            TaskMode taskMode = JSONObject.parseObject(data, TaskMode.class);

                            String content = JSONObject.parseObject(data).getString("content");
                            List<ChatEventMessage> chatEventMessageList = JSONObject.parseArray(content
                                    , ChatEventMessage.class);

                            for (int i = 0; i < chatEventMessageList.size(); i++) {

                                taskList.add(chatEventMessageList.get(i));

                            }

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            adapter = new MultiTaskListAdapter(getContext(), taskList,new int[]{
                                    R.layout.item_task_type_recevice, //所有任务，已被领取
                                    R.layout.item_task_type_six,//所有任务，查看/领取任务
                                    R.layout.item_task_type_two,//我的任务，未领取
//                                    R.layout.item_task_type_donging,//我的任务，对方正在完成
//                                    R.layout.item_task_type_four, //我的任务，去确认
                            });
                            rvTaskList.setLayoutManager(linearLayoutManager);
                            adapter.setHasStableIds(true);
                            rvTaskList.setAdapter(adapter);
                            closeLoadingDialog();

                            /*adapter.setOnClickListener(new MultiTaskListAdapter.OnClickListener() {
                                @Override
                                public void OnClick(ChatEventMessage item, View view, BaseViewHolder holder) {
                                    final LinearLayout front = view.findViewById(R.id.item_task_front);
                                    final LinearLayout back = view.findViewById(R.id.item_task_back);
                                    FrameLayout frame = view.findViewById(R.id.frame);
                                    int direction = 1;
                                    if (back.isShown()) {
                                        direction = -1;
                                    }
                                    ViewAnimUtils.flip(frame, 500, direction);
                                    frame.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            switchViewVisibility(back,front);
                                        }
                                    }, 500);

                                }

                            });*/
                        }
                    }
                });

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
////       TaskListAdapter taskAdapter = new TaskListAdapter(getContext(), taskList);
//        rvTaskList.setLayoutManager(linearLayoutManager);
//        rvTaskList.addOnItemTouchListener(new OnItemChildClickListener() {
//            @Override
//            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                switch (view.getId()){
//                    case R.id.btn_event_task:
//                        ToastUtils.showToast(getContext(),"sfgasfaj");
//
//                        break;
//                }
//
//            }
//        });
//
//        rvTaskList.setAdapter(taskAdapter);
       /* taskAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int viewType = adapter.getItemViewType(position);
                switch (viewType){
                    case ChatEventMessage.type5:
                        ToastUtils.showToast(getContext(),""+taskList.get(position).getIntro());
                        break;
                }
                    final LinearLayout front = view.findViewById(R.id.item_task_front);
                    final LinearLayout back = view.findViewById(R.id.item_task_back);
                    FrameLayout frame = view.findViewById(R.id.frame);
                    int direction = 1;
                    if (back.isShown()) {
                        direction = -1;
                    }
                    ViewAnimUtils.flip(frame, 500, direction);
                    frame.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            switchViewVisibility(back,front);
                        }
                    }, 500);
                    ToastUtils.showToast(getContext(), "点击item"+taskList.get(position).getIntro());

            }
        });*/
 /*       taskAdapter.setOnClickListener(new MultiTaskListAdapter.OnClickListener() {
            @Override
            public void OnClick(ChatEventMessage item, View view, BaseViewHolder holder,int position) {
            final LinearLayout front = view.findViewById(R.id.item_task_front);
            final LinearLayout back = view.findViewById(R.id.item_task_back);
            FrameLayout frame = view.findViewById(R.id.frame);
                int direction = 1;
                if (back.isShown()) {
                    direction = -1;
                }
                ViewAnimUtils.flip(frame, 500, direction);
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switchViewVisibility(back,front);
                    }
                }, 500);
                ChatEventMessage chatEventMessage = (ChatEventMessage)adapter.getHolder().getConvertView().getTag();
            ToastUtils.showToast(getContext(),item.getIntro()+ "点击item"+chatEventMessage.getIntro());
            }
        });*/
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

    private void switchViewVisibility(LinearLayout back,LinearLayout front) {
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
    public void taskListEventBus(Object event){

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
    public void refreshUseTask(String characterId){
        String roomId = SCCacheUtils.getCacheRoomId();
        SCHttpUtils.postWithUserId()
                .url(HttpApi.GROUP_TASK_LIST)
                .addParams("characterId", characterId + "")
                .addParams("roomId", roomId + "")
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
                        adapter = new MultiTaskListAdapter(getContext(), taskList, new int[]{
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

                        adapter.setOnItemClickListener(new MultiTaskListAdapter.OnItemClickListener() {
                            @Override
                            public void OnItemClick(ChatEventMessage item, final View view, final com.shanchain.shandata.adapter.BaseViewHolder holder, int position) {

                                ToastUtils.showToast(getContext(),"点击了查看按钮"+item.getIntro());
                                switch (view.getId()) {
                                    case R.id.item_task_urge:
                                        showProgress();
                                        SCHttpUtils.postWithUserId()
                                                .url(HttpApi.TASK_DETAIL_URGE)
                                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                                .addParams("taskId", taskList.get(position).getTaskId() + "")
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
                                                .addParams("taskId", taskList.get(position).getTaskId() + "")
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
                                                .addParams("taskId(", taskList.get(position).getTaskId() + "")
                                                .addParams("userId(", SCCacheUtils.getCacheUserId()+ "")
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
                                                .addParams("taskId", item.getTaskId() + "")
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
                                                .addParams("taskId", taskList.get(position).getTaskId() + "")
                                                .addParams("userId", SCCacheUtils.getCacheUserId() + "")
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
                                closeLoadingDialog();
                                closeProgress();
                            }
                        });

                    }
                });
    }


}
