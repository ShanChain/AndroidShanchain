package com.shanchain.data.common.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanchain.common.R;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.BitmapUtils;
import com.shanchain.data.common.utils.ImageUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SetWalletPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    ArthurToolBar tbWallet;
    TextView tvSelectPictureHint;
    ImageView ivSelectPicture;
    Button btnCreateWalletPassword;
    RelativeLayout relativeCreate;
    TextView tvSelectPictureTitle;
    ImageView ivPasswordPicture;
    Button btnSaveWalletPassword;
    TextView tvSafetyTips;
    Button btnNextStep;
    RelativeLayout relativeSavePasswordImage;
    private File file;
    private String photoPath, filename;
    private byte[] mResultBytes;
    private CustomDialog mCustomDialog;
    private ProgressDialog mDialog;
    public static final int WALLET_PHOTO = 5;
    public static final int INTENT_TAKE = 3;
    private Bitmap mSignBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wallet_password);
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
        ButterKnife.bind(this);
        tbWallet = findViewById(R.id.tb_wallet);
        tvSelectPictureHint = findViewById(R.id.tv_select_picture_hint);
        ivSelectPicture = findViewById(R.id.iv_select_picture);
        btnCreateWalletPassword = findViewById(R.id.btn_create_wallet_password);
        relativeCreate = findViewById(R.id.relative_create);
        tvSelectPictureTitle = findViewById(R.id.tv_select_picture_title);
        ivPasswordPicture = findViewById(R.id.iv_password_picture);
        btnSaveWalletPassword = findViewById(R.id.btn_save_wallet_password);
        tvSafetyTips = findViewById(R.id.tv_safety_tips);
        btnNextStep = findViewById(R.id.btn_next_step);
        relativeSavePasswordImage = findViewById(R.id.relative_save_password_image);
        ivSelectPicture.setOnClickListener(this);
        btnCreateWalletPassword.setOnClickListener(this);
        btnSaveWalletPassword.setOnClickListener(this);
        btnNextStep.setOnClickListener(this);
        initToolBar();
    }

    private void initToolBar() {
        tbWallet.setTitleText("设置钱包密码");
//        tbWallet.setRightText("提示");
        tbWallet.setTitleTextColor(getResources().getColor(R.color.colorTextDefault));
        tbWallet.setRightTextColor(getResources().getColor(R.color.colorViolet));
        tbWallet.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        tbWallet.setOnLeftClickListener(new ArthurToolBar.OnLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                if (!relativeSavePasswordImage.isShown()) {
                    try {
                        Class clazz = Class.forName("com.shanchain.shandata.rn.activity.SCWebViewActivity");
                        ActivityStackManager.getInstance().finishActivity(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    finish();
                } else {
                    tbWallet.setLeftImage(0);
                    ToastUtils.showToast(SetWalletPasswordActivity.this, "请保存图片");
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_select_picture) {
            final CustomDialog bottomDialog = new CustomDialog(SetWalletPasswordActivity.this, true, 1.0, R.layout.common_dialog_bottom_take_photos, new int[]{R.id.tv_dialog_take_picture, R.id.tv_dialog_photos});
            bottomDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                @Override
                public void OnItemClick(CustomDialog dialog, View view) {
                    int i1 = view.getId();//拍照

                    if (i1 == R.id.tv_dialog_take_picture) {
                        if (ContextCompat.checkSelfPermission(SetWalletPasswordActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(SetWalletPasswordActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.Images.Media.MIME_TYPE, "image/*");
                            String path = ImageUtils.getTempFileName();
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, path);
                            Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                            Uri imageUri = FileProvider.getUriForFile(SetWalletPasswordActivity.this, getApplication().getPackageName() + ".fileProvider", file);
//                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            startActivityForResult(intent, INTENT_TAKE);
                            bottomDialog.dismiss();
                        }
                    } else if (i1 == R.id.tv_dialog_photos) {//打开相册
                        selectImage();
                        bottomDialog.dismiss();
                    }
                }
            });
            bottomDialog.show();

        } else if (i == R.id.btn_create_wallet_password) {
            if (photoPath == null && mSignBitmap == null) {
                return;
            }
            StandardDialog standardDialog = new StandardDialog(SetWalletPasswordActivity.this);
            standardDialog.setCancelText("取消");
            standardDialog.setSureText("确认");
            standardDialog.setStandardMsg("即将生成安全码，生成后将不可更改，是否确认生成?");
            standardDialog.setStandardTitle("  ");
            standardDialog.show();
            standardDialog.setCallback(new com.shanchain.data.common.base.Callback() {
                @Override
                public void invoke() {//确认生成
                    showLoadingDialog(true);
                    file = null;
                    String path = ImageUtils.getSDPath() + File.separator + "shanchain";
                    try {
                        file = BitmapUtils.saveFile(mSignBitmap, path, ImageUtils.getTempFileName() + ".jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //创建requestBody
                    MediaType MEDIA_TYPE = MediaType.parse("image/*");
                    RequestBody fileBody = MultipartBody.create(MEDIA_TYPE, file);
                    MultipartBody.Builder multiBuilder = new MultipartBody.Builder()
                            .addFormDataPart("file", file.getName(), fileBody)
                            .setType(MultipartBody.FORM);
                    RequestBody multiBody = multiBuilder.build();
                    SCHttpUtils.postByBody(HttpApi.WALLET_GET_USE_PASSWORD + "?token=" + SCCacheUtils.getCacheToken(), multiBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            LogUtils.d("setWalletPassword", "网络异常");
                            closeLoadingDialog();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            closeLoadingDialog();
                            Headers contentHeader = response.headers();
                            final Map<String, List<String>> headsMap = response.headers().toMultimap();
//                                final List<String> heads = response.headers("Content-Disposition");
                            final List<String> heads = headsMap.get("Content-Disposition");
                            //attachment; filename=test_img.png
//                            for (int i = 0; i < heads.size(); i++) {
//                                String headsField = heads.get(i);
//                                String[] fields = headsField.indexOf(";") != -1 ? headsField.split(";") :
//                                        headsField.split(",");
//                                if (fields != null && fields.length != 0) {
//                                    for (int j = 0; j < fields.length; j++) {
//                                        if (fields[j].indexOf("filename=") != -1) {
//                                            String substring = fields[j].substring(fields[j].indexOf("=") + 1, fields[j].length()).trim();
//                                            filename = substring.indexOf(";") != -1 ? substring.replace(";", "") : substring;
//                                        }
//                                    }
//                                }
//                            }
                            filename = ImageUtils.getTempFileName() + ".jpg";
                            mResultBytes = response.body().bytes();
                            final Bitmap passwordBm = BitmapFactory.decodeByteArray(mResultBytes, 0, mResultBytes.length);
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    relativeCreate.setVisibility(View.GONE);
                                    btnCreateWalletPassword.setVisibility(View.GONE);
                                    relativeSavePasswordImage.setVisibility(View.VISIBLE);
                                    btnSaveWalletPassword.setVisibility(View.VISIBLE);
                                    ivPasswordPicture.setImageBitmap(passwordBm);
//                                    ToastUtils.showToastLong(SetWalletPasswordActivity.this, "" + filename);
                                }
                            });

                        }
                    });

                }
            }, new com.shanchain.data.common.base.Callback() {
                @Override
                public void invoke() {
                }
            });

        } else if (i == R.id.btn_save_wallet_password) {
            if (ContextCompat.checkSelfPermission(SetWalletPasswordActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ToastUtils.showToast(SetWalletPasswordActivity.this, "检测到手机系统已关闭读取相册权限，请前往【设置】-【权限管理】中开启");
            }
            if (mResultBytes != null) {
                final Bitmap bitmap = BitmapFactory.decodeByteArray(mResultBytes, 0, mResultBytes.length);
                //使用io流保存图片
                FileOutputStream fos = null;
                File passwordImage = null;
                try {
                    if (filename != null) {
                        String dirPath = ImageUtils.getSDPath() + File.separator + "shanchain";
                        String filePath = dirPath + File.separator + filename;
                        File fPath = new File(dirPath);
                        if (!fPath.exists()) {
                            fPath.mkdir();

                        }
                        passwordImage = new File(filePath);
                        fos = new FileOutputStream(passwordImage);
                        fos.write(mResultBytes);
                        fos.flush();
                        fos.close();
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToast(SetWalletPasswordActivity.this, "保存成功");
                            }
                        });
                        btnNextStep.setClickable(true);
                        btnNextStep.setBackground(getResources().getDrawable(R.drawable.common_shape_bg_btn_login));
                    } else {
                        ToastUtils.showToast(SetWalletPasswordActivity.this, "保存失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showToast(SetWalletPasswordActivity.this, "保存失败");
                } finally {
                    displayToGallery(SetWalletPasswordActivity.this, passwordImage);
                }
            }

        } else if (i == R.id.btn_next_step) {
            Intent intent = new Intent(SetWalletPasswordActivity.this, WalletEnterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //图片显示到相册
    public void displayToGallery(Context context, File photoFile) {
        if (photoFile == null || !photoFile.exists()) {
            return;
        }
        String photoPath = photoFile.getAbsolutePath();
        String photoName = photoFile.getName();
        // 把文件插入到系统图库
//        try {
//            ContentResolver contentResolver = context.getContentResolver();
////            MediaStore.Images.Media.insertImage(contentResolver, photoPath, photoName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + photoPath)));
    }

    //打开相册
    private void selectImage() {
        int requestCode = WALLET_PHOTO;
        if (ContextCompat.checkSelfPermission(SetWalletPasswordActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = data;
        if (data == null || data.getData() == null) {
            btnCreateWalletPassword.setClickable(true);
            btnCreateWalletPassword.setBackground(getResources().getDrawable(R.drawable.common_shape_coupon_btn_bg_gray));
            btnSaveWalletPassword.setBackground(getResources().getDrawable(R.drawable.common_shape_coupon_btn_bg_gray));
            btnSaveWalletPassword.setClickable(false);
            return;
        } else {
            btnCreateWalletPassword.setClickable(true);
            btnCreateWalletPassword.setBackground(getResources().getDrawable(R.drawable.common_shape_bg_btn_login));
            btnSaveWalletPassword.setClickable(true);
            btnSaveWalletPassword.setBackground(getResources().getDrawable(R.drawable.common_shape_bg_btn_login));
        }
        switch (requestCode) {
            case WALLET_PHOTO:
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //获取照片路径
                photoPath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                mSignBitmap = ImageUtils.zoomBitmap(bitmap, 100, 100);
                ivSelectPicture.setImageBitmap(bitmap);

                break;
            case INTENT_TAKE:
                Uri s = data.getData();

                break;
        }
    }

    public void closeProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
        }

    }

    protected void showLoadingDialog(boolean cancelable) {
        mCustomDialog = new CustomDialog(this, 0.4, R.layout.common_dialog_progress, null);
        mCustomDialog.show();
        mCustomDialog.setCancelable(cancelable);
    }

    protected void showLoadingDialog() {
        mCustomDialog = new CustomDialog(this, 0.4, R.layout.common_dialog_progress, null);
        mCustomDialog.show();
        mCustomDialog.setCancelable(false);
    }

    protected void closeLoadingDialog() {
        if (mCustomDialog != null) {
            mCustomDialog.dismiss();
        }
    }
}
