package com.shanchain.shandata.rn.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.shanchain.common.R;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.SetWalletPasswordActivity;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SystemUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.view.activity.login.LoginActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;

//import com.example.test_webview_demo.utils.X5WebView;

public class SCWebViewXYActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvWebBack;
    private TextView mTvWebTbTitle;
    private WebView mWbSc;
    //    private com.tencent.smtt.sdk.WebView mWbSc;
    //    private X5WebView mX5WebView;
    private String mTitle;
    private String mUrl;
    private String token, characterId, userId;
    private ProgressBar mPbWeb;
    private Map<String, String> map;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final int RESULT_CODE_PICK_FROM_ALBUM_BELLOW_LOLLILOP = 1;
    private final int RESULT_CODE_PICK_FROM_ALBUM_ABOVE_LOLLILOP = 2;
    private String url = "";//这里添加含有图片上传功能的H5页面访问地址即可。
    String compressPath = "";
    private TextView mMTextGo;
    private EditText mMEditUrl;
    public static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scweb_view_new);
        ActivityStackManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mActivity = this;
        initStatusBar();
        initView();
        Intent intent = getIntent();
        String webParams = intent.getStringExtra("webParams");
        LogUtils.d("webPararms", "" + webParams);
        if (intent.getStringExtra("url") != null) {
            mTitle = intent.getStringExtra("title");
            mUrl = intent.getStringExtra("url");
        } else {
            mTitle = webParams == null ? getResources().getString(R.string.nav_my_wallet) : JSONObject.parseObject(webParams).getString("title");
            mUrl = webParams != null ? JSONObject.parseObject(webParams).getString("url") : HttpApi.SEAT_WALLET;
//        mUrl = "http://m.qianqianshijie.com/wallet/Chargebond";
        }
        initWeb();
    }

    private void initWeb() {
//        LogUtils.d("当前活动Activity",ActivityStackManager.getInstance().getCurrentActivity().getLocalClassName());
        mWbSc = findViewById(R.id.wb_sc);
//        com.tencent.smtt.sdk.WebSettings settings = mWbSc.getSettings();
        WebSettings settings = mWbSc.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        settings.setJavaScriptEnabled(true);//是否允许JavaScript脚本运行，默认为false。设置true时，会提醒可能造成XSS漏洞
        settings.setSupportZoom(false);//是否可以缩放，默认true
        settings.setBuiltInZoomControls(false);//是否显示缩放按钮，默认false
        settings.setUseWideViewPort(false);//设置此属性，可任意比例缩放。大视图模式
        settings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setAppCacheEnabled(true);//是否使用缓存
        settings.setDomStorageEnabled(true);//开启本地DOM存储
//        settings.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        settings.setLoadsImagesAutomatically(true); // 加载图片
        settings.setMediaPlaybackRequiresUserGesture(false);//播放音频，多媒体需要用户手动？设置为false为可自动播放
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWbSc.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        mWbSc.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient());
        mWbSc.setWebChromeClient(new MyWebChromeClient());
        mWbSc.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
//            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Map headers = request.getRequestHeaders();
                    String urlPath = request.getUrl().getPath();
                    String loadUrl = request.getUrl().toString();
                    String url = webView.getUrl();
                    LogUtils.d("Url", url + "");
                    LogUtils.d("UrlPath", urlPath + "");
                    LogUtils.d("loadUrl", loadUrl + "");
                    if (loadUrl.contains("toPwd=true")) {
                        Intent intent = new Intent(SCWebViewXYActivity.this, SetWalletPasswordActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    }
                    if (loadUrl.contains("toPrev=true")) {
                        LogUtils.d("toPrev", url + "");
                        finish();
                        return true;
                    }
                    if (loadUrl.contains("comfirm=true")) {
                        mWbSc.loadUrl(mUrl + "?token=" + map.get("token") + "&characterId=" + map.get("characterId") + "&userId=" + map.get("userId"));
//                    mWbSc.reload();
                        LogUtils.d("comfirm", url + "");
                        return true;
                    }
                    if (loadUrl.contains("toLogin=true")) {
                        String url1 = url;
                        Intent intent = new Intent(SCWebViewXYActivity.this, LoginActivity.class);
                        intent.putExtra("wallet", "wallet");
                        startActivity(intent);
                        LogUtils.d("toLogin", url + "");
                        ActivityStackManager.getInstance().finishAllActivity();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            public void onPageStarted(com.tencent.smtt.sdk.WebView view, String s, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
//            public void onPageFinished(com.tencent.smtt.sdk.WebView webView, String s) {
                LogUtils.d("webView加载完成：", "url:" + s);
                super.onPageFinished(webView, s);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView view, String url) {
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                if (url.contains("toPwd=true")) {
                    Intent intent = new Intent(SCWebViewXYActivity.this, SetWalletPasswordActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (url.contains("toPrev=true")) {
                    LogUtils.d("toPrev", url + "");
                    finish();
                    return true;
                } else if (url.contains("comfirm=true")) {
                    mWbSc.loadUrl(mUrl + "?token=" + map.get("token") + "&characterId=" + map.get("characterId") + "&userId=" + map.get("userId"));
//                    mWbSc.reload();
                    LogUtils.d("comfirm", url + "");
                    return true;
                } else if (url.contains("toLogin=true")) {
                    String url1 = url;
                    Intent intent = new Intent(SCWebViewXYActivity.this, LoginActivity.class);
                    intent.putExtra("wallet", "wallet");
                    startActivity(intent);
                    finish();
                    LogUtils.d("toLogin", url + "");
                    return true;
                } else {
//                }
//                mWbSc.loadUrl("http://www.baidu.com");
                    mWbSc.loadUrl(url);
                    return false;
                }
            }


        });
        mTvWebTbTitle.setText(mTitle);
        /**
         * 防止token过期，获取最新token后再加载webView
         */
        mWbSc.loadUrl(mUrl);
        LogUtils.d("mUrl", "" + mWbSc.getUrl());
        LogUtils.d("mTitle", "" + mTitle);

        //下载监听
        mWbSc.setDownloadListener(new android.webkit.DownloadListener()

        {
            @Override
            public void onDownloadStart(String url, String userAgent, String
                    contentDisposition, String mimetype, long contentLength) {

            }
        });

        mWbSc.setOnLongClickListener(new View.OnLongClickListener()

        {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult result = mWbSc.getHitTestResult();
//                com.tencent.smtt.sdk.WebView.HitTestResult result = mWbSc.getHitTestResult();
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
                        final String url = result.getExtra();
//                        ToastUtils.showToastLong(SCWebViewActivity.this, "" + url);
//                        if (url.indexOf("data:image/*;base64,") != -1) {
//                            byte[] bitmapArray = Base64.decode(url.split(",")[1], Base64.DEFAULT);
//                            ToastUtils.showToastLong(SCWebViewActivity.this, "Base64字符串：");
//                            //使用io流保存图片
//                            FileOutputStream fos = null;
//                            File passwordImage = null;
//                            String filename = ImageUtils.getTempFileName();
//                            try {
//                                if (filename != null) {
//                                    String appPath = getApplicationContext().getFilesDir().getAbsolutePath();//app文件路径
//                                    String filePath = ImageUtils.getSDPath() + "/shanchain/" + filename;
//                                    passwordImage = new File(filePath);
//                                    fos = new FileOutputStream(passwordImage);
//                                    fos.write(bitmapArray);
//                                    fos.flush();
//                                    fos.close();
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            } finally {
//                                displayToGallery(SCWebViewActivity.this, passwordImage);
//
//                            }
//                        }
                        StandardDialog standardDialog = new StandardDialog(SCWebViewXYActivity.this);
                        standardDialog.setStandardTitle("是否保存图片");
                        standardDialog.setSureText("保存");
                        standardDialog.setCancelText("取消");
                        standardDialog.setCallback(new Callback() {
                            @Override
                            public void invoke() {
                                ThreadUtils.runOnSubThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        File saveFile = null;
                                        try {
                                            saveFile = Glide.with(SCWebViewXYActivity.this).asFile().load(url).submit().get();
                                            displayToGallery(SCWebViewXYActivity.this, saveFile);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                ToastUtils.showToastLong(SCWebViewXYActivity.this, "保存成功");
                            }
                        }, new Callback() {
                            @Override
                            public void invoke() {

                            }
                        });
                        standardDialog.show();
                        return true;
                    case WebView.HitTestResult.UNKNOWN_TYPE: //未知
                        break;
                }
                return false;
            }
        });
    }

    public static void displayToGallery(Context context, File photoFile) {
        if (photoFile == null || !photoFile.exists()) {
            return;
        }
        String photoPath = photoFile.getAbsolutePath();
        String photoName = photoFile.getName();
        // 把文件插入到系统图库
        try {
            ContentResolver contentResolver = context.getContentResolver();
            MediaStore.Images.Media.insertImage(contentResolver, photoPath, photoName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + photoPath)));
    }

    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getRealFilePath(final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 选择照片后结束
     *
     * @param data
     */
    private Uri afterChosePic(Intent data) {
        if (data == null) {
            return null;
        }
        String path = getRealFilePath(data.getData());
        if (path == null) {//取消选择图片的时候，返回回来的Intent不为空，但getData为空，这里加个判空
            return null;
        }
        String[] names = path.split("\\.");
        String endName = null;
        if (names != null) {
            endName = names[names.length - 1];
        }
        if (endName != null) {
            compressPath = compressPath.split("\\.")[0] + "." + endName;
        }
        File newFile;
        try {
            newFile = new File(path);
        } catch (Exception e) {
            newFile = null;
        }
        return Uri.fromFile(newFile);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (null != data) {
//            LogUtils.d("" + data.getData().getPath());
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_CODE_PICK_FROM_ALBUM_BELLOW_LOLLILOP:
                if (mUploadMessage == null) {
                    return;
                }
                Uri result = data == null || resultCode != Activity.RESULT_OK ? null
                        : data.getData();
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }
                break;
            case RESULT_CODE_PICK_FROM_ALBUM_ABOVE_LOLLILOP:
                if (mUploadCallbackAboveL == null) {
                    return;
                }
//                    try {
//                        uri = afterChosePic(data);
//                        if (uri == null) {
                mUploadCallbackAboveL.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                mUploadCallbackAboveL = null;
//                            break;
//                        }
//                        if (mUploadCallbackAboveL != null && uri != null) {
//                            mUploadCallbackAboveL.onReceiveValue(new Uri[]{uri});
//                            mUploadCallbackAboveL = null;
//                        }
//                    } catch (Exception e) {
//                        mUploadCallbackAboveL = null;
//                        e.printStackTrace();
//                    }
                break;
//            }
        }
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
//        mWbSc = (WebView) findViewById(R.id.wb_sc);
        mMTextGo = findViewById(R.id.text_go_url);
        mMEditUrl = findViewById(R.id.edit_url);
        mWbSc = (WebView) findViewById(R.id.wb_sc);
//        mWbSc = (com.tencent.smtt.sdk.WebView) findViewById(R.id.wb_sc);
        mIvWebBack.setOnClickListener(this);
        mMTextGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMEditUrl.getText().toString() != null) {
                    mWbSc.loadUrl(mMEditUrl.getText().toString());
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (mWbSc != null) {
            mWbSc.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWbSc.clearHistory();

            ((ViewGroup) mWbSc.getParent()).removeView(mWbSc);
            mWbSc.destroy();
            mWbSc = null;
        }
        ActivityStackManager.getInstance().finishActivity(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String loadUrl = mWbSc.getUrl();
            LogUtils.d("webView", loadUrl + "");
            if (mWbSc.canGoBack()) {
                if (loadUrl.contains("walletCenter")) {
                    finish();
                }
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

    /**
     * 打开图库,同时处理图片（项目业务需要统一命名）
     */
    private void selectImage(int resultCode) {
        compressPath = Environment.getExternalStorageDirectory().getPath() + "/QWB/temp";
        File file = new File(compressPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        compressPath = compressPath + File.separator + "compress.png";
        File image = new File(compressPath);
        if (image.exists()) {
            image.delete();
        }
        Intent intent = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, resultCode);

    }

    class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mPbWeb.setVisibility(View.GONE);
            } else {
                mPbWeb.setVisibility(View.VISIBLE);
                mPbWeb.setProgress(newProgress);
            }
        }

        //openFileChooser（隐藏方法）仅适用android5.0以下的环境，android5.0及以上使用onShowFileChooser
        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType) {
            if (mUploadMessage != null)
                return;
            mUploadMessage = uploadMsg;
            selectImage(RESULT_CODE_PICK_FROM_ALBUM_BELLOW_LOLLILOP);
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        // For Android > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            openFileChooser(uploadMsg, acceptType);
        }

        // For Android 5.0+
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            mUploadCallbackAboveL = filePathCallback;
            selectImage(RESULT_CODE_PICK_FROM_ALBUM_ABOVE_LOLLILOP);
            return true;
        }
    }
}
