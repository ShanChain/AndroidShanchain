package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.activeandroid.query.Update;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.NativeImageLoader;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.ui.model.FriendEntry;
import com.shanchain.shandata.ui.view.activity.jmessageui.view.ImgBrowserViewPager;
import com.shanchain.shandata.utils.entity.Event;
import com.shanchain.shandata.utils.entity.EventType;
import com.shanchain.shandata.widgets.other.FriendInfoController;
import com.shanchain.shandata.widgets.other.FriendInfoView;
import com.shanchain.shandata.widgets.photochoose.DialogCreator;
import com.shanchain.shandata.widgets.photochoose.HandleResponseCode;

import cn.jiguang.imui.model.DefaultUser;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.eventbus.EventBus;

/**
 * Created by ${chenyn} on 2017/3/22.
 */

public class FriendInfoActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    private String mTargetId;
    private String mTargetAppKey;
    private boolean mIsFromContact;

    private FriendInfoView mFriendInfoView;
    private UserInfo mUserInfo;
    private FriendInfoController mFriendInfoController;
    private long mGroupId;
    private String mTitle;
    private boolean mIsGetAvatar = false;
    private boolean mIsAddFriend = false;
    private boolean mIsFromSearch = false;
    private boolean mFromGroup = false;
    private String mUserID;
    private UserInfo jmUserInfo;
    private ArthurToolBar tbMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        mFriendInfoView = (FriendInfoView) findViewById(R.id.friend_info_view);
//        mTargetId = getIntent().getStringExtra(MyApplication.TARGET_ID);
//        mTargetAppKey = getIntent().getStringExtra(MyApplication.TARGET_APP_KEY);
//        mUserID = getIntent().getStringExtra("targetId");
//        if (mTargetAppKey == null) {
//            mTargetAppKey = JMessageClient.getMyInfo().getAppKey();
//        }
        final Bundle bundle = getIntent().getExtras();
        mUserID = bundle.getString("targetId", "11111");
        if (bundle.getParcelable("userInfo") != null) {
            DefaultUser defaultUser = bundle.getParcelable("userInfo");
            mTargetId = defaultUser.getHxUserId();
            JMessageClient.getUserInfo(defaultUser.getHxUserId() + "", new GetUserInfoCallback() {
                @Override
                public void gotResult(int i, String s, final UserInfo userInfo) {
                    jmUserInfo = userInfo;
                }
            });
        }
        initToolBar();
        mFriendInfoView.initModel(this);
        mFriendInfoController = new FriendInfoController(mFriendInfoView, this);
        mFriendInfoView.setListeners(mFriendInfoController);
        mFriendInfoView.setOnChangeListener(mFriendInfoController);
        mIsFromContact = getIntent().getBooleanExtra("fromContact", false);
        mIsFromSearch = getIntent().getBooleanExtra("fromSearch", false);
        mIsAddFriend = getIntent().getBooleanExtra("addFriend", false);
        mFromGroup = getIntent().getBooleanExtra("group_grid", false);
        updateUserInfo();
        //从通讯录中点击过来
//        if (mIsFromContact || mIsFromSearch || mFromGroup || mIsAddFriend) {
//            updateUserInfo();
//        } else {
//            mGroupId = getIntent().getLongExtra(MyApplication.GROUP_ID, 0);
//            Conversation conv;
//            if (mGroupId == 0) {
//                conv = JMessageClient.getSingleConversation(mTargetId, mTargetAppKey);
//                mUserInfo = (UserInfo) conv.getTargetInfo();
//            } else {
//                conv = JMessageClient.getGroupConversation(mGroupId);
//                GroupInfo groupInfo = (GroupInfo) conv.getTargetInfo();
//                mUserInfo = groupInfo.getGroupMemberInfo(mTargetId, mTargetAppKey);
//            }
//
//            //先从Conversation里获得UserInfo展示出来
//            mFriendInfoView.initInfo(mUserInfo);
//            updateUserInfo();
//        }

    }

    private void initToolBar() {
        tbMain = findViewById(R.id.tb_main);
        tbMain.setTitleTextColor(getResources().getColor(R.color.colorTextDefault));
        tbMain.isShowChatRoom(false);//不在导航栏显示聊天室信息
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tbMain.getTitleView().setLayoutParams(layoutParams);
        tbMain.setTitleText("详细资料");
        tbMain.setBackgroundColor(getResources().getColor(R.color.white));
        tbMain.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        tbMain.setOnLeftClickListener(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_friend_info;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    private void updateUserInfo() {
        final Dialog dialog = DialogCreator.createLoadingDialog(FriendInfoActivity.this,
                FriendInfoActivity.this.getString(R.string.jmui_loading));
        dialog.show();
        if (TextUtils.isEmpty(mTargetId) && !TextUtils.isEmpty(mUserID)) {
            mTargetId = mUserID;
        }
        JMessageClient.getUserInfo(mTargetId, new GetUserInfoCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, UserInfo info) {
                dialog.dismiss();
                if (responseCode == 0) {
                    //拉取好友信息时候要更新数据库中的nickName.因为如果对方修改了nickName我们是无法感知的.如果不在拉取信息
                    //时候更新数据库的话会影响到搜索好友的nickName, 注意要在没有备注名并且有昵称时候去更新.因为备注名优先级更高
                    new Update(FriendEntry.class).set("DisplayName=?", info.getDisplayName()).where("Username=?", mTargetId).execute();
                    new Update(FriendEntry.class).set("NickName=?", info.getNickname()).where("Username=?", mTargetId).execute();
                    new Update(FriendEntry.class).set("NoteName=?", info.getNotename()).where("Username=?", mTargetId).execute();

                    if (info.getAvatarFile() != null) {
                        new Update(FriendEntry.class).set("Avatar=?", info.getAvatarFile().getAbsolutePath()).where("Username=?", mTargetId).execute();
                    }
                    mUserInfo = info;
                    mFriendInfoController.setFriendInfo(info);
                    mTitle = info.getNotename();
                    if (TextUtils.isEmpty(mTitle)) {
                        mTitle = info.getNickname();
                    }
                    mFriendInfoView.initInfo(info);
                } else {
                    HandleResponseCode.onHandle(FriendInfoActivity.this, responseCode, false);
                }
            }
        });
    }


    public void startChatActivity() {
        if (mIsFromContact || mIsAddFriend || mIsFromSearch || mFromGroup) {
            Intent intent = new Intent(this, SingleChatActivity.class);
            String title = mUserInfo.getNotename();
            if (TextUtils.isEmpty(title)) {
                title = mUserInfo.getNickname();
                if (TextUtils.isEmpty(title)) {
                    title = mUserInfo.getUserName();
                }
            }
            intent.putExtra(MyApplication.CONV_TITLE, title);
            intent.putExtra(MyApplication.TARGET_ID, mUserInfo.getUserName());
            intent.putExtra(MyApplication.TARGET_APP_KEY, mUserInfo.getAppKey());
            startActivity(intent);
        } else {
            if (mGroupId != 0) {
//                Intent intent = new Intent();
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra(MyApplication.TARGET_ID, mTargetId);
//                intent.putExtra(MyApplication.TARGET_APP_KEY, mTargetAppKey);
//                intent.setClass(this, ChatActivity.class);
//                startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.putExtra("returnChatActivity", true);
                intent.putExtra(MyApplication.CONV_TITLE, mTitle);
                setResult(MyApplication.RESULT_CODE_FRIEND_INFO, intent);
            }
        }
//        Conversation conv = JMessageClient.getSingleConversation(mTargetId, mTargetAppKey);
        Conversation conv = JMessageClient.getSingleConversation(mTargetId);
        //如果会话为空，使用EventBus通知会话列表添加新会话
        if (conv == null) {
//            conv = Conversation.createSingleConversation(mTargetId, mTargetAppKey);
            conv = Conversation.createSingleConversation(mTargetId);
            EventBus.getDefault().post(new Event.Builder()
                    .setType(EventType.createConversation)
                    .setConversation(conv)
                    .build());
        }
        finish();

    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public String getUserName() {
        return mUserInfo.getUserName();
    }

    public String getTargetAppKey() {
        return mTargetAppKey;
    }

    //点击头像预览大图
    public void startBrowserAvatar() {
        if (mUserInfo != null && !TextUtils.isEmpty(mUserInfo.getAvatar())) {
            if (mIsGetAvatar) {
                //如果缓存了图片，直接加载
                Bitmap bitmap = NativeImageLoader.getInstance().getBitmapFromMemCache(mUserInfo.getUserName());
                if (bitmap != null) {
                    Intent intent = new Intent();
                    intent.putExtra("browserAvatar", true);
                    intent.putExtra("avatarPath", mUserInfo.getAvatarFile().getAbsolutePath());
//                    intent.setClass(this, BrowserViewPagerActivity.class);
                    intent.setClass(this, ImgBrowserViewPager.class);
                    startActivity(intent);
                }
            } else {
                final Dialog dialog = DialogCreator.createLoadingDialog(this, this.getString(R.string.jmui_loading));
                dialog.show();
                mUserInfo.getBigAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int status, String desc, Bitmap bitmap) {
                        if (status == 0) {
                            mIsGetAvatar = true;
                            //缓存头像
                            NativeImageLoader.getInstance().updateBitmapFromCache(mUserInfo.getUserName(), bitmap);
                            Intent intent = new Intent();
                            intent.putExtra("browserAvatar", true);
                            intent.putExtra("avatarPath", mUserInfo.getUserName());
                            intent.setClass(FriendInfoActivity.this, BrowserImageActivity.class);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (resultCode == MyApplication.RESULT_CODE_EDIT_NOTENAME) {
            mTitle = data.getStringExtra(MyApplication.NOTENAME);
            FriendEntry friend = FriendEntry.getFriend(MyApplication.getUserEntry(), mTargetId, mTargetAppKey);
            if (null != friend) {
                friend.displayName = mTitle;
                friend.save();
            }
        }
    }

    //将获得的最新的昵称返回到聊天界面
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(MyApplication.CONV_TITLE, mTitle);
        setResult(MyApplication.RESULT_CODE_FRIEND_INFO, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

//    public int getWidth() {
//        return mWidth;
//    }

}
