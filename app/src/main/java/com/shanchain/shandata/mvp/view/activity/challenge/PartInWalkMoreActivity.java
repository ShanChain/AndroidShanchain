package com.shanchain.shandata.mvp.view.activity.challenge;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.CustomSeekBar;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class PartInWalkMoreActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    ArthurToolBar mToolbarPartInWalkMore;
    @Bind(R.id.tv_part_walk_counts)
    TextView mTvPartWalkCounts;
    @Bind(R.id.tv_part_walk_rules)
    TextView mTvPartWalkRules;
    @Bind(R.id.iv_part_walk_img)
    ImageView mIvPartWalkImg;
    @Bind(R.id.tv_part_walk_name1)
    TextView mTvPartWalkName1;
    @Bind(R.id.tv_part_walk_name2)
    TextView mTvPartWalkName2;
    @Bind(R.id.tv_part_walk_time)
    TextView mTvPartWalkTime;
    @Bind(R.id.tv_part_walk_info)
    TextView mTvPartWalkInfo;
    @Bind(R.id.csb_part_walk)
    CustomSeekBar mCsbPartWalk;
    @Bind(R.id.tv_part_walk_pay)
    TextView mTvPartWalkPay;
    @Bind(R.id.btn_part_walk_in)
    Button mBtnPartWalkIn;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_part_in_walk_more;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mToolbarPartInWalkMore = (ArthurToolBar) findViewById(R.id.toolbar_part_in_walk_more);
        mToolbarPartInWalkMore.setBtnEnabled(true, false);
        mToolbarPartInWalkMore.setBtnVisibility(true, false);
        mToolbarPartInWalkMore.setOnLeftClickListener(this);


    }

    @OnClick({R.id.tv_part_walk_rules, R.id.btn_part_walk_in})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_part_walk_rules:
                CustomDialog customDialog = new CustomDialog(this, R.layout.dialog_walk_more, null);
                customDialog.show();
                break;
            case R.id.btn_part_walk_in:
                readyGo(WalkingMoreActivity.class);
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
