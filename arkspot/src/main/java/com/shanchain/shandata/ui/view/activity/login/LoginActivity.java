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
import com.shanchain.shandata.ui.model.RegisterHxBean;
import com.shanchain.shandata.ui.model.ResponseLoginBean;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintActivity;
import com.shanchain.shandata.utils.CountDownTimeUtils;
import com.shanchain.shandata.utils.KeyboardUtils;

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

    @Bind(R.id.tb_login)
    ArthurToolBar mTbLogin;
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
    @Bind(R.id.iv_login_wb)
    ImageView mIvLoginWb;
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
    @Bind(R.id.linear_dynamic_login)
    LinearLayout dynamicLogin;
    @Bind(R.id.iv_login_fb)
    ImageView ivLoginFb;
    @Bind(R.id.rl_login_wx)
    RelativeLayout rlLoginWx;
    @Bind(R.id.rl_login_wb)
    RelativeLayout rlLoginWb;
    @Bind(R.id.rl_login_fb)
    RelativeLayout rlLoginFb;
    @Bind(R.id.rl_login_qq)
    RelativeLayout rlLoginQq;
    @Bind(R.id.sp_phone_number)
    Spinner spPhoneNumber;
    @Bind(R.id.sp_phone_number_dn)
    Spinner spPhoneNumberDn;

    private ProgressDialog mDialog;
    private List<String> dataList = new ArrayList<String>();

    private String [] countrysAttr= new String[]{"+86(CHN)","+852(HK)","+65(SGP)","+44(UK)"};
    private String [] countryPhoneAttr = new String[]{"+86","+852","+65","+44"};
    private String aAcount = "";

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
        mTbLogin.setBtnEnabled(false);
        String RegistrationID = JPushInterface.getRegistrationID(this);
        LogUtils.d("JPushInterface", RegistrationID);
        channel = MyApplication.getAppMetaData(getApplicationContext(), "UMENG_CHANNEL");
        LogUtils.d("appChannel", channel);
        btnDynamicLogin.setClickable(false);
        btnDynamicLogin.setBackground(getResources().getDrawable(R.drawable.shape_btn_bg_send_unenable));

        addCountrysPhone();
    }

    private void checkCache() {
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
        if (!TextUtils.isEmpty(userId)) {
            LogUtils.e("当前用户id" + userId);
            String characterId = getCache(userId, Constants.CACHE_CHARACTER_ID);
            String characterInfo = getCache(userId, Constants.CACHE_CHARACTER_INFO);
            String spaceId = getCache(userId, Constants.CACHE_SPACE_ID);
            String spaceInfo = getCache(userId, Constants.CACHE_SPACE_INFO);
            String hxUserName = SCCacheUtils.getCacheHxUserName();
            String hxPwd = SCCacheUtils.getCacheHxPwd();
            String token = SCCacheUtils.getCacheToken();
//            if (TextUtils.isEmpty(characterId) || TextUtils.isEmpty(characterInfo) || TextUtils.isEmpty(spaceId) || TextUtils.isEmpty(spaceInfo) || TextUtils.isEmpty(hxUserName) || TextUtils.isEmpty(hxPwd) || TextUtils.isEmpty(token)) {
            if (TextUtils.isEmpty(characterId) || TextUtils.isEmpty(characterInfo) || TextUtils.isEmpty(hxUserName) || TextUtils.isEmpty(hxPwd) || TextUtils.isEmpty(token)) {
                checkServer();
            } else {
                closeProgress();
                CharacterInfo characterInfo1 = JSONObject.parseObject(characterInfo, CharacterInfo.class);
                registerJmUser(hxUserName, hxPwd);
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
                        ToastUtils.showToast(mContext, "网络异常");
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
                                    ToastUtils.showToast(LoginActivity.this, "登录失败，请检查用户名密码1");
                                    return;
                                }
                                String character = JSONObject.parseObject(data).getString("characterInfo");
                                RoleManager.switchRoleCacheCharacterInfo(character);
                                if (TextUtils.isEmpty(character)) {
                                    closeProgress();
                                    ToastUtils.showToast(LoginActivity.this, "登录失败，请检查用户名密码2");
                                    return;
                                } else {
                                    String isBindPwd = SCJsonUtils.parseString(character, "isBindPwd");
                                    String allowNotify = SCJsonUtils.parseString(character, "allowNotify");
//                                    if (allowNotify.equals("false")) {
//                                        JPushInterface.stopPush(getApplicationContext());
//                                    }
                                    CharacterInfo characterInfo = JSONObject.parseObject(character, CharacterInfo.class);
                                    if (characterInfo == null) {
                                        closeProgress();
                                        ToastUtils.showToast(LoginActivity.this, "登录失败，请检查用户名密码3");
                                    } else {
                                        String hxAccount = JSONObject.parseObject(data).getString("hxAccount");
                                        RegisterHxBean hxBean = JSONObject.parseObject(hxAccount, RegisterHxBean.class);
                                        //注册/登录 极光IM账号
                                        registerJmUser(hxBean.getHxUserName(), hxBean.getHxPassword());
//                                        loginJm(hxBean.getHxUserName(), hxBean.getHxPassword(),characterInfo);
                                        int spaceId = characterInfo.getSpaceId();
                                        int characterId = characterInfo.getCharacterId();
                                        String jmUser = JSONObject.parseObject(hxAccount).getString("hxUserName");
                                        String jmPassword = JSONObject.parseObject(hxAccount).getString("hxPassword");
                                        RoleManager.switchJmRoleCache(String.valueOf(characterId), jmUser, jmPassword);
                                    }
                                }

                            } else {
                                //code错误
                                closeProgress();
                                LogUtils.i("获取当前角色code错误");
                                ToastUtils.showToast(mContext, "网络异常");
                            }
                        } catch (Exception e) {
                            closeProgress();
                            ToastUtils.showToast(mContext, "网络异常");
                            LogUtils.i("获取当前角色信息数据解析错误");
                            e.printStackTrace();
                        }
                    }
                });

    }

    public void registerJmUser(final String jmUser, final String jmPassword) {
        boolean guided = PrefUtils.getBoolean(mContext, Constants.SP_KEY_GUIDE, false);
        //是否第一次打开app
        JPushInterface.init(getApplicationContext());
        String registrationID = JPushInterface.getRegistrationID(this);
        LogUtils.d("JPushInterface", registrationID);
        if (guided && !TextUtils.isEmpty(registrationID)) {
            //登录极光账号
//            showProgress();
            JMessageClient.login(jmUser, jmPassword, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    int i1 = i;
                    String s1 = s;
                    LogUtils.d("返回码: " + i + " message: " + s1);
                    if (i == 0) {
                        LogUtils.d("极光IM############## 登录成功 ##############极光IM");
                        closeProgress();
                        UserInfo userInfo = JMessageClient.getMyInfo();
                        LogUtils.d("极光账号: " + jmUser);
                        LogUtils.d("极光DisplayName: " + userInfo.getDisplayName());
                        LogUtils.d("极光Nickname: " + userInfo.getNickname());
                        LogUtils.d("极光UserID: " + userInfo.getUserID());
                        LogUtils.d("极光Signature: " + userInfo.getSignature());
                        CharacterInfo characterInfo = JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(), CharacterInfo.class);
                        if (userInfo != null && characterInfo != null) {
                            userInfo.setNickname(characterInfo.getName());
                            userInfo.setSignature(characterInfo.getSignature());
                            JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    String s1 = s;
                                    int i1 = i;
                                }
                            });

                            JMessageClient.updateMyInfo(UserInfo.Field.signature, userInfo, new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    String s1 = s;
                                    int i1 = i;
                                }
                            });

                        }

//                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        Intent intent = new Intent(LoginActivity.this, FootPrintActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        LogUtils.d("极光IM############## 登录失败 ##############极光IM");
//                        ToastUtils.showToastLong(LoginActivity.this, "登录失败，请检查用户名密码");
                        closeProgress();
                    }
                }
            });


        } else {
            //注册极光账号
            ThreadUtils.runOnSubThread(new Runnable() {
                @Override
                public void run() {
                    JMessageClient.register(jmUser, jmPassword, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0 || s.equals("Success")) {
                                LogUtils.d("极光IM############## 注册成功 ##############极光IM");
                                RoleManager.switchRoleCacheHx(jmUser, jmPassword);
                            } else {
                                LogUtils.d("极光IM############## 注册失败 ##############极光IM");
//                                ToastUtils.showToast(LoginActivity.this, "消息服务异常");

                            }

                        }
                    });
                }
            });
            startActivity(new Intent(mContext, GuideActivity.class));
            finish();
        }

    }

    private void loginJm(String hxUserName, String hxPwd, final CharacterInfo characterInfo) {
        final long startTime = System.currentTimeMillis();
        LogUtils.i("登录极光IM = 开始时间 = " + startTime);
        JMessageClient.login(hxUserName, hxPwd, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                String ss = s;
                if (s.equals("Success")) {
                    LogUtils.d("极光IM############## 登录成功 ##############极光IM");
                    UserInfo userInfo = JMessageClient.getMyInfo();
                    if (userInfo != null) {
                        userInfo.setNickname(characterInfo.getName());
                        userInfo.setSignature(characterInfo.getSignature());
                        JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                String s1 = s;
                                int i1 = i;
                            }
                        });

                        JMessageClient.updateMyInfo(UserInfo.Field.signature, userInfo, new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                String s1 = s;
                                int i1 = i;
                            }
                        });
                    }

                    Intent intent = new Intent(LoginActivity.this, FootPrintActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    LogUtils.d("极光IM############## 登录失败 ##############极光IM");
                    ToastUtils.showToastLong(LoginActivity.this, "登录失败，请检查用户名密码");

                }
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
        spPhoneNumberDn.setAdapter(spinnerAdapter);

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
        spPhoneNumberDn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aAcount = countryPhoneAttr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        aAcount = countryPhoneAttr[0];
    }

    @OnClick({R.id.tv_login_forget, R.id.btn_login, R.id.btn_register, R.id.iv_login_fb, R.id.rl_login_wx, R.id.rl_login_wb, R.id.rl_login_fb, R.id.rl_login_qq})
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
            case R.id.rl_login_wx:
                //微信登录
                thirdPlatform(UserType.USER_TYPE_WEIXIN);
//                LoginUtil.login(this, LoginPlatform.WX, listener, true);
                break;
            case R.id.rl_login_wb:
                //微博登录
                thirdPlatform(UserType.USER_TYPE_WEIBO);
//                LoginUtil.login(this, LoginPlatform.WEIBO, listener, true);
                break;
            case R.id.rl_login_fb:
                //facebook登录
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                    ToastUtils.showToastLong(LoginActivity.this, getResources().getString(R.string.third_platform));
                    return;
                } else {
                    thirdPlatform(UserType.USER_TYPE_FB);
                }
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

            case R.id.rl_login_qq:
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

    private void login() {
        String account = mEtLoginAccount.getText().toString().trim();
        String pwd = mEtLoginPwd.getText().toString().trim();

        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {
            ToastUtils.showToast(this, "账号或密码为空！");
            return;
        }
        /*if (!AccountUtils.isPhone(account)) {
            ToastUtils.showToast(this, "账号格式不正确！");
            return;
        }*/
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
                        ToastUtils.showToast(mContext, "网络错误");
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
                                ToastUtils.showToast(mContext, "账号或密码错误");
                                closeProgress();
                            } else {
                                ToastUtils.showToast(mContext, "网络错误");
                                closeProgress();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(mContext, "网络错误");
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
                        toastMsg = "取消授权";
                        break;
                    case Platform.ACTION_REMOVE_AUTHORIZING:
                        break;
                    case Platform.ACTION_USER_INFO:
                        toastMsg = "取消获取个人信息";
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
                                             ToastUtils.showToast(mContext, "网络异常");
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
                                                     ToastUtils.showToast(mContext, code + ":未知异常");
                                                 }
                                             });
                                         }
                                     } catch (final Exception e) {
                                         closeProgress();
                                         e.printStackTrace();
                                         ThreadUtils.runOnMainThread(new Runnable() {
                                             @Override
                                             public void run() {
                                                 ToastUtils.showToast(mContext, "未知异常：" + e.getMessage());
                                             }
                                         });
                                     }
                                 }
                             }
                    );
        } catch (Exception e) {
            e.printStackTrace();
            closeProgress();
            ToastUtils.showToast(mContext, "网络异常");
            LogUtils.i("网络异常");
        }
    }

    /*
     * 绑定获取验证码
     * */
    private void obtainCheckCode() {
        mobilePhone = etDynamicLoginAccount.getText().toString().trim();
        verifyCode = etDynamicLoginCode.getText().toString().trim();
        if (TextUtils.isEmpty(mobilePhone)) {
            ToastUtils.showToast(this, "请填写手机号码");
            return;
        } else {
            /*if (AccountUtils.isPhone(mobilePhone)) {
                getCheckCode(mobilePhone, verifyCode);
            } else {
                ToastUtils.showToast(this, "请输入正确格式的账号");
                return;
            }*/
            getCheckCode(mobilePhone, verifyCode);
        }

        CountDownTimeUtils countDownTimeUtils = new CountDownTimeUtils(tvRegisterCode, 60 * 1000, 1000);
        countDownTimeUtils.start();
    }

    //从后台获取验证码
    private void getCheckCode(String phone, final String verifyCode) {
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
                        ToastUtils.showToast(LoginActivity.this, "获取验证码失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            salt = JSONObject.parseObject(data).getString("salt");
                            timestamp = JSONObject.parseObject(data).getString("timestamp");
                            btnDynamicLogin.setClickable(true);
                            btnDynamicLogin.setBackground(getResources().getDrawable(R.drawable.shape_bg_btn_login));
                        }
                    }
                });

    }
    //动态验证码登录
    private void sureLogin(String mobilePhone, String sign, String verifyCode) {
        if(!"+86".equals(aAcount)){
            mobilePhone = aAcount.substring(1,aAcount.length())+mobilePhone;
        }
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
                            readyGo(FootPrintActivity.class);
                            finish();
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

    @OnClick({R.id.tv_dynamic_login, R.id.linear_normal_login, R.id.et_dynamic_login_account, R.id.tv_register_code, R.id.et_dynamic_login_code, R.id.tv_normal_login, R.id.btn_dynamic_login, R.id.linear_dynamic_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //密码账号登录
            case R.id.tv_normal_login:
                normalLogin.setVisibility(View.VISIBLE);
                dynamicLogin.setVisibility(View.GONE);
                aAcount = countryPhoneAttr[0];
                break;
            //动态密码登录
            case R.id.tv_dynamic_login:
                dynamicLogin.setVisibility(View.VISIBLE);
                normalLogin.setVisibility(View.GONE);
                aAcount = countryPhoneAttr[0];
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
                mobilePhone = etDynamicLoginAccount.getText().toString().trim();
                verifyCode = etDynamicLoginCode.getText().toString().trim();
                if (TextUtils.isEmpty(verifyCode) || TextUtils.isEmpty(mobilePhone)) {
                    ToastUtils.showToast(LoginActivity.this, "请输入手机号、验证码");
                    return;
                }
                sign = MD5Utils.getMD5(verifyCode + salt + timestamp);
                sureLogin(mobilePhone, sign, verifyCode);
                break;
            case R.id.linear_dynamic_login:
                break;
        }
    }

}
