package com.shanchain.shandata.mvp.view.activity.challenge;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.global.Constans;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.CustomSeekBar;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.weigan.loopview.LoopView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.shanchain.shandata.R.id.tv_sleep_set_repeat;

public class SleepEarlierSettingActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {


    private static final int REPEAT_REQUEST_CODE = 1;
    private static final int TYPE_SLEEP_EARLIER = 2;
    ArthurToolBar mToolbarSleepEarlierSetting;
    @Bind(R.id.tv_sleep_set_rules)
    TextView mTvSleepSetRules;
    @Bind(R.id.lv_sleep_set_hours)
    LoopView mLvSleepSetHours;
    @Bind(R.id.lv_sleep_set_mins)
    LoopView mLvSleepSetMins;
    @Bind(tv_sleep_set_repeat)
    TextView mTvSleepSetRepeat;
    @Bind(R.id.csb_sleep_set)
    CustomSeekBar mCsbSleepSet;
    private List<String> mMinsData;
    private List<String> mHoursData;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_sleep_earlier_setting;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initData() {
        mMinsData = Arrays.asList(Constans.TIMES_MINS_HOURS);
        mHoursData = Arrays.asList(Constans.TIMES_HOURS_DAY);
        mLvSleepSetHours.setItems(mHoursData);
        mLvSleepSetMins.setItems(mMinsData);

    }

    private void initToolBar() {
        mToolbarSleepEarlierSetting = (ArthurToolBar) findViewById(R.id.toolbar_sleep_earlier_setting);
        mToolbarSleepEarlierSetting.setBtnEnabled(true);
        mToolbarSleepEarlierSetting.setBtnVisibility(true);
        mToolbarSleepEarlierSetting.setOnLeftClickListener(this);
        mToolbarSleepEarlierSetting.setOnRightClickListener(this);
    }


    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        String selectedMins = mMinsData.get(mLvSleepSetMins.getSelectedItem());
        String selectedHours = mHoursData.get(mLvSleepSetHours.getSelectedItem());
        int progress = mCsbSleepSet.getProgress();
        ToastUtils.showToast(this, selectedHours + "\r\n" + selectedMins + "\r\n" + progress);
        Intent intent = new Intent(this,ChallengeDynamicActivity.class);
        intent.putExtra("type",TYPE_SLEEP_EARLIER);
        startActivity(intent);
    }

    @OnClick({R.id.tv_sleep_set_rules, tv_sleep_set_repeat})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sleep_set_rules:
                CustomDialog customDialog = new CustomDialog(this, R.layout.dialog_sleep_rules, null);
                customDialog.show();
                break;
            case tv_sleep_set_repeat:

                Intent intent = new Intent(this, RepeatSettingActivity.class);
                startActivityForResult(intent, REPEAT_REQUEST_CODE);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REPEAT_REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> repeatData = data.getStringArrayListExtra("repeatData");
                if (repeatData.size() == 7) {
                    mTvSleepSetRepeat.setText("每天");
                    return;
                }
                StringBuffer sb = new StringBuffer();

                for (int i = 0; i < repeatData.size(); i++) {
                    if (i == repeatData.size() - 1) {
                        sb.append(repeatData.get(i));
                    } else {
                        sb.append(repeatData.get(i) + " ");
                    }
                }
                if (TextUtils.isEmpty(sb.toString())) {
                    mTvSleepSetRepeat.setText("永不");
                } else {
                    mTvSleepSetRepeat.setText(sb.toString());
                }

            }
        }

    }
}
