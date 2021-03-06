package com.shanchain.shandata.ui.view.activity.chat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCUploadImgHelper;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.AnnouncementContent;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.Call;


public class AddAnnouncementActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    @Bind(R.id.tb_add_announcement)
    ArthurToolBar mTbAddAnnouncement;
    @Bind(R.id.et_add_announcement_title)
    EditText mEtAddAnnouncementTitle;
    @Bind(R.id.et_add_announcement_content)
    EditText mEtAddAnnouncementContent;
    @Bind(R.id.iv_add_announcement_img)
    ImageView mIvAddAnnouncementImg;
    @Bind(R.id.iv_add_announcement_delete)
    ImageView mIvAddAnnouncementDelete;
    @Bind(R.id.rl_add_announcement_img)
    RelativeLayout mRlAddAnnouncementImg;
    @Bind(R.id.iv_add_announcement_add)
    ImageView mIvAddAnnouncementAdd;
    private String mImgPath;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_add_announcement;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        //隐藏回显图片
        mRlAddAnnouncementImg.setVisibility(View.GONE);
    }

    private void initToolBar() {
        mTbAddAnnouncement.setOnLeftClickListener(this);
        mTbAddAnnouncement.setOnRightClickListener(this);
    }

    @OnClick({R.id.iv_add_announcement_delete, R.id.iv_add_announcement_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_announcement_delete:
                //删除图片
                mImgPath = "";
                mRlAddAnnouncementImg.setVisibility(View.GONE);
                break;
            case R.id.iv_add_announcement_add:
                //添加图片
                selectImage();
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        String title = mEtAddAnnouncementTitle.getText().toString().trim();
        String content = mEtAddAnnouncementContent.getText().toString().trim();

        if (TextUtils.isEmpty(title)){
            ToastUtils.showToast(this,"公告标题不能为空！");
            return;
        }

        if (TextUtils.isEmpty(content)){
            ToastUtils.showToast(this,"公告内容不能为空！");
            return;
        }

        //提交公告消息
        showLoadingDialog(false);
        if (TextUtils.isEmpty(mImgPath)){   //无图片
            AnnouncementContent announcementContent = new AnnouncementContent();
            announcementContent.setImg("");
            announcementContent.setInfo(content);
            String jContent = JSONObject.toJSONString(announcementContent);
            releaseAnnouncement(title,jContent);
        }else { //有图片
            uploadImg(title,content);
        }


    }

    private void releaseAnnouncement(String title, final String content) {
        SCHttpUtils.postWithSpaceAndChaId()
                .url(HttpApi.SPACE_ANNO_CREATE)
                .addParams("content",content)
                .addParams("title",title)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeLoadingDialog();
                        LogUtils.i("添加公告失败");
                        e.printStackTrace();
                        ToastUtils.showToast(mContext,"添加公告失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        closeLoadingDialog();
                        try {
                            LogUtils.i("公告信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                                ToastUtils.showToast(mContext,"添加公告成功");
                                finish();
                            }else{
                                ToastUtils.showToast(mContext,"添加公告失败");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(mContext,"添加公告失败");
                        }
                    }
                });
    }

    private void uploadImg(final String title, final String content) {
        SCUploadImgHelper helper = new SCUploadImgHelper();
        helper.setUploadListener(new SCUploadImgHelper.UploadListener() {
            @Override
            public void onUploadSuc(List<String> urls) {
                String imgUrl = urls.get(0);
                AnnouncementContent announcementContent = new AnnouncementContent();
                announcementContent.setImg(imgUrl);
                announcementContent.setInfo(content);
                String jContent = JSONObject.toJSONString(announcementContent);
                releaseAnnouncement(title,jContent);
            }

            @Override
            public void error() {
                closeLoadingDialog();
                ToastUtils.showToast(mContext,"上传图片失败");
            }
        });
        List<String> src = new ArrayList<>();
        src.add(mImgPath);
        helper.upLoadImg(mContext,src);
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
                mRlAddAnnouncementImg.setVisibility(View.VISIBLE);

                LogUtils.d("选择的图片地址 ：" + mImgPath);

                Glide.with(this).load(mImgPath).into(mIvAddAnnouncementImg);
            }else {
                LogUtils.d("没有图片返回啊");
            }
        }else {
            LogUtils.d("返回码错误");
        }
    }

}
