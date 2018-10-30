package com.shanchain.shandata.ui.view.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.base.UserType;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.AccountUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.PrefUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.encryption.AESUtils;
import com.shanchain.data.common.utils.encryption.Base64;
import com.shanchain.data.common.utils.encryption.MD5Utils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.manager.ActivityManager;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.LoginUserInfoBean;
import com.shanchain.shandata.ui.model.RegisterHxBean;
import com.shanchain.shandata.ui.model.ResponseLoginBean;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.ui.view.activity.story.StoryTitleActivity;
import com.shanchain.shandata.utils.KeyboardUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.tencent.tauth.Tencent;

import butterknife.Bind;
import butterknife.OnClick;
import me.shaohui.shareutil.LoginUtil;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;
import me.shaohui.shareutil.login.result.BaseToken;
import me.shaohui.shareutil.login.result.BaseUser;
import okhttp3.Call;

import static com.shanchain.data.common.cache.SCCacheUtils.getCache;


public class LoginActivity extends BaseActivity {

    @Bind(R.id.tb_login)
    ArthurToolBar mTbLogin;
    @Bind(R.id.et_login_account)
    EditText mEtLoginAccount;
    @Bind(R.id.et_login_pwd)
    EditText mEtLoginPwd;
    @Bind(R.id.tv_login_forget)
    TextView mTvLoginForget;
    @Bind(R.id.btn_login)
    Button mBtnLogin;
    @Bind(R.id.btn_register)
    Button mBtnRegister;
    @Bind(R.id.iv_login_wx)
    ImageView mIvLoginWx;
    @Bind(R.id.iv_login_wb)
    ImageView mIvLoginWb;
    @Bind(R.id.iv_login_qq)
    ImageView mIvLoginQq;
    private ProgressDialog mDialog;
    private Tencent mTencent;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewsAndEvents() {
        mTbLogin.setBtnEnabled(false);
    }

    private void checkCache() {
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);


        if (!TextUtils.isEmpty(userId)) {
            LogUtils.e("当前用户id" + userId);
            String characterId = getCache(userId, Constants.CACHE_CHARACTER_ID);
            String characterInfo = getCache(userId, Constants.CACHE_CHARACTER_INFO);
            String spaceId = getCache(userId, Constants.CACHE_SPACE_ID);
            String spaceInfo = getCache(userId, Constants.CACHE_SPACE_INFO);
            String hxUserName = SCCacheUtils.getCacheHxUserName();
            String hxPwd = SCCacheUtils.getCacheHxPwd();
            String token = SCCacheUtils.getCacheToken();
            if (TextUtils.isEmpty(characterId) || TextUtils.isEmpty(characterInfo) || TextUtils.isEmpty(spaceId) || TextUtils.isEmpty(spaceInfo) || TextUtils.isEmpty(hxUserName) || TextUtils.isEmpty(hxPwd) || TextUtils.isEmpty(token)) {
                checkServer();
            } else {
                closeProgress();
                readyGo(MainActivity.class);
                finish();
            }

        }
    }

    private void checkServer() {
        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHARACTER_GET_CURRENT)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeProgress();
                        ToastUtils.showToast(mContext, "网络异常");
                        LogUtils.i("获取当前角色失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取当前角色成功 " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                if (TextUtils.isEmpty(data)) {
                                    closeProgress();
                                    readyGo(StoryTitleActivity.class);
                                    finish();
                                    return;
                                }
                                String character = JSONObject.parseObject(data).getString("characterInfo");
                                if (TextUtils.isEmpty(character)) {
                                    closeProgress();
                                    readyGo(StoryTitleActivity.class);
                                    finish();
                                    return;
                                } else {
                                    CharacterInfo characterInfo = JSONObject.parseObject(character, CharacterInfo.class);
                                    if (characterInfo == null) {
                                        closeProgress();
                                        readyGo(StoryTitleActivity.class);
                                        finish();
                                    } else {
                                        String hxAccount = JSONObject.parseObject(data).getString("hxAccount");
                                        int spaceId = characterInfo.getSpaceId();
                                        int characterId = characterInfo.getCharacterId();
                                        obtainSpaceInfo(characterId + "", character, spaceId + "", hxAccount);
                                    }
                                }
                            } else {
                                //code错误
                                closeProgress();
                                LogUtils.i("获取当前角色code错误");
                                ToastUtils.showToast(mContext, "网络异常");
                            }
                        } catch (Exception e) {
                            closeProgress();
                            ToastUtils.showToast(mContext, "网络异常");
                            LogUtils.i("获取当前角色信息数据解析错误");
                            e.printStackTrace();
                        }
                    }
                });

    }


    @OnClick({R.id.tv_login_forget, R.id.btn_login, R.id.btn_register, R.id.iv_login_wx, R.id.iv_login_wb, R.id.iv_login_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_forget:
                //忘记密码
                forgetPwd();
                break;
            case R.id.btn_login:
                //登录
                login();
                break;
            case R.id.btn_register:
                //注册
                readyGo(RegisterActivity.class);
                break;
            case R.id.iv_login_wx:
                //微信登录
                LoginUtil.login(this, LoginPlatform.WX, listener, true);
                break;
            case R.id.iv_login_wb:
                //微博登录
                LoginUtil.login(this, LoginPlatform.WEIBO, listener, true);
                break;
            case R.id.iv_login_qq:
                //qq登录
                LoginUtil.login(this, LoginPlatform.QQ, listener, true);
                break;
            default:
                break;
        }
    }

    private void login() {

        String account = mEtLoginAccount.getText().toString().trim();
        String pwd = mEtLoginPwd.getText().toString().trim();

        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {
            ToastUtils.showToast(this, "账号或密码为空！");
            return;
        }

        if (!AccountUtils.isPhone(account)) {
            ToastUtils.showToast(this, "账号格式不正确！");
            return;
        }

        String time = String.valueOf(System.currentTimeMillis());
        //加密后的账号
        String encryptAccount = Base64.encode(AESUtils.encrypt(account, Base64.encode(UserType.USER_TYPE_MOBILE + time)));
        //加密后的密码
        String md5Pwd = MD5Utils.md5(pwd);
        String passwordAccount = Base64.encode(AESUtils.encrypt(md5Pwd, Base64.encode(UserType.USER_TYPE_MOBILE + time + account)));
        KeyboardUtils.hideSoftInput(this);
        showProgress();

        SCHttpUtils.postWithParamsForLogin()
                .url(HttpApi.USER_LOGIN)
                .addParams("Timestamp", time)
                .addParams("encryptAccount", encryptAccount)
                .addParams("encryptPassword", passwordAccount)
                .addParams("userType", UserType.USER_TYPE_MOBILE)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeProgress();
                        LogUtils.i("登录错误");
                        e.printStackTrace();
                        ToastUtils.showToast(mContext, "网络错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.d("登录返回数据 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                //登录成功
                                String data = JSONObject.parseObject(response).getString("data");
                                ResponseLoginBean loginBean = JSONObject.parseObject(data, ResponseLoginBean.class);
                                //登录成功 在此缓存用户数据
                                String token = loginBean.getToken();
                                LoginUserInfoBean userInfo = loginBean.getUserInfo();
                                int userId = userInfo.getUserId();
                                LogUtils.d("登录成功  uid" + userId);

                                SCCacheUtils.setCache("0", Constants.CACHE_CUR_USER, userId + "");
                                SCCacheUtils.setCache(userId + "", Constants.CACHE_USER_INFO, new Gson().toJson(userInfo));
                                SCCacheUtils.setCache(userId + "", Constants.CACHE_TOKEN, userId + "_" + token);
                                checkCache();
                            } else if (TextUtils.equals(code, NetErrCode.LOGIN_ERR_CODE)) {
                                closeProgress();
                                ToastUtils.showToast(mContext, "账号或密码错误");
                            } else {
                                closeProgress();
                                ToastUtils.showToast(mContext, "网络错误");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            closeProgress();
                            ToastUtils.showToast(mContext, "网络错误");
                        }
                    }
                });

    }

    private void forgetPwd() {
        readyGo(ResetPwdActivity.class);
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

            String time = String.valueOf(System.currentTimeMillis());
            //加密后的openid
            String encryptOpenId = Base64.encode(AESUtils.encrypt(openId, Base64.encode(userType + time)));
            //加密后的accesstoken
            LogUtils.d("accessToken : " + accessToken);
            String accesToken = accessToken.substring(0, 16);
            LogUtils.d("accesToken截取 : " + accesToken);
            String encryptToken16 = Base64.encode(AESUtils.encrypt(MD5Utils.md5(accesToken), Base64.encode(userType + time + openId)));

            LogUtils.d("加密后openid：" + encryptOpenId);
            LogUtils.d("加密后accesstoken：" + encryptToken16);
            LogUtils.d("用户类型：" + userType);
            LogUtils.i("回调了登录成功");
            thridLogin(time, encryptOpenId, encryptToken16, headImageUrlLarge, nickname, sex == 1 ? "0" : "1", userType);

        }

        @Override
        public void loginFailure(Exception e) {
            closeProgress();
            ToastUtils.showToast(mContext, "网络异常");
            LogUtils.i("还回调了登录失败");
            e.printStackTrace();
        }

        @Override
        public void loginCancel() {
            LogUtils.d("登录取消");
        }
    };

    /**
     * 三方登录注册信息到服务器
     */
    public void thridLogin(String time, String encryptOpenId, String encryptToken16, String headIcon, String nickName, String sex, String userType) {
        showProgress();
        try {
            SCHttpUtils.postWithParamsForLogin()
                    .url(HttpApi.USER_THIRD_LOGIN)
                    .addParams("Timestamp", time)
                    .addParams("encryptOpenId", encryptOpenId)
                    .addParams("encryptToken16", encryptToken16)
                    .addParams("headIcon", headIcon)
                    .addParams("nickName", nickName)
                    .addParams("sex", sex)
                    .addParams("userType", userType)
                    .build()
                    .execute(new SCHttpStringCallBack() {
                                 @Override
                                 public void onError(Call call, Exception e, int id) {
                                     closeProgress();
                                     LogUtils.e("三方登录创建账号失败");
                                     ToastUtils.showToast(mContext, "网络异常");
                                     e.printStackTrace();
                                 }

                                 @Override
                                 public void onResponse(String response, int id) {
                                     try {
                                         LogUtils.i("第三方注册结果 = " + response);
                                         String code = SCJsonUtils.parseCode(response);
                                         if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                             String data = SCJsonUtils.parseData(response);
                                             ResponseLoginBean responseLoginBean = JSONObject.parseObject(data, ResponseLoginBean.class);
                                             String token = responseLoginBean.getToken();
                                             LoginUserInfoBean userInfo = responseLoginBean.getUserInfo();
                                             int userId = userInfo.getUserId();
                                             LogUtils.d("登录成功  uid" + userId);
                                             SCCacheUtils.setCache("0", Constants.CACHE_CUR_USER, userId + "");
                                             SCCacheUtils.setCache(userId + "", Constants.CACHE_USER_INFO, new Gson().toJson(userInfo));
                                             SCCacheUtils.setCache(userId + "", Constants.CACHE_TOKEN, userId + "_" + token);
                                             checkCache();
                                         } else {
                                             closeProgress();
                                             ToastUtils.showToast(mContext, "网络异常");
                                         }
                                     } catch (Exception e) {
                                         closeProgress();
                                         e.printStackTrace();
                                         ToastUtils.showToast(mContext, "网络异常");
                                     }
                                 }
                             }
                    );
        } catch (Exception e) {
            e.printStackTrace();
            closeProgress();
            ToastUtils.showToast(mContext, "网络异常");
            LogUtils.i("网络异常");
        }
    }


    private void obtainSpaceInfo(final String characterId, final String characterInfoJson, final String spaceId, final String hxAccount) {
        //获取space详情并缓存

        SCHttpUtils.post()
                .url(HttpApi.SPACE_GET_ID)
                .addParams("spaceId", spaceId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeProgress();
                        ToastUtils.showToast(mContext, "网络错误");
                        LogUtils.i("获取时空详情失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("space详情 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                closeProgress();
                                final String data = JSONObject.parseObject(response).getString("data");
                                RoleManager.switchRoleCacheComment(characterId, characterInfoJson, spaceId, data);
                                RegisterHxBean hxBean = JSONObject.parseObject(hxAccount, RegisterHxBean.class);
                                Intent intent = new Intent(mContext, MainActivity.class);
                                ActivityManager.getInstance().finishAllActivity();
                                boolean guided = PrefUtils.getBoolean(mContext, Constants.SP_KEY_GUIDE, false);
                                if (guided) {
                                    startActivity(intent);
                                } else {
                                    startActivity(new Intent(mContext, GuideActivity.class));
                                }

                                final String userName = hxBean.getHxUserName();
                                final String pwd = hxBean.getHxPassword();
                                ThreadUtils.runOnSubThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        hxLogin(userName, pwd);
                                    }
                                });
                            } else {
                                //code错误
                                closeProgress();
                                ToastUtils.showToast(mContext, "网络错误");
                            }
                        } catch (IllegalArgumentException e) {
                            closeProgress();
                            LogUtils.i("解析数据错误");
                            e.printStackTrace();
                            ToastUtils.showToast(mContext, "网络异常");
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
                        long endTime = System.currentTimeMillis();
                        LogUtils.i("登录环信成功 = 结束时间 = " + endTime);

                        LogUtils.i("耗时 = " + (endTime - startTime));
                        EMClient.getInstance().chatManager().loadAllConversations();
                        EMClient.getInstance().groupManager().loadAllGroups();
                        //RoleManager.switchRoleCache(characterId, characterInfoJson, spaceId, data, userName, pwd);
                        RoleManager.switchRoleCacheHx(userName, pwd);
                        //ToastUtils.showToast(mContext, "欢迎来到千千世界");

                    }
                });

            }

            @Override
            public void onError(int i, String s) {
                //closeProgress();
                LogUtils.i("登录环信账号失败 = " + s + "code" + i);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(mContext, "网络异常");
                    }
                });
                if (i == 200) {

                }
            }

            @Override
            public void onProgress(int i, String s) {
                LogUtils.i("登录进度 = " + i + " 进度信息 = " + s);
            }
        });

    }

    public void showProgress() {
        mDialog = new ProgressDialog(this);
        mDialog.setMax(100);
        mDialog.setMessage("登录中..");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public void closeProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
        }

    }


}
