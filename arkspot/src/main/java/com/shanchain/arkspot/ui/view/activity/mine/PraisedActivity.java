package com.shanchain.arkspot.ui.view.activity.mine;

import android.view.View;
import android.widget.FrameLayout;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;


public class PraisedActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_praised)
    ArthurToolBar mTbPraised;
    @Bind(R.id.fl_praised_container)
    FrameLayout mFlPraisedContainer;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_praised;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initData() {

    }

    private void initToolBar() {
        mTbPraised.setBtnEnabled(true,false);
        mTbPraised.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
