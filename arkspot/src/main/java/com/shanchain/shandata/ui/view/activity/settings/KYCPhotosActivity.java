package com.shanchain.shandata.ui.view.activity.settings;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.PasspostBean;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by WealChen
 * Date : 2019/9/26
 * Describe : KYC认证照片
 */
public class KYCPhotosActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener{
    @Bind(R.id.tb_setting)
    ArthurToolBar tbSetting;
    @Bind(R.id.im_shfzzm)
    ImageView imShfzzm;
    @Bind(R.id.im_shfzfm)
    ImageView imShfzfm;
    @Bind(R.id.im_scsfz)
    ImageView imScsfz;
    @Bind(R.id.im_hz)
    ImageView imHz;
    @Bind(R.id.im_schz)
    ImageView imSchz;
    private PasspostBean mPasspostBean;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_kyc_photos;
    }

    @Override
    protected void initViewsAndEvents() {
        mPasspostBean = (PasspostBean) getIntent().getSerializableExtra("info");
        initToolbar();

        //身份证正面
        Glide.with(this).load(HttpApi.BASE_URL+mPasspostBean.getCardPhotoFront())
                .apply(new RequestOptions().placeholder(R.mipmap.kyc_defautl_icon)
                        .error(R.mipmap.kyc_defautl_icon)).into(imShfzzm);
        //身份证反面
        Glide.with(this).load(HttpApi.BASE_URL+mPasspostBean.getCardPhotoBackground())
                .apply(new RequestOptions().placeholder(R.mipmap.kyc_defautl_icon)
                        .error(R.mipmap.kyc_defautl_icon)).into(imShfzfm);
        //手持身份证
        Glide.with(this).load(HttpApi.BASE_URL+mPasspostBean.getCardPhotoHand())
                .apply(new RequestOptions().placeholder(R.mipmap.kyc_defautl_icon)
                        .error(R.mipmap.kyc_defautl_icon)).into(imScsfz);
        //护照照片
        Glide.with(this).load(HttpApi.BASE_URL+mPasspostBean.getPassportPhoto())
                .apply(new RequestOptions().placeholder(R.mipmap.kyc_defautl_icon)
                        .error(R.mipmap.kyc_defautl_icon)).into(imHz);
        //手持护照照片
        Glide.with(this).load(HttpApi.BASE_URL+mPasspostBean.getPassportPhotoHand())
                .apply(new RequestOptions().placeholder(R.mipmap.kyc_defautl_icon)
                        .error(R.mipmap.kyc_defautl_icon)).into(imSchz);
    }

    private void initToolbar() {
        ArthurToolBar arthurToolBar = findViewById(R.id.tb_setting);
        arthurToolBar.setTitleText(getString(R.string.photo_info));
        arthurToolBar.setOnLeftClickListener(this);
    }

    @OnClick(R.id.im_shfzzm)
    void shfzzm(){
        startActivity(new Intent(KYCPhotosActivity.this,PhotoVSingleActivity.class)
                .putExtra("url",HttpApi.BASE_URL+mPasspostBean.getCardPhotoFront()));
    }
    @OnClick(R.id.im_shfzfm)
    void shfzfm(){
        startActivity(new Intent(KYCPhotosActivity.this,PhotoVSingleActivity.class)
                .putExtra("url",HttpApi.BASE_URL+mPasspostBean.getCardPhotoBackground()));
    }
    @OnClick(R.id.im_scsfz)
    void scsfz(){
        startActivity(new Intent(KYCPhotosActivity.this,PhotoVSingleActivity.class)
                .putExtra("url",HttpApi.BASE_URL+mPasspostBean.getCardPhotoHand()));
    }
    @OnClick(R.id.im_hz)
    void hz(){
        startActivity(new Intent(KYCPhotosActivity.this,PhotoVSingleActivity.class)
                .putExtra("url",HttpApi.BASE_URL+mPasspostBean.getPassportPhoto()));
    }
    @OnClick(R.id.im_schz)
    void schz(){
        startActivity(new Intent(KYCPhotosActivity.this,PhotoVSingleActivity.class)
                .putExtra("url",HttpApi.BASE_URL+mPasspostBean.getPassportPhotoHand()));
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
