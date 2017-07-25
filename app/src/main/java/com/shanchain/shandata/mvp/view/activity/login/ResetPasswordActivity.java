package com.shanchain.shandata.mvp.view.activity.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.global.UserType;
import com.shanchain.shandata.http.HttpApi;
import com.shanchain.shandata.http.HttpUtils;
import com.shanchain.shandata.http.MyHttpCallBack;
import com.shanchain.shandata.mvp.Bean.ResponseRegisteUserBean;
import com.shanchain.shandata.mvp.model.ResponseSmsBean;
import com.shanchain.shandata.utils.AccountUtils;
import com.shanchain.shandata.utils.CountDownTimeUtils;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.utils.encryption.AESUtils;
import com.shanchain.shandata.utils.encryption.Base64;
import com.shanchain.shandata.utils.encryption.MD5Utils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

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
    private static final int OPERATION_LOGIN_RESET_PWD = 500;
    private String mSmsVerifyCode;
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
        operationType = getIntent().getIntExtra("operationType", OPERATION_LOGIN_RESET_PWD);
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
            case OPERATION_LOGIN_RESET_PWD:
                mToolbarResetPassword.setTitleText("重置密码");
                mEtResetPwdAccount.setHint("请输入账号");
                mTvResetPwdType.setText("新密码");
                mEtResetPwdNewPwd.setHint("请输入新密码");
                break;
        }

        mToolbarResetPassword.setBtnVisibility(true, false);
        mToolbarResetPassword.setBtnEnabled(true, false);
        mToolbarResetPassword.setOnLeftClickListener(this);
    }


    @OnClick({R.id.btn_reset_pwd_checkcode, R.id.btn_reset_pwd_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset_pwd_checkcode:
                getCheckCode();
                break;
            case R.id.btn_reset_pwd_sure:
                //请求修改密码接口
                resetPassword();
                break;
        }
    }

    private void getCheckCode() {
        String userType;
        String account = mEtResetPwdAccount.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showToast(this, "注册账号不能为空");
            return;
        } else {
            if (AccountUtils.isPhone(account)) {
                //是手机号码
                userType = UserType.USER_TYPE_MOBILE;

            } else if (AccountUtils.isEmail(account)) {
                //是邮箱账号
                userType = UserType.USER_TYPE_EMAIL;

            } else {
                //既不是手机也不是邮箱
                ToastUtils.showToast(this, "输入的账号格式不正确！");
                return;
            }

        }
        OkHttpUtils.post()
                .addParams("mobile", account)
                .url(HttpApi.USER_CHECK_SMS)
                .build()
                .execute(new MyHttpCallBack<ResponseSmsBean>(ResponseSmsBean.class) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("获取验证码失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(ResponseSmsBean response, int id) {
                        mSmsVerifyCode = response.getSmsVerifyCode();
                        LogUtils.d("从后台获取的验证码:" + mSmsVerifyCode);
                    }
                });

        CountDownTimeUtils countDownTimeUtils = new CountDownTimeUtils(mBtnResetPwdCheckcode, 1000 * 60, 1000);
        countDownTimeUtils.start();


    }

    private void resetPassword() {
        String userType;
        String account = mEtResetPwdAccount.getText().toString().trim();
        String pwd = mEtResetPwdNewPwd.getText().toString().trim();
        String checkCode = mEtResetPwdCheckcode.getText().toString().trim();

        //校验验证码
       /* if (!TextUtils.equals(checkCode, mSmsVerifyCode)) {
            ToastUtils.showToast(this, "验证码错误");
            return;
        }*/

        if (TextUtils.isEmpty(account)) {
            ToastUtils.showToast(this, "账号不能为空");
            return;
        } else {
            if (AccountUtils.isPhone(account)) {
                //是手机号码
                userType = UserType.USER_TYPE_MOBILE;

            } else if (AccountUtils.isEmail(account)) {
                //是邮箱账号
                userType = UserType.USER_TYPE_EMAIL;
            } else {
                //既不是手机也不是邮箱
                ToastUtils.showToast(this, "输入的账号格式不正确！");
                return;
            }
        }

        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showToast(this, "输入的密码不能为空！");
            return;
        }
        String time = String.valueOf(System.currentTimeMillis());
        //加密后的账号
        String encryptAccount = Base64.encode(AESUtils.encrypt(account, Base64.encode(userType + time)));
        //加密后的密码
        String passwordAccount = Base64.encode(AESUtils.encrypt(MD5Utils.md5(pwd), Base64.encode(userType + time + account)));

        LogUtils.d("加密后账号：" + encryptAccount);
        LogUtils.d("加密后密码：" + passwordAccount);
        LogUtils.d("用户类型：" + userType);

        HttpUtils.postWithParamsForLogin()
                .addParams("Timestamp", time)
                .addParams("encryptAccount", encryptAccount)
                .addParams("encryptPassword", passwordAccount)
                .addParams("userType", userType)
                .url(HttpApi.USER_RESET_PWD)
                .build()
                .execute(new MyHttpCallBack<ResponseRegisteUserBean>(ResponseRegisteUserBean.class) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showToast(ResetPasswordActivity.this, "修改密码失败！");
                        LogUtils.d("注册失败 ; 错误码：" + id);
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(ResponseRegisteUserBean response, int id) {
                        ToastUtils.showToast(ResetPasswordActivity.this,"修改成功");
                        finish();
                    }
                });

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
