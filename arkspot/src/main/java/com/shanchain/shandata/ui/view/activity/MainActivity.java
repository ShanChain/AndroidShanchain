package com.shanchain.shandata.ui.view.activity;

import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
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


public class MainActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener, ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnTitleClickListener, DefaultHardwareBackBtnHandler {

    ArthurToolBar mTbMain;
    @Bind(R.id.fl_main_container)
    FrameLayout mFlMainContainer;
    @Bind(R.id.bnb)
    BottomNavigationBar mBnb;
    private int mFragmentId;
    private String[] navigationBarTitles = {"故事", "对话", "广场", "我的"};
    private BadgeItem mStoryBadge;
    private BadgeItem mNewsBadge;
    private BadgeItem mSquareBadge;
    private BadgeItem mMineBadge;
    private long downloadId;
    private boolean isMsgRead = true;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
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


        initToolBar();
        //初始化底部导航栏
        initBottomNavigationBar();
        //检查apk版本
        checkApkVersion();
        initDeviceToken();

    }

    private void initDeviceToken() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                setDeviceToken();
            }
        });
        thread.start();
    }

    private void checkApkVersion() {
        final String localVersion = VersionUtils.getVersionName(mContext);
        SCHttpUtils.postNoToken()
                .url(HttpApi.OSS_APK_GET_LASTEST)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取版本信息失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到版本信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                String forceUpdate = JSONObject.parseObject(data).getString("forceUpdate");
                                boolean force = Boolean.parseBoolean(forceUpdate);
                                String url = JSONObject.parseObject(data).getString("url");
                                String version = JSONObject.parseObject(data).getString("version");
                                boolean isUpdata = VersionUtils.compareVersion(localVersion, version);
                                if (isUpdata) {
                                    showUpdateDialog(url, force, version);
                                } else {
                                    return;
                                }

                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showUpdateDialog(final String url, final boolean force, String version) {
        String msg = "";
        if (force) {
            msg = "新版本有较大改进，马上更新吧";
        } else {
            msg = "确定要更新吗？";
        }
        final StandardDialog dialog = new StandardDialog(this);
        dialog.setStandardTitle("发现新版本 (" + version + ")");
        dialog.setStandardMsg(msg);
        dialog.setSureText("确定");
        dialog.setCancelText("取消");
        dialog.setCallback(new Callback() {
            @Override
            public void invoke() {  //确定
                downLoadApk(url);
            }
        }, new Callback() {
            @Override
            public void invoke() {  //取消
                if (force) {
                    dialog.dismiss();
                    ActivityStackManager.getInstance().finishAllActivity();
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
        dialog.setCancelable(!force);
        dialog.setCanceledOnTouchOutside(!force);
    }

    private void downLoadApk(String url) {
        DownloadManager manager;

        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        query.setFilterByStatus(DownloadManager.STATUS_RUNNING);//正在下载
        Cursor c = manager.query(query);
        if (c.moveToNext()) {
        } else {
            DownloadManager.Request down = new DownloadManager.Request(Uri.parse(url));
            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            down.setVisibleInDownloadsUi(true);
            down.setTitle("马甲");
            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//            //down.setDestinationInExternalFilesDir(this, null, "arkspot-release.apk");
//            String filePath = getCacheDir().getAbsoluteFile() + "arkspot-release.apk";
//            File apkFile = new File(filePath);
//            Uri.withAppendedPath(Uri.fromFile(getCacheDir()),"arkspot-release.apk");
            down.setDestinationUri(Uri.withAppendedPath(Uri.fromFile(getExternalCacheDir()), "arkspot-release.apk"));
            downloadId = manager.enqueue(down);
        }
    }


    private void initToolBar() {
        mTbMain = (ArthurToolBar) findViewById(R.id.tb_main);
    }

    private void initBottomNavigationBar() {
        BottomNavigationItem btmItemStory = new BottomNavigationItem(R.drawable.selector_tab_story, navigationBarTitles[0]);
        mStoryBadge = new BadgeItem();
        mStoryBadge.hide();
        //btmItemStory.setBadgeItem(mStoryBadge);
        BottomNavigationItem btmItemNews = new BottomNavigationItem(R.drawable.selector_tab_news, navigationBarTitles[1]);
        mNewsBadge = new BadgeItem();
        mNewsBadge.hide();
        //btmItemNews.setBadgeItem(mNewsBadge);
        BottomNavigationItem btmItemSquare = new BottomNavigationItem(R.drawable.selector_tab_square, navigationBarTitles[2]);
        mSquareBadge = new BadgeItem();
        mSquareBadge.hide();
        //btmItemSquare.setBadgeItem(mSquareBadge);
        BottomNavigationItem btmItemMine = new BottomNavigationItem(R.drawable.selector_tab_mine, navigationBarTitles[3]);
        mMineBadge = new BadgeItem();
        mMineBadge.setText("  ").setBorderWidth(DensityUtils.dip2px(mContext, 3)).setBorderColor(getResources().getColor(R.color.colorWhite));
        String userId = CommonCacheHelper.getInstance().getCache("0", CACHE_CUR_USER);
        String msgReadStatus = CommonCacheHelper.getInstance().getCache(userId, CACHE_USER_MSG_READ_STATUS);
        btmItemMine.setBadgeItem(mMineBadge);
        if (!TextUtils.isEmpty(msgReadStatus) && !Boolean.parseBoolean(msgReadStatus)) {
            isMsgRead = Boolean.parseBoolean(msgReadStatus);
            mMineBadge.show();
        } else {
            isMsgRead = true;
            mMineBadge.hide();
        }

        mBnb.setActiveColor(R.color.colorActive)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setInActiveColor(R.color.colorInactive)
                .addItem(btmItemStory)
                .addItem(btmItemNews)
                .addItem(btmItemSquare)
                .addItem(btmItemMine)
                .setFirstSelectedPosition(mFragmentId)
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
                    default:
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    private StoryFragment mStoryFragment;
    private NewsFragment mNewsFragment;
    private RNSquareFragment mSquareFragment;
    private RNMineFragment mMineFragment;

    private void setFragment(int position) {

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();

        hideAllFragment(fragmentTransaction);

        switch (position) {
            case 0:
                if (mStoryFragment == null) {
                    mStoryFragment = new StoryFragment();
                    fragmentTransaction.add(R.id.fl_main_container, mStoryFragment);
                } else {
                    fragmentTransaction.show(mStoryFragment);
                }
                break;
            case 1:
                if (mNewsFragment == null) {
                    mNewsFragment = new NewsFragment();
                    fragmentTransaction.add(R.id.fl_main_container, mNewsFragment);
                } else {
                    fragmentTransaction.show(mNewsFragment);
                }
                break;
            case 2:
                if (mSquareFragment == null) {
                    mSquareFragment = new RNSquareFragment();
                    fragmentTransaction.add(R.id.fl_main_container, mSquareFragment);
                } else {
                    fragmentTransaction.show(mSquareFragment);
                }
                break;
            case 3:
                if (mMineFragment == null) {
                    mMineFragment = new RNMineFragment();
                    fragmentTransaction.add(R.id.fl_main_container, mMineFragment);
                } else {
                    fragmentTransaction.show(mMineFragment);
                }
                break;
            default:
                break;
        }

        fragmentTransaction.commit();

    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (mStoryFragment != null) {
            fragmentTransaction.hide(mStoryFragment);
        }

        if (mSquareFragment != null) {
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
            default:
                break;
        }
    }

    private void setToolBar(int position) {

        String uId = SCCacheUtils.getCache("0", CACHE_CUR_USER);
        String spaceInfo = SCCacheUtils.getCache(uId, Constants.CACHE_SPACE_INFO);
        SpaceInfo spaceDetailInfo = new Gson().fromJson(spaceInfo, SpaceInfo.class);
        String name = spaceDetailInfo.getName();

        mTbMain.setTitleText(name);
        TextView titleStory = mTbMain.getTitleView();
        Drawable drawable = getResources().getDrawable(R.mipmap.abs_home_btn_dropdown_default);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        titleStory.setCompoundDrawables(null, null, drawable, null);
        titleStory.setCompoundDrawablePadding(DensityUtils.dip2px(this, 4));

        switch (position) {
            case 0:
                mTbMain.setRightImage(R.mipmap.abs_home_btn_dynamic_default);
                mTbMain.setOnTitleClickListener(this);
                mTbMain.setBtnEnabled(false, true);
                mTbMain.setBtnVisibility(false, true);
                mTbMain.setOnRightClickListener(this);
                break;
            case 1:
                mTbMain.setBtnEnabled(false, true);
                mTbMain.setBtnVisibility(false, true);
                mTbMain.setRightImage(R.mipmap.abs_home_btn_more_default);
                mTbMain.setOnRightClickListener(this);
                break;
            case 2:
                mTbMain.setRightImage(R.mipmap.abs_home_btn_more_default);
                mTbMain.setOnTitleClickListener(this);
                mTbMain.setBtnVisibility(false, false);
                mTbMain.setBtnEnabled(false, false);
                mTbMain.setOnRightClickListener(this);
                break;
            case 3:
                if (isMsgRead) {
                    mTbMain.setRightImage(R.mipmap.abs_mine_btn_information_default);
                } else {
                    mTbMain.setRightImage(R.mipmap.abs_mine_btn_haveinformation_default);
                }
                mTbMain.setBtnEnabled(false, true);
                mTbMain.setBtnVisibility(false, true);
                mTbMain.setOnRightClickListener(this);
                break;
            default:
                break;
        }
    }


    @Override
    public void onRightClick(View v) {
        switch (mFragmentId) {
            case 0:
                readyGo(CreateDynamicActivity.class);
                overridePendingTransition(R.anim.activity_create_dynamic_in, R.anim.activity_anim_default);
                break;
            case 1:
                newsRightClick();
                break;
            case 2:
                squareRightClick();
                break;
            case 3:
                notifyRightClick();
                break;
            default:
                break;
        }
    }

    private void notifyRightClick() {
        String userId = CommonCacheHelper.getInstance().getCache("0", CACHE_CUR_USER);
        CommonCacheHelper.getInstance().setCache(userId, CACHE_USER_MSG_READ_STATUS, "true");
        mMineBadge.hide();
        isMsgRead = true;
        Bundle bundle = new Bundle();
        JSONObject screenProps = new JSONObject();
        screenProps.put(Constants.CACHE_GDATA, JSONObject.parse(CommonCacheHelper.getInstance().getCache("0", Constants.CACHE_GDATA)));
        bundle.putString(REACT_PROPS, screenProps.toString());
        NavigatorModule.startReactPage(this, RNPagesConstant.NotificationScreen, bundle);
    }

    private void newsRightClick() {
        final CustomDialog customDialog = new CustomDialog(this, true, 1, R.layout.dialog_msg_bottom, new int[]{R.id.tv_dialog_msg_new,
                R.id.tv_dialog_msg_focus, R.id.tv_dialog_msg_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {

                switch (view.getId()) {
                    case R.id.tv_dialog_msg_new:
                        //邀请好友，创建新场景（群）
                        Intent invitationIntent = new Intent(MainActivity.this, SelectContactActivity.class);
                        invitationIntent.putExtra("isAt", false);
                        startActivity(invitationIntent);
                        customDialog.dismiss();
                        break;
                    case R.id.tv_dialog_msg_focus:
                        //互相关注
                        Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                        startActivity(intent);
                        customDialog.dismiss();
                        break;
                    /*case R.id.tv_dialog_msg_code:
                        //扫码
                        readyGo(FindSceneActivity.class);
                        customDialog.dismiss();
                        break;*/
                    case R.id.tv_dialog_msg_cancel:
                        //取消
                        // readyGo(MeetPersonActivity.class);
                        customDialog.dismiss();
                        break;
                    default:
                        break;
                }

            }
        });
        customDialog.show();
    }

    private void squareRightClick() {
        final CustomDialog customDialog = new CustomDialog(this, true, 1, R.layout.dialog_square_msg_bottom, new int[]{
                R.id.tv_dialog_msg_background_img, R.id.tv_dialog_msg_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                Bundle bundle = new Bundle();
                JSONObject screenProps = new JSONObject();
                screenProps.put(Constants.CACHE_GDATA, JSONObject.parse(CommonCacheHelper.getInstance().getCache("0", Constants.CACHE_GDATA)));
                bundle.putString(REACT_PROPS, screenProps.toString());
                switch (view.getId()) {
                    case R.id.tv_dialog_msg_headlines:
                        NavigatorModule.startReactPage(view.getContext(), RNPagesConstant.HeadlinesScreen, new Bundle());
                        customDialog.dismiss();
                        break;
                    case R.id.tv_dialog_msg_background_img:
                        NavigatorModule.startReactPage(view.getContext(), RNPagesConstant.SpaceBGImgScreen, bundle);
                        customDialog.dismiss();
                        break;
                    case R.id.tv_dialog_msg_intro:
                        NavigatorModule.startReactPage(view.getContext(), RNPagesConstant.SpaceIntroScreen, bundle);
                        customDialog.dismiss();
                        break;
                    case R.id.tv_dialog_msg_cancel:
                        customDialog.dismiss();
                        break;
                    default:
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
            default:
                break;
        }
    }

    @Override
    public void onTitleClick(View v) {

        Intent intent = new Intent(this, StoryTitleActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter_alpha, R.anim.activity_anim_default);

        switch (mFragmentId) {
            case 0:
                break;
            case 1:

                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
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
    public void onBackPressed() {
        //super.onBackPressed();
        //复写back键，主页点击back不finish页面
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

    /**
     * 描述：设置底部栏红点显示的内容
     *
     * @param position 刷新哪个位置的红点
     * @param num      显示的数值
     */
    private void setRedPoint(int position, String num) {
        switch (position) {
            case 0:
                if (mStoryBadge.isHidden()) {
                    mStoryBadge.show();
                }
                mStoryBadge.setText(num);
                break;
            case 1:
                if (mNewsBadge.isHidden()) {
                    mNewsBadge.show();
                }
                mNewsBadge.setText(num);
                break;
            case 2:
                if (mSquareBadge.isHidden()) {
                    mSquareBadge.show();
                }
                mSquareBadge.setText(num);
                break;
            case 3:
                if (mMineBadge.isHidden()) {
                    mMineBadge.show();
                }
                mMineBadge.setText(num);
                break;
        }
    }

    /**
     * 描述：隐藏底部栏的红点
     *
     * @param position 隐藏的红点的位置
     */
    private void hideRedPoint(int position) {
        switch (position) {
            case 0:
                if (!mStoryBadge.isHidden()) {
                    mStoryBadge.hide();
                }
                break;
            case 1:
                if (!mNewsBadge.isHidden()) {
                    mNewsBadge.hide();
                }
                break;
            case 2:
                if (!mSquareBadge.isHidden()) {
                    mSquareBadge.hide();
                }
                break;
            case 3:
                if (!mMineBadge.isHidden()) {
                    mMineBadge.hide();
                }
                break;
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        onBackPressed();
    }

    @Subscribe
    public void onEventMainThread(SCBaseEvent event) {
        if (event.receiver.equalsIgnoreCase(EventConstant.EVENT_MODULE_ARKSPOT) && event.key.equalsIgnoreCase(EventConstant.EVENT_KEY_NEW_MSG)) {
            isMsgRead = false;
            mMineBadge.show();
        }
    }


    private void setDeviceToken() {
        final String userId = SCCacheUtils.getCacheUserId();

        if (!TextUtils.isEmpty(CommonCacheHelper.getInstance().getCache(userId, CACHE_DEVICE_TOKEN_STATUS)) && CommonCacheHelper.getInstance().getCache(userId, CACHE_DEVICE_TOKEN_STATUS).equalsIgnoreCase("true")) {
            return;
        }
        String token = SCCacheUtils.getCache(userId, CACHE_TOKEN);
        if (TextUtils.isEmpty(token)) {
            return;
        }
        if (!TextUtils.isEmpty(PrefUtils.getString(AppManager.getInstance().getContext(), SP_KEY_DEVICE_TOKEN, ""))) {
            SCHttpUtils.postWithUserId()
                    .url(HttpApi.SET_DEVICE_TOKEN)
                    .addParams("osType", "android")
                    .addParams("token", token)
                    .addParams("deviceToken", PrefUtils.getString(AppManager.getInstance().getContext(), SP_KEY_DEVICE_TOKEN, ""))
                    .build()
                    .execute(new SCHttpStringCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.i("设置DeviceToken失败");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                CommonCacheHelper.getInstance().setCache(userId, CACHE_DEVICE_TOKEN_STATUS, "true");
                            }

                        }
                    });
        }
    }
}
