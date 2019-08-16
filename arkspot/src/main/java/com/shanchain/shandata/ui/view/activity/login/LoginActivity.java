package com.shanchain.shandata.ui.view.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.base.UserType;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.cache.SharedPreferencesUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.AccountUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.PrefUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.VersionUtils;
import com.shanchain.data.common.utils.encryption.AESUtils;
import com.shanchain.data.common.utils.encryption.Base64;
import com.shanchain.data.common.utils.encryption.MD5Utils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.rn.activity.SCWebViewActivity;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.LoginUserInfoBean;
import com.shanchain.shandata.ui.model.PhoneFrontBean;
import com.shanchain.shandata.ui.model.RegisterHxBean;
import com.shanchain.shandata.ui.model.ResponseLoginBean;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintNewActivity;
import com.shanchain.shandata.utils.CountDownTimeUtils;
import com.shanchain.shandata.utils.KeyboardUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
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
import cn.jiguang.share.weibo.SinaWeibo;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;

import static com.shanchain.data.common.cache.SCCacheUtils.getCache;

//import com.tencent.tauth.Tencent;
//import me.shaohui.shareutil.LoginUtil;
//import me.shaohui.shareutil.login.LoginListener;
//import me.shaohui.shareutil.login.LoginPlatform;
//import me.shaohui.shareutil.login.LoginResult;
//import me.shaohui.shareutil.login.result.BaseToken;
//import me.shaohui.shareutil.login.result.BaseUser;


public class LoginActivity extends BaseActivity {

    @Bind(R.id.et_login_account)
    EditText mEtLoginAccount;
    @Bind(R.id.et_login_pwd)
    EditText mEtLoginPwd;
    @Bind(R.id.tv_login_forget)
    TextView mTvLoginForget;
    @Bind(R.id.btn_login)
    Button mBtnLogin;
    @Bind(R.id.btn_register)
    Button mBtnRegister;
    @Bind(R.id.iv_login_wx)
    ImageView mIvLoginWx;
    @Bind(R.id.iv_login_qq)
    ImageView mIvLoginQq;
    @Bind(R.id.tv_dynamic_login)
    TextView tvDynamicLogin;
    @Bind(R.id.linear_normal_login)
    LinearLayout normalLogin;
    @Bind(R.id.et_dynamic_login_account)
    EditText etDynamicLoginAccount;
    @Bind(R.id.tv_register_code)
    TextView tvRegisterCode;
    @Bind(R.id.et_dynamic_login_code)
    EditText etDynamicLoginCode;
    @Bind(R.id.tv_normal_login)
    TextView tvNormalLogin;
    @Bind(R.id.btn_dynamic_login)
    Button btnDynamicLogin;
    @Bind(R.id.btn_dynamic_login_next)
    Button btnDynamicLoginNext;
    @Bind(R.id.linear_dynamic_login)
    LinearLayout dynamicLogin;
    @Bind(R.id.iv_login_fb)
    ImageView ivLoginFb;
    @Bind(R.id.sp_phone_number)
    Spinner spPhoneNumber;
    @Bind(R.id.sp_phone_number_dn)
    Spinner spPhoneNumberDn;
    @Bind(R.id.rl_enter_phone)
    RelativeLayout rlEnterPhone;
    @Bind(R.id.rl_code_item)
    RelativeLayout rlCodeItem;
    @Bind(R.id.tv_sms_tip)
    TextView tvSmsTip;
    @Bind(R.id.tv_phone_q_1)
    TextView tvPhoneQ1;
    @Bind(R.id.tv_phone_q_2)
    TextView tvPhoneQ2;

    private ProgressDialog mDialog;
    private List<String> dataList = new ArrayList<String>();

    private String aAcount = "+86";
    private CountDownTimeUtils countDownTimeUtils;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String userType = (String) msg.obj;
                encryptToken16 = SharedPreferencesUtils.getPreferences(LoginActivity.this, userType);
                thridLogin(encryptOpenId, encryptToken16, imageUrl, name, gender + "", userType);
            } else {
                String toastMsg = (String) msg.obj;
                Toast.makeText(LoginActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private AuthListener mAuthListener;
    private String accessToken, encryptOpenId, encryptToken16, name, imageUrl;
    private int gender;
    private long expiration;
    private String sign, verifyCode, mobilePhone;
    private String salt;
    private String timestamp;
    private String channel;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewsAndEvents() {
        String RegistrationID = JPushInterface.getRegistrationID(this);
        LogUtils.d("JPushInterface", RegistrationID);
        channel = MyApplication.getAppMetaData(getApplicationContext(), "UMENG_CHANNEL");
        LogUtils.d("appChannel", channel);
        btnDynamicLogin.setClickable(false);
//        btnDynamicLogin.setBackground(getResources().getDrawable(R.drawable.shape_btn_bg_send_unenable));



    }

    @Override
    public void onResume() {
        super.onResume();
        PhoneFrontActivity.setListener(new PhoneFrontActivity.PhoneFrontNumCallback() {
            @Override
            public void getPhoneData(PhoneFrontBean phoneFrontBean) {
                if(phoneFrontBean !=null){
                    if(phoneFrontBean.getSourceType() ==1){
                        tvPhoneQ1.setText(phoneFrontBean.getPhoneFront());
                    }else if(phoneFrontBean.getSourceType() ==2){
                        tvPhoneQ2.setText(phoneFrontBean.getPhoneFront());
                    }
                    aAcount = phoneFrontBean.getPhoneFront();
                }
            }
        });
    }

    private void checkCache() {
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
        if (!TextUtils.isEmpty(userId)) {
            LogUtils.e("当前用户id" + userId);
            String characterId = getCache(userId, Constants.CACHE_CHARACTER_ID);
            String characterInfo = getCache(userId, Constants.CACHE_CHARACTER_INFO);
            String hxUserName = SCCacheUtils.getCacheHxUserName();
            String hxPwd = SCCacheUtils.getCacheHxPwd();
            String token = SCCacheUtils.getCacheToken();
            if (TextUtils.isEmpty(characterId) || TextUtils.isEmpty(characterInfo) || TextUtils.isEmpty(hxUserName) || TextUtils.isEmpty(hxPwd) || TextUtils.isEmpty(token)) {
                checkServer();
            } else {
                //基本不会执行这里的逻辑
                closeProgress();
                CharacterInfo characterInfo1 = JSONObject.parseObject(characterInfo, CharacterInfo.class);
                registerJmUser(hxUserName, hxPwd,characterInfo1);
            }

        }
    }

    private void checkServer() {
        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHARACTER_GET_CURRENT)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeProgress();
                        ToastUtils.showToast(mContext, getString(R.string.network_wrong));
                        LogUtils.i("获取当前角色失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取当前角色成功 " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                if (TextUtils.isEmpty(data)) {
                                    closeProgress();
                                    ToastUtils.showToast(LoginActivity.this, R.string.login_failed);
                                    return;
                                }
                                String character = JSONObject.parseObject(data).getString("characterInfo");
                                if (TextUtils.isEmpty(character)) {
                                    closeProgress();
                                    ToastUtils.showToast(LoginActivity.this, R.string.login_failed);
                                    return;
                                } else {
                                    RoleManager.switchRoleCacheCharacterInfo(character);
                                    CharacterInfo characterInfo = JSONObject.parseObject(character, CharacterInfo.class);
                                    String hxAccount = JSONObject.parseObject(data).getString("hxAccount");
                                    RegisterHxBean hxBean = JSONObject.parseObject(hxAccount, RegisterHxBean.class);
                                    if(hxBean == null){
                                        closeProgress();
                                        ToastUtils.showToast(LoginActivity.this, R.string.login_failed);
                                        return;
                                    }
                                    int characterId = characterInfo.getCharacterId();
                                    String jmUser = hxBean.getHxUserName();
                                    String jmPassword = hxBean.getHxPassword();
                                    //缓存用户名和密码信息
                                    RoleManager.switchJmRoleCache(String.valueOf(characterId), jmUser, jmPassword);
                                    RoleManager.switchRoleCacheHx(jmUser, jmPassword);
                                    //注册/登录 极光IM账号
                                    registerJmUser(hxBean.getHxUserName(), hxBean.getHxPassword(),characterInfo);
                                }

                            } else {
                                //code错误
                                closeProgress();
                                LogUtils.i("获取当前角色code错误");
                                ToastUtils.showToast(mContext, R.string.network_wrong);
                            }
                        } catch (Exception e) {
                            closeProgress();
                            ToastUtils.showToast(mContext, R.string.network_wrong);
                            LogUtils.i("获取当前角色信息数据解析错误");
                            e.printStackTrace();
                        }
                    }
                });

    }

    public void registerJmUser(final String jmUser, final String jmPassword,CharacterInfo characterInfo) {
        boolean guided = PrefUtils.getBoolean(mContext, Constants.SP_KEY_GUIDE, false);
        //是否第一次打开app
        JPushInterface.init(getApplicationContext());
        String registrationID = JPushInterface.getRegistrationID(this);
        LogUtils.d("JPushInterface", registrationID);
        //第一次打开app
        if(guided){
            if(TextUtils.isEmpty(registrationID)){
                //注册极光账号
                loginJm(jmUser,jmPassword,characterInfo,1);
            }else {
                //登陆极光
                loginJm(jmUser,jmPassword,characterInfo,2);
            }
            startActivity(new Intent(mContext, GuideActivity.class));
            finish();
        }else {
            if(TextUtils.isEmpty(registrationID)){
                //注册极光账号
                loginJm(jmUser,jmPassword,characterInfo,1);
            }else {
                //登陆极光
                loginJm(jmUser,jmPassword,characterInfo,2);
            }
            startActivity(new Intent(mContext,FootPrintNewActivity.class));
            finish();
        }

    }
    //注册或者登陆极光
    private void loginJm(String hxUserName, String hxPwd, final CharacterInfo characterInfo,int type) {
        final long startTime = System.currentTimeMillis();
        LogUtils.i("登录极光IM = 开始时间 = " + startTime);
        if(type == 1){//注册极光
            JMessageClient.register(hxUserName, hxPwd, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i == 0 || s.equals("Success")) {
                        LogUtils.d("极光IM############## Login注册成功 ##############极光IM,i: "+i+",s: "+s);
                    } else {
                        LogUtils.d("极光IM############## Login注册失败 ##############极光IM,i: "+i+",s: "+s);
                    }
                }
            });
        }else {
            //登陆极光
            JMessageClient.login(hxUserName, hxPwd, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i == 0 || s.equals("Success")) {
                        LogUtils.d("极光IM############## Login登录成功 ##############极光IM,i: "+i+",s: "+s);
                        UserInfo userInfo = JMessageClient.getMyInfo();
                        if (userInfo != null) {
                            userInfo.setNickname(characterInfo.getName());
                            userInfo.setSignature(characterInfo.getSignature());
                            JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                }
                            });
                            JMessageClient.updateMyInfo(UserInfo.Field.signature, userInfo, new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                }
                            });
                        }
                    } else {
                        LogUtils.d("极光IM############## Login登录失败 ##############极光IM,i: "+i+",s: "+s);
//                        ToastUtils.showToastLong(LoginActivity.this, getString(R.string.login_failed));
                    }
                }
            });
        }
    }


    @OnClick({R.id.tv_login_forget, R.id.btn_login, R.id.btn_register, R.id.iv_login_fb, R.id.iv_login_wx, R.id.iv_login_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_forget:
                //忘记密码
                forgetPwd();
                break;
            case R.id.btn_login:
                //登录
                login();
                break;
            case R.id.btn_register:
                //注册
                readyGo(RegisterActivity.class);
                break;
            case R.id.iv_login_wx:
                //微信登录
                thirdPlatform(UserType.USER_TYPE_WEIXIN);
                break;
            case R.id.iv_login_fb:
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                    ToastUtils.showToastLong(LoginActivity.this, getResources().getString(R.string.third_platform));
                    return;
                } else {
                    thirdPlatform(UserType.USER_TYPE_FB);
                }
//                LoginUtil.login(this, LoginPlatform.WEIBO, listener, true);
                break;

            case R.id.iv_login_qq:
                //qq登录
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                    ToastUtils.showToastLong(LoginActivity.this, getResources().getString(R.string.third_platform));
                    return;
                } else {
                    thirdPlatform(UserType.USER_TYPE_QQ);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 动态密码登录获取验证码
     */
    @OnClick(R.id.btn_dynamic_login_next)
    void getSmscodeByDynamicLogin(){
        obtainCheckCode();
    }

    /**
     * 选择其他国家的手机号前缀
     */
    @OnClick(R.id.tv_phone_q_1)
    void getPhoneQ1(){
        startActivity(new Intent(this,PhoneFrontActivity.class).putExtra("type",1));
    }
    /**
     * 选择其他国家的手机号前缀
     */
    @OnClick(R.id.tv_phone_q_2)
    void getPhoneQ2(){
        startActivity(new Intent(this,PhoneFrontActivity.class).putExtra("type",2));
    }

    //账号密码登陆
    private void login() {
        String account = mEtLoginAccount.getText().toString().trim();
        String pwd = mEtLoginPwd.getText().toString().trim();

        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {
            ToastUtils.showToast(this, R.string.passw_or_account_entity);
            return;
        }
        //暂时处理下账号,如果是中国大陆号码，则不需要拼接前缀，其他的需要拼接前缀
        if(!"+86".equals(aAcount)){
            account = aAcount.substring(1,aAcount.length())+account;
        }

        String time = String.valueOf(System.currentTimeMillis());
        //加密后的账号
        String encryptAccount = Base64.encode(AESUtils.encrypt(account, Base64.encode(UserType.USER_TYPE_MOBILE + time)));
        //加密后的密码
        String md5Pwd = MD5Utils.md5(pwd);
        String passwordAccount = Base64.encode(AESUtils.encrypt(md5Pwd, Base64.encode(UserType.USER_TYPE_MOBILE + time + account)));
        KeyboardUtils.hideSoftInput(this);
        showProgress();
        final String localVersion = VersionUtils.getVersionName(mContext);
        SCHttpUtils.postWithParamsForLogin()
                .url(HttpApi.USER_LOGIN)
                .addParams("deviceToken", JPushInterface.getRegistrationID(this))
                .addParams("version", localVersion)
                .addParams("os", "android")
                .addParams("channel", "" + channel)
                .addParams("Timestamp", time)
                .addParams("encryptAccount", encryptAccount)
                .addParams("encryptPassword", passwordAccount)
                .addParams("userType", UserType.USER_TYPE_MOBILE)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("登录错误");
                        e.printStackTrace();
                        ToastUtils.showToast(mContext, getString(R.string.network_wrong));
                        closeProgress();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.d("登录返回数据 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                closeProgress();
                                //登录成功
                                String data = JSONObject.parseObject(response).getString("data");
                                ResponseLoginBean loginBean = JSONObject.parseObject(data, ResponseLoginBean.class);
                                //登录成功 在此缓存用户数据
                                String token = loginBean.getToken();
                                LoginUserInfoBean userInfo = loginBean.getUserInfo();
                                int userId = userInfo.getUserId();
                                LogUtils.d("登录成功  uid" + userId);
                                SCCacheUtils.setCache("0", Constants.CACHE_CUR_USER, userId + "");
                                SCCacheUtils.setCache(userId + "", Constants.CACHE_USER_INFO, new Gson().toJson(userInfo));
                                SCCacheUtils.setCache(userId + "", Constants.CACHE_TOKEN, userId + "_" + token);
                                if (getIntent() == null) {
                                    return;
                                } else {
                                    if (getIntent().getStringExtra("wallet") != null) {
                                        Intent intent = new Intent(mContext, SCWebViewActivity.class);
                                        JSONObject obj = new JSONObject();
                                        obj.put("url", HttpApi.SEAT_WALLET);
                                        obj.put("title", getResources().getString(R.string.nav_my_wallet));
                                        String webParams = obj.toJSONString();
                                        intent.putExtra("webParams", webParams);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                checkCache();
                            } else if (TextUtils.equals(code, NetErrCode.LOGIN_ERR_CODE)) {
                                ToastUtils.showToast(mContext, R.string.ac_ps_wrong);
                                closeProgress();
                            } else {
                                ToastUtils.showToast(mContext, getString(R.string.network_wrong));
                                closeProgress();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(mContext, getString(R.string.network_wrong));
                            closeProgress();
                        }
                    }
                });

    }

    private void forgetPwd() {
        readyGo(ResetPwdActivity.class);
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
                            String openId = ((AccessTokenInfo) data).getOpenid();//openid
                            //授权原始数据，开发者可自行处理
                            String originData = data.getOriginData();
//                            toastMsg = "授权成功:" + data.toString();
                            toastMsg = "授权成功";
                            LogUtils.d(TAG, "openid:" + openId + ",token:" + accessToken + ",expiration:" + expiration + ",refresh_token:" + refresh_token);
                            LogUtils.d(TAG, "originData:" + originData);
                            //加密后的accesstoken
                            LogUtils.d("accessToken : " + accessToken);
                            String accesToken = accessToken.substring(0, 16);
                            LogUtils.d("accesToken截取 : " + accesToken);
                            String time = String.valueOf(System.currentTimeMillis());
                            encryptToken16 = Base64.encode(AESUtils.encrypt(MD5Utils.md5(accesToken), Base64.encode(userType + time + openId)));
                            SharedPreferencesUtils.setPreferences(LoginActivity.this, userType, encryptToken16);
                            //加密后的openid
                            encryptOpenId = Base64.encode(AESUtils.encrypt(openId, Base64.encode(userType + time)));
                            LogUtils.d("加密后openid：" + encryptOpenId);
                            LogUtils.d("加密后accesstoken：" + encryptToken16);
                            LogUtils.d("用户类型：" + userType);
                        }
                        break;
                    case Platform.ACTION_REMOVE_AUTHORIZING:
                        toastMsg = "删除授权成功";
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
                        break;
                }
                if (handler != null) {
                    Message msg = handler.obtainMessage(1);
                    msg.obj = userType;
                    msg.sendToTarget();
                }
            }

            @Override
            public void onError(Platform platform, int action, int errorCode, Throwable error) {
                Logger.dd(TAG, "onError:" + platform + ",action:" + action + ",error:" + error);
                String toastMsg = null;
                switch (action) {
                    case Platform.ACTION_AUTHORIZING:
                        toastMsg = getString(R.string.ar_wrong);
                        break;
                    case Platform.ACTION_REMOVE_AUTHORIZING:
                        toastMsg = getString(R.string.delete_at_failed);
                        break;
                    case Platform.ACTION_USER_INFO:
                        toastMsg = getString(R.string.get_info_faile);
                        break;
                }
                if (handler != null) {
                    Message msg = handler.obtainMessage(2);
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
                        toastMsg = getString(R.string.deauthorization);
                        break;
                    case Platform.ACTION_REMOVE_AUTHORIZING:
                        break;
                    case Platform.ACTION_USER_INFO:
                        toastMsg = getString(R.string.get_info_faile);
                        break;
                }
                if (handler != null) {
                    Message msg = handler.obtainMessage(3);
                    msg.obj = toastMsg;
                    msg.sendToTarget();
                }
            }
        };

        switch (userType) {
            case UserType.USER_TYPE_WEIXIN:
                if (!JShareInterface.isAuthorize(Wechat.Name)) {
                    JShareInterface.authorize(Wechat.Name, mAuthListener);
                } else {
                    JShareInterface.getUserInfo(Wechat.Name, mAuthListener);
                }

                break;
            case UserType.USER_TYPE_QQ:
                if (!JShareInterface.isAuthorize(QQ.Name)) {
                    JShareInterface.authorize(QQ.Name, mAuthListener);
                } else {
                    JShareInterface.getUserInfo(QQ.Name, mAuthListener);
                }
                break;
            case UserType.USER_TYPE_WEIBO:
                if (!JShareInterface.isAuthorize(SinaWeibo.Name)) {
                    JShareInterface.authorize(SinaWeibo.Name, mAuthListener);
                } else {
                    JShareInterface.getUserInfo(SinaWeibo.Name, mAuthListener);
                }
                break;
            case UserType.USER_TYPE_FB:
                if (!JShareInterface.isAuthorize(Facebook.Name)) {
                    JShareInterface.authorize(Facebook.Name, mAuthListener);
                } else {
                    JShareInterface.getUserInfo(Facebook.Name, mAuthListener);
                }
                break;
        }
    }

    /**
     * 三方登录注册信息到服务器
     */
    public void thridLogin(final String encryptOpenId, String encryptToken16, String headIcon, String nickName, String sex, String userType) {
        showProgress();
        try {
            final String localVersion = VersionUtils.getVersionName(mContext);
            SCHttpUtils.postWithParamsForLogin()
                    .url(HttpApi.USER_THIRD_LOGIN)
                    .addParams("deviceToken", JPushInterface.getRegistrationID(this))
                    .addParams("version", localVersion)
                    .addParams("os", "android")
                    .addParams("channel", "" + MyApplication.getAppMetaData(getApplicationContext(), "UMENG_CHANNEL"))
                    .addParams("encryptOpenId", encryptOpenId + "")
                    .addParams("encryptToken16", encryptToken16 + "")
                    .addParams("headIcon", headIcon + "")
                    .addParams("nickName", nickName + "")
                    .addParams("sex", sex + "")
                    .addParams("userType", userType + "")
                    .build()
                    .execute(new SCHttpStringCallBack() {
                                 @Override
                                 public void onError(Call call, Exception e, int id) {
                                     closeProgress();
                                     LogUtils.d("三方登录创建账号失败");
                                     ThreadUtils.runOnMainThread(new Runnable() {
                                         @Override
                                         public void run() {
                                             ToastUtils.showToast(mContext, getString(R.string.network_wrong));
                                         }
                                     });

                                 }

                                 @Override
                                 public void onResponse(String response, int id) {
                                     try {
                                         LogUtils.i("第三方注册结果 = " + response);
                                         final String code = SCJsonUtils.parseCode(response);
                                         if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                             String data = SCJsonUtils.parseData(response);
                                             ResponseLoginBean responseLoginBean = JSONObject.parseObject(data, ResponseLoginBean.class);
                                             String token = responseLoginBean.getToken();
                                             LoginUserInfoBean userInfo = responseLoginBean.getUserInfo();
                                             int userId = userInfo.getUserId();
                                             LogUtils.d("登录成功  uid" + userId);
                                             SCCacheUtils.setCache("0", Constants.CACHE_CUR_USER, userId + "");
                                             SCCacheUtils.setCache(userId + "", Constants.CACHE_USER_INFO, new Gson().toJson(userInfo));
                                             SCCacheUtils.setCache(userId + "", Constants.CACHE_TOKEN, userId + "_" + token);
                                             closeProgress();
                                             checkCache();
                                         } else if (TextUtils.equals(code, NetErrCode.BIND_PONE_ERR_CODE)) {
                                             closeProgress();
                                             Intent intent = new Intent(LoginActivity.this, BindPhoneActivity.class);
                                             intent.putExtra("encryptOpenId", encryptOpenId);
                                             startActivity(intent);
                                             finish();
                                         } else {
                                             closeProgress();
                                             ThreadUtils.runOnMainThread(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     ToastUtils.showToast(mContext, code + ":"+getString(R.string.unknown_ex));
                                                 }
                                             });
                                         }
                                     } catch (final Exception e) {
                                         closeProgress();
                                         e.printStackTrace();
                                         ThreadUtils.runOnMainThread(new Runnable() {
                                             @Override
                                             public void run() {
                                                 ToastUtils.showToast(mContext, getString(R.string.unknown_ex)+":" + e.getMessage());
                                             }
                                         });
                                     }
                                 }
                             }
                    );
        } catch (Exception e) {
            e.printStackTrace();
            closeProgress();
            ToastUtils.showToast(mContext, getString(R.string.network_wrong));
            LogUtils.i("网络异常");
        }
    }

    /*
     * 动态密码登录获取验证码
     * */
    private void obtainCheckCode() {
        mobilePhone = etDynamicLoginAccount.getText().toString().trim();
        if (TextUtils.isEmpty(mobilePhone)) {
            ToastUtils.showToast(this, getString(R.string.str_hint_register_phone));
            return;
        } else {
            if ("+86".equals(aAcount)){
                if(AccountUtils.isPhone(mobilePhone)){
                    getCheckCode();
                }else {
                    ToastUtils.showToast(this, R.string.phone_right);
                    return;
                }
            }else {
                getCheckCode();
            }
        }

    }

    //从后台获取验证码
    private void getCheckCode() {
        if(!"+86".equals(aAcount)){
            mobilePhone = aAcount.substring(1,aAcount.length())+mobilePhone;
        }
        SCHttpUtils.postNoToken()
                .url(HttpApi.SMS_BIND_UNLOGIN_VERIFYCODE)
                .addParams("mobile", mobilePhone)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showToast(LoginActivity.this, R.string.get_code_failed);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            salt = JSONObject.parseObject(data).getString("salt");
                            timestamp = JSONObject.parseObject(data).getString("timestamp");
                            btnDynamicLogin.setClickable(true);
//                            btnDynamicLogin.setBackground(getResources().getDrawable(R.drawable.shape_bg_btn_login));
                            countDownTimeUtils = new CountDownTimeUtils(tvRegisterCode, 60 * 1000, 1000);
                            countDownTimeUtils.setContext(LoginActivity.this);
                            countDownTimeUtils.start();

                            //显示下一步输入验证码界面
                            rlEnterPhone.setVisibility(View.GONE);
                            rlCodeItem.setVisibility(View.VISIBLE);
                            tvSmsTip.setText(getString(R.string.enter_sms_coed_tip,phoneSecurityParse(aAcount,mobilePhone)));
                        }else{
                            ToastUtils.showToast(LoginActivity.this, R.string.get_code_failed);
                        }
                    }
                });

    }
    //截取手机号显示
    private String phoneSecurityParse(String front,String phone){
        if(TextUtils.isEmpty(front) || TextUtils.isEmpty(phone) || phone.length()<4){
            return "";
        }
        return front+"******"+phone.substring(phone.length()-4,(phone.length()));

    }

    //动态验证码登录
    private void sureLogin(String sign, String verifyCode) {
        /*if(!"+86".equals(aAcount)){
            mobilePhone = aAcount.substring(1,aAcount.length())+mobilePhone;
        }*/
        final String localVersion = VersionUtils.getVersionName(mContext);
        SCHttpUtils.postNoToken()
                .url(HttpApi.SMS_LOGIN)
                .addParams("channel", "" + MyApplication.getAppMetaData(getApplicationContext(), "UMENG_CHANNEL"))
                .addParams("mobile", mobilePhone)
                .addParams("sign", sign)
                .addParams("verifyCode", verifyCode)
                .addParams("version", localVersion)
                .build()
                .execute(new SCHttpStringCallBack(LoginActivity.this) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        LogUtils.d("dynamicLogin", e.toString());
//                        ToastUtils.showToast(LoginActivity.this, "网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        //登录成功
                        if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            ResponseLoginBean loginBean = JSONObject.parseObject(data, ResponseLoginBean.class);
                            //登录成功 在此缓存用户数据
                            String token = loginBean.getToken();
                            LoginUserInfoBean userInfo = loginBean.getUserInfo();
                            int userId = userInfo.getUserId();
                            LogUtils.d("登录成功  uid" + userId);

                            SCCacheUtils.setCache("0", Constants.CACHE_CUR_USER, userId + "");
                            SCCacheUtils.setCache(userId + "", Constants.CACHE_USER_INFO, new Gson().toJson(userInfo));
                            SCCacheUtils.setCache(userId + "", Constants.CACHE_TOKEN, userId + "_" + token);

                            checkServer();
                        }else {
                            ToastUtils.showToast(LoginActivity.this, R.string.sms_code_wrong);
                        }
                    }
                });
    }

    public void showProgress() {
        mDialog = new ProgressDialog(this);
        mDialog.setMax(100);
        mDialog.setMessage(getResources().getString(R.string.str_dialog_logging));
        mDialog.setCancelable(true);
        mDialog.show();
    }

    public void closeProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_dynamic_login, R.id.linear_normal_login, R.id.et_dynamic_login_account, R.id.tv_register_code, R.id.et_dynamic_login_code, R.id.tv_normal_login,R.id.tv_normal_login_next, R.id.btn_dynamic_login, R.id.linear_dynamic_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //密码账号登录
            case R.id.tv_normal_login:
            case R.id.tv_normal_login_next:
                normalLogin.setVisibility(View.VISIBLE);
                dynamicLogin.setVisibility(View.GONE);
                aAcount = "+86";
                tvPhoneQ1.setText(aAcount);
                etDynamicLoginAccount.setText("");
                etDynamicLoginCode.setText("");
                rlEnterPhone.setVisibility(View.VISIBLE);
                rlCodeItem.setVisibility(View.GONE);
                break;
            //动态密码登录
            case R.id.tv_dynamic_login:
                dynamicLogin.setVisibility(View.VISIBLE);
                normalLogin.setVisibility(View.GONE);
                aAcount = "+86";
                tvPhoneQ2.setText(aAcount);
                break;
            case R.id.linear_normal_login:
                break;
            case R.id.et_dynamic_login_account:
                break;
            case R.id.tv_register_code:
                obtainCheckCode();
                break;
            case R.id.et_dynamic_login_code:
                break;
            case R.id.btn_dynamic_login:
//                mobilePhone = etDynamicLoginAccount.getText().toString().trim();
                verifyCode = etDynamicLoginCode.getText().toString().trim();
                if (TextUtils.isEmpty(verifyCode)) {
                    ToastUtils.showToast(LoginActivity.this, R.string.str_register_code_input);
                    return;
                }
                sign = MD5Utils.getMD5(verifyCode + salt + timestamp);
                sureLogin(sign, verifyCode);
                break;
            case R.id.linear_dynamic_login:
                break;
        }
    }

}
