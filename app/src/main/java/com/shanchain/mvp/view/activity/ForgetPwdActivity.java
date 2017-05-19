package com.shanchain.mvp.view.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.widgits.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class ForgetPwdActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, View.OnFocusChangeListener {

    /** 描述：顶部工具栏*/
    @Bind(R.id.toolbar_forget)
    ArthurToolBar mToolbarForget;
    /** 描述：账号输入框*/
    @Bind(R.id.et_forget_account)
    EditText mEtForgetAccount;
    /** 描述：验证码输入框*/
    @Bind(R.id.et_forget_checkcode)
    EditText mEtForgetCheckcode;
    /** 描述：获取验证码按钮*/
    @Bind(R.id.btn_forget_checkcode)
    Button mBtnForgetCheckcode;
    /** 描述：下一步按钮*/
    @Bind(R.id.btn_forget_next)
    Button mBtnForgetNext;
    /** 描述：整体布局*/
    @Bind(R.id.activity_forget_pwd)
    LinearLayout mActivityForgetPwd;
    /** 描述：账号输入框提示文字*/
    private CharSequence mAccountHint;
    /** 描述：验证码输入框提示文字*/
    private CharSequence mCheckcodeHint;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initViewsAndEvents() {
        //初始化工具栏
        initToolBar();
        //初始化输入框
        initEditText();
    }




    @OnClick({R.id.btn_forget_checkcode, R.id.btn_forget_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_forget_checkcode:
                //获取验证码
                obtainCheckCode();
                break;
            case R.id.btn_forget_next:
                //下一步
                next();
                break;
        }
    }

    /**
     * 2017/5/17
     * 描述：初始化工具栏,沉浸式和按钮事件
     */
    private void initToolBar() {
        //设置沉浸式
        mToolbarForget.setImmersive(this, true);
        mToolbarForget.setBackgroundColor(getResources().getColor(R.color.colorTheme));

        //顶部工具栏左侧按钮点击事件
        mToolbarForget.setOnLeftClickListener(this);
    }

    /**
     *  2017/5/18
     *  描述：下一步
     *
     */
    private void next() {
        readyGo(ResetPwdActivity.class);
    }

    /**
     *  2017/5/18
     *  描述：获取验证码
     *
     */
    private void obtainCheckCode() {
    }

    /**
     *  2017/5/18
     *  描述：回退按钮的点击事件
     *
     */
    @Override
    public void onLeftClick(View v) {
        finish();
    }

    /**
     *  2017/5/18
     *  描述：初始化输入框，给输入框添加焦点监听事件
     *
     */
    private void initEditText() {
        mAccountHint = mEtForgetAccount.getHint();
        mCheckcodeHint = mEtForgetCheckcode.getHint();
        mEtForgetAccount.setOnFocusChangeListener(this);
        mEtForgetCheckcode.setOnFocusChangeListener(this);
    }

    /**
     *  2017/5/18
     *  描述：输入框焦点变化监听
     *
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.et_forget_account:
                changeHint(mEtForgetAccount,hasFocus,mAccountHint);
                break;
            case R.id.et_forget_checkcode:
                changeHint(mEtForgetCheckcode,hasFocus,mCheckcodeHint);
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
