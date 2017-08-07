package com.shanchain.shandata.mvp.view.activity.mine;

import android.view.View;

import com.sevenheaven.iosswitch.ShSwitchView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.PrefUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;

public class MessageSettingActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarMessageSetting;
    @Bind(R.id.shs_msg_setting_push)
    ShSwitchView mShsMsgSettingPush;
    @Bind(R.id.shs_msg_setting_challenge_start_notify)
    ShSwitchView mShsMsgSettingChallengeStartNotify;
    @Bind(R.id.shs_msg_setting_challenge_complement_notify)
    ShSwitchView mShsMsgSettingChallengeComplementNotify;
    @Bind(R.id.shs_msg_setting_comment_notify)
    ShSwitchView mShsMsgSettingCommentNotify;
    @Bind(R.id.shs_msg_setting_aite_notify)
    ShSwitchView mShsMsgSettingAiteNotify;
    @Bind(R.id.shs_msg_setting_story_change)
    ShSwitchView mShsMsgSettingStoryChange;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_message_setting;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initListener();
    }

    private void initListener() {


        //消息推送
        boolean mShsMsgSettingPushOn = PrefUtils.getBoolean(this, "mShsMsgSettingPushOn", true);
        mShsMsgSettingPush.setOn(mShsMsgSettingPushOn);

        //挑战开始通知
        boolean mShsMsgSettingChallengeStartNotifyOn = PrefUtils.getBoolean(this, "mShsMsgSettingChallengeStartNotifyOn", true);
        mShsMsgSettingChallengeStartNotify.setOn(mShsMsgSettingChallengeStartNotifyOn);

        //挑战完成通知
        boolean mShsMsgSettingChallengeComplementNotifyOn = PrefUtils.getBoolean(this, "mShsMsgSettingChallengeComplementNotifyOn", true);
        mShsMsgSettingChallengeComplementNotify.setOn(mShsMsgSettingChallengeComplementNotifyOn);

        //评论提醒
        boolean mShsMsgSettingCommentNotifyOn = PrefUtils.getBoolean(this, "mShsMsgSettingCommentNotifyOn", false);
        mShsMsgSettingCommentNotify.setOn(mShsMsgSettingCommentNotifyOn);
        //@通知
        boolean mShsMsgSettingAiteNotifyOn = PrefUtils.getBoolean(this, "mShsMsgSettingAiteNotifyOn", true);
        mShsMsgSettingAiteNotify.setOn(mShsMsgSettingAiteNotifyOn);

        //故事路线改变后通知
        boolean mShsMsgSettingStoryChangeOn = PrefUtils.getBoolean(this, "mShsMsgSettingStoryChangeOn", false);
        mShsMsgSettingStoryChange.setOn(mShsMsgSettingStoryChangeOn);

    }

    private void initToolBar() {
        mToolbarMessageSetting = (ArthurToolBar) findViewById(R.id.toolbar_message_setting);
        mToolbarMessageSetting.setBtnVisibility(true, false);
        mToolbarMessageSetting.setBtnEnabled(true, false);
        mToolbarMessageSetting.setOnLeftClickListener(this);
    }


    @Override
    public void onLeftClick(View v) {
        saveSettingAndFinish();
    }

    private void saveSettingAndFinish() {

        boolean mShsMsgSettingPushOn = mShsMsgSettingPush.isOn();
        boolean mShsMsgSettingChallengeStartNotifyOn = mShsMsgSettingChallengeStartNotify.isOn();
        boolean mShsMsgSettingChallengeComplementNotifyOn = mShsMsgSettingChallengeComplementNotify.isOn();
        boolean mShsMsgSettingCommentNotifyOn = mShsMsgSettingCommentNotify.isOn();
        boolean mShsMsgSettingAiteNotifyOn = mShsMsgSettingAiteNotify.isOn();
        boolean mShsMsgSettingStoryChangeOn = mShsMsgSettingStoryChange.isOn();
        LogUtils.d( mShsMsgSettingPushOn
                + ":" + mShsMsgSettingChallengeStartNotifyOn + ":" + mShsMsgSettingChallengeComplementNotifyOn
                + ":" + mShsMsgSettingCommentNotifyOn + ":" + mShsMsgSettingAiteNotifyOn
                + ":" + mShsMsgSettingStoryChangeOn);

        PrefUtils.putBoolean(this,"mShsMsgSettingPushOn",mShsMsgSettingPushOn);
        PrefUtils.putBoolean(this,"mShsMsgSettingChallengeStartNotifyOn",mShsMsgSettingChallengeStartNotifyOn);
        PrefUtils.putBoolean(this,"mShsMsgSettingChallengeComplementNotifyOn",mShsMsgSettingChallengeComplementNotifyOn);
        PrefUtils.putBoolean(this,"mShsMsgSettingCommentNotifyOn",mShsMsgSettingCommentNotifyOn);
        PrefUtils.putBoolean(this,"mShsMsgSettingAiteNotifyOn",mShsMsgSettingAiteNotifyOn);
        PrefUtils.putBoolean(this,"mShsMsgSettingStoryChangeOn",mShsMsgSettingStoryChangeOn);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveSettingAndFinish();
    }
}
