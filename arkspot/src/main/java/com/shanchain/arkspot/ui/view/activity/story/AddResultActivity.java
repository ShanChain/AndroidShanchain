package com.shanchain.arkspot.ui.view.activity.story;

import android.content.Intent;
import android.view.View;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.manager.ActivityManager;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;

public class AddResultActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener, ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_add_new_role)
    ArthurToolBar mTbAddNewRole;
    private boolean mIsRole;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_add_result;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        mIsRole = intent.getBooleanExtra("isRole", false);
        initToolBar();
    }

    private void initToolBar() {
        if (mIsRole) {
            mTbAddNewRole.setTitleText("添加新角色");
        } else {
            mTbAddNewRole.setTitleText("添加新时空");
        }
        mTbAddNewRole.setOnRightClickListener(this);
        mTbAddNewRole.setOnLeftClickListener(this);
    }

    @Override
    public void onRightClick(View v) {
        ActivityManager.getInstance().finishActivity(AddNewSpaceActivity.class);
        ActivityManager.getInstance().finishActivity(AddRoleActivity.class);
        ActivityManager.getInstance().finishActivity(SearchRoleActivity.class);
        finish();
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
