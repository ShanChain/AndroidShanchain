package com.shanchain.mvp.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.mvp.view.fragment.DynamicFragment;
import com.shanchain.mvp.view.fragment.FoundFragment;
import com.shanchain.mvp.view.fragment.HomeFragment;
import com.shanchain.mvp.view.fragment.MineFragment;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {


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
    /**
     * 描述：顶部工具栏
     */
    private ArthurToolBar mToolbarMain;

    /**
     * 描述：记录当前页
     */
    private int currentPage;

    private String[] toolBarTitles = {"首页", "动态", "发现", "我的"};

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents() {
        mToolbarMain = (ArthurToolBar) findViewById(R.id.toolbar_main);
        initTooBar();

        //初始化底部导航栏
        initBottomNavigationBar();


    }


    /**
     * 2017/5/18
     * 描述：初始化工具栏，设置沉浸式
     */
    private void initTooBar() {
        currentPage = 1;
        mToolbarMain.setTitleText(toolBarTitles[currentPage]);
        //设置沉浸式
        mToolbarMain.setImmersive(this, true);
        mToolbarMain.setBackgroundColor(getResources().getColor(R.color.colorTheme));
    }

    /**
     * 2017/5/16
     * 描述：初始化底部导航栏
     */
    private void initBottomNavigationBar() {
        mBnb.setActiveColor(R.color.colorActive)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setInActiveColor(R.color.colorInactive)
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_homepager, toolBarTitles[0]))
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_dynamic, toolBarTitles[1]))
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_find, toolBarTitles[2]))
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_mine, toolBarTitles[3]))
                .setFirstSelectedPosition(1)
                .initialise();

        mBnb.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                currentPage = position;
                setToolBar(currentPage);
                switch (position) {
                    case 0:
                        setFragment(new HomeFragment());
                        break;
                    case 1:
                        setFragment(new DynamicFragment());
                        break;
                    case 2:
                        setFragment(new FoundFragment());
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
        //默认选中动态页
        setFragment(new DynamicFragment());
        setToolBar(currentPage);
    }

    /**
     * 2017/5/22
     * 描述：根据选择的不同的底部按钮,设置不同toolbar状态
     */
    private void setToolBar(int position) {
        switch (position) {
            case 0:
                mToolbarMain.setTitleText(toolBarTitles[position]);
                break;
            case 1:
                mToolbarMain.setTitleText(toolBarTitles[position]);
                mToolbarMain.setBtnEnabled(true);
                mToolbarMain.setLeftImage(R.mipmap.nav_button_addfriend_default);
                mToolbarMain.setRightImage(R.mipmap.nav_button_publish_dynamic_default);
                mToolbarMain.setOnLeftClickListener(this);
                mToolbarMain.setOnRightClickListener(this);
                break;
            case 2:
                mToolbarMain.setTitleText(toolBarTitles[position]);
                break;
            case 3:
                mToolbarMain.setTitleText(toolBarTitles[position]);
                break;
        }
    }

    /**
     * 2017/5/22
     * 描述：创建fragment
     */
    private void setFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_main_container, fragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * 2017/5/23
     * 描述：顶部菜单栏左侧按钮的点击事件
     */
    @Override
    public void onLeftClick(View v) {
        switch (currentPage) {
            case 0:
                //当前页为首页

                break;
            case 1:
                //当前页为动态

                break;
            case 2:
                //当前页为发现

                break;
            case 3:
                //当前页为我的

                break;
        }
    }

    /**
     * 2017/5/23
     * 描述：顶部工具栏右侧按钮点击事件
     */
    @Override
    public void onRightClick(View v) {

        switch (currentPage) {
            case 0:
                //当前页为首页

                break;
            case 1:
                //当前页为动态

                break;
            case 2:
                //当前页为发现

                break;
            case 3:
                //当前页为我的

                break;
        }
    }
}
