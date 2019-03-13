package com.shanchain.shandata.rn.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
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
import com.bumptech.glide.Glide;
import com.shanchain.common.R;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SystemUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.data.common.ui.SetWalletPasswordActivity;
import com.shanchain.shandata.ui.view.activity.login.LoginActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;

public class SCWebViewActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvWebBack;
    private TextView mTvWebTbTitle;
    private WebView mWbSc;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scweb_view);
        ActivityStackManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        initStatusBar();
        initView();
        Intent intent = getIntent();
        String webParams = intent.getStringExtra("webParams");
        mTitle = webParams == null ? getResources().getString(R.string.nav_my_wallet) : JSONObject.parseObject(webParams).getString("title");
        mUrl = webParams != null ? JSONObject.parseObject(webParams).getString("url") : HttpApi.SEAT_WALLET;
//        mUrl = "http://m.qianqianshijie.com/wallet/Chargebond";
        initWeb();
    }

    private void initWeb() {
        mWbSc = findViewById(R.id.wb_sc);
        WebSettings settings = mWbSc.getSettings();
        settings.setJavaScriptEnabled(true);
        mTvWebTbTitle.setText(mTitle);
//        mWbSc.loadUrl(mUrl);//加载url
        if (mTitle.equals(getResources().getString(com.shanchain.common.R.string.nav_my_wallet) + "")) {
            SCHttpUtils.postWithUserId()
                    .url(HttpApi.CHARACTER_GET_CURRENT)
                    .build()
                    .execute(new SCHttpStringCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.d("网络错误");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            String code = JSONObject.parseObject(response).getString("code");
                            if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                if (TextUtils.isEmpty(data)) {
                                    return;
                                }
                                String character = JSONObject.parseObject(data).getString("characterInfo");
                                token = SCCacheUtils.getCacheToken();
                                CharacterInfo characterInfo = JSONObject.parseObject(character, CharacterInfo.class);
                                characterId = String.valueOf(characterInfo.getCharacterId());
                                userId = SCCacheUtils.getCacheUserId();
                                map = new HashMap<String, String>();
                                map.put("token", token);
                                map.put("characterId", characterId);
                                map.put("userId", userId);
                                mWbSc.loadUrl(mUrl + "?token=" + map.get("token") + "&characterId=" + map.get("characterId") + "&userId=" + map.get("userId"));
//                                mWbSc.loadUrl( "userId");
                            } else {
                                mWbSc.loadUrl(mUrl);
                            }
                        }
                    });
        } else {
            mWbSc.loadUrl(mUrl);//加载url
        }

        mWbSc.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
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
                        StandardDialog standardDialog = new StandardDialog(SCWebViewActivity.this);
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
                                            saveFile = Glide.with(SCWebViewActivity.this).asFile().load(url).submit().get();
                                            displayToGallery(SCWebViewActivity.this, saveFile);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                ToastUtils.showToastLong(SCWebViewActivity.this, "保存成功");
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


        mWbSc.setWebViewClient(new WebViewClient() {
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
                    if (loadUrl.contains("toPwd=true")) {
                        Intent intent = new Intent(SCWebViewActivity.this, SetWalletPasswordActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    }
                    if (loadUrl.contains("toPrev=true")) {
                        LogUtils.d("toPrev", url);
                        finish();
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
                        LogUtils.d("toLogin", url);
                        ActivityStackManager.getInstance().finishAllActivity();
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
                    if (url.contains("toPwd=true")) {
                        Intent intent = new Intent(SCWebViewActivity.this, SetWalletPasswordActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    }
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

        mWbSc.setWebChromeClient(new MyWebChromeClient());
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
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
