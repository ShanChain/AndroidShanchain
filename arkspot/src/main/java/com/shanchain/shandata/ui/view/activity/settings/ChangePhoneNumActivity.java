package com.shanchain.shandata.ui.view.activity.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.AccountUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.encryption.MD5Utils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.utils.CountDownTimeUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ChangePhoneNumActivity extends AppCompatActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_setting)
    ArthurToolBar tbSetting;
    @Bind(R.id.tv_set_password_title)
    TextView TvSetPhoneTitle;
    @Bind(R.id.tv_change_phone_hint)
    TextView tvChangePhoneHint;
    @Bind(R.id.tv_change_phone)
    TextView tvChangePhone;
    @Bind(R.id.tv_register_code)
    TextView tvRegisterCode;
    @Bind(R.id.edit_set_phone)
    RelativeLayout editSetPhone;
    @Bind(R.id.tv_set_password_hint)
    TextView tvSetPasswordHint;
    @Bind(R.id.tv_select_num)
    TextView tvSelectNum;
    @Bind(R.id.edit_phone_num)
    EditText editPhoneNum;
    @Bind(R.id.next_step)
    RelativeLayout nextStep;
    @Bind(R.id.btn_set_password_sure)
    Button btnSetPasswordSure;
    @Bind(R.id.edit_verify_code)
    EditText editVerifyCode;
    @Bind(R.id.relative_change_phone)
    RelativeLayout relativeChangePhone;
    @Bind(R.id.relative_first)
    RelativeLayout relativeFirst;
    @Bind(R.id.tv_set_phone_title)
    TextView tvSetPhoneTitle;
    @Bind(R.id.btn_set_phone_sure)
    Button btnSetPhoneSure;

    private String phone;
    private String verifyCode, smsVerifyCode = "";
    private String encryptOpenId = "", sign = "";
    private String salt;
    private String timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone = getIntent().getStringExtra("account");
        setContentView(R.layout.activity_change_phone_num);
        ButterKnife.bind(this);
        tbSetting.setTitleText("");
        tbSetting.setOnLeftClickListener(this);
        initData();
        editVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    tvSetPasswordHint.setVisibility(View.INVISIBLE);
                }
                if (s.toString().length() == 6) {
                    verifyCode = editVerifyCode.getText().toString();
                    if (verifyCode.equals(smsVerifyCode)) {
                        btnSetPasswordSure.setClickable(true);
                        btnSetPasswordSure.setBackground(getResources().getDrawable(R.drawable.shape_bg_btn_login));
                    } else {
                        tvSetPasswordHint.setVisibility(View.VISIBLE);
                    }
                } else {
                    btnSetPasswordSure.setClickable(false);
                    btnSetPasswordSure.setBackground(getResources().getDrawable(R.drawable.common_shape_coupon_btn_bg_gray));
                }
            }
        });

    }

    private void initData() {
        SCHttpUtils.postWithUserId()
                .url(HttpApi.USER_BOUND)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = SCJsonUtils.parseCode(response);
                        String data = SCJsonUtils.parseData(response);
                        if (NetErrCode.SUC_CODE.equals(code) || NetErrCode.COMMON_SUC_CODE.equals(code)) {
                            phone = SCJsonUtils.parseString(data, "mobile");
                            tvChangePhone.setText(phone);
                        }
                    }
                });
    }

    @OnClick({R.id.tv_register_code, R.id.btn_set_password_sure, R.id.btn_set_phone_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_register_code:
                obtainCheckCode(phone);
                break;
            case R.id.btn_set_password_sure:
                TvSetPhoneTitle.setVisibility(View.GONE);
                relativeFirst.setVisibility(View.GONE);
                nextStep.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_set_phone_sure:
                String mobilePhone = editPhoneNum.getText().toString().trim();
                if (TextUtils.isEmpty(mobilePhone)) {
                    ToastUtils.showToast(this, "请填写手机号码");
                    return;
                } else {
                    if (AccountUtils.isPhone(mobilePhone)) {
                        sign = MD5Utils.getMD5(smsVerifyCode + salt + timestamp);
                        SCHttpUtils.postWithUserId()
                                .url(HttpApi.CHANGE_PHONE)
                                .addParams("newPhone", mobilePhone)
                                .addParams("sign", sign)
                                .build()
                                .execute(new SCHttpStringCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {

                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        String code = SCJsonUtils.parseCode(response);
                                        String msg = SCJsonUtils.parseMsg(response);
                                        if (NetErrCode.COMMON_SUC_CODE.equals(code)) {
                                            ToastUtils.showToast(ChangePhoneNumActivity.this, "绑定手机号修改成功！");
                                        } else {
                                            ToastUtils.showToast(ChangePhoneNumActivity.this, "" + msg);

                                        }
                                    }
                                });
                    } else {
                        ToastUtils.showToast(this, "手机号格式错误");
                        return;
                    }
                }
                break;
        }
    }

    //检验手机号和验证码
    private void obtainCheckCode(String mobilePhone) {
        if (TextUtils.isEmpty(mobilePhone)) {
            ToastUtils.showToast(this, "手机号码有误");
            return;
        } else {
            getCheckCode(mobilePhone);
        }
        CountDownTimeUtils countDownTimeUtils = new CountDownTimeUtils(tvRegisterCode, 60 * 1000, 1000);
        countDownTimeUtils.start();
    }

    //从后台获取验证码
    private void getCheckCode(String phone) {
        SCHttpUtils.postWithUserId()
                .url(HttpApi.SETTING_GET_VERIFY_CODE)
                .addParams("mobile", phone)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showToast(ChangePhoneNumActivity.this, "网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (NetErrCode.COMMON_SUC_CODE.equals(code)) {
                            String data = SCJsonUtils.parseData(response);
                            smsVerifyCode = SCJsonUtils.parseString(data, "smsVerifyCode");
                            salt = SCJsonUtils.parseString(data, "salt");
                            timestamp = SCJsonUtils.parseString(data, "timestamp");
                            String mobile = SCJsonUtils.parseString(data, "mobile");
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
