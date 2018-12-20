package com.shanchain.shandata.rn.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.common.R;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SystemUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.view.activity.login.LoginActivity;

import java.util.HashMap;
import java.util.Map;

public class SCWebViewActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvWebBack;
    private TextView mTvWebTbTitle;
    private WebView mWbSc;
    private String mTitle;
    private String mUrl;
    private String token, characterId, userId;
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
//        mUrl = "http://m.qianqianshijie.com/orderDetails?id=15450393575245641";
        initWeb();
    }

    private void initWeb() {
//        SCHttpUtils.postWithUserId()
//                .url(HttpApi.CHARACTER_GET_CURRENT)
//                .addParams()
        token = SCCacheUtils.getCacheToken();
        CharacterInfo characterInfo = JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(), CharacterInfo.class);
//        characterId = SCCacheUtils.getCacheCharacterId();
        characterId = String.valueOf(characterInfo.getCharacterId());
        userId = SCCacheUtils.getCacheUserId();
        mTvWebTbTitle.setText(mTitle);
        WebSettings settings = mWbSc.getSettings();
        settings.setJavaScriptEnabled(true);
        final Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        map.put("characterId", characterId);
        map.put("userId", userId);
        mWbSc.loadUrl(mUrl + "?token=" + map.get("token") + "&characterId=" + map.get("characterId") + "&userId=" + map.get("userId"));
//        mWbSc.loadUrl(mUrl);
        mWbSc.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                ToastUtils.showToast(SCWebViewActivity.this, "长安webView");
                LogUtils.d("webView", "长安webView");
                WebView.HitTestResult result = mWbSc.getHitTestResult();
                if (null == result)
                    return false;
                int type = result.getType();
                switch (type) {
                    case WebView.HitTestResult.EDIT_TEXT_TYPE: // 选中的文字类型
                        break;
                    case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                        break;
                    case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                        break;
                    case WebView.HitTestResult.GEO_TYPE: // 　地图类型
                        break;
                    case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                        break;
                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型

                    case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                        String url = result.getExtra();
                        ToastUtils.showToast(SCWebViewActivity.this,url);
//                        if (mOnSelectItemListener != null && url != null && URLUtil.isValidUrl(url)) {
//                            mOnSelectItemListener.onSelected(touchX, touchY, result.getType(), url);
//                        }
                        return true;
                    case WebView.HitTestResult.UNKNOWN_TYPE: //未知
                        break;
                }
                return false;
            }
        });
        mWbSc.setWebViewClient(new

                                       WebViewClient() {
                                           @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                           @Override
                                           public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                               Map headers = request.getRequestHeaders();
                                               String urlPath = request.getUrl().getPath();
                                               String loadUrl = request.getUrl().toString();
                                               String url = view.getUrl();
                                               LogUtils.d("Url", url);
                                               LogUtils.d("UrlPath", urlPath);
                                               LogUtils.d("loadUrl", loadUrl);
                                               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                   if (loadUrl.contains("toPrev=true")) {
                                                       finish();
                                                       LogUtils.d("toPrev", url);
                                                       return true;
                                                   }
                                                   if (loadUrl.contains("comfirm=true")) {
                                                       mWbSc.loadUrl(mUrl + "?token=" + map.get("token") + "&characterId=" + map.get("characterId") + "&userId=" + map.get("userId"));
//                    mWbSc.reload();
                                                       LogUtils.d("comfirm", url);
                                                       return true;
                                                   }
                                                   if (loadUrl.contains("toLogin=true")) {
                                                       String url1 = url;
                                                       Intent intent = new Intent(SCWebViewActivity.this, LoginActivity.class);
                                                       intent.putExtra("wallet", "wallet");
                                                       startActivity(intent);
                                                       finish();
                                                       LogUtils.d("toLogin", url);
                                                       return true;
                                                   }
                                               }

                                               return false;
                                           }

                                           @Override
                                           public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                               super.onPageStarted(view, url, favicon);
                                           }

                                           @Override
                                           public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                               if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                                   if (url.contains("toPrev=true")) {
                                                       finish();
                                                       LogUtils.d("toPrev", url);
                                                       return true;
                                                   }
                                                   if (url.contains("comfirm=true")) {
                                                       mWbSc.loadUrl(mUrl + "?token=" + map.get("token") + "&characterId=" + map.get("characterId") + "&userId=" + map.get("userId"));
//                    mWbSc.reload();
                                                       LogUtils.d("comfirm", url);
                                                       return true;
                                                   }
                                                   if (url.contains("toLogin=true")) {
                                                       String url1 = url;
                                                       Intent intent = new Intent(SCWebViewActivity.this, LoginActivity.class);
                                                       intent.putExtra("wallet", "wallet");
                                                       startActivity(intent);
                                                       finish();
                                                       LogUtils.d("toLogin", url);
                                                       return true;
                                                   }
                                               }
                                               return false;
                                           }

                                           @Nullable
                                           @Override
                                           public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                                               return super.shouldInterceptRequest(view, url);
                                           }
                                       });

        mWbSc.setWebChromeClient(new

                                         WebChromeClient() {
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
                finish();
                return true;
            }
        }
        return false;
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
