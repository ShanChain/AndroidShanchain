package com.shanchain.shandata.mvp.view.activity.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * create by zhoujian
 * date 2017/5/18
 * 描述：邀请码注册页面
 */
public class InviteCodeRegistActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, View.OnFocusChangeListener {


    /** 描述：顶部工具栏*/
    @Bind(R.id.toolbar_invite)
    ArthurToolBar mToolbarInvite;
    /** 描述：邀请码输入框*/
    @Bind(R.id.et_invite_code)
    EditText mEtInviteCode;
    /** 描述：昵称输入框*/
    @Bind(R.id.et_invite_nickname)
    EditText mEtInviteNickname;
    /** 描述：密码输入框*/
    @Bind(R.id.et_invite_pwd)
    EditText mEtInvitePwd;
    /** 描述：密码确认输入框*/
    @Bind(R.id.et_invite_pwdconfirm)
    EditText mEtInvitePwdconfirm;
    /** 描述：同意注册按钮*/
    @Bind(R.id.btn_invite_agree)
    Button mBtnInviteAgree;
    /** 描述：服务条款按钮*/
    @Bind(R.id.tv_invite_terms)
    TextView mTvInviteTerms;
    @Bind(R.id.activity_invite_code_regist)
    LinearLayout mActivityInviteCodeRegist;
    private CharSequence mCodeHint;
    private CharSequence mNicknameHint;
    private CharSequence mPwdHint;
    private CharSequence mPwdCinfirmHint;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_invite_code_regist;
    }

    @Override
    protected void initViewsAndEvents() {
        //初始化工具栏
        initToolBar();
        
        //初始化输入框监听
        initEditText();
    }


    @OnClick({R.id.btn_invite_agree, R.id.tv_invite_terms})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_invite_agree:
                //注册
                regist();
                break;
            case R.id.tv_invite_terms:
                //查看条款
                lookTerms();
                break;
        }
    }

    /**
     *  2017/5/18
     *  描述：查看条款
     *
     */
    private void lookTerms() {

    }

    /**
     *  2017/5/18
     *  描述：邀请码注册账号
     *
     */
    private void regist() {
    }

    /**
     * 2017/5/17
     * 描述：初始化工具栏,沉浸式和按钮事件
     */
    private void initToolBar() {
        //没图标时,右侧设置为不可用
        mToolbarInvite.setBtnEnabled(true,false);
        //设置沉浸式
        mToolbarInvite.setImmersive(this, true);
        mToolbarInvite.setBackgroundColor(getResources().getColor(R.color.colorTheme));

        //顶部工具栏左侧按钮点击事件
        mToolbarInvite.setOnLeftClickListener(this);
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
        mCodeHint = mEtInviteCode.getHint();
        mNicknameHint = mEtInviteNickname.getHint();
        mPwdHint = mEtInvitePwd.getHint();
        mPwdCinfirmHint = mEtInvitePwdconfirm.getHint();
        mEtInviteCode.setOnFocusChangeListener(this);
        mEtInviteNickname.setOnFocusChangeListener(this);
        mEtInvitePwd.setOnFocusChangeListener(this);
        mEtInvitePwdconfirm.setOnFocusChangeListener(this);
    }

    /**
     *  2017/5/18
     *  描述：输入框焦点变化监听
     *
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.et_invite_code:
                changeHint(mEtInviteCode,hasFocus,mCodeHint);
                break;
            case R.id.et_invite_nickname:
                changeHint(mEtInviteNickname,hasFocus,mNicknameHint);
                break;
            case R.id.et_invite_pwd:
                changeHint(mEtInvitePwd,hasFocus,mPwdHint);
                break;
            case R.id.et_invite_pwdconfirm:
                changeHint(mEtInvitePwdconfirm,hasFocus,mPwdCinfirmHint);
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
