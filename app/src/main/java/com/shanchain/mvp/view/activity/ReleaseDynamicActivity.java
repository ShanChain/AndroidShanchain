package com.shanchain.mvp.view.activity;

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

import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.mvp.model.ContactInfo;
import com.shanchain.mvp.model.PositionInfo;
import com.shanchain.mvp.model.TopicInfo;
import com.shanchain.utils.LogUtils;
import com.shanchain.utils.ToastUtils;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

public class ReleaseDynamicActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener, ArthurToolBar.OnLeftClickListener {


    ArthurToolBar mToolbarReleaseDynamic;
    @Bind(R.id.et_publish_content)
    EditText mEtPublishContent;
    @Bind(R.id.tv_publish_position)
    TextView mLlPublishPosition;
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

    /**
     * 描述：选择图片的路径集合
     */
    private ArrayList<String> images;
    private static final int POSITION_REQUSET_CODE = 10;
    private static final int VISIABLE_RANGE_CODE = 20;
    private static final int TOPIC_REQUEST_CODE = 30;
    private static final int CHOOSE_CONTACTS_REQUESTCODE = 40;
    private static final int AITE_CONTACTS_REQUESTCODE = 50;

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


    @OnClick({R.id.tv_publish_position, R.id.tv_public, R.id.iv_publish_image, R.id.iv_publish_aite, R.id.iv_publish_theme, R.id.iv_publish_expression})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_publish_position:
                //获取位置信息
                selectPosition();

                break;
            case R.id.tv_public:
                selectVisibleRange();
                break;
            case R.id.iv_publish_image:
                //选择图片
                selectImages();
                break;
            case R.id.iv_publish_aite:
                readyGoForResult(AiteContactsActivity.class, AITE_CONTACTS_REQUESTCODE);
                break;
            case R.id.iv_publish_theme:
                selectTopic();
                break;
            case R.id.iv_publish_expression:

                break;
        }
    }

    private void selectTopic() {
        readyGoForResult(TopicActivity.class, TOPIC_REQUEST_CODE);
    }

    private void selectVisibleRange() {
        readyGoForResult(VisibleRangeActivity.class, VISIABLE_RANGE_CODE);
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

    private void getPosition() {
        readyGoForResult(PositionActivity.class, POSITION_REQUSET_CODE);
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
                .setPhotoCount(9)
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
                    ToastUtils.showToast(ReleaseDynamicActivity.this, "未授权");
                }
                break;
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //授权了
                    getPosition();
                } else {
                    ToastUtils.showToast(ReleaseDynamicActivity.this, "未授权");
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
                images = new ArrayList<>();
                for (int i = 0; i < photos.size(); i++) {
                    String path = photos.get(i);
                    LogUtils.d(TAG, "onActivityResult: " + path);
                    images.add(path);
                }
            }
        } else if (requestCode == POSITION_REQUSET_CODE) {
            if (data != null) {
                PositionInfo positionInfo = (PositionInfo) data.getSerializableExtra("positionInfo");
                LogUtils.d(positionInfo.getAddress());
                mLlPublishPosition.setText(positionInfo.getAddress());
            }
        } else if (requestCode == VISIABLE_RANGE_CODE) {
            if (data != null) {
                String visible = data.getStringExtra("visible");
                LogUtils.d(visible);
                mTvPublic.setText(visible);
            }
        } else if (requestCode == TOPIC_REQUEST_CODE) {
            if (data != null){
                TopicInfo topicInfo = (TopicInfo) data.getSerializableExtra("topicReturn");
                String topic = topicInfo.getTopic();
                LogUtils.d(topic);

            }
        } else if (requestCode == CHOOSE_CONTACTS_REQUESTCODE) {

        } else if (requestCode == AITE_CONTACTS_REQUESTCODE) {
            if (data != null) {
                ContactInfo contactInfo =
                        (ContactInfo) data.getSerializableExtra("aiteReturn");
                LogUtils.d(contactInfo.getName());
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
