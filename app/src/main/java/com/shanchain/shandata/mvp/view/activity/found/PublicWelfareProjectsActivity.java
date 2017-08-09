package com.shanchain.shandata.mvp.view.activity.found;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.view.activity.DetailsActivity;
import com.shanchain.shandata.utils.GlideRoundTransform;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class PublicWelfareProjectsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {
    ArthurToolBar mToolbarPublicWelfareProjects;
    @Bind(R.id.iv_public_welfare_img)
    ImageView mIvPublicWelfareImg;
    @Bind(R.id.tv_public_welfare_title)
    TextView mTvPublicWelfareTitle;
    @Bind(R.id.tv_public_welfare_charity)
    TextView mTvPublicWelfareCharity;
    @Bind(R.id.tv_public_welfare_des)
    TextView mTvPublicWelfareDes;
    @Bind(R.id.tv_public_welfare_extra)
    TextView mTvPublicWelfareExtra;
    @Bind(R.id.tv_public_welfare_detail)
    TextView mTvPublicWelfareDetail;
    @Bind(R.id.btn_public_welfare_donate)
    Button mBtnPublicWelfareDonate;
    @Bind(R.id.tv_public_welfare_start)
    TextView mTvPublicWelfareStart;
    @Bind(R.id.tv_public_welfare_num)
    TextView mTvPublicWelfareNum;
    @Bind(R.id.tv_public_welfare_total)
    TextView mTvPublicWelfareTotal;
    @Bind(R.id.activity_public_welfare_projects)
    LinearLayout mActivityPublicWelfareProjects;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_public_welfare_projects;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initToolBar() {
        mToolbarPublicWelfareProjects = (ArthurToolBar) findViewById(R.id.toolbar_public_welfare_projects);
        mToolbarPublicWelfareProjects.setBtnEnabled(true, false);
        mToolbarPublicWelfareProjects.setBtnVisibility(true, false);
        mToolbarPublicWelfareProjects.setOnLeftClickListener(this);
    }

    private void initData() {
        Glide.with(this).load(R.drawable.photo_public).transform(new GlideRoundTransform(this)).into(mIvPublicWelfareImg);
    }

    @OnClick({R.id.tv_public_welfare_detail, R.id.btn_public_welfare_donate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_public_welfare_detail:
                Intent detailIntent = new Intent(this, DetailsActivity.class);
                startActivity(detailIntent);
                break;
            case R.id.btn_public_welfare_donate:
                Intent intent = new Intent(this,DonateActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
