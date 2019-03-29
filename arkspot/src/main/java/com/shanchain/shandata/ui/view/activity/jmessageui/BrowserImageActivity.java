package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.shanchain.data.common.utils.BitmapUtils;
import com.shanchain.data.common.utils.ImageUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.view.activity.jmessageui.impl.PhotoView;
import com.shanchain.shandata.widgets.photochoose.DialogCreator;
import com.shanchain.shandata.widgets.photochoose.ImgBrowserViewPager;
import com.shanchain.shandata.widgets.pickerimage.utils.AttachmentStore;
import com.shanchain.shandata.widgets.pickerimage.utils.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jiguang.imui.commons.BitmapLoader;


public class BrowserImageActivity extends Activity {

    private ImgBrowserViewPager mViewPager;
    private List<String> mPathList = new ArrayList<>();
    private List<String> mMsgIdList = new ArrayList<>();
    private LruCache<String, Bitmap> mCache;
    private int mWidth;
    private int mHeight;
    protected float mDensity;
    protected int mDensityDpi;
    protected int mAvatarSize;
    protected float mRatio;
    private Dialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);
        mPathList = getIntent().getStringArrayListExtra("pathList");
        mMsgIdList = getIntent().getStringArrayListExtra("idList");
        mViewPager = findViewById(R.id.img_browser_viewpager);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        mDensity = dm.density;
        mDensityDpi = dm.densityDpi;
        mRatio = Math.min((float) mWidth / 720, (float) mHeight / 1280);
        mAvatarSize = (int) (50 * mDensity);

        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        int cacheSize = maxMemory / 4;
        mCache = new LruCache<>(cacheSize);
        mViewPager.setAdapter(pagerAdapter);
        initCurrentItem();
    }

    private void initCurrentItem() {
        PhotoView photoView = new PhotoView(true, this);
        String msgId = getIntent().getStringExtra("messageId");
        int position = mMsgIdList.indexOf(msgId);
        if (position != -1) {
            String path = mPathList.get(position);
            if (path != null) {
                Bitmap bitmap = mCache.get(path);
                if (bitmap != null) {
                    photoView.setImageBitmap(bitmap);
                } else {
                    File file = new File(path);
                    if (file.exists()) {
                        bitmap = BitmapLoader.getBitmapFromFile(path, mWidth, mHeight);
                        if (bitmap != null) {
                            photoView.setImageBitmap(bitmap);
                            mCache.put(path, bitmap);
                        } else {
                            photoView.setImageResource(R.drawable.aurora_picture_not_found);
                        }
                    } else {
                        photoView.setImageResource(R.drawable.aurora_picture_not_found);
                    }
                }
            } else {
                photoView.setImageResource(R.drawable.aurora_picture_not_found);
            }
            mViewPager.setCurrentItem(position);
        } else {
            photoView.setImageResource(R.drawable.aurora_picture_not_found);
        }
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mPathList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(true, BrowserImageActivity.this);
//            photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            photoView.setTag(position);
            String path = mPathList.get(position);
            if (path != null) {
                Bitmap bitmap = mCache.get(path);
                if (bitmap != null) {
                    photoView.setImageBitmap(bitmap);
                } else {
                    File file = new File(path);
                    if (file.exists()) {
                        bitmap = BitmapLoader.getBitmapFromFile(path, mWidth, mHeight);
//                        bitmap = BitmapUtils.getBitmap(path);
                        if (bitmap != null) {
                            photoView.setImageBitmap(bitmap);
                            mCache.put(path, bitmap);
                        } else {
                            photoView.setImageResource(R.drawable.aurora_picture_not_found);
                        }
                    } else {
                        photoView.setImageResource(R.drawable.aurora_picture_not_found);
                    }
                }
            } else {
                photoView.setImageResource(R.drawable.aurora_picture_not_found);
            }
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //图片长按保存到手机
            onImageViewFound(photoView, path);
            return photoView;
        }

        private void onImageViewFound(PhotoView photoView, final String path) {
            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.jmui_delete_conv_ll://保存图片
                                    savePicture(path, mDialog);
                                    break;
//                                case R.id.jmui_top_conv_ll://转发
//                                    Intent intent = new Intent(BrowserViewPagerActivity.this, ForwardMsgActivity.class);
//                                    JGApplication.forwardMsg.clear();
//                                    JGApplication.forwardMsg.add(mMsg);
//                                    startActivity(intent);
//                                    break;
                                default:
                                    break;
                            }
                            mDialog.dismiss();
                        }
                    };
                    mDialog = DialogCreator.createSavePictureDialog(BrowserImageActivity.this, listener);
                    mDialog.show();
                    mDialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
                    return false;
                }
            });
        }

        // 保存图片
        public void savePicture(String path, Dialog dialog) {
            if (TextUtils.isEmpty(path)) {
                return;
            }

            String picPath = StorageUtil.getSystemImagePath();
            String dstPath = picPath + path;
            if (AttachmentStore.copy(path, dstPath) != -1) {
                try {
                    ContentValues values = new ContentValues(2);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.Images.Media.DATA, dstPath);
                    getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Toast.makeText(BrowserImageActivity.this, "保存图片到手机", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } catch (Exception e) {
                    dialog.dismiss();
                    Toast.makeText(BrowserImageActivity.this, "保存失败", Toast.LENGTH_LONG).show();
                }
            } else {
                dialog.dismiss();
                Toast.makeText(BrowserImageActivity.this, "保存失败", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            View view = (View) object;
            int currentPage = mViewPager.getCurrentItem();
            if (currentPage == (Integer) view.getTag()) {
                return POSITION_NONE;
            } else {
                return POSITION_UNCHANGED;
            }
        }
    };


}
