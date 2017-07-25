package com.shanchain.shandata.mvp.view.activity;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.other.CustomView;

import butterknife.Bind;

public class WebActivity extends BaseActivity {

    @Bind(R.id.tv_web)
    TextView mTvWeb;
    @Bind(R.id.cv_web)
    CustomView mCvWeb;
    @Bind(R.id.zc_web)
    ZoomControls mZcWeb;
    @Bind(R.id.activity_web)
    LinearLayout mActivityWeb;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_web;
    }

    @Override
    protected void initViewsAndEvents() {
        mZcWeb.setIsZoomInEnabled(true);
        mZcWeb.setIsZoomOutEnabled(true);
    }

}
