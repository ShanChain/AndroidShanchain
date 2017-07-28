package com.shanchain.shandata.mvp.view.activity.story;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;

public class ProtagonistActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    ArthurToolBar mToolbarProtagonist;
    @Bind(R.id.et_protagonist_name)
    EditText mEtProtagonistName;
    @Bind(R.id.et_protagonist_des)
    EditText mEtProtagonistDes;
    @Bind(R.id.activity_protagonist)
    LinearLayout mActivityProtagonist;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_protagonist;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mToolbarProtagonist = (ArthurToolBar) findViewById(R.id.toolbar_protagonist);
        mToolbarProtagonist.setBtnEnabled(true);
        mToolbarProtagonist.setBtnVisibility(true);
        mToolbarProtagonist.setOnLeftClickListener(this);
        mToolbarProtagonist.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        readyGo(ParticipantsInfoActivity.class);
    }
}
