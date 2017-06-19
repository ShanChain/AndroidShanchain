package com.shanchain.mvp.view.activity.challenge;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;


public class RepeatSettingActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    private static final int RESULT_CODE = 10;
    ArthurToolBar mToolbarRepeatSet;
    @Bind(R.id.rb_repeat_set_sunday)
    RadioButton mRbRepeatSetSunday;
    @Bind(R.id.rl_repeat_set_sunday)
    RelativeLayout mRlRepeatSetSunday;
    @Bind(R.id.rb_repeat_set_monday)
    RadioButton mRbRepeatSetMonday;
    @Bind(R.id.rl_repeat_set_monday)
    RelativeLayout mRlRepeatSetMonday;
    @Bind(R.id.rb_repeat_set_tuesday)
    RadioButton mRbRepeatSetTuesday;
    @Bind(R.id.rl_repeat_set_tuesday)
    RelativeLayout mRlRepeatSetTuesday;
    @Bind(R.id.rb_repeat_set_wednesday)
    RadioButton mRbRepeatSetWednesday;
    @Bind(R.id.rl_repeat_set_wednesday)
    RelativeLayout mRlRepeatSetWednesday;
    @Bind(R.id.rb_repeat_set_thursday)
    RadioButton mRbRepeatSetThursday;
    @Bind(R.id.rl_repeat_set_thursday)
    RelativeLayout mRlRepeatSetThursday;
    @Bind(R.id.rb_repeat_set_friday)
    RadioButton mRbRepeatSetFriday;
    @Bind(R.id.rl_repeat_set_friday)
    RelativeLayout mRlRepeatSetFriday;
    @Bind(R.id.rb_repeat_set_saturday)
    RadioButton mRbRepeatSetSaturday;
    @Bind(R.id.rl_repeat_set_saturday)
    RelativeLayout mRlRepeatSetSaturday;
    @Bind(R.id.activity_repeat_setting)
    LinearLayout mActivityRepeatSetting;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_repeat_setting;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mToolbarRepeatSet = (ArthurToolBar) findViewById(R.id.toolbar_repeat_set);
        mToolbarRepeatSet.setBtnEnabled(true, false);
        mToolbarRepeatSet.setBtnVisibility(true, false);
        mToolbarRepeatSet.setOnLeftClickListener(this);
    }


    @OnClick({R.id.rb_repeat_set_sunday, R.id.rl_repeat_set_sunday, R.id.rb_repeat_set_monday, R.id.rl_repeat_set_monday, R.id.rb_repeat_set_tuesday, R.id.rl_repeat_set_tuesday, R.id.rb_repeat_set_wednesday, R.id.rl_repeat_set_wednesday, R.id.rb_repeat_set_thursday, R.id.rl_repeat_set_thursday, R.id.rb_repeat_set_friday, R.id.rl_repeat_set_friday, R.id.rb_repeat_set_saturday, R.id.rl_repeat_set_saturday})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rl_repeat_set_sunday:
                mRbRepeatSetSunday.setChecked(!mRbRepeatSetSunday.isChecked());
                break;
            case R.id.rl_repeat_set_monday:
                mRbRepeatSetMonday.setChecked(!mRbRepeatSetMonday.isChecked());
                break;
            case R.id.rl_repeat_set_tuesday:
                mRbRepeatSetTuesday.setChecked(!mRbRepeatSetTuesday.isChecked());
                break;
            case R.id.rl_repeat_set_wednesday:
                mRbRepeatSetWednesday.setChecked(!mRbRepeatSetWednesday.isChecked());
                break;
            case R.id.rl_repeat_set_thursday:
                mRbRepeatSetThursday.setChecked(!mRbRepeatSetThursday.isChecked());
                break;
            case R.id.rl_repeat_set_friday:
                mRbRepeatSetFriday.setChecked(!mRbRepeatSetFriday.isChecked());
                break;
            case R.id.rl_repeat_set_saturday:
                mRbRepeatSetSaturday.setChecked(!mRbRepeatSetSaturday.isChecked());
                break;
            case R.id.rb_repeat_set_sunday:
                mRbRepeatSetSunday.setChecked(!mRbRepeatSetSunday.isChecked());
                break;
            case R.id.rb_repeat_set_monday:
                mRbRepeatSetMonday.setChecked(!mRbRepeatSetMonday.isChecked());
                break;
            case R.id.rb_repeat_set_tuesday:
                mRbRepeatSetTuesday.setChecked(!mRbRepeatSetTuesday.isChecked());
                break;
            case R.id.rb_repeat_set_wednesday:
                mRbRepeatSetWednesday.setChecked(!mRbRepeatSetWednesday.isChecked());
                break;
            case R.id.rb_repeat_set_thursday:
                mRbRepeatSetThursday.setChecked(!mRbRepeatSetThursday.isChecked());
                break;
            case R.id.rb_repeat_set_friday:
                mRbRepeatSetFriday.setChecked(!mRbRepeatSetFriday.isChecked());
                break;
            case R.id.rb_repeat_set_saturday:
                mRbRepeatSetSaturday.setChecked(!mRbRepeatSetSaturday.isChecked());
                break;
        }
    }

    private ArrayList<String> weeks;

    @Override
    public void onLeftClick(View v) {
        getCheckedState();
        Intent intent = new Intent();
        intent.putExtra("repeatData",weeks);
        setResult(RESULT_CODE,intent);
        finish();
    }

    private void getCheckedState() {
        weeks = new ArrayList<>();
        if (mRbRepeatSetSunday.isChecked()) {
            weeks.add("周日");
        }
        if (mRbRepeatSetMonday.isChecked()) {
            weeks.add("周一");
        }
        if (mRbRepeatSetTuesday.isChecked()) {
            weeks.add("周二");
        }
        if (mRbRepeatSetWednesday.isChecked()) {
            weeks.add("周三");
        }
        if (mRbRepeatSetThursday.isChecked()) {
            weeks.add("周四");
        }
        if (mRbRepeatSetFriday.isChecked()) {
            weeks.add("周五");
        }
        if (mRbRepeatSetSaturday.isChecked()) {
            weeks.add("周六");
        }
    }
}
