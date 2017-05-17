package com.shanchain.mvp.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.shanchain.R;
import com.shanchain.base.BaseActivity;

import butterknife.Bind;

public class MainActivity extends BaseActivity {


    /**
     * 描述：标题栏文本
     */
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    /**
     * 描述：内容填充区域
     */
    @Bind(R.id.fl_main_container)
    FrameLayout mFlMainContainer;
    /**
     * 描述：底部导航栏
     */
    @Bind(R.id.bnb)
    BottomNavigationBar mBnb;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents() {

        //初始化底部导航栏
        initBottomNavigationBar();
    }

    /**
     * 2017/5/16
     * 描述：初始化底部导航栏
     */
    private void initBottomNavigationBar() {
        mBnb.setActiveColor(R.color.colorPrimary)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setInActiveColor(R.color.colorAccent)
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "首页"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "动态"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "发现"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "我的"))
                .initialise();
        /*mBnb.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 1:

                        break;
                    case 2:

                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });*/
    }


    private void setFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_main_container,fragment);
            fragmentTransaction.commit();
        }
    }

}
