package com.shanchain.shandata.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.shanchain.shandata.R;

import java.text.DecimalFormat;

/**
 * Created by zhoujian on 2017/5/19.
 * 倒计时工具类
 */

public class CountDownTimeUtils extends CountDownTimer {

    private TextView view;
    private String mString;
    private Context mContext;

    public void setContext(Context context) {
        mContext = context;
    }

    public CountDownTimeUtils(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        view = textView;
    }

    public CountDownTimeUtils(TextView textView, String string, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        view = textView;
        this.mString = string;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        DecimalFormat df = new DecimalFormat("00");
        long millisSecond = millisUntilFinished / 1000;
        long day = millisSecond / (60 * 60 * 24);//以天为单位，显示倒计时
//                                    long hour = (millisSecond / (60 * 60) - day * 24);（当以天为计时单位时，小时数以24小时显示，减去day*24小时）
        long hour = (millisSecond / (60 * 60));//以小时的单位显示倒计时
        long min = ((millisSecond / 60) - day * 24 * 60 - hour * 60);
        long s = (millisSecond - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        //正在计时
        if (mString != null) {
            view.setEnabled(false);
            if (millisSecond < 60 * 60) {//小于一小时
                view.setText(mString + df.format(min) + ":" + df.format(s) + "s");
            } else if (millisSecond < 60 * 60 * 24) { //小于一天
                view.setText(mString + df.format(hour) + ":" + df.format(min) + ":" + df.format(s) + "s");
            } else {
                view.setText(mString + df.format(day) + "天");
            }
        } else {
            view.setEnabled(false);
            view.setText(mContext.getResources().getString(R.string.again_send_sms,(millisUntilFinished / 1000)+""));
        }
    }

    @Override
    public void onFinish() {
        //计时完成
        if (mString != null) {
            view.setVisibility(View.GONE);
        } else {
            view.setEnabled(true);
            view.setText(mContext.getResources().getString(R.string.str_register_code_obtain));
        }
    }

}
