package com.shanchain.shandata.mvp.view.activity.login;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.global.Constans;
import com.shanchain.shandata.global.GlobalVariable;
import com.shanchain.shandata.global.UserType;
import com.shanchain.shandata.http.HttpApi;
import com.shanchain.shandata.http.HttpUtils;
import com.shanchain.shandata.http.MyHttpCallBack;
import com.shanchain.shandata.mvp.Bean.ResponseLoginBean;
import com.shanchain.shandata.mvp.model.ThirdUserInfo;
import com.shanchain.shandata.mvp.view.activity.MainActivity;
import com.shanchain.shandata.utils.AccountUtils;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.PrefUtils;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.utils.encryption.AESUtils;
import com.shanchain.shandata.utils.encryption.Base64;
import com.shanchain.shandata.utils.encryption.MD5Utils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;
import me.shaohui.shareutil.LoginUtil;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;
import me.shaohui.shareutil.login.result.BaseToken;
import me.shaohui.shareutil.login.result.BaseUser;
import okhttp3.Call;

public class LoginActivity extends BaseActivity implements View.OnFocusChangeListener {

    private static final int OPERATION_LOGIN_RESET_PWD = 500;
    @Bind(R.id.toolbar_login)
    ArthurToolBar mToolbarLogin;
    @Bind(R.id.activity_login)
    LinearLayout mActivityLogin;
    @Bind(R.id.et_login_account)
    EditText mEtLoginAccount;
    @Bind(R.id.et_login_pwd)
    EditText mEtLoginPwd;
    @Bind(R.id.tv_login_forget)
    TextView mTvLoginForget;
    @Bind(R.id.btn_login_login)
    Button mBtnLoginLogin;
    @Bind(R.id.btn_login_regist)
    Button mBtnLoginRegist;
    @Bind(R.id.tv_other_login)
    TextView mTvOtherLogin;
    @Bind(R.id.iv_login_wechat)
    ImageView mIvLoginWechat;
    @Bind(R.id.iv_login_qq)
    ImageView mIvLoginQq;
    @Bind(R.id.iv_login_weibo)
    ImageView mIvLoginWeibo;
    @Bind(R.id.ll_login_other_login)
    LinearLayout mLlLoginOtherLogin;
    private boolean isHint = true;
    private CharSequence mAccountHint;
    private CharSequence mPwdHint;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewsAndEvents() {

        initTooBar();
        initEditText();
    }

    private void initTooBar() {
        mToolbarLogin.setBtnEnabled(false);
    }

    @OnClick({R.id.tv_login_forget, R.id.btn_login_login, R.id.btn_login_regist, R.id.tv_other_login, R.id.iv_login_wechat, R.id.iv_login_qq, R.id.iv_login_weibo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_forget:
                //忘记密码
                forget();
                break;
            case R.id.btn_login_login:
                //登陆
                login();
                break;
            case R.id.btn_login_regist:
                //注册按钮的点击事件
                regist();
                break;
            case R.id.tv_other_login:
                //其他社交账号登录
                otherLogin();
                break;
            case R.id.iv_login_wechat:
                ToastUtils.showToast(this, "微信登陆");
                LoginUtil.login(this, LoginPlatform.WX, listener, true);
                break;
            case R.id.iv_login_qq:
                ToastUtils.showToast(this, "qq登陆");
                LoginUtil.login(this, LoginPlatform.QQ, listener, true);
                break;
            case R.id.iv_login_weibo:
                ToastUtils.showToast(this, "微博登陆");
                LoginUtil.login(this, LoginPlatform.WEIBO, listener, false);
                break;
        }
    }

    /**
     * 描述：登陆按钮点击事件，登陆成功到主界面
     */
    private void login() {
        readyGo(MainActivity.class);
        final String account = mEtLoginAccount.getText().toString().trim();
        String pwd = mEtLoginPwd.getText().toString().trim();

        String time = String.valueOf(System.currentTimeMillis());

        //用户类型
        String userType;

        if (TextUtils.isEmpty(account)) {
            ToastUtils.showToast(this, "账号不能为空");
            return;
        } else {
            if (AccountUtils.isPhone(account)) {
                //是手机号码
                userType = UserType.USER_TYPE_MOBILE;

            } else if (AccountUtils.isEmail(account)) {
                //是邮箱账号
                userType = UserType.USER_TYPE_EMAIL;
            } else {
                //既不是手机也不是邮箱
                ToastUtils.showToast(this, "请输入正确的账号格式！");
                return;
            }
        }

        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showToast(this, "密码不能为空！");
            return;
        }

        //加密后的账号
        String encryptAccount = Base64.encode(AESUtils.encrypt(account, Base64.encode(userType + time)));
        //加密后的密码
        String md5Pwd = MD5Utils.md5(pwd);
        String passwordAccount = Base64.encode(AESUtils.encrypt(md5Pwd, Base64.encode(userType + time + account)));
        LogUtils.e("加密时间" + time);
        HttpUtils.postWithParamsForLogin()
                .url(HttpApi.USER_LOGIN)
                .addParams("Timestamp", time)
                .addParams("encryptAccount", encryptAccount)
                .addParams("encryptPassword", passwordAccount)
                .addParams("userType", userType)
                .build()
                .execute(new MyHttpCallBack<ResponseLoginBean>(ResponseLoginBean.class) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("登录失败!");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(ResponseLoginBean response, int id) {
                        if (response != null) {
                            String tokenData = response.getToken();
                            String nickName = response.getUserInfo().getNickName();
                            int userId = response.getUserInfo().getUserId();

                            LogUtils.d("token:" + tokenData + "nickName : " + nickName + "userId" + userId);
                            PrefUtils.putString(LoginActivity.this, "token", AESUtils.encrypt(tokenData, Constans.ENCRYPT_KEY));

                            GlobalVariable.token = tokenData;
                            readyGoThenKill(MainActivity.class);
                        }

                    }
                });
    }

    /**
     * 描述：忘记密码的点击事件
     */
    private void forget() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra("operationType", OPERATION_LOGIN_RESET_PWD);
        startActivity(intent);
    }

    /**
     * 描述：注册逻辑,弹出注册选择对话框
     */
    private void regist() {
        readyGo(PhoneRegistActivity.class);
    }

    /**
     * 描述：其他社交账号登录文本的点击事件,控制布局的显示和隐藏
     */
    private void otherLogin() {
        if (isHint) {
            showOtherLogin(true);
        } else {
            showOtherLogin(false);
        }
        isHint = !isHint;
    }

    /**
     * 描述：执行动画
     */
    private void showOtherLogin(boolean show) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mLlLoginOtherLogin, "translationY",
                show ? DensityUtils.dip2px(this, -85) : 0
        );
        animator.setDuration(500);
        animator.start();
    }


    /**
     * 描述：初始化输入框，给输入框添加焦点监听事件
     */
    private void initEditText() {
        mAccountHint = mEtLoginAccount.getHint();
        mPwdHint = mEtLoginPwd.getHint();
        mEtLoginAccount.setOnFocusChangeListener(this);
        mEtLoginPwd.setOnFocusChangeListener(this);
    }

    /**
     * 描述：输入框焦点变化监听
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_login_account:
                changeHint(mEtLoginAccount, hasFocus, mAccountHint);
                break;
            case R.id.et_login_pwd:
                changeHint(mEtLoginPwd, hasFocus, mPwdHint);
                break;
        }
    }


    /**
     * 描述：控制hint的显示与隐藏
     */
    private void changeHint(EditText et, boolean hasFocus, CharSequence hint) {
        if (hasFocus) {
            et.setHint("");
        } else {
            et.setHint(hint);
        }
    }

    final LoginListener listener = new LoginListener() {
        @Override
        public void loginSuccess(LoginResult result) {
            //登录成功， 如果你选择了获取用户信息，可以通过
            int platform = result.getPlatform();

            BaseToken token = result.getToken();
            String accessToken = token.getAccessToken();
            //String openid = token.getOpenid();
            BaseUser userInfo = result.getUserInfo();
            String nickname = userInfo.getNickname();
            String headImageUrl = userInfo.getHeadImageUrl();
            String openId = userInfo.getOpenId();
            String headImageUrlLarge = userInfo.getHeadImageUrlLarge();

            int sex = userInfo.getSex();
            switch (platform) {
                case LoginPlatform.QQ:

                    LogUtils.d("QQ登录成功 ！！！"
                            + "\r\n token = " + token
                            + ";\r\n >< " + "昵称=" + nickname
                            + ";\r\n 性别 = " + (sex == 1 ? "男" : "女")
                            + ";\r\n headImageUrl = " + headImageUrl
                            + ";\r\n openId = " + openId
                            + ";\r\n headImageUrlLarge = " + headImageUrlLarge
                            + ";\r\n accessToken = " + accessToken
                    );

                    ThirdUserInfo thirdUserInfo = new ThirdUserInfo();
                    thirdUserInfo.setHeadIcon(headImageUrlLarge);
                    thirdUserInfo.setNickName(nickname);
                    thirdUserInfo.setSex(sex);

                    String time = String.valueOf(System.currentTimeMillis());
                    //加密后的openid
                    String encryptOpenId = Base64.encode(AESUtils.encrypt(openId, Base64.encode(UserType.USER_TYPE_QQ + time)));
                    //加密后的accesstoken
                    LogUtils.d("accessToken : " + accessToken);
                    String accesToken = accessToken.substring(0, 16);
                    LogUtils.d("accesToken截取 : " + accesToken);
                    String encryptToken16 = Base64.encode(AESUtils.encrypt(MD5Utils.md5(accesToken), Base64.encode(UserType.USER_TYPE_QQ + time + openId)));

                    LogUtils.d("加密后openid：" + encryptOpenId);
                    LogUtils.d("加密后accesstoken：" + encryptToken16);
                    LogUtils.d("用户类型：" + UserType.USER_TYPE_QQ);

                    thridLogin(time, encryptOpenId, encryptToken16, headImageUrlLarge, nickname, sex == 1 ? "0" : "1", UserType.USER_TYPE_QQ);

                    break;
                case LoginPlatform.WX:

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
        HttpUtils.postWithParamsForLogin()
                .addParams("", time)
                .addParams("encryptOpenId", encryptOpenId)
                .addParams("encryptToken16", encryptToken16)
                .addParams("headIcon", headIcon)
                .addParams("nickName", nickName)
                .addParams("sex", sex)
                .addParams("userType", userType)
                .url(HttpApi.USER_THIRD_LOGIN)
                .build()
                .execute(new MyHttpCallBack<ResponseLoginBean>(ResponseLoginBean.class) {
                             @Override
                             public void onError(Call call, Exception e, int id) {
                                 LogUtils.e("三方登录创建账号失败");
                                 e.printStackTrace();
                             }

                             @Override
                             public void onResponse(ResponseLoginBean response, int id) {
                                 if (response != null) {
                                     String nickName1 = response.getUserInfo().getNickName();
                                     String token = response.getToken();

                                     GlobalVariable.token = token;
                                     PrefUtils.putString(LoginActivity.this, "token", AESUtils.encrypt(token, Constans.ENCRYPT_KEY));

                                     LogUtils.d("token   ===  " + token);
                                     LogUtils.d("nickName1 " + nickName1);
                                 }
                             }
                         }
                );
    }

}
