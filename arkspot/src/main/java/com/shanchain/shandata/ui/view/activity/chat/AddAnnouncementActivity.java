package com.shanchain.shandata.ui.view.activity.chat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.shandata.ui.model.UpLoadImgBean;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.net.SCHttpCallBack;
import com.shanchain.data.common.net.SCHttpUtils;

import java.util.ArrayList;

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

        if(TextUtils.isEmpty(mImgPath)){
            ToastUtils.showToast(this,"图片为空！");
            return;
        }

        SCHttpUtils.post().url(HttpApi.UP_LOAD_FILE)
                .addParams("num","1")
                .build()
                .execute(new SCHttpCallBack<UpLoadImgBean>(UpLoadImgBean.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("上传图片失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(UpLoadImgBean response, int id) {
                        if (response == null){
                            LogUtils.e("上传图片返回为空");
                            return;
                        }

                        String endPoint = response.getEndPoint();
                        String accessKeyId = response.getAccessKeyId();
                        String accessKeySecret = response.getAccessKeySecret();
                        String securityToken = response.getSecurityToken();
                        String bucket = response.getBucket();
                        String img1 = response.getUuidList().get(0);
                        String host = response.getHost();
                        LogUtils.d("response = "+response.toString());
                        int h  = 120;
                        int w = 100;
                        String imgUrl = img1 + ".jpg";
                        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, securityToken);
                        OSSLog.enableLog();
                        OSS oss = new OSSClient(getApplicationContext(), endPoint, credentialProvider);
                        ossUpLoad(bucket, imgUrl, oss);
                    }
                });
    }

    private void ossUpLoad(String bucket, String imgUrl, OSS oss) {
        PutObjectRequest put = new PutObjectRequest(bucket,imgUrl,mImgPath);
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
                LogUtils.d("objectKey = " + request.getObjectKey());
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
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
