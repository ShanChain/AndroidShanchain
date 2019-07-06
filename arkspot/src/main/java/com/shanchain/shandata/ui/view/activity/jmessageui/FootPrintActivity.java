package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
//import com.example.test_webview_demo.BrowserActivity;
//import com.example.test_webview_demo.MainActivity;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.nostra13.universalimageloader.utils.L;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.EventBusObject;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.ImageUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.SCUploadImgHelper;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.VersionUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.HotChatRoomAdapter;
import com.shanchain.shandata.adapter.ImagePickerAdapter;
import com.shanchain.shandata.adapter.TaskPagerAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.event.EventMessage;
//import com.shanchain.shandata.rn.activity.X5WebViewActivity;
import com.shanchain.shandata.rn.activity.SCWebViewActivity;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.Coordinates;
import com.shanchain.shandata.ui.model.CouponSubInfo;
import com.shanchain.shandata.ui.model.HotChatRoom;
import com.shanchain.shandata.ui.model.JmAccount;
import com.shanchain.shandata.ui.model.ModifyUserInfo;
import com.shanchain.shandata.ui.model.SearchResult;
import com.shanchain.shandata.ui.view.activity.HomeActivity;
import com.shanchain.shandata.ui.view.activity.ModifyUserInfoActivity;
import com.shanchain.shandata.ui.view.activity.coupon.CouponListActivity;
import com.shanchain.shandata.ui.view.activity.coupon.MyCouponListActivity;
import com.shanchain.shandata.ui.view.activity.login.LoginActivity;
import com.shanchain.shandata.ui.view.activity.settings.SettingsActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskDetailActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskListActivity;
import com.shanchain.shandata.ui.view.fragment.MainARSGameFragment;
import com.shanchain.shandata.ui.view.fragment.MainChatRoomFragment;
import com.shanchain.shandata.utils.GlideImageLoader;
import com.shanchain.shandata.utils.RequestCode;
import com.shanchain.shandata.utils.TextSearcher;
import com.shanchain.shandata.widgets.arcMenu.ArcMenu;
import com.shanchain.shandata.widgets.other.MyLoadMoreView;
import com.shanchain.shandata.widgets.photochoose.DialogCreator;
import com.shanchain.shandata.widgets.photochoose.PhotoUtils;
import com.shanchain.shandata.widgets.pickerimage.PickImageActivity;
import com.shanchain.shandata.widgets.pickerimage.utils.Extras;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jiguang.imui.model.DefaultUser;
import cn.jiguang.imui.model.MyMessage;
import cn.jiguang.imui.view.RoundImageView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.content.VideoContent;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;

public class FootPrintActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        ArthurToolBar.OnRightClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {

    private ArthurToolBar arthurToolBar;
    private LinearLayout linearFootPrint;
    private RecyclerView reviewFoodPrint;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private TextView userNikeView;
    private TextView tvUserSign;
    private TextView tvBtnCoupon, tvBtnTask;
    private LinearLayout linearAddChatRoom;
    private SearchView mSearchView;
    private ImageView ivUserModify;
    private ImageView userHeadView;
    private View footerView;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    BGARefreshLayout bgaRefreshLayout;
    private List<HotChatRoom> hotChatRoomList,
            searchRoomList = new ArrayList<>(),
            adapterChatRoomList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList();
    private UserInfo mMyInfo;
    private Coordinates mCoordinates;
    private String localRoomName, localRoomID;
    private String localVersion;
    private boolean isIn;
    private int pageNo = 0, searchPage = 0, size = 10, currentPage, totalPage = 1;
    private boolean last = false, isLoadMore = false;
    private ArcMenu.OnMenuItemClickListener onMenuItemClickListener;
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.shanchain.shandata.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private MyMessageReceiver mMyMessageReceiver;
    private BaseQuickAdapter mQuickAdapter;

    @Override
    protected int getContentViewLayoutID() {
//        return R.layout.activity_food_print;
        return R.layout.activity_foot_print_seting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initJPushUser();
    }

    /**
     * 登录极光账号
     */
    private void initJPushUser(){
        JMessageClient.registerEventReceiver(this);
        JMessageClient.login(SCCacheUtils.getCacheHxUserName(), SCCacheUtils.getCacheHxPwd(), new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    LogUtils.d(TAG, "极光账号登录成功");
                } else {
                    LogUtils.d(TAG, "极光账号登录失败");
                    ToastUtils.showToastLong(mContext, getString(R.string.login_faile_again));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            readyGo(LoginActivity.class);
                        }
                    }, 3000);
                }
            }
        });
    }

    @Override
    public void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        if (mQuickAdapter != null) {
//            pageNo = 0;
//            isLoadMore = false;
//            initData(pageNo, size);
//            mQuickAdapter.notifyLoadMoreToLoading();
//        }
    }

    @Override
    public void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void initViewsAndEvents() {
        try {
            initToolBar();
            initView();
            setFragment();
            initData(pageNo, size, bgaRefreshLayout);
            //注册自定义消息广播
//        registerMessageReceiver();
            initMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData(int pageNo, int size, final BGARefreshLayout refreshLayout) {
//        showLoadingDialog();
        SCHttpUtils.get()
                .url(HttpApi.HOT_CHAT_ROOM)
                .addParams("token", SCCacheUtils.getCacheToken() + "")
                .addParams("version", localVersion + "")
                .addParams("page", pageNo + "")
                .addParams("size", size + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeLoadingDialog();
                        LogUtils.d("网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = com.alibaba.fastjson.JSONObject.parseObject(response).getString("code");
                        if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                            String data = com.alibaba.fastjson.JSONObject.parseObject(response).getString("data");
                            String content = SCJsonUtils.parseString(data, "content");
                            String allPage = JSONObject.parseObject(data).getString("totalPages");
                            totalPage = Integer.valueOf(allPage);
//                                last = SCJsonUtils.parseBoolean(data, "last");
                            String isLast = JSONObject.parseObject(data).getString("last");
                            last = Boolean.valueOf(isLast);
                            hotChatRoomList = JSONArray.parseArray(content, HotChatRoom.class);
//                            hotChatRoomList = JSONArray.parseArray(data, HotChatRoom.class);
                            if (mQuickAdapter != null && hotChatRoomList != null) {
                                mQuickAdapter.replaceData(hotChatRoomList);
                                if (refreshLayout != null) {
                                    refreshLayout.endRefreshing();
                                }
                            }
                        }
                        closeLoadingDialog();
                    }
                });
    }

    private void initView() {
        linearFootPrint = findViewById(R.id.linear_foot_print);
        reviewFoodPrint = findViewById(R.id.review_food_print);
        footerView = LayoutInflater.from(this).inflate(R.layout.view_load_more, null);
        reviewFoodPrint.setVisibility(View.VISIBLE);
        linearFootPrint.setVisibility(View.GONE);
        tvBtnCoupon = findViewById(R.id.text_btn_coupon);
        tvBtnTask = findViewById(R.id.text_btn_task);
        linearAddChatRoom = findViewById(R.id.linear_add_chat_room);
        bgaRefreshLayout = findViewById(R.id.refresh_layout);
//        mSearchView = findViewById(R.id.search_view);
        bgaRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this, true);//微博效果
        // 设置正在加载更多时的文本
//        refreshViewHolder.setLoadingMoreText("加载更多");
        // 设置正在加载更多时显示加载更多控件
        bgaRefreshLayout.setIsShowLoadingMoreView(true);
        bgaRefreshLayout.beginLoadingMore();
        LogUtils.d("刷新列表 开始", bgaRefreshLayout.isLoadingMore() + "");
        bgaRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        showLoadingDialog();
        localVersion = VersionUtils.getVersionName(mContext);
        SCHttpUtils.get()
                .url(HttpApi.HOT_CHAT_ROOM)
                .addParams("token", SCCacheUtils.getCacheToken() + "")
                .addParams("version", localVersion + "")
                .addParams("page", pageNo + "")
                .addParams("size", size + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeLoadingDialog();
                        LogUtils.d("网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = com.alibaba.fastjson.JSONObject.parseObject(response).getString("code");
                        if (NetErrCode.COMMON_SUC_CODE.equals(code)) {
                            String data = com.alibaba.fastjson.JSONObject.parseObject(response).getString("data");
                            String content = SCJsonUtils.parseString(data, "content");
                            totalPage = SCJsonUtils.parseInt(data, "totalPage");
                            String isLast = JSONObject.parseObject(response).getString("last");
                            last = Boolean.valueOf(isLast);
                            hotChatRoomList = JSONArray.parseArray(content, HotChatRoom.class);
                            if (hotChatRoomList == null) {
                                return;
                            }
                            //                                intent.putExtra("isInCharRoom", isIn);
                            mQuickAdapter = new HotChatRoomAdapter(FootPrintActivity.this, hotChatRoomList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(FootPrintActivity.this, LinearLayoutManager.VERTICAL, false);
                            reviewFoodPrint.setLayoutManager(layoutManager);
                            mQuickAdapter.setLoadMoreView(new MyLoadMoreView());
                            mQuickAdapter.setFooterView(footerView);
                            initLoadMoreListener();
                            reviewFoodPrint.setAdapter(mQuickAdapter);
                            mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    //获取当前item的对象
                                    HotChatRoom hotChatRoom = (HotChatRoom) adapter.getItem(position);
                                    isInBlacklist(hotChatRoom.getRoomId(), hotChatRoom);
                                }
                            });
                        }
                        closeLoadingDialog();
                    }
                });
        initDrawer();
        //底部邀按钮
        tvBtnCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent couponIntent = new Intent(FootPrintActivity.this, CouponListActivity.class);
                couponIntent.putExtra("roomId", SCCacheUtils.getCacheRoomId() + "");
                startActivity(couponIntent);
            }
        });
        //底部帮按钮
        tvBtnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taskIntent = new Intent(FootPrintActivity.this, TaskDetailActivity.class);
                taskIntent.putExtra("roomId", SCCacheUtils.getCacheRoomId() + "");
                startActivity(taskIntent);
            }
        });
        //底部添加按钮
        linearAddChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FootPrintActivity.this, HomeActivity.class);
                intent.putExtra("isAddRoom", true);
                startActivity(intent);
            }
        });
    }

    private void setFragment() {
        mViewPager = findViewById(R.id.vp_main);
        mTabLayout = findViewById(R.id.tab_layout_main);
        String[] titles = {getString(R.string.hot_meta), "ARS",};
        fragmentList.add(new MainChatRoomFragment());
        fragmentList.add(new MainARSGameFragment());
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

    private void initToolBar() {
        arthurToolBar = (ArthurToolBar) findViewById(R.id.toolbar_nav);
//        arthurToolBar.isShowChatRoom(false);//不在导航栏显示聊天室信息
        arthurToolBar.setTitleText("");
//        arthurToolBar.setTitleTextColor(getResources().getColor(R.color.colorTextDefault));
//        arthurToolBar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//        arthurToolBar.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        arthurToolBar.setRightImage(R.mipmap.home_nav_map);
        arthurToolBar.setOnFavoriteClickListener(null);
        arthurToolBar.setOnLeftClickListener(this);
        arthurToolBar.setOnRightClickListener(this);

        //设置头像
        final UserInfo userInfo = JMessageClient.getMyInfo();
        if (userInfo != null && userInfo.getAvatarFile() != null) {
            arthurToolBar.setUserHeadImg(FootPrintActivity.this, userInfo.getAvatarFile().getAbsolutePath());
        } else {
            arthurToolBar.setUserHeadImg(FootPrintActivity.this, SCCacheUtils.getCacheHeadImg());
        }
    }

    private void initDrawer() {
        /*
         * 初始化侧滑栏
         * */
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


//        drawer.setStatusBarBackgroundColor(Color.parseColor("#00000"));
        drawer.setStatusBarBackground(R.drawable.selector_bg_msg_send_theme);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        arthurToolBar.setOnUserHeadClickListener(new ArthurToolBar.OnUserHeadClickListener() {
            @Override
            public void onUserHeadClick(View v) {
//                ToastUtils.showToast(MessageListActivity.this,"头像按钮");
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                toggle.onDrawerOpened(drawer);
            }
        });

        View headView = navigationView.getHeaderView(0);
        userNikeView = headView.findViewById(R.id.tv_nike_name);//用户昵称
        tvUserSign = headView.findViewById(R.id.tv_user_sign);//用户签名
        ivUserModify = headView.findViewById(R.id.iv_user_modify);//修改资料按钮
        userHeadView = headView.findViewById(R.id.iv_user_head);//用户头像

        initUserInfo();
        //初始化图片选择器
        pickImages();
//        initWidget();
        userNikeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FootPrintActivity.this, ModifyUserInfoActivity.class);
                String nikeName = userNikeView.getText().toString();
                String userSign = tvUserSign.getText().toString();
                intent.putExtra("nikeName", nikeName);
                intent.putExtra("userSign", userSign);
                startActivity(intent);
            }
        });

        userHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置头像
                int from = PickImageActivity.FROM_LOCAL;
                int requestCode = PhotoUtils.INTENT_SELECT;
                if (ContextCompat.checkSelfPermission(FootPrintActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FootPrintActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                } else {
//            PickImageActivity.start(MessageListActivity.this, requestCode, from, tempFile(), true, 1,
//                    true, false, 0, 0);
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, requestCode);
                }
            }
        });

        //修改用户资料
        ivUserModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FootPrintActivity.this, ModifyUserInfoActivity.class);
                String charater = SCCacheUtils.getCacheCharacterInfo();
                CharacterInfo characterInfo = JSONObject.parseObject(charater, CharacterInfo.class);
                String headImg = characterInfo.getHeadImg();
                String cacheHeadImg = SCCacheUtils.getCacheHeadImg();
                intent.putExtra("headImg", SCCacheUtils.getCacheHeadImg());
                startActivity(intent);
            }
        });

        /*
         * 初始化悬浮按钮
         * */
//        onMenuItemClickListener = new ArcMenu.OnMenuItemClickListener() {
//            @Override
//            public void onClick(View view, int pos) {
////                mChatView.getChatInputView().setFocusable(false);
//                switch (view.getId()) {
//                    //马甲劵
//                    case R.id.linear_add_coupon:
//                        arthurToolBar.getChildAt(0).findViewWithTag("circelText").setBackground(getResources().getDrawable(R.drawable.shape_guide_point_default));
//                        Intent couponIntent = new Intent(FootPrintActivity.this, CouponListActivity.class);
////                        couponIntent.putExtra("roomId", roomID);
//                        startActivity(couponIntent);
//                        break;
//                    //查询任务
//                    case R.id.linear_add_query:
//                        arthurToolBar.getChildAt(0).findViewWithTag("circelText").setBackground(getResources().getDrawable(R.drawable.shape_guide_point_default));
//                        Intent taskIntent = new Intent(FootPrintActivity.this, TaskDetailActivity.class);
////                        taskIntent.putExtra("roomId", roomID);
//                        startActivity(taskIntent);
//                        break;
//                    //添加任务
//                    case R.id.linear_play:
//                        arthurToolBar.getChildAt(0).findViewWithTag("circelText").setBackground(getResources().getDrawable(R.drawable.shape_guide_point_default));
//                        /*点亮活动信息*/
//                        SCHttpUtils.get()
//                                .url(HttpApi.LIGHT_ACTIVE)
//                                .addParams("token", SCCacheUtils.getCacheToken() + "")
//                                .build()
//                                .execute(new SCHttpStringCallBack() {
//                                    @Override
//                                    public void onError(Call call, Exception e, int id) {
//                                        com.shanchain.data.common.utils.LogUtils.d("####### GET_LIGHT_ACTIVE 请求失败 #######");
//                                    }
//
//                                    @Override
//                                    public void onResponse(String response, int id) {
//                                        com.shanchain.data.common.utils.LogUtils.d("####### GET_LIGHT_ACTIVE 请求成功 #######");
//                                        String code = JSONObject.parseObject(response).getString("code");
//                                        if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
//                                            String data = JSONObject.parseObject(response).getString("data") != null ? JSONObject.parseObject(response).getString("data") : "暂无活动";
//                                            if (data.equals("暂无活动")) {
//                                                ToastUtils.showToastLong(FootPrintActivity.this, "新玩法开发中，敬请期待");
//                                            } else {
//                                                String ruleDescribe = JSONObject.parseObject(data).getString("ruleDescribe");
//                                                String startTme = JSONObject.parseObject(data).getString("startTime");
//                                                String endTime = JSONObject.parseObject(data).getString("endTime");
//                                                if (System.currentTimeMillis() > Long.valueOf(endTime)) {
//                                                    ToastUtils.showToastLong(FootPrintActivity.this, "新玩法开发中，敬请期待");
//                                                } else {
//                                                    finish();
//                                                }
//                                            }
//                                        }
//                                    }
//                                });
//                        break;
//
//                }
//
//            }
//        };
    }

    private void isInBlacklist(String roomID, final HotChatRoom item) {
        //判断是否在群黑名单里
        final boolean[] isBlackMember = new boolean[1];
        SCHttpUtils.get()
                .url(HttpApi.IS_BLACK_MEMBER)
                .addParams("roomId", roomID)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = SCJsonUtils.parseCode(response);
                        if (NetErrCode.COMMON_SUC_CODE.equals(code) || NetErrCode.SUC_CODE.equals(code)) {
                            String data = SCJsonUtils.parseData(response);
                            boolean isBlackMember = Boolean.valueOf(data);
                            if (!isBlackMember) {
                                Intent intent = new Intent(mContext, MessageListActivity.class);
                                intent.putExtra("roomId", item.getRoomId());
                                intent.putExtra("roomName", item.getRoomName());
                                intent.putExtra("hotChatRoom", item);
                                intent.putExtra("isHotChatRoom", true);
//                                intent.putExtra("isInCharRoom", isIn);
                                mContext.startActivity(intent);
                            } else {
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showToast(mContext, "您已被该聊天室管理员删除");
                                    }
                                });
                            }


                        }
                    }
                });
    }

    /**
     * 描述： //上传用户头像
     */
    private void pickImages() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    private void initUserInfo() {
        final Dialog dialog = DialogCreator.createLoadingDialog(FootPrintActivity.this,
                FootPrintActivity.this.getString(R.string.jmui_loading));
        String character = SCCacheUtils.getCacheCharacterInfo();
        final CharacterInfo cacheCharacter = JSONObject.parseObject(character, CharacterInfo.class);
        mMyInfo = JMessageClient.getMyInfo();
        if (mMyInfo != null) {
//            userNikeView.setText(mMyInfo.getNickname());
//            SharePreferenceManager.setRegisterUsername(mMyInfo.getNickname());
            userNikeView.setText("" + mMyInfo.getNickname());
            tvUserSign.setText(mMyInfo.getSignature());
            mMyInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                @Override
                public void gotResult(int responseCode, String responseMessage, Bitmap avatarBitmap) {
                    String s = responseMessage;
                    if (responseCode == 0) {
                        userHeadView.setImageBitmap(avatarBitmap);
                    } else {
                        userHeadView.setImageResource(R.mipmap.aurora_headicon_default);
                    }
                }
            });
        } else {
            CharacterInfo characterInfo = JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(), CharacterInfo.class);
            if (characterInfo != null && characterInfo.getCharacterId() != 0) {
                String nikeName = characterInfo.getName() != null ? characterInfo.getName() : "";
                String signature = characterInfo.getSignature() != null ? characterInfo.getSignature() : "";
                final String headImg = characterInfo.getHeadImg() != null ? characterInfo.getHeadImg() : "";
                JmAccount jmUserInfo = new JmAccount();
                if (!TextUtils.isEmpty(nikeName)) {
                    userNikeView.setText(nikeName);
                    jmUserInfo.setNickname(nikeName);//设置昵称
                    JMessageClient.updateMyInfo(UserInfo.Field.nickname, jmUserInfo, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            String s1 = s;
                        }
                    });
                }
                if (!TextUtils.isEmpty(signature)) {
                    tvUserSign.setText(signature);
                    jmUserInfo.setSignature(signature);//设置签名
                    JMessageClient.updateMyInfo(UserInfo.Field.signature, jmUserInfo, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            String s1 = s;
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(headImg)) {
                            RequestOptions options = new RequestOptions();
                            options.placeholder(R.mipmap.aurora_headicon_default);
                            Glide.with(FootPrintActivity.this).load(headImg).apply(options).into(userHeadView);
                        }
                    }
                });
            }
        }

    }

    //搜索过滤元社区
    private SearchResult filterChatRoom(String filterStr, List<HotChatRoom> hotChatRoomList) {
        SearchResult searchResult = new SearchResult();
        List<HotChatRoom> hotChatRooms = new ArrayList<>();

        if (filterStr.equals("")) {
            SearchResult result = new SearchResult();
            result.setFilterStr("");
            result.setChatRoomList(hotChatRooms);
            return result;
        }
        if (filterStr.equals("'")) {
            SearchResult result = new SearchResult();
            result.setChatRoomList(hotChatRooms);
            return result;
        }

        //所有好友名单
        for (HotChatRoom chatRoom : hotChatRoomList) {
            //如果好友名 包含 搜索内容 就把这个人的userinfo添加
            if (TextSearcher.contains(false, chatRoom.getRoomId(), filterStr) ||
                    TextSearcher.contains(false, chatRoom.getRoomName(), filterStr)) {
                hotChatRooms.add(chatRoom);
            }
        }
        searchResult.setFilterStr(filterStr);
        searchResult.setChatRoomList(hotChatRooms);

        return searchResult;
    }

    @Override
    protected void getChatRoomInfo(LatLng latLng) {
        super.getChatRoomInfo(latLng);
        localRoomID = SCCacheUtils.getCacheRoomId();

    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode,resultCode,data);
        if (fragmentList.size() >= 2) {
            Fragment mainArsFragment = fragmentList.get(1);
            mainArsFragment.onActivityResult(requestCode, resultCode, data);
        }
        switch (requestCode) {
            case PhotoUtils.INTENT_SELECT:
                if (data == null) {
                    return;
                }
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String photoPath = cursor.getString(columnIndex);  //获取照片路径
                cursor.close();
                final Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                //个人中心换头像
                userHeadView.setImageBitmap(bitmap);
                //标题栏换头像
                ImageView toolBarUserHead = (ImageView) arthurToolBar.getUserHeadImg();
                toolBarUserHead.setImageBitmap(bitmap);
                SCUploadImgHelper helper = new SCUploadImgHelper();
                helper.setUploadListener(new SCUploadImgHelper.UploadListener() {
                    @Override
                    public void onUploadSuc(List<String> urls) {
                        RoleManager.switchRoleCacheHeadImg(urls.get(0));
                        //更改头像
                        String characterInfo = SCCacheUtils.getCacheCharacterInfo();
                        CharacterInfo character = JSONObject.parseObject(characterInfo, CharacterInfo.class);
                        ModifyUserInfo modifyUserInfo = new ModifyUserInfo();
                        modifyUserInfo.setName(character.getName());
                        modifyUserInfo.setSignature(character.getSignature());
                        modifyUserInfo.setHeadImg(urls.get(0));
                        String modifyUser = JSONObject.toJSONString(modifyUserInfo);
//                        org.greenrobot.eventbus.EventBus.getDefault().postSticky(modifyUserInfo);
                        SCHttpUtils.postWithUserId()
                                .url(HttpApi.MODIFY_CHARACTER)
                                .addParams("characterId", "" + SCCacheUtils.getCacheCharacterId())
                                .addParams("dataString", modifyUser)
                                .build()
                                .execute(new SCHttpStringCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        com.shanchain.data.common.utils.LogUtils.d("修改角色信息失败");
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        String code = JSONObject.parseObject(response).getString("code");
                                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                            com.shanchain.data.common.utils.LogUtils.d("修改角色信息");
                                            String data = JSONObject.parseObject(response).getString("data");
                                            String signature = JSONObject.parseObject(data).getString("signature");
                                            String headImg = JSONObject.parseObject(data).getString("headImg");
                                            String name = JSONObject.parseObject(data).getString("name");
                                            String avatar = JSONObject.parseObject(data).getString("avatar");
                                            UserInfo jmUserInfo = JMessageClient.getMyInfo();
                                            CharacterInfo characterInfo = new CharacterInfo();
                                            characterInfo.setHeadImg(headImg);
//                                            characterInfo.setSignature(signature);
//                                            characterInfo.setName(name);
                                            String character = JSONObject.toJSONString(characterInfo);
                                            RoleManager.switchRoleCacheCharacterInfo(character);
                                            RoleManager.switchRoleCacheHeadImg(avatar);
                                        }
                                    }
                                });

                        File headFiel = null;
                        try {
                            Bitmap bitmap1 = ImageUtils.returnBitMap(urls.get(0));
                            headFiel = ImageUtils.saveUrlImgFile(bitmap, "head_img.jpg");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //调用极光更新头像
                        UserInfo userInfo = JMessageClient.getMyInfo();
                        JMessageClient.updateUserAvatar(headFiel, new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                String s1 = s;
                            }
                        });
                    }

                    @Override
                    public void error() {
                        com.shanchain.data.common.utils.LogUtils.i("oss上传失败");
                    }
                });
                List list = new ArrayList();
                list.add(photoPath);
                helper.upLoadImg(mContext, list);
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
//        finish();
    }

    @Override
    public void onRightClick(View v) {
        readyGo(HomeActivity.class);
    }

    //侧滑栏按钮实现
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_my_wallet) {
            Intent intent = new Intent(mContext, com.shanchain.shandata.rn.activity.SCWebViewActivity.class);
//            Intent intent = new Intent(mContext, X5WebViewActivity.class);
            JSONObject obj = new JSONObject();
            obj.put("url", HttpApi.SEAT_WALLET);
            obj.put("title", getResources().getString(R.string.nav_my_wallet));
            String webParams = obj.toJSONString();
            intent.putExtra("webParams", webParams);
            startActivity(intent);
        } else if (id == R.id.nav_my_coupon) {
            Intent intent = new Intent(FootPrintActivity.this, MyCouponListActivity.class);
            intent.putExtra("roomId", localRoomID);
            startActivity(intent);
        } else if (id == R.id.nav_my_task) {
            Intent intent = new Intent(FootPrintActivity.this, TaskListActivity.class);
            intent.putExtra("roomId", localRoomID);
            startActivity(intent);

        } else if (id == R.id.nav_my_message) {
            readyGo(MyMessageActivity.class);

        } else if (id == R.id.nav_my_favorited) {
            readyGo(FootPrintActivity.class);

        } else if (id == R.id.real_identity) {
//            readyGo(VerifiedActivity.class);

        } else if (id == R.id.nav_setting) {
            readyGo(SettingsActivity.class);
        }
//        else if (id == R.id.nav_logout) {
//            JMessageClient.logout();
//            readyGoThenKill(LoginActivity.class);
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void registerMessageReceiver() {
        mMyMessageReceiver = new FootPrintActivity.MyMessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMyMessageReceiver, filter);
    }

    public class MyMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent customIntent = new Intent(context, MyMessageActivity.class);
            customIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(customIntent);
        }

    }

    //监听用户登录状态
    public void onEventMainThread(LoginStateChangeEvent event) {
        LoginStateChangeEvent.Reason reason = event.getReason();//获取变更的原因
        UserInfo myInfo = event.getMyInfo();//获取当前被登出账号的信息
        switch (reason) {
            case user_password_change:
                //用户密码在服务器端被修改
                com.shanchain.data.common.utils.LogUtils.d("LoginStateChangeEvent", "用户密码在服务器端被修改");
//                ToastUtils.showToast(mContext, "您的密码已被修改");
                break;
            case user_logout:
                //用户换设备登录
                com.shanchain.data.common.utils.LogUtils.d("LoginStateChangeEvent", "账号在其他设备上登录");
                final StandardDialog standardDialog = new StandardDialog(FootPrintActivity.this);
                standardDialog.setStandardTitle("提示");
                standardDialog.setStandardMsg("账号已在其他设备上登录，请重新登录");
                standardDialog.setSureText("重新登录");
                standardDialog.setCancelText("取消");
                standardDialog.setCallback(new Callback() {//确定
                    @Override
                    public void invoke() {
                        readyGo(LoginActivity.class);
                        ActivityStackManager.getInstance().finishAllActivity();
                    }
                }, new Callback() {//取消
                    @Override
                    public void invoke() {

                    }
                });
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
//                        ToastUtils.showToast(getApplicationContext(), "账号在其他设备上登录");
                        standardDialog.show();
                    }
                });

                break;
            case user_deleted:
                //用户被删除
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventMessage eventMessage) {
        if (eventMessage.getCode() == NetErrCode.ADD_ROOM_SUCCESS) {
            if (mQuickAdapter != null) {
                pageNo = 0;
                isLoadMore = false;
                last = false;
                initData(pageNo, size, bgaRefreshLayout);
                mQuickAdapter.notifyLoadMoreToLoading();
            }
        }
    }

    //初始化上拉加载
    private void initLoadMoreListener() {
        reviewFoodPrint.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (last == true) {
                    return;

                }
                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mQuickAdapter.getItemCount()) {
                    mQuickAdapter.getFooterLayout().setVisibility(View.VISIBLE);
                    pageNo++;
                    SCHttpUtils.get()
                            .url(HttpApi.HOT_CHAT_ROOM)
                            .addParams("token", SCCacheUtils.getCacheToken() + "")
                            .addParams("version", localVersion + "")
                            .addParams("page", pageNo + "")
                            .addParams("size", size + "")
                            .build()
                            .execute(new SCHttpStringCallBack() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    closeLoadingDialog();
                                    LogUtils.d("网络异常");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    String code = com.alibaba.fastjson.JSONObject.parseObject(response).getString("code");
                                    if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                                        String data = com.alibaba.fastjson.JSONObject.parseObject(response).getString("data");
                                        String content = SCJsonUtils.parseString(data, "content");
                                        String allPage = JSONObject.parseObject(data).getString("totalPages");
                                        totalPage = Integer.valueOf(allPage);
//                                last = SCJsonUtils.parseBoolean(data, "last");
                                        String isLast = JSONObject.parseObject(data).getString("last");
                                        last = Boolean.valueOf(isLast);
                                        List<HotChatRoom> roomList = JSONArray.parseArray(content, HotChatRoom.class);
                                        mQuickAdapter.addData(roomList);
                                        mQuickAdapter.notifyDataSetChanged();
                                        mQuickAdapter.getFooterLayout().setVisibility(View.GONE);
                                        closeLoadingDialog();
//                                        mQuickAdapter.loadMoreEnd(true);
                                    }
                                }
                            });
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

    }

    /* 下拉刷新 */
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        pageNo = 0;
        initData(pageNo, size, refreshLayout);
    }


    /* 上拉加载 */
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(final BGARefreshLayout refreshLayout) {
//        LogUtils.d("刷新列表", bgaRefreshLayout.isLoadingMore() + "");
//        LogUtils.d("mQuickAdapter刷新列表", mQuickAdapter.isLoading() + "");
        return false;
    }
}
