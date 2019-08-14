package com.shanchain.shandata.ui.view.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.AccountUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.VersionUtils;
import com.shanchain.data.common.utils.encryption.MD5Utils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.ui.model.PhoneFrontBean;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintNewActivity;
import com.shanchain.shandata.utils.CountDownTimeUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

public class BindPhoneActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {
    @Bind(R.id.tb_bind_pone)
    ArthurToolBar toolBar;
    @Bind(R.id.tv_phone_q_1)
    TextView tvPhoneQ1;
    @Bind(R.id.et_login_account)
    EditText editTextAccount;
    @Bind(R.id.et_register_code)
    EditText editTextCode;
    @Bind(R.id.tv_register_code)
    TextView textViewCode;
    @Bind(R.id.btn_sure)
    Button sure;
    private String mobilePhone, verifyCode;
    private String encryptOpenId = "", sign = "";
    private String salt;
    private String timestamp;
    private String aAcount = "+86";

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void initViewsAndEvents() {
        toolBar.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        toolBar.setOnLeftClickListener(this);

        initData();
    }

    /**
     * 收到用户选择某个手机前缀
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPhoneFront(PhoneFrontBean phoneFrontBean){
        if(phoneFrontBean !=null){
            if(phoneFrontBean.getSourceType() == 4){
                tvPhoneQ1.setText(phoneFrontBean.getPhoneFront());
                aAcount = phoneFrontBean.getPhoneFront();
            }
        }
    }

    private void initData() {
        encryptOpenId = getIntent() != null ? getIntent().getStringExtra("encryptOpenId") : "";
        textViewCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobilePhone = editTextAccount.getText().toString().trim();
                verifyCode = editTextCode.getText().toString().trim();
                obtainCheckCode(mobilePhone);
                LogUtils.d("sign", sign);
            }
        });

        /*
         * 动态登录、绑定手机号
         * */
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobilePhone = editTextAccount.getText().toString();
                verifyCode = editTextCode.getText().toString();
                if (TextUtils.isEmpty(mobilePhone) || TextUtils.isEmpty(verifyCode)) {
                    ToastUtils.showToast(BindPhoneActivity.this, getString(R.string.phone_sms_not_entity));
                    return;
                }
                if (!"+86".equals(aAcount)) {
                    mobilePhone = aAcount.substring(1, aAcount.length()) + mobilePhone;
                }
                sign = MD5Utils.getMD5(verifyCode + salt + timestamp);
                final String localVersion = VersionUtils.getVersionName(mContext);
                SCHttpUtils.postNoToken()
                        .url(HttpApi.SMS_LOGIN)
                        .addParams("deviceToken", JPushInterface.getRegistrationID(mContext))
                        .addParams("version", localVersion)
                        .addParams("os", "android")
                        .addParams("channel", "" + MyApplication.getAppMetaData(getApplicationContext(), "UMENG_CHANNEL"))
                        .addParams("encryptOpenId", encryptOpenId)
                        .addParams("mobile", mobilePhone)
                        .addParams("sign", sign)
                        .addParams("verifyCode", verifyCode)
                        .build()
                        .execute(new SCHttpStringCallBack(BindPhoneActivity.this) {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                LogUtils.d("dynamicLogin", "三方登录失败" + e.toString());
                                ToastUtils.showToast(BindPhoneActivity.this, "网络异常");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                String code = JSONObject.parseObject(response).getString("code");
                                if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                                    String data = JSONObject.parseObject(response).getString("data");
                                    String userInfo = JSONObject.parseObject(data).getString("userInfo");
                                    String token = JSONObject.parseObject(data).getString("token");
                                    String userId = JSONObject.parseObject(userInfo).getString("userId");

                                    SCCacheUtils.setCache("0", Constants.CACHE_CUR_USER, userId + "");
                                    SCCacheUtils.setCache(userId + "", Constants.CACHE_USER_INFO, new Gson().toJson(userInfo));
                                    SCCacheUtils.setCache(userId + "", Constants.CACHE_TOKEN, userId + "_" + token);

                                    LogUtils.d("dynamicLogin", "三方登录成功");
//                                    Intent intent = new Intent(BindPhoneActivity.this,HomeActivity.class);
                                    Intent intent = new Intent(BindPhoneActivity.this, FootPrintNewActivity.class);
                                    startActivity(intent);
                                    finish();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                            ToastUtils.showToast(BindPhoneActivity.this, "关联手机号成功");
                                        }
                                    });
                                }
                            }
                        });
            }
        });
    }

    //获取手机号前缀
    @OnClick(R.id.tv_phone_q_1)
    void phoneFront(){
        startActivity(new Intent(this,PhoneFrontActivity.class).putExtra("type",4));
    }

    private void obtainCheckCode(String mobilePhone) {
        if (TextUtils.isEmpty(mobilePhone)) {
            ToastUtils.showToast(this, getString(R.string.str_hint_register_phone));
            return;
        } else {
            if ("+86".equals(aAcount)){
                if(AccountUtils.isPhone(mobilePhone)){
                    getCheckCode(mobilePhone);
                }else {
                    ToastUtils.showToast(this, R.string.phone_right);
                    return;
                }
            }else {
                getCheckCode(mobilePhone);
            }

        }
        CountDownTimeUtils countDownTimeUtils = new CountDownTimeUtils(textViewCode, 60 * 1000, 1000);
        countDownTimeUtils.setContext(this);
        countDownTimeUtils.start();
    }

    //从后台获取验证码
    private void getCheckCode(String phone) {
        if (!"+86".equals(aAcount)) {
            phone = aAcount.substring(1, aAcount.length()) + phone;
        }
        SCHttpUtils.postNoToken()
                .url(HttpApi.SMS_BIND_UNLOGIN_VERIFYCODE)
                .addParams("mobile", phone)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showToast(BindPhoneActivity.this, getString(R.string.network_wrong));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            salt = JSONObject.parseObject(data).getString("salt");
                            timestamp = JSONObject.parseObject(data).getString("timestamp");
                            LogUtils.d("response", data.toString());
                            LogUtils.d("data", "盐值" + salt + " 时间戳：" + timestamp);
                        }
                    }
                });

    }


    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
