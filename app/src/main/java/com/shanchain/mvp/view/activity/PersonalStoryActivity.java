package com.shanchain.mvp.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.R;
import com.shanchain.adapter.HotAdapter;
import com.shanchain.base.BaseActivity;
import com.shanchain.mvp.model.PublisherInfo;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;

public class PersonalStoryActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    @Bind(R.id.xrv_personal_story)
    XRecyclerView mXrvPersonalStory;
    @Bind(R.id.activity_personal)
    LinearLayout mActivityPersonal;
    private ArthurToolBar mPersonalStoryToolBar;
    private List<PublisherInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_personal_story;
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
        mXrvPersonalStory.setLayoutManager(linearLayoutManager);
        mXrvPersonalStory.setPullRefreshEnabled(false);
        mXrvPersonalStory.setLoadingMoreEnabled(false);
        HotAdapter hotAdapter = new HotAdapter(this,R.layout.item_hot,datas);
        mXrvPersonalStory.setAdapter(hotAdapter);
    }

    private void initDatas() {
        datas = new ArrayList<>();
        for (int i = 0; i < 24; i ++) {
            PublisherInfo publisherInfo = new PublisherInfo();
            publisherInfo.setName("石家海" + i);
            publisherInfo.setTime("2" + i + "分钟前");
            publisherInfo.setDes("哈哈哈哈哈!");
            if (i == 3) {
                publisherInfo.setStroyImgUrl("");
                publisherInfo.setVoted(true);
            }else {
                publisherInfo.setStroyImgUrl(i+".png");
                publisherInfo.setVoted(false);
            }
            publisherInfo.setLikes(new Random().nextInt(1000));
            publisherInfo.setVote(new Random().nextInt(80));
            publisherInfo.setComments(new Random().nextInt(400));
            publisherInfo.setType(3);
            publisherInfo.setIconUrl(i+"icon.png");
            publisherInfo.setTitle("故事标题" + i);
            publisherInfo.setActiveDes("活动描述信息" + i);
            publisherInfo.setOtherDes("今天有" + i *2 + 3 + "人也在愉快的玩耍");
            datas.add(publisherInfo);
        }
    }

    private void initToolBar() {
        mPersonalStoryToolBar = (ArthurToolBar) findViewById(R.id.toolbar_personal_story);
        mPersonalStoryToolBar.setBtnEnabled(true,false);
        mPersonalStoryToolBar.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
