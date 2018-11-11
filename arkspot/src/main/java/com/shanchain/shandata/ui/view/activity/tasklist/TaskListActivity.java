package com.shanchain.shandata.ui.view.activity.tasklist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.eventbus.SCBaseEvent;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TaskPagerAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.rn.fragment.RNfragment;
import com.shanchain.shandata.ui.view.activity.story.StoryTitleActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.shanchain.data.common.base.Constants.CACHE_CUR_USER;


public class TaskListActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
        ArthurToolBar.OnRightClickListener,
        ArthurToolBar.OnLeftClickListener,DefaultHardwareBackBtnHandler {

    @Bind(R.id.tb_main)
    ArthurToolBar mTbMain;
    @Bind(R.id.tab_task)
    TabLayout tabTask;
    @Bind(R.id.vp_task)
    ViewPager vpTask;

    private int mFragmentId;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_task_list;
    }

    @Override
    protected void initViewsAndEvents() {

        String uId = SCCacheUtils.getCache("0", CACHE_CUR_USER);
        String token = SCCacheUtils.getCache(uId, Constants.CACHE_TOKEN);
        String spaceId = SCCacheUtils.getCache(uId, Constants.CACHE_SPACE_ID);
        String characterId = SCCacheUtils.getCache(uId, Constants.CACHE_CHARACTER_ID);

        initToolBar();
        setFragment();

    }

    private void initToolBar() {

        mTbMain.setLeftImage(R.mipmap.back);
        mTbMain.setRightImage(R.mipmap.nav_task_add);
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
        String[] titles = {"任务列表","我的任务"};
        TaskPagerAdapter adapter = new TaskPagerAdapter(getSupportFragmentManager(), titles);
        vpTask.setOffscreenPageLimit(2);
        vpTask.setAdapter(adapter);
        tabTask.setupWithViewPager(vpTask);
        vpTask.setCurrentItem(0);
    }

    /*
     * 初始化右侧导航栏点击事件
     * */
    @Override
    public void onRightClick(View v) {


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

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
