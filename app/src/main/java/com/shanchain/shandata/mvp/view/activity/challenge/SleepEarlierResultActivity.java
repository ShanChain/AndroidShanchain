package com.shanchain.shandata.mvp.view.activity.challenge;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.SleepEarlierResultAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.manager.ActivityManager;
import com.shanchain.shandata.mvp.model.SleepEarlierResultInfo;
import com.shanchain.shandata.mvp.view.activity.MainActivity;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;

public class SleepEarlierResultActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {


    ArthurToolBar mToolbarSleepEarlierResult;
    @Bind(R.id.xrv_sleep_earlier_result)
    XRecyclerView mXrvSleepEarlierResult;
    @Bind(R.id.iv_sleep_result_feeling)
    ImageView mIvSleepResultFeeling;
    @Bind(R.id.et_sleep_result_feeling)
    EditText mEtSleepResultFeeling;
    private View mHeadView;
    private boolean isSuccess;
    private TextView mTvHeadSleepResult;
    private List<SleepEarlierResultInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_sleep_earlier_result;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        isSuccess = intent.getBooleanExtra("isSuccess", false);
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        initHeadView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXrvSleepEarlierResult.setLayoutManager(linearLayoutManager);
        mXrvSleepEarlierResult.setPullRefreshEnabled(false);
        mXrvSleepEarlierResult.setLoadingMoreEnabled(false);
        mXrvSleepEarlierResult.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this,1),
                getResources().getColor(R.color.colorListDivider)));
        mXrvSleepEarlierResult.addHeaderView(mHeadView);
        SleepEarlierResultAdapter sleepEarlierResultAdapter = new SleepEarlierResultAdapter(this, R.layout.item_sleep_result, datas);
        mXrvSleepEarlierResult.setAdapter(sleepEarlierResultAdapter);
    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(this)
                .inflate(R.layout.headview_sleep_earlier_result,
                        (ViewGroup) findViewById(android.R.id.content), false);
        mTvHeadSleepResult = (TextView) mHeadView.findViewById(R.id.tv_head_sleep_result);
        mTvHeadSleepResult.setText(isSuccess ? "挑战成功！" : "挑战失败！");
    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            SleepEarlierResultInfo earlierResultInfo = new SleepEarlierResultInfo();
            earlierResultInfo.setConfidence("信心"+new Random().nextInt(100) + "%");
            earlierResultInfo.setName("郭达·斯坦森");
            earlierResultInfo.setShanquan("善券+" + new Random().nextInt(80));
            earlierResultInfo.setShanyuan("善圆+" + new Random().nextInt(20));
            datas.add(earlierResultInfo);
        }

    }

    private void initToolBar() {
        mToolbarSleepEarlierResult = (ArthurToolBar) findViewById(R.id.toolbar_sleep_earlier_result);
        mToolbarSleepEarlierResult.setBtnVisibility(true);
        mToolbarSleepEarlierResult.setBtnVisibility(true);
        mToolbarSleepEarlierResult.setTitleText(isSuccess ? "挑战成功" : "挑战失败");
        mToolbarSleepEarlierResult.setOnLeftClickListener(this);
        mToolbarSleepEarlierResult.setOnRightClickListener(this);
    }


    @OnClick(R.id.iv_sleep_result_feeling)
    public void onClick() {
        String feelingContent = mEtSleepResultFeeling.getText().toString().trim();
        if (TextUtils.isEmpty(feelingContent)) {
            ToastUtils.showToast(this, "快去写点此刻的心情吧~");
        } else {

        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        readyGo(MainActivity.class);
        ActivityManager.getInstance().finishAllActivity();
    }
}
