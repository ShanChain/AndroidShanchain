package com.shanchain.shandata.ui.view.activity.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePhoneNumActivity extends AppCompatActivity implements ArthurToolBar.OnLeftClickListener{

    @Bind(R.id.tb_setting)
    ArthurToolBar tbSetting;
    @Bind(R.id.tv_set_password)
    TextView tvSetPassword;
    @Bind(R.id.tv_change_phone_hint)
    TextView tvChangePhoneHint;
    @Bind(R.id.tv_change_phone)
    TextView tvChangePhone;
    @Bind(R.id.change_password)
    RelativeLayout changePassword;
    @Bind(R.id.tv_register_code)
    TextView tvRegisterCode;
    @Bind(R.id.edit_set_phone)
    RelativeLayout editSetPhone;
    @Bind(R.id.tv_set_password_hint)
    TextView tvSetPasswordHint;
    @Bind(R.id.tv_select_num)
    TextView tvSelectNum;
    @Bind(R.id.edit_phone_num)
    EditText editPhoneNum;
    @Bind(R.id.next_step)
    RelativeLayout nextStep;
    @Bind(R.id.btn_set_password_sure)
    Button btnSetPasswordSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_num);
        ButterKnife.bind(this);
        tbSetting.setTitleText("");
        tbSetting.setOnLeftClickListener(this);
    }

    @OnClick({R.id.tv_register_code, R.id.btn_set_password_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_register_code:
                break;
            case R.id.btn_set_password_sure:
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
