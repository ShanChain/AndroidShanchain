package com.shanchain.data.common.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.common.R;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

public class WalletEnterActivity extends AppCompatActivity implements ArthurToolBar.OnLeftClickListener {
    private Button enterWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_enter);
        initToolBar();
        enterWallet = findViewById(R.id.btn_wallet_go);
        enterWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("activity://qianqianshijie:80/webview"));
                JSONObject obj = new JSONObject();
                startActivity(intent);
                finish();

            }
        });
    }

    private void initToolBar() {
        ArthurToolBar toolBar = findViewById(R.id.tb_about);
        toolBar.setTitleText("进入钱包");
        toolBar.setOnLeftClickListener(this);
    }


    @Override
    public void onLeftClick(View v) {
        try {
            Class clazz = Class.forName("com.shanchain.shandata.rn.activity.SCWebViewActivity");
            ActivityStackManager.getInstance().finishActivity(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finish();

    }
}
