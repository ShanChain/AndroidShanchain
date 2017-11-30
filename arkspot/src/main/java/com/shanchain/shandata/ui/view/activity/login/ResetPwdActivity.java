package com.shanchain.shandata.ui.view.activity.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shanchain.data.common.base.UserType;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpCallBack;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.AccountUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.encryption.AESUtils;
import com.shanchain.data.common.utils.encryption.Base64;
import com.shanchain.data.common.utils.encryption.MD5Utils;
import com.shanchain.data.common.utils.encryption.SCJsonUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.ResponseSmsBean;
import com.shanchain.shandata.utils.CountDownTimeUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;


public class ResetPwdActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_reset_pwd)
    ArthurToolBar mTbResetPwd;
    @Bind(R.id.et_reset_phone)
    EditText mEtResetPhone;
    @Bind(R.id.et_reset_pwd)
    EditText mEtResetPwd;
    @Bind(R.id.tv_reset_code)
    TextView mTvResetCode;
    @Bind(R.id.et_reset_code)
    EditText mEtResetCode;
    @Bind(R.id.btn_reset_sure)
    Button mBtnResetSure;
    private String verifyCode = "";
    private String mMobile = "";

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mTbResetPwd.setBtnEnabled(true, false);
        mTbResetPwd.setOnLeftClickListener(this);
    }


    @OnClick({R.id.tv_reset_code, R.id.btn_reset_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_reset_code:
                //获取验证码
                obtainCheckCode();
                break;
            case R.id.btn_reset_sure:
                //确定修改
                resetPwd();
                break;
        }
    }

    private void resetPwd() {
        String phone = mEtResetPhone.getText().toString().trim();
        String code = mEtResetCode.getText().toString().trim();
        String pwd = mEtResetPwd.getText().toString().trim();

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(code) || TextUtils.isEmpty(pwd)) {
            ToastUtils.showToast(this, "请填写完整数据");
            return;
        }

        if (!TextUtils.equals(phone, mMobile)) {
            ToastUtils.showToast(this, "账号错误");
            return;
        }

        if (!TextUtils.equals(verifyCode, code)) {
            ToastUtils.showToast(this, "验证码错误");
            return;
        }

        String time = String.valueOf(System.currentTimeMillis());
        //加密后的账号
        String encryptAccount = Base64.encode(AESUtils.encrypt(phone, Base64.encode(UserType.USER_TYPE_MOBILE + time)));
        //加密后的密码
        String passwordAccount = Base64.encode(AESUtils.encrypt(MD5Utils.md5(pwd), Base64.encode(UserType.USER_TYPE_MOBILE + time + phone)));

        LogUtils.d("加密后账号：" + encryptAccount);
        LogUtils.d("加密后密码：" + passwordAccount);

        SCHttpUtils.postNoToken()
                .url(HttpApi.RESET_PWD)
                .addParams("Timestamp", time)
                .addParams("encryptAccount", encryptAccount)
                .addParams("encryptPassword", passwordAccount)
                .addParams("userType", UserType.USER_TYPE_MOBILE)
                .build()
                .execute(new SCHttpStringCallBack() {
                             @Override
                             public void onError(Call call, Exception e, int id) {
                                 ToastUtils.showToast(mContext, "重置密码失败");
                                 LogUtils.e("重置密码失败");
                                 e.printStackTrace();
                             }

                             @Override
                             public void onResponse(String response, int id) {
                                 try {
                                     LogUtils.i("重置密码成功 = " + response);
                                     String code = SCJsonUtils.parseCode(response);
                                     if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                                         ToastUtils.showToast(mContext,"重置密码成功");
                                         finish();
                                     }else{
                                         ToastUtils.showToast(mContext, "重置密码失败");
                                     }
                                 } catch (Exception e) {
                                     e.printStackTrace();
                                     ToastUtils.showToast(mContext, "重置密码失败");
                                 }
                             }
                         }
                );

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    private void obtainCheckCode() {
        String phone = mEtResetPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToast(this, "请填写手机号码");
            return;
        } else {
            if (AccountUtils.isPhone(phone)) {
                getCheckCode(phone);
            } else {
                ToastUtils.showToast(this, "请输入正确格式的账号");
                return;
            }
        }

        CountDownTimeUtils countDownTimeUtils = new CountDownTimeUtils(mTvResetCode, 60 * 1000, 1000);
        countDownTimeUtils.start();
    }

    private void getCheckCode(String phone) {
        SCHttpUtils.postNoToken()
                .url(HttpApi.SMS_UNLOGIN_VERIFYCODE)
                .addParams("mobile", phone)
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
                            LogUtils.d("从后台获取的验证码:" + verifyCode + "\n 手机账号 :" + mMobile);
                        } else {
                            LogUtils.d("请求的数据为空");
                        }
                    }
                });

    }

}
