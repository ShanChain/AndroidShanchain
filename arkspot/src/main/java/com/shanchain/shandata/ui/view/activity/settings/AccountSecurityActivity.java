package com.shanchain.shandata.ui.view.activity.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.view.activity.login.BindPhoneActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountSecurityActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    @Bind(R.id.tv_user_id)
    TextView tvMajaId;
    @Bind(R.id.iv_feedback_arrow)
    TextView ivFeedbackArrow;
    @Bind(R.id.relative_user_id)
    RelativeLayout relativeMajaId;
    @Bind(R.id.tv_login_password)
    TextView tvLoginPassword;
    @Bind(R.id.tv_set_password)
    TextView tvSetPassword;
    @Bind(R.id.iv_login_password_arrow)
    ImageView ivLoginPasswordArrow;
    @Bind(R.id.relative_login_password)
    RelativeLayout relativeLoginPassword;
    @Bind(R.id.tv_bind_phone)
    TextView tvBindPhone;
    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @Bind(R.id.iv_bind_phone_arrow)
    ImageView ivBindPhoneArrow;
    @Bind(R.id.relative_bind_phone)
    RelativeLayout relativeBindPhone;
    @Bind(R.id.relative_top)
    RelativeLayout relativeTop;
    @Bind(R.id.tv_account)
    TextView tvAccount;
    @Bind(R.id.tv_account_wechat)
    TextView tvAccountWechat;
    @Bind(R.id.tv_wechat_info)
    TextView tvWechatInfo;
    @Bind(R.id.iv_wechat_arrow)
    ImageView ivWechatArrow;
    @Bind(R.id.relative_account)
    RelativeLayout relativeAccount;
    @Bind(R.id.tv_qq)
    TextView tvQq;
    @Bind(R.id.tv_qq_info)
    TextView tvQqInfo;
    @Bind(R.id.iv_qq_arrow)
    ImageView ivQqArrow;
    @Bind(R.id.relative_qq)
    RelativeLayout relativeQq;
    @Bind(R.id.tv_facebook)
    TextView tvFacebook;
    @Bind(R.id.tv_facebook_info)
    TextView tvFacebookInfo;
    @Bind(R.id.iv_facebook_arrow)
    ImageView ivFacebookArrow;
    @Bind(R.id.relative_facebook)
    RelativeLayout relativeFacebook;
    @Bind(R.id.tv_identity)
    TextView tvIdentity;
    @Bind(R.id.tv_identity_info)
    TextView tvIdentityInfo;
    @Bind(R.id.iv_update_arrow)
    ImageView ivUpdateArrow;
    @Bind(R.id.relative_identity)
    RelativeLayout relativeIdentity;
    @Bind(R.id.relative_bottom)
    RelativeLayout relativeBottom;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_account_security;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolbar();
        tvMajaId.setText(SCCacheUtils.getCacheUserId()+"");
        tvSetPassword.setVisibility(View.GONE);

    }

    private void initToolbar() {
        ArthurToolBar arthurToolBar = findViewById(R.id.tb_setting);
        arthurToolBar.setTitleText("");
        arthurToolBar.setOnLeftClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.relative_login_password, R.id.relative_bind_phone, R.id.relative_account, R.id.relative_qq, R.id.relative_facebook, R.id.relative_identity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.relative_login_password:
                readyGo(SetPasswordActivity.class);
                break;
            case R.id.relative_bind_phone:
                readyGo(ChangePhoneNumActivity.class);
                break;
            case R.id.relative_account:

                break;
            case R.id.relative_qq:

                break;
            case R.id.relative_facebook:

                break;
            case R.id.relative_identity:

                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
