package com.shanchain.shandata.mvp.view.activity.story;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class CreateSuccessActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarCreateSuccess;
    @Bind(R.id.iv_create_success_img)
    ImageView mIvCreateSuccessImg;
    @Bind(R.id.tv_create_success_name)
    TextView mTvCreateSuccessName;
    @Bind(R.id.tv_create_success_result)
    TextView mTvCreateSuccessResult;
    @Bind(R.id.iv_create_success_shan)
    ImageView mIvCreateSuccessShan;
    @Bind(R.id.tv_create_success_shan)
    TextView mTvCreateSuccessShan;
    @Bind(R.id.btn_create_success_record)
    Button mBtnCreateSuccessRecord;
    @Bind(R.id.activity_create_success)
    LinearLayout mActivityCreateSuccess;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_create_success;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initData() {

        Glide.with(this)
                .load(R.drawable.photo_yue)
                .transform(new GlideCircleTransform(this))
                .into(mIvCreateSuccessImg);

        Glide.with(this)
                .load(R.drawable.photo_yue)
                .transform(new GlideCircleTransform(this))
                .into(mIvCreateSuccessShan);

    }

    private void initToolBar() {
        mToolbarCreateSuccess = (ArthurToolBar) findViewById(R.id.toolbar_create_success);
        mToolbarCreateSuccess.setBtnEnabled(true,false);
        mToolbarCreateSuccess.setBtnVisibility(true,false);
        mToolbarCreateSuccess.setOnLeftClickListener(this);
    }

    @OnClick(R.id.btn_create_success_record)
    public void onClick() {
        readyGo(NewRouteActivity.class);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
