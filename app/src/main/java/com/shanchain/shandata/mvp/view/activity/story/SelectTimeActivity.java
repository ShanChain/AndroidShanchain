package com.shanchain.shandata.mvp.view.activity.story;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.global.Constans;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.time.TimePickerDialog;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

public class SelectTimeActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    private static final int RESULT_CODE = 10;
    ArthurToolBar mToolbarSelectTime;
    @Bind(R.id.mcv_select_time)
    MaterialCalendarView mMcvSelectTime;
    private final Calendar mCalendar = Calendar.getInstance();
    @Bind(R.id.btn_select_time_start)
    Button mBtnSelectTimeStart;
    @Bind(R.id.btn_select_time_end)
    Button mBtnSelectTimeEnd;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_select_time;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initData() {
        mMcvSelectTime.setSelectedDate(Calendar.getInstance());
    }

    private void initToolBar() {
        mToolbarSelectTime = (ArthurToolBar) findViewById(R.id.toolbar_select_time);
        mToolbarSelectTime.setBtnEnabled(true);
        mToolbarSelectTime.setOnLeftClickListener(this);
        mToolbarSelectTime.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        CalendarDay selectedDate = mMcvSelectTime.getSelectedDate();
        int day = selectedDate.getDay();
        int month = selectedDate.getMonth();
        LogUtils.d(Constans.TIMES_MONTH[month] + "月" + day + "日");
        String startTime = mBtnSelectTimeStart.getText().toString();
        String endTime = mBtnSelectTimeEnd.getText().toString();
        if (!startTime.contains(":")||!endTime.contains(":")){
            ToastUtils.showToast(this,"请选择开始时间和结束时间");
            return;
        }

        String startHour = startTime.split(":")[0];
        String startMin = startTime.split(":")[1];
        String endHour = endTime.split(":")[0];
        String endMin = endTime.split(":")[1];

        if (Integer.parseInt(startHour)>Integer.parseInt(endHour)){
            //开始小时时间大于结束小时时间，不符合要求
            ToastUtils.showToast(this,"结束时间不能早于开始时间");
            return;
        }else if (Integer.parseInt(startHour)==Integer.parseInt(endHour)){
            //开始小时时间等于结束小时时间，需判断分钟时间
            if (Integer.parseInt(startMin)>=Integer.parseInt(endMin)){
                //开始分钟时间大于结束分钟时间，不符合要求
                ToastUtils.showToast(this,"结束时间不能早于开始时间");
                return;
            }

        }else {
            //开始小时时间小于结束小时时间，符合要求
        }

        Intent intent = new Intent();
        intent.putExtra("date",Constans.TIMES_MONTH[month] + "月" + day + "日");
        intent.putExtra("startTime",startTime);
        intent.putExtra("endTime",endTime);
        setResult(RESULT_CODE,intent);
        finish();
    }




    @OnClick({R.id.btn_select_time_start, R.id.btn_select_time_end})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_time_start:
                selectTime(true);
                break;
            case R.id.btn_select_time_end:
                selectTime(false);
                break;
        }
    }

    private void selectTime(final boolean isStartTime) {
        TimePickerDialog timePickerDialog24h = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog dialog, int hourOfDay, int minute) {
                LogUtils.d(hourOfDay + "点" + minute + "分");

                if (isStartTime){
                    mBtnSelectTimeStart.setText(hourOfDay+":"+(minute<10?"0" + minute:minute));
                }else {
                    mBtnSelectTimeEnd.setText(hourOfDay+":"+(minute<10?"0" + minute:minute));
                }
            }

            @Override
            public void onTimeCleared(TimePickerDialog timePickerDialog) {

            }
        }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false, true);

        timePickerDialog24h.show(getFragmentManager(), "24h");
    }
}
