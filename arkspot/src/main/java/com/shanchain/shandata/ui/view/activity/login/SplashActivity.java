package com.shanchain.shandata.ui.view.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintNewActivity;
import com.shanchain.shandata.ui.view.activity.story.StoryTitleActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.Date;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;

import static com.shanchain.data.common.cache.SCCacheUtils.getCache;
import static com.shanchain.data.common.cache.SCCacheUtils.getCacheHxPwd;
import static com.shanchain.data.common.cache.SCCacheUtils.getCacheHxUserName;


public class SplashActivity extends AppCompatActivity {
    LinearLayout mLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String lan = MyApplication.systemLanguge;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if("zh".equals(lan)){
//            this.setTheme(R.style.AppStartTheme);
            this.getWindow().getDecorView().setBackgroundResource(R.mipmap.splash_new);
        }else {
//            this.setTheme(R.style.AppStartThemeEn);
            this.getWindow().getDecorView().setBackgroundResource(R.mipmap.splash_new_en);
        }
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
        mLinearLayout = findViewById(R.id.ll_rootview);
        String lan = MyApplication.systemLanguge;
        if("zh".equals(lan)){
            mLinearLayout.setBackgroundResource(R.mipmap.splash_new);
        }else {
            mLinearLayout.setBackgroundResource(R.mipmap.splash_new_en);
        }

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
            CharacterInfo cacheCharacter = JSONObject.parseObject(characterInfo, CharacterInfo.class);
            if (TextUtils.isEmpty(characterId) || TextUtils.isEmpty(characterInfo) || TextUtils.isEmpty(spaceInfo) || TextUtils.isEmpty(hxUserName) || TextUtils.isEmpty(hxPwd)) {
                checkServer();
            } else {
                loginJm(hxUserName, hxPwd, cacheCharacter);
                Intent intent = new Intent(SplashActivity.this, FootPrintNewActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void loginJm(final String hxUserName, String hxPwd, final CharacterInfo characterInfo) {
        LogUtils.d("-----characterInfo: " + characterInfo.getName());
        final long startTime = System.currentTimeMillis();
        Date date = new Date(startTime);
        LogUtils.i("登录极光IM = 开始时间 = " + date.toString());
        JMessageClient.login(hxUserName, hxPwd, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                String ss = s;
                if (s.equals("Success")) {
                    LogUtils.d("极光IM##############SplashActivity 登录成功 ##############极光IM");
                    UserInfo userInfo = JMessageClient.getMyInfo();
                    if (userInfo != null) {
                        LogUtils.d("极光账号: " + hxUserName);
                        LogUtils.d("极光DisplayName: " + userInfo.getDisplayName());
                        LogUtils.d("极光Nickname: " + userInfo.getNickname());
                        LogUtils.d("极光UserID: " + userInfo.getUserID());
                        LogUtils.d("极光Signature: " + userInfo.getSignature());
                    }
                    Intent intent = new Intent(SplashActivity.this, FootPrintNewActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    LogUtils.d("极光IM############## 登录失败 ##############极光IM");
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

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
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                if (TextUtils.isEmpty(data)) {
                                    LogUtils.d("获取数据", "数据信息为空");
                                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return;
                                }
                                String character = JSONObject.parseObject(data).getString("characterInfo");
                                if (TextUtils.isEmpty(character)) {
                                    LogUtils.d("获取角色信息", "角色信息为空");
                                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return;
                                } else {
                                    CharacterInfo characterInfo = JSONObject.parseObject(character, CharacterInfo.class);
//                                    RoleManager.switchRoleCacheHeadImg(characterInfo.getHeadImg());
                                    if (characterInfo == null) {
                                        LogUtils.d("获取角色ID", "角色ID为空");
                                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        String hxAccount = JSONObject.parseObject(data).getString("hxAccount");
                                        if (hxAccount == null) {
                                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            String jmUser = JSONObject.parseObject(hxAccount).getString("hxUserName");
                                            String jmPassword = JSONObject.parseObject(hxAccount).getString("hxPassword");
                                            loginJm(jmUser, jmPassword, characterInfo);
                                        }
                                    }

                                }
                            } else {
                                exception();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            exception();
                        }
                    }
                });

    }

//    private void obtainSpaceInfo(final String characterId, final String characterInfoJson, final String spaceId, final String hxAccount) {
//        //获取space详情并缓存
//
//        SCHttpUtils.post()
//                .url(HttpApi.SPACE_GET_ID)
//                .addParams("spaceId",spaceId)
//                .build()
//                .execute(new SCHttpStringCallBack() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        LogUtils.i("获取时空详情失败");
//                        e.printStackTrace();
//                        exception();
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        try {
//                            LogUtils.i("space详情 = " + response);
//                            String code = JSONObject.parseObject(response).getString("code");
//                            if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
//                                final String data = JSONObject.parseObject(response).getString("data");
//                                //SpaceInfo spaceInfo = JSONObject.parseObject(data, SpaceInfo.class);
//                                RoleManager.switchRoleCacheComment(characterId,characterInfoJson,spaceId,data);
////                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
//                                startActivity(intent);
////                                ActivityManager.getInstance().finishAllActivity();
//                                finish();
//                                RegisterHxBean hxBean = JSONObject.parseObject(hxAccount, RegisterHxBean.class);
//                                final String userName = hxBean.getHxUserName();
//                                final String pwd = hxBean.getHxPassword();
//                                ThreadUtils.runOnSubThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        //hxLogin(userName, pwd);
////                                        loginJm(userName,pwd);
//                                    }
//                                });
//
//                            }
//                        } catch (IllegalArgumentException e) {
//                            e.printStackTrace();
//                            exception();
//                        }
//                    }
//                });
//    }


    private void error() {
        Intent intent = new Intent(this, StoryTitleActivity.class);
        startActivity(intent);
        finish();
    }

    private void exception() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
