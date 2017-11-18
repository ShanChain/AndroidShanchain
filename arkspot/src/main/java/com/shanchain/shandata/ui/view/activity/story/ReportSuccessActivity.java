package com.shanchain.shandata.ui.view.activity.story;

import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.manager.ActivityManager;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import com.shanchain.data.common.utils.SpanUtils;


public class ReportSuccessActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener {


    @Bind(R.id.tb_report_success)
    ArthurToolBar mTbReportSuccess;
    @Bind(R.id.tv_report_success_protocol)
    TextView mTvReportSuccessProtocol;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_report_success;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        String str = "感谢您的举报，我们将根据《十维使用协议》进 行审核（夜间时段稍有延迟）";
        SpannableStringBuilder spannableStringBuilder = SpanUtils.buildSpannableString(str, getResources().getColor(R.color.colorDialogBtn), 12, 20);
        mTvReportSuccessProtocol.setText(spannableStringBuilder);
    }

    private void initToolBar() {
        mTbReportSuccess.setBtnEnabled(false,true);
        mTbReportSuccess.setOnRightClickListener(this);
    }

    @Override
    public void onRightClick(View v) {
        ActivityManager.getInstance().finishActivity(ReportActivity.class);
        finish();
    }
}
