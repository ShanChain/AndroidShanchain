package com.shanchain.shandata.ui.view.activity.square;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.Bind;

/**
 * Created by WealChen
 * Date : 2019/8/8
 * Describe :支付成功回调界面
 */
public class PayforSuccessActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener{
    @Bind(R.id.tb_register)
    ArthurToolBar mTbRegister;
    @Bind(R.id.iv_scode)
    ImageView ivScode;
    @Bind(R.id.tv_code_num)
    TextView tvCodeNum;
    @Bind(R.id.btn_reset_sure)
    Button btnResetSure;
    @Bind(R.id.tv_go_mining)
    TextView tvGoMining;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_payfor_success;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        Bitmap bitmap = CodeUtils.createImage(tvCodeNum.getText().toString().trim(), 400, 400, null);
        ivScode.setImageBitmap(bitmap);
    }

    private void initToolBar() {
        mTbRegister.setTitleTextColor(Color.BLACK);
        mTbRegister.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        mTbRegister.setOnLeftClickListener(this);
        mTbRegister.setTitleText("支付完成");

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
