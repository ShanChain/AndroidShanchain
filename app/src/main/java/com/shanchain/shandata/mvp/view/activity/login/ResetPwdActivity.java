package com.shanchain.shandata.mvp.view.activity.login;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class ResetPwdActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, View.OnFocusChangeListener {


    /** 描述：顶部工具栏*/
    @Bind(R.id.toolbar_reset)
    ArthurToolBar mToolbarReset;
    /** 描述：密码输入框*/
    @Bind(R.id.et_reset_pwd)
    EditText mEtResetPwd;
    /** 描述：密码确认输入框*/
    @Bind(R.id.et_reset_pwdconfirm)
    EditText mEtResetPwdconfirm;
    /** 描述：确认按钮*/
    @Bind(R.id.btn_reset_comfirm)
    Button mBtnResetComfirm;
    @Bind(R.id.activity_reset_pwd)
    LinearLayout mActivityResetPwd;
    /** 描述：取出的pwd提示值*/
    private CharSequence mPwdHint;
    /** 描述：取出的pwd确认的提示值*/
    private CharSequence mPwdConfirmHint;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initEditText();
    }


    @OnClick(R.id.btn_reset_comfirm)
    public void onClick() {
        Intent intent = new Intent(this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 2017/5/17
     * 描述：初始化工具栏,沉浸式和按钮事件
     */
    private void initToolBar() {
        //没图标时,左右侧设置为不可用
        mToolbarReset.setBtnEnabled(true,false);
        //设置沉浸式
        mToolbarReset.setImmersive(this, true);
        mToolbarReset.setBackgroundColor(getResources().getColor(R.color.colorTheme));

        //顶部工具栏左侧按钮点击事件
        mToolbarReset.setOnLeftClickListener(this);
    }

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
        mPwdHint = mEtResetPwd.getHint();
        mPwdConfirmHint = mEtResetPwdconfirm.getHint();

        mEtResetPwd.setOnFocusChangeListener(this);
        mEtResetPwdconfirm.setOnFocusChangeListener(this);
    }

    /**
     *  2017/5/18
     *  描述：输入框焦点变化监听
     *
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.et_reset_pwd:
                changeHint(mEtResetPwd,hasFocus,mPwdHint);
                break;
            case R.id.et_reset_pwdconfirm:
                changeHint(mEtResetPwdconfirm,hasFocus,mPwdConfirmHint);
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
