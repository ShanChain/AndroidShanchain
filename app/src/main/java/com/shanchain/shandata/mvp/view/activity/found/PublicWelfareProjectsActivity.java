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
        String s1 = "我的天啊我是第一行。";
        String s2 = "我是第二行，我好想换行。";
        String s3 = "我是第三行，真的假的。真的可以换行吗，我这一行好像有点长啊，是不是哦？";
        String s4 = "上面说的对。";
        Glide.with(this).load(R.drawable.photo_public).into(mIvPublicWelfareImg);
        mTvPublicWelfareDes.setText(s1 + "\n" + s2 + "\n"+ s3 + "\n"+ s4 );
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
