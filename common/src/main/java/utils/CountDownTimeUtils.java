package utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by zhoujian on 2017/5/19.
 * 倒计时工具类
 */

public class CountDownTimeUtils extends CountDownTimer {

    private TextView view;

    public CountDownTimeUtils(TextView textView , long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        view = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //正在计时
        view.setEnabled(false);
        view.setText(millisUntilFinished/1000 + "s后可重发");
    }

    @Override
    public void onFinish() {
        //计时完成
        view.setEnabled(true);
        view.setText("获取验证码");
    }

}
