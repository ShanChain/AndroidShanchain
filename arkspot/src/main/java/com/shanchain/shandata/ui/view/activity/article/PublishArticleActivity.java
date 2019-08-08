package com.shanchain.shandata.ui.view.activity.article;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.vod.common.utils.ToastUtil;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.PhotoArticleAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.interfaces.IAddPhotoCallback;
import com.shanchain.shandata.interfaces.IDeletePhotoCallback;
import com.shanchain.shandata.ui.model.PhotoBean;
import com.shanchain.shandata.ui.presenter.PublishArticlePresenter;
import com.shanchain.shandata.ui.presenter.impl.PublishArticlePresenterImpl;
import com.shanchain.shandata.ui.view.activity.article.view.PublishArticleView;
import com.shanchain.shandata.widgets.photochoose.PhotoUtils;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by WealChen
 * Date : 2019/7/24
 * Describe :发布文章
 */
public class PublishArticleActivity extends BaseActivity implements PublishArticleView {
    @Bind(R.id.im_back)
    ImageView imBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_publish)
    TextView tvPublish;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.tv_number)
    TextView tvNumber;
    @Bind(R.id.gv_photo)
    GridView gvPhoto;

    private int sourceType = 0;
    private PhotoArticleAdapter mAdapter;
    private List<PhotoBean> mList = new ArrayList<>();
    private final MyHandler mHandler = new MyHandler(this);
    private PublishArticlePresenter mArticlePresenter;
    private String content="";
    private String []attr;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_publish_article;
    }

    @Override
    protected void initViewsAndEvents() {
        sourceType = getIntent().getIntExtra("type",1);
        if(sourceType ==1){
            tvTitle.setText(getString(R.string.publish_essay));
        }else if(sourceType == 2){
            tvTitle.setText(getString(R.string.publish_long_text));
        }else {
            tvTitle.setText(getString(R.string.publish_link));
        }
        mArticlePresenter = new PublishArticlePresenterImpl(this);
        mList.add(new PhotoBean());
        mAdapter = new PhotoArticleAdapter(this);
        mAdapter.setList(mList);
        gvPhoto.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

//        initEditeViewListener();
        setIAddPhotoCallback();
    }

    //发布
    @OnClick(R.id.tv_publish)
    void publishContent(){
        content = etContent.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            ToastUtil.showToast(this, R.string.enter_post_content);
            return;
        }
        for (int i = 0; i < mList.size(); i++) {
            if(TextUtils.isEmpty(mList.get(i).getUrl())){
                mList.remove(i);
            }
        }
        if(mList.size()>0){
            //有图片先上传图片d
            mArticlePresenter.uploadPhotoListToServer(mList);
        }else {
            //没有图片直接发布
            mArticlePresenter.addArticleNoPictrue(Integer.parseInt(SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER)),content.length()>10 ? content.substring(0,10):content,content,"");
        }
    }

    @OnClick(R.id.im_back)
    void finished(){
        finish();
    }

    //设置输入框监听
    private void initEditeViewListener(){
        tvNumber.setText(getString(R.string.enter_400_words,"400"));
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = etContent.getText().length();
                if(count<=0){
                    tvNumber.setText(getString(R.string.enter_400_words,"400"));
                }
                if(count >=400){
                    tvNumber.setText(getString(R.string.enter_400_words,"0"));
                }
                if(0<count && count<400){
                    tvNumber.setText(getString(R.string.enter_400_words,""+(400-count)));
                }
            }
        });
    }

    //监听添加图片按钮和删除图片
    public void setIAddPhotoCallback(){
        mAdapter.setCallback(new IAddPhotoCallback() {
            @Override
            public void addPhoto() {
                int requestCode = PhotoUtils.INTENT_SELECT;
                if (ContextCompat.checkSelfPermission(PublishArticleActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PublishArticleActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, requestCode);
                }
            }
        });
        mAdapter.setIDeletePhotoCallback(new IDeletePhotoCallback() {
            @Override
            public void deletePhoto(int position) {
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtils.INTENT_SELECT:
                if (data == null) {
                    return;
                }
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String photoPath = cursor.getString(columnIndex);  //获取照片路径
                cursor.close();
                LogUtils.d("压缩前路径：",photoPath);
                comressImage(photoPath);
                break;
    }}

    //重新处理列表的顺序逻辑
    private void resetPhoteoList(){
        if(mList.size()==0)return;
        for (int i = 0; i < mList.size(); i++) {
            if(TextUtils.isEmpty(mList.get(i).getUrl())){
                mList.remove(i);
            }
            continue;
        }
        if(mList.size()<9)
            mList.add(new PhotoBean());
    }

    private void comressImage(final String s){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //图片压缩处理
                String fileName = s.substring(s.lastIndexOf("/") + 1, s.length());
                Bitmap bitmap= BitmapFactory.decodeFile(s);
                Bitmap bm1=compress(bitmap);
                try {
                    //要存到data目录下的文件夹名
                    String basePath = getApplication().getCacheDir() +"";
                    File folder = new File(basePath);
                    String fname=basePath+File.separator+fileName;
                    File myCaptureFile = new File(fname);
                    if (!folder.exists() && !folder.isDirectory()) {
                        LogUtils.d(".........", "创建");
                        folder.mkdirs();
                    }
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                    bm1.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    LogUtils.d("filename",fname);
                    PhotoBean p = new PhotoBean();
                    p.setFileName(fileName);
                    p.setUrl(fname);
                    mList.add(p);
                    bos.flush();
                    bos.close();
                    mHandler.sendEmptyMessage(1);
                }catch (IOException e){

                }

            }
        }).start();
    }
    private static Bitmap compress(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 50;
        while (baos.toByteArray().length / 1024 > 300) {
            // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            // 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10

            if (options < 0) {
                options= 0;
            }
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            if(options==0){
                break;
            }

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        // 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    @Override
    public void showProgressStart() {
        showLoadingDialog();
    }

    @Override
    public void showProgressEnd() {
        closeLoadingDialog();
    }

    @Override
    public void addArticleResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            ToastUtil.showToast(PublishArticleActivity.this,getString(R.string.publish_success));
            finish();
        }else {
            ToastUtil.showToast(PublishArticleActivity.this, getString(R.string.publish_failed));
        }
    }

    @Override
    public void setPhotoListSuccess(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            String data = JSONObject.parseObject(response).getString("filenames");
            attr = data.substring(1,data.length()-1).split(",");
            mHandler.sendEmptyMessage(2);
        }else {
            ToastUtil.showToast(PublishArticleActivity.this, getString(R.string.images_upload_failed));
        }
    }

    private class MyHandler extends Handler {
        private final WeakReference<PublishArticleActivity> mActivity;

        public MyHandler(PublishArticleActivity activity) {
            mActivity = new WeakReference<PublishArticleActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            PublishArticleActivity activity = mActivity.get();
            switch (msg.what){
                case 2:
                    String a="";
                    for (int i = 0; i < attr.length; i++) {
                        String phth = attr[i].substring(1,attr[i].length()-1);
                        a += phth+",";
                    }
                    mArticlePresenter.addArticleNoPictrue(Integer.parseInt(SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER)),content.length()>10 ? content.substring(0,10):content,content,
                            a.substring(0,a.length()-1));
                    break;
                case 1:
                    if (activity != null) {
                        resetPhoteoList();
                        mAdapter.notifyDataSetChanged();
                    }
                 break;
            }

        }
    }
}
