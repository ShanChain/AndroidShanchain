package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.ImageUtils;
import com.shanchain.data.common.utils.SCUploadImgHelper;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ImagePickerAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.Coordinates;
import com.shanchain.shandata.ui.model.HotChatRoom;
import com.shanchain.shandata.ui.model.JmAccount;
import com.shanchain.shandata.ui.model.ModifyUserInfo;
import com.shanchain.shandata.ui.view.activity.HomeActivity;
import com.shanchain.shandata.ui.view.activity.ModifyUserInfoActivity;
import com.shanchain.shandata.ui.view.activity.coupon.CouponListActivity;
import com.shanchain.shandata.ui.view.activity.coupon.MyCouponListActivity;
import com.shanchain.shandata.ui.view.activity.settings.SettingsActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskDetailActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskListActivity;
import com.shanchain.shandata.utils.GlideImageLoader;
import com.shanchain.shandata.utils.RequestCode;
import com.shanchain.shandata.widgets.arcMenu.ArcMenu;
import com.shanchain.shandata.widgets.photochoose.DialogCreator;
import com.shanchain.shandata.widgets.photochoose.PhotoUtils;
import com.shanchain.shandata.widgets.pickerimage.utils.Extras;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.model.DefaultUser;
import cn.jiguang.imui.model.MyMessage;
import cn.jiguang.imui.view.RoundImageView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.content.VideoContent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;

public class FootPrintActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, NavigationView.OnNavigationItemSelectedListener, ArthurToolBar.OnRightClickListener {

    private ArthurToolBar arthurToolBar;
    private LinearLayout linearFootPrint;
    private RecyclerView reviewFoodPrint;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private TextView userNikeView;
    private TextView tvUserSign;
    private ImageView ivUserModify;
    private ImageView userHeadView;
    private List<HotChatRoom> hotChatRoomList;
    private UserInfo mMyInfo;
    private Coordinates mCoordinates;
    private String roomName, mRoomID;
    private boolean isIn;
    private ArcMenu.OnMenuItemClickListener onMenuItemClickListener;

    public static final String MESSAGE_RECEIVED_ACTION = "com.shanchain.shandata.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private MyMessageReceiver mMyMessageReceiver;

    @Override
    protected int getContentViewLayoutID() {
//        return R.layout.activity_food_print;
        return R.layout.activity_foot_print_seting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();
        intent.setAction(".receiver.MyLocationReceiver");
//        sendBroadcast(intent);
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initView();
        initData();
        //注册自定义消息广播
//        registerMessageReceiver();


    }

    private void initData() {

    }

    private void initView() {
        linearFootPrint = findViewById(R.id.linear_foot_print);
        reviewFoodPrint = findViewById(R.id.review_food_print);
        reviewFoodPrint.setVisibility(View.VISIBLE);
        linearFootPrint.setVisibility(View.GONE);
        SCHttpUtils.get()
                .url(HttpApi.HOT_CHAT_ROOM)
                .addParams("token", SCCacheUtils.getCacheToken() + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = com.alibaba.fastjson.JSONObject.parseObject(response).getString("code");
                        if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                            String data = com.alibaba.fastjson.JSONObject.parseObject(response).getString("data");
//                            String roomId = com.alibaba.fastjson.JSONObject.parseObject(data).getString("roomId");
////                            String Id = com.alibaba.fastjson.JSONObject.parseObject(data).getString("id");
//                            String roomName = com.alibaba.fastjson.JSONObject.parseObject(data).getString("roomName");
//                            String sortNo = com.alibaba.fastjson.JSONObject.parseObject(data).getString("sortNo");
//                            String background = com.alibaba.fastjson.JSONObject.parseObject(data).getString("background");
//                            String thumbnails = com.alibaba.fastjson.JSONObject.parseObject(data).getString("thumbnails");
                            hotChatRoomList = JSONArray.parseArray(data, HotChatRoom.class);
                            BaseQuickAdapter adapter = new BaseQuickAdapter<HotChatRoom, BaseViewHolder>(R.layout.item_hot_chat_room, hotChatRoomList) {
                                @Override
                                protected void convert(BaseViewHolder helper, final HotChatRoom item) {
                                    RoundImageView roundImageView = helper.getView(R.id.item_round_view);
                                    RoundImageView avatar = helper.getView(R.id.iv_item_msg_home_avatar);
                                    TextView roomName = helper.getView(R.id.tv_item_room_name);
                                    TextView roomNum = helper.getView(R.id.tv_item_member_num);
                                    TextView visitTime = helper.getView(R.id.tv_item_visit_time);
                                    Button btnJoin = helper.getView(R.id.bt_item_join);
                                    RequestOptions options = new RequestOptions();
                                    options.placeholder(R.mipmap.empty_foot_print);
                                    Glide.with(FootPrintActivity.this).load(item.getBackground()).apply(options).into(roundImageView);
                                    Glide.with(FootPrintActivity.this).load(item.getThumbnails()).apply(options).into(avatar);
                                    roomName.setText(item.getRoomName());
                                    roomNum.setText(item.getUserNum());
                                    btnJoin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(FootPrintActivity.this, MessageListActivity.class);
                                            intent.putExtra("roomId", item.getRoomId());
                                            intent.putExtra("roomName", item.getRoomName());
                                            intent.putExtra("hotChatRoom", item);
                                            intent.putExtra("isHotChatRoom", true);
//                                intent.putExtra("isInCharRoom", isIn);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            };
                            LinearLayoutManager layoutManager = new LinearLayoutManager(FootPrintActivity.this, LinearLayoutManager.VERTICAL, false);
                            reviewFoodPrint.setLayoutManager(layoutManager);
                            reviewFoodPrint.setAdapter(adapter);
                            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    Intent intent = new Intent(FootPrintActivity.this, MessageListActivity.class);
                                    intent.putExtra("roomId", hotChatRoomList.get(position).getRoomId());
                                    intent.putExtra("roomName", hotChatRoomList.get(position).getRoomName());
                                    intent.putExtra("hotChatRoom", hotChatRoomList.get(position));
                                    intent.putExtra("isHotChatRoom", true);
//                                intent.putExtra("isInCharRoom", isIn);
                                    startActivity(intent);
                                }
                            });

                        }
                    }
                });
        initDrawer();
    }

    private void initToolBar() {
        arthurToolBar = findViewById(R.id.toolbar_nav);
        arthurToolBar.isShowChatRoom(false);//不在导航栏显示聊天室信息
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        arthurToolBar.getTitleView().setLayoutParams(layoutParams);
        arthurToolBar.setTitleText("热门元社区");
        arthurToolBar.setTitleTextColor(getResources().getColor(R.color.colorTextDefault));
        arthurToolBar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//        arthurToolBar.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        arthurToolBar.setRightImage(R.mipmap.home_nav_map);
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
                selectImage(FootPrintActivity.this);
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

    private void getChatRoomInfo(final String roomID, LatLng latLng) {
        SCHttpUtils.get()
                .url(HttpApi.CHAT_ROOM_INFO)
                .addParams("longitude", latLng.longitude + "")
                .addParams("latitude", latLng.latitude + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        com.shanchain.data.common.utils.LogUtils.d("####### GET_CHAT_ROOM_INFO 请求失败 #######");
                        closeLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        closeLoadingDialog();
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            com.shanchain.data.common.utils.LogUtils.d("####### " + "获取聊天室信息" + " ########");
                            String data = JSONObject.parseObject(response).getString("data");
                            mCoordinates = JSONObject.parseObject(data, Coordinates.class);
                            //房间roomId
                            mRoomID = mCoordinates.getRoomId();
                            RoleManager.switchRoleCacheRoomId(mRoomID);
                            if (roomID.equals(mCoordinates.getRoomId())) {
                                isIn = true;
                            } else {
                                roomName = mCoordinates.getRoomName();
                                isIn = false;
                            }
                            android.os.Message message = new android.os.Message();
                            message.what = 3;
                            message.obj = mCoordinates;
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
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
            JSONObject obj = new JSONObject();
            obj.put("url", HttpApi.SEAT_WALLET);
            obj.put("title", getResources().getString(R.string.nav_my_wallet));
            String webParams = obj.toJSONString();
            intent.putExtra("webParams", webParams);
            startActivity(intent);
        } else if (id == R.id.nav_my_coupon) {
            Intent intent = new Intent(FootPrintActivity.this, MyCouponListActivity.class);
            intent.putExtra("roomId", mRoomID);
            startActivity(intent);
        } else if (id == R.id.nav_my_task) {
            Intent intent = new Intent(FootPrintActivity.this, TaskListActivity.class);
            intent.putExtra("roomId", mRoomID);
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
}
