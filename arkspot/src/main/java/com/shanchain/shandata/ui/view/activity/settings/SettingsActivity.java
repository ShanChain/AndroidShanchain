package com.shanchain.shandata.ui.view.activity.settings;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.EventBusObject;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.net.UpdateAppHttpUtil;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.view.activity.jmessageui.FeedbackActivity;
import com.shanchain.shandata.ui.view.activity.login.LoginActivity;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.service.DownloadService;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

public class SettingsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tv_account)
    TextView tvAccount;
    @Bind(R.id.tv_account_security)
    TextView tvAccountSecurity;
    @Bind(R.id.iv_arrow)
    ImageView ivArrow;
    @Bind(R.id.relative_account)
    RelativeLayout relativeAccount;
    @Bind(R.id.tv_common)
    TextView tvCommon;
    @Bind(R.id.tv_message_push)
    TextView tvMessagePush;
    @Bind(R.id.switch_message_push)
    Switch switchMessagePush;
    @Bind(R.id.relative_message_push)
    RelativeLayout relativeMessagePush;
    @Bind(R.id.tv_feedback)
    TextView tvFeedback;
    @Bind(R.id.iv_feedback_arrow)
    ImageView ivFeedbackArrow;
    @Bind(R.id.relative_feedback)
    RelativeLayout relativeFeedback;
    @Bind(R.id.tv_about)
    TextView tvAbout;
    @Bind(R.id.iv_about_arrow)
    ImageView ivAboutArrow;
    @Bind(R.id.relative_app_about)
    RelativeLayout relativeAppAbout;
    @Bind(R.id.tv_update)
    TextView tvUpdate;
    @Bind(R.id.tv_app_version_code)
    TextView tvAppVersionCode;
    @Bind(R.id.iv_update_arrow)
    ImageView ivUpdateArrow;
    @Bind(R.id.relative_app_update)
    RelativeLayout relativeAppUpdate;
    @Bind(R.id.relative_others)
    RelativeLayout relativeOthers;
    @Bind(R.id.tv_logout)
    TextView tvLogout;
    @Bind(R.id.relative_logout)
    RelativeLayout relativeLogout;
    @Bind(R.id.tv_free_password)
    TextView tvFreePassword;
    @Bind(R.id.switch_free_password)
    Switch switchFreePassword;

    private CustomDialog customDialog;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolbar();
        PackageManager packageManager = getApplicationContext().getPackageManager();
        String packagerName = getApplicationContext().getPackageName();
        customDialog = new CustomDialog(mContext, true, 1.0,
                R.layout.dialog_bottom_wallet_password,
                new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
        try {
            String versionCode = packageManager.getPackageInfo(packagerName, 0).versionName;
            tvAppVersionCode.setText("V" + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        initCurrentUserStatus();
        isRealName();
    }

    private void initToolbar() {
        ArthurToolBar arthurToolBar = findViewById(R.id.tb_setting);
        arthurToolBar.setTitleText(getResources().getString(R.string.nav_setting));
        arthurToolBar.setOnLeftClickListener(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void isRealName() {
        SCHttpUtils.getAndToken()
                .url(HttpApi.IS_REAL_NAME)
                .build()
                .execute(new SCHttpStringCallBack(mContext, new StandardDialog(mContext)) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToast(mContext, "网络异常");
                            }
                        });
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        final String code = SCJsonUtils.parseCode(response);
                        final String msg = SCJsonUtils.parseMsg(response);
                        if (NetErrCode.SUC_CODE.equals(code) || NetErrCode.COMMON_SUC_CODE.equals(code)) {
                            isRealName = SCJsonUtils.parseBoolean(response, "data");
                            MyApplication.setRealName(isRealName);
                        } else {
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showToast(mContext, code + ":" + msg);
                                }
                            });
                        }


                    }
                });

    }

    @Override
    protected void initCurrentUserStatus() {
        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHARACTER_GET_CURRENT)
                .build()
                .execute(new SCHttpStringCallBack(mContext, new StandardDialog(mContext)) {
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
                            CharacterInfo characterInfo = JSONObject.parseObject(character, CharacterInfo.class);
                            if (!TextUtils.isEmpty(character)) {
                                isBindPwd = SCJsonUtils.parseBoolean(character, "isBindPwd");
                                allowNotify = SCJsonUtils.parseBoolean(character, "allowNotify");
                                MyApplication.setAllowNotify(allowNotify);
                                MyApplication.setBindPwd(isBindPwd);
                                setBindPwd(isBindPwd);
                                if (isBindPwd) {
                                    switchFreePassword.setChecked(true);
                                } else {
                                    switchFreePassword.setChecked(false);
                                }
                                if (allowNotify) {
                                    switchMessagePush.setChecked(true);
                                } else {
                                    switchMessagePush.setChecked(false);
                                }
                                switchMessagePush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            JPushInterface.resumePush(getApplicationContext());
                                        } else {
                                            JPushInterface.stopPush(getApplicationContext());
                                        }
                                    }
                                });
                                switchFreePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            EventBusObject busObject = new EventBusObject(NetErrCode.WALLET_PHOTO, customDialog);
                                            EventBus.getDefault().post(busObject);
                                        } else {
                                            EventBusObject busObject = new EventBusObject(NetErrCode.WALLET_PHOTO, customDialog);
                                            EventBus.getDefault().post(busObject);
                                        }
//                                        SCHttpUtils.get()
//                                                .url(HttpApi.WALLET_FREE_PASSWORD)
//                                                .addParams("bind", "" + String.valueOf(bind))
//                                                .build()
//                                                .execute(new SCHttpStringCallBack() {
//                                                    @Override
//                                                    public void onError(Call call, Exception e, int id) {
//
//                                                    }
//
//                                                    @Override
//                                                    public void onResponse(String response, int id) {
//                                                        MyApplication.setBindPwd(bind);
//                                                        ThreadUtils.runOnMainThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                mStandardDialog.dismiss();
//                                                            }
//                                                        });
//                                                    }
//                                                });
                                    }
                                });
                            }
                        }
                    }
                });
    }

    @OnClick({R.id.relative_account, R.id.relative_message_push, R.id.relative_feedback, R.id.relative_app_about, R.id.relative_app_update, R.id.relative_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.relative_account:
                readyGo(AccountSecurityActivity.class);
                break;
            case R.id.relative_message_push:

                break;
            case R.id.relative_feedback:
                readyGo(FeedbackActivity.class);
                break;
            case R.id.relative_app_about:
                readyGo(AboutAppActivity.class);
                break;
            case R.id.relative_app_update:
                SCHttpUtils.postWithUserId()
                        .url(HttpApi.OSS_APK_GET_LASTEST)
                        .addParams("type", "android")
                        .build()
                        .execute(new SCHttpStringCallBack() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                String code = SCJsonUtils.parseCode(response);
                                String data = SCJsonUtils.parseData(response);
                                if (NetErrCode.COMMON_SUC_CODE.equals(code)) {
                                    //获取当前版本号
                                    PackageManager packageManager = getApplicationContext().getPackageManager();
                                    String packagerName = getApplicationContext().getPackageName();
                                    String versionCode = null;
                                    try {
                                        versionCode = packageManager.getPackageInfo(packagerName, 0).versionName;
                                        //服务端版本号
                                        String serviceVersion = SCJsonUtils.parseString(data, "version");
                                        String intro = SCJsonUtils.parseString(data, "intro"); //更新内容
                                        String url = SCJsonUtils.parseString(data, "url"); //下载连接
                                        String title = SCJsonUtils.parseString(data, "title"); //下载标题
                                        tvAppVersionCode.setText("V" + serviceVersion);
                                        if (versionCode.equals(serviceVersion)) {
                                            ToastUtils.showToast(SettingsActivity.this, "当前已是最新版本");
                                        } else {
                                            showUpdateDialog(url, title, intro);
                                        }
                                    } catch (PackageManager.NameNotFoundException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        });

                break;
            case R.id.relative_logout:
                readyGo(LoginActivity.class);
                ActivityStackManager.getInstance().finishAllActivity();
                break;
        }
    }

    //版本更新提示弹窗
    private void showUpdateDialog(final String url, String title, String intro) {
        final StandardDialog dialog = new StandardDialog(this);
        dialog.setStandardTitle("" + title);
        dialog.setStandardMsg(intro + "");
        dialog.setSureText("立即更新");
        dialog.setCancelText("取消");
        dialog.setCallback(new Callback() {
            @Override
            public void invoke() {  //确定
                downLoadApk(url);
            }
        }, new Callback() {
            @Override
            public void invoke() {  //取消
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    //下载APK版本
    private void downLoadApk(String url) {
        UpdateAppBean updateAppBean = new UpdateAppBean();
        //设置 apk 的下载地址
        updateAppBean.setApkFileUrl(url);
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
            try {
                path = getExternalCacheDir().getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(path)) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            }
        } else {
            path = getCacheDir().getAbsolutePath();
        }
        //设置apk 的保存路径
        updateAppBean.setTargetPath(path);
        //实现网络接口，只实现下载就可以
        updateAppBean.setHttpManager(new UpdateAppHttpUtil());

        UpdateAppManager.download(this, updateAppBean, new DownloadService.DownloadCallback() {
            @Override
            public void onStart() {
//                HProgressDialogUtils.showHorizontalProgressDialog(JavaActivity.this, "下载进度", false);
                Log.d(TAG, "onStart() called");
            }

            @Override
            public void onProgress(float progress, long totalSize) {
//                HProgressDialogUtils.setProgress(Math.round(progress * 100));
                Log.d(TAG, "onProgress() called with: progress = [" + progress + "], totalSize = [" + totalSize + "]");

            }

            @Override
            public void setMax(long totalSize) {
                Log.d(TAG, "setMax() called with: totalSize = [" + totalSize + "]");
            }

            @Override
            public boolean onFinish(File file) {
//                HProgressDialogUtils.cancel();
                Log.d(TAG, "onFinish() called with: file = [" + file.getAbsolutePath() + "]");
                return true;
            }

            @Override
            public void onError(String msg) {
//                HProgressDialogUtils.cancel();
                Log.e(TAG, "onError() called with: msg = [" + msg + "]");
            }

            @Override
            public boolean onInstallAppAndAppOnForeground(File file) {
                Log.d(TAG, "onInstallAppAndAppOnForeground() called with: file = [" + file + "]");
                return false;
            }
        });

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
