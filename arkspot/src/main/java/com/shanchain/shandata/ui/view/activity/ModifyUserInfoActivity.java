package com.shanchain.shandata.ui.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.JmAccount;
import com.shanchain.shandata.ui.model.ModifyUserInfo;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;

public class ModifyUserInfoActivity extends BaseActivity {

    @Bind(R.id.et_input_nike_name)
    EditText etInputNikeName;
    @Bind(R.id.et_input_sign)
    EditText etInputSign;
    private ArthurToolBar mTbMain;
    private String nikeName, inputSign;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_modify_user_info;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent() {

    }

    @Override
    protected void initViewsAndEvents() {
        if (getIntent() != null) {
            nikeName = getIntent().getStringExtra("nikeName");
            inputSign = getIntent().getStringExtra("userSign");
            etInputNikeName.setHint("" + nikeName);
            etInputSign.setHint("" + inputSign);
        }
        mTbMain = findViewById(R.id.tb_main);
        mTbMain.isShowChatRoom(false);//不在导航栏显示聊天室信息
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTbMain.getTitleView().setLayoutParams(layoutParams);
        mTbMain.setTitleText("修改个人资料");
        mTbMain.setTitleTextColor(getResources().getColor(R.color.colorTextDefault));
        mTbMain.setRightText("确定");
        mTbMain.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        mTbMain.setBackgroundColor(getResources().getColor(R.color.white));

        mTbMain.setOnRightClickListener(new ArthurToolBar.OnRightClickListener() {
            @Override
            public void onRightClick(View v) {
                String charater = SCCacheUtils.getCacheCharacterInfo();
                CharacterInfo characterInfo = JSONObject.parseObject(charater, CharacterInfo.class);
                String headImg = characterInfo.getHeadImg();
                String cacheHeadImg = SCCacheUtils.getCacheHeadImg();
                ModifyUserInfo modifyUserInfo = new ModifyUserInfo();
                modifyUserInfo.setName(etInputNikeName.getText().toString());
                modifyUserInfo.setSignature(etInputSign.getText().toString());
                modifyUserInfo.setRestartActivity(true);
//                modifyUserInfo.setHeadImg(headImg);
                String modifyUser = JSONObject.toJSONString(modifyUserInfo);
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
                                    String data = JSONObject.parseObject(response).getString("data");
                                    String character = JSONObject.parseObject(data).getString("characterInfo");
                                    String hxAccount = JSONObject.parseObject(data).getString("hxAccount");
                                    CharacterInfo characterInfo = JSONObject.parseObject(character, CharacterInfo.class);
                                    JmAccount jmAccount = JSONObject.parseObject(character, JmAccount.class);
                                    String headImg = characterInfo.getHeadImg();
                                    String name = characterInfo.getName();
                                    String signature = characterInfo.getSignature();

                                    UserInfo jmUserInfo = JMessageClient.getMyInfo();
                                    if (jmUserInfo != null) {
                                        if (TextUtils.isEmpty(name)) {
                                            jmUserInfo.setNickname(name);//设置昵称
                                            JMessageClient.updateMyInfo(UserInfo.Field.nickname, jmUserInfo, new BasicCallback() {
                                                @Override
                                                public void gotResult(int i, String s) {
                                                    String s1 = s;
                                                }
                                            });
                                        }
                                        if (TextUtils.isEmpty(signature)) {
                                            jmUserInfo.setSignature(signature);//设置签名
                                            JMessageClient.updateMyInfo(UserInfo.Field.signature, jmUserInfo, new BasicCallback() {
                                                @Override
                                                public void gotResult(int i, String s) {
                                                    String s1 = s;
                                                }
                                            });
                                        }
                                    }
                                    RoleManager.switchRoleCacheCharacterInfo(character);
                                    RoleManager.switchRoleCacheHeadImg(headImg);
                                }
                            }
                        });
//                Intent intent = new Intent(ModifyUserInfoActivity.this,MessageListActivity.class);
//                intent.putExtra("name",modifyUserInfo.getName());
//                intent.putExtra("signature",modifyUserInfo.getSignature());
//                intent.putExtra("HeadImg",modifyUserInfo.getHeadImg());
//                startActivity(intent);
                org.greenrobot.eventbus.EventBus.getDefault().post(modifyUserInfo);
                finish();

            }
        });

        mTbMain.setOnLeftClickListener(new ArthurToolBar.OnLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
//        EventBus.getDefault().unregister(this);
    }
}
