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

import com.alibaba.fastjson.JSONObject;
import com.aliyun.vod.common.utils.ToastUtil;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
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
import com.shanchain.shandata.utils.ManagerUtils;
import com.shanchain.shandata.utils.PhotoSelectHelper;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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
    private String fileList;
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
        if(mList.size()<3) {
            mList.add(new PhotoBean());
        }
        mAdapter.notifyDataSetChanged();
    }
    @OnClick(R.id.btn_submit)
    void commitFeedback(){
        if (editFeedback.getText().toString().length() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                if(TextUtils.isEmpty(mList.get(i).getUrl())){
                    mList.remove(i);
                }
            }
            if(mList.size()>0){
                //上传反馈图片
                uploadImages();
            }else {
                commiteFeedback();
            }
        } else {
            ToastUtils.showToast(FeedbackActivity.this, R.string.enter_feedback);
        }
    }

    //上传图片
    private void uploadImages(){
        showLoadingDialog();
        OkHttpClient mOkHttpClient = OkHttpUtils.getInstance().getOkHttpClient();
        MultipartBody.Builder mbody=new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(PhotoBean p:mList){
            if(new File(p.getUrl()).exists()){
                mbody.addFormDataPart("files",p.getFileName(), RequestBody.create(SCHttpUtils.FORM_DATA,new File(p.getUrl())));
            }
        }
        RequestBody requestBody =mbody.build();
        Request request = new Request.Builder()
                .url(HttpApi.UPLOAD_IMAGE)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("Resonse IOException: ", e.toString());
                closeLoadingDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                closeLoadingDialog();
                String result = response.body().string();
                LogUtils.d("Resonse: ", result);
                String code = SCJsonUtils.parseCode(result);
                if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                    String data = JSONObject.parseObject(result).getString("filenames");
                    if(!TextUtils.isEmpty(data)){
                        String attr [] = data.substring(1,data.length()-1).split(",");
                        for (int i = 0; i < attr.length; i++) {
                            String phth = attr[i].substring(1,attr[i].length()-1);
                            fileList += phth+",";
                        }
                        commiteFeedback();
                    }else {
                        ToastUtil.showToast(FeedbackActivity.this, getString(R.string.images_upload_failed));;
                    }
                }else {
                    ToastUtil.showToast(FeedbackActivity.this, getString(R.string.images_upload_failed));
                }

            }
        });
    }
    //提交反馈
    private void commiteFeedback(){
        String phoneMode = ManagerUtils.getSystemModel()+"("+ManagerUtils.getSystemVersion()+")";
        Map<String,String> map = new HashMap<>();
        map.put("title","用户反馈");
        map.put("disc",editFeedback.getText().toString()+"&&"+phoneMode);
        map.put("type","1");
        if(!TextUtils.isEmpty(fileList)){
            map.put("listImg",fileList.substring(0,fileList.length()-1));
        }else {
            map.put("listImg","");
        }
        String dataJson = JSONObject.toJSONString(map);
        LogUtils.d("------->>>"+dataJson);
        SCHttpUtils.postWithUserId()
                .url(HttpApi.USE_FEEDBACK)
                .addParams("dataString", dataJson)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = SCJsonUtils.parseCode(response);
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            ToastUtils.showToast(FeedbackActivity.this, R.string.feedback_success);
                            finish();
                        }else {
                            ToastUtil.showToast(FeedbackActivity.this, getString(R.string.images_upload_failed));
                        }
                    }
                });
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
