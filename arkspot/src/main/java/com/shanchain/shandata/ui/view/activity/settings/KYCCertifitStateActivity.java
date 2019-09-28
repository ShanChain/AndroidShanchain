package com.shanchain.shandata.ui.view.activity.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.PasspostBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WealChen
 * Date : 2019/9/26
 * Describe :
 */
public class KYCCertifitStateActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {
    @Bind(R.id.tb_setting)
    ArthurToolBar tbSetting;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.tv_real_name)
    TextView tvRealName;
    @Bind(R.id.tv_id_card)
    TextView tvIdCard;
    @Bind(R.id.tv_passpost)
    TextView tvPasspost;
    @Bind(R.id.ll_photos)
    LinearLayout llPhotos;
    @Bind(R.id.tv_reson)
    TextView tvReson;
    @Bind(R.id.bt_re_cert)
    Button btReCert;
    @Bind(R.id.ll_certifit_faile)
    LinearLayout llCertifitFaile;
    @Bind(R.id.rl_state)
    RelativeLayout rlState;
    @Bind(R.id.tv_photo)
    TextView tvPhoto;
    @Bind(R.id.im_state)
    ImageView imState;

    private PasspostBean mPasspostBean;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_kyc_status;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolbar();

        mPasspostBean = (PasspostBean) getIntent().getSerializableExtra("info");
        initData();
    }

    private void initToolbar() {
        ArthurToolBar arthurToolBar = findViewById(R.id.tb_setting);
        arthurToolBar.setTitleText(getString(R.string.kyc_identify));
        arthurToolBar.setOnLeftClickListener(this);
    }

    private void initData() {
        if (mPasspostBean.getRealStatus() == 0) {//审核中
            tvStatus.setText(R.string.kyc_review_ing);
            llCertifitFaile.setVisibility(View.GONE);
            imState.setBackgroundResource(R.mipmap.kyc_certifit_icon_ing);
            rlState.setBackgroundColor(this.getResources().getColor(R.color.kyc_state_bg_s));
            tvPhoto.setTextColor(this.getResources().getColor(R.color.kyc_state_bg_s));
        } else if (mPasspostBean.getRealStatus() == 1) {//审核通过
            tvStatus.setText(R.string.kyc_certifit_success);
            llCertifitFaile.setVisibility(View.GONE);
            imState.setBackgroundResource(R.mipmap.kyc_status_icon);
            rlState.setBackgroundColor(this.getResources().getColor(R.color.kyc_state_bg_s));
            tvPhoto.setTextColor(this.getResources().getColor(R.color.kyc_state_bg_s));
        } else {
            tvStatus.setText(R.string.certifit_failed);
            llCertifitFaile.setVisibility(View.VISIBLE);
            tvReson.setText(mPasspostBean.getFailReason());
            imState.setBackgroundResource(R.mipmap.kyc_certifit_faile);
            rlState.setBackgroundColor(this.getResources().getColor(R.color.text_color_n));
            tvPhoto.setTextColor(this.getResources().getColor(R.color.kyc_state_bg_f));
        }
        tvRealName.setText(mPasspostBean.getRealName());
        if(mPasspostBean.getIdcardNo().length()>8){
            tvIdCard.setText(mPasspostBean.getIdcardNo().substring(0,4)+"****"+mPasspostBean.getIdcardNo().substring(mPasspostBean.getIdcardNo().length()-4,mPasspostBean.getIdcardNo().length()));
        }else if(mPasspostBean.getIdcardNo().length()>4 && mPasspostBean.getIdcardNo().length()<9){
            tvIdCard.setText("****"+mPasspostBean.getIdcardNo().substring(mPasspostBean.getIdcardNo().length()-4,mPasspostBean.getIdcardNo().length()));
        }else {
            tvIdCard.setText("****");
        }
        if(mPasspostBean.getPassportNo().length()>8){
            tvPasspost.setText(mPasspostBean.getPassportNo().substring(0,4)+"****"+mPasspostBean.getPassportNo().substring(mPasspostBean.getPassportNo().length()-4,mPasspostBean.getPassportNo().length()));
        }else if(mPasspostBean.getPassportNo().length()>4 && mPasspostBean.getPassportNo().length()<9){
            tvPasspost.setText("****"+mPasspostBean.getPassportNo().substring(mPasspostBean.getPassportNo().length()-4,mPasspostBean.getPassportNo().length()));
        }else {
            tvPasspost.setText("****");
        }
    }

    //重新认证
    @OnClick(R.id.bt_re_cert)
    void recertifit(){
        startActivity(new Intent(KYCCertifitStateActivity.this,KYCCertificatActivity.class)
                        .putExtra("info",mPasspostBean));
        finish();
    }

    //证件照片
    @OnClick(R.id.ll_photos)
    void checkPhoto(){
        startActivity(new Intent(KYCCertifitStateActivity.this,KYCPhotosActivity.class).putExtra("info",mPasspostBean));
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
