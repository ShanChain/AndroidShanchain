package com.shanchain.arkspot.ui.view.activity.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.global.Constants;
import com.shanchain.arkspot.global.UserType;
import com.shanchain.arkspot.http.HttpApi;
import com.shanchain.arkspot.ui.model.LoginUserInfoBean;
import com.shanchain.arkspot.ui.model.ResponseLoginBean;
import com.shanchain.arkspot.ui.view.activity.MainActivity;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.cache.CommonCacheHelper;
import com.shanchain.data.common.utils.AccountUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.encryption.AESUtils;
import com.shanchain.data.common.utils.encryption.Base64;
import com.shanchain.data.common.utils.encryption.MD5Utils;
import com.shanchain.netrequest.SCHttpCallBack;
import com.shanchain.netrequest.SCHttpUtils;

import butterknife.Bind;
import butterknife.OnClick;
import me.shaohui.shareutil.LoginUtil;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;
import me.shaohui.shareutil.login.result.BaseToken;
import me.shaohui.shareutil.login.result.BaseUser;
import okhttp3.Call;


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

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewsAndEvents() {
        mTbLogin.setBtnEnabled(false);
        String userId = CommonCacheHelper.getInstance(mContext).getCache("0", Constants.CACHE_CUR_USER);
        /*String userInfo = CommonCacheHelper.getInstance(mContext).getCache(userId, Constants.CACHE_USER_INFO);
        String token = CommonCacheHelper.getInstance(mContext).getCache(userId, Constants.CACHE_TOKEN);
        LogUtils.d("token = " + token);
        LogUtils.d("userId = " + userId);
        LogUtils.d("userInfo = " + userInfo);
        LoginUserInfoBean loginUserInfoBean = new Gson().fromJson(userInfo, LoginUserInfoBean.class);*/
        if (!TextUtils.isEmpty(userId)){
            readyGo(MainActivity.class);
            finish();
        }
    }

    @OnClick({R.id.tv_login_forget, R.id.btn_login, R.id.btn_register, R.id.iv_login_wx, R.id.iv_login_wb, R.id.iv_login_qq})
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
                LoginUtil.login(this, LoginPlatform.WX, listener, true);
                break;
            case R.id.iv_login_wb:
                //微博登录
                LoginUtil.login(this, LoginPlatform.WEIBO, listener, true);
                break;
            case R.id.iv_login_qq:
                //qq登录
                LoginUtil.login(this, LoginPlatform.QQ, listener, true);
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

        if (!AccountUtils.isPhone(account)) {
            ToastUtils.showToast(this, "账号格式不正确！");
            return;
        }

        String time = String.valueOf(System.currentTimeMillis());
        //加密后的账号
        String encryptAccount = Base64.encode(AESUtils.encrypt(account, Base64.encode(UserType.USER_TYPE_MOBILE + time)));
        //加密后的密码
        String md5Pwd = MD5Utils.md5(pwd);
        String passwordAccount = Base64.encode(AESUtils.encrypt(md5Pwd, Base64.encode(UserType.USER_TYPE_MOBILE + time + account)));

        SCHttpUtils.postWithParamsForLogin()
                .url(HttpApi.USER_LOGIN)
                .addParams("Timestamp", time)
                .addParams("encryptAccount", encryptAccount)
                .addParams("encryptPassword", passwordAccount)
                .addParams("userType", UserType.USER_TYPE_MOBILE)
                .build()
                .execute(new SCHttpCallBack<ResponseLoginBean>(ResponseLoginBean.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showToast(mContext, "登录失败！");
                        LogUtils.e("登录失败!");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(ResponseLoginBean response, int id) {
                        if (response == null) {
                            ToastUtils.showToast(mContext, "登录失败！");
                        } else {
                            //登录成功 在此缓存用户数据
                            String token = response.getToken();
                            String account = response.getAccount();
                            LoginUserInfoBean userInfo = response.getUserInfo();
                            int userId = userInfo.getUserId();
                            CommonCacheHelper.getInstance(mContext).setCache("0", Constants.CACHE_CUR_USER, userId + "");
                            CommonCacheHelper.getInstance(mContext).setCache(userId + "", Constants.CACHE_USER_INFO, new Gson().toJson(userInfo));
                            CommonCacheHelper.getInstance(mContext).setCache(userId + "", Constants.CACHE_TOKEN, token);
                            readyGo(MainActivity.class);
                            finish();
                        }
                    }
                });


    }

    private void forgetPwd() {
        readyGo(ResetPwdActivity.class);
    }

    final LoginListener listener = new LoginListener() {
        @Override
        public void loginSuccess(LoginResult result) {
            //登录成功， 如果你选择了获取用户信息，可以通过
            int platform = result.getPlatform();

            BaseToken token = result.getToken();
            String accessToken = token.getAccessToken();
            BaseUser userInfo = result.getUserInfo();
            String nickname = userInfo.getNickname();
            String headImageUrl = userInfo.getHeadImageUrl();
            String openId = userInfo.getOpenId();
            String headImageUrlLarge = userInfo.getHeadImageUrlLarge();
            String userType = UserType.USER_TYPE_WEIXIN;
            int sex = userInfo.getSex();
            switch (platform) {
                case LoginPlatform.QQ:
                    userType = UserType.USER_TYPE_QQ;
                    LogUtils.d("QQ登录成功 ！！！"
                            + "\r\n token = " + token
                            + ";\r\n >< " + "昵称=" + nickname
                            + ";\r\n 性别 = " + (sex == 1 ? "男" : "女")
                            + ";\r\n headImageUrl = " + headImageUrl
                            + ";\r\n openId = " + openId
                            + ";\r\n headImageUrlLarge = " + headImageUrlLarge
                            + ";\r\n accessToken = " + accessToken
                    );


                    break;
                case LoginPlatform.WX:
                    userType = UserType.USER_TYPE_WEIXIN;
                    LogUtils.d("微信登录成功 ！！！"
                            + "\r\n token = " + token
                            + ";\r\n  " + "昵称=" + nickname
                            + ";\r\n 性别 = " + (sex == 1 ? "男" : "女")
                            + ";\r\n headImageUrl = " + headImageUrl
                            + ";\r\n openId = " + openId
                            + ";\r\n headImageUrlLarge = " + headImageUrlLarge
                            + ";\r\n accessToken = " + accessToken);

                    break;

                case LoginPlatform.WEIBO:
                    userType = UserType.USER_TYPE_WEIBO;
                    LogUtils.d("微博登录成功 ！！！"
                            + "\r\n token = " + token
                            + ";\r\n  " + "昵称=" + nickname
                            + ";\r\n 性别 = " + (sex == 1 ? "男" : "女")
                            + ";\r\n headImageUrl = " + headImageUrl
                            + ";\r\n openId = " + openId
                            + ";\r\n headImageUrlLarge = " + headImageUrlLarge
                            + ";\r\n accessToken = " + accessToken);
                    break;
            }

            String time = String.valueOf(System.currentTimeMillis());
            //加密后的openid
            String encryptOpenId = Base64.encode(AESUtils.encrypt(openId, Base64.encode(userType + time)));
            //加密后的accesstoken
            LogUtils.d("accessToken : " + accessToken);
            String accesToken = accessToken.substring(0, 16);
            LogUtils.d("accesToken截取 : " + accesToken);
            String encryptToken16 = Base64.encode(AESUtils.encrypt(MD5Utils.md5(accesToken), Base64.encode(userType + time + openId)));

            LogUtils.d("加密后openid：" + encryptOpenId);
            LogUtils.d("加密后accesstoken：" + encryptToken16);
            LogUtils.d("用户类型：" + UserType.USER_TYPE_QQ);

            thridLogin(time, encryptOpenId, encryptToken16, headImageUrlLarge, nickname, sex == 1 ? "0" : "1", userType);

        }

        @Override
        public void loginFailure(Exception e) {
            LogUtils.d("登录失败");
        }

        @Override
        public void loginCancel() {
            LogUtils.d("登录取消");
        }
    };

    /**
     * 三方登录注册信息到服务器
     */
    public void thridLogin(String time, String encryptOpenId, String encryptToken16, String headIcon, String nickName, String sex, String userType) {
        SCHttpUtils.postWithParamsForLogin()
                .addParams("", time)
                .addParams("encryptOpenId", encryptOpenId)
                .addParams("encryptToken16", encryptToken16)
                .addParams("headIcon", headIcon)
                .addParams("nickName", nickName)
                .addParams("sex", sex)
                .addParams("userType", userType)
                .url(HttpApi.USER_THIRD_LOGIN)
                .build()
                .execute(new SCHttpCallBack<ResponseLoginBean>(ResponseLoginBean.class) {
                             @Override
                             public void onError(Call call, Exception e, int id) {
                                 LogUtils.e("三方登录创建账号失败");
                                 e.printStackTrace();
                             }

                             @Override
                             public void onResponse(ResponseLoginBean response, int id) {
                                 if (response != null) {
                                     LogUtils.d("三方登录成功");
                                     String token = response.getToken();
                                     String account = response.getAccount();
                                     LoginUserInfoBean userInfo = response.getUserInfo();

                                     if (userInfo == null){

                                         return;
                                     }

                                     int userId = userInfo.getUserId();

                                     CommonCacheHelper.getInstance(mContext).setCache("0", Constants.CACHE_CUR_USER, userId + "");
                                     CommonCacheHelper.getInstance(mContext).setCache(userId + "", Constants.CACHE_USER_INFO, new Gson().toJson(userInfo));
                                     CommonCacheHelper.getInstance(mContext).setCache(userId + "", Constants.CACHE_TOKEN, token);
                                     readyGo(MainActivity.class);
                                     finish();
                                 } else {
                                     LogUtils.e("登录返回数据为空");
                                 }
                             }
                         }
                );
    }

}
