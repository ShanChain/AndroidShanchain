package com.shanchain.mvp.view.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class ReleaseDynamicActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener, ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarReleaseDynamic;
    @Bind(R.id.et_publish_content)
    EditText mEtPublishContent;
    @Bind(R.id.ll_publish_position)
    LinearLayout mLlPublishPosition;
    @Bind(R.id.tv_public)
    TextView mTvPublic;
    @Bind(R.id.iv_publish_image)
    ImageView mIvPublishImage;
    @Bind(R.id.iv_publish_aite)
    ImageView mIvPublishAite;
    @Bind(R.id.iv_publish_theme)
    ImageView mIvPublishTheme;
    @Bind(R.id.iv_publish_expression)
    ImageView mIvPublishExpression;
    @Bind(R.id.activity_release_dynamic)
    LinearLayout mActivityReleaseDynamic;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_release_dynamic;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mToolbarReleaseDynamic = (ArthurToolBar) findViewById(R.id.toolbar_release_dynamic);
        mToolbarReleaseDynamic.setOnLeftClickListener(this);
        mToolbarReleaseDynamic.setOnRightClickListener(this);
    }


    @OnClick({R.id.ll_publish_position, R.id.tv_public, R.id.iv_publish_image, R.id.iv_publish_aite, R.id.iv_publish_theme, R.id.iv_publish_expression})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_publish_position:

                break;
            case R.id.tv_public:

                break;
            case R.id.iv_publish_image:

                break;
            case R.id.iv_publish_aite:

                break;
            case R.id.iv_publish_theme:

                break;
            case R.id.iv_publish_expression:

                break;
        }
    }

    @Override
    public void onRightClick(View v) {

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
