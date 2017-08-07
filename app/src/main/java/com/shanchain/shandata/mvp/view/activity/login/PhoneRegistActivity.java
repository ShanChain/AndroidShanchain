package com.shanchain.shandata.mvp.view.activity.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class PhoneRegistActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, View.OnFocusChangeListener {


    /**
     * 描述：顶部工具栏
     */
    @Bind(R.id.toolbar_login)
    ArthurToolBar mToolbarLogin;

    /**
     * 描述：账号输入框
     */
    @Bind(R.id.et_phone_regist_number)
    EditText mEtPhoneRegistNumber;
    /**
     * 描述：验证码输入框
     */
    @Bind(R.id.et_phone_regist_checkcode)
    EditText mEtPhoneRegistCheckcode;
    /**
     * 描述：获取验证码按钮
     */
    @Bind(R.id.btn_phone_regist_checkcode)
    Button mBtnPhoneRegistCheckcode;
    /**
     * 描述：昵称输入框
     */
    @Bind(R.id.et_phone_regist_nickname)
    EditText mEtPhoneRegistNickname;
    /**
     * 描述：密码输入框
     */
    @Bind(R.id.et_phone_regist_pwd)
    EditText mEtPhoneRegistPwd;
    /**
     * 描述：密码确认输入框
     */
    @Bind(R.id.et_phone_regist_pwdconfirm)
    EditText mEtPhoneRegistPwdconfirm;
    /**
     * 描述：同意注册按钮
     */
    @Bind(R.id.btn_phone_regist_agree)
    Button mBtnPhoneRegistAgree;
    /**
     * 描述：条款详情文本
     */
    @Bind(R.id.tv_phone_regist_terms)
    TextView mTvPhoneRegistTerms;
    /**
     * 描述：整体布局
     */
    @Bind(R.id.activity_phone_regist)
    LinearLayout mActivityPhoneRegist;
    @Bind(R.id.et_regist_invite_code)
    EditText mEtRegistInviteCode;
    /**
     * 描述：账号输入框提示文字
     */
    private CharSequence mNumHint;
    /**
     * 描述：验证码输入框提示文字
     */
    private CharSequence mCheckcodeHint;
    /**
     * 描述：昵称输入框提示文字
     */
    private CharSequence mNicknameHint;
    /**
     * 描述：密码输入框提示文字
     */
    private CharSequence mPwdHint;
    /**
     * 描述：确认密码输入框提示文字
     */
    private CharSequence mPwdConfirmHint;

    private String verifyCode;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_phone_regist;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initEditText();
    }

    /**
     * 2017/5/18
     * 描述：初始化输入框,给输入框添加焦点监听事件
     */
    private void initEditText() {
        mNumHint = mEtPhoneRegistNumber.getHint();
        mCheckcodeHint = mEtPhoneRegistCheckcode.getHint();
        mNicknameHint = mEtPhoneRegistNickname.getHint();
        mPwdHint = mEtPhoneRegistPwd.getHint();
        mPwdConfirmHint = mEtPhoneRegistPwdconfirm.getHint();
        mEtPhoneRegistNumber.setOnFocusChangeListener(this);
        mEtPhoneRegistCheckcode.setOnFocusChangeListener(this);
        mEtPhoneRegistNickname.setOnFocusChangeListener(this);
        mEtPhoneRegistPwd.setOnFocusChangeListener(this);
        mEtPhoneRegistPwdconfirm.setOnFocusChangeListener(this);
    }

    private void initToolBar() {
        mToolbarLogin.setBtnEnabled(true, false);
        mToolbarLogin.setOnLeftClickListener(this);
    }

    @OnClick({R.id.btn_phone_regist_checkcode, R.id.btn_phone_regist_agree, R.id.tv_phone_regist_terms})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_phone_regist_checkcode:
                //获取验证码
                obtainCheckCode();
                break;
            case R.id.btn_phone_regist_agree:
                //同意注册
                agreeRegist();
                break;
            case R.id.tv_phone_regist_terms:
                //查看条款详情
                lookTerms();
                break;
        }
    }

    /**
     * 描述：查看条款
     */
    private void lookTerms() {

    }

    /**
     * 描述：同意注册
     */
    private void agreeRegist() {
        //用户类型
        String userType;

        String account = mEtPhoneRegistNumber.getText().toString().trim();

        String pwd = mEtPhoneRegistPwd.getText().toString().trim();
        String pwdConfirm = mEtPhoneRegistPwdconfirm.getText().toString().trim();

        String nickName = mEtPhoneRegistNickname.getText().toString().toString();

        String checkCode = mEtPhoneRegistCheckcode.getText().toString().trim();
        //校验验证码
        /*if (!checkCode.equalsIgnoreCase(verifyCode)) {
            ToastUtils.showToast(this,"验证码错误！");
            return;
        }*/

        if (TextUtils.isEmpty(nickName)) {
            ToastUtils.showToast(this, "昵称不能为空！");
            return;
        }

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

        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdConfirm) || !TextUtils.equals(pwd, pwdConfirm)) {
            ToastUtils.showToast(this, "输入的密码不正确");
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
                .addParams("nickName", nickName)
                .addParams("Timestamp", time)
                .addParams("encryptAccount", encryptAccount)
                .addParams("encryptPassword", passwordAccount)
                .addParams("userType", userType)
                .url(HttpApi.USER_REGISTER)
                .build()
                .execute(new MyHttpCallBack<ResponseRegisteUserBean>(ResponseRegisteUserBean.class) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showToast(PhoneRegistActivity.this, "注册失败！");
                        LogUtils.d("注册失败 ; 错误码：" + id);
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(ResponseRegisteUserBean response, int id) {
                        if (response != null) {
                            LogUtils.d("userID = " + response.getUserInfo().getUserId());
                            finish();
                        }
                    }
                });
    }

    /**
     * 描述：获取验证码
     */
    private void obtainCheckCode() {
        String userType;
        String account = mEtPhoneRegistNumber.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showToast(this, "注册账号不能为空");
            return;
        } else {
            if (AccountUtils.isPhone(account)) {
                //是手机号码
                userType = UserType.USER_TYPE_MOBILE;
                getSMSCheck(account);
            } else if (AccountUtils.isEmail(account)) {
                //是邮箱账号
                userType = UserType.USER_TYPE_EMAIL;
                getEmailCheck(account);
                ToastUtils.showToast(this, "请注意查收辣鸡邮件！");
            } else {
                //既不是手机也不是邮箱
                ToastUtils.showToast(this, "请输入正确格式的账号！");
                return;
            }

        }

        CountDownTimeUtils countDownTimeUtils = new CountDownTimeUtils(mBtnPhoneRegistCheckcode, 1000 * 60, 1000);
        countDownTimeUtils.start();

    }

    /**
     * 描述：通过邮箱获取验证码
     */
    private void getEmailCheck(String account) {
        OkHttpUtils.post()
                .addParams("mail", account)
                .url(HttpApi.USER_CHECK_EMAIL)
                .build()
                .execute(new MyHttpCallBack<ResponseSmsBean>(ResponseSmsBean.class) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("获取验证码失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(ResponseSmsBean response, int id) {
                        if (response != null) {
                            verifyCode = response.getMailVerifyCode();
                            LogUtils.d("从后台获取的验证码:" + verifyCode);
                        } else {
                            LogUtils.d("请求的数据为空");
                        }
                    }
                });
    }

    /**
     * 描述：通过短信获取验证码
     */
    private void getSMSCheck(String account) {
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
                        if (response != null) {
                            verifyCode = response.getSmsVerifyCode();
                            LogUtils.d("从后台获取的验证码:" + verifyCode);
                        } else {
                            LogUtils.d("请求的数据为空");
                        }
                    }
                });
    }

    /**
     * 描述：顶部工具栏左侧按钮点击事件
     */
    @Override
    public void onLeftClick(View v) {
        finish();
    }

    /**
     * 2017/5/18
     * 描述：输入框焦点变化监听
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_phone_regist_number:
                changeHint(mEtPhoneRegistNumber, hasFocus, mNumHint);
                break;
            case R.id.et_phone_regist_checkcode:
                changeHint(mEtPhoneRegistCheckcode, hasFocus, mCheckcodeHint);
                break;
            case R.id.et_phone_regist_nickname:
                changeHint(mEtPhoneRegistNickname, hasFocus, mNicknameHint);
                break;
            case R.id.et_phone_regist_pwd:
                changeHint(mEtPhoneRegistPwd, hasFocus, mPwdHint);
                break;
            case R.id.et_phone_regist_pwdconfirm:
                changeHint(mEtPhoneRegistPwdconfirm, hasFocus, mPwdConfirmHint);
                break;
        }
    }

    /**
     * 描述：控制hint的显示与隐藏
     */
    private void changeHint(EditText et, boolean hasFocus, CharSequence hint) {
        if (hasFocus) {
            et.setHint("");
        } else {
            et.setHint(hint);
        }
    }

}
