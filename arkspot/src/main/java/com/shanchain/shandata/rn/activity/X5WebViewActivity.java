//package com.shanchain.shandata.rn.activity;
//
//import android.app.Activity;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.annotation.RequiresApi;
//import android.support.v7.app.AppCompatActivity;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSONObject;
//import com.bumptech.glide.Glide;
//import com.example.test_webview_demo.utils.X5WebView;
//import com.shanchain.common.R;
//import com.shanchain.data.common.base.ActivityStackManager;
//import com.shanchain.data.common.base.Callback;
//import com.shanchain.data.common.cache.SCCacheUtils;
//import com.shanchain.data.common.net.HttpApi;
//import com.shanchain.data.common.ui.SetWalletPasswordActivity;
//import com.shanchain.data.common.ui.widgets.StandardDialog;
//import com.shanchain.data.common.utils.LogUtils;
//import com.shanchain.data.common.utils.SystemUtils;
//import com.shanchain.data.common.utils.ThreadUtils;
//import com.shanchain.data.common.utils.ToastUtils;
//import com.shanchain.shandata.base.MyApplication;
//import com.shanchain.shandata.ui.view.activity.login.LoginActivity;
//import com.tencent.smtt.sdk.DownloadListener;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//
//public class X5WebViewActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private ImageView mIvWebBack;
//    private TextView mTvWebTbTitle;
//    //    private WebView mWbSc;
//    private com.tencent.smtt.sdk.WebView mWbSc;
//    private X5WebView mX5WebView;
//    private String mTitle;
//    private String mUrl;
//    private String token, characterId, userId;
//    private ProgressBar mPbWeb;
//    private Map<String, String> map;
//
//    private ValueCallback<Uri> mUploadMessage;
//    private ValueCallback<Uri[]> mUploadCallbackAboveL;
//    private final int RESULT_CODE_PICK_FROM_ALBUM_BELLOW_LOLLILOP = 1;
//    private final int RESULT_CODE_PICK_FROM_ALBUM_ABOVE_LOLLILOP = 2;
//    private String url = "";//这里添加含有图片上传功能的H5页面访问地址即可。
//    String compressPath = "";
//    private TextView mMTextGo;
//    private EditText mMEditUrl;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_x5web_view);
//        ActivityStackManager.getInstance().addActivity(this);
//        Intent intent = getIntent();
//        String webParams = intent.getStringExtra("webParams");
//        LogUtils.d("webPararms", "" + webParams);
//        if (intent.getStringExtra("url") != null) {
//            mTitle = intent.getStringExtra("title");
//            mUrl = intent.getStringExtra("url");
//        } else {
//            mTitle = webParams == null ? getResources().getString(R.string.nav_my_wallet) : JSONObject.parseObject(webParams).getString("title");
//            mUrl = webParams != null ? JSONObject.parseObject(webParams).getString("url") : HttpApi.SEAT_WALLET;
////        mUrl = "http://m.qianqianshijie.com/wallet/Chargebond";
//        }
//        initView();
//    }
//
//
//    public static void displayToGallery(Context context, File photoFile) {
//        if (photoFile == null || !photoFile.exists()) {
//            return;
//        }
//        String photoPath = photoFile.getAbsolutePath();
//        String photoName = photoFile.getName();
//        // 把文件插入到系统图库
//        try {
//            ContentResolver contentResolver = context.getContentResolver();
//            MediaStore.Images.Media.insertImage(contentResolver, photoPath, photoName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        // 最后通知图库更新
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + photoPath)));
//    }
//
//    public boolean parseScheme(String url) {
//        if (url.contains("platformapi/startapp")) {
//            return true;
//        } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
//                && (url.contains("platformapi") && url.contains("startapp"))) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private void initView() {
//        mIvWebBack = (ImageView) findViewById(R.id.iv_web_back);
//        mTvWebTbTitle = (TextView) findViewById(R.id.tv_web_tb_title);
//        mPbWeb = (ProgressBar) findViewById(R.id.pb_web);
////        mWbSc = (WebView) findViewById(R.id.wb_sc);
//        mMTextGo = findViewById(R.id.text_go_url);
//        mMEditUrl = findViewById(R.id.edit_url);
//        mX5WebView = findViewById(R.id.wb_sc);
//        mIvWebBack.setOnClickListener(this);
//        mMTextGo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mMEditUrl.getText().toString() != null) {
//                    mWbSc.loadUrl(mMEditUrl.getText().toString());
//                }
//            }
//        });
//        if (mTitle.equals("交易推送")) {
//            mUrl = mUrl + "&token=" + SCCacheUtils.getCacheToken() + "&JPush=JPush";
//        } else {
//            mUrl = mUrl + "?token=" + SCCacheUtils.getCacheToken() + "&characterId=" + SCCacheUtils.getCacheCharacterId() + "&userId=" + SCCacheUtils.getCacheUserId() + "&channel=" + MyApplication.getAppMetaData(X5WebViewActivity.this, "UMENG_CHANNEL");
//            ;
//        }
//        mX5WebView.loadUrl(mUrl);
////        mX5WebView.loadUrl("http://h5.qianqianshijie.com/wallet?token=100561_40ec7b2de4934896a57dd4d55cc01fe71557248389844&characterId=207&userId=100561&channel=arkspot");
//
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (mWbSc != null) {
//            mWbSc.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//            mWbSc.clearHistory();
//
//            ((ViewGroup) mWbSc.getParent()).removeView(mWbSc);
//            mWbSc.destroy();
//            mWbSc = null;
//        }
//        ActivityStackManager.getInstance().finishActivity(this);
//        super.onDestroy();
//    }
//
//    @Override
//    public void onClick(View v) {
//        finish();
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            String loadUrl = mWbSc.getUrl();
//            LogUtils.d("webView", loadUrl + "");
//            if (mWbSc.canGoBack()) {
//                if (loadUrl.contains("walletCenter")) {
//                    finish();
//                }
//                mWbSc.goBack();
//                return true;
//            } else {
//                finish();
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        ActivityStackManager.getInstance().finishActivity(this);
//    }
//
//    /**
//     * 打开图库,同时处理图片（项目业务需要统一命名）
//     */
//    private void selectImage(int resultCode) {
//        compressPath = Environment.getExternalStorageDirectory().getPath() + "/QWB/temp";
//        File file = new File(compressPath);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        compressPath = compressPath + File.separator + "compress.png";
//        File image = new File(compressPath);
//        if (image.exists()) {
//            image.delete();
//        }
//        Intent intent = new Intent(
//                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, resultCode);
//
//    }
//
//}
