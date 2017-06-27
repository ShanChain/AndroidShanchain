package com.shanchain.shandata.mvp.view.activity.challenge;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ChallengeOnGoingAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.ChallengeOnGoingInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ChallengeSearchActivity extends BaseActivity {

    @Bind(R.id.et_challenge_search_search)
    EditText mEtChallengeSearchSearch;
    @Bind(R.id.tv_challenge_search_cancel)
    TextView mTvChallengeSearchCancel;
    @Bind(R.id.rv_challenge_search)
    RecyclerView mRvChallengeSearch;
    private List<ChallengeOnGoingInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_challenge_search;
    }

    @Override
    protected void initViewsAndEvents() {
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
        mRvChallengeSearch.setLayoutManager(linearLayoutManager);
        ChallengeOnGoingAdapter challengeOnGoingAdapter = new ChallengeOnGoingAdapter(this, R.layout.item_challenge_on_going, datas);
        mRvChallengeSearch.setAdapter(challengeOnGoingAdapter);

        mRvChallengeSearch.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this, 1),
                getResources().getColor(R.color.colorListDivider)));

        challengeOnGoingAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @OnClick(R.id.tv_challenge_search_cancel)
    public void onClick() {
        finish();
    }
}
