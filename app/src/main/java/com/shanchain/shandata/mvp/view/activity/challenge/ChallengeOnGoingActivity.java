package com.shanchain.shandata.mvp.view.activity.challenge;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ChallengeOnGoingAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.ChallengeOnGoingInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ChallengeOnGoingActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    ArthurToolBar mToolbarChallengeOnGoing;
    @Bind(R.id.xrv_challenge_on_going)
    XRecyclerView mXrvChallengeOnGoing;
    private List<ChallengeOnGoingInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_challenge_on_going;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initData() {
        datas = new ArrayList<>();

        for (int i = 1; i < 5; i++) {
            ChallengeOnGoingInfo challengeOnGoingInfo = new ChallengeOnGoingInfo();
            challengeOnGoingInfo.setDes("百变如我，秀出自己");
            challengeOnGoingInfo.setType(i);
            datas.add(challengeOnGoingInfo);
        }

    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXrvChallengeOnGoing.setLayoutManager(linearLayoutManager);
        mXrvChallengeOnGoing.setPullRefreshEnabled(false);
        mXrvChallengeOnGoing.setLoadingMoreEnabled(false);
        mXrvChallengeOnGoing.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this, 1),
                getResources().getColor(R.color.colorListDivider)));

        ChallengeOnGoingAdapter challengeOnGoingAdapter = new ChallengeOnGoingAdapter(this, R.layout.item_challenge_on_going, datas);
        mXrvChallengeOnGoing.setAdapter(challengeOnGoingAdapter);
        challengeOnGoingAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                /** 描述：
                 * type = 1  :  多走走
                 * type = 2  :  开心点
                 * type = 3  :  早点睡
                 * type = 4  :  别低头
                 * type = 5  :  其他
                 * */
                switch (datas.get(position-1).getType()) {
                    case 1:
                        readyGo(WalkingMoreActivity.class);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void initToolBar() {
        mToolbarChallengeOnGoing = (ArthurToolBar) findViewById(R.id.toolbar_challenge_on_going);
        mToolbarChallengeOnGoing.setBtnEnabled(true, false);
        mToolbarChallengeOnGoing.setBtnVisibility(true, false);
        mToolbarChallengeOnGoing.setOnLeftClickListener(this);

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
