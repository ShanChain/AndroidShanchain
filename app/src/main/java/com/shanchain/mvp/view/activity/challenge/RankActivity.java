package com.shanchain.mvp.view.activity.challenge;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.R;
import com.shanchain.adapter.RankAdapter;
import com.shanchain.base.BaseActivity;
import com.shanchain.mvp.model.HappierRankInfo;
import com.shanchain.utils.DensityUtils;
import com.shanchain.widgets.other.RecyclerViewDivider;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class RankActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    ArthurToolBar mToolbarRank;
    @Bind(R.id.xrv_rank)
    XRecyclerView mXrvRank;
    @Bind(R.id.activity_rank)
    LinearLayout mActivityRank;
    private List<HappierRankInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_rank;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecycler();
    }

    private void initToolBar() {
        mToolbarRank = (ArthurToolBar) findViewById(R.id.toolbar_rank);
        mToolbarRank.setBtnVisibility(true,false);
        mToolbarRank.setBtnEnabled(true,false);
        mToolbarRank.setOnLeftClickListener(this);
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

        for (int i = 0; i < 133; i ++) {
            HappierRankInfo happierRankInfo1 = new HappierRankInfo();
            happierRankInfo1.setNickName("提莫一米五");
            happierRankInfo1.setPriseCount(400 - i *3);
            datas.add(happierRankInfo1);
        }
    }

    private void initRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXrvRank.setLayoutManager(linearLayoutManager);
        mXrvRank.setPullRefreshEnabled(false);
        mXrvRank.setLoadingMoreEnabled(false);
        mXrvRank.addItemDecoration(new RecyclerViewDivider(RankActivity.this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(RankActivity.this,1),
                getResources().getColor(R.color.colorListDivider)));

        RankAdapter adapter = new RankAdapter(this,R.layout.item_happier_rank,datas);
        mXrvRank.setAdapter(adapter);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
