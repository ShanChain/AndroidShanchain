package com.shanchain.shandata.mvp.view.activity.mine;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MyStorysAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.MyStoryInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MyChallengeActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarMyChallenge;
    @Bind(R.id.xrv_my_challenge)
    XRecyclerView mXrvMyChallenge;
    @Bind(R.id.activity_my_challenge)
    LinearLayout mActivityMyChallenge;
    private List<MyStoryInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_challenge;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXrvMyChallenge.setLayoutManager(linearLayoutManager);

        mXrvMyChallenge.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this,1),
                getResources().getColor(R.color.colorListDivider)));

        mXrvMyChallenge.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXrvMyChallenge.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        MyStorysAdapter myStorysAdapter = new MyStorysAdapter(this,R.layout.item_my_story,datas);
        mXrvMyChallenge.setLoadingMoreEnabled(false);
        mXrvMyChallenge.setAdapter(myStorysAdapter);
        mXrvMyChallenge.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mXrvMyChallenge.refreshComplete();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
            }
        });
    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 12; i ++) {
            MyStoryInfo storyInfo = new MyStoryInfo();
            storyInfo.setStory("开心点");
            storyInfo.setTime("6月20日17:00");
            storyInfo.setDes("2小时6666步，成功，善圆+2，善券+30");
            datas.add(storyInfo);
        }
    }

    private void initToolBar() {
        mToolbarMyChallenge = (ArthurToolBar) findViewById(R.id.toolbar_my_challenge);
        mToolbarMyChallenge.setBtnEnabled(true,false);
        mToolbarMyChallenge.setBtnVisibility(true,false);
        mToolbarMyChallenge.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
