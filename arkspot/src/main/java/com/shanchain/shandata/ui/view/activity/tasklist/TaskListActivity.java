package com.shanchain.shandata.ui.view.activity.tasklist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.timepicker.SCTimePickerView;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.alibaba.fastjson.JSONObject;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TaskPagerAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jiguang.imui.model.MyMessage;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.eventbus.EventBus;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;

import static com.shanchain.data.common.base.Constants.CACHE_CUR_USER;


public class TaskListActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
        ArthurToolBar.OnRightClickListener,
        ArthurToolBar.OnLeftClickListener, DefaultHardwareBackBtnHandler {

    @Bind(R.id.tb_main)
    ArthurToolBar mTbMain;
    @Bind(R.id.tab_task)
    TabLayout tabTask;
    @Bind(R.id.vp_task)
    ViewPager vpTask;
    @Bind(R.id.spinner_task_list)
    Spinner spinnerTaskList;

    private int mFragmentId;
    private String roomID;
    private AdapterView.OnItemSelectedListener onItemSelectedListener;
    private TextView limitedTime;
    private ChatEventMessage chatEventMessage1;
    private Conversation chatRoomConversation;
    private SCTimePickerView scTimePickerView;
    private SCTimePickerView.OnTimeSelectListener onTimeSelectListener;
    private long timeStamp;
    private String formatDate;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_task_list;
    }

    @Override
    protected void initViewsAndEvents() {

        Intent intent = getIntent();
//        roomID = intent.getStringExtra("roomId");
//        roomID = "12826211";
        roomID = SCCacheUtils.getCacheRoomId();

        String uId = SCCacheUtils.getCache("0", CACHE_CUR_USER);
        String token = SCCacheUtils.getCache(uId, Constants.CACHE_TOKEN);
        String spaceId = SCCacheUtils.getCache(uId, Constants.CACHE_SPACE_ID);
        String characterId = SCCacheUtils.getCache(uId, Constants.CACHE_CHARACTER_ID);
        initToolBar();
        setFragment();

    }

    private void initToolBar() {

        mTbMain.setLeftImage(R.mipmap.back);
//        mTbMain.setRightImage(R.mipmap.nav_task_add);
        //设置导航栏标题
        mTbMain.setTitleTextColor(Color.WHITE);
        mTbMain.isShowChatRoom(false);//不在导航栏显示聊天室信息
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTbMain.getTitleView().setLayoutParams(layoutParams);
        mTbMain.setTitleText("悬赏任务");
        mTbMain.setBackgroundColor(Color.parseColor("#4FD1F6"));

        mTbMain.setOnLeftClickListener(this);//左侧导航栏监听
        mTbMain.setOnRightClickListener(this);//右侧导航栏监听
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setFragment() {
        String[] titles = {"任务列表", "我的任务"};
        TaskPagerAdapter adapter = new TaskPagerAdapter(getSupportFragmentManager(), titles);
        vpTask.setOffscreenPageLimit(2);
        vpTask.setAdapter(adapter);
        tabTask.setupWithViewPager(vpTask);
        vpTask.setCurrentItem(0);
        vpTask.setOnPageChangeListener(this);

        String type[] = new String[]{"全部任务", "未领取任务","已结束"};
        onItemSelectedListener = new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //全部任务
                    case 0:
                        SCHttpUtils.postWithUserId()
                                .url(HttpApi.GROUP_TASK_LIST)
                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                .addParams("roomId", roomID)
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

                                        }
                                    }
                                });
//                        EventBus.getDefault().post();

                        break;
                    //未领取任务
                    case 1:
                        SCHttpUtils.postWithUserId()
                                .url(HttpApi.TASK_DETAIL_UNACCALIMED_LIST)
                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                .addParams("roomId", roomID)
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

                                        }
                                    }
                                });
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);                       //设置下拉框样式
        spinnerTaskList.setAdapter(arrayAdapter);
        spinnerTaskList.setOnItemSelectedListener(onItemSelectedListener);

    }

    /*
     * 初始化右侧导航栏点击事件
     * */
    @Override
    public void onRightClick(View v) {

        final int[] idItems = new int[]{R.id.et_input_dialog_describe, R.id.et_input_dialog_bounty, R.id.dialog_select_task_time, R.id.btn_dialog_input_sure, R.id.iv_dialog_close};
        final CustomDialog dialog = new CustomDialog(TaskListActivity.this, false, 1.0, R.layout.common_dialog_chat_room_task, idItems);
        View layout = View.inflate(TaskListActivity.this, R.layout.common_dialog_chat_room_task, null);
        dialog.setView(layout);
        dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(final CustomDialog dialog, View view) {
                EditText describeEditText = (EditText) dialog.getByIdView(R.id.et_input_dialog_describe);
                EditText bountyEditText = (EditText) dialog.getByIdView(R.id.et_input_dialog_bounty);
                limitedTime = (TextView) dialog.getByIdView(R.id.dialog_select_task_time);

                switch (view.getId()) {
                    case R.id.et_input_dialog_describe:
                        ToastUtils.showToast(TaskListActivity.this, "输入任务描述");
                        break;
                    case R.id.et_input_dialog_bounty:
                        ToastUtils.showToast(TaskListActivity.this, "输入赏金");
                        break;
                    case R.id.btn_dialog_input_sure:

                        if (TextUtils.isEmpty(describeEditText.getText().toString()) && TextUtils.isEmpty(bountyEditText.getText().toString()) && TextUtils.isEmpty(limitedTime.getText().toString())) {
                            ToastUtils.showToast(TaskListActivity.this, "请输入完整信息");
                        } else {
                            final String spaceId = SCCacheUtils.getCacheSpaceId();//获取当前的空间ID
                            final String bounty = bountyEditText.getText().toString();
                            final String dataString = describeEditText.getText().toString();
                            final String LimitedTtime = limitedTime.getText().toString();

                            final String characterId = SCCacheUtils.getCacheCharacterId();
                            //向服务器请求添加任务
                            SCHttpUtils.postWithUserId()
                                    .url(HttpApi.CHAT_TASK_ADD)
                                    .addParams("characterId", characterId + "")
                                    .addParams("bounty", bounty + "")
                                    .addParams("roomId", roomID + "")
                                    .addParams("dataString", dataString + "") //任务内容
                                    .addParams("time", timeStamp + "")
                                    .build()
                                    .execute(new SCHttpStringCallBack() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            LogUtils.d("TaskPresenterImpl", "添加任务失败");
                                            chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
//                                            mAdapter.addToStart(chatEventMessage1, true);
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            String code = JSONObject.parseObject(response).getString("code");
                                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                                String data = JSONObject.parseObject(response).getString("data");
                                                String publishTime = JSONObject.parseObject(data).getString("PublishTime");
                                                String task = JSONObject.parseObject(data).getString("Task");
                                                chatEventMessage1 = JSONObject.parseObject(task, ChatEventMessage.class);

                                                Map customMap = new HashMap();
                                                customMap.put("taskId", chatEventMessage1.getTaskId() + "");
                                                customMap.put("bounty", chatEventMessage1.getBounty() + "");
                                                customMap.put("dataString", chatEventMessage1.getIntro() + "");
                                                customMap.put("time", LimitedTtime);

                                                Message sendCustomMessage = chatRoomConversation.createSendCustomMessage(customMap);
                                                sendCustomMessage.setOnSendCompleteCallback(new BasicCallback() {
                                                    @Override
                                                    public void gotResult(int i, String s) {
                                                        String s1 = s;
                                                        if (0 == i) {
                                                            Toast.makeText(TaskListActivity.this, "发送任务消息成功", Toast.LENGTH_SHORT);
                                                            LogUtils.d("发送任务消息", "code: " + i + " 回调信息：" + s);
                                                            chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
//                                                            mAdapter.addToStart(chatEventMessage1, true);
                                                        } else {
                                                            Toast.makeText(TaskListActivity.this, "发送任务消息失败", Toast.LENGTH_SHORT);
                                                            chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
//                                                            mAdapter.addToStart(chatEventMessage1, true);
                                                        }
                                                    }
                                                });
                                                JMessageClient.sendMessage(sendCustomMessage);

//                                                chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
//                                                mAdapter.addToStart(chatEventMessage1, true);
                                                dialog.dismiss();
                                            } else if (code == "1001") {
                                                dialog.dismiss();
                                                //余额不足
                                                Toast.makeText(TaskListActivity.this, "您的钱包余额不足", Toast.LENGTH_SHORT);

                                            }
                                        }
                                    });

                        }
                        dialog.dismiss();
                        break;
                    case R.id.iv_dialog_close:
                        dialog.dismiss();
                        break;
                    //选择时间
                    case R.id.dialog_select_task_time:
                        scTimePickerView = new SCTimePickerView.Builder(TaskListActivity.this, new SCTimePickerView.OnTimeSelectListener() {
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
                        }).setType(new boolean[]{true, true, true, true, false, false})//设置显示年、月、日、时、分、秒
                                .setDecorView((ViewGroup) findViewById(android.R.id.content).getRootView())
//                .setDecorView((ViewGroup) dialog.getWindow().getDecorView().getRootView())
                                .isCenterLabel(true)
                                .setLabel("年", "月", "日", "时", "分", "秒")
                                .setCancelText("清除")
                                .setCancelColor(TaskListActivity.this.getResources().getColor(com.shanchain.common.R.color.colorDialogBtn))
                                .setSubmitText("完成")
                                .setSubCalSize(14)
                                .setTitleBgColor(TaskListActivity.this.getResources().getColor(com.shanchain.common.R.color.colorWhite))
                                .setSubmitColor(TaskListActivity.this.getResources().getColor(com.shanchain.common.R.color.colorDialogBtn))
                                .isDialog(true)
                                .build();
                        scTimePickerView.setDate(Calendar.getInstance());
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
        dialog.show();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeLoadingDialog();
    }

    /*
     * 初始化左侧导航点击事件
     * */
    @Override
    public void onLeftClick(View v) {
        finish();
    }


    @Override
    public void invokeDefaultOnBackPressed() {
        onBackPressed();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        switch (position) {
            case 0:
                String type[] = new String[]{"全部任务", "未领取任务","已结束"};
                onItemSelectedListener = new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            //我领取的
                            case 0:
                                SCHttpUtils.postWithUserId()
                                        .url(HttpApi.TASK_DETAIL_RECEIVE_LIST)
                                        .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
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

                                                }
                                            }
                                        });
                                break;
                            //未领取的
                            case 1:
                                SCHttpUtils.postWithUserId()
                                        .url(HttpApi.TASK_DETAIL_RECEIVE_LIST)
                                        .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
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

                                                }
                                            }
                                        });
                                break;
                        }

//                        ToastUtils.showToast(TaskListActivity.this, "选择了第" + position + "个按钮");

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                };
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);                       //设置下拉框样式//设置下拉框样式
                spinnerTaskList.setAdapter(arrayAdapter);
                spinnerTaskList.setOnItemSelectedListener(onItemSelectedListener);
                break;
            case 1:
                String type1[] = new String[]{"全部任务","我发布的", "我领取的", "已结束"};
                onItemSelectedListener = new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            //全部任务
                            case 0:
                                break;
                            //未领取任务
                            case 1:
                                vpTask.setCurrentItem(1);
                                SCHttpUtils.postWithUserId()
                                        .url(HttpApi.TASK_DETAIL_UNACCALIMED_LIST)
                                        .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                        .addParams("roomId", roomID)
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

                                                }
                                            }
                                        });


                                break;
                        }

//                        ToastUtils.showToast(TaskListActivity.this, "选择了第" + position + "个按钮");

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                };
                ArrayAdapter<String> arrayAdapter0 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type1);
                arrayAdapter0.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);                       //设置下拉框样式
                spinnerTaskList.setAdapter(arrayAdapter0);
                spinnerTaskList.setOnItemSelectedListener(onItemSelectedListener);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    private class SpinnerAdapter extends ArrayAdapter<String> {
        Context context;
        String[] items = new String[]{};

        public SpinnerAdapter(final Context context,
                              final int textViewResourceId, final String[] objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
            this.context = context;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_item, parent, false);
            }

            TextView tv = (TextView) convertView
                    .findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(16);
            return convertView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_item, parent, false);
            }

            // android.R.id.text1 is default text view in resource of the android.
            // android.R.layout.simple_spinner_item is default layout in resources of android.

            TextView tv = (TextView) convertView
                    .findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(16);
            return convertView;
        }
    }

}
