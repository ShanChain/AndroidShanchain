package com.shanchain.arkspot.ui.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.view.activity.story.ReleaseDynamicActivity;
import com.shanchain.arkspot.ui.view.activity.story.StoryTitleActivity;
import com.shanchain.arkspot.ui.view.fragment.MineFragment;
import com.shanchain.arkspot.ui.view.fragment.NewsFragment;
import com.shanchain.arkspot.ui.view.fragment.SquareFragment;
import com.shanchain.arkspot.ui.view.fragment.StoryFragment;
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
    private String[] navigationBarTitles = {"故事","会话","广场","我的"};

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
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_story,navigationBarTitles[0]))
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_news,navigationBarTitles[1]))
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_square,navigationBarTitles[2]))
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_mine,navigationBarTitles[3]))
                .setFirstSelectedPosition(0)
                .initialise();
        mBnb.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mFragmentId = position;
                setToolBar(mFragmentId);
                switch (position) {
                    case 0:
                        setFragment(new StoryFragment());
                        break;
                    case 1:
                        setFragment(new NewsFragment());
                        break;
                    case 2:
                        setFragment(new SquareFragment());
                        break;
                    case 3:
                        setFragment(new MineFragment());
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

    private void setFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_main_container, fragment);
            fragmentTransaction.commit();
        }
    }

    private void setSelectedPager() {
        switch (mFragmentId) {
            case 0:
                setFragment(new StoryFragment());
                setToolBar(mFragmentId);
                break;
            case 1:
                setFragment(new NewsFragment());
                setToolBar(mFragmentId);
                break;
            case 2:
                setFragment(new SquareFragment());
                setToolBar(mFragmentId);
                break;
            case 3:
                setFragment(new MineFragment());
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
                titleStory.setCompoundDrawables(null,null,drawable,null);
                titleStory.setCompoundDrawablePadding(DensityUtils.dip2px(this,4));
                mTbMain.setRightImage(R.mipmap.abs_home_btn_dynamic_default);
                mTbMain.setOnTitleClickListener(this);
                mTbMain.setBtnEnabled(false, true);
                mTbMain.setBtnVisibility(false, true);
                mTbMain.setOnRightClickListener(this);
                break;
            case 1:
                mTbMain.setTitleText(navigationBarTitles[position]);
                TextView titleNews = mTbMain.getTitleView();
                titleNews.setCompoundDrawables(null,null,null,null);
                mTbMain.setBtnEnabled(true);
                mTbMain.setBtnVisibility(true);
                mTbMain.setOnLeftClickListener(this);
                mTbMain.setOnRightClickListener(this);
                break;
            case 2:
                mTbMain.setTitleText(navigationBarTitles[position]);
                TextView titleSquare = mTbMain.getTitleView();
                titleSquare.setCompoundDrawables(null,null,null,null);
                mTbMain.setBtnVisibility(true, false);
                mTbMain.setBtnEnabled(true, false);
                break;
            case 3:
                mTbMain.setTitleText(navigationBarTitles[position]);
                TextView titleMine = mTbMain.getTitleView();
                titleMine.setCompoundDrawables(null,null,null,null);
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
                break;
            case 2:
                break;
            case 3:
                break;
        }
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
                overridePendingTransition(R.anim.activity_enter_alpha,R.anim.activity_anim_default);
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
