package com.shanchain.shandata.mvp.view.activity.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class ResetPasswordActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {
    ArthurToolBar mToolbarResetPassword;
    @Bind(R.id.et_reset_pwd_account)
    EditText mEtResetPwdAccount;
    @Bind(R.id.tv_reset_pwd_type)
    TextView mTvResetPwdType;
    @Bind(R.id.et_reset_pwd_new_pwd)
    EditText mEtResetPwdNewPwd;
    @Bind(R.id.et_reset_pwd_checkcode)
    EditText mEtResetPwdCheckcode;
    @Bind(R.id.btn_reset_pwd_checkcode)
    Button mBtnResetPwdCheckcode;
    @Bind(R.id.btn_reset_pwd_sure)
    Button mBtnResetPwdSure;
    @Bind(R.id.activity_reset_password)
    LinearLayout mActivityResetPassword;

    private static final int OPERATION_PHONE_BIND = 100;
    private static final int OPERATION_PHONE_RESET_PWD = 200;
    private static final int OPERATION_EMAIL_BIND = 300;
    private static final int OPERATION_EMAIL_RESET_PWD = 400;

    private int operationType;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void initViewsAndEvents() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorTheme));
        initData();
        initToolBar();
    }

    private void initData() {
        operationType = 10;
    }

    private void initToolBar() {
        mToolbarResetPassword = (ArthurToolBar) findViewById(R.id.toolbar_reset_password);

        switch (operationType) {
            case OPERATION_PHONE_BIND:
                mToolbarResetPassword.setTitleText("绑定手机");
                mEtResetPwdAccount.setHint("请输入手机号码");
                mTvResetPwdType.setText("密码");
                mEtResetPwdNewPwd.setHint("请输入密码");
                break;
            case OPERATION_PHONE_RESET_PWD:
                mToolbarResetPassword.setTitleText("重置密码");
                mEtResetPwdAccount.setHint("请输入手机号码");
                mTvResetPwdType.setText("新密码");
                mEtResetPwdNewPwd.setHint("请输入新密码");
                break;
            case OPERATION_EMAIL_BIND:
                mToolbarResetPassword.setTitleText("绑定邮箱");
                mEtResetPwdAccount.setHint("请输入邮箱号");
                mTvResetPwdType.setText("密码");
                mEtResetPwdNewPwd.setHint("请输入密码");
                break;
            case OPERATION_EMAIL_RESET_PWD:
                mToolbarResetPassword.setTitleText("重置密码");
                mEtResetPwdAccount.setHint("请输入邮箱号");
                mTvResetPwdType.setText("新密码");
                mEtResetPwdNewPwd.setHint("请输入新密码");
                break;
        }

        mToolbarResetPassword.setBtnVisibility(true,false);
        mToolbarResetPassword.setBtnEnabled(true,false);
        mToolbarResetPassword.setOnLeftClickListener(this);
    }


    @OnClick({R.id.btn_reset_pwd_checkcode, R.id.btn_reset_pwd_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset_pwd_checkcode:

                break;
            case R.id.btn_reset_pwd_sure:
                //请求修改密码接口
                resetPassword();
                break;
        }
    }

    private void resetPassword() {
        String account = mEtResetPwdAccount.getText().toString().trim();
        String pwd = mEtResetPwdNewPwd.getText().toString().trim();
        String checkCode = mEtResetPwdCheckcode.getText().toString().trim();

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
