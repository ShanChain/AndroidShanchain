package com.shanchain.data.common.h5;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.common.R;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.SystemUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.LongToIntFunction;

public class SCWebViewActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvWebBack;
    private TextView mTvWebTbTitle;
    private WebView mWbSc;
    private String mTitle;
    private String mUrl;
    private String token,characterId,userId;
    private ProgressBar mPbWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scweb_view);
        ActivityStackManager.getInstance().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initStatusBar();
        initView();
        Intent intent = getIntent();
        String webParams = intent.getStringExtra("webParams");
        mTitle = JSONObject.parseObject(webParams).getString("title");
        mUrl = JSONObject.parseObject(webParams).getString("url");
        initWeb();
    }

    private void initWeb() {
        token = SCCacheUtils.getCacheToken();
        characterId = SCCacheUtils.getCacheCharacterId();
        userId = SCCacheUtils.getCacheUserId();
        mTvWebTbTitle.setText(mTitle);
        WebSettings settings = mWbSc.getSettings();
        settings.setJavaScriptEnabled(true);
        final Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        map.put("characterId", characterId);
        map.put("userId", userId);
        mWbSc.loadUrl(mUrl+"?token="+map.get("token")+"&characterId="+map.get("characterId")+"&userId="+map.get("userId"));
        mWbSc.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(mUrl,map);
                return true;
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.contains("toPrev=true")){
                    finish();
                }else if (url.contains("toLogin")){
                    Intent intent = new Intent();
                    intent.setAction(".ui.view.activity.login");
                    startActivity(intent);
                }
                return super.shouldInterceptRequest(view, url);
            }
        });

        mWbSc.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mPbWeb.setVisibility(View.GONE);
                } else {
                    mPbWeb.setVisibility(View.VISIBLE);
                    mPbWeb.setProgress(newProgress);
                }
            }
        });
    }

    public boolean parseScheme(String url) {
        if (url.contains("platformapi/startapp")) {
            return true;
        } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                && (url.contains("platformapi") && url.contains("startapp"))) {
            return true;
        } else {
            return false;
        }
    }

    private void initView() {
        mIvWebBack = (ImageView) findViewById(R.id.iv_web_back);
        mTvWebTbTitle = (TextView) findViewById(R.id.tv_web_tb_title);
        mPbWeb = (ProgressBar) findViewById(R.id.pb_web);
        mWbSc = (WebView) findViewById(R.id.wb_sc);
        mIvWebBack.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackManager.getInstance().finishActivity(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWbSc.canGoBack()) {
                mWbSc.goBack();
                return true;
            } else {
//                finish();
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void finish() {
        super.finish();
        ActivityStackManager.getInstance().finishActivity(this);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SystemUtils.setImmersiveStatusBar_API21(this, getResources().getColor(R.color.colorWhite));
            SystemUtils.setStatusBarLightMode_API23(this);
        }
        SystemUtils.MIUISetStatusBarLightModeWithWhiteColor(this, getWindow(), true);
        SystemUtils.FlymeSetStatusBarLightModeWithWhiteColor(this, getWindow(), true);
    }

}
