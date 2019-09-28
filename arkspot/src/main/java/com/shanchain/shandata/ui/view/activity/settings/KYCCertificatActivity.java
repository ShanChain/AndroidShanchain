package com.shanchain.shandata.ui.view.activity.settings;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.vod.common.utils.ToastUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.ui.model.PasspostBean;
import com.shanchain.shandata.ui.presenter.KYCPresenter;
import com.shanchain.shandata.ui.presenter.impl.KYCPresenterImpl;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.ui.view.activity.settings.view.KYCView;
import com.shanchain.shandata.utils.FilePathUtils;
import com.shanchain.shandata.utils.PhotoSelectHelper;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * Created by WealChen
 * Date : 2019/9/25
 * Describe :
 */
public class KYCCertificatActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, KYCView {
    @Bind(R.id.tb_setting)
    ArthurToolBar tbSetting;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_idcard)
    EditText etIdcard;
    @Bind(R.id.et_passport)
    EditText etPassport;
    @Bind(R.id.im_shfzzm)
    ImageView imShfzzm;
    @Bind(R.id.im_add_sfzm)
    ImageView imAddSfzm;
    @Bind(R.id.im_shfzfm)
    ImageView imShfzfm;
    @Bind(R.id.im_add_sffm)
    ImageView imAddSffm;
    @Bind(R.id.im_scsfz)
    ImageView imScsfz;
    @Bind(R.id.im_add_scsfz)
    ImageView imAddScsfz;
    @Bind(R.id.im_hz)
    ImageView imHz;
    @Bind(R.id.im_add_hz)
    ImageView imAddHz;
    @Bind(R.id.im_schz)
    ImageView imSchz;
    @Bind(R.id.im_add_schz)
    ImageView imAddSchz;
    @Bind(R.id.btn_register)
    Button btnEegister;

    private int selectType;
    private static final int REQUEST_CODE_GALLERY = 0x112;
    private KYCPresenter mKYCPresenter;
    private MyHandler mMyHandler;
    private PasspostBean mPasspostBean;
    private String sfzzm, sfzfm, scsfz, hzzp, schz;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_kyc_certificat;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolbar();
    }

    private void initToolbar() {
        ArthurToolBar arthurToolBar = findViewById(R.id.tb_setting);
        arthurToolBar.setTitleText(getString(R.string.kyc_identify));
        arthurToolBar.setOnLeftClickListener(this);

        mKYCPresenter = new KYCPresenterImpl(this);
//        mKYCPresenter.queryKycCertifitInfo(SCCacheUtils.getCacheUserId());
        mMyHandler = new MyHandler(this);

        mPasspostBean = (PasspostBean) getIntent().getSerializableExtra("info");
        if(mPasspostBean!=null){
            //重新认证过来的，需要赋值
            reCertifit();
        }
    }

    private void reCertifit(){
        etName.setText(mPasspostBean.getRealName());
        etIdcard.setText(mPasspostBean.getIdcardNo());
        etPassport.setText(mPasspostBean.getPassportNo());
        //身份证正面
        Glide.with(this).load(HttpApi.BASE_URL+mPasspostBean.getCardPhotoFront())
                .apply(new RequestOptions().placeholder(R.mipmap.kyc_defautl_icon)
                        .error(R.mipmap.kyc_defautl_icon)).into(imShfzzm);
        sfzzm = mPasspostBean.getCardPhotoFront();
        //身份证反面
        Glide.with(this).load(HttpApi.BASE_URL+mPasspostBean.getCardPhotoBackground())
                .apply(new RequestOptions().placeholder(R.mipmap.kyc_defautl_icon)
                        .error(R.mipmap.kyc_defautl_icon)).into(imShfzfm);
        sfzfm = mPasspostBean.getCardPhotoBackground();
        //手持身份证
        Glide.with(this).load(HttpApi.BASE_URL+mPasspostBean.getCardPhotoHand())
                .apply(new RequestOptions().placeholder(R.mipmap.kyc_defautl_icon)
                        .error(R.mipmap.kyc_defautl_icon)).into(imScsfz);
        scsfz = mPasspostBean.getCardPhotoHand();
        //护照照片
        Glide.with(this).load(HttpApi.BASE_URL+mPasspostBean.getPassportPhoto())
                .apply(new RequestOptions().placeholder(R.mipmap.kyc_defautl_icon)
                        .error(R.mipmap.kyc_defautl_icon)).into(imHz);
        hzzp = mPasspostBean.getPassportPhoto();
        //手持护照照片
        Glide.with(this).load(HttpApi.BASE_URL+mPasspostBean.getPassportPhotoHand())
                .apply(new RequestOptions().placeholder(R.mipmap.kyc_defautl_icon)
                        .error(R.mipmap.kyc_defautl_icon)).into(imSchz);
        schz = mPasspostBean.getPassportPhotoHand();

    }

    //身份证正面
    @OnClick(R.id.im_shfzzm)
    void shfzzm() {
        seleceIamgeType(1);
    }

    //身份证反面
    @OnClick(R.id.im_shfzfm)
    void shfzfm() {
        seleceIamgeType(2);
    }

    //手持身份证
    @OnClick(R.id.im_scsfz)
    void imScsfz() {
        seleceIamgeType(3);
    }

    //护照照片
    @OnClick(R.id.im_hz)
    void imHz() {
        seleceIamgeType(4);
    }

    //手持护照照片
    @OnClick(R.id.im_schz)
    void imSchz() {
        seleceIamgeType(5);
    }

    //提交信息
    @OnClick(R.id.btn_register)
    void commitInfo(){
        String realName = etName.getText().toString().trim();
        String idCardNo = etIdcard.getText().toString().trim();
        String passport = etPassport.getText().toString().trim();
        if(TextUtils.isEmpty(realName)){
            ToastUtil.showToast(this,getString(R.string.verified_name_hint));
            return;
        }
        if(TextUtils.isEmpty(idCardNo)){
            ToastUtil.showToast(this,getString(R.string.verified_code_hint));
            return;
        }
        if(TextUtils.isEmpty(passport)){
            ToastUtil.showToast(this,getString(R.string.enter_you_passp));
            return;
        }
        if(TextUtils.isEmpty(sfzzm)){
            ToastUtil.showToast(this, R.string.upload_front_p);
            return;
        }
        if(TextUtils.isEmpty(sfzfm)){
            ToastUtil.showToast(this, R.string.upload_reverse_p);
            return;
        }
        if(TextUtils.isEmpty(scsfz)){
            ToastUtil.showToast(this, R.string.upload_h_p);
            return;
        }
        if(TextUtils.isEmpty(hzzp)){
            ToastUtil.showToast(this, R.string.upload_pass_p);
            return;
        }
        if(TextUtils.isEmpty(schz)){
            ToastUtil.showToast(this, R.string.upload_h_p_p);
            return;
        }
        if(mPasspostBean == null){
            mKYCPresenter.commitUserInfo(SCCacheUtils.getCacheUserId(),realName,idCardNo,passport,
                    sfzzm,sfzfm,scsfz,hzzp,schz);
        }else {//更新
            mKYCPresenter.updateUserKYCInfo(mPasspostBean.getId(),SCCacheUtils.getCacheUserId(),realName,idCardNo,passport,
                    sfzzm,sfzfm,scsfz,hzzp,schz);
        }

        btnEegister.setEnabled(false);

    }

    private void seleceIamgeType(int type) {
        selectImage();
        selectType = type;
    }

    //galleryfinal多选图片
    private void selectImage() {
        final PhotoSelectHelper helper = new PhotoSelectHelper(this, false, 200);
        helper.openGalleryMulti(REQUEST_CODE_GALLERY, 1, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (reqeustCode == REQUEST_CODE_GALLERY) {
                    if (resultList != null && resultList.size() > 0) {
                        //异步压缩
                        List<String> s = new ArrayList<>();
                        for (PhotoInfo p : resultList) {
                            LogUtils.d("KYC压缩前路径", p.getPhotoPath());
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

    //鲁班压缩算法
    private void asyTaskLoadImages(final List<String> list) {
        Flowable.just(list).subscribeOn(Schedulers.io())
                .map(new Function<List<String>, List<File>>() {
                    @Override
                    public List<File> apply(List<String> strings) throws Exception {
                        return Luban.with(KYCCertificatActivity.this).load(list).setTargetDir(MyApplication.getInstance().initCacheDirPath().getAbsolutePath()).get();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<File>>() {
                    @Override
                    public void accept(List<File> files) throws Exception {
                        if (files != null && files.size() > 0) {
                            File file = files.get(0);
                            LogUtils.d("KYC压缩后的路径：" + file.getAbsolutePath() + ",压缩后的大小：" + FilePathUtils.getDataSize(file.length()));
                            mKYCPresenter.uploadPhotoListToServer(file.getAbsolutePath());
                        }
                    }
                });
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }



    @Override
    public void setUploadImages(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            String data = JSONObject.parseObject(response).getString("filenames");
            String urlPath = "";
            List<String> list = JSONObject.parseArray(data,String.class);
            if(list!=null && list.size()>0){
                urlPath = list.get(0);
            }
            LogUtils.d("KYC upload image url: " + data+"-----list: "+urlPath);
            if (!TextUtils.isEmpty(data)) {
                Message message = new Message();
                switch (selectType) {
                    case 1:
                        sfzzm = urlPath;
                        message.what = 1;
                        break;
                    case 2:
                        sfzfm = urlPath;
                        message.what = 2;
                        break;
                    case 3:
                        scsfz = urlPath;
                        message.what =3;
                        break;
                    case 4:
                        hzzp = urlPath;
                        message.what = 4;
                        break;
                    default:
                        schz = urlPath;
                        message.what = 5;
                        break;

                }
                mMyHandler.sendMessage(message);
            } else {
                ToastUtil.showToast(KYCCertificatActivity.this, getString(R.string.images_upload_failed));
            }
        } else {
            ToastUtil.showToast(KYCCertificatActivity.this, getString(R.string.images_upload_failed));
        }
    }

    @Override
    public void setCommitPassportInfpResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        btnEegister.setEnabled(true);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE_NEW)) {
            ToastUtil.showToast(KYCCertificatActivity.this, R.string.commit_success_1);

            finish();
        }
    }

    @Override
    public void setQueryKycInfoResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE_NEW)) {

        }
    }

    private class MyHandler extends Handler {
        private WeakReference<KYCCertificatActivity> mWeakReference;
        public MyHandler(KYCCertificatActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            KYCCertificatActivity kycCertificatActivity = mWeakReference.get();
            switch (msg.what) {
                case 1:
                    if(kycCertificatActivity!=null){
                        Glide.with(kycCertificatActivity).load(HttpApi.BASE_URL+sfzzm).into(imShfzzm);
                        imAddSfzm.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    if(kycCertificatActivity!=null){
                        Glide.with(mContext).load(HttpApi.BASE_URL+sfzfm).into(imShfzfm);
                        imAddSffm.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    if(kycCertificatActivity!=null){
                        Glide.with(mContext).load(HttpApi.BASE_URL+scsfz).into(imScsfz);
                        imAddScsfz.setVisibility(View.GONE);
                    }
                    break;
                case 4:
                    if(kycCertificatActivity!=null){
                        Glide.with(mContext).load(HttpApi.BASE_URL+hzzp).into(imHz);
                        imAddHz.setVisibility(View.GONE);
                    }
                    break;
                default:
                    if(kycCertificatActivity!=null){
                        Glide.with(mContext).load(HttpApi.BASE_URL+schz).into(imSchz);
                        imAddSchz.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }
}
