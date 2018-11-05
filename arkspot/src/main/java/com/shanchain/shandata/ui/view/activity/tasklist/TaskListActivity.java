package com.shanchain.shandata.ui.view.activity.tasklist;

import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.google.gson.Gson;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RNPagesConstant;
import com.shanchain.data.common.cache.CommonCacheHelper;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.eventbus.SCBaseEvent;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.PrefUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.VersionUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.rn.fragment.RNMineFragment;
import com.shanchain.shandata.rn.fragment.RNSquareFragment;
import com.shanchain.shandata.rn.fragment.RNfragment;
import com.shanchain.shandata.ui.model.RNGDataBean;
import com.shanchain.shandata.ui.model.SpaceInfo;
import com.shanchain.shandata.ui.view.activity.chat.ContactActivity;
import com.shanchain.shandata.ui.view.activity.story.CreateDynamicActivity;
import com.shanchain.shandata.ui.view.activity.story.SelectContactActivity;
import com.shanchain.shandata.ui.view.activity.story.StoryTitleActivity;
import com.shanchain.shandata.ui.view.fragment.NewsFragment;
import com.shanchain.shandata.ui.view.fragment.StoryFragment;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import okhttp3.Call;

import static com.shanchain.data.common.base.Constants.CACHE_CUR_USER;
import static com.shanchain.data.common.base.Constants.CACHE_DEVICE_TOKEN_STATUS;
import static com.shanchain.data.common.base.Constants.CACHE_TOKEN;
import static com.shanchain.data.common.base.Constants.CACHE_USER_MSG_READ_STATUS;
import static com.shanchain.data.common.base.Constants.SP_KEY_DEVICE_TOKEN;
import static com.shanchain.data.common.rn.modules.NavigatorModule.REACT_PROPS;


public class TaskListActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener, ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnTitleClickListener, DefaultHardwareBackBtnHandler {

    @Bind(R.id.tb_main)
    ArthurToolBar mTbMain;
    @Bind(R.id.fl_main_container)
    FrameLayout mFlMainContainer;
    private int mFragmentId;
    private boolean isMsgRead = true;
    private TaskListFragment taskListFragment;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_task_list;
    }

    @Override
    protected void initViewsAndEvents() {

        Intent intent = getIntent();
        mFragmentId = intent.getIntExtra("fragmentId", 0);

        String uId = SCCacheUtils.getCache("0", CACHE_CUR_USER);
        String token = SCCacheUtils.getCache(uId, Constants.CACHE_TOKEN);
        String spaceId = SCCacheUtils.getCache(uId, Constants.CACHE_SPACE_ID);
        String characterId = SCCacheUtils.getCache(uId, Constants.CACHE_CHARACTER_ID);

        RNGDataBean rngDataBean = new RNGDataBean();
        rngDataBean.setUserId(uId);
        rngDataBean.setToken(token);
        rngDataBean.setSpaceId(spaceId);
        rngDataBean.setCharacterId(characterId);
        String jsonGData = JSON.toJSONString(rngDataBean);
        SCCacheUtils.setCache(uId, Constants.CACHE_GDATA, jsonGData);
        String cacheGData = SCCacheUtils.getCache(uId, Constants.CACHE_GDATA);
        LogUtils.i("缓存的gdata = " + cacheGData);

//        UMConfigure.setLogEnabled(true); //显示友盟log日记
        initToolBar();
        setFragment();

    }

    private void initToolBar() {
//        mTbMain = (ArthurToolBar) findViewById(R.id.tb_main);
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

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();

        if (taskListFragment == null) {
            taskListFragment = new TaskListFragment();
            fragmentTransaction.add(R.id.fl_main_container, taskListFragment);
        } else {
            fragmentTransaction.show(taskListFragment);
        }

        fragmentTransaction.commit();

    }

    /*
     * 初始化右侧导航栏点击事件
     * */
    @Override
    public void onRightClick(View v) {
        switch (mFragmentId) {
            case 0:
                ToastUtils.showToast(TaskListActivity.this,"右侧导航栏");
//                readyGo(CreateDynamicActivity.class);
//                overridePendingTransition(R.anim.activity_create_dynamic_in, R.anim.activity_anim_default);
                break;
            default:
                break;
        }
    }

    /*
    * 初始化左侧导航点击事件
    * */
    @Override
    public void onLeftClick(View v) {
        finish();
    }

    /*
    * 标题导航事件
    * */
    @Override
    public void onTitleClick(View v) {
        Intent intent = new Intent(this, StoryTitleActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter_alpha, R.anim.activity_anim_default);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean handled = false;
        Fragment activeFragment = getSupportFragmentManager().findFragmentById(R.id.fl_main_container);
        if (activeFragment instanceof RNfragment) {
            handled = ((RNfragment) activeFragment).onKeyUp(keyCode, event);
        }
        return handled || super.onKeyUp(keyCode, event);
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        onBackPressed();
    }

    @Subscribe
    public void onEventMainThread(SCBaseEvent event) {
        if (event.receiver.equalsIgnoreCase(EventConstant.EVENT_MODULE_ARKSPOT) && event.key.equalsIgnoreCase(EventConstant.EVENT_KEY_NEW_MSG)) {
            isMsgRead = false;
        }
    }

}
