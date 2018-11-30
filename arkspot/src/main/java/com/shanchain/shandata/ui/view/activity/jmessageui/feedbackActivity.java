package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class feedbackActivity extends AppCompatActivity implements ArthurToolBar.OnRightClickListener {

    @Bind(R.id.tb_main)
    ArthurToolBar tbMain;
    @Bind(R.id.edit_feedback)
    EditText editFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeback);
        ButterKnife.bind(this);

        tbMain.setTitleText("反馈");
        tbMain.setLeftImage(R.mipmap.back);
        tbMain.setBackgroundColor(getResources().getColor(R.color.colorHomeBtn));
        tbMain.setRightText("确定");
        tbMain.setRightTextColor(getResources().getColor(R.color.__picker_black_40));
        tbMain.setTitleTextColor(getResources().getColor(R.color.__picker_black_40));
        tbMain.setOnRightClickListener(this);

    }

    @Override
    public void onRightClick(View v) {
        if (editFeedback.getText().toString().length() > 0) {
            String dataString = "{\"title\":\"用户反馈\",\"disc\":\"" + editFeedback.getText().toString() + "\",\"type\":1}";
            SCHttpUtils.postWithUserId()
                    .url(HttpApi.USE_FEEDBACK)
                    .addParams("dataString", dataString)
                    .build()
                    .execute(new SCHttpStringCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }
                        @Override
                        public void onResponse(String response, int id) {
                            ToastUtils.showToast(feedbackActivity.this,"反馈成功");
                            finish();
                        }
                    });
        } else {
            ToastUtils.showToast(feedbackActivity.this, "请输入反馈内容");
        }
    }
}
