package com.shanchain.mvp.view.activity.challenge;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.R;
import com.shanchain.adapter.SleepEarlierAdapter;
import com.shanchain.base.BaseActivity;
import com.shanchain.mvp.model.SleepEarlierListInfo;
import com.shanchain.utils.ToastUtils;
import com.shanchain.widgets.toolBar.ArthurToolBar;
import com.weigan.loopview.LoopView;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class NoBowActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, View.OnClickListener {

    ArthurToolBar mToolbarNoBow;
    @Bind(R.id.xrv_no_bow)
    XRecyclerView mXrvNoBow;
    @Bind(R.id.activity_no_bow)
    LinearLayout mActivityNoBow;
    private List<SleepEarlierListInfo> datas;

    private View mHeadView;
    private LoopView mLvHeadBowHours;
    private LoopView mLvHeadBowMin;
    private Button mBtnStart;
    private ArrayList<String> mListHours;
    private ArrayList<String> mListMins;
    private BubbleSeekBar mBsbBow;

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
    }

    private void initRecyclerView() {
        initHeadView();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXrvNoBow.setLayoutManager(linearLayoutManager);

        mXrvNoBow.setPullRefreshEnabled(false);
        mXrvNoBow.setLoadingMoreEnabled(false);
        mXrvNoBow.addHeaderView(mHeadView);
        SleepEarlierAdapter sleepEarlierAdapter = new SleepEarlierAdapter(this,R.layout.item_sleep_earier,datas);
        mXrvNoBow.setAdapter(sleepEarlierAdapter);


    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(this)
                .inflate(R.layout.headview_no_bow,(ViewGroup)findViewById(android.R.id.content),false);
        mLvHeadBowHours = (LoopView) mHeadView.findViewById(R.id.lv_head_bow_hours);
        mLvHeadBowMin = (LoopView) mHeadView.findViewById(R.id.lv_head_bow_min);
        mBtnStart = (Button) mHeadView.findViewById(R.id.btn_head_no_bow_start);
        mBsbBow = (BubbleSeekBar) mHeadView.findViewById(R.id.bsb_bow);
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
                ToastUtils.showToast(this,hours+":"+mins);
                break;
            case 2:

                break;
        }
    }
}
