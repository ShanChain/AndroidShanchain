package com.shanchain.shandata.manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.base.UserType;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.eventbus.SCBaseEvent;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.shaohui.shareutil.LoginUtil;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;
import me.shaohui.shareutil.login.result.BaseToken;
import me.shaohui.shareutil.login.result.BaseUser;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;
import okhttp3.Call;

import static com.shanchain.data.common.base.Constants.CACHE_TOKEN;

/**
 * Created by flyye on 2017/11/25.
 */

public class LoginManager {
    private static LoginManager instance;
    private ProgressDialog mDialog;
    private static Context mContext;

    public synchronized static LoginManager getInstance(Context context) {
        mContext = context;
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
            if (TextUtils.isEmpty(userToken)) {
                return;
            }
            bindOtherAccount(openId, userToken, userType, userId);
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

    private void bindOtherAccount(String otherAccount, String token, String userType, String userId) {
        SCHttpUtils.post()
                .url(HttpApi.BIND_OTHER_ACCOUNT)
                .addParams("otherAccount", otherAccount)
                .addParams("userType", userType)
                .addParams("userId", userId)
                .addParams("token", token)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("绑定失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("绑定结果 = " + response);
                        String code = SCJsonUtils.parseCode(response);
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            ToastUtils.showToast(mContext, "绑定成功");
                        } else if (TextUtils.equals(code, NetErrCode.COMMON_ERR_CODE)) {
                            ToastUtils.showToast(mContext, "此账号已被使用");
                        }


                    }
                });
    }


    public void bindWithWeibo() {
        LoginUtil.login(AppManager.getInstance().getContext(), LoginPlatform.WEIBO, listener, true);
    }

    public void bindWithWX() {
        LoginUtil.login(AppManager.getInstance().getContext(), LoginPlatform.WX, listener, true);
    }

    public void bindWithQQ() {
        LoginUtil.login(AppManager.getInstance().getContext(), LoginPlatform.QQ, listener, true);

    }


    @Subscribe
    public void onEventMainThread(SCBaseEvent event) {
        if (event.receiver.equalsIgnoreCase(EventConstant.EVENT_MODULE_ARKSPOT) && event.key.equalsIgnoreCase(EventConstant.EVENT_KEY_BIND_OTHER_ACCOUNT)) {
            JSONObject jsonObject = (JSONObject) event.params;
            if (TextUtils.isEmpty(jsonObject.getString("userType"))) {
                return;
            }
            switch (jsonObject.getString("userType")) {
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
        } else if (event.receiver.equalsIgnoreCase(EventConstant.EVENT_MODULE_ARKSPOT) && event.key.equalsIgnoreCase(EventConstant.EVENT_KEY_SHARE_WEB)) {
            JSONObject obj = (JSONObject) event.params;
            int shareType = obj.getInteger("shareType");
            String id = obj.getString("id");
            String type = obj.getString("type");
            LogUtils.i("分享 shareType = " + shareType + "; id = " + id + "; type = " + type);
            obtainShareInfo(shareType, type, id);
        }
    }

    private void obtainShareInfo(final int shareType, String type, String id) {
        SCHttpUtils.postWithChaId()
                .url(HttpApi.SHARE_SAVE)
                .addParams("id", id)
                .addParams("type", type)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("获取分享信息失败");
                        e.printStackTrace();
                        ToastUtils.showToast(mContext, "分享失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到分享信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                share(shareType, data);
                            } else {
                                ToastUtils.showToast(mContext, "分享失败");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(mContext, "分享失败");
                        }
                    }
                });
    }

    private void share(int shareType, String data) {
        String background = SCJsonUtils.parseString(data, "background");
        //String contentId = SCJsonUtils.parseString(data, "contentId");
        String intro = SCJsonUtils.parseString(data, "intro");
        //String shareId = SCJsonUtils.parseString(data, "shareId");
        String title = SCJsonUtils.parseString(data, "title");
        //String type = SCJsonUtils.parseString(data, "type");
        String url = SCJsonUtils.parseString(data, "url");
        if (TextUtils.isEmpty(url)){
            url = "http://www.qianqianshijie.com";
        }
        switch (shareType) {
            case SharePlatform.WX:
                ShareUtil.shareMedia(AppManager.getInstance().getContext(), SharePlatform.WX, title, intro, url, background, new SCShareListener());
                break;
            case SharePlatform.WEIBO:
                ShareUtil.shareMedia(AppManager.getInstance().getContext(), SharePlatform.WEIBO, title, intro, url, background, new SCShareListener());
                break;
            case SharePlatform.QQ:
                ShareUtil.shareMedia(AppManager.getInstance().getContext(), SharePlatform.QQ, title, intro, url, background, new SCShareListener());
                break;
            case SharePlatform.QZONE:
                ShareUtil.shareMedia(AppManager.getInstance().getContext(), SharePlatform.QZONE, title, intro, url, background, new SCShareListener());
                break;
            case SharePlatform.WX_TIMELINE:
                ShareUtil.shareMedia(AppManager.getInstance().getContext(), SharePlatform.WX_TIMELINE, title, intro, url, background, new SCShareListener());
                break;
            default:
                break;

        }
    }

    private class SCShareListener extends ShareListener {

        @Override
        public void shareSuccess() {
            LogUtils.i("分享成功");
        }

        @Override
        public void shareFailure(Exception e) {
            LogUtils.i("分享失败");
            e.printStackTrace();
        }

        @Override
        public void shareCancel() {
            LogUtils.i("分享取消");
        }
    }

}
