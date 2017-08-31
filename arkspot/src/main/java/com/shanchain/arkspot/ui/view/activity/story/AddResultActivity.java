package com.shanchain.arkspot.ui.view.activity.story;

import android.view.View;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;

public class AddResultActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener, ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_add_new_role)
    ArthurToolBar mTbAddNewRole;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_add_result;
    }

    @Override
    protected void initViewsAndEvents() {
        mTbAddNewRole.setOnRightClickListener(this);
        mTbAddNewRole.setOnLeftClickListener(this);
    }

    @Override
    public void onRightClick(View v) {

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
