package com.shanchain.shandata.mvp.view.activity.mine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.PrefUtils;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.ClearEditText;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;


public class PersonalDataActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    private static final int RESULT_CODE = 200;
    private static final int REQUEST_CODE_BG = 10;
    ArthurToolBar mToolbarMinePersonalData;
    @Bind(R.id.iv_personal_data_avatar)
    ImageView mIvPersonalDataAvatar;
    @Bind(R.id.ll_personal_data_avatar)
    LinearLayout mLlPersonalDataAvatar;
    @Bind(R.id.textView5)
    TextView mTextView5;
    @Bind(R.id.iv_personal_data_bg)
    ImageView mIvPersonalDataBg;
    @Bind(R.id.ll_personal_data_bg)
    LinearLayout mLlPersonalDataBg;
    @Bind(R.id.et_personal_data_nick)
    ClearEditText mEtPersonalDataNick;

    @Bind(R.id.et_personal_data_signature)
    ClearEditText mEtPersonalDataSignature;
    @Bind(R.id.activity_personal_data)
    LinearLayout mActivityPersonalData;
    @Bind(R.id.tv_personal_data_sex)
    TextView mTvPersonalDataSex;
    @Bind(R.id.tv_personal_data_birth)
    TextView mTvPersonalDataBirth;
    @Bind(R.id.ll_personal_data_sex)
    LinearLayout mLlPersonalDataSex;
    @Bind(R.id.ll_personal_data_birth)
    LinearLayout mLlPersonalDataBirth;
    private String mAvatarPath;
    TimePickerView pvTime;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_personal_data;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initData() {
        int bgPath = PrefUtils.getInt(this, "bgPath", R.drawable.mine_bg_spring_default);
        Glide.with(this)
                .load(bgPath)
                .into(mIvPersonalDataBg);

        String sex = PrefUtils.getString(this, "sex", "");
        mTvPersonalDataSex.setText(sex);

        String nickname = PrefUtils.getString(this, "nickname", "");
        mEtPersonalDataNick.setText(nickname);

        String signature = PrefUtils.getString(this, "signature", "");
        mEtPersonalDataSignature.setText(signature);
    }

    private void initToolBar() {
        mToolbarMinePersonalData = (ArthurToolBar) findViewById(R.id.toolbar_mine_personal_data);
        mToolbarMinePersonalData.setBtnVisibility(true);
        mToolbarMinePersonalData.setBtnEnabled(true);
        mToolbarMinePersonalData.setOnLeftClickListener(this);
        mToolbarMinePersonalData.setOnRightClickListener(this);
    }


    @OnClick({R.id.ll_personal_data_sex, R.id.ll_personal_data_birth,R.id.ll_personal_data_avatar, R.id.ll_personal_data_bg, R.id.tv_personal_data_sex, R.id.tv_personal_data_birth})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_personal_data_avatar:
                selectImages();
                break;
            case R.id.ll_personal_data_bg:
                selectBg();
                break;

            case R.id.ll_personal_data_sex:
                selectSex();
                break;
            case R.id.ll_personal_data_birth:
                selectBirth();
                break;
        }
    }

    private void selectBirth() {

    }

    private void selectSex() {
        CustomDialog customDialog = new CustomDialog(this, true, true, 0.95, R.layout.details_personal_sex, new int[]{R.id.tv_dialog_male, R.id.tv_dialog_female, R.id.tv_dialog_secret, R.id.tv_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_dialog_male:
                        PrefUtils.putString(PersonalDataActivity.this,"sex","男");
                        mTvPersonalDataSex.setText("男");
                        break;
                    case R.id.tv_dialog_female:
                        PrefUtils.putString(PersonalDataActivity.this,"sex","女");
                        mTvPersonalDataSex.setText("女");
                        break;
                    case R.id.tv_dialog_secret:
                        PrefUtils.putString(PersonalDataActivity.this,"sex","保密");
                        mTvPersonalDataSex.setText("保密");
                        break;
                    case R.id.tv_dialog_cancel:
                        break;

                }
            }
        });
        customDialog.show();
    }

    private void selectBg() {
        Intent intent = new Intent(this, BackgroundActivity.class);
        startActivityForResult(intent, REQUEST_CODE_BG);
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
                    ToastUtils.showToast(this, "未授权");
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
                mAvatarPath = photos.get(0);
                if (mAvatarPath != null) {
                    PrefUtils.putString(this, "avatarPath", mAvatarPath);
                    Glide.with(this)
                            .load(mAvatarPath)
                            .transform(new GlideCircleTransform(this))
                            .into(mIvPersonalDataAvatar);
                }
            }
        } else if (requestCode == REQUEST_CODE_BG) {
            if (data != null) {
                int bg = data.getIntExtra("bg", 0);
                if (bg != 0) {
                    mIvPersonalDataBg.setImageResource(bg);
                }
            }
        }
    }


    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

        String nickName = mEtPersonalDataNick.getText().toString().trim();
        PrefUtils.putString(this,"nickname",nickName);
        String signature = mEtPersonalDataSignature.getText().toString().trim();
        PrefUtils.putString(this,"signature",signature);
        Intent intent = new Intent();
        intent.putExtra("signature",signature);
        intent.putExtra("nickname",nickName);
        intent.putExtra("avatarPath", mAvatarPath);
        intent.putExtra("bgPath",PrefUtils.getInt(this, "bgPath", R.drawable.mine_bg_spring_default));
        setResult(RESULT_CODE, intent);
        finish();
    }

}
