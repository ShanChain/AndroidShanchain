package com.shanchain.shandata.ui.view.activity.settings;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.UserType;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.encryption.AESUtils;
import com.shanchain.data.common.utils.encryption.Base64;
import com.shanchain.data.common.utils.encryption.MD5Utils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.view.activity.login.LoginActivity;
import com.shanchain.shandata.utils.CountDownTimeUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class SetPasswordActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    @Bind(R.id.tb_setting)
    ArthurToolBar tbSetting;
    @Bind(R.id.tv_set_password)
    TextView tvSetPassword;
    @Bind(R.id.edit_set_password)
    EditText editSetPassword;
    @Bind(R.id.tv_set_password_hint)
    TextView tvSetPasswordHint;
    @Bind(R.id.btn_set_password_sure)
    Button btnSetPasswordSure;

    //密码校验正则表达式，8至20位长度，允许字母数字符号至少两种组合
    private static final String SPE_CHAT = "^[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+$";
    @Bind(R.id.relative_set_password)
    RelativeLayout relativeSetPassword;
    @Bind(R.id.tv_reset_password)
    TextView tvResetPassword;
    @Bind(R.id.tv_change_phone_hint)
    TextView tvChangePhoneHint;
    @Bind(R.id.tv_change_phone)
    TextView tvChangePhone;
    @Bind(R.id.change_password)
    RelativeLayout changePassword;
    @Bind(R.id.tv_register_code)
    TextView tvRegisterCode;
    @Bind(R.id.edit_verify_code)
    EditText editVerifyCode;
    @Bind(R.id.edit_set_phone)
    RelativeLayout editSetPhone;
    @Bind(R.id.edit_error_hint)
    TextView editErrorHint;
    @Bind(R.id.btn_reset_password_sure)
    Button btnResetPasswordSure;
    @Bind(R.id.relative_reset_password)
    RelativeLayout relativeResetPassword;
    private String phone;
    private boolean password;
    private String smsVerifyCode;
    private String salt;
    private String timestamp;
    private String verifyCode;
    private String sign;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_set_password;
    }

    @Override
    protected void initViewsAndEvents() {
        password = getIntent().getBooleanExtra("password", false);
        if (password) {
            relativeSetPassword.setVisibility(View.GONE);
            relativeResetPassword.setVisibility(View.VISIBLE);
        } else {
            relativeSetPassword.setVisibility(View.VISIBLE);
            relativeResetPassword.setVisibility(View.GONE);
        }
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
                    editErrorHint.setVisibility(View.INVISIBLE);
                }
                if (s.toString().length() == 6) {
                    verifyCode = editVerifyCode.getText().toString();
                    if (verifyCode.equals(smsVerifyCode)) {
                        btnResetPasswordSure.setClickable(true);
                        btnResetPasswordSure.setBackground(getResources().getDrawable(R.drawable.shape_bg_btn_login));
                    } else {
                        editErrorHint.setVisibility(View.VISIBLE);
                    }
                } else {
                    btnResetPasswordSure.setClickable(false);
                    btnResetPasswordSure.setBackground(getResources().getDrawable(R.drawable.common_shape_coupon_btn_bg_gray));
                }
            }
        });
        initToolBar();
        initData();
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
                        if (NetErrCode.COMMON_SUC_CODE.equals(code) || NetErrCode.SUC_CODE.equals(code)) {
                            String data = SCJsonUtils.parseData(response);
                            phone = SCJsonUtils.parseString(data, "mobile");
                            tvChangePhone.setText(phone);
                        }
                    }
                });

    }

    private void initToolBar() {
        ArthurToolBar arthurToolBar = findViewById(R.id.tb_setting);
        arthurToolBar.setTitleText("");
        arthurToolBar.setOnLeftClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_set_password_sure, R.id.tv_register_code, R.id.btn_reset_password_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_set_password_sure:
                String password = editSetPassword.getText().toString().trim();
                if (password.length() < 8 || password.length() > 20) {
                    ToastUtils.showToast(SetPasswordActivity.this, getString(R.string.password_fh));
                } else if (password.matches("^[0-9]+$")) {//排除纯数字
                    ToastUtils.showToast(SetPasswordActivity.this, R.string.least_have_two);
                } else if (password.matches("^[A-Za-z]+$")) {//排除纯字母
                    ToastUtils.showToast(SetPasswordActivity.this, R.string.least_have_two);
                } else if (password.matches(SPE_CHAT)) {//排除纯符号
                    ToastUtils.showToast(SetPasswordActivity.this, R.string.least_have_two);
                } else {
//                    ToastUtils.showToast(mContext, "设置密码成功");
                    resetPwd();
                }
                break;
            case R.id.tv_register_code:
                obtainCheckCode(phone);
                break;
            case R.id.btn_reset_password_sure:
                relativeSetPassword.setVisibility(View.VISIBLE);
                relativeResetPassword.setVisibility(View.GONE);
                break;
        }

    }

    private void resetPwd() {
        String pwd = editSetPassword.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showToast(this, getString(R.string.str_login_pwd));
            return;
        }
        String time = String.valueOf(System.currentTimeMillis());
        //加密后的账号
        phone = phone == null ? getIntent().getStringExtra("account") : phone;
        String encryptAccount = Base64.encode(AESUtils.encrypt(phone, Base64.encode(UserType.USER_TYPE_MOBILE + time)));
        //加密后的密码
        String passwordAccount = Base64.encode(AESUtils.encrypt(MD5Utils.md5(pwd), Base64.encode(UserType.USER_TYPE_MOBILE + time + phone)));

        LogUtils.d("加密后账号：" + encryptAccount);
        LogUtils.d("加密后密码：" + passwordAccount);
        sign = MD5Utils.getMD5(smsVerifyCode + salt + timestamp);
        CustomDialog showPasswordDialog = new CustomDialog(SetPasswordActivity.this, true, 1.0,
                R.layout.dialog_bottom_wallet_password,
                new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
        SCHttpUtils.postWithChaId()
                .url(HttpApi.RESET_PASSWORD)
                .addParams("Timestamp", time)
                .addParams("encryptAccount", encryptAccount + "")
                .addParams("encryptPassword", passwordAccount + "")
                .addParams("sign", sign)
                .addParams("userType", UserType.USER_TYPE_MOBILE)
                .build()
                .execute(new SCHttpStringCallBack(mContext, showPasswordDialog) {
                             @Override
                             public void onError(Call call, Exception e, int id) {
//                                 ToastUtils.showToast(mContext, "请检查网络连接");
                                 LogUtils.e("设置密码失败");
                                 e.printStackTrace();
                             }

                             @Override
                             public void onResponse(String response, int id) {
                                 try {
                                     LogUtils.i("设置密码成功 = " + response);
                                     String code = SCJsonUtils.parseCode(response);
                                     String msg = SCJsonUtils.parseMsg(response);
                                     if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                         ThreadUtils.runOnMainThread(new Runnable() {
                                             @Override
                                             public void run() {
                                                 CustomDialog customDialog = new CustomDialog(mContext, R.layout.common_dialog_costom,
                                                         new int[]{R.id.tv_input_dialog_title,
                                                                 R.id.btn_dialog_task_detail_sure, R.id.even_message_content});
                                                 customDialog.setMessageContent(getString(R.string.login_again));
                                                 customDialog.setDialogTitle("  ");
                                                 customDialog.setMessageContentSize(16);
                                                 customDialog.setSureText(getString(R.string.str_sure));
                                                 customDialog.setCancelable(false);
                                                 customDialog.setCanceledOnTouchOutside(false);
                                                 customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                                                     @Override
                                                     public void OnItemClick(CustomDialog dialog, View view) {
                                                         switch (view.getId()) {
                                                             case R.id.btn_dialog_task_detail_sure:
                                                                 readyGo(LoginActivity.class);
                                                                 ActivityStackManager.getInstance().finishAllActivity();
                                                                 break;
                                                         }
                                                     }
                                                 });
                                                 customDialog.show();
                                             }
                                         });
                                     } else {
                                         ToastUtils.showToast(mContext, getString(R.string.password_set_failed) + msg);
                                     }
                                 } catch (Exception e) {
                                     e.printStackTrace();
                                     ToastUtils.showToast(mContext, getString(R.string.password_set_failed));
                                 }
                             }
                         }
                );
    }

    //检验手机号和验证码
    private void obtainCheckCode(String mobilePhone) {
        /*if (TextUtils.isEmpty(mobilePhone)) {
            ToastUtils.showToast(this, "手机号码有误");
            return;
        } else {
            getCheckCode(mobilePhone);
        }*/
        getCheckCode(mobilePhone);
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
                        ToastUtils.showToast(SetPasswordActivity.this, getString(R.string.network_wrong));
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
