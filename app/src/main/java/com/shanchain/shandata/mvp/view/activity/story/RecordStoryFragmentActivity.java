package com.shanchain.shandata.mvp.view.activity.story;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.manager.ActivityManager;
import com.shanchain.shandata.mvp.model.PositionInfo;
import com.shanchain.shandata.mvp.view.activity.PositionActivity;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

public class RecordStoryFragmentActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    private static final int POSITION_REQUEST_CODE = 100;
    private static final int DATE_REQUEST_CODE = 200;
    ArthurToolBar mToolbarRecordStoryFragment;
    @Bind(R.id.tv_record_story_fragment_date)
    TextView mTvRecordStoryFragmentDate;
    @Bind(R.id.tv_record_story_fragment_position)
    TextView mTvRecordStoryFragmentPosition;
    @Bind(R.id.tv_record_story_fragment_time)
    TextView mTvRecordStoryFragmentTime;
    @Bind(R.id.iv_record_story_fragment_camera)
    ImageView mIvRecordStoryFragmentCamera;
    @Bind(R.id.ll_record_story_fragment_camera)
    LinearLayout mLlRecordStoryFragmentCamera;
    @Bind(R.id.iv_record_story_fragment_img)
    ImageView mIvRecordStoryFragmentImg;
    @Bind(R.id.et_record_story_fragment_data)
    EditText mEtRecordStoryFragmentData;
    @Bind(R.id.activity_record_story_fragment)
    LinearLayout mActivityRecordStoryFragment;
    @Bind(R.id.et_record_story_fragment_content)
    EditText mEtRecordStoryFragmentContent;

    private String photoImg;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_record_story_fragment;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initToolBar() {
        mToolbarRecordStoryFragment = (ArthurToolBar) findViewById(R.id.toolbar_record_story_fragment);
        mToolbarRecordStoryFragment.setBtnEnabled(true);
        mToolbarRecordStoryFragment.setOnLeftClickListener(this);
        mToolbarRecordStoryFragment.setOnRightClickListener(this);
    }

    private void initData() {

    }

    @OnClick({R.id.tv_record_story_fragment_date, R.id.tv_record_story_fragment_position, R.id.tv_record_story_fragment_time, R.id.iv_record_story_fragment_camera})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_record_story_fragment_date:
                //选择时间和日期
                Intent intentDate = new Intent(this,SelectTimeActivity.class);
                startActivityForResult(intentDate,DATE_REQUEST_CODE);
                break;
            case R.id.tv_record_story_fragment_position:
                //选择地点
                selectPosition();
                break;
            case R.id.tv_record_story_fragment_time:
                //选择时间和日期
                Intent intentTime = new Intent(this,SelectTimeActivity.class);
                startActivityForResult(intentTime,DATE_REQUEST_CODE);
                break;
            case R.id.iv_record_story_fragment_camera:
                //选择图片
                selectImages();
                break;
        }
    }

    private void getPosition() {
        readyGoForResult(PositionActivity.class, POSITION_REQUEST_CODE);
    }

    private void selectPosition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //6.0权限申请
            LogUtils.d("版本6.0");
            int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                LogUtils.d("未申请权限,正在申请");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
            } else {
                LogUtils.d("已经申请权限");
                getPosition();
            }
        } else {
            LogUtils.d("版本低于6.0");
            getPosition();
        }

    }

    private void selectImages() {
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
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //授权了
                    pickImages();
                } else {
                    ToastUtils.showToast(RecordStoryFragmentActivity.this, "未授权");
                }
                break;
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //授权了
                    getPosition();
                } else {
                    ToastUtils.showToast(RecordStoryFragmentActivity.this, "未授权");
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                photoImg = photos.get(0);
                mLlRecordStoryFragmentCamera.setVisibility(View.GONE);
                mIvRecordStoryFragmentImg.setVisibility(View.VISIBLE);
                Glide.with(this).load(photoImg).into(mIvRecordStoryFragmentImg);
            }
        } else if (requestCode == POSITION_REQUEST_CODE) {
            if (data != null) {
                PositionInfo positionInfo = (PositionInfo) data.getSerializableExtra("positionInfo");
                LogUtils.d(positionInfo.getAddress());
                mTvRecordStoryFragmentPosition.setText(positionInfo.getAddress());
            }

        }else if (requestCode == DATE_REQUEST_CODE){
            if (data != null) {
                String date = data.getStringExtra("date");
                String startTime = data.getStringExtra("startTime");
                String endTime = data.getStringExtra("endTime");
                mTvRecordStoryFragmentDate.setText(date);
                mTvRecordStoryFragmentTime.setText(startTime + "~" + endTime);
            }
        }
    }


    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        //上传图片

        //位置信息
        String position = mTvRecordStoryFragmentPosition.getText().toString();
        //时间日期
        String date = mTvRecordStoryFragmentDate.getText().toString();
        String time = mTvRecordStoryFragmentTime.getText().toString();
        //输入的数据
        String data = mEtRecordStoryFragmentData.getText().toString();
        //输入的文字内容
        String content = mEtRecordStoryFragmentContent.getText().toString();




        readyGo(ShareStoryActivity.class);

    }

}
