package com.shanchain.shandata.ui.view.activity.article;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.baidu.mapapi.model.LatLng;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.PhotoArticleAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.interfaces.IAddPhotoCallback;
import com.shanchain.shandata.interfaces.IDeletePhotoCallback;
import com.shanchain.shandata.ui.model.PhotoBean;
import com.shanchain.shandata.ui.presenter.PublishArticlePresenter;
import com.shanchain.shandata.ui.presenter.impl.PublishArticlePresenterImpl;
import com.shanchain.shandata.ui.view.activity.article.view.PublishArticleView;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintNewActivity;
import com.shanchain.shandata.ui.view.activity.login.LoginActivity;
import com.shanchain.shandata.utils.FilePathUtils;
import com.shanchain.shandata.utils.PhotoSelectHelper;
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
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.model.UserInfo;
import io.reactivex.Flowable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import rx.Observable;
import rx.Scheduler;
import top.zibin.luban.Luban;

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
    private PublishArticlePresenter mArticlePresenter;
    private String content="";
    private String []attr;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_publish_article;
    }

    @Override
    protected void initViewsAndEvents() {
        //极光用户登录状态监听
        JMessageClient.registerEventReceiver(this);
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

        initEditeViewListener();
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
        tvNumber.setText(getString(R.string.enter_400_words,"1000"));
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
                    tvNumber.setText(getString(R.string.enter_400_words,"1000"));
                }
                if(count >=1000){
                    tvNumber.setText(getString(R.string.enter_400_words,"0"));
                }
                if(0<count && count<1000){
                    tvNumber.setText(getString(R.string.enter_400_words,""+(1000-count)));
                }
            }
        });
    }

    //监听添加图片按钮和删除图片
    public void setIAddPhotoCallback(){
        mAdapter.setCallback(new IAddPhotoCallback() {
            @Override
            public void addPhoto() {
                selectImage();

            }
        });
        mAdapter.setIDeletePhotoCallback(new IDeletePhotoCallback() {
            @Override
            public void deletePhoto(int position) {
                mList.remove(position);
                resetPhoteoList();
                mAdapter.notifyDataSetChanged();
            }
        });
    }
    private static final int REQUEST_CODE_GALLERY = 0x112;
    //galleryfinal多选图片
    private void selectImage(){
        final PhotoSelectHelper helper = new PhotoSelectHelper(this, false, 200);
        helper.openGalleryMulti(REQUEST_CODE_GALLERY, 9-mList.size()+1, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (reqeustCode == REQUEST_CODE_GALLERY) {
                    if(resultList != null && resultList.size()>0){
                        //异步压缩
                        List<String> s = new ArrayList<>();
                        for (PhotoInfo p:resultList) {
                            LogUtils.d("-----压缩前路径-----",p.getPhotoPath());
                            s.add(p.getPhotoPath());
                        }
                        //鲁班压缩
                        asyTaskLoadImages(s);
                    }
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

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
                LogUtils.d("压缩前路径：",photoPath+"------size :"+new File(photoPath).length()+"");
//                comressImage(photoPath);

                break;
    }}

    //重新处理列表的顺序逻辑
    private void resetPhoteoList(){
        if(mList.size()==0)return;
        for (int i = 0; i < mList.size(); i++) {
            if(TextUtils.isEmpty(mList.get(i).getUrl())){
                mList.remove(i);
            }
//            continue;
        }
        if(mList.size()<9) {
            /*PhotoBean p = new PhotoBean();
            p.setUrl("");*/
            mList.add(new PhotoBean());
        }
//        mAdapter.setList(mList);
        mAdapter.notifyDataSetChanged();
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
            handler.sendEmptyMessage(2);
        }else {
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast(PublishArticleActivity.this, getString(R.string.images_upload_failed));
                }
            });
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
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
                    resetPhoteoList();
                    break;
            }

        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }


    //采用鲁班压缩算法+Rxjava压缩图片
    private void asyTaskLoadImages(final List<String> list){
        Flowable.just(list).subscribeOn(Schedulers.io())
                .map(new Function<List<String>, List<File>>() {
                    @Override
                    public List<File> apply(List<String> strings) throws Exception {
                        return Luban.with(PublishArticleActivity.this).load(list).setTargetDir(MyApplication.getInstance().initCacheDirPath().getAbsolutePath()).get();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<File>>() {
                    @Override
                    public void accept(List<File> files) throws Exception {
                        for (File s:files) {
                            PhotoBean photoBean = new PhotoBean();
                            photoBean.setUrl(s.getAbsolutePath());
                            photoBean.setFileName(s.getName());
                            mList.add(photoBean);
                            LogUtils.d("-----压缩后的路径："+s.getAbsolutePath()+",压缩后的大小："+ FilePathUtils.getDataSize(s.length()));
                        }
                        resetPhoteoList();
                    }
                });
    }

    //监听用户登录状态
    public void onEventMainThread(LoginStateChangeEvent event) {
        LoginStateChangeEvent.Reason reason = event.getReason();//获取变更的原因
        switch (reason) {
            case user_logout:
                //用户换设备登录
                com.shanchain.data.common.utils.LogUtils.d("LoginStateChangeEvent", "账号在其他设备上登录");
                final StandardDialog standardDialog = new StandardDialog(PublishArticleActivity.this);
                standardDialog.setStandardTitle(getString(R.string.prompt));
                standardDialog.setStandardMsg(getString(R.string.account_other_login));
                standardDialog.setSureText(getString(R.string.re_login));
                standardDialog.setCancelText(getString(R.string.str_cancel));
                standardDialog.setCallback(new Callback() {//确定
                    @Override
                    public void invoke() {
                        loginOut();
                    }
                }, new Callback() {//取消
                    @Override
                    public void invoke() {
                        loginOut();
                    }
                });
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        standardDialog.show();
                    }
                });
                standardDialog.setCanceledOnTouchOutside(false);
                break;
            case user_deleted:
                //用户被删除
                break;
        }
    }
}
