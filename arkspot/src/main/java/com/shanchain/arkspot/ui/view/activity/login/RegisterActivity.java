package com.shanchain.arkspot.ui.view.activity.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.global.UserType;
import com.shanchain.arkspot.ui.model.ResponseRegisteUserBean;
import com.shanchain.arkspot.ui.model.ResponseSmsBean;
import com.shanchain.arkspot.utils.CountDownTimeUtils;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.AccountUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.encryption.AESUtils;
import com.shanchain.data.common.utils.encryption.Base64;
import com.shanchain.data.common.utils.encryption.MD5Utils;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;


public class RegisterActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_register)
    ArthurToolBar mTbRegister;
    @Bind(R.id.et_register_phone)
    EditText mEtRegisterPhone;
    @Bind(R.id.tv_register_code)
    TextView mTvRegisterCode;
    @Bind(R.id.et_register_code)
    EditText mEtRegisterCode;
    @Bind(R.id.et_register_pwd)
    EditText mEtRegisterPwd;
    @Bind(R.id.et_register_pwd_confirm)
    EditText mEtRegisterPwdConfirm;
    @Bind(R.id.btn_register_agree)
    Button mBtnRegisterAgree;
    @Bind(R.id.tv_register_terms)
    TextView mTvRegisterTerms;
    private String verifyCode = "";
    private String mMobile;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mTbRegister.setBtnEnabled(true,false);
        mTbRegister.setOnLeftClickListener(this);
    }


    @OnClick({R.id.tv_register_code, R.id.btn_register_agree, R.id.tv_register_terms})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register_code:
                //获取验证码
                obtainCheckCode();
                break;
            case R.id.btn_register_agree:
                //同意注册
                agreeRegister();
                break;
            case R.id.tv_register_terms:
                //查看条款

                break;
        }
    }

    private void agreeRegister() {
        String phone = mEtRegisterPhone.getText().toString().trim();
        String code = mEtRegisterCode.getText().toString().trim();
        String pwd = mEtRegisterPwd.getText().toString().trim();
        String pwdConfirm = mEtRegisterPwdConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(phone)||TextUtils.isEmpty(code)||TextUtils.isEmpty(pwd)||TextUtils.isEmpty(pwdConfirm)){
            ToastUtils.showToast(this,"请填写完整数据");
            return;
        }

        if (!TextUtils.equals(code,verifyCode)){
            ToastUtils.showToast(this,"验证码错误");
            return;
        }

        if (!TextUtils.equals(phone,mMobile)){
            ToastUtils.showToast(this,"账号错误");
            return;
        }

        if (!TextUtils.equals(pwd,pwdConfirm)){
            ToastUtils.showToast(this,"两次输入的密码不相同");
            return;
        }

        String time = String.valueOf(System.currentTimeMillis());
        //加密后的账号
        String encryptAccount = Base64.encode(AESUtils.encrypt(phone, Base64.encode(UserType.USER_TYPE_MOBILE + time)));
        //加密后的密码
        String passwordAccount = Base64.encode(AESUtils.encrypt(MD5Utils.md5(pwd), Base64.encode(UserType.USER_TYPE_MOBILE + time + phone)));

        LogUtils.d("加密后账号：" + encryptAccount);
        LogUtils.d("加密后密码：" + passwordAccount);

        SCHttpUtils.postWithParamsForLogin()
                .url(HttpApi.USER_REGISTER)
                .addParams("Timestamp", time)
                .addParams("encryptAccount", encryptAccount)
                .addParams("encryptPassword", passwordAccount)
                .addParams("userType", UserType.USER_TYPE_MOBILE)
                .build()
                .execute(new SCHttpCallBack<ResponseRegisteUserBean>(ResponseRegisteUserBean.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showToast(mContext, "注册失败！");
                        LogUtils.d("注册失败 ; 错误码：" + id);
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(ResponseRegisteUserBean response, int id) {
                        if (response == null){
                            ToastUtils.showToast(mContext, "注册失败！");
                            LogUtils.d("注册失败 ; 错误码：" + id);
                        }else {
                            ToastUtils.showToast(mContext,"注册成功");
                            LogUtils.d("userID = " + response.getUserInfo().getUserId());

                            finish();
                        }
                    }
                });
    }

    private void obtainCheckCode() {
        String phone = mEtRegisterPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            ToastUtils.showToast(this,"请填写手机号码");
            return;
        }else {
            if (AccountUtils.isPhone(phone)){
                getCheckCode(phone);
            }else {
                ToastUtils.showToast(this,"请输入正确格式的账号");
                return;
            }
        }

        CountDownTimeUtils countDownTimeUtils = new CountDownTimeUtils(mTvRegisterCode,60*1000,1000);
        countDownTimeUtils.start();
    }

    private void getCheckCode(String phone) {
        SCHttpUtils.post()
                .url(HttpApi.SMS_UNLOGIN_VERIFYCODE)
                .addParams("mobile",phone)
                .build()
                .execute(new SCHttpCallBack<ResponseSmsBean>(ResponseSmsBean.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("获取验证码失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(ResponseSmsBean response, int id) {
                        if (response != null) {
                            verifyCode = response.getSmsVerifyCode();
                            mMobile = response.getMobile();
                            LogUtils.d("从后台获取的验证码:" + verifyCode +"\n 手机账号 :" + mMobile);
                        } else {
                            LogUtils.d("请求的数据为空");
                        }
                    }
                });

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}