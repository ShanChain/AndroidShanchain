package com.shanchain.shandata.ui.view.activity.tasklist;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MultiTaskListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.event.EventMessage;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.presenter.TaskPresenter;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.SingerChatInfoActivity;
import com.shanchain.shandata.widgets.dialog.CustomDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.commons.models.IUser;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jiguang.imui.model.DefaultUser;
import cn.jiguang.imui.model.MyMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TaskDetailActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener,
        ArthurToolBar.OnLeftClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.tb_task_comment)
    ArthurToolBar tbTaskComment;
    @Bind(R.id.rv_task_details)
    RecyclerView rvTaskDetails;
    @Bind(R.id.tv_task_details_comment)
    TextView tvTaskDetailsComment;
    @Bind(R.id.srl_task_list)
    SwipeRefreshLayout srlTaskList;
    private MultiTaskListAdapter adapter;

    private List<ChatEventMessage> taskList = new ArrayList();
    private BaseQuickAdapter taskCommentAdapter;
    private View mHeadView;
    private TaskPresenter taskPresenter;
    private TextView mTvHeadComment;
    private TextView mTvHeadLike;
    private ChatEventMessage detailsChatEventMessage;
    private CharacterInfo characterInfo;
    private DefaultUser defaultUser;
    private LinearLayout liAddTask;
    private Handler handler, dialogHandler;
    private double seatRmbRate = 10.00;
    private TextView tvSeatRate;
    private EditText limitedTime;
    private Thread addTaskThread;
    private TextView tvEmptyWord;
    private long timeStamp;
    private SCTimePickerView scTimePickerView;
    private SCTimePickerView.OnTimeSelectListener onTimeSelectListener;
    private ChatEventMessage chatEventMessage1;
    private String formatDate, roomID;
    private Conversation chatRoomConversation;
    private int page = 0;
    private int size = 10;
    private boolean last = false, isLoadMore = false;
    private String characterId = SCCacheUtils.getCacheCharacterId();
    private String userId = SCCacheUtils.getCacheUserId();
    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    private com.shanchain.data.common.ui.widgets.CustomDialog showPasswordDialog;
    private CustomDialog taskDialog;
    private EditText mBountyEditText;
    private EditText mDescribeEditText;
    private String mAuthCode;
    private File mPasswordFile;
    private StandardDialog mStandardDialog;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_task_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        detailsChatEventMessage = (ChatEventMessage) getIntent().getSerializableExtra("chatEventMessage");
//        ToastUtils.showToast(TaskDetailActivity.this, "" + getIntent().getStringExtra("roomId"));
        roomID = getIntent().getStringExtra("roomId") != null ? getIntent().getStringExtra("roomId") : SCCacheUtils.getCacheRoomId();
        chatRoomConversation = JMessageClient.getChatRoomConversation(Long.valueOf(roomID));
        if (null == chatRoomConversation) {
            chatRoomConversation = Conversation.createChatRoomConversation(Long.valueOf(roomID));
        }
        initToolBar();
        initView();
        initData();
        initLoadMoreListener();
    }

    private void initView() {
        liAddTask = findViewById(R.id.linear_add_task);
        tvEmptyWord = findViewById(R.id.tv_empty_word);
        dialogHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    ToastUtils.showToastLong(TaskDetailActivity.this, "求助发布失败，您的余额不足");
                } else if (msg.what == 2) {
                    ToastUtils.showToastLong(TaskDetailActivity.this, "求助发布失败，网络连接错误");
                }
            }
        };

        liAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTaskDialog();
            }
        });
    }

    private void initHeadView(final ChatEventMessage chatEventMessage) {
        characterInfo = JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(), CharacterInfo.class);
        mHeadView = View.inflate(this, R.layout.item_task_details_head, null);
        ImageView ivAvatar = (ImageView) mHeadView.findViewById(R.id.iv_item_story_avatar);
        TextView tvName = (TextView) mHeadView.findViewById(R.id.tv_item_story_name);
        TextView tvTime = (TextView) mHeadView.findViewById(R.id.tv_item_story_time);
        TextView tvLastTime = (TextView) mHeadView.findViewById(R.id.even_message_last_time);
        TextView bounty = (TextView) mHeadView.findViewById(R.id.even_message_bounty);
        TextView tvContent = (TextView) mHeadView.findViewById(R.id.even_message_content);

        final Button btnEvenTask = mHeadView.findViewById(R.id.btn_event_task);


        //获取角色信息
        characterInfo = JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(), CharacterInfo.class);
        String characterImg = characterInfo.getHeadImg();
        String characterName = characterInfo.getName();

        if (chatEventMessage == null) {
            return;
        }
        if (chatEventMessage.getHeadImg() != null && chatEventMessage.getName() != null) {
            GlideUtils.load(mContext, chatEventMessage.getHeadImg(), ivAvatar, 0);//加载头像
            tvName.setText(chatEventMessage.getName() == null ? "无昵称" : chatEventMessage.getName());
        } else {
            GlideUtils.load(mContext, characterInfo.getHeadImg(), ivAvatar, 0);//加载头像
            tvName.setText(characterInfo.getName() == null ? "无昵称" : characterInfo.getName());
        }
        bounty.setText("赏金：SEAT " + chatEventMessage.getBounty());
        tvContent.setText(chatEventMessage.getIntro() + "");
        mTvHeadLike.setText(chatEventMessage.getSupportCount() + "");
        mTvHeadComment.setText(chatEventMessage.getCommentCount() + "");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm MM-dd");
        String createTime = sdf1.format(chatEventMessage.getPublishTime());
        String expiryTime = sdf.format(chatEventMessage.getExpiryTime());
//        String expiryTime = DateUtils.formatFriendly(new Date(chatEventMessage.getExpiryTime()));

        tvTime.setText(createTime + "");
        tvLastTime.setText("完成时限：" + expiryTime + "");


        //领取任务
        if (chatEventMessage.getStatus() >= 10) {
            btnEvenTask.setText("已被领取");
            btnEvenTask.setFocusable(false);
            btnEvenTask.setOnClickListener(null);
            btnEvenTask.setTextColor(getResources().getColor(R.color.aurora_bg_edittext_default));
        } else {
            btnEvenTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //领取任务请求
                    //上传密码图片弹窗
                    showPasswordDialog = new com.shanchain.data.common.ui.widgets.CustomDialog(TaskDetailActivity.this, true, 1.0,
                            R.layout.dialog_bottom_wallet_password,
                            new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
                    SCHttpUtils.postWithUserId()
                            .url(HttpApi.TASK_DETAIL_RECEIVE)
                            .addParams("roomId", roomID + "")
                            .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                            .addParams("taskId", chatEventMessage.getTaskId() + "")
                            .build()
                            .execute(new SCHttpStringCallBack(TaskDetailActivity.this, showPasswordDialog) {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtils.showToast(TaskDetailActivity.this, "网络异常");
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    String code = JSONObject.parseObject(response).getString("code");
                                    if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                        String data = JSONObject.parseObject(response).getString("data");
                                        final String HxUserName = JSONObject.parseObject(data).getString("HxUserName");
                                        btnEvenTask.setText("已被领取");
                                        btnEvenTask.setFocusable(false);
                                        btnEvenTask.setOnClickListener(null);
                                        btnEvenTask.setTextColor(getResources().getColor(R.color.aurora_bg_edittext_default));
                                        if (chatEventMessage.getFromUser() != null) {
                                            IUser user = chatEventMessage.getFromUser();
                                            String displayName = chatEventMessage.getFromUser().getDisplayName();
                                            String avatar = user.getAvatarFilePath() != null ? user.getAvatarFilePath() : "";
                                            defaultUser = new DefaultUser(user.getId(), displayName, avatar);
                                            defaultUser.setHxUserId(HxUserName);
                                        }
                                    }
                                }
                            });
                    final int[] idItems = new int[]{R.id.btn_dialog_task_detail_sure, R.id.iv_dialog_close};
                    final CustomDialog dialog = new CustomDialog(TaskDetailActivity.this, false, 1.0, R.layout.common_dialog_task_receive, idItems);
                    dialog.show();
                    TextView messageContent = (TextView) dialog.getByIdView(R.id.even_message_content);
                    TextView lastTime = (TextView) dialog.getByIdView(R.id.tv_task_detail_last_time);
                    messageContent.setText(chatEventMessage.getIntro() + "");
                    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm");
                    String expiryTime = sdf.format(chatEventMessage.getExpiryTime());
                    lastTime.setText(expiryTime + "");
                    EventBus.getDefault().post(new EventMessage<ChatEventMessage>(NetErrCode.REFRESH_MY_TASK));
                    dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                        @Override
                        public void OnItemClick(final CustomDialog dialog, View view) {
                            switch (view.getId()) {
                                case R.id.btn_dialog_task_detail_sure:
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("userInfo", defaultUser);
                                    bundle.putString("hxUserId", defaultUser.getHxUserId());
                                    readyGo(SingerChatInfoActivity.class, bundle);
                                    dialog.dismiss();
                                    break;
                                case R.id.iv_dialog_close:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    });

                }

            });

        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TaskDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        adapter = new MultiTaskListAdapter(TaskDetailActivity.this, taskList, new int[]{
                R.layout.item_task_list_type1, //所有任务，已被领取
                R.layout.item_task_list_type2,//所有任务，查看/领取任务
                R.layout.item_task_type_two,//我的任务，未领取
//              R.layout.item_task_type_donging,//我的任务，对方正在完成
//              R.layout.item_task_type_four, //我的任务，去确认
        });
        rvTaskDetails.setLayoutManager(linearLayoutManager);
        adapter.setHasStableIds(true);
        rvTaskDetails.setAdapter(adapter);

    }

    private void initData() {
        srlTaskList.setOnRefreshListener(this);
        showLoadingDialog(true);
//        SCHttpUtils.postWithUserId()
//                .url(HttpApi.GROUP_TASK_LIST)
        SCHttpUtils.getAndToken()
                .url(HttpApi.ALL_TASK_LIST)
                .addParams("characterId", characterId + "")
                .addParams("roomId", roomID + "")
                .addParams("page", page + "")
                .addParams("size", size + "")
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
                            if (chatEventMessageList.size() <= 0) {
                                tvEmptyWord.setVisibility(View.VISIBLE);
                            }
                            for (int i = 0; i < chatEventMessageList.size(); i++) {
                                taskList.add(chatEventMessageList.get(i));
                            }
                            //构造Adapter
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TaskDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                            adapter = new MultiTaskListAdapter(TaskDetailActivity.this, taskList, new int[]{
                                    R.layout.item_task_list_type1, //所有任务，已被领取
                                    R.layout.item_task_list_type2,//所有任务，查看/领取任务
                                    R.layout.item_task_type_two,//我的任务，未领取
//                                    R.layout.item_task_type_donging,//我的任务，对方正在完成
//                                    R.layout.item_task_type_four, //我的任务，去确认
                            });
                            rvTaskDetails.setLayoutManager(linearLayoutManager);
                            adapter.setHasStableIds(true);
                            rvTaskDetails.setAdapter(adapter);
                            closeLoadingDialog();
                        }
                    }
                });
    }

    private void initToolBar() {
//        tbTaskComment.isShowChatRoom(false);//不在导航栏显示聊天室信息
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT
//        );
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tbTaskComment.setTitleText(getResources().getString(R.string.shequbang));
        tbTaskComment.setTitleTextColor(Color.BLACK);
//        tbTaskComment.getTitleView().setLayoutParams(layoutParams);
        tbTaskComment.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        tbTaskComment.setOnLeftClickListener(this);
        tbTaskComment.setRightText("我的");
        tbTaskComment.setOnRightClickListener(this);

    }

    /*
     * 初始化弹窗
     *
     * */
    private void initTaskDialog() {
        SCHttpUtils.get()
                .url(HttpApi.GET_SEAT_CURRENCY)
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
                            String rate = JSONObject.parseObject(data).getString("rate");//汇率，获取当前汇率
                            String currency = JSONObject.parseObject(data).getString("currency");
                            seatRmbRate = Double.valueOf(rate);
                        }
                    }
                });

        final int[] idItems = new int[]{R.id.et_input_dialog_describe, R.id.dialog_select_task_time, R.id.et_input_dialog_bounty, R.id.iv_dialog_close, R.id.btn_dialog_input_sure, R.id.iv_dialog_close};
        taskDialog = new CustomDialog(TaskDetailActivity.this, false, 1.0, R.layout.common_dialog_chat_room_task, idItems);
        View layout = View.inflate(TaskDetailActivity.this, R.layout.common_dialog_chat_room_task, null);
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
//                bountyEditText.setFocusable(true);
                mDescribeEditText = (EditText) dialog.getByIdView(R.id.et_input_dialog_describe);
                mBountyEditText = (EditText) dialog.getByIdView(R.id.et_input_dialog_bounty);
//                mBountyEditText.requestFocus();
//                tvSeatRate = (TextView) dialog.getByIdView(R.id.seatRate);
                limitedTime = (EditText) dialog.getByIdView(R.id.dialog_select_task_time);
//                tvSeatRate.setText("= 0 SEAT");
                switch (view.getId()) {
                    case R.id.et_input_dialog_describe:
//                        ToastUtils.showToast(TaskDetailActivity.this, getResources().getString(R.string.my_task_release_des_hint));
                        break;
                    case R.id.et_input_dialog_bounty:
//                        mBountyEditText.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                            }
//
//                            @Override
//                            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                if (s.toString().length() > 0) {
//                                    final Double bounty = Double.valueOf(s.toString());
//                                    ThreadUtils.runOnMainThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            tvSeatRate.setText(("≈ " + decimalFormat.format(bounty / seatRmbRate)) + " SEAT");
//                                        }
//                                    });
//
//                                }
//                            }
//
//                            @Override
//                            public void afterTextChanged(Editable s) {
//                            }
//                        });
//                        ToastUtils.showToast(TaskDetailActivity.this, getResources().getString(R.string.my_task_release_reward_hint));
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
                        ToastUtils.showToast(mContext, "发布成功");
                        taskDialog.dismiss();
                        break;
                }
            }
        };

    }

    //初始化时间选择弹窗
    private void initPickerView() {
        SCTimePickerView.Builder builder = new SCTimePickerView.Builder(TaskDetailActivity.this);
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        int year = startDate.get(Calendar.YEAR);
        Date date = new Date(System.currentTimeMillis());
        int hour = date.getHours();
//        startDate.set(year, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE));//设置起始年份
        startDate.set(year, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE), selectedDate.get(Calendar.HOUR), selectedDate.get(Calendar.MINUTE));//设置起始年份
        Calendar endDate = Calendar.getInstance();
        endDate.set(year + 10, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE), selectedDate.get(Calendar.HOUR), selectedDate.get(Calendar.MINUTE));//设置结束年份
        builder.setType(new boolean[]{true, true, true, true, true, false})//设置显示年、月、日、时、分、秒
                .setDecorView((ViewGroup) findViewById(android.R.id.content).getRootView())
//                .setDecorView((ViewGroup) dialog.getWindow().getDecorView().getRootView())
                .isCenterLabel(true)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setCancelText("清除")
                .setCancelColor(TaskDetailActivity.this.getResources().getColor(com.shanchain.common.R.color.colorDialogBtn))
                .setSubmitText("完成")
                .setRangDate(startDate, endDate)
                .setSubCalSize(15)
                .setTitleSize(15)
                .setContentSize(15)
                .setTitleBgColor(TaskDetailActivity.this.getResources().getColor(com.shanchain.common.R.color.colorWhite))
                .setSubmitColor(TaskDetailActivity.this.getResources().getColor(com.shanchain.common.R.color.colorDialogBtn))
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
//                if (timeStamp < System.currentTimeMillis()+60*60*1000){
//                    ToastUtils.showToastLong(TaskDetailActivity.this,"必须选择大于当前时间1小时");
//                }
            }
        };
    }

    /*
     * 发布任务
     *
     * */
    private void releaseTask(String authCode, EditText describeEditText, EditText bountyEditText, TextView textViewTime) {
        if (TextUtils.isEmpty(describeEditText.getText().toString()) || TextUtils.isEmpty(bountyEditText.getText().toString()) || TextUtils.isEmpty(textViewTime.getText().toString())) {
            ToastUtils.showToast(TaskDetailActivity.this, "请输入完整信息");
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
                ToastUtils.showToastLong(TaskDetailActivity.this, "必须选择大于当前时间1小时");
                closeLoadingDialog();
                return;
            }
            showPasswordDialog = new com.shanchain.data.common.ui.widgets.CustomDialog(TaskDetailActivity.this, true, 1.0,
                    R.layout.dialog_bottom_wallet_password,
                    new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
            SCHttpUtils.postWithUserId()
                    .url(HttpApi.CHAT_TASK_ADD)
                    .addParams("authCode", "" + authCode)
                    .addParams("deviceToken", registrationId + "")
                    .addParams("characterId", characterId + "")
                    .addParams("bounty", bounty)
                    .addParams("roomId", roomID + "")
                    .addParams("dataString", dataString + "") //任务内容
                    .addParams("time", timeStamp + "")
                    .build()
                    .execute(new SCHttpStringCallBack(mContext, showPasswordDialog) {
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
//                                dialogHandler.sendEmptyMessage(2);
                            }
                        }
                    });
        }
    }

    @OnClick(R.id.tv_task_details_comment)
    public void onClick() {

    }

    @Override
    public void onLeftClick(View v) {
        finish();
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
//                ToastUtils.showToast(mContext, "" + NetErrCode.WALLET_PHOTO);
                return;
            }
            if (showPasswordDialog.getContext() != TaskDetailActivity.this) {
                return;
            }
            showPasswordDialog.setOnItemClickListener(new com.shanchain.data.common.ui.widgets.CustomDialog.OnItemClickListener() {
                @Override
                public void OnItemClick(com.shanchain.data.common.ui.widgets.CustomDialog dialog, View view) {
                    if (view.getId() == com.shanchain.common.R.id.iv_dialog_add_picture) {
                        selectImage(ActivityStackManager.getInstance().getTopActivity());
                    } else if (view.getId() == com.shanchain.common.R.id.tv_dialog_sure) {
                        ToastUtils.showToastLong(ActivityStackManager.getInstance().getTopActivity(), "请上传二维码图片");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null) {
            if (requestCode == NetErrCode.WALLET_PHOTO) {

                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //获取照片路径
                String photoPath = cursor.getString(columnIndex);
//            ToastUtils.showToastLong(mBusContext, "选择的图片途径：" + photoPath);
                cursor.close();
                final Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                mPasswordFile = new File(photoPath);
                if (showPasswordDialog != null) {
                    showPasswordDialog.dismiss();
                }
                showPasswordDialog = new com.shanchain.data.common.ui.widgets.CustomDialog(TaskDetailActivity.this, true, 1.0,
                        R.layout.dialog_bottom_wallet_password,
                        new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
                showPasswordDialog.setPasswordBitmap(bitmap);
                showPasswordDialog.setOnItemClickListener(new com.shanchain.data.common.ui.widgets.CustomDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(com.shanchain.data.common.ui.widgets.CustomDialog dialog, View view) {
                        if (view.getId() == com.shanchain.common.R.id.iv_dialog_add_picture) {
                            selectImage(ActivityStackManager.getInstance().getTopActivity());
                            showPasswordDialog.dismiss();
                        } else if (view.getId() == com.shanchain.common.R.id.tv_dialog_sure) {
                            checkPwd(mPasswordFile);
                            if (showPasswordDialog != null) {
                                showPasswordDialog.dismiss();
                            }
                        }
                    }
                });
                showPasswordDialog.show();

            }
        }
    }


    private void checkPwd(final File file) {
        final Handler releaseHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    MediaType MEDIA_TYPE = MediaType.parse("image/*");
                    RequestBody fileBody = MultipartBody.create(MEDIA_TYPE, file);
                    MultipartBody.Builder multiBuilder = new MultipartBody.Builder()
                            .addFormDataPart("file", file.getName(), fileBody)
                            .addFormDataPart("deviceToken", "" + registrationId)
                            .setType(MultipartBody.FORM);
                    RequestBody multiBody = multiBuilder.build();
                    SCHttpUtils.postByBody(HttpApi.WALLET_BIND_PHONE_IMEI + SCCacheUtils.getCacheToken(), multiBody, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showToast(mContext, "网络异常");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            final String code = SCJsonUtils.parseCode(result);
                            final String msg = SCJsonUtils.parseMsg(result);
                            if (NetErrCode.COMMON_SUC_CODE.equals(code) || NetErrCode.SUC_CODE.equals(code)) {
                                final String data = SCJsonUtils.parseData(result);
                                authCode = data;
                                String userId = SCCacheUtils.getCacheUserId();
                                SCCacheUtils.setCache(userId, Constants.TEMPORARY_CODE, data);
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        releaseTask(data, mDescribeEditText, mBountyEditText, limitedTime);
                                        pwdFree(file);
                                    }
                                });

                            } else {
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showToast(mContext, code + ":" + msg);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        };

        //创建requestBody
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        RequestBody fileBody = MultipartBody.create(MEDIA_TYPE, file);
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("suberUser", "" + SCCacheUtils.getCacheCharacterId())
                .addFormDataPart("userId", "" + SCCacheUtils.getCacheUserId())
                .setType(MultipartBody.FORM);
        RequestBody multiBody = multiBuilder.build();
        SCHttpUtils.postByBody(HttpApi.WALLET_CHECK_USE_PASSWORD + "?token=" + SCCacheUtils.getCacheToken(), multiBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(mContext, "网络异常");

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                final String code = SCJsonUtils.parseCode(result);
                final String msg = SCJsonUtils.parseMsg(result);
                if (NetErrCode.COMMON_SUC_CODE.equals(code) || NetErrCode.SUC_CODE.equals(code)) {
                    releaseHandler.sendEmptyMessage(1);
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(mContext, "" + msg);
                        }
                    });
                } else {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(mContext, code + ":" + msg);
                        }
                    });
                }
            }
        });
    }

    private void pwdFree(final File file) {
        //设置免密操作
        final Handler freePasswordHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                final boolean bind = (boolean) msg.obj;
                switch (msg.what) {
                    case 1:
                        if (!TextUtils.isEmpty(SCCacheUtils.getCacheAuthCode())) {
                            Map stringMap = new HashMap();
                            stringMap.put("os", "android");
                            stringMap.put("token", "" + SCCacheUtils.getCacheToken());
                            stringMap.put("deviceToken", "" + JPushInterface.getRegistrationID(mContext));
                            stringMap.put("bind", bind);
                            String modifyUser = JSONObject.toJSONString(stringMap);
                            SCHttpUtils.postWithUserId()
                                    .url(HttpApi.MODIFY_CHARACTER)
                                    .addParams("characterId", "" + SCCacheUtils.getCacheCharacterId())
                                    .addParams("dataString", modifyUser)
                                    .build()
                                    .execute(new SCHttpStringCallBack() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            LogUtils.d("修改角色信息失败");
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            String code = JSONObject.parseObject(response).getString("code");
                                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                                LogUtils.d("修改角色信息");
                                            }
                                        }
                                    });

                        }
                        break;
                }
            }
        };

        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mStandardDialog = new StandardDialog(mContext);
                mStandardDialog.setStandardTitle("验证成功！");
                mStandardDialog.setStandardMsg("您也可以选择开启免密功能，在下次使用马甲券时便无需再次上传安全码，让使用更加方便快捷，是否开通免密功能？");
                mStandardDialog.setCancelText("暂不需要");
                mStandardDialog.setSureText("立即开通");
                //开通免密
                mStandardDialog.setCallback(new com.shanchain.data.common.base.Callback() {
                    @Override
                    public void invoke() {
                        String userId = SCCacheUtils.getCacheUserId();
                        String passwordCode = getAuCode(file);
                        SCCacheUtils.setCache(userId, Constants.CACHE_AUTH_CODE, passwordCode);
                        SCCacheUtils.setCache(userId, Constants.TEMPORARY_CODE, passwordCode);
                        Message message = new Message();
                        message.what = 1;
                        message.obj = true;
                        ActivityStackManager.getInstance().getTopActivity();
                        freePasswordHandler.sendMessage(message);
                    }
                }, new com.shanchain.data.common.base.Callback() {//不开启免密
                    @Override
                    public void invoke() {
                        Message message = new Message();
                        message.what = 1;
                        message.obj = false;
                        freePasswordHandler.sendMessage(message);
                        String userId = SCCacheUtils.getCacheUserId();
                        mAuthCode = getAuCode(file);
                        SCCacheUtils.setCache(userId, Constants.CACHE_AUTH_CODE, "");
                        SCCacheUtils.setCache(userId, Constants.TEMPORARY_CODE, mAuthCode);
                    }
                });
                mStandardDialog.show();
            }
        });
    }

    private String getAuCode(File file) {
        //创建requestBody,获取密码凭证
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        RequestBody fileBody = MultipartBody.create(MEDIA_TYPE, file);
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("deviceToken", "" + registrationId)
                .setType(MultipartBody.FORM);
        RequestBody multiBody = multiBuilder.build();
        SCHttpUtils.postByBody(HttpApi.WALLET_BIND_PHONE_IMEI + SCCacheUtils.getCacheToken(), multiBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(mContext, "网络异常");

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                final String code = SCJsonUtils.parseCode(result);
                final String msg = SCJsonUtils.parseMsg(result);
                if (NetErrCode.COMMON_SUC_CODE.equals(code) || NetErrCode.SUC_CODE.equals(code)) {
                    String data = SCJsonUtils.parseData(result);
                    authCode = data;
                    String userId = SCCacheUtils.getCacheUserId();
                    SCCacheUtils.setCache(userId, Constants.TEMPORARY_CODE, authCode);
                } else {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(mContext, code + ":" + msg);
                        }
                    });
                }
            }
        });
        return authCode;
    }

    @Override
    public void onRightClick(View v) {
        Intent intent = new Intent(TaskDetailActivity.this, TaskListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        page = 0;
        if (taskList.size() < 0) {
            return;
        }
        initData();
        adapter.upData(taskList);
        adapter.notifyDataSetChanged();
        rvTaskDetails.setAdapter(adapter);
        srlTaskList.setRefreshing(false);
    }

    private void initLoadMoreListener() {

        rvTaskDetails.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (last == true) {
                    return;
                }
                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    page++;
                    SCHttpUtils.getAndToken()
                            .url(HttpApi.ALL_TASK_LIST)
                            .addParams("characterId", characterId + "")
                            .addParams("roomId", roomID + "")
                            .addParams("page", page + "")
                            .addParams("size", size + "")
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
                                        String isLast = JSONObject.parseObject(response).getString("last");
                                        last = Boolean.valueOf(isLast);
                                        List<ChatEventMessage> chatEventMessageList = JSONObject.parseArray(content
                                                , ChatEventMessage.class);
                                        adapter.addData(chatEventMessageList);
                                        closeLoadingDialog();
                                    }
                                }
                            });
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

    }
}
