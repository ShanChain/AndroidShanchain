package com.shanchain.arkspot.ui.view.activity.story;

import android.view.View;
import android.widget.TextView;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;

public class ReadModelActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    @Bind(R.id.tb_read_model)
    ArthurToolBar mTbReadModel;
    @Bind(R.id.tv_read_model_title)
    TextView mTvReadModelTitle;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_read_model;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        edit();
    }

    private void edit() {

    }

    private void initToolBar() {
        mTbReadModel.setOnLeftClickListener(this);
        mTbReadModel.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }



}
