package com.shanchain.shandata.mvp.view.activity.challenge;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.SleepEarlierAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.SleepEarlierListInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class WalkedMoreActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, View.OnClickListener {


    ArthurToolBar mToolbarWalkMore;
    @Bind(R.id.xrv_walk_more)
    XRecyclerView mXrvWalkMore;
    @Bind(R.id.activity_walk_more)
    LinearLayout mActivityWalkMore;
    private View mHeadView;
    private TextView mTvHeadWalkCounts;
    private TextView mTvHeadWalkRulesDetails;
    private Button mBtnHeadWalkChallenge;
    private TextView mTvHeadWalkChampionCounts;
    private List<SleepEarlierListInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_walk_more;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initToolBar() {
        mToolbarWalkMore = (ArthurToolBar) findViewById(R.id.toolbar_walk_more);
        mToolbarWalkMore.setOnLeftClickListener(this);
        mToolbarWalkMore.setOnRightClickListener(this);
    }

    private void initData() {
        datas = new ArrayList<>();
        SleepEarlierListInfo sleepEarierListInfo = new SleepEarlierListInfo();
        sleepEarierListInfo.setTitle("90走图");
        sleepEarierListInfo.setDes("坚持，不放弃");
        sleepEarierListInfo.setActive(1);
        sleepEarierListInfo.setName("张小杨");
        sleepEarierListInfo.setTime("今天");
        datas.add(sleepEarierListInfo);


        SleepEarlierListInfo sleepEarierListInfo2 = new SleepEarlierListInfo();
        sleepEarierListInfo2.setTitle("凑步数");
        sleepEarierListInfo2.setDes("步数");
        sleepEarierListInfo2.setActive(1);
        sleepEarierListInfo2.setName("鑫海湾");
        sleepEarierListInfo2.setTime("今天");
        datas.add(sleepEarierListInfo2);

        SleepEarlierListInfo sleepEarierListInfo3 = new SleepEarlierListInfo();
        sleepEarierListInfo3.setTitle("走路比赛");
        sleepEarierListInfo3.setDes("5天步数比赛");
        sleepEarierListInfo3.setActive(2);
        sleepEarierListInfo3.setName("鲁冰花");
        sleepEarierListInfo3.setTime("4月23日");
        datas.add(sleepEarierListInfo3);

        SleepEarlierListInfo sleepEarierListInfo4 = new SleepEarlierListInfo();
        sleepEarierListInfo4.setTitle("90走团");
        sleepEarierListInfo4.setDes("坚持，不放弃！");
        sleepEarierListInfo4.setActive(5);
        sleepEarierListInfo4.setName("龙宏毅");
        sleepEarierListInfo4.setTime("今天");
        datas.add(sleepEarierListInfo4);

        SleepEarlierListInfo sleepEarierListInfo5 = new SleepEarlierListInfo();
        sleepEarierListInfo5.setTitle("多走走");
        sleepEarierListInfo5.setDes("坚持，不放弃！");
        sleepEarierListInfo5.setActive(200);
        sleepEarierListInfo5.setName("习大大");
        sleepEarierListInfo5.setTime("今天");
        datas.add(sleepEarierListInfo5);
    }

    private void initRecyclerView() {
        initHeadView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXrvWalkMore.setLayoutManager(linearLayoutManager);
        mXrvWalkMore.setLoadingMoreEnabled(false);
        mXrvWalkMore.setPullRefreshEnabled(false);
        mXrvWalkMore.addHeaderView(mHeadView);

        mXrvWalkMore.addItemDecoration(new RecyclerViewDivider(WalkedMoreActivity.this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(WalkedMoreActivity.this,10),
                getResources().getColor(R.color.colorWhite)));

        SleepEarlierAdapter walkMoreAdapter = new SleepEarlierAdapter(this,R.layout.item_sleep_earier,datas);
        mXrvWalkMore.setAdapter(walkMoreAdapter);
        walkMoreAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                readyGo(PartInWalkMoreActivity.class);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(this)
                .inflate(R.layout.headview_walk_more,
                        (ViewGroup)findViewById(android.R.id.content),false);
        mTvHeadWalkCounts = (TextView) mHeadView.findViewById(R.id.tv_head_walk_counts);
        mTvHeadWalkRulesDetails = (TextView) mHeadView.findViewById(R.id.tv_head_walk_rules_details);
        mBtnHeadWalkChallenge = (Button) mHeadView.findViewById(R.id.btn_head_walk_challenge);
        mTvHeadWalkChampionCounts = (TextView) mHeadView.findViewById(R.id.tv_head_walk_champion_counts);
        mTvHeadWalkRulesDetails.setOnClickListener(this);
        mBtnHeadWalkChallenge.setOnClickListener(this);

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
            case R.id.tv_head_walk_rules_details:
                CustomDialog customDialog = new CustomDialog(this,false,0.8,R.layout.dialog_walk_more,null);
                customDialog.show();
                break;
            case R.id.btn_head_walk_challenge:
                readyGo(ChooseChallengeActivity.class);
                break;
        }
    }
}
