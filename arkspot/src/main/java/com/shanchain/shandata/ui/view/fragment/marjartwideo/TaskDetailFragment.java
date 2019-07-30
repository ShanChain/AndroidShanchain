package com.shanchain.shandata.ui.view.fragment.marjartwideo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.EventBusObject;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.ui.widgets.timepicker.SCTimePickerView;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MultiTaskListAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.presenter.TaskDetailPresenter;
import com.shanchain.shandata.ui.presenter.impl.TaskDetailPresenterImpl;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskDetailActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskListActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.TaskDetailListView;
import com.shanchain.shandata.widgets.dialog.CustomDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jiguang.imui.model.MyMessage;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/7/19
 * Describe :社区帮
 */
public class TaskDetailFragment extends BaseFragment implements ArthurToolBar.OnRightClickListener,
        SwipeRefreshLayout.OnRefreshListener , TaskDetailListView {
    @Bind(R.id.tb_task_comment)
    ArthurToolBar tbTaskComment;
    @Bind(R.id.linear_add_task)
    LinearLayout linearAddTask;
    @Bind(R.id.tv_empty_word)
    TextView tvEmptyWord;
    @Bind(R.id.rv_task_details)
    RecyclerView rvTaskDetails;
    @Bind(R.id.srl_task_list)
    SwipeRefreshLayout srlTaskList;
    @Bind(R.id.tv_task_details_comment)
    TextView tvTaskDetailsComment;

    private String characterId = SCCacheUtils.getCacheCharacterId();
    private String roomId = SCCacheUtils.getCacheRoomId();
    private View footerView;
    private String roomid;
    private int pageNo = 1;
    private List<ChatEventMessage> taskList = new ArrayList();
    private MultiTaskListAdapter adapter;
    private TaskDetailPresenter mTaskDetailPresenter;
    private boolean isLast = false;//是否最后一页
    private double seatRmbRate = 10.00;
    private CustomDialog taskDialog;
    private TextView tvSeatRate;
    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    private EditText mBountyEditText,limitedTime,mDescribeEditText;
    private String formatDate;
    private SCTimePickerView scTimePickerView;
    private SCTimePickerView.OnTimeSelectListener onTimeSelectListener;
    private long timeStamp;
    private ChatEventMessage chatEventMessage1;
    private Handler handler, dialogHandler;
    private com.shanchain.data.common.ui.widgets.CustomDialog showPasswordDialog;
    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.activity_task_detail, null);
    }

    public static TaskDetailFragment newInstance() {
        TaskDetailFragment fragment = new TaskDetailFragment();
        return fragment;
    }

    @Override
    public void initData() {
        srlTaskList.setOnRefreshListener(this);
        srlTaskList.setColorSchemeColors(getResources().getColor(R.color.login_marjar_color),
                getResources().getColor(R.color.register_marjar_color),getResources().getColor(R.color.google_yellow));
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_load_more, null);
        mTaskDetailPresenter = new TaskDetailPresenterImpl(this);
        initToolBar();
        initLoadMoreListener();
    }

    private void initToolBar() {
        tbTaskComment.setTitleText(getResources().getString(R.string.shequbang));
        tbTaskComment.setTitleTextColor(Color.BLACK);
        tbTaskComment.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        tbTaskComment.setLeftTitleLayoutView(View.INVISIBLE);
        tbTaskComment.setRightText(getString(R.string.my_));
        tbTaskComment.setOnRightClickListener(this);

        //构造Adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new MultiTaskListAdapter(getActivity(), taskList, new int[]{
                R.layout.item_task_list_type1, //所有任务，已被领取
                R.layout.item_task_list_type2,//所有任务，查看/领取任务
                R.layout.item_task_type_two,//我的任务，未领取
        });
        rvTaskDetails.setLayoutManager(linearLayoutManager);
        adapter.setHasStableIds(true);
        rvTaskDetails.setAdapter(adapter);

        dialogHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    ToastUtils.showToastLong(getActivity(), getString(R.string.yue_not_enough));
                } else if (msg.what == 2) {
                    ToastUtils.showToastLong(getActivity(), getString(R.string.network_wrong));
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        mTaskDetailPresenter.getTaskListData(characterId,roomId,pageNo, Constants.pageSize,Constants.pullRefress);
    }

    @Override
    public void onRefresh() {
        pageNo  = 1;
        mTaskDetailPresenter.getTaskListData(characterId,roomId,pageNo, Constants.pageSize,Constants.pullRefress);
    }

    @Override
    public void onRightClick(View v) {
        Intent intent = new Intent(getActivity(), TaskListActivity.class);
        startActivity(intent);
    }

    //添加帮助
    @OnClick(R.id.linear_add_task)
    void addHelp(){
        //获取当前的最新汇率
        mTaskDetailPresenter.getCurrency();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(EventBusObject busObject) {
        try {
            busObject = (EventBusObject) busObject;
            showPasswordDialog = (com.shanchain.data.common.ui.widgets.CustomDialog) busObject.getData();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        if (NetErrCode.WALLET_PHOTO == busObject.getCode()) {
//                        创建上传密码图片弹窗
            if (showPasswordDialog == null) {
                return;
            }
            /*if (showPasswordDialog.getContext() != TaskDetailActivity.this) {
                return;
            }*/
            showPasswordDialog.setOnItemClickListener(new com.shanchain.data.common.ui.widgets.CustomDialog.OnItemClickListener() {
                @Override
                public void OnItemClick(com.shanchain.data.common.ui.widgets.CustomDialog dialog, View view) {
                    if (view.getId() == com.shanchain.common.R.id.iv_dialog_add_picture) {
                        selectImage(ActivityStackManager.getInstance().getTopActivity());
                    } else if (view.getId() == com.shanchain.common.R.id.tv_dialog_sure) {
                        ToastUtils.showToastLong(ActivityStackManager.getInstance().getTopActivity(), getString(R.string.upload_qr_code));
                    }
                }
            });
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    showPasswordDialog.setPasswordBitmap(null);
                    showPasswordDialog.show();
                }
            });

        }
    }


    @Override
    public void showProgressStart() {
        showLoadingDialog(false);
    }

    @Override
    public void showProgressEnd() {
        closeLoadingDialog();
    }

    @Override
    public void setTaskDetailList(String response,int pullType) {
        srlTaskList.setRefreshing(false);
        String code = JSONObject.parseObject(response).getString("code");
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            LogUtils.d("TaskPresenterImpl", "查询任务成功");
            String data = JSONObject.parseObject(response).getString("data");
            String content = JSONObject.parseObject(data).getString("content");
            String isL = JSONObject.parseObject(response).getString("last");
            isLast  = Boolean.valueOf(isL);
            List<ChatEventMessage> chatEventMessageList = JSONObject.parseArray(content
                    , ChatEventMessage.class);
            if (chatEventMessageList.size() <= 0) {
                tvEmptyWord.setVisibility(View.VISIBLE);
            }
            if(pullType == Constants.pullRefress){
                taskList.clear();
                taskList.addAll(chatEventMessageList);
                rvTaskDetails.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else {
                adapter.addData(chatEventMessageList);
            }

        }
    }
    //返回汇率信息
    @Override
    public void setCurrencyInfo(String response) {
        String code = JSONObject.parseObject(response).getString("code");
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            String data = JSONObject.parseObject(response).getString("data");
            String rate = JSONObject.parseObject(data).getString("rate");//汇率，获取当前汇率
            seatRmbRate = Double.valueOf(rate);
            initTaskDialog();
        }
    }

    //上拉加载
    private void initLoadMoreListener() {
        rvTaskDetails.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(isLast){
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()){
                    pageNo ++;
                    mTaskDetailPresenter.getTaskListData(characterId,roomId,pageNo,Constants.pageSize,Constants.pillLoadmore);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    //初始化弹窗信息
    private void initTaskDialog(){
        final int[] idItems = new int[]{R.id.et_input_dialog_describe, R.id.dialog_select_task_time, R.id.et_input_dialog_bounty, R.id.iv_dialog_close, R.id.btn_dialog_input_sure, R.id.iv_dialog_close};
        taskDialog = new CustomDialog(getActivity(), false, 1.0, R.layout.common_dialog_chat_room_task, idItems);
        View layout = View.inflate(getActivity(), R.layout.common_dialog_chat_room_task, null);
        taskDialog.setView(layout);
        taskDialog.setAddTextChangedListener(new CustomDialog.OnAddTextChangedListener() {
            @Override
            public void TextChanged(CustomDialog dialog, EditText editText, String s, int start, int before, int count) {
                tvSeatRate = (TextView) dialog.getByIdView(R.id.seatRate);
                if (s.length() > 0) {
                    final Double bounty = Double.valueOf(s);
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            tvSeatRate.setText(("≈ " + decimalFormat.format(bounty / seatRmbRate)) + " SEAT");
                        }
                    });
                }
            }
        });
        taskDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(final CustomDialog dialog, View view) {
                mDescribeEditText = (EditText) dialog.getByIdView(R.id.et_input_dialog_describe);
                mBountyEditText = (EditText) dialog.getByIdView(R.id.et_input_dialog_bounty);
                limitedTime = (EditText) dialog.getByIdView(R.id.dialog_select_task_time);
                switch (view.getId()) {
                    case R.id.et_input_dialog_describe:
                        break;
                    case R.id.et_input_dialog_bounty:
                        break;
                    case R.id.btn_dialog_input_sure:
                        showLoadingDialog(true);
                        releaseTask(SCCacheUtils.getCacheAuthCode(), mDescribeEditText, mBountyEditText, limitedTime);
                        break;
                    case R.id.iv_dialog_close:
                        dialog.dismiss();
                        break;
                    //选择时间
                    case R.id.dialog_select_task_time:
                        if (scTimePickerView != null) {
                            scTimePickerView.dismiss();
                        }
                        initPickerView();
                        scTimePickerView.setOnTimeSelectListener(onTimeSelectListener);
                        scTimePickerView.setOnCancelClickListener(new SCTimePickerView.OnCancelClickListener() {
                            @Override
                            public void onCancelClick(View v) {

                            }
                        });
                        scTimePickerView.show(limitedTime);
                        break;
                }
            }
        });
        taskDialog.show();
        handler = new Handler() {
            @Override
            public void handleMessage(final android.os.Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        ToastUtils.showToast(getActivity(), R.string.publish_success);
                        taskDialog.dismiss();
                        break;
                }
            }
        };

    }

    //初始化时间选择弹窗
    private void initPickerView() {
        SCTimePickerView.Builder builder = new SCTimePickerView.Builder(getActivity());
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        int year = startDate.get(Calendar.YEAR);
        startDate.set(year, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE), selectedDate.get(Calendar.HOUR), selectedDate.get(Calendar.MINUTE));//设置起始年份
        Calendar endDate = Calendar.getInstance();
        endDate.set(year + 10, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE), selectedDate.get(Calendar.HOUR), selectedDate.get(Calendar.MINUTE));//设置结束年份
        builder.setType(new boolean[]{true, true, true, true, true, false})//设置显示年、月、日、时、分、秒
                .setDecorView((ViewGroup) getActivity().findViewById(android.R.id.content).getRootView())
//                .setDecorView((ViewGroup) dialog.getWindow().getDecorView().getRootView())
                .isCenterLabel(true)
                .setLabel(getString(R.string.year), getString(R.string.month), getString(R.string.day), getString(R.string.hour), getString(R.string.minute), getString(R.string.second))
                .setCancelText(getString(R.string.clean))
                .setCancelColor(getActivity().getResources().getColor(com.shanchain.common.R.color.colorDialogBtn))
                .setSubmitText(getString(R.string.finish))
                .setRangDate(startDate, endDate)
                .setSubCalSize(15)
                .setTitleSize(15)
                .setContentSize(15)
                .setTitleBgColor(getActivity().getResources().getColor(com.shanchain.common.R.color.colorWhite))
                .setSubmitColor(getActivity().getResources().getColor(com.shanchain.common.R.color.colorDialogBtn))
                .isDialog(true)
                .build();

        scTimePickerView = new SCTimePickerView(builder);
        scTimePickerView.setDate(Calendar.getInstance());

        onTimeSelectListener = new SCTimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formatDate = simpleDateFormat.format(date);
                timeStamp = date.getTime();
                LogUtils.d("SCTimePickerView", "点击了SCTimePickerView" + formatDate);
                TextView clickView = (TextView) v;
                clickView.setText(formatDate);
                scTimePickerView.show(limitedTime);

            }
        };
    }
    /*
     * 发布任务
     *
     * */
    private void releaseTask(String authCode, EditText describeEditText, EditText bountyEditText, TextView textViewTime) {
        if (TextUtils.isEmpty(describeEditText.getText().toString()) || TextUtils.isEmpty(bountyEditText.getText().toString()) || TextUtils.isEmpty(textViewTime.getText().toString())) {
            ToastUtils.showToast(getActivity(), getString(R.string.toast_no_empty));
            closeLoadingDialog();
        } else {
            final String spaceId = SCCacheUtils.getCacheSpaceId();//获取当前的空间ID
            final String bounty = bountyEditText.getText().toString();
            final String dataString = describeEditText.getText().toString();
            final String LimitedTtime = textViewTime.getText().toString();

            final String characterId = SCCacheUtils.getCacheCharacterId();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            final String createTime = simpleDateFormat.format(LimitedTtime);
            //向服务器请求添加任务
            if (timeStamp < System.currentTimeMillis() + 60 * 60 * 1000) {
                ToastUtils.showToastLong(getActivity(), getString(R.string.one_hour_lage));
                closeLoadingDialog();
                return;
            }
            showPasswordDialog = new com.shanchain.data.common.ui.widgets.CustomDialog(getActivity(), true, 1.0,
                    R.layout.dialog_bottom_wallet_password,
                    new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
            SCHttpUtils.postWithUserId()
                    .url(HttpApi.CHAT_TASK_ADD)
                    .addParams("authCode", "" + authCode)
                    .addParams("deviceToken", JPushInterface.getRegistrationID(getActivity().getApplicationContext()))
                    .addParams("characterId", characterId + "")
                    .addParams("bounty", bounty)
                    .addParams("roomId", roomId + "")
                    .addParams("dataString", dataString + "") //任务内容
                    .addParams("time", timeStamp + "")
                    .build()
                    .execute(new SCHttpStringCallBack(getActivity(), showPasswordDialog) {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.d("TaskPresenterImpl", "添加任务失败");
                            chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                            MyMessage myMessage = new MyMessage(IMessage.MessageType.EVENT.ordinal());
                            myMessage.setChatEventMessage(chatEventMessage1);
//                            mAdapter.addToStart(myMessage, true);
                            closeLoadingDialog();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            closeLoadingDialog();
                            String code = JSONObject.parseObject(response).getString("code");
                            final String message = JSONObject.parseObject(response).getString("message");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                String task = JSONObject.parseObject(data).getString("Task");
                                chatEventMessage1 = JSONObject.parseObject(task, ChatEventMessage.class);
                                handler.sendEmptyMessage(0);
                            } else if (NetErrCode.BALANCE_NOT_ENOUGH.equals(code)) {   //余额不足
                                closeLoadingDialog();
                                dialogHandler.sendEmptyMessage(1);
                            } else {
                                closeLoadingDialog();
                            }
                        }
                    });
        }
    }

}
