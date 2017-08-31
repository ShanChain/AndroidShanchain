package com.shanchain.arkspot.ui.view.activity.story;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;

public class BoxActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    private static final int RESULT_CODE_BOX = 100;
    @Bind(R.id.tb_box)
    ArthurToolBar mTbBox;
    @Bind(R.id.et_box)
    EditText mEtBox;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_box;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mTbBox.setOnLeftClickListener(this);
        mTbBox.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        String content = mEtBox.getText().toString().trim();
        if (TextUtils.isEmpty(content)){
            finish();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("box",content);
        setResult(RESULT_CODE_BOX,intent);
        finish();
    }
}
