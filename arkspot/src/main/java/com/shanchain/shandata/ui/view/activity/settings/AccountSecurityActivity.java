package com.shanchain.shandata.ui.view.activity.settings;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.UserType;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.cache.SharedPreferencesUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.SCInputDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.encryption.AESUtils;
import com.shanchain.data.common.utils.encryption.Base64;
import com.shanchain.data.common.utils.encryption.MD5Utils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.VerifiedActivity;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.share.android.api.AuthListener;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.model.AccessTokenInfo;
import cn.jiguang.share.android.model.BaseResponseInfo;
import cn.jiguang.share.android.utils.Logger;
import cn.jiguang.share.facebook.Facebook;
import cn.jiguang.share.qqmodel.QQ;
import cn.jiguang.share.wechat.Wechat;
import okhttp3.Call;

public class AccountSecurityActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tv_user_id)
    TextView tvMajaId;
    @Bind(R.id.relative_user_id)
    RelativeLayout relativeMajaId;
    @Bind(R.id.tv_login_password)
    TextView tvLoginPassword;
    @Bind(R.id.tv_set_password)
    TextView tvSetPassword;
    @Bind(R.id.iv_login_password_arrow)
    ImageView ivLoginPasswordArrow;
    @Bind(R.id.relative_login_password)
    RelativeLayout relativeLoginPassword;
    @Bind(R.id.tv_bind_phone)
    TextView tvBindPhone;
    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @Bind(R.id.iv_bind_phone_arrow)
    ImageView ivBindPhoneArrow;
    @Bind(R.id.relative_bind_phone)
    RelativeLayout relativeBindPhone;
    @Bind(R.id.relative_top)
    RelativeLayout relativeTop;
    @Bind(R.id.tv_account)
    TextView tvAccount;
    @Bind(R.id.tv_account_wechat)
    TextView tvAccountWechat;
    @Bind(R.id.tv_wechat_info)
    TextView tvWechatInfo;
    @Bind(R.id.iv_wechat_arrow)
    ImageView ivWechatArrow;
    @Bind(R.id.relative_account)
    RelativeLayout relativeAccount;
    @Bind(R.id.tv_qq)
    TextView tvQq;
    @Bind(R.id.tv_qq_info)
    TextView tvQqInfo;
    @Bind(R.id.iv_qq_arrow)
    ImageView ivQqArrow;
    @Bind(R.id.relative_qq)
    RelativeLayout relativeQq;
    @Bind(R.id.tv_facebook)
    TextView tvFacebook;
    @Bind(R.id.tv_facebook_info)
    TextView tvFacebookInfo;
    @Bind(R.id.iv_facebook_arrow)
    ImageView ivFacebookArrow;
    @Bind(R.id.relative_facebook)
    RelativeLayout relativeFacebook;
    @Bind(R.id.tv_identity)
    TextView tvIdentity;
    @Bind(R.id.tv_identity_info)
    TextView tvIdentityInfo;
    @Bind(R.id.iv_update_arrow)
    ImageView ivUpdateArrow;
    @Bind(R.id.relative_identity)
    RelativeLayout relativeIdentity;
    @Bind(R.id.relative_bottom)
    RelativeLayout relativeBottom;
    @Bind(R.id.relative_logout_account)
    RelativeLayout relativeLogoutAccount;

    private AuthListener mAuthListener, removeAuthListener;
    private String accessToken, encryptOpenId, encryptToken16, name, imageUrl, thirdOpenId;
    private int gender;
    private long expiration;
    private String sign, verifyCode, mobilePhone;
    private String salt;
    private String timestamp;
    private String mobile;
    private StandardDialog standardDialog;
    private SCInputDialog mScInputDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String userType = (String) msg.obj;
                if (!TextUtils.isEmpty(thirdOpenId) && !TextUtils.isEmpty(userType)) {
                    bindThirdPlatform(thirdOpenId, userType);
                }
            } else if (msg.what == 1) {
                String userType = (String) msg.obj;
                bindThirdPlatform("", userType);
            } else {
                String toastMsg = (String) msg.obj;
                Toast.makeText(AccountSecurityActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private boolean facebook;
    private boolean idcard;
    private boolean password;
    private boolean qq;
    private boolean wechat;
    private boolean weibo;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_account_security;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolbar();
        tvMajaId.setText(SCCacheUtils.getCacheUserId() + "");
        initCurrentUserStatus();
        isRealName();
        initData();
    }

    private void initData() {
        SCHttpUtils.postWithUserId()
                .url(HttpApi.USER_BOUND)
                .build()
                .execute(new SCHttpStringCallBack(mContext, commonDialog) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = SCJsonUtils.parseCode(response);
                        if (NetErrCode.SUC_CODE.equals(code) || NetErrCode.COMMON_SUC_CODE.equals(code)) {
                            String data = SCJsonUtils.parseData(response);
//                        String email = SCJsonUtils.parseString(data, "email");
                            facebook = SCJsonUtils.parseBoolean(data, "facebook");
                            idcard = SCJsonUtils.parseBoolean(data, "idcard");
                            mobile = SCJsonUtils.parseString(data, "mobile");
                            password = SCJsonUtils.parseBoolean(data, "password");
                            qq = SCJsonUtils.parseBoolean(data, "qq");
                            String userId = SCJsonUtils.parseString(data, "userId");
                            wechat = SCJsonUtils.parseBoolean(data, "wechat");
                            weibo = SCJsonUtils.parseBoolean(data, "weibo");
                            if (!TextUtils.isEmpty(mobile)) {
                                tvPhoneNum.setVisibility(View.VISIBLE);
                                tvPhoneNum.setText(mobile + "");//显示手机号
                            }
                            //显示用户id
                            if (TextUtils.isEmpty(SCCacheUtils.getCacheUserId()))
                                tvMajaId.setText(userId + "");
                            //是否绑定微信
                            if (wechat) {
                                tvWechatInfo.setText(getResources().getString(R.string.bound));
                            } else {
                                tvWechatInfo.setText(getResources().getString(R.string.unbound));
                            }
                            //是否绑定qq
                            if (qq) {
                                tvQqInfo.setText(getResources().getString(R.string.bound));
                            } else {
                                tvQqInfo.setText(getResources().getString(R.string.unbound));
                            }
                            //是否绑定facebook
                            if (facebook) {
                                tvFacebookInfo.setText(getResources().getString(R.string.bound));
                            } else {
                                tvFacebookInfo.setText(getResources().getString(R.string.unbound));
                            }
                            //是否实名认证
                            if (idcard) {
                                tvIdentityInfo.setText(getResources().getString(R.string.certified));
                            } else {
                                tvIdentityInfo.setText(getResources().getString(R.string.uncertified));
                            }
                            //是否设置登录密码
                            if (!password) {
                                tvSetPassword.setVisibility(View.VISIBLE);
                                tvSetPassword.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        readyGo(SetPasswordActivity.class);
                                    }
                                });
                            } else {
                                tvSetPassword.setVisibility(View.GONE);
//                            relativeLoginPassword.setOnClickListener(null);
                            }
                        }
                    }
                });
    }

    private void initToolbar() {
        ArthurToolBar arthurToolBar = findViewById(R.id.tb_setting);
        arthurToolBar.setTitleText("");
        arthurToolBar.setOnLeftClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @OnClick({R.id.relative_login_password, R.id.relative_bind_phone, R.id.relative_account, R.id.relative_qq,
            R.id.relative_facebook, R.id.relative_identity,R.id.relative_logout_account})
    public void onViewClicked(View view) {
        StandardDialog standardDialog = new StandardDialog(mContext);
        standardDialog.setStandardTitle(" ");
        standardDialog.setStandardMsg(getResources().getString(R.string.dialog_tip_unbind));
        standardDialog.setSureText(getResources().getString(R.string.dialog_cancel));
        standardDialog.setCancelText(getResources().getString(R.string.unbind));

        switch (view.getId()) {
            case R.id.relative_login_password:
                Bundle bundle = new Bundle();
                bundle.putString("account", mobile);
                bundle.putBoolean("password", password);
                readyGo(SetPasswordActivity.class, bundle);
                break;
            case R.id.relative_bind_phone:
                Bundle phoneBundle = new Bundle();
                phoneBundle.putString("account", mobile);
                readyGo(ChangePhoneNumActivity.class);
                break;
            case R.id.relative_account: //第三方账号，绑定微信
                if (wechat) {
                    standardDialog.setCallback(new Callback() {
                        @Override
                        public void invoke() { //取消

                        }
                    }, new Callback() { //解绑
                        @Override
                        public void invoke() {
                            thirdPlatform(UserType.USER_TYPE_WEIXIN);
                            initData();
                        }
                    });
                    standardDialog.show();
                } else {
                    thirdPlatform(UserType.USER_TYPE_WEIXIN);
                }
                break;
            case R.id.relative_qq://绑定qq
                if (qq) {
                    standardDialog.setCallback(new Callback() {
                        @Override
                        public void invoke() {

                        }
                    }, new Callback() {
                        @Override
                        public void invoke() {
                            thirdPlatform(UserType.USER_TYPE_QQ);
                            initData();
                        }
                    });
                    standardDialog.show();
                } else {
                    thirdPlatform(UserType.USER_TYPE_QQ);
                }
                break;
            case R.id.relative_facebook://绑定facebook
                if (facebook) {
                    standardDialog.setCallback(new Callback() {
                        @Override
                        public void invoke() {

                        }
                    }, new Callback() {
                        @Override
                        public void invoke() {
                            thirdPlatform(UserType.USER_TYPE_FB);
                            initData();
                        }
                    });
                    standardDialog.show();
                } else {
                    thirdPlatform(UserType.USER_TYPE_FB);
                }
                break;
            case R.id.relative_identity://实名认证
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("idcard", idcard);
                readyGo(VerifiedActivity.class, bundle1);
                break;
            case R.id.relative_logout_account://注销账号
                isLogoutTip();
                break;
        }
    }

    public void thirdPlatform(final String userType) {
        List<String> platformList = JShareInterface.getPlatformList();
        mAuthListener = new AuthListener() {
            @Override
            public void onComplete(Platform platform, int action, BaseResponseInfo data) {
                Logger.dd(TAG, "onComplete:" + platform + ",action:" + action + ",data:" + data);
                String toastMsg = null;

                switch (action) {
                    case Platform.ACTION_AUTHORIZING:
                        if (data instanceof AccessTokenInfo) {        //授权信息
                            accessToken = ((AccessTokenInfo) data).getToken();//token
                            expiration = ((AccessTokenInfo) data).getExpiresIn();//token有效时间，时间戳
                            String refresh_token = ((AccessTokenInfo) data).getRefeshToken();//refresh_token
                            thirdOpenId = ((AccessTokenInfo) data).getOpenid();//openid
                            //授权原始数据，开发者可自行处理
                            String originData = data.getOriginData();
//                            toastMsg = "授权成功:" + data.toString();
                            toastMsg = "授权成功";
                            LogUtils.d(TAG, "openid:" + thirdOpenId + ",token:" + accessToken + ",expiration:" + expiration + ",refresh_token:" + refresh_token);
                            LogUtils.d(TAG, "originData:" + originData);
                            //加密后的accesstoken
                            LogUtils.d("accessToken : " + accessToken);
                            String accesToken = accessToken.substring(0, 16);
                            LogUtils.d("accesToken截取 : " + accesToken);
                            String time = String.valueOf(System.currentTimeMillis());
                            encryptToken16 = Base64.encode(AESUtils.encrypt(MD5Utils.md5(accesToken), Base64.encode(userType + time + thirdOpenId)));
                            SharedPreferencesUtils.setPreferences(AccountSecurityActivity.this, userType, encryptToken16);
                            //加密后的openid
                            encryptOpenId = Base64.encode(AESUtils.encrypt(thirdOpenId, Base64.encode(userType + time)));
                            LogUtils.d("加密后openid：" + encryptOpenId);
                            LogUtils.d("加密后accesstoken：" + encryptToken16);
                            LogUtils.d("用户类型：" + userType);
                            if (handler != null) {
                                Message msg = new Message();
                                msg.what = 0;
                                msg.obj = userType;
                                handler.sendMessage(msg);
                            }
                        }
                        break;
                    case Platform.ACTION_REMOVE_AUTHORIZING:
                        toastMsg = "删除授权成功";
                        if (handler != null) {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = userType;
                            handler.sendMessage(msg);
                        }
                        break;
                    case Platform.ACTION_USER_INFO:
                        if (data instanceof cn.jiguang.share.android.model.UserInfo) {      //第三方个人信息
                            String openId = ((cn.jiguang.share.android.model.UserInfo) data).getOpenid();  //openid
                            name = ((cn.jiguang.share.android.model.UserInfo) data).getName();  //昵称
                            imageUrl = ((cn.jiguang.share.android.model.UserInfo) data).getImageUrl();  //头像url
                            String accessToken = ((cn.jiguang.share.android.model.UserInfo) data).getOriginData();  //token
                            gender = ((cn.jiguang.share.android.model.UserInfo) data).getGender();//性别, 1表示男性；2表示女性
                            String sex = gender == 1 ? "男" : "女";
                            //个人信息原始数据，开发者可自行处理
                            String originData = data.getOriginData();
//                            toastMsg = "获取个人信息成功:" + data.toString()
//                            toastMsg = "获取个人信息成功";
                            Logger.dd(TAG, "openid:" + openId + ",name:" + name + ",gender:" + gender + ",imageUrl:" + imageUrl);
                            Logger.dd(TAG, "originData:" + originData);
                            String time = String.valueOf(System.currentTimeMillis());
                            encryptOpenId = Base64.encode(AESUtils.encrypt(openId, Base64.encode(userType + time)));
                        }
                        if (handler != null) {
                            toastMsg = "获取个人信息成功";
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = userType;
                            handler.sendMessage(msg);
                        }
                        break;
                    default:
                        if (handler != null) {
//                            Message msg = handler.obtainMessage(1);
//                            msg.obj = userType;
//                            msg.sendToTarget();
                        }
                        break;
                }
            }

            @Override
            public void onError(Platform platform, int action, int errorCode, Throwable error) {
                Logger.dd(TAG, "onError:" + platform + ",action:" + action + ",error:" + error);
                String toastMsg = null;
                switch (action) {
                    case Platform.ACTION_AUTHORIZING:
                        toastMsg = "授权失败";
                        break;
                    case Platform.ACTION_REMOVE_AUTHORIZING:
                        toastMsg = "删除授权失败";
                        break;
                    case Platform.ACTION_USER_INFO:
                        toastMsg = "获取个人信息失败";
                        break;
                }
                if (handler != null) {
                    Message msg = handler.obtainMessage(3);
                    msg.obj = toastMsg + (error != null ? error.getMessage() : "") + "---" + errorCode;
                    msg.sendToTarget();
                }
            }

            @Override
            public void onCancel(Platform platform, int action) {
                Logger.dd(TAG, "onCancel:" + platform + ",action:" + action);
                String toastMsg = null;
                switch (action) {
                    case Platform.ACTION_AUTHORIZING:
                        toastMsg = "取消授权";
                        break;
                    case Platform.ACTION_REMOVE_AUTHORIZING:
                        toastMsg = "取消删除授权";
                        break;
                    case Platform.ACTION_USER_INFO:
                        toastMsg = "取消获取个人信息";
                        break;
                }
                if (handler != null) {
                    Message msg = handler.obtainMessage(2);
                    msg.obj = toastMsg;
                    msg.sendToTarget();
                }
            }
        };
        switch (userType) {
            case UserType.USER_TYPE_WEIXIN:
                if (!JShareInterface.isAuthorize(Wechat.Name)) {
                    JShareInterface.authorize(Wechat.Name, mAuthListener);
//                    JShareInterface.getUserInfo(Wechat.Name, mAuthListener);
                } else {
                    JShareInterface.removeAuthorize(Wechat.Name, mAuthListener);
                }

                break;
            case UserType.USER_TYPE_QQ:
                if (!JShareInterface.isAuthorize(QQ.Name)) {
                    JShareInterface.authorize(QQ.Name, mAuthListener);
//                    JShareInterface.getUserInfo(QQ.Name, mAuthListener);
                } else {
                    JShareInterface.removeAuthorize(QQ.Name, mAuthListener);
                }
                break;
            case UserType.USER_TYPE_FB:
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                    ToastUtils.showToastLong(mContext, getResources().getString(R.string.third_platform));
                    return;
                }
                if (!JShareInterface.isAuthorize(Facebook.Name)) {
                    JShareInterface.authorize(Facebook.Name, mAuthListener);
                } else {
                    JShareInterface.removeAuthorize(Facebook.Name, mAuthListener);
                }
                break;
        }
    }

    /* 绑定解绑第三方账号 */
    private void bindThirdPlatform(final String otherAccount, String userType) {
        SCHttpUtils.postWithUserId()
                .url(HttpApi.BAND_THIRD_PLATFORM)
                .addParams("otherAccount", otherAccount)
                .addParams("userType", userType)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("网络异常");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("绑定结果 = " + response);
                        String code = SCJsonUtils.parseCode(response);
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            initData();
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (TextUtils.isEmpty(otherAccount)) {
                                        ToastUtils.showToast(AccountSecurityActivity.this, "解绑成功!");
                                    } else {
                                        ToastUtils.showToast(AccountSecurityActivity.this, "绑定成功!");
                                    }
                                }
                            });
                        } else {
                            ToastUtils.showToast(mContext, "绑定失败" + code);
                        }
                    }
                });
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    //弹窗提示账户注销
    private void isLogoutTip(){
        standardDialog = new StandardDialog(this);
        standardDialog.setStandardTitle(getString(R.string.logout_account));
        standardDialog.setStandardMsg(getString(R.string.logout_not_reverse));
        standardDialog.setSureText(getString(R.string.str_sure));
        standardDialog.setCallback(new Callback() {
            @Override
            public void invoke() {
                standardDialog.dismiss();
                //弹窗提示输入密码
                showEnterPassword();
            }
        }, new Callback() {
            @Override
            public void invoke() {
                standardDialog.dismiss();
            }
        });
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                standardDialog.show();
                TextView msgTextView = standardDialog.findViewById(R.id.dialog_msg);
                msgTextView.setTextSize(18);
            }
        });
    }

    //弹窗提示输入密码
    private void showEnterPassword(){
        mScInputDialog = new SCInputDialog(this, "输入登陆密码",
                "输入登陆密码");
        mScInputDialog.setCallback(new Callback() {//确定
            @Override
            public void invoke() {
                if(TextUtils.isEmpty(mScInputDialog.getEtContent().getText())){
                    ToastUtils.showToast(AccountSecurityActivity.this, "输入登陆密码");
                    return;
                }
                if(mScInputDialog.getEtContent().getText().length()>20){
                    ToastUtils.showToast(AccountSecurityActivity.this, R.string.mining_nam_exant_10);
                    return;
                }
                mScInputDialog.dismiss();

            }
        }, new Callback() {//取消
            @Override
            public void invoke() {

            }
        });
        //显示输入元社区弹窗
        mScInputDialog.show();
    }
}
