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
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCUploadImgHelper;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.encryption.SCJsonUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.Call;


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
    private String mImgPath;
    private String mGroupId;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_edit_scene;
    }

    @Override
    protected void initViewsAndEvents() {
        mGroupId = getIntent().getStringExtra("groupId");
        if (TextUtils.isEmpty(mGroupId)){
            return;
        }
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
        String name = mEtEditSceneName.getText().toString().trim();
        String des = mEtEditSceneDes.getText().toString().trim();

        if (TextUtils.isEmpty(name)&&TextUtils.isEmpty(des)&&TextUtils.isEmpty(mImgPath)){
            ToastUtils.showToast(mContext,"总得填点东西吧~");
            return;
        }

        if (TextUtils.isEmpty(mImgPath)){
            modify(name,des,"");
        }else {
            uploadImg(name,des);
        }
    }

    private void uploadImg(final String name, final String des) {
        SCUploadImgHelper helper = new SCUploadImgHelper();
        helper.setUploadListener(new SCUploadImgHelper.UploadListener() {
            @Override
            public void onUploadSuc(List<String> urls) {
                modify(name,des,urls.get(0));
            }

            @Override
            public void error() {
                ToastUtils.showToast(mContext,"上传图片失败");
            }
        });
        List<String> src = new ArrayList<>();
        src.add(mImgPath);
        helper.upLoadImg(mContext,src);

    }

    private void modify(String name, String des,String imgUrl) {

        LogUtils.i("图片地址 = " + imgUrl);

        JSONObject obj = new JSONObject();
        if (!TextUtils.isEmpty(name)){
            obj.put("groupname",name);
        }

        if (!TextUtils.isEmpty(des)){
            obj.put("description",des);
        }

        if (!TextUtils.isEmpty(imgUrl)){
            obj.put("icon_url",imgUrl);
        }

        String jStr = obj.toJSONString();

        LogUtils.i("dataString = " + jStr);

        SCHttpUtils.post()
                .url(HttpApi.HX_GROUP_MODIFY)
                .addParams("dataString",jStr)
                .addParams("groupId",mGroupId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("修改群信息失败");
                        e.printStackTrace();
                        ToastUtils.showToast(mContext,"修改群信息失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("修改群信息成功 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                                modifySuc();
                            }else{
                            ToastUtils.showToast(mContext,"修改群信息失败");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(mContext,"修改群信息失败");
                        }
                    }
                });

    }

    private void modifySuc() {
        ToastUtils.showToast(mContext,"修改成功");
        finish();
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
                GlideUtils.load(mContext,mImgPath,mIvEditSceneImg,0);
            } else {
                LogUtils.d("没有图片返回啊");
            }
        } else {
            LogUtils.d("返回码错误");
        }
    }

}
