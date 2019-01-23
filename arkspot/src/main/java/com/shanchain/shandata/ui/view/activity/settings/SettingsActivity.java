package com.shanchain.shandata.ui.view.activity.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.FeedbackActivity;
import com.shanchain.shandata.ui.view.activity.login.LoginActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener{

    @Bind(R.id.tv_account)
    TextView tvAccount;
    @Bind(R.id.tv_account_security)
    TextView tvAccountSecurity;
    @Bind(R.id.iv_arrow)
    ImageView ivArrow;
    @Bind(R.id.relative_account)
    RelativeLayout relativeAccount;
    @Bind(R.id.tv_common)
    TextView tvCommon;
    @Bind(R.id.tv_message_push)
    TextView tvMessagePush;
    @Bind(R.id.switch_message_push)
    Switch switchMessagePush;
    @Bind(R.id.relative_message_push)
    RelativeLayout relativeMessagePush;
    @Bind(R.id.tv_feedback)
    TextView tvFeedback;
    @Bind(R.id.iv_feedback_arrow)
    ImageView ivFeedbackArrow;
    @Bind(R.id.relative_feedback)
    RelativeLayout relativeFeedback;
    @Bind(R.id.tv_about)
    TextView tvAbout;
    @Bind(R.id.iv_about_arrow)
    ImageView ivAboutArrow;
    @Bind(R.id.relative_app_about)
    RelativeLayout relativeAppAbout;
    @Bind(R.id.tv_update)
    TextView tvUpdate;
    @Bind(R.id.tv_app_version_code)
    TextView tvAppVersionCode;
    @Bind(R.id.iv_update_arrow)
    ImageView ivUpdateArrow;
    @Bind(R.id.relative_app_update)
    RelativeLayout relativeAppUpdate;
    @Bind(R.id.relative_others)
    RelativeLayout relativeOthers;
    @Bind(R.id.tv_logout)
    TextView tvLogout;
    @Bind(R.id.relative_logout)
    RelativeLayout relativeLogout;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolbar();

    }

    private void initToolbar() {
       ArthurToolBar arthurToolBar = findViewById(R.id.tb_setting);
       arthurToolBar.setTitleText("设置");
       arthurToolBar.setOnLeftClickListener(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.relative_account, R.id.relative_message_push, R.id.relative_feedback, R.id.relative_app_about, R.id.relative_app_update, R.id.relative_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.relative_account:
                readyGo(AccountSecurityActivity.class);
                break;
            case R.id.relative_message_push:

                break;
            case R.id.relative_feedback:
                readyGo(FeedbackActivity.class);
                break;
            case R.id.relative_app_about:
                break;
            case R.id.relative_app_update:
                break;
            case R.id.relative_logout:
                readyGo(LoginActivity.class);
                ActivityStackManager.getInstance().finishAllActivity();
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
