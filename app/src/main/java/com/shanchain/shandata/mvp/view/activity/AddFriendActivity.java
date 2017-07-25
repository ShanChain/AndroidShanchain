package com.shanchain.shandata.mvp.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.AddFriendAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.InterestedPersonInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class AddFriendActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {


    @Bind(R.id.toolbar_addfriend)
    ArthurToolBar mToolbarAddfriend;
    @Bind(R.id.activity_add_friend)
    LinearLayout mActivityAddFriend;
    @Bind(R.id.ll_add_search)
    LinearLayout mLlAddSearch;
    @Bind(R.id.ll_add_contacts)
    LinearLayout mLlAddContacts;
    @Bind(R.id.ll_add_wechat)
    LinearLayout mLlAddWechat;
    @Bind(R.id.iv_add_contacts)
    ImageView mIvAddContacts;
    @Bind(R.id.ll_add_sharecode)
    LinearLayout mLlAddSharecode;
    @Bind(R.id.rv_addfriend)
    RecyclerView mRvAddfriend;
    private List<InterestedPersonInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void initViewsAndEvents() {
        initTooBar();
        initDatas();
        initRecyclerView();
    }

    private void initDatas() {
        if (datas == null) {
            datas = new ArrayList<>();
            for (int i = 0; i < 12; i ++) {
                InterestedPersonInfo interestedPersonInfo = new InterestedPersonInfo();
                interestedPersonInfo.setAvatarUrl("" + i);
                interestedPersonInfo.setNickName("一个学霸" + i);
                interestedPersonInfo.setSignature("有时候,同一件事,我们可以有不同的处理方式");
                interestedPersonInfo.setReason("经常出现在同一个位置");
                datas.add(interestedPersonInfo);
            }

        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvAddfriend.setLayoutManager(linearLayoutManager);
        mRvAddfriend.addItemDecoration(new RecyclerViewDivider(AddFriendActivity.this,
                LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(AddFriendActivity.this,1),
                getResources().getColor(R.color.colorAddFriendDivider)));
        AddFriendAdapter adapter = new AddFriendAdapter(AddFriendActivity.this,R.layout.item_interested_person,datas);
        mRvAddfriend.setAdapter(adapter);
    }

    private void initTooBar() {
        mToolbarAddfriend = (ArthurToolBar) findViewById(R.id.toolbar_addfriend);
        mToolbarAddfriend.setBtnEnabled(true);
        mToolbarAddfriend.setOnLeftClickListener(this);
        mToolbarAddfriend.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }


    @OnClick({R.id.ll_add_search, R.id.ll_add_contacts, R.id.ll_add_wechat, R.id.ll_add_sharecode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_add_search:
                readyGo(SearchNickActivity.class);
                break;
            case R.id.ll_add_contacts:
                readyGo(FriendsActivity.class);
                break;
            case R.id.ll_add_wechat:
                break;
            case R.id.ll_add_sharecode:

                break;
        }
    }
}
