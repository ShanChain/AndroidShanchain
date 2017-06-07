package com.shanchain.mvp.view.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class VisibleRangeActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.rb_visible_public)
    RadioButton mRbVisiblePublic;
    @Bind(R.id.ll_visible_public)
    LinearLayout mLlVisiblePublic;
    @Bind(R.id.rb_visible_friends)
    RadioButton mRbVisibleFriends;
    @Bind(R.id.ll_visible_friends)
    LinearLayout mLlVisibleFriends;
    @Bind(R.id.rb_visible_ownself)
    RadioButton mRbVisibleOwnself;
    @Bind(R.id.ll_visible_ownself)
    LinearLayout mLlVisibleOwnself;
    @Bind(R.id.tv_visiable_select_friends)
    TextView mTvVisiableSelectFriends;
    @Bind(R.id.activity_visible_range)
    LinearLayout mActivityVisibleRange;
    private ArthurToolBar mVisibleRangeToolBar;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_visible_range;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mVisibleRangeToolBar = (ArthurToolBar) findViewById(R.id.toolbar_visible_range);
        mVisibleRangeToolBar.setBtnEnabled(true,false);
        mVisibleRangeToolBar.setOnLeftClickListener(this);
    }


    @OnClick({R.id.ll_visible_public, R.id.ll_visible_friends, R.id.ll_visible_ownself, R.id.tv_visiable_select_friends})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_visible_public:
                break;
            case R.id.ll_visible_friends:
                break;
            case R.id.ll_visible_ownself:
                break;
            case R.id.tv_visiable_select_friends:
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
