package com.shanchain.shandata.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.EventBusObject;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.event.EventMessage;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskListActivity;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 懒加载fragment
 */

public abstract class BaseFragment extends Fragment {
    public String TAG = null;

    /**
     * 描述：fragment所依赖的activity
     */
    public MainActivity mActivity;
    public TaskListActivity mTaskListActivity;
    private CustomDialog mCustomDialog;
    private StandardDialog mStandardDialog;
    private String authCode,registrationId;

    /**
     * 描述：fragment创建时调用的方法,在此获取fragment所依赖的activitty对象
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mTaskListActivity = (TaskListActivity) getActivity();
        TAG = this.getClass().getSimpleName();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        registrationId = JPushInterface.getRegistrationID(getContext());

        // 获取屏幕宽高及密度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // 初始化屏幕属性
        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventBusObject eventBusObject) {

    }

    /**
     * 描述：为fragment填充布局
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return initView();
    }


    /**
     * 描述：fragment创建完成后
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // 注册ButterKnife
        ButterKnife.bind(this, view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Object obj) {

    }

    /**
     * 描述：友盟统计
     */
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName()); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    /**
     * 描述：打开相册
     */
    protected void selectImage(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//            Activity activity = (Activity) context;
            startActivityForResult(intent, NetErrCode.WALLET_PHOTO);
        }
    }

    //检查密码是否正确
    protected void checkPassword(final File file) {
        //创建requestBody
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        RequestBody fileBody = MultipartBody.create(MEDIA_TYPE, file);
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("suberUser", "" + SCCacheUtils.getCacheCharacterId())
                .addFormDataPart("userId", "" + SCCacheUtils.getCacheUserId())
                .setType(MultipartBody.FORM);
        RequestBody multiBody = multiBuilder.build();
        SCHttpUtils.postByBody(HttpApi.WALLET_CHECK_USE_PASSWORD + "?token=" + SCCacheUtils.getCacheToken(), multiBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(getContext(), "网络异常");
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                String code = "";
                String msg = "";
                try {
                    code = SCJsonUtils.parseCode(result);
                    msg = SCJsonUtils.parseMsg(result);
                } catch (JSONException jsonException) {
                    code = "";
                    msg = "密码错误";
                }
                final String finalMsg = msg;
                final String finalCode = code;
                if (NetErrCode.COMMON_SUC_CODE.equals(code) || NetErrCode.SUC_CODE.equals(code)) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(getContext(), "" + finalMsg);

                        }
                    });
                } else {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(getContext(), finalCode + ":" + finalMsg);
                        }
                    });
                }
            }
        });
    }
    //设置免密操作
    protected void passwordFree(final File file) {
        //设置免密操作
        final Handler freePasswordHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                final boolean bind = (boolean) msg.obj;
                switch (msg.what) {
                    case 1:
                        if (!TextUtils.isEmpty(SCCacheUtils.getCacheAuthCode())) {
                            Map stringMap = new HashMap();
                            stringMap.put("os", "android");
                            stringMap.put("token", "" + SCCacheUtils.getCacheToken());
                            stringMap.put("deviceToken", "" + JPushInterface.getRegistrationID(getContext()));
                            stringMap.put("bind", bind);
                            String modifyUser = JSONObject.toJSONString(stringMap);
                            SCHttpUtils.postWithUserId()
                                    .url(HttpApi.MODIFY_CHARACTER)
                                    .addParams("characterId", "" + SCCacheUtils.getCacheCharacterId())
                                    .addParams("dataString", modifyUser)
                                    .build()
                                    .execute(new SCHttpStringCallBack() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            LogUtils.d("修改角色信息失败");
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            String code = JSONObject.parseObject(response).getString("code");
                                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                                LogUtils.d("修改角色信息");
                                            }
                                        }
                                    });

                        }
                        break;
                }
            }
        };

        mStandardDialog = new StandardDialog(getContext());
        mStandardDialog.setStandardTitle("验证成功！");
        mStandardDialog.setStandardMsg("您也可以选择开启免密功能，在下次使用马甲券时便无需再次上传安全码，让使用更加方便快捷，是否开通免密功能？");
        mStandardDialog.setCancelText("暂不需要");
        mStandardDialog.setSureText("立即开通");
        //开通免密
        mStandardDialog.setCallback(new com.shanchain.data.common.base.Callback() {
            @Override
            public void invoke() {
                String userId = SCCacheUtils.getCacheUserId();
                String passwordCode = getAuthCode(file);
                SCCacheUtils.setCache(userId, Constants.CACHE_AUTH_CODE, passwordCode);
                SCCacheUtils.setCache(userId, Constants.TEMPORARY_CODE, passwordCode);
                Message message = new Message();
                message.what = 1;
                message.obj = true;
                ActivityStackManager.getInstance().getTopActivity();
                freePasswordHandler.sendMessage(message);
            }
        }, new com.shanchain.data.common.base.Callback() {//不开启免密
            @Override
            public void invoke() {
                Message message = new Message();
                message.what = 1;
                message.obj = false;
                freePasswordHandler.sendMessage(message);
                authCode = getAuthCode(file);
                String userId = SCCacheUtils.getCacheUserId();
                SCCacheUtils.setCache(userId, Constants.CACHE_AUTH_CODE, "");
                SCCacheUtils.setCache(userId, Constants.TEMPORARY_CODE, authCode);
//                EventBus eventBus = new EventBus();
//                EventBusObject<String> eventBusObject = new EventBusObject<String>(EventConstant.EVENT_RELEASE, authCode);
//                eventBus.post(eventBusObject);
            }
        });
        mStandardDialog.show();
    }
    //获取免密临时密码
    protected String getAuthCode(File file) {
        //创建requestBody,获取密码凭证
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        RequestBody fileBody = MultipartBody.create(MEDIA_TYPE, file);
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("deviceToken", "" + registrationId)
                .setType(MultipartBody.FORM);
        RequestBody multiBody = multiBuilder.build();
        SCHttpUtils.postByBody(HttpApi.WALLET_BIND_PHONE_IMEI + SCCacheUtils.getCacheToken(), multiBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(getContext(), "网络异常");

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                final String code = SCJsonUtils.parseCode(result);
                final String msg = SCJsonUtils.parseMsg(result);
                if (NetErrCode.COMMON_SUC_CODE.equals(code) || NetErrCode.SUC_CODE.equals(code)) {
                    String data = SCJsonUtils.parseData(result);
                    authCode = data;
                } else {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
//                            ToastUtils.showToast(mContext, code + ":" + msg);
                        }
                    });
                }
            }
        });
        return authCode;
    }

    protected void showLoadingDialog() {
        mCustomDialog = new CustomDialog(getContext(), 0.4, R.layout.common_dialog_progress, null);
        mCustomDialog.show();
        mCustomDialog.setCancelable(false);
    }

    protected void showLoadingDialog(boolean isShow) {
        mCustomDialog = new CustomDialog(getContext(), 0.4, R.layout.common_dialog_progress, null);
        mCustomDialog.show();
        mCustomDialog.setCanceledOnTouchOutside(isShow);
    }

    protected void closeLoadingDialog() {
        if (mCustomDialog != null) {
            mCustomDialog.dismiss();
        }
    }


    /*========================================
     * 子类需重写的抽象方法
     =========================================*/

    /**
     * 初始化布局
     *
     * @return 布局视图
     */
    public abstract View initView();

    /**
     * 2017/5/22
     * 描述：给视图填充数据
     */
    public abstract void initData();


    /**
     * 日期: 2017/1/8 10:13
     * 描述: 重写onDestroy 取消ButterKnife
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 取消ButterKnife
        ButterKnife.unbind(getActivity());
        EventBus.getDefault().unregister(this);
    }

//    public
    /**
     * 描述：屏幕宽度
     */
    protected int mScreenWidth = 0;

    /**
     * 描述：屏幕高度
     */
    protected int mScreenHeight = 0;
    /**
     * 描述：屏幕密度
     */
    protected float mScreenDensity = 0.0f;
}
