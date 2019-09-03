package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.shandata.adapter.PhotoArticleAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.interfaces.IAddPhotoCallback;
import com.shanchain.shandata.interfaces.IDeletePhotoCallback;
import com.shanchain.shandata.ui.model.PhotoBean;
import com.shanchain.shandata.ui.view.activity.article.PublishArticleActivity;
import com.shanchain.shandata.utils.FilePathUtils;
import com.shanchain.shandata.utils.PhotoSelectHelper;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import top.zibin.luban.Luban;

public class FeedbackActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_main)
    ArthurToolBar tbMain;
    @Bind(R.id.edit_feedback)
    EditText editFeedback;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    @Bind(R.id.gv_photo)
    GridView gvPhoto;
    private PhotoArticleAdapter mAdapter;
    private List<PhotoBean> mList = new ArrayList<>();
    private static final int REQUEST_CODE_GALLERY = 0x112;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_feeback;
    }


    @Override
    protected void initViewsAndEvents() {
        tbMain.setTitleTextColor(Color.BLACK);
        tbMain.isShowChatRoom(false);//不在导航栏显示聊天室信息
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tbMain.getTitleView().setLayoutParams(layoutParams);
        tbMain.setTitleText(getResources().getString(R.string.nav_feedback));
        tbMain.setRightText("");
        tbMain.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        tbMain.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        tbMain.setOnLeftClickListener(this);

        mList.add(new PhotoBean());
        mAdapter = new PhotoArticleAdapter(this);
        mAdapter.setList(mList);
        gvPhoto.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        setIAddPhotoCallback();
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
    //galleryfinal多选图片
    private void selectImage(){
        final PhotoSelectHelper helper = new PhotoSelectHelper(this, false, 200);
        helper.openGalleryMulti(REQUEST_CODE_GALLERY, 3-mList.size()+1, new GalleryFinal.OnHanlderResultCallback() {
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

    //采用鲁班压缩算法+Rxjava压缩图片
    private void asyTaskLoadImages(final List<String> list){
        Flowable.just(list).subscribeOn(Schedulers.io())
                .map(new Function<List<String>, List<File>>() {
                    @Override
                    public List<File> apply(List<String> strings) throws Exception {
                        return Luban.with(FeedbackActivity.this).load(list).setTargetDir(MyApplication.getInstance().initCacheDirPath().getAbsolutePath()).get();
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

    //重新处理列表的顺序逻辑
    private void resetPhoteoList(){
        if(mList.size()==0)return;
        for (int i = 0; i < mList.size(); i++) {
            if(TextUtils.isEmpty(mList.get(i).getUrl())){
                mList.remove(i);
            }
        }
        if(mList.size()<9) {
            mList.add(new PhotoBean());
        }
        mAdapter.notifyDataSetChanged();
    }
    @OnClick(R.id.btn_submit)
    void commitFeedback(){
        if (editFeedback.getText().toString().length() > 0) {
            String dataString = "{\"title\":\"用户反馈\",\"disc\":\"" + editFeedback.getText().toString() + "\",\"type\":1}";
            SCHttpUtils.postWithUserId()
                    .url(HttpApi.USE_FEEDBACK)
                    .addParams("dataString", dataString)
                    .build()
                    .execute(new SCHttpStringCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            ToastUtils.showToast(FeedbackActivity.this, "反馈成功");
                            finish();
                        }
                    });
        } else {
            ToastUtils.showToast(FeedbackActivity.this, R.string.enter_feedback);
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
