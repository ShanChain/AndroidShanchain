package com.shanchain.shandata.mvp.view.activity.challenge;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.global.TimeDown;
import com.shanchain.shandata.utils.CountDownUtils;
import com.shanchain.shandata.widgets.dialog.CustomDialog;


public class CountdownActivity extends BaseActivity implements TimeDown {

    private TextView mTvCountdownTime;
    private Button mBtnCountdownQuit;
    private CountDownUtils mCountDownUtils;
    private int mTime;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDownUtils.cancel();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_countdown;
    }

    @Override
    protected void initViewsAndEvents() {
        mTime = getIntent().getIntExtra("time", 0);

        mTvCountdownTime = (TextView) findViewById(R.id.tv_countdown_time);
        mBtnCountdownQuit = (Button) findViewById(R.id.btn_countdown_quit);

        mCountDownUtils = new CountDownUtils(this, mTvCountdownTime,  mTime *60 *1000, 1000);
        mCountDownUtils.start();

        mBtnCountdownQuit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CustomDialog dialog = new CustomDialog(CountdownActivity.this, false, 0.8, R.layout.dialog_quit_countdown, new int[]{R.id.tv_dialog_countdown_continue, R.id.tv_dialog_countdown_quit});

                dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(CustomDialog dialog, View view) {
                        switch (view.getId()) {
                            case R.id.tv_dialog_countdown_continue:
                                break;
                            case R.id.tv_dialog_countdown_quit:
                                noBowResult(false);
                                break;
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {

            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTimeOver() {
        noBowResult(true);
    }

    private void noBowResult(boolean isSuccess) {
        Intent intent = new Intent(this, SleepEarlierResultActivity.class);
        intent.putExtra("isSuccess", isSuccess);
        startActivity(intent);
        finish();
    }
}