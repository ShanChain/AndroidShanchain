package com.shanchain.shandata.mvp.view.activity.login;

import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.view.activity.MainActivity;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements View.OnFocusChangeListener {

    private static final String WX_APPID = "wx0c49828919e7fd03";    //申请的wx appid
    private static final String QQ_APPID = "1106258018";    //申请的qq appid
    private static final String SINA_WB_APPKEY = "2099719405";       //申请的新浪微博 appkey

    /**
     * 描述：顶部工具栏
     */
    @Bind(R.id.toolbar_login)
    ArthurToolBar mToolbarLogin;
    /**
     * 描述：整体布局
     */
    @Bind(R.id.activity_login)
    LinearLayout mActivityLogin;
    /**
     * 描述：账号输入框
     */
    @Bind(R.id.et_login_account)
    EditText mEtLoginAccount;
    /**
     * 描述：密码输入框
     */
    @Bind(R.id.et_login_pwd)
    EditText mEtLoginPwd;
    /**
     * 描述：忘记密码点击按钮
     */
    @Bind(R.id.tv_login_forget)
    TextView mTvLoginForget;
    /**
     * 描述：登陆按钮
     */
    @Bind(R.id.btn_login_login)
    Button mBtnLoginLogin;
    /**
     * 描述：注册按钮
     */
    @Bind(R.id.btn_login_regist)
    Button mBtnLoginRegist;
    /**
     * 描述：其他注册方式按钮
     */
    @Bind(R.id.tv_other_login)
    TextView mTvOtherLogin;
    /**
     * 描述：微信第三方登陆
     */
    @Bind(R.id.iv_login_wechat)
    ImageView mIvLoginWechat;
    /**
     * 描述：qq第三方登陆
     */
    @Bind(R.id.iv_login_qq)
    ImageView mIvLoginQq;
    /**
     * 描述：微博第三方登陆
     */
    @Bind(R.id.iv_login_weibo)
    ImageView mIvLoginWeibo;
    /**
     * 描述：整体布局
     */
    @Bind(R.id.ll_login_other_login)
    LinearLayout mLlLoginOtherLogin;
    /**
     * 描述：其他账号登录是否隐藏 默认为隐藏
     */
    private boolean isHint = true;
    private CharSequence mAccountHint;
    private CharSequence mPwdHint;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewsAndEvents() {

        //初始化工具栏
        initTooBar();
        //初始化输入框
        initEditText();
    }

    /**
     * 2017/5/18
     * 描述：初始化工具栏，设置沉浸式
     */
    private void initTooBar() {
        mToolbarLogin.setTitleText("登录");
        //没图标时,左右侧设置为不可用
        mToolbarLogin.setBtnEnabled(false);

        //设置沉浸式
        mToolbarLogin.setImmersive(this, true);
        mToolbarLogin.setBackgroundColor(getResources().getColor(R.color.colorTheme));
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
//                LoginUtil.login(this, LoginPlatform.WX, listener,true);
                break;
            case R.id.iv_login_qq:
                ToastUtils.showToast(this, "qq登陆");
//                LoginUtil.login(this, LoginPlatform.QQ, listener,true);
                break;
            case R.id.iv_login_weibo:
                ToastUtils.showToast(this, "微博登陆");
//                LoginUtil.login(this, LoginPlatform.WEIBO, listener,true);
                break;
        }
    }

    /**
     * 2017/5/18
     * 描述：登陆按钮点击事件，登陆成功到主界面
     */
    private void login() {
        readyGoThenKill(MainActivity.class);
    }

    /**
     * 2017/5/18
     * 描述：忘记密码的点击事件
     */
    private void forget() {
        readyGo(ResetPasswordActivity.class);
    }

    /**
     * 2017/5/17
     * 描述：注册逻辑,弹出注册选择对话框
     */
    private void regist() {
        readyGo(PhoneRegistActivity.class);
    }

    /**
     * 2017/5/17
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
     * 2017/5/17
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
     * 2017/5/18
     * 描述：初始化输入框，给输入框添加焦点监听事件
     */
    private void initEditText() {
        mAccountHint = mEtLoginAccount.getHint();
        mPwdHint = mEtLoginPwd.getHint();
        mEtLoginAccount.setOnFocusChangeListener(this);
        mEtLoginPwd.setOnFocusChangeListener(this);
    }

    /**
     * 2017/5/18
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
     * 2017/5/18
     * 描述：控制hint的显示与隐藏
     */
    private void changeHint(EditText et, boolean hasFocus, CharSequence hint) {
        if (hasFocus) {
            et.setHint("");
        } else {
            et.setHint(hint);
        }
    }
   /* final LoginListener listener = new LoginListener() {
        @Override
        public void loginSuccess(LoginResult result) {
            //登录成功， 如果你选择了获取用户信息，可以通过

            LogUtils.d("登录","success");


            int platform = result.getPlatform();
            BaseToken token = result.getToken();
            BaseUser userInfo = result.getUserInfo();
            String nickname = userInfo.getNickname();
            int sex = userInfo.getSex();
            switch (platform) {
                case LoginPlatform.QQ:


                    LogUtils.d("QQ登录成功 ！！！" + "token = " + token + " >< " + "昵称=" + nickname + ";性别 = " + (sex==1?"男":"女"));

                    break;
                case LoginPlatform.WX:

                    LogUtils.d("微信登录成功 ！！！" + "token = " + token + " >< " + "昵称=" + nickname + ";性别 = " + (sex==1?"男":"女"));

                    break;
                case LoginPlatform.WEIBO:
                    LogUtils.d("微博登录成功 ！！！" + "token = " + token + " >< " + "昵称=" + nickname + ";性别 = " + (sex==1?"男":"女"));
                    break;
            }

            LogUtils.d("登录"," platform ="+platform+" ！！！" + "token = " + token + " >< " + "昵称=" + nickname + ";性别 = " + (sex==1?"男":"女"));

        }

        @Override
        public void loginFailure(Exception e) {
            Log.i("TAG", "登录失败:"  );
        }

        @Override
        public void loginCancel() {
            Log.i("TAG", "登录取消");
        }
    };*/

}
