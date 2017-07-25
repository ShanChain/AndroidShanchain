package com.shanchain.shandata.mvp.view.activity.challenge;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.HasPartInSleepListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.manager.ActivityManager;
import com.shanchain.shandata.mvp.model.HappierRankInfo;
import com.shanchain.shandata.mvp.view.activity.MainActivity;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.AutoHeightListView;
import com.shanchain.shandata.widgets.other.CustomSeekBar;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class HasPartInSleepEarlierActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, View.OnClickListener {


    ArthurToolBar mToolbarHasPartSleepEarlier;
    @Bind(R.id.tv_has_part_sleep_counts)
    TextView mTvHasPartSleepCounts;
    @Bind(R.id.tv_has_part_sleep_rules)
    TextView mTvHasPartSleepRules;
    @Bind(R.id.btn_has_part_sleep_start)
    Button mBtnHasPartSleepStart;
    @Bind(R.id.tv_has_part_in_sleep_rank)
    TextView mTvHasPartInSleepRank;
    @Bind(R.id.lv_has_sleep)
    AutoHeightListView mLvHasSleep;
    @Bind(R.id.iv_has_part_sleep_img)
    ImageView mIvHasPartSleepImg;
    @Bind(R.id.tv_has_part_sleep_name1)
    TextView mTvHasPartSleepName1;
    @Bind(R.id.tv_has_part_sleep_name2)
    TextView mTvHasPartSleepName2;
    @Bind(R.id.tv_has_part_sleep_time)
    TextView mTvHasPartSleepTime;
    @Bind(R.id.tv_has_part_sleep_info)
    TextView mTvHasPartSleepInfo;
    @Bind(R.id.csb_has_part_sleep_earlier)
    CustomSeekBar mCsbHasPartSleepEarlier;
    @Bind(R.id.tv_has_part_sleep_pay)
    TextView mTvHasPartSleepPay;
    @Bind(R.id.btn_has_part_sleep_quit)
    Button mBtnHasPartSleepQuit;

    private List<HappierRankInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_has_part_in_sleep_earlier;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        //  initRecyclerView();
        initListView();
    }

    private void initListView() {
        HasPartInSleepListAdapter listAdapter = new HasPartInSleepListAdapter(this, datas);
        mLvHasSleep.setAdapter(listAdapter);
    }

    private void initData() {
        datas = new ArrayList<>();

        HappierRankInfo happierRankInfo = new HappierRankInfo();
        happierRankInfo.setNickName("天王盖地虎");
        happierRankInfo.setPriseCount(1234);
        datas.add(happierRankInfo);

        HappierRankInfo happierRankInfo2 = new HappierRankInfo();
        happierRankInfo2.setNickName("小鸡炖蘑菇");
        happierRankInfo2.setPriseCount(998);
        datas.add(happierRankInfo2);

        HappierRankInfo happierRankInfo3 = new HappierRankInfo();
        happierRankInfo3.setNickName("宝塔镇河妖");
        happierRankInfo3.setPriseCount(666);
        datas.add(happierRankInfo3);

        HappierRankInfo happierRankInfo4 = new HappierRankInfo();
        happierRankInfo4.setNickName("蘑菇放辣椒");
        happierRankInfo4.setPriseCount(435);
        datas.add(happierRankInfo4);

        for (int i = 0; i < 6; i++) {
            HappierRankInfo happierRankInfo1 = new HappierRankInfo();
            happierRankInfo1.setNickName("提莫一米五");
            happierRankInfo1.setPriseCount(400 - i * 3);
            datas.add(happierRankInfo1);
        }
    }

    private void initToolBar() {
        mToolbarHasPartSleepEarlier = (ArthurToolBar) findViewById(R.id.toolbar_has_part_sleep_earlier);
        mToolbarHasPartSleepEarlier.setBtnEnabled(true, false);
        mToolbarHasPartSleepEarlier.setBtnVisibility(true, false);
        mToolbarHasPartSleepEarlier.setOnLeftClickListener(this);
        mTvHasPartSleepRules.setOnClickListener(this);
        mBtnHasPartSleepStart.setOnClickListener(this);
        mTvHasPartInSleepRank.setOnClickListener(this);
        mBtnHasPartSleepQuit.setOnClickListener(this);
        mCsbHasPartSleepEarlier.setProgress(60);
        mCsbHasPartSleepEarlier.setSeekBarEnable(false);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_has_part_sleep_rules:

                CustomDialog customDialog = new CustomDialog(this, R.layout.dialog_sleep_rules, null);
                customDialog.show();
                break;
            case R.id.btn_has_part_sleep_start:
                readyGo(CountdownActivity.class);
                break;
            case R.id.tv_has_part_in_sleep_rank:
                readyGo(RankActivity.class);
                break;
            case R.id.btn_has_part_sleep_quit:
                CustomDialog dialog = new CustomDialog(this, false, 0.8, R.layout.dialog_quit_countdown, new int[]{R.id.tv_dialog_countdown_quit, R.id.tv_dialog_countdown_continue});
                dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(CustomDialog dialog, View view) {
                        switch (view.getId()) {
                            case R.id.tv_dialog_countdown_quit:
                                readyGo(MainActivity.class);
                                ActivityManager.getInstance().finishAllActivity();
                                break;
                            case R.id.tv_dialog_countdown_continue:

                                break;
                        }
                    }
                });
                dialog.show();
                break;
        }
    }

}
