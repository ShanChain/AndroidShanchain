package com.shanchain.shandata.ui.view.activity.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.dialog.MyDialog;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by WealChen
 * Date : 2019/8/29
 * Describe :
 */
public class ViewImageActivity extends BaseActivity {
    @Bind(R.id.pv_image)
    PhotoView pvImage;
    @Bind(R.id.im_back)
    ImageView imBack;

    private String urlImage;
    private static final int SAVE_SUCCESS = 0;//保存图片成功
    private static final int SAVE_FAILURE = 1;//保存图片失败
    private static final int SAVE_BEGIN = 2;//开始保存图片
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE_BEGIN:
                    ToastUtils.showToast(ViewImageActivity.this,"开始保存图片...");
                    break;
                case SAVE_SUCCESS:
                    ToastUtils.showToast(ViewImageActivity.this,"图片保存成功,请到相册查找");
                    break;
                case SAVE_FAILURE:
                    ToastUtils.showToast(ViewImageActivity.this,"图片保存失败,请稍后再试...");
                    break;
            }
        }
    };
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_view_image;
    }

    @Override
    protected void initViewsAndEvents() {
        urlImage = getIntent().getStringExtra("url");
        Glide.with(mContext).load(HttpApi.BASE_URL + File.separator+urlImage).into(pvImage);

        pvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pvImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new MyDialog(ViewImageActivity.this).setTitle("保存图片到相册？")
                        .setLeftButton(getString(R.string.str_sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //保存图片到相册
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mHandler.obtainMessage(SAVE_BEGIN).sendToTarget();
                                        Bitmap bp = returnBitMap(HttpApi.BASE_URL + File.separator+urlImage);
                                        saveImageToPhotos(mContext, bp);
                                    }
                                }).start();
                            }
                        }).setRightButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                return true;
            }
        });

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    /**
     * 将URL转化成bitmap形式
     *
     * @param url
     * @return bitmap type
     */
    public final static Bitmap returnBitMap(String url) {
        URL myFileUrl;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 保存二维码到本地相册
     */
    private void saveImageToPhotos(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "marjar");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mHandler.obtainMessage(SAVE_FAILURE).sendToTarget();
        } catch (IOException e) {
            e.printStackTrace();
            mHandler.obtainMessage(SAVE_FAILURE).sendToTarget();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mHandler.obtainMessage(SAVE_FAILURE).sendToTarget();
            return;
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        mHandler.obtainMessage(SAVE_SUCCESS).sendToTarget();
    }

}
