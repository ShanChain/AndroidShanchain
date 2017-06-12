package com.shanchain.mvp.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
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
import com.shanchain.utils.DensityUtils;
import com.shanchain.utils.LogUtils;
import com.shanchain.utils.ToastUtils;
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

    private String[] navigationBarTitles;

    private boolean isExit;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents() {
        int height = getWindowManager().getDefaultDisplay().getHeight();
        int i = DensityUtils.px2dip(this, height);
        LogUtils.d("屏幕高度为;" + i);
        navigationBarTitles = getResources().getStringArray(R.array.main_tab_name);
        initTooBar();

        //初始化底部导航栏
        initBottomNavigationBar();

    }


    /**
     * 2017/5/18
     * 描述：初始化工具栏，设置沉浸式
     */
    private void initTooBar() {
        mToolbarMain = (ArthurToolBar) findViewById(R.id.toolbar_main);
        currentPage = 0;
        mToolbarMain.setTitleText(navigationBarTitles[currentPage]);
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
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_homepager, navigationBarTitles[0]))
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_dynamic, navigationBarTitles[1]))
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_find, navigationBarTitles[2]))
                .addItem(new BottomNavigationItem(R.drawable.selector_tab_mine, navigationBarTitles[3]))
                .setFirstSelectedPosition(0)
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
        setFragment(new HomeFragment());
        setToolBar(currentPage);
    }

    /**
     * 2017/5/22
     * 描述：根据选择的不同的底部按钮,设置不同toolbar状态
     */
    private void setToolBar(int position) {
        switch (position) {
            case 0:
                mToolbarMain.setTitleText(navigationBarTitles[position]);
                mToolbarMain.setRightImage(R.mipmap.home_nav_icon_default);
                mToolbarMain.setBtnEnabled(false,true);
                mToolbarMain.setBtnVisibility(false,true);
                mToolbarMain.setOnRightClickListener(this);
                break;
            case 1:
                mToolbarMain.setTitleText(navigationBarTitles[position]);
                mToolbarMain.setBtnEnabled(true);
                mToolbarMain.setBtnVisibility(true,true);
                mToolbarMain.setLeftImage(R.mipmap.nav_button_addfriend_default);
                mToolbarMain.setRightImage(R.mipmap.nav_button_publish_dynamic_default);
                mToolbarMain.setOnLeftClickListener(this);
                mToolbarMain.setOnRightClickListener(this);
                break;
            case 2:
                mToolbarMain.setTitleText(navigationBarTitles[position]);

                break;
            case 3:
                mToolbarMain.setTitleText(navigationBarTitles[position]);
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
                readyGo(AddFriendActivity.class);
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
                ToastUtils.showToast(this,"首页。。。");
                break;
            case 1:
                //当前页为动态
                readyGo(ReleaseDynamicActivity.class);
                break;
            case 2:
                //当前页为发现

                break;
            case 3:
                //当前页为我的

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isExit = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (isExit){
                finish();
            }else {
                isExit = true;
                ToastUtils.showToast(MainActivity.this,"再按一次退出程序");
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
