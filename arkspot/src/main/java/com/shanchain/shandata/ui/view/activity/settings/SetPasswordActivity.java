package com.shanchain.shandata.ui.view.activity.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetPasswordActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener{


    @Bind(R.id.tb_setting)
    ArthurToolBar tbSetting;
    @Bind(R.id.tv_set_password)
    TextView tvSetPassword;
    @Bind(R.id.edit_set_password)
    EditText editSetPassword;
    @Bind(R.id.tv_set_password_hint)
    TextView tvSetPasswordHint;
    @Bind(R.id.btn_set_password_sure)
    Button btnSetPasswordSure;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_set_password;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();

    }

    private void initToolBar() {
        ArthurToolBar arthurToolBar = findViewById(R.id.tb_setting);
        arthurToolBar.setTitleText("");
        arthurToolBar.setOnLeftClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_set_password_sure)
    public void onViewClicked() {
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
