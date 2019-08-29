package com.shanchain.shandata.ui.view.activity.basepara;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.utils.PrefUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by WealChen
 * Date : 2019/8/28
 * Describe :参数切换界面
 */
public class BaseParaActivity extends BaseActivity {
    @Bind(R.id.bt_shit_web)
    Button btShitWeb;
    @Bind(R.id.et_web)
    EditText etWeb;
    @Bind(R.id.et_wallet)
    EditText etWallet;
    @Bind(R.id.et_money)
    EditText etMoney;
    private boolean isProduct = true;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_basepara;
    }

    @Override
    protected void initViewsAndEvents() {
        String baseUrl = PrefUtils.getString(this, Constants.SP_KEY_BASE_PARA,Constants.SC_HOST_RELEASE);
        String walletUrl = PrefUtils.getString(this, Constants.SP_KEY_BASE_PARA_WALLET,Constants.SC_WALLET_RELEASE);
        String money = PrefUtils.getString(this, Constants.SP_KEY_BASE_PARA_MONEY,Constants.PAYFOR_MINING_MONEY);
        etWeb.setText(baseUrl);
        etWallet.setText(walletUrl);
        etMoney.setText(money);

    }

    @OnClick(R.id.bt_shit_web)
    void changeWeb(){
        if(isProduct){
            PrefUtils.putString(this, Constants.SP_KEY_BASE_PARA,Constants.SC_HOST_TEST);
            PrefUtils.putString(this, Constants.SP_KEY_BASE_PARA_WALLET,Constants.SC_WALLET_TEST);
            PrefUtils.putString(this, Constants.SP_KEY_BASE_PARA_MONEY,"0.001");
            isProduct = false;
            etWeb.setText(Constants.SC_HOST_TEST);
            etWallet.setText(Constants.SC_WALLET_TEST);
            etMoney.setText("0.001");
        }else {
            PrefUtils.putString(this, Constants.SP_KEY_BASE_PARA,Constants.SC_HOST_RELEASE);
            PrefUtils.putString(this, Constants.SP_KEY_BASE_PARA_WALLET,Constants.SC_WALLET_RELEASE);
            PrefUtils.putString(this, Constants.SP_KEY_BASE_PARA_MONEY,Constants.PAYFOR_MINING_MONEY);
            isProduct = true;
            etWeb.setText(Constants.SC_HOST_RELEASE);
            etWallet.setText(Constants.SC_WALLET_RELEASE);
            etMoney.setText(Constants.PAYFOR_MINING_MONEY);
        }
    }

}
