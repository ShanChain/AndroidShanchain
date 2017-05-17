package com.shanchain.mvp.view.activity;

import android.widget.LinearLayout;

import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.widgits.toolBar.ArthurToolBar;

import butterknife.Bind;

public class LoginActivity extends BaseActivity {


    @Bind(R.id.toolbar_login)
    ArthurToolBar mToolbarLogin;
    @Bind(R.id.activity_login)
    LinearLayout mActivityLogin;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewsAndEvents() {
        mToolbarLogin.setTitleText("登录");
        mToolbarLogin.setImmersive(this,true);
        mToolbarLogin.setBackgroundColor(getResources().getColor(R.color.colorTheme));
    }

}
