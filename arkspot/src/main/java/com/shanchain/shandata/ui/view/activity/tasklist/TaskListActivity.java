package com.shanchain.shandata.ui.view.activity.tasklist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.ui.widgets.timepicker.SCTimePickerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TaskPagerAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.view.fragment.FragmentMyTask;
import com.shanchain.shandata.ui.view.fragment.FragmentTaskList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jpush.im.android.api.model.Conversation;
import okhttp3.Call;

import static com.shanchain.data.common.base.Constants.CACHE_CUR_USER;

/**
 * 我的帮助
 */
public class TaskListActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
        ArthurToolBar.OnRightClickListener,
        ArthurToolBar.OnLeftClickListener {
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
    private List<Fragment> fragmentList = new ArrayList();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_task_list;
    }

    @Override
    protected void initViewsAndEvents() {

        Intent intent = getIntent();
        roomID = intent.getStringExtra("roomId") != null ?
                intent.getStringExtra("roomId") : SCCacheUtils.getCacheRoomId();

        String uId = SCCacheUtils.getCache("0", CACHE_CUR_USER);
        String token = SCCacheUtils.getCache(uId, Constants.CACHE_TOKEN);
        String spaceId = SCCacheUtils.getCache(uId, Constants.CACHE_SPACE_ID);
        String characterId = SCCacheUtils.getCache(uId, Constants.CACHE_CHARACTER_ID);

        initToolBar();
        setFragment();

    }

    private void initToolBar() {

        mTbMain.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
//        mTbMain.setRightImage(R.mipmap.nav_task_add);
        //设置导航栏标题
        mTbMain.setTitleTextColor(Color.BLACK);
//        mTbMain.isShowChatRoom(false);//不在导航栏显示聊天室信息
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT
//        );
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        mTbMain.getTitleView().setLayoutParams(layoutParams);
        mTbMain.setTitleText(getResources().getString(R.string.tool_bar_my_task));
        mTbMain.setBackgroundColor(getResources().getColor(R.color.colorWhite));

        mTbMain.setOnLeftClickListener(this);//左侧导航栏监听
        mTbMain.setOnRightClickListener(this);//右侧导航栏监听
    }


    private void setFragment() {
        String[] titles = {getResources().getString(R.string.my_task_my_post), getResources().getString(R.string.my_task_my_helped)};
        fragmentList.add(new FragmentTaskList());
        fragmentList.add(new FragmentMyTask());
        TaskPagerAdapter adapter = new TaskPagerAdapter(getSupportFragmentManager(), titles, fragmentList);
        vpTask.setOffscreenPageLimit(2);
        vpTask.setAdapter(adapter);
        tabTask.setupWithViewPager(vpTask);
        if (getIntent().getStringExtra("receiveTaskList") != null) {
            vpTask.setCurrentItem(1);
        } else {
            vpTask.setCurrentItem(0);
        }
        vpTask.setOnPageChangeListener(this);

    }

    /*
     * 初始化右侧导航栏点击事件
     * */
    @Override
    public void onRightClick(View v) {
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        switch (position) {
            case 0:
                String type[] = new String[]{"全部任务", "未领取任务", "已结束"};
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
                String type1[] = new String[]{"全部任务", "我发布的", "我领取的", "已结束"};
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

}
