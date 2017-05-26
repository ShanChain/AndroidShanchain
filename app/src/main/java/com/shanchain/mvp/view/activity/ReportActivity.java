package com.shanchain.mvp.view.activity;

import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.mvp.model.PublisherInfo;

import butterknife.Bind;

public class ReportActivity extends BaseActivity {

    @Bind(R.id.tv_info)
    TextView mTvInfo;
    @Bind(R.id.activity_report)
    RelativeLayout mActivityReport;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_report;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        PublisherInfo publishInfo = (PublisherInfo) intent.getSerializableExtra("publishInfo");
        int comments = publishInfo.getComments();
        mTvInfo.setText(comments+"");
    }


}
