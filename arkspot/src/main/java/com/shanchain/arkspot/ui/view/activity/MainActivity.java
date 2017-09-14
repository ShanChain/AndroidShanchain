package com.shanchain.arkspot.ui.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.view.activity.chat.ContactActivity;
import com.shanchain.arkspot.ui.view.activity.story.ReleaseDynamicActivity;
import com.shanchain.arkspot.ui.view.activity.story.SelectContactActivity;
import com.shanchain.arkspot.ui.view.activity.story.StoryTitleActivity;
import com.shanchain.arkspot.ui.view.fragment.MineFragment;
import com.shanchain.arkspot.ui.view.fragment.NewsFragment;
import com.shanchain.arkspot.ui.view.fragment.SquareFragment;
import com.shanchain.arkspot.ui.view.fragment.StoryFragment;
import com.shanchain.arkspot.widgets.dialog.CustomDialog;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import utils.DensityUtils;


public class MainActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener, ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnTitleClickListener {

    ArthurToolBar mTbMain;
    @Bind(R.id.fl_main_container)
    FrameLayout mFlMainContainer;
    @Bind(R.id.bnb)
    BottomNavigationBar mBnb;
    private int mFragmentId;
    private String[] navigationBarTitles = {"故事", "会话", "广场", "我的"};

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents() {

        Intent intent = getIntent();
        mFragmentId = intent.getIntExtra("fragmentId", 0);

        initToolBar();
        initBottomNavigationBar();
    }

    private void initToolBar() {
        mTbMain = (ArthurToolBar) findViewById(R.id.tb_main);
        mTbMain.setTitleText("三体");

    }

    private void initBottomNavigationBar() {
        mBnb.setActiveColor(R.color.colorActive)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setInActiveColor(R.color.colorInactive)
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_story, navigationBarTitles[0]))
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_news, navigationBarTitles[1]))
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_square, navigationBarTitles[2]))
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_mine, navigationBarTitles[3]))
                .setFirstSelectedPosition(0)
                .initialise();
        mBnb.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mFragmentId = position;
                setToolBar(mFragmentId);
                switch (position) {
                    case 0:
                        setFragment(0);
                        break;
                    case 1:
                        setFragment(1);
                        break;
                    case 2:
                        setFragment(2);
                        break;
                    case 3:
                        setFragment(3);
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }

        });

        setSelectedPager();

    }

    private StoryFragment mStoryFragment;
    private NewsFragment mNewsFragment;
    private SquareFragment mSquareFragment;
    private MineFragment mMineFragment;
    private void setFragment(int position) {

            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();

        hideAllFragment(fragmentTransaction);

            switch (position) {
                case 0:
                    if (mStoryFragment == null){
                        mStoryFragment = new StoryFragment();
                        fragmentTransaction.add(R.id.fl_main_container,mStoryFragment);
                    }else {
                        fragmentTransaction.show(mStoryFragment);
                    }
                    break;
                case 1:
                    if (mNewsFragment == null){
                        mNewsFragment = new NewsFragment();
                        fragmentTransaction.add(R.id.fl_main_container,mNewsFragment);
                    }else {
                        fragmentTransaction.show(mNewsFragment);
                    }
                    break;
                case 2:
                    if (mSquareFragment == null){
                        mSquareFragment = new SquareFragment();
                        fragmentTransaction.add(R.id.fl_main_container,mSquareFragment);
                    }else {
                        fragmentTransaction.show(mSquareFragment);
                    }
                    break;
                case 3:
                    if (mMineFragment == null){
                        mMineFragment = new MineFragment();
                        fragmentTransaction.add(R.id.fl_main_container,mMineFragment);
                    }else {
                        fragmentTransaction.show(mMineFragment);
                    }
                    break;
            }

            fragmentTransaction.commit();

    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (mStoryFragment!= null){
            fragmentTransaction.hide(mStoryFragment);
        }

        if (mSquareFragment != null){
            fragmentTransaction.hide(mSquareFragment);
        }

        if (mNewsFragment != null) {
            fragmentTransaction.hide(mNewsFragment);
        }

        if (mMineFragment != null) {
            fragmentTransaction.hide(mMineFragment);
        }
    }

    private void setSelectedPager() {
        switch (mFragmentId) {
            case 0:
                setFragment(0);
                setToolBar(mFragmentId);
                break;
            case 1:
                setFragment(1);
                setToolBar(mFragmentId);
                break;
            case 2:
                setFragment(2);
                setToolBar(mFragmentId);
                break;
            case 3:
                setFragment(3);
                setToolBar(mFragmentId);
                break;
        }
    }

    private void setToolBar(int position) {
        switch (position) {
            case 0:
                mTbMain.setTitleText(navigationBarTitles[position]);
                TextView titleStory = mTbMain.getTitleView();
                Drawable drawable = getResources().getDrawable(R.mipmap.abs_home_btn_dropdown_default);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                titleStory.setCompoundDrawables(null, null, drawable, null);
                titleStory.setCompoundDrawablePadding(DensityUtils.dip2px(this, 4));
                mTbMain.setRightImage(R.mipmap.abs_home_btn_dynamic_default);
                mTbMain.setOnTitleClickListener(this);
                mTbMain.setBtnEnabled(false, true);
                mTbMain.setBtnVisibility(false, true);
                mTbMain.setOnRightClickListener(this);
                break;
            case 1:
                mTbMain.setTitleText(navigationBarTitles[position]);
                TextView titleNews = mTbMain.getTitleView();
                titleNews.setCompoundDrawables(null, null, null, null);
                mTbMain.setBtnEnabled(false, true);
                mTbMain.setBtnVisibility(false, true);
                mTbMain.setRightImage(R.mipmap.abs_home_btn_more_default);
                mTbMain.setOnRightClickListener(this);
                break;
            case 2:
                mTbMain.setTitleText(navigationBarTitles[position]);
                TextView titleSquare = mTbMain.getTitleView();
                titleSquare.setCompoundDrawables(null, null, null, null);
                mTbMain.setBtnVisibility(true, false);
                mTbMain.setBtnEnabled(true, false);
                break;
            case 3:
                mTbMain.setTitleText(navigationBarTitles[position]);
                TextView titleMine = mTbMain.getTitleView();
                titleMine.setCompoundDrawables(null, null, null, null);
                mTbMain.setBtnEnabled(false, true);
                mTbMain.setBtnVisibility(false, true);
                mTbMain.setOnRightClickListener(this);
                break;
        }
    }


    @Override
    public void onRightClick(View v) {
        switch (mFragmentId) {
            case 0:
                readyGo(ReleaseDynamicActivity.class);
                break;
            case 1:
                newsRightClick();
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    private void newsRightClick() {
        final CustomDialog customDialog = new CustomDialog(this, true, 1, R.layout.dialog_msg_bottom, new int[]{R.id.tv_dialog_msg_new,
                R.id.tv_dialog_msg_focus, R.id.tv_dialog_msg_code, R.id.tv_dialog_msg_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {

                switch (view.getId()) {
                    case R.id.tv_dialog_msg_new:
                        //邀请好友，创建新场景（群）
                        Intent invitationIntent = new Intent(MainActivity.this, SelectContactActivity.class);
                        invitationIntent.putExtra("isAt",false);
                        startActivity(invitationIntent);
                        customDialog.dismiss();
                        break;
                    case R.id.tv_dialog_msg_focus:
                        //互相关注
                        Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                        startActivity(intent);
                        customDialog.dismiss();
                        break;
                    case R.id.tv_dialog_msg_code:
                        //扫码

                        customDialog.dismiss();
                        break;
                    case R.id.tv_dialog_msg_cancel:
                        //取消
                        customDialog.dismiss();
                        break;
                }

            }
        });
        customDialog.show();
    }

    @Override
    public void onLeftClick(View v) {
        switch (mFragmentId) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    @Override
    public void onTitleClick(View v) {
        switch (mFragmentId) {
            case 0:
                Intent intent = new Intent(this, StoryTitleActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_enter_alpha, R.anim.activity_anim_default);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
}
