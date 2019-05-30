package com.shanchain.shandata.ui.view.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TaskPagerAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.view.fragment.FragmentMyTask;
import com.shanchain.shandata.ui.view.fragment.FragmentTaskList;
import com.shanchain.shandata.ui.view.fragment.MainARSGameFragment;
import com.shanchain.shandata.ui.view.fragment.MainChatRoomFragment;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.response.ErrorResponse;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainEntranceActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        ArthurToolBar.OnRightClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {

    private ArthurToolBar mTbMain;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<Fragment> fragmentList = new ArrayList();
    private TextView tvBtnCoupon;
    private TextView tvBtnTask;
    private LinearLayout linearAddChatRoom;
    private WebSocketManager mWebSocketManager;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main_entrance;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        mTabLayout = findViewById(R.id.tab_layout_main);
        mViewPager = findViewById(R.id.vp_main);
        tvBtnCoupon = findViewById(R.id.text_btn_coupon);
        tvBtnTask = findViewById(R.id.text_btn_task);
        linearAddChatRoom = findViewById(R.id.linear_add_chat_room);
        setFragment();
        //获取WebSocket管理对象
        mWebSocketManager = WebSocketHandler.getDefault();
//        mWebSocketManager.addListener(new SimpleListener() {
//            @Override
//            public <T> void onMessage(final String message, T data) {
//                super.onMessage(message, data);
//                LogUtils.d("WebSocketHandler", "接收到的message:" + message);
//
//                ThreadUtils.runOnMainThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        StandardDialog dialog = new StandardDialog(MainEntranceActivity.this);
//                        dialog.setStandardTitle("进入元社区");
//                        dialog.setStandardMsg("点击确认进入元社区");
//                        dialog.show();
//                        ToastUtils.showToast(MainEntranceActivity.this, "" + message);
//                    }
//                });
//            }
//
//            @Override
//            public void onSendDataError(ErrorResponse errorResponse) {
//                super.onSendDataError(errorResponse);
//                LogUtils.d("WebSocketHandler", "发送消息失败:" + errorResponse.toString());
//            }
//
//            @Override
//            public <T> void onMessage(ByteBuffer bytes, T data) {
//                super.onMessage(bytes, data);
//                LogUtils.d("WebSocketHandler", "接收到的bytes:");
//            }
//        });
    }

    private void initToolBar() {
        mTbMain = findViewById(R.id.toolbar_nav);
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
        String[] titles = {"ARS聊天室", "聊天室"};
        fragmentList.add(new MainARSGameFragment());
        fragmentList.add(new MainChatRoomFragment());
        TaskPagerAdapter adapter = new TaskPagerAdapter(getSupportFragmentManager(), titles, fragmentList);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        if (getIntent().getStringExtra("receiveTaskList") != null) {
            mViewPager.setCurrentItem(1);
        } else {
            mViewPager.setCurrentItem(0);
        }
//        mViewPager.setOnPageChangeListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onLeftClick(View v) {
        mWebSocketManager.send("发送消息给服务器");
    }

    @Override
    public void onRightClick(View v) {

    }
}
