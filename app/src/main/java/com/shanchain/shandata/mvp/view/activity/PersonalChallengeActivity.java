package com.shanchain.shandata.mvp.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.HotAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.PublisherInfo;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;

public class PersonalChallengeActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    @Bind(R.id.xrv_personal_challenge)
    XRecyclerView mXrvPersonalChallenge;
    @Bind(R.id.activity_personal_challenge)
    LinearLayout mActivityPersonalChallenge;

    private ArthurToolBar mPersonalChallengeToolBar;
    private List<PublisherInfo> datas;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_personal_challenge;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initDatas();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXrvPersonalChallenge.setLayoutManager(linearLayoutManager);
        mXrvPersonalChallenge.setPullRefreshEnabled(false);
        mXrvPersonalChallenge.setLoadingMoreEnabled(false);
        HotAdapter hotAdapter = new HotAdapter(this,R.layout.item_hot,datas);
        mXrvPersonalChallenge.setAdapter(hotAdapter);
        hotAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                readyGo(DetailsActivity.class);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initDatas() {
        datas = new ArrayList<>();
        for (int i = 0; i < 16; i ++) {
            PublisherInfo publisherInfo = new PublisherInfo();
            publisherInfo.setType(2);
            publisherInfo.setName("李江宇" + i);
            publisherInfo.setTime("3"+i + "分钟前");
            publisherInfo.setDes("#善数者正式发布#" + i+"我自定义了一个挑战任务,要不要和我一起来?");
            publisherInfo.setIconUrl(i+".png");
            publisherInfo.setTitle("寻找神秘点" );
            publisherInfo.setLikes(new Random().nextInt(1000));

            publisherInfo.setComments(new Random().nextInt(400));
            publisherInfo.setChallegeTime("5月" + i + "日");
            publisherInfo.setAddr("深圳市");
            publisherInfo.setActiveDes("假期放松一下,率先找到神秘地点打卡者获胜。");
            publisherInfo.setOtherDes("一起来试试吧");
            datas.add(publisherInfo);
        }
    }

    private void initToolBar() {
        mPersonalChallengeToolBar = (ArthurToolBar) findViewById(R.id.toolbar_personal_challenge);
        mPersonalChallengeToolBar.setBtnEnabled(true, false);
        mPersonalChallengeToolBar.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
