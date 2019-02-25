package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.view.fragment.SingerChatFragment;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import cn.jiguang.imui.model.DefaultUser;

public class SetFragmentActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    ArthurToolBar mTbMain;
    FrameLayout mFlMainContainer;

    private SingerChatFragment singerChatFragment;
    private DefaultUser userInfo;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_single_chat;
    }

    @Override
    protected void initViewsAndEvents() {
        mTbMain = findViewById(R.id.tb_main);
        mFlMainContainer = findViewById(R.id.fl_main_container);
        userInfo =(DefaultUser) getIntent().getParcelableExtra("userInfo");
        initToolBar();
        setFragment();
    }

    private void setFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if (fragmentTransaction.isEmpty()) {
            singerChatFragment = new SingerChatFragment();
        }
        fragmentTransaction.add(R.id.fl_main_container, singerChatFragment);
        fragmentTransaction.show(singerChatFragment);

        fragmentTransaction.commit();
    }

    private void initToolBar() {

        mTbMain.setBackgroundColor(getResources().getColor(R.color.colorHomeBtn));
        mTbMain.setTitleTextColor(getResources().getColor(R.color.white));
        mTbMain.setTitleText("好友");
        mTbMain.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        mTbMain.setOnLeftClickListener(this);

    }


    @Override
    public void onLeftClick(View v) {


    }

    @Override
    public void onRightClick(View v) {

    }
}
