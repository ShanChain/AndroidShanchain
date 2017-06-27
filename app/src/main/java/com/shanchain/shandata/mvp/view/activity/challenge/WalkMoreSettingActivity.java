package com.shanchain.shandata.mvp.view.activity.challenge;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.Bind;

public class WalkMoreSettingActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener, ArthurToolBar.OnLeftClickListener, View.OnClickListener {


    private static final int TYPE_WALKMORE = 1;
    ArthurToolBar mToolbarWalkMoreSetting;
    @Bind(R.id.tv_walk_set_rules)
    TextView mTvWalkSetRules;
    @Bind(R.id.lv_walk_set_start_data)
    LoopView mLvWalkSetStartData;
    @Bind(R.id.lv_walk_set_start_time)
    LoopView mLvWalkSetStartTime;
    @Bind(R.id.lv_walk_set_end_data)
    LoopView mLvWalkSetEndData;
    @Bind(R.id.lv_walk_set_end_time)
    LoopView mLvWalkSetEndTime;
    /*   @Bind(R.id.bsb_walk_set)
       BubbleSeekBar mBsbWalkSet;*/
    @Bind(R.id.activity_walk_more_setting)
    LinearLayout mActivityWalkMoreSetting;
    @Bind(R.id.csb_walk_more_set)
    CustomSeekBar mCsbWalkMoreSet;
    private ArrayList<String> mDates;
    private List<String> mTimes;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_walk_more_setting;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        setData();
        initListener();
    }

    private void initListener() {
        mTvWalkSetRules.setOnClickListener(this);
    }

    private void setData() {
        mLvWalkSetStartData.setItems(mDates);
        mLvWalkSetEndData.setItems(mDates);
        mLvWalkSetStartTime.setItems(mTimes);
        mLvWalkSetEndTime.setItems(mTimes);
        mLvWalkSetStartData.setInitPosition(0);
        mLvWalkSetEndData.setInitPosition(0);
    }

    private void initData() {

        mDates = new ArrayList<>();

        int[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        GregorianCalendar gc = new GregorianCalendar();
        mDates.add("今天");
        for (int i = 0; i < 30; i++) {
            gc.add(Calendar.DAY_OF_MONTH, 1);
            Date date = gc.getTime();
            int month = date.getMonth();
            int realMonth = months[month];
            int day = date.getDate();
            mDates.add(realMonth + "月" + day + "日");
        }


        mTimes = new ArrayList<>();
        mTimes = Arrays.asList(Constans.TIMES_MINS_DAY);

    }

    private void initToolBar() {
        mToolbarWalkMoreSetting = (ArthurToolBar) findViewById(R.id.toolbar_walk_more_setting);
        mToolbarWalkMoreSetting.setBtnEnabled(true);
        mToolbarWalkMoreSetting.setBtnVisibility(true);
        mToolbarWalkMoreSetting.setOnLeftClickListener(this);
        mToolbarWalkMoreSetting.setOnRightClickListener(this);
    }


    @Override
    public void onRightClick(View v) {
        int confidence = mCsbWalkMoreSet.getProgress();
        String startDate = mDates.get(mLvWalkSetStartData.getSelectedItem());
        String startTime = mTimes.get(mLvWalkSetStartTime.getSelectedItem());
        String endDate = mDates.get(mLvWalkSetEndData.getSelectedItem());
        String endTime = mTimes.get(mLvWalkSetEndTime.getSelectedItem());
        Intent intent = new Intent(this, ChallengeDynamicActivity.class);
        intent.putExtra("type", TYPE_WALKMORE);

        ToastUtils.showToast(this, "开始时间：" + startDate + " " + startTime + "\r\n" + "结束时间：" + endDate + " " + endTime + "\r\n" + "信心:" + confidence);

        startActivity(intent);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_walk_set_rules:
                CustomDialog customDialog = new CustomDialog(this, R.layout.dialog_walk_more, null);
                customDialog.show();
                break;
        }
    }
}
