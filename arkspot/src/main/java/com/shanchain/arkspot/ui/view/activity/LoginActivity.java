package com.shanchain.arkspot.ui.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.arkspot.ui.view.activity.square.AddTopicActivity;
import com.shanchain.data.common.cache.CommonCacheHelper;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.net.SCHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class LoginActivity extends BaseActivity {

    @Bind(R.id.et_input)
    EditText mEtInput;
    @Bind(R.id.btn_register)
    Button mBtnRegister;
    @Bind(R.id.btn_login)
    Button mBtnLogin;
    @Bind(R.id.btn_test)
    Button mBtnTest;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    //    String userName = "sc-738727063";
    String userName = "sc-738726166";
    //    String userName = "sc-738727191";
    private String psw = "123456";

    @OnClick({R.id.btn_register, R.id.btn_login,R.id.btn_test})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test:
                readyGo(AddTopicActivity.class);
                break;

            case R.id.btn_register:
                //添加好友
                String friend = mEtInput.getText().toString().trim();
            /*   SCHttpUtils.post()
                        .url(HttpApi.HX_USER_ADD_FRIEND)
                        .addParams("userName", userName)
                        .addParams("friendName", friend)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showToast(LoginActivity.this, "失敗");
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                ToastUtils.showToast(mContext, "成功");
                                LogUtils.d("成功" + response);
                                readyGo(MainActivity.class);
                            }
                        });
*/


                //添加群成员(可批量添加)
                List<String> lists = new ArrayList<>();
                lists.add(userName);

                String array = new Gson().toJson(lists);

                LogUtils.d("=======" + array);
                SCHttpUtils.post()
                        .url(HttpApi.HX_GROUP_ADD_MEMBERS)
                        .addParams("groupId", friend)
                        .addParams("jArray", array)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showToast(LoginActivity.this, "失败");
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                ToastUtils.showToast(mContext, "成功");
                                LogUtils.d("成功" + response);
                                readyGo(MainActivity.class);
                            }
                        });


                break;
            case R.id.btn_login:
                String trim = mEtInput.getText().toString().trim();

                EMClient.getInstance().logout(true);

                EMClient.getInstance().login(userName, psw, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToast(mContext, "登录成功" + EMClient.getInstance().getCurrentUser());
                                CommonCacheHelper.getInstance(getApplicationContext()).setCache("0","cur_user_id","8");
                                readyGo(MainActivity.class);
                            }
                        });

                    }

                    @Override
                    public void onError(int i, final String s) {
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToast(mContext, "登录失败" + EMClient.getInstance().getCurrentUser());
                                LogUtils.d("失败原因 : " + s);
                            }
                        });

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });

                //移除群管理员
                /*SCHttpUtils.post()
                        .url(HttpApi.HX_GROUP_REMOVE_ADMIN)
                        .addParams("groupId","27697479745538")
                        .addParams("oldadmin","sc-738727190")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showToast(LoginActivity.this, "失败");
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                ToastUtils.showToast(mContext, "成功");
                                LogUtils.d("成功" + response);
                                readyGo(MainActivity.class);
                            }
                        });*/

                //添加群管理
               /* SCHttpUtils.post()
                        .url(HttpApi.HX_GROUP_ADD_ADMIN)
                        .addParams("groupId","27697479745538")
                        .addParams("newAdmin","sc-738727190")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showToast(LoginActivity.this, "失败");
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                ToastUtils.showToast(mContext, "成功");
                                LogUtils.d("成功" + response);
                                readyGo(MainActivity.class);
                            }
                        });*/

                //移除群成员（可批量移除）
                /*List<String> lists = new ArrayList<>();
                lists.add("sc-738727125");

                String array = new Gson().toJson(lists);

                LogUtils.d("======="+array);
                SCHttpUtils.post()
                        .url(HttpApi.HX_GROUP_REMOVE_MEMBERS)
                        .addParams("groupId","27697479745538")
                        .addParams("jArray",array)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showToast(LoginActivity.this, "失败");
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                ToastUtils.showToast(mContext, "成功");
                                LogUtils.d("成功" + response);
                                readyGo(MainActivity.class);
                            }
                        });*/

                /*SCHttpUtils.post()
                        .url(HttpApi.HX_GROUP_OWNER)
                        .addParams("groupId", "27697479745538")
                        .addParams("newowner", "sc-738727063")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showToast(LoginActivity.this, "失败");
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                ToastUtils.showToast(mContext, "成功");
                                LogUtils.d("成功" + response);
                                readyGo(MainActivity.class);
                            }
                        });*/

                //删除群
                /*SCHttpUtils.post()
                        .url(HttpApi.HX_GROUP_DEL)
                        .addParams("groupId","27697459822593")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                LogUtils.d("删除群失败");
                                e.printStackTrace();
                                ToastUtils.showToast(mContext,"失败");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                LogUtils.d("成功" + response);
                                ToastUtils.showToast(mContext,"成功");
                            }
                        });*/

                //修改群信息
                /*ModifyGroupInfo modifyGroupInfo = new ModifyGroupInfo();
                modifyGroupInfo.setGroupname(trim);
                modifyGroupInfo.setDescription("hahahaha");
                String modifyJson = new Gson().toJson(modifyGroupInfo);
                SCHttpUtils.post()
                        .url(HttpApi.HX_GROUP_MODIFY)
                        .addParams("groupId", "27697479745538")
                        .addParams("dataString", modifyJson)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showToast(mContext, "失败");
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                ToastUtils.showToast(mContext, "成功");
                                LogUtils.d("成功" + response);
                            }
                        });*/


                //创建群组
                /*CreatGroupInfo groupInfo = new CreatGroupInfo();
                groupInfo.setGroupname(trim);
                groupInfo.setApproval(true);
                groupInfo.setOwner("sc-738727063");
                groupInfo.setDesc("测试创建的群");
                groupInfo.setMaxusers(300);
                List<String> list = new ArrayList<>();
                list.add("sc-738727190");
                list.add("sc-738727191");
                list.add("sc-1425704407");
                groupInfo.setMembers(list);
                groupInfo.setPublicX(true);
                Gson gson = new Gson();
                String json = gson.toJson(groupInfo);
                SCHttpUtils.post()
                        .url(HttpApi.HX_GROUP_CREATE)
                        //.addParams("token","dawdawd")
                        .addParams("dataString",json)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showToast(LoginActivity.this,"失败");
                                LogUtils.d("失败" + id);
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                ToastUtils.showToast(mContext,"成功");
                                LogUtils.d("成功" + response);
                                readyGo(MainActivity.class);
                            }
                        });
*/

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
