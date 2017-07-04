package com.shanchain.shandata.mvp.view.activity.login;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class PhoneRegistActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, View.OnFocusChangeListener {


    /**
     * 描述：获取区域码结果的请求码
     */
    private static final int REQUESTCODE = 100;
    /**
     * 描述：顶部工具栏
     */
    @Bind(R.id.toolbar_login)
    ArthurToolBar mToolbarLogin;
    /**
     * 描述：区域码选择按钮
     */
    @Bind(R.id.tv_phone_regist_areacode)
    TextView mTvPhoneRegistAreacode;
    /**
     * 描述：账号输入框
     */
    @Bind(R.id.et_phone_regist_number)
    EditText mEtPhoneRegistNumber;
    /**
     * 描述：验证码输入框
     */
    @Bind(R.id.et_phone_regist_checkcode)
    EditText mEtPhoneRegistCheckcode;
    /**
     * 描述：获取验证码按钮
     */
    @Bind(R.id.btn_phone_regist_checkcode)
    Button mBtnPhoneRegistCheckcode;
    /**
     * 描述：昵称输入框
     */
    @Bind(R.id.et_phone_regist_nickname)
    EditText mEtPhoneRegistNickname;
    /**
     * 描述：密码输入框
     */
    @Bind(R.id.et_phone_regist_pwd)
    EditText mEtPhoneRegistPwd;
    /**
     * 描述：密码确认输入框
     */
    @Bind(R.id.et_phone_regist_pwdconfirm)
    EditText mEtPhoneRegistPwdconfirm;
    /**
     * 描述：同意注册按钮
     */
    @Bind(R.id.btn_phone_regist_agree)
    Button mBtnPhoneRegistAgree;
    /**
     * 描述：条款详情文本
     */
    @Bind(R.id.tv_phone_regist_terms)
    TextView mTvPhoneRegistTerms;
    /** 描述：整体布局*/
    @Bind(R.id.activity_phone_regist)
    LinearLayout mActivityPhoneRegist;
    /** 描述：账号输入框提示文字*/
    private CharSequence mNumHint;
    /** 描述：验证码输入框提示文字*/
    private CharSequence mCheckcodeHint;
    /** 描述：昵称输入框提示文字*/
    private CharSequence mNicknameHint;
    /** 描述：密码输入框提示文字*/
    private CharSequence mPwdHint;
    /** 描述：确认密码输入框提示文字*/
    private CharSequence mPwdConfirmHint;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_phone_regist;
    }

    @Override
    protected void initViewsAndEvents() {
        //初始化工具栏
        initToolBar();
        initEditText();
    }

    /**
     * 2017/5/18
     * 描述：初始化输入框,给输入框添加焦点监听事件
     */
    private void initEditText() {
        mNumHint = mEtPhoneRegistNumber.getHint();
        mCheckcodeHint = mEtPhoneRegistCheckcode.getHint();
        mNicknameHint = mEtPhoneRegistNickname.getHint();
        mPwdHint = mEtPhoneRegistPwd.getHint();
        mPwdConfirmHint = mEtPhoneRegistPwdconfirm.getHint();
        mEtPhoneRegistNumber.setOnFocusChangeListener(this);
        mEtPhoneRegistCheckcode.setOnFocusChangeListener(this);
        mEtPhoneRegistNickname.setOnFocusChangeListener(this);
        mEtPhoneRegistPwd.setOnFocusChangeListener(this);
        mEtPhoneRegistPwdconfirm.setOnFocusChangeListener(this);
    }

    /**
     * 2017/5/17
     * 描述：初始化工具栏,沉浸式和按钮事件
     */
    private void initToolBar() {

        //没图标时,左右侧设置为不可用
        mToolbarLogin.setBtnEnabled(true,false);
        //设置沉浸式
        mToolbarLogin.setImmersive(this, true);
        mToolbarLogin.setBackgroundColor(getResources().getColor(R.color.colorTheme));

        //顶部工具栏左侧按钮点击事件
        mToolbarLogin.setOnLeftClickListener(this);
    }

    @OnClick({R.id.tv_phone_regist_areacode, R.id.btn_phone_regist_checkcode, R.id.btn_phone_regist_agree, R.id.tv_phone_regist_terms})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_phone_regist_areacode:
                //选择区域码
                chooseAreaCode();
                break;
            case R.id.btn_phone_regist_checkcode:
                //获取验证码
                obtainCheckCode();
                break;
            case R.id.btn_phone_regist_agree:
                //同意注册
                agreeRegist();
                break;
            case R.id.tv_phone_regist_terms:
                //查看条款详情
                lookTerms();
                break;
        }
    }

    /**
     * 2017/5/18
     * 描述：查看条款
     */
    private void lookTerms() {

    }

    /**
     * 2017/5/18
     * 描述：同意注册
     */
    private void agreeRegist() {

    }

    /**
     * 2017/5/18
     * 描述：获取验证码
     */
    private void obtainCheckCode() {

    }

    /**
     * 2017/5/17
     * 描述：选择区域码的点击事件
     */
    private void chooseAreaCode() {
        readyGoForResult(ChooseAreaCodeActivity.class, REQUESTCODE);
    }

    /**
     * 2017/5/17
     * 描述：顶部工具栏左侧按钮点击事件
     */
    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != data) {
            String areacode = data.getExtras().getString("areacode");
            LogUtils.d(areacode);
            mTvPhoneRegistAreacode.setText(areacode);
        }

    }

    /**
     * 2017/5/18
     * 描述：输入框焦点变化监听
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_phone_regist_number:
                changeHint(mEtPhoneRegistNumber, hasFocus, mNumHint);
                break;
            case R.id.et_phone_regist_checkcode:
                changeHint(mEtPhoneRegistCheckcode, hasFocus, mCheckcodeHint);
                break;
            case R.id.et_phone_regist_nickname:
                changeHint(mEtPhoneRegistNickname, hasFocus, mNicknameHint);
                break;
            case R.id.et_phone_regist_pwd:
                changeHint(mEtPhoneRegistPwd, hasFocus, mPwdHint);
                break;
            case R.id.et_phone_regist_pwdconfirm:
                changeHint(mEtPhoneRegistPwdconfirm, hasFocus, mPwdConfirmHint);
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
