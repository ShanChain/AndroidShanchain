package com.shanchain.shandata.mvp.view.activity.mine;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class AccountAndSafeActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

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
        switch (view.getId()) {
            case R.id.ll_account_safe_modify_pwd:
                break;
            case R.id.ll_account_safe_bind_phone:
                break;
            case R.id.ll_account_safe_bind_email:
                break;
            case R.id.ll_account_safe_bind_wechat:
                break;
            case R.id.ll_account_safe_bind_qq:
                break;
            case R.id.ll_account_safe_bind_weibo:
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
