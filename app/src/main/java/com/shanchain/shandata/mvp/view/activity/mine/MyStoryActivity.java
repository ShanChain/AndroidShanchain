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

public class MyStoryActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    ArthurToolBar mToolbarMyStory;
    @Bind(R.id.xrv_my_story)
    XRecyclerView mXrvMyStory;
    @Bind(R.id.activity_my_story)
    LinearLayout mActivityMyStory;
    private List<MyStoryInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_story;
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
        mXrvMyStory.setLayoutManager(linearLayoutManager);

        mXrvMyStory.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this,1),
                getResources().getColor(R.color.colorListDivider)));

        mXrvMyStory.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXrvMyStory.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        MyStorysAdapter myStorysAdapter = new MyStorysAdapter(this,R.layout.item_my_story,datas);
        mXrvMyStory.setLoadingMoreEnabled(false);
        mXrvMyStory.setAdapter(myStorysAdapter);
        mXrvMyStory.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mXrvMyStory.refreshComplete();
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
            storyInfo.setStory("生命力");
            storyInfo.setTime("6月20日17:00");
            storyInfo.setDes("支持了“绿色”路线，3赞成，1反对。获得了很多的奖项");
            datas.add(storyInfo);
        }

    }

    private void initToolBar() {
        mToolbarMyStory = (ArthurToolBar) findViewById(R.id.toolbar_my_story);
        mToolbarMyStory.setBtnEnabled(true, false);
        mToolbarMyStory.setBtnVisibility(true, false);
        mToolbarMyStory.setOnLeftClickListener(this);
    }


    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
