package com.shanchain.mvp.view.activity;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.utils.LogUtils;
import com.shanchain.utils.ToastUtils;
import com.shanchain.widgits.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements View.OnFocusChangeListener {

    /** 描述：顶部工具栏*/
    @Bind(R.id.toolbar_login)
    ArthurToolBar mToolbarLogin;
    /** 描述：整体布局*/
    @Bind(R.id.activity_login)
    LinearLayout mActivityLogin;
    /** 描述：账号输入框*/
    @Bind(R.id.et_login_account)
    EditText mEtLoginAccount;
    /** 描述：密码输入框*/
    @Bind(R.id.et_login_pwd)
    EditText mEtLoginPwd;
    /** 描述：忘记密码点击按钮*/
    @Bind(R.id.tv_login_forget)
    TextView mTvLoginForget;
    /** 描述：登陆按钮*/
    @Bind(R.id.btn_login_login)
    Button mBtnLoginLogin;
    /** 描述：注册按钮*/
    @Bind(R.id.btn_login_regist)
    Button mBtnLoginRegist;
    /** 描述：其他注册方式按钮*/
    @Bind(R.id.tv_other_login)
    TextView mTvOtherLogin;
    /** 描述：微信第三方登陆*/
    @Bind(R.id.iv_login_wechat)
    ImageView mIvLoginWechat;
    /** 描述：qq第三方登陆*/
    @Bind(R.id.iv_login_qq)
    ImageView mIvLoginQq;
    /** 描述：微博第三方登陆*/
    @Bind(R.id.iv_login_weibo)
    ImageView mIvLoginWeibo;
    /** 描述：整体布局*/
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
     *  2017/5/18
     *  描述：初始化工具栏，设置沉浸式
     *
     */
    private void initTooBar() {
        mToolbarLogin.setTitleText("登录");

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
                LogUtils.d("其他账号登录");
                otherLogin();
                break;
            case R.id.iv_login_wechat:
                ToastUtils.showToast(this,"微信登陆");
                break;
            case R.id.iv_login_qq:
                ToastUtils.showToast(this,"qq登陆");
                break;
            case R.id.iv_login_weibo:
                ToastUtils.showToast(this,"微博登陆");
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
        readyGo(ForgetPwdActivity.class);
    }

    /**
     * 2017/5/17
     * 描述：注册逻辑,弹出注册选择对话框
     */
    private void regist() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_regist, null);
        Button btnRegistPhone = (Button) view.findViewById(R.id.btn_dialog_regist_phone);
        Button btnRegistInvite = (Button) view.findViewById(R.id.btn_dialog_regist_invite);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.show();
        btnRegistPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //手机邮箱注册
                readyGo(PhoneRegistActivity.class);
                dialog.dismiss();
            }
        });

        btnRegistInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //邀请码注册
                readyGo(InviteCodeRegistActivity.class);
                dialog.dismiss();
            }
        });


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
                show ? -220 : 0
        );
        animator.setDuration(500);
        animator.start();
    }


    /**
     *  2017/5/18
     *  描述：初始化输入框，给输入框添加焦点监听事件
     *
     */
    private void initEditText() {
        mAccountHint = mEtLoginAccount.getHint();
        mPwdHint = mEtLoginPwd.getHint();
        mEtLoginAccount.setOnFocusChangeListener(this);
        mEtLoginPwd.setOnFocusChangeListener(this);
    }

    /**
     *  2017/5/18
     *  描述：输入框焦点变化监听
     *
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.et_login_account:
                changeHint(mEtLoginAccount,hasFocus,mAccountHint);
                break;
            case R.id.et_login_pwd:
                changeHint(mEtLoginPwd,hasFocus,mPwdHint);
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


}
