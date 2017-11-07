package com.shanchain.arkspot.manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.CharacterInfo;
import com.shanchain.arkspot.ui.model.RegisterHxBean;
import com.shanchain.arkspot.ui.model.RegisterHxInfo;
import com.shanchain.arkspot.ui.model.ResponseSpaceInfo;
import com.shanchain.arkspot.ui.model.ResponseSwitchRoleInfo;
import com.shanchain.arkspot.ui.model.SpaceInfo;
import com.shanchain.arkspot.ui.view.activity.MainActivity;
import com.shanchain.arkspot.widgets.dialog.CustomDialog;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.eventbus.SCBaseEvent;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by flyye on 2017/11/7.
 */

public class CharacterManager {

    private CustomDialog mCustomDialog;
    private String mSpaceInfo = "";
    private boolean isHxLogin = false;
    private RegisterHxBean mRegisterHxBean;
    private CharacterInfo mCharacterInfo;

    private static CharacterManager instance;

    public synchronized static CharacterManager getInstance() {
        if (null == instance) {
            instance = new CharacterManager();
        }
        EventBus.getDefault().register(instance);
        return instance;
    }
    @Subscribe
    public void onEventMainThread(SCBaseEvent event) {
        if(event.receiver.equalsIgnoreCase(EventConstant.EVENT_MODULE_ARKSPOT) && event.key.equalsIgnoreCase(EventConstant.EVENT_KEY_SWITCH_ROLE)){
            JSONObject jsonObject = (JSONObject)event.params;
            if(!TextUtils.isEmpty(jsonObject.getString("modelId")) && !TextUtils.isEmpty(jsonObject.getString("spaceId"))){
                switchRole(jsonObject.getString("modelId"),jsonObject.getString("spaceId"),jsonObject.getString("spaceInfo"));
            }

        }
    }

    private void switchRole(String modelId, String spaceId, String spaceInfo){
        if(!TextUtils.isEmpty(spaceInfo)){
            mSpaceInfo = spaceInfo;
        }
        showLoadingDialog();
        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHARACTER_CHANGE)
                .addParams("spaceId", spaceId)
                .addParams("modelId",modelId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        error();
                        LogUtils.i("切换角色失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("切换角色成功" + response);

                        if (TextUtils.isEmpty(response)){
                            error();
                            return;
                        }

                        ResponseSwitchRoleInfo responseSwitchRoleInfo = JSONObject.parseObject(response, ResponseSwitchRoleInfo.class);
                        if (responseSwitchRoleInfo == null){
                            error();
                            return;
                        }

                        String code = responseSwitchRoleInfo.getCode();

                        if (!TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                            error();
                            return;
                        }

                        CharacterInfo data = responseSwitchRoleInfo.getData();

                        if (data == null) {
                            error();
                            return;
                        }
                        mCharacterInfo = data;
                        if(TextUtils.isEmpty(mSpaceInfo)){
                            obtainSpaceInfo(data.getSpaceId() + "");
                        }
                        registerHxUserAndLogin(data);

                    }
                });
    }

    private  void registerHxUserAndLogin(final CharacterInfo data) {
        SCHttpUtils.post()
                .url(HttpApi.HX_USER_REGIST)
                .addParams("characterId",data.getCharacterId()+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        error();
                        LogUtils.i("注册环信账号失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        LogUtils.i("注册环信账号成功 " + response );

                        String currentUser = EMClient.getInstance().getCurrentUser();
                        //String cacheHxUserName = SCCacheUtils.getCacheHxUserName();
                        //退出当前登录的账号
                        if (TextUtils.isEmpty(currentUser)){
                            login(response,data);
                            return;
                        }
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                login(response, data);
                            }

                            @Override
                            public void onError(int i, String s) {
                                error();
                                LogUtils.i("登出失败 = " + s);
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });

                    }
                });
    }

    private void login(String response, final CharacterInfo data) {
        try {
            RegisterHxInfo registerHxInfo = JSONObject.parseObject(response, RegisterHxInfo.class);
            String code = registerHxInfo.getCode();
            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                 mRegisterHxBean = registerHxInfo.getData();
                final String userName = mRegisterHxBean.getHxUserName();
                final String pwd = mRegisterHxBean.getHxPassword();
                EMClient.getInstance().login(userName, pwd, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        ActivityStackManager.getInstance().getTopActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isHxLogin = true;
                                closeLoadingDialog();
                                LogUtils.i("登录环信账号成功");
                                EMClient.getInstance().chatManager().loadAllConversations();
                                saveToCache();
                                ToastUtils.showToast(AppManager.getInstance().getContext(),"穿越角色成功");
                                Intent intent = new Intent(ActivityStackManager.getInstance().getTopActivity(), MainActivity.class);
                                ActivityManager.getInstance().finishAllActivity();
                                ActivityStackManager.getInstance().getTopActivity().startActivity(intent);
                            }
                        });

                    }

                    @Override
                    public void onError(int i, String s) {
                        error();
                        LogUtils.i("登录环信账号失败");
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });

            }
        } catch (Exception e) {
            error();
            LogUtils.i("注册失败");
            e.printStackTrace();
        }
    }

    private void obtainSpaceInfo(final String spaceId) {
        //获取space详情并缓存

        List<String> spaceIds = new ArrayList<>();
        spaceIds.add(spaceId);
        String jArr = JSON.toJSONString(spaceIds);
        SCHttpUtils.post()
                .url(HttpApi.SPACE_LIST_SPACEID)
                .addParams("jArray", jArr)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取时空详情失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("space详情 = " + response);
                        ResponseSpaceInfo responseSpaceInfo = new Gson().fromJson(response, ResponseSpaceInfo.class);
                        List<SpaceInfo> data = responseSpaceInfo.getData();
                        SpaceInfo spaceDetailInfo = data.get(0);
                        mSpaceInfo = JSON.toJSONString(spaceDetailInfo);
                        saveToCache();
                    }
                });

    }

    private void showLoadingDialog() {
        mCustomDialog = new CustomDialog(ActivityStackManager.getInstance().getTopActivity(), 0.4, R.layout.common_dialog_progress, null);
        mCustomDialog.show();
        mCustomDialog.setCancelable(false);
    }

    private void closeLoadingDialog() {
        if (mCustomDialog != null) {
            mCustomDialog.dismiss();
        }
    }

    private void error(){
        ActivityStackManager.getInstance().getTopActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast(AppManager.getInstance().getContext(),"穿越失败！");
                closeLoadingDialog();
            }
        });

    }

    private void saveToCache(){
        if(isHxLogin && mCharacterInfo != null && mRegisterHxBean != null && mSpaceInfo != null){
            RoleManager.switchRoleCache(mCharacterInfo.getCharacterId(),JSON.toJSONString(mCharacterInfo),mCharacterInfo.getSpaceId(),mSpaceInfo,mRegisterHxBean.getHxUserName(),mRegisterHxBean.getHxPassword());
        }
    }




}

