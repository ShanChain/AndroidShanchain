package com.shanchain.shandata.ui.view.activity.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.vod.common.utils.ToastUtil;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.utils.AccountUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.data.common.base.UserType;
import com.shanchain.shandata.ui.model.PhoneFrontBean;
import com.shanchain.shandata.ui.model.ResponseRegisteUserBean;
import com.shanchain.shandata.ui.model.ResponseSmsBean;
import com.shanchain.shandata.utils.CountDownTimeUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.encryption.AESUtils;
import com.shanchain.data.common.utils.encryption.Base64;
import com.shanchain.data.common.utils.encryption.MD5Utils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
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
    @Bind(R.id.tv_psd_login)
    TextView tvPsdLogin;
    @Bind(R.id.tv_phone_q_1)
    TextView tvPhoneQ1;
    private String verifyCode = "";
    private String mMobile;

    private String aAcount = "+86";
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
//        mBtnRegisterAgree.setClickable(false);
//        mBtnRegisterAgree.setBackground(getResources().getDrawable(R.drawable.shape_btn_bg_send_unenable));

    }

    private void initToolBar() {
        mTbRegister.setBtnEnabled(true, false);
        mTbRegister.setOnLeftClickListener(this);


    }
    /**
     * 收到用户选择某个手机前缀
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPhoneFront(PhoneFrontBean phoneFrontBean){
        if(phoneFrontBean !=null){
            if(phoneFrontBean.getSourceType() == 3){
                tvPhoneQ1.setText(phoneFrontBean.getPhoneFront());
                aAcount = phoneFrontBean.getPhoneFront();
            }
        }
    }

    @OnClick({R.id.tv_register_code, R.id.btn_register_agree, R.id.tv_register_terms,R.id.tv_psd_login,R.id.tv_phone_q_1})
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
                Intent intent = new Intent(mContext, com.shanchain.shandata.rn.activity.SCWebViewXYActivity.class);
                JSONObject obj = new JSONObject();
                obj.put("url", "http://h5.qianqianshijie.com/agreement");
                obj.put("title", getString(R.string.user_agreement));
                String webParams = obj.toJSONString();
                intent.putExtra("webParams", webParams);
                startActivity(intent);
                break;
            case R.id.tv_psd_login:
                readyGo(LoginActivity.class);
                finish();
                break;
            case R.id.tv_phone_q_1:
                startActivity(new Intent(this,PhoneFrontActivity.class).putExtra("type",3));
                break;
        }
    }

    private void agreeRegister() {
        String phone = mEtRegisterPhone.getText().toString().trim();
        String code = mEtRegisterCode.getText().toString().trim();
        String pwd = mEtRegisterPwd.getText().toString().trim();
        String pwdConfirm = mEtRegisterPwdConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(code) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdConfirm)) {
            ToastUtils.showToast(this, getString(R.string.full_information));
            return;
        }
        if(pwd.length()<6){
            ToastUtils.showToast(this, getString(R.string.str_register_hint_pwd));
            return;
        }

        if (!TextUtils.equals(code, verifyCode)) {
            ToastUtils.showToast(this, getString(R.string.sms_code_wrong));
            return;
        }
        //支持国外手机号判断
        if(!"+86".equals(aAcount)){
            phone = aAcount.substring(1,aAcount.length())+phone;
        }
        if (!TextUtils.equals(phone, mMobile)) {
            ToastUtils.showToast(this, getString(R.string.account_error));
            return;
        }

        if (!TextUtils.equals(pwd, pwdConfirm)) {
            ToastUtils.showToast(this, R.string.psw_twice_dif);
            return;
        }

        String time = String.valueOf(System.currentTimeMillis());
        //加密后的账号
        final String encryptAccount = Base64.encode(AESUtils.encrypt(phone, Base64.encode(UserType.USER_TYPE_MOBILE + time)));
        //加密后的密码
        final String passwordAccount = Base64.encode(AESUtils.encrypt(MD5Utils.md5(pwd), Base64.encode(UserType.USER_TYPE_MOBILE + time + phone)));

        LogUtils.d("加密后账号：" + encryptAccount);
        LogUtils.d("加密后密码：" + passwordAccount);

        /**注册环信聊天用户URL*/
//        String HX_USER_REGIST = BASE_URL_IM + "/hx/user/regist";


        SCHttpUtils.postWithParamsForLogin()
                .url(HttpApi.USER_REGISTER)
                .addParams("Timestamp", time)
                .addParams("deviceToken", "adhiakdh23424")
                .addParams("encryptAccount", encryptAccount)
                .addParams("encryptPassword", passwordAccount)
                .addParams("userType", UserType.USER_TYPE_MOBILE)
                .build()
                .execute(new SCHttpCallBack<ResponseRegisteUserBean>(ResponseRegisteUserBean.class, RegisterActivity.this) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        ToastUtils.showToast(mContext, "注册失败！"+ id);
                        LogUtils.d("注册失败 ; 错误码：" + id);
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(ResponseRegisteUserBean response, int id) {
                        if (response == null) {
//                            ToastUtils.showToast(mContext, "注册失败！" + id);
                            LogUtils.d("注册失败 ; 错误码：" + id);
                        } else {
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showToast(mContext, R.string.register_success);
                                }
                            });
                            LogUtils.d("userID = " + response.getUserInfo().getUserId());
//                            JMessageClient.register(encryptAccount, passwordAccount, new BasicCallback() {
//                                @Override
//                                public void gotResult(int i, String s) {
//                                    LogUtils.d("注册极光用户成功");
//                                }
//                            });

                            finish();
                        }
                    }
                });
    }

    public void registerJmUser(final String jmUser, final String jmPassword) {
        //注册极光账号
        JMessageClient.register(jmUser, jmPassword, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (s.equals("Success")) {
                    LogUtils.d("极光IM############## 注册成功 ##############极光IM");
                    RoleManager.switchRoleCacheHx(jmUser, jmPassword);
                } else {
                    LogUtils.d("极光IM############## 注册失败 ##############极光IM");
//                                ToastUtils.showToast(LoginActivity.this, "消息服务异常");

                }

            }
        });
    }


    private void obtainCheckCode() {
        String phone = mEtRegisterPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToast(this, getString(R.string.str_login_account));
            return;
        } else {
            if ("+86".equals(aAcount)){
                if(AccountUtils.isPhone(phone)){
                    getCheckCode(phone);
                }else {
                    ToastUtils.showToast(this, R.string.phone_right);
                    return;
                }
            }else {
                getCheckCode(phone);
            }
        }

        CountDownTimeUtils countDownTimeUtils = new CountDownTimeUtils(mTvRegisterCode, 60 * 1000, 1000);
        countDownTimeUtils.setContext(this);
        countDownTimeUtils.start();
    }

    //从后台获取验证码
    private void getCheckCode(String phone) {
        if(!"+86".equals(aAcount)){
            phone = aAcount.substring(1,aAcount.length())+phone;
        }
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
                            /*mBtnRegisterAgree.setClickable(true);
                            mBtnRegisterAgree.setBackground(getResources().getDrawable(R.drawable.shape_bg_btn_login));*/
                            mMobile = response.getMobile();
                            LogUtils.d("从后台获取的验证码:" + verifyCode + "\n 手机账号 :" + mMobile);
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
