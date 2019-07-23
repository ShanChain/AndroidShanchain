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
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintActivity;
import com.shanchain.shandata.utils.CountDownTimeUtils;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

public class BindPhoneActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {
    private ArthurToolBar toolBar;
    private EditText editTextAccount;
    private EditText editTextCode;
    private TextView textViewCode;
    private Button sure;
    private String mobilePhone, verifyCode;
    private String encryptOpenId = "", sign = "";
    private String salt;
    private String timestamp;
    private Spinner spPhoneNumber;
    private String [] countrysAttr= new String[]{"+86(CHN)","+852(HK)","+65(SGP)","+44(UK)"};
    private String [] countryPhoneAttr = new String[]{"+86","+852","+65","+44"};
    private String aAcount = "";
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void initViewsAndEvents() {
        toolBar = findViewById(R.id.tb_bind_pone);
        toolBar.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        toolBar = findViewById(R.id.tb_bind_pone);
        editTextAccount = findViewById(R.id.et_login_account);
        editTextCode = findViewById(R.id.et_register_code);
        textViewCode = findViewById(R.id.tv_register_code);
        spPhoneNumber = findViewById(R.id.sp_phone_number);
        sure = findViewById(R.id.btn_sure);
        toolBar.setOnLeftClickListener(this);
//        sign = MD5Utils.getMD5("647414" +"817372"+ "1545047532084");

        initData();
        addCountrysPhone();
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
                if(!"+86".equals(aAcount)){
                    mobilePhone = aAcount.substring(1,aAcount.length())+mobilePhone;
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
                                    /*Intent intent = new Intent(BindPhoneActivity.this, FootPrintActivity.class);
                                    startActivity(intent);*/
                                    goToMyWallet();
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

    //添加几个测试国家的手机号前缀
    private void addCountrysPhone(){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, countrysAttr);
        //下拉的样式res
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spPhoneNumber.setAdapter(spinnerAdapter);

        spPhoneNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                aAcount = countryPhoneAttr[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        aAcount = countryPhoneAttr[0];
    }

    private void obtainCheckCode(String mobilePhone) {
        if (TextUtils.isEmpty(mobilePhone)) {
            ToastUtils.showToast(this, getString(R.string.str_hint_register_phone));
            return;
        } else {
            /*if (AccountUtils.isPhone(mobilePhone)) {
                getCheckCode(mobilePhone);
            } else {
                ToastUtils.showToast(this, "请输入正确格式的账号");
                return;
            }*/
            getCheckCode(mobilePhone);
        }
        CountDownTimeUtils countDownTimeUtils = new CountDownTimeUtils(textViewCode, 60 * 1000, 1000);
        countDownTimeUtils.setContext(this);
        countDownTimeUtils.start();
    }

    //从后台获取验证码
    private void getCheckCode(String phone) {
        if(!"+86".equals(aAcount)){
            phone = aAcount.substring(1,aAcount.length())+phone;
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
