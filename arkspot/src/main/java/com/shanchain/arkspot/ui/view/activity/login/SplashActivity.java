package com.shanchain.arkspot.ui.view.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.manager.ActivityManager;
import com.shanchain.arkspot.ui.model.CharacterInfo;
import com.shanchain.arkspot.ui.model.RegisterHxBean;
import com.shanchain.arkspot.ui.view.activity.MainActivity;
import com.shanchain.arkspot.ui.view.activity.story.StoryTitleActivity;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                            String data = JSONObject.parseObject(response).getString("data");
                            if (TextUtils.isEmpty(data)){
                                Intent intent = new Intent(SplashActivity.this,StoryTitleActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            String character = JSONObject.parseObject(data).getString("characterInfo");
                            if (TextUtils.isEmpty(character)){
                                Intent intent = new Intent(SplashActivity.this,StoryTitleActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                CharacterInfo characterInfo = JSONObject.parseObject(character, CharacterInfo.class);
                                if (characterInfo == null){
                                    Intent intent = new Intent(SplashActivity.this,StoryTitleActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    String hxAccount = JSONObject.parseObject(data).getString("hxAccount");
                                    int spaceId = characterInfo.getSpaceId();
                                    int characterId = characterInfo.getCharacterId();
                                    obtainSpaceInfo(characterId+"",character,spaceId+"",hxAccount);
                                }

                            }

                        }
                    }
                });

    }

    private void obtainSpaceInfo(final String characterId, final String characterInfoJson, final String spaceId, final String hxAccount) {
        //获取space详情并缓存

        SCHttpUtils.post()
                .url(HttpApi.SPACE_GET_ID)
                .addParams("spaceId",spaceId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取时空详情失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("space详情 = " + response);
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                            final String data = JSONObject.parseObject(response).getString("data");
                            //SpaceInfo spaceInfo = JSONObject.parseObject(data, SpaceInfo.class);
                            RegisterHxBean hxBean = JSONObject.parseObject(hxAccount, RegisterHxBean.class);
                            final String userName = hxBean.getHxUserName();
                            final String pwd = hxBean.getHxPassword();
                            EMClient.getInstance().login(userName, pwd, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            LogUtils.i("登录环信账号成功");
                                            EMClient.getInstance().chatManager().loadAllConversations();
                                            EMClient.getInstance().groupManager().loadAllGroups();
                                            RoleManager.switchRoleCache(characterId,characterInfoJson,spaceId,data,userName,pwd);
                                            ToastUtils.showToast(SplashActivity.this,"穿越角色成功");
                                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                            ActivityManager.getInstance().finishAllActivity();
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                }

                                @Override
                                public void onError(int i, String s) {
                                    LogUtils.i("登录环信账号失败 = " + s);
                                }

                                @Override
                                public void onProgress(int i, String s) {

                                }
                            });

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
