package com.shanchain.shandata.mvp.view.activity.challenge;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.view.activity.MainActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

public class HappierCompleteActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarHappierComplete;
    @Bind(R.id.tv_happier_complete_word)
    TextView mTvHappierCompleteWord;
    @Bind(R.id.iv_happier_complete_img)
    ImageView mIvHappierCompleteImg;
    @Bind(R.id.btn_happier_complete_release)
    Button mBtnHappierCompleteRelease;
    private String mImgPath;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_happier_complete;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        mImgPath = intent.getStringExtra("imgPath");
        File file = new File(mImgPath);
        Glide.with(mContext).load(file).into(mIvHappierCompleteImg);
        initToolBar();


    }

    private void initToolBar() {
        mToolbarHappierComplete = (ArthurToolBar) findViewById(R.id.toolbar_happier_complete);
        mToolbarHappierComplete.setBtnVisibility(true,false);
        mToolbarHappierComplete.setBtnEnabled(true,false);
        mToolbarHappierComplete.setOnLeftClickListener(this);
    }


    @OnClick(R.id.btn_happier_complete_release)
    public void onClick() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("fragmentId",1);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
