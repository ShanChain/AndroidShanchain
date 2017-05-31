package com.shanchain.mvp.view.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.widgets.toolBar.ArthurToolBar;

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

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void initViewsAndEvents() {
        initTooBar();
    }

    private void initTooBar() {
        mToolbarAddfriend = (ArthurToolBar) findViewById(R.id.toolbar_addfriend);
        //设置沉浸式
        mToolbarAddfriend.setImmersive(this, true);
        mToolbarAddfriend.setBackgroundColor(getResources().getColor(R.color.colorTheme));
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
