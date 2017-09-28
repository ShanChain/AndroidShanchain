package com.shanchain.arkspot.ui.view.activity.chat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.BitmapUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;


public class EditSceneActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    @Bind(R.id.tb_edit_scene)
    ArthurToolBar mTbEditScene;
    @Bind(R.id.iv_edit_scene_img)
    ImageView mIvEditSceneImg;
    @Bind(R.id.rl_edit_scene_img)
    RelativeLayout mRlEditSceneImg;
    @Bind(R.id.et_edit_scene_name)
    EditText mEtEditSceneName;
    @Bind(R.id.et_edit_scene_des)
    EditText mEtEditSceneDes;
    @Bind(R.id.iv_img1)
    ImageView mIvImg1;
    @Bind(R.id.iv_img2)
    ImageView mIvImg2;
    private String mImgPath;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_edit_scene;
    }

    @Override
    protected void initViewsAndEvents() {
        mTbEditScene.setOnLeftClickListener(this);
        mTbEditScene.setOnRightClickListener(this);
    }

    @OnClick(R.id.rl_edit_scene_img)
    public void onClick() {
        selectImage();
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }

    private void selectImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //6.0权限申请
            LogUtils.d("版本6.0");
            int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                LogUtils.d("未申请权限,正在申请");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            } else {
                LogUtils.d("已经申请权限");
                pickImages();
            }
        } else {
            LogUtils.d("版本低于6.0");
            pickImages();
        }
    }

    private void pickImages() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //授权了
                pickImages();
            } else {
                ToastUtils.showToast(this, "未授权");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                mImgPath = photos.get(0);

                LogUtils.d("选择的图片地址 ：" + mImgPath);

                Glide.with(this).load(mImgPath).into(mIvImg1);

                File file = new File(mImgPath);
                long length = file.length();

                Bitmap bitmap = BitmapUtils.getBitmap(mImgPath);

                int byteCount1 = bitmap.getByteCount();

                LogUtils.d("压缩前图片大小" + length + "，======" + byteCount1 + "，图片宽度" + bitmap.getWidth() + "，图片高度" + bitmap.getHeight());

                //压缩图片
                Bitmap image = BitmapUtils.getCompURLImage(mImgPath, 500, 500);

                mIvEditSceneImg.setImageBitmap(bitmap);

                int byteCount = image.getByteCount();

                LogUtils.d("压缩后图片大小" + byteCount + "，图片宽度" + image.getWidth() + "，图片高度" + image.getHeight());

                String tempFileName = BitmapUtils.getTempFileName();
                try {
                    File file1 = BitmapUtils.saveFile(image, getCacheDir().getPath(), tempFileName);
                    String filePath = file1.getPath();

                    LogUtils.d("新图片的文件路径" + filePath);

                    LogUtils.d("新图片的大小" + file1.length());

                    Glide.with(mContext).load(filePath).into(mIvImg2);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                LogUtils.d("没有图片返回啊");
            }
        } else {
            LogUtils.d("返回码错误");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
