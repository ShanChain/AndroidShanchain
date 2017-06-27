package com.shanchain.shandata.mvp.view.activity.challenge;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.NoBowRankAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.NoBowRankInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.CustomSeekBar;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.weigan.loopview.LoopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class NoBowActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, View.OnClickListener {

    ArthurToolBar mToolbarNoBow;
    @Bind(R.id.xrv_no_bow)
    XRecyclerView mXrvNoBow;
    @Bind(R.id.activity_no_bow)
    LinearLayout mActivityNoBow;
    private List<NoBowRankInfo> datas;

    private View mHeadView;
    private LoopView mLvHeadBowHours;
    private LoopView mLvHeadBowMin;
    private Button mBtnStart;
    private ArrayList<String> mListHours;
    private ArrayList<String> mListMins;
    private TextView mTvHeadNoBowRules;
    private CustomSeekBar mCustomSeekBar;
    private TextView mTvHeadNoBowRank;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_no_bow;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initToolBar() {
        mToolbarNoBow = (ArthurToolBar) findViewById(R.id.toolbar_no_bow);
        mToolbarNoBow.setBtnEnabled(true,false);
        mToolbarNoBow.setBtnVisibility(true,false);
        mToolbarNoBow.setOnLeftClickListener(this);
    }

    private void initData() {
        datas = new ArrayList<>();
        NoBowRankInfo noBowRankInfo1 = new NoBowRankInfo();
        noBowRankInfo1.setRank(1);
        noBowRankInfo1.setNickName("张伟");
        noBowRankInfo1.setTime("10小时");
        datas.add(noBowRankInfo1);

        NoBowRankInfo noBowRankInfo2 = new NoBowRankInfo();
        noBowRankInfo2.setRank(2);
        noBowRankInfo2.setNickName("刘天明");
        noBowRankInfo2.setTime("9小时30分");
        datas.add(noBowRankInfo2);

        NoBowRankInfo noBowRankInfo3 = new NoBowRankInfo();
        noBowRankInfo3.setRank(3);
        noBowRankInfo3.setNickName("黄伟");
        noBowRankInfo3.setTime("8小时");
        datas.add(noBowRankInfo3);

        NoBowRankInfo noBowRankInfo4 = new NoBowRankInfo();
        noBowRankInfo4.setRank(4);
        noBowRankInfo4.setNickName("李瑶");
        noBowRankInfo4.setTime("8小时6分");
        datas.add(noBowRankInfo4);

        NoBowRankInfo noBowRankInfo5 = new NoBowRankInfo();
        noBowRankInfo5.setRank(5);
        noBowRankInfo5.setNickName("郭国康");
        noBowRankInfo5.setTime("6小时");
        datas.add(noBowRankInfo5);

        for (int i = 0; i < 5; i ++) {
            NoBowRankInfo noBowRankInfo = new NoBowRankInfo();
            noBowRankInfo.setRank(6 + i);
            noBowRankInfo.setNickName("李瑶");
            noBowRankInfo.setTime("5小时");
            datas.add(noBowRankInfo);
        }

        NoBowRankInfo noBowRankInf = new NoBowRankInfo();
        noBowRankInf.setRank(233);
        noBowRankInf.setNickName("深港澳第一帅");
        noBowRankInf.setTime("4小时");
        datas.add(noBowRankInf);
    }

    private void initRecyclerView() {
        initHeadView();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXrvNoBow.setLayoutManager(linearLayoutManager);

        mXrvNoBow.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this,1),
                getResources().getColor(R.color.colorListDivider)));

        mXrvNoBow.setPullRefreshEnabled(false);
        mXrvNoBow.setLoadingMoreEnabled(false);
        mXrvNoBow.addHeaderView(mHeadView);
        NoBowRankAdapter adapter = new NoBowRankAdapter(this,R.layout.item_happier_rank,datas);
        mXrvNoBow.setAdapter(adapter);


    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(this)
                .inflate(R.layout.headview_no_bow,(ViewGroup)findViewById(android.R.id.content),false);
        mLvHeadBowHours = (LoopView) mHeadView.findViewById(R.id.lv_head_bow_hours);
        mLvHeadBowMin = (LoopView) mHeadView.findViewById(R.id.lv_head_bow_min);
        mTvHeadNoBowRank = (TextView) mHeadView.findViewById(R.id.tv_head_no_bow_rank);
        mBtnStart = (Button) mHeadView.findViewById(R.id.btn_head_no_bow_start);
        mCustomSeekBar = (CustomSeekBar) mHeadView.findViewById(R.id.csb_head_no_bow);
        mTvHeadNoBowRules = (TextView) mHeadView.findViewById(R.id.tv_head_no_bow_rules);
        mTvHeadNoBowRules.setOnClickListener(this);
        mTvHeadNoBowRank.setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
        mListHours = new ArrayList();
        for (int i = 0; i < 25; i ++) {
            mListHours.add(""+(24-i));
        }
        mListMins = new ArrayList();
        for (int i = 0; i < 61; i ++) {
            mListMins.add("" +(60-i));
        }

        mLvHeadBowHours.setItems(mListHours);
        mLvHeadBowHours.setInitPosition(mListHours.size()-1);
        mLvHeadBowMin.setItems(mListMins);
        mLvHeadBowMin.setInitPosition(mListMins.size()-1);


    }


    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_head_no_bow_start:
                int hoursItem = mLvHeadBowHours.getSelectedItem();
                String hours = mListHours.get(hoursItem);
                int minsItem = mLvHeadBowMin.getSelectedItem();
                String mins = mListMins.get(minsItem);
                int progress = mCustomSeekBar.getProgress();

                ToastUtils.showToast(this,hours+":"+mins + ",信心：" + progress);

                readyGo(CountdownActivity.class);

                break;
            case R.id.tv_head_no_bow_rules:
                CustomDialog dialog = new CustomDialog(this,R.layout.dialog_no_bow,null);
                dialog.show();
                break;
            case R.id.tv_head_no_bow_rank:
                readyGo(RankActivity.class);
                break;
        }
    }
}
