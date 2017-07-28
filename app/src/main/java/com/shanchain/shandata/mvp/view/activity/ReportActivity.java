package com.shanchain.shandata.mvp.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.SystemUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class ReportActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    ArthurToolBar mToolbarReport;
    @Bind(R.id.rb_pornographic)
    RadioButton mRbPornographic;
    @Bind(R.id.rb_harmful_info)
    RadioButton mRbHarmfulInfo;
    @Bind(R.id.rb_sham_content)
    RadioButton mRbShamContent;
    @Bind(R.id.rb_personal_attack)
    RadioButton mRbPersonalAttack;
    @Bind(R.id.rb_break_law)
    RadioButton mRbBreakLaw;
    @Bind(R.id.rb_other)
    RadioButton mRbOther;
    @Bind(R.id.activity_report)
    LinearLayout mActivityReport;
    @Bind(R.id.btn_report)
    Button mBtnReport;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_report;
    }

    @Override
    protected void initViewsAndEvents() {

        String brand = Build.BRAND;
        LogUtils.d("当前版本为:"+brand);
        SystemUtils.MIUISetStatusBarLightMode(getWindow(),true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //6.0以上
            SystemUtils.setImmersiveStatusBar_API21(this, Color.WHITE);
            SystemUtils.setStatusBarLightMode_API23(this);
            if (brand.contains("Xiaomi")){
                SystemUtils.MIUISetStatusBarLightMode(getWindow(),true);
            }
        }

       // if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.)

        initToolBar();

//        Intent intent = getIntent();
//        PublisherInfo publishInfo = (PublisherInfo) intent.getSerializableExtra("publishInfo");
//        int comments = publishInfo.getComments();


    }

    private void initToolBar() {
        mToolbarReport = (ArthurToolBar) findViewById(R.id.toolbar_report);
        mToolbarReport.setOnLeftClickListener(this);
        mToolbarReport.setBtnEnabled(true,false);
    }


    @OnClick({R.id.rb_pornographic, R.id.rb_harmful_info, R.id.rb_sham_content, R.id.rb_personal_attack, R.id.rb_break_law, R.id.rb_other,R.id.btn_report})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_pornographic:
                setRadioButtonChecked(0);
                break;
            case R.id.rb_harmful_info:
                setRadioButtonChecked(1);
                break;
            case R.id.rb_sham_content:
                setRadioButtonChecked(2);
                break;
            case R.id.rb_personal_attack:
                setRadioButtonChecked(3);
                break;
            case R.id.rb_break_law:
                setRadioButtonChecked(4);
                break;
            case R.id.rb_other:
                setRadioButtonChecked(5);
                break;
            case R.id.btn_report:
                finish();
                break;

        }
    }

    boolean[] isChecked = {false, false, false, false, false, false};

    private void setRadioButtonChecked(int position) {
        for (int i = 0; i < isChecked.length; i++) {
            if (i == position) {
                isChecked[i] = true;
            } else {
                isChecked[i] = false;
            }
        }
        mRbPornographic.setChecked(isChecked[0]);
        mRbHarmfulInfo.setChecked(isChecked[1]);
        mRbShamContent.setChecked(isChecked[2]);
        mRbPersonalAttack.setChecked(isChecked[3]);
        mRbBreakLaw.setChecked(isChecked[4]);
        mRbOther.setChecked(isChecked[5]);
        mBtnReport.setTextColor(getResources().getColor(R.color.colorWhite));
        mBtnReport.setEnabled(true);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
