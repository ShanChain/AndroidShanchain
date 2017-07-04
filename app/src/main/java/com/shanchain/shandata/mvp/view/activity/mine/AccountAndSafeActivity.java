package com.shanchain.shandata.mvp.view.activity.mine;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.view.activity.login.ResetPasswordActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class AccountAndSafeActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {
    private static final int BIND_TYPE_WEIBO = 10;
    private static final int BIND_TYPE_WECHAT = 20;
    private static final int BIND_TYPE_QQ = 30;
    private static final int BIND_TYPE_PHONE=40;
    private static final int BIND_TYPE_EMAIL=50;

    private static final int OPERATION_PHONE_BIND = 100;
    private static final int OPERATION_PHONE_RESET_PWD = 200;
    private static final int OPERATION_EMAIL_BIND = 300;
    private static final int OPERATION_EMAIL_RESET_PWD = 400;
    ArthurToolBar mToolbarAccountSafe;
    @Bind(R.id.ll_account_safe_modify_pwd)
    RelativeLayout mLlAccountSafeModifyPwd;
    @Bind(R.id.iv_account_safe_phone)
    ImageView mIvAccountSafePhone;
    @Bind(R.id.tv_account_safe_phone)
    TextView mTvAccountSafePhone;
    @Bind(R.id.ll_account_safe_bind_phone)
    RelativeLayout mLlAccountSafeBindPhone;
    @Bind(R.id.iv_account_safe_email)
    ImageView mIvAccountSafeEmail;
    @Bind(R.id.tv_account_safe_email)
    TextView mTvAccountSafeEmail;
    @Bind(R.id.ll_account_safe_bind_email)
    RelativeLayout mLlAccountSafeBindEmail;
    @Bind(R.id.iv_account_safe_wechat)
    ImageView mIvAccountSafeWechat;
    @Bind(R.id.tv_account_safe_wechat)
    TextView mTvAccountSafeWechat;
    @Bind(R.id.ll_account_safe_bind_wechat)
    RelativeLayout mLlAccountSafeBindWechat;
    @Bind(R.id.iv_account_safe_qq)
    ImageView mIvAccountSafeQq;
    @Bind(R.id.tv_account_safe_qq)
    TextView mTvAccountSafeQq;
    @Bind(R.id.ll_account_safe_bind_qq)
    RelativeLayout mLlAccountSafeBindQq;
    @Bind(R.id.iv_account_safe_weibo)
    ImageView mIvAccountSafeWeibo;
    @Bind(R.id.tv_account_safe_weibo)
    TextView mTvAccountSafeWeibo;
    @Bind(R.id.ll_account_safe_bind_weibo)
    RelativeLayout mLlAccountSafeBindWeibo;
    @Bind(R.id.activity_account_and_safe)
    LinearLayout mActivityAccountAndSafe;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_account_and_safe;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mToolbarAccountSafe = (ArthurToolBar) findViewById(R.id.toolbar_account_safe);
        mToolbarAccountSafe.setBtnVisibility(true,false);
        mToolbarAccountSafe.setBtnEnabled(true,false);
        mToolbarAccountSafe.setOnLeftClickListener(this);
    }

    @OnClick({R.id.ll_account_safe_modify_pwd, R.id.ll_account_safe_bind_phone, R.id.ll_account_safe_bind_email, R.id.ll_account_safe_bind_wechat, R.id.ll_account_safe_bind_qq, R.id.ll_account_safe_bind_weibo})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.ll_account_safe_modify_pwd:
                intent = new Intent(this, ResetPasswordActivity.class);
                intent.putExtra("operationType",OPERATION_PHONE_RESET_PWD);
                startActivity(intent);
                break;
            case R.id.ll_account_safe_bind_phone:
                intent = new Intent(this,BindResultActivity.class);
                intent.putExtra("bindType",BIND_TYPE_PHONE);
                startActivity(intent);
                break;
            case R.id.ll_account_safe_bind_email:
                intent = new Intent(this,BindResultActivity.class);
                intent.putExtra("bindType",BIND_TYPE_EMAIL);
                startActivity(intent);
                break;
            case R.id.ll_account_safe_bind_wechat:
                intent = new Intent(this,BindResultActivity.class);
                intent.putExtra("bindType",BIND_TYPE_WECHAT);
                startActivity(intent);
                break;
            case R.id.ll_account_safe_bind_qq:
                intent = new Intent(this,BindResultActivity.class);
                intent.putExtra("bindType",BIND_TYPE_QQ);
                startActivity(intent);
                break;
            case R.id.ll_account_safe_bind_weibo:
                intent = new Intent(this,BindResultActivity.class);
                intent.putExtra("bindType",BIND_TYPE_WEIBO);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
