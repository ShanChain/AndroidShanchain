package com.shanchain.mvp.view.activity.challenge;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.R;
import com.shanchain.adapter.SleepEarlierAdapter;
import com.shanchain.base.BaseActivity;
import com.shanchain.mvp.model.SleepEarlierListInfo;
import com.shanchain.utils.DensityUtils;
import com.shanchain.widgets.dialog.CustomDialog;
import com.shanchain.widgets.other.RecyclerViewDivider;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class SleepEarlierActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, View.OnClickListener {

    private ArthurToolBar mToolbarSleepEarlier;
    @Bind(R.id.xrv_sleep_earlier)
    XRecyclerView mXrvSleepEarlier;
    @Bind(R.id.activity_sleep_earlier)
    LinearLayout mActivitySleepEarlier;

    private View mHeadView;
    private TextView mTvHeadSleepCounts;
    private TextView mTvHeadSleepRulesDetails;
    private Button mBtnHeadSleepChallenge;
    private List<SleepEarlierListInfo> datas;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_sleep_earlier;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        initHeadView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mXrvSleepEarlier.setLayoutManager(layoutManager);
        mXrvSleepEarlier.addHeaderView(mHeadView);
        mXrvSleepEarlier.setLoadingMoreEnabled(false);
        mXrvSleepEarlier.setPullRefreshEnabled(false);
        mXrvSleepEarlier.addItemDecoration(new RecyclerViewDivider(SleepEarlierActivity.this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(SleepEarlierActivity.this,10),
                getResources().getColor(R.color.colorWhite)));
        SleepEarlierAdapter sleepEarlierAdapter = new SleepEarlierAdapter(this,R.layout.item_sleep_earier,datas);
        mXrvSleepEarlier.setAdapter(sleepEarlierAdapter);
    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(this)
                .inflate(R.layout.headview_sleep_earlier,(ViewGroup)findViewById(android.R.id.content),false);

        mTvHeadSleepCounts = (TextView) mHeadView.findViewById(R.id.tv_head_sleep_counts);
        mTvHeadSleepRulesDetails = (TextView) mHeadView.findViewById(R.id.tv_head_sleep_rules_details);
        mBtnHeadSleepChallenge = (Button) mHeadView.findViewById(R.id.btn_head_sleep_challenge);

        mTvHeadSleepRulesDetails.setOnClickListener(this);
        mBtnHeadSleepChallenge.setOnClickListener(this);

    }

    private void initData() {
        datas = new ArrayList<>();
        SleepEarlierListInfo sleepEarierListInfo = new SleepEarlierListInfo();
        sleepEarierListInfo.setTitle("11点");
        sleepEarierListInfo.setDes("坚持，不放弃");
        sleepEarierListInfo.setActive(1);
        sleepEarierListInfo.setName("张小杨");
        sleepEarierListInfo.setTime("今天");
        datas.add(sleepEarierListInfo);


        SleepEarlierListInfo sleepEarierListInfo2 = new SleepEarlierListInfo();
        sleepEarierListInfo2.setTitle("早点睡吧");
        sleepEarierListInfo2.setDes("步数");
        sleepEarierListInfo2.setActive(1);
        sleepEarierListInfo2.setName("鑫海湾");
        sleepEarierListInfo2.setTime("今天");
        datas.add(sleepEarierListInfo2);

        SleepEarlierListInfo sleepEarierListInfo3 = new SleepEarlierListInfo();
        sleepEarierListInfo3.setTitle("早睡");
        sleepEarierListInfo3.setDes("5天步数比赛");
        sleepEarierListInfo3.setActive(2);
        sleepEarierListInfo3.setName("鲁冰花");
        sleepEarierListInfo3.setTime("4月23日");
        datas.add(sleepEarierListInfo3);

        SleepEarlierListInfo sleepEarierListInfo4 = new SleepEarlierListInfo();
        sleepEarierListInfo4.setTitle("睡吧");
        sleepEarierListInfo4.setDes("坚持，不放弃！");
        sleepEarierListInfo4.setActive(5);
        sleepEarierListInfo4.setName("龙宏毅");
        sleepEarierListInfo4.setTime("今天");
        datas.add(sleepEarierListInfo4);


    }

    private void initToolBar() {
        mToolbarSleepEarlier = (ArthurToolBar) findViewById(R.id.toolbar_sleep_earlier);
        mToolbarSleepEarlier.setOnLeftClickListener(this);
        mToolbarSleepEarlier.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_sleep_rules_details:
                CustomDialog customDialog = new CustomDialog(this,false,0.8,R.layout.dialog_sleep_rules,new int[]{});
                customDialog.show();
                break;
            case R.id.btn_head_sleep_challenge:

                break;
        }
    }
}
