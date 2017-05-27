package com.shanchain.mvp.view.activity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class FriendsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    ArthurToolBar mToolbarFriends;
    @Bind(R.id.textView)
    TextView mTextView;
    @Bind(R.id.btn_friends_start)
    Button mBtnFriendsStart;
    @Bind(R.id.activity_friends)
    LinearLayout mActivityFriends;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_friends;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mToolbarFriends= (ArthurToolBar) findViewById(R.id.toolbar_friends);
        mToolbarFriends.setOnLeftClickListener(this);
        mToolbarFriends.setImmersive(this, true);
        mToolbarFriends.setBackgroundColor(getResources().getColor(R.color.colorTheme));
    }


    @OnClick(R.id.btn_friends_start)
    public void onClick() {

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
