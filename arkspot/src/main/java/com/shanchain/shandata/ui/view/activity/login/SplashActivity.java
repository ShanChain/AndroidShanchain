package com.shanchain.shandata.ui.view.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.manager.ActivityManager;
import com.shanchain.shandata.manager.ConversationManager;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.RegisterHxBean;
import com.shanchain.shandata.ui.view.activity.HomeActivity;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.ui.view.activity.story.StoryTitleActivity;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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
//                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

                //loginHx(hxUserName, hxPwd);
                loginJm("weal","123456");
//                loginJm(hxUserName,hxPwd);

            }
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

  /*  private void loginHx(String hxUserName, String hxPwd) {
        final long startTime = System.currentTimeMillis();
        LogUtils.i("登录环信 = 开始时间 = " + startTime);
        EMClient.getInstance().login(hxUserName, hxPwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                LogUtils.i("登录环信账号成功");
                long endTime = System.currentTimeMillis();
                LogUtils.i("登录环信成功 = 结束时间 = " + endTime );

                LogUtils.i("耗时 = " + (endTime - startTime));
                EMClient.getInstance().chatManager().loadAllConversations();
                EMClient.getInstance().groupManager().loadAllGroups();
                //初始化对话信息
                ConversationManager.getInstance().obtainConversationInfoFromServer();
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.i("环信登录失败 = " + s);
                exception();
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }*/

    private void loginJm(String hxUserName, String hxPwd) {
        final long startTime = System.currentTimeMillis();
        LogUtils.i("登录极光IM = 开始时间 = " + startTime);
        JMessageClient.login(hxUserName, hxPwd, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (s.contains("Success")){
                    LogUtils.d("极光IM############## 登录成功 ##############极光IM");

                }else {
                    LogUtils.d("极光IM############## 登录失败 ##############极光IM");
                }
            }
        });

//        EMClient.getInstance().login(hxUserName, hxPwd, new EMCallBack() {
//            @Override
//            public void onSuccess() {
//                LogUtils.i("登录极光IM账号成功");
//                long endTime = System.currentTimeMillis();
//                LogUtils.i("登录极光IM成功 = 结束时间 = " + endTime );
//
//                LogUtils.i("耗时 = " + (endTime - startTime));
//                EMClient.getInstance().chatManager().loadAllConversations();
//                EMClient.getInstance().groupManager().loadAllGroups();
//                //初始化对话信息
//                ConversationManager.getInstance().obtainConversationInfoFromServer();
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                LogUtils.i("极光IM登录失败 = " + s);
//                exception();
//            }
//
//            @Override
//            public void onProgress(int i, String s) {
//
//            }
//        });
    }

    private void checkServer() {

        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHARACTER_GET_CURRENT)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取当前角色失败");
                        e.printStackTrace();
                        exception();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取当前角色成功 " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                                String data = JSONObject.parseObject(response).getString("data");
                                if (TextUtils.isEmpty(data)){
                                    Intent intent = new Intent(SplashActivity.this,StoryTitleActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return;
                                }
                                String character = JSONObject.parseObject(data).getString("characterInfo");
                                if (TextUtils.isEmpty(character)){
                                    Intent intent = new Intent(SplashActivity.this,StoryTitleActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return;
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
                            }else {
                                exception();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            exception();
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
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取时空详情失败");
                        e.printStackTrace();
                        exception();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("space详情 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                                final String data = JSONObject.parseObject(response).getString("data");
                                //SpaceInfo spaceInfo = JSONObject.parseObject(data, SpaceInfo.class);
                                RoleManager.switchRoleCacheComment(characterId,characterInfoJson,spaceId,data);
//                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                ActivityManager.getInstance().finishAllActivity();
                                startActivity(intent);
                                RegisterHxBean hxBean = JSONObject.parseObject(hxAccount, RegisterHxBean.class);
                                final String userName = hxBean.getHxUserName();
                                final String pwd = hxBean.getHxPassword();
                                ThreadUtils.runOnSubThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //hxLogin(userName, pwd);
                                    }
                                });

                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            exception();
                        }
                    }
                });
    }

    private void hxLogin(final String userName, final String pwd) {
        final long startTime = System.currentTimeMillis();
        LogUtils.i("登录环信 = 开始时间 = " + startTime);
        EMClient.getInstance().login(userName, pwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.i("登录环信账号成功");
                        long endTime = System.currentTimeMillis();
                        LogUtils.i("登录环信成功 = 结束时间 = " + endTime );

                        LogUtils.i("耗时 = " + (endTime - startTime));
                        EMClient.getInstance().chatManager().loadAllConversations();
                        EMClient.getInstance().groupManager().loadAllGroups();
                        //RoleManager.switchRoleCache(characterId,characterInfoJson,spaceId,data,userName,pwd);
                        RoleManager.switchRoleCacheHx(userName,pwd);
                    }
                });

            }

            @Override
            public void onError(int i, String s) {
                LogUtils.i("登录环信账号失败 = " + s);
                exception();
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


    private void error() {
        Intent intent = new Intent(this, StoryTitleActivity.class);
        startActivity(intent);
        finish();
    }

    private void exception(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
