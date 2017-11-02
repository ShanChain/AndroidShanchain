package com.shanchain.arkspot.ui.view.activity.chat;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.utils.DateUtils;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.timepicker.SCTimePickerView;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;


public class CreateDramaActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_create_drama)
    ArthurToolBar mTbCreateDrama;
    @Bind(R.id.et_create_drama_name)
    EditText mEtCreateDramaName;
    @Bind(R.id.et_create_drama_des)
    EditText mEtCreateDramaDes;
    @Bind(R.id.ll_create_drama_time)
    LinearLayout mLlCreateDramaTime;
    @Bind(R.id.btn_create_drama_start)
    Button mBtnCreateDramaStart;
    @Bind(R.id.btn_create_drama_cancel)
    Button mBtnCreateDramaCancel;
    @Bind(R.id.tv_create_drama_time)
    TextView mTvCreateDramaTime;
    private long mStartTime = 0;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_create_drama;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mTbCreateDrama.setBtnEnabled(true, false);
        mTbCreateDrama.setOnLeftClickListener(this);
    }


    @OnClick({R.id.ll_create_drama_time, R.id.btn_create_drama_start, R.id.btn_create_drama_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_create_drama_time:
                //设定时间
                setTime();

                break;
            case R.id.btn_create_drama_start:
                //

                startDrama();

                break;
            case R.id.btn_create_drama_cancel:
                finish();
                break;
        }
    }


    private void setTime() {
        SCTimePickerView pickerView = new SCTimePickerView.Builder(this, new SCTimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                long time = date.getTime() / 1000;    //单位转成秒
                long currentTime = System.currentTimeMillis() / 1000;//单位转成秒
                long diffTime = time - currentTime;
                if (diffTime < 0) {    //时间在当前时间之前
                    ToastUtils.showToast(mContext, "选择的时间不正确");
                } else if (diffTime > 60 * 60 * 24 * 7) {   //选择的时间超过七天
                    ToastUtils.showToast(mContext, "倒计时必须在七天之内");
                } else {
                    String dramaStartTime = DateUtils.getDramaStartTime(diffTime);
                    mTvCreateDramaTime.setText(dramaStartTime);
                    mStartTime = date.getTime();
                }

            }

        })
                .setType(new boolean[]{true, true, true, true, true, true})
                .isCenterLabel(false)
                .setCancelText("取消")
                .setCancelColor(getResources().getColor(R.color.colorDialogBtn))
                .setSubmitText("完成")
                .setSubCalSize(14)
                .setTitleBgColor(getResources().getColor(R.color.colorWhite))
                .setSubmitColor(getResources().getColor(R.color.colorDialogBtn))
                .build();
        pickerView.setDate(Calendar.getInstance());
        pickerView.show();

        pickerView.setOnCancelClickListener(new SCTimePickerView.OnCancelClickListener() {
            @Override
            public void onCancelClick(View v) {

            }
        });

    }

    private void startDrama() {
        String title = mEtCreateDramaName.getText().toString().trim();
        String intro = mEtCreateDramaDes.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            ToastUtils.showToast(this, "戏名不能为空");
            return;
        }

        if (TextUtils.isEmpty(intro)) {
            ToastUtils.showToast(this, "对戏描述不能为空");
            return;
        }

        if (mStartTime == 0){
            ToastUtils.showToast(this,"请设置大戏开始时间");
            return;
        }
        //提交数据
        createDrama(title,intro);
    }

    private void createDrama(String title, String intro) {
        SCHttpUtils.post()
                .url(HttpApi.HX_DRAMA_CREATE)
                .addParams("groupId","")
                .addParams("intro",intro)
                .addParams("startTime",mStartTime+"")
                .addParams("title",title)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("创建大戏失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("创建大戏成功 = " + response);
                    }
                });
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
