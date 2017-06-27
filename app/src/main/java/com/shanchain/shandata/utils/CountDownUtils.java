package com.shanchain.shandata.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.shanchain.shandata.mvp.view.activity.challenge.CountdownActivity;

/**
 * Created by zhoujian on 2017/6/20.
 */

public class CountDownUtils extends CountDownTimer {
    TextView mTextView;
    private CountdownActivity mActivity;

    public CountDownUtils(CountdownActivity activity, TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        mTextView = textView;
        mActivity = activity;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        String formatTime = formatTime(millisUntilFinished);
        mTextView.setText(formatTime);
    }

    @Override
    public void onFinish() {
        mActivity.onTimeOver();
    }


    public static String formatTime(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        return strHour + ":"+strMinute + ":" + strSecond;
    }
}
