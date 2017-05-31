package com.shanchain.mvp.view.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.utils.LogUtils;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

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

    /** 描述：选择图片的路径集合*/
    private ArrayList<String> images;

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
                //选择图片
                selectImages();
                break;
            case R.id.iv_publish_aite:

                break;
            case R.id.iv_publish_theme:

                break;
            case R.id.iv_publish_expression:

                break;
        }
    }

    private void selectImages() {
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
            //6.权限申请
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == 1){

            }
        }
        pickImages();
    }

    private void pickImages() {
        PhotoPicker.builder()
                .setPhotoCount(9)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    images = new ArrayList<>();
                for (int i = 0; i < photos.size(); i++) {
                    String path = photos.get(i);
                    LogUtils.d(TAG, "onActivityResult: " + path);
                    images.add(path);
                }
            }
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
