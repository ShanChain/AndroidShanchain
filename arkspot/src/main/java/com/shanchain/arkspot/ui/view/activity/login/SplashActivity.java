package com.shanchain.arkspot.ui.view.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.manager.ActivityManager;
import com.shanchain.arkspot.ui.model.CharacterInfo;
import com.shanchain.arkspot.ui.model.RegisterHxBean;
import com.shanchain.arkspot.ui.model.RegisterHxInfo;
import com.shanchain.arkspot.ui.model.ResponseCurrentUser;
import com.shanchain.arkspot.ui.model.ResponseSpaceInfo;
import com.shanchain.arkspot.ui.model.SpaceInfo;
import com.shanchain.arkspot.ui.view.activity.MainActivity;
import com.shanchain.arkspot.ui.view.activity.story.StoryTitleActivity;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.shanchain.data.common.cache.SCCacheUtils.getCache;
import static com.shanchain.data.common.cache.SCCacheUtils.getCacheHxPwd;
import static com.shanchain.data.common.cache.SCCacheUtils.getCacheHxUserName;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initAPP();
    }

    private void initAPP() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkCache();
            }
        }, 2000);

    }

    private void checkCache() {
        String userId = getCache("0", Constants.CACHE_CUR_USER);
        if (!TextUtils.isEmpty(userId)) {
            LogUtils.e("当前用户id" + userId);
            String characterId = getCache(userId, Constants.CACHE_CHARACTER_ID);
            String characterInfo = getCache(userId, Constants.CACHE_CHARACTER_INFO);
            String spaceId = getCache(userId, Constants.CACHE_SPACE_ID);
            String spaceInfo = getCache(userId, Constants.CACHE_SPACE_INFO);
            String hxUserName = getCacheHxUserName();
            String hxPwd = getCacheHxPwd();
            if (TextUtils.isEmpty(characterId) || TextUtils.isEmpty(characterInfo) || TextUtils.isEmpty(spaceId) || TextUtils.isEmpty(spaceInfo)||TextUtils.isEmpty(hxUserName)||TextUtils.isEmpty(hxPwd)) {
                checkServer();
            } else {
                EMClient.getInstance().login(hxUserName, hxPwd, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        EMClient.getInstance().chatManager().loadAllConversations();
                        EMClient.getInstance().groupManager().loadAllGroups();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {
                        LogUtils.i("环信登录失败 = " + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });

            }
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkServer() {

        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHARACTER_GET_CURRENT)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取当前角色失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取当前角色成功 " + response);
                        if (TextUtils.isEmpty(response)) {
                            return;
                        }

                        ResponseCurrentUser currentUser = JSONObject.parseObject(response, ResponseCurrentUser.class);
                        if (currentUser == null) {
                            return;
                        }

                        String code = currentUser.getCode();

                        if (!TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            return;
                        }
                        CharacterInfo characterInfo = currentUser.getData();
                        if (characterInfo == null) {
                            error();
                        } else {
                            int characterId = characterInfo.getCharacterId();
                            int spaceId = characterInfo.getSpaceId();
                            String characterInfoJson = JSON.toJSONString(characterInfo);
                            String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
                            obtainSpaceInfo(characterId + "", characterInfoJson, userId, spaceId + "");

                        }

                    }
                });

    }

    private void obtainSpaceInfo(final String characterId, final String characterInfoJson, final String userId, final String spaceId) {
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
                        String spaceJson = new Gson().toJson(spaceDetailInfo);
                        obtainHxInfo(characterId, characterInfoJson, spaceId, spaceJson);
                    }
                });

    }

    private void obtainHxInfo(final String characterId, final String characterInfoJson, final String spaceId, final String spaceJson) {
        SCHttpUtils.post()
                .url(HttpApi.HX_USER_REGIST)
                .addParams("characterId", characterId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取当前用户环信账号失败");
                        e.printStackTrace();
                        error();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取当前用户环信账号成功 = " + response);
                        try {
                            RegisterHxInfo registerHxInfo = JSONObject.parseObject(response, RegisterHxInfo.class);
                            String code = registerHxInfo.getCode();
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                RegisterHxBean registerHxBean = registerHxInfo.getData();
                                final String userName = registerHxBean.getHxUserName();
                                final String pwd = registerHxBean.getHxPassword();

                                EMClient.getInstance().login(userName, pwd, new EMCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        LogUtils.i("登录环信账号成功");
                                        EMClient.getInstance().chatManager().loadAllConversations();
                                        EMClient.getInstance().groupManager().loadAllGroups();
                                        RoleManager.switchRoleCache(characterId, characterInfoJson, spaceId, spaceJson, userName, pwd);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtils.showToast(SplashActivity.this, "穿越角色成功");
                                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                                ActivityManager.getInstance().finishAllActivity();
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        LogUtils.i("登录环信账号失败");
                                        error();
                                    }

                                    @Override
                                    public void onProgress(int i, String s) {

                                    }
                                });
                            }
                        } catch (Exception e) {
                            LogUtils.i("获取环信账号失败");
                            e.printStackTrace();
                            error();
                        }

                    }
                });

    }

    private void error() {
        Intent intent = new Intent(this, StoryTitleActivity.class);
        startActivity(intent);
        finish();
    }

}
