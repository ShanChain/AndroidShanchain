package com.shanchain.shandata.manager;

import android.app.ProgressDialog;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.eventbus.SCBaseEvent;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.base.UserType;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.shaohui.shareutil.LoginUtil;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;
import me.shaohui.shareutil.login.result.BaseToken;
import me.shaohui.shareutil.login.result.BaseUser;
import okhttp3.Call;

import static com.shanchain.data.common.base.Constants.CACHE_CUR_USER;
import static com.shanchain.data.common.base.Constants.CACHE_TOKEN;

/**
 * Created by flyye on 2017/11/25.
 */

public class LoginManager {
    private static LoginManager instance;
    private ProgressDialog mDialog;

    public synchronized static LoginManager getInstance() {
        if (null == instance) {
            instance = new LoginManager();
        }
        EventBus.getDefault().register(instance);
        return instance;
    }

    final LoginListener listener = new LoginListener() {
        @Override
        public void loginSuccess(LoginResult result) {
            //登录成功， 如果你选择了获取用户信息，可以通过
            int platform = result.getPlatform();

            BaseToken token = result.getToken();
            String accessToken = token.getAccessToken();
            BaseUser userInfo = result.getUserInfo();
            String nickname = userInfo.getNickname();
            String headImageUrl = userInfo.getHeadImageUrl();
            String openId = userInfo.getOpenId();
            String headImageUrlLarge = userInfo.getHeadImageUrlLarge();
            String userType = UserType.USER_TYPE_WEIXIN;
            int sex = userInfo.getSex();
            switch (platform) {
                case LoginPlatform.QQ:
                    userType = UserType.USER_TYPE_QQ;
                    LogUtils.d("QQ登录成功 ！！！"
                            + "\r\n token = " + token
                            + ";\r\n >< " + "昵称=" + nickname
                            + ";\r\n 性别 = " + (sex == 1 ? "男" : "女")
                            + ";\r\n headImageUrl = " + headImageUrl
                            + ";\r\n openId = " + openId
                            + ";\r\n headImageUrlLarge = " + headImageUrlLarge
                            + ";\r\n accessToken = " + accessToken
                    );

                    break;
                case LoginPlatform.WX:
                    userType = UserType.USER_TYPE_WEIXIN;
                    LogUtils.d("微信登录成功 ！！！"
                            + "\r\n token = " + token
                            + ";\r\n  " + "昵称=" + nickname
                            + ";\r\n 性别 = " + (sex == 1 ? "男" : "女")
                            + ";\r\n headImageUrl = " + headImageUrl
                            + ";\r\n openId = " + openId
                            + ";\r\n headImageUrlLarge = " + headImageUrlLarge
                            + ";\r\n accessToken = " + accessToken);

                    break;

                case LoginPlatform.WEIBO:
                    userType = UserType.USER_TYPE_WEIBO;
                    LogUtils.d("微博登录成功 ！！！"
                            + "\r\n token = " + token
                            + ";\r\n  " + "昵称=" + nickname
                            + ";\r\n 性别 = " + (sex == 1 ? "男" : "女")
                            + ";\r\n headImageUrl = " + headImageUrl
                            + ";\r\n openId = " + openId
                            + ";\r\n headImageUrlLarge = " + headImageUrlLarge
                            + ";\r\n accessToken = " + accessToken);
                    break;
                default:
                    break;
            }

            String userId = SCCacheUtils.getCache("0", "curUser");
            String userToken = SCCacheUtils.getCache(userId, CACHE_TOKEN);
            if(TextUtils.isEmpty(userToken)){
                return;
            }
            bindOtherAccount(openId, userToken, userType,userId);
        }

        @Override
        public void loginFailure(Exception e) {
            LogUtils.i("还回调了登录失败");
            e.printStackTrace();
        }

        @Override
        public void loginCancel() {
            LogUtils.d("登录取消");
        }
    };

    private void bindOtherAccount(String otherAccount,String token,String userType,String userId){
        SCHttpUtils.post()
                .url(HttpApi.BIND_OTHER_ACCOUNT)
                .addParams("otherAccount", otherAccount)
                .addParams("userType", userType)
                .addParams("userId", userId)
                .addParams("token",token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("绑定失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("绑定成功 = " + response);
                    }
                });
    }






    public void bindWithWeibo(){
        LoginUtil.login(AppManager.getInstance().getContext(), LoginPlatform.WEIBO,listener , true);
    }
    public void bindWithWX(){
        LoginUtil.login(AppManager.getInstance().getContext(), LoginPlatform.WX,listener , true);
    }
    public void bindWithQQ(){
        LoginUtil.login(AppManager.getInstance().getContext(), LoginPlatform.QQ,listener , true);

    }


    @Subscribe
    public void onEventMainThread(SCBaseEvent event) {
        if(event.receiver.equalsIgnoreCase(EventConstant.EVENT_MODULE_ARKSPOT) && event.key.equalsIgnoreCase(EventConstant.EVENT_KEY_BIND_OTHER_ACCOUNT)){
            JSONObject jsonObject = (JSONObject)event.params;
            if(TextUtils.isEmpty(jsonObject.getString("userType"))){
                return;
            }
           switch (jsonObject.getString("userType")){
               case UserType.USER_TYPE_WEIBO:
                   bindWithWeibo();
                   break;
               case UserType.USER_TYPE_QQ:
                   bindWithQQ();
                   break;
               case UserType.USER_TYPE_WEIXIN:
                   bindWithWX();
                   break;
               default:
                   return;
           }
        }
    }


}
