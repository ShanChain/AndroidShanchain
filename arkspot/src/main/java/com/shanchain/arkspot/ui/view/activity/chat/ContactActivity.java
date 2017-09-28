package com.shanchain.arkspot.ui.view.activity.chat;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.ContactAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.http.HttpApi;
import com.shanchain.arkspot.ui.model.ContactInfo;
import com.shanchain.arkspot.ui.model.FansInfo;
import com.shanchain.arkspot.ui.model.FansListInfo;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.netrequest.SCHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;


public class ContactActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_contact)
    ArthurToolBar mTbContact;
    @Bind(R.id.et_contact)
    EditText mEtContact;
    @Bind(R.id.elv_contact)
    ExpandableListView mElvContact;
    List<String> parentList;
    Map<String, ArrayList<ContactInfo>> map;
    private ContactAdapter mContactAdapter;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_contact;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initListView();
    }

    private void initListView() {
        mContactAdapter = new ContactAdapter(parentList, map);

        mElvContact.setAdapter(mContactAdapter);

        mElvContact.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String name = map.get(parentList.get(groupPosition)).get(childPosition).getName();
                //ToastUtils.showToast(mContext, "点击了" + parentList.get(groupPosition) + "的" + name);
                Intent intent = new Intent(mContext, ChatRoomActivity.class);
                if (parentList.get(groupPosition).equals("对话场景")) {
                    intent.putExtra("isGroup", true);
                } else {
                    intent.putExtra("isGroup", false);
                }
                intent.putExtra("toChatName", name);
                startActivity(intent);
                return true;
            }
        });


    }

    private void initData() {
        //当前登录的环信用户的id
        String currentUser = EMClient.getInstance().getCurrentUser();
        map = new HashMap<>();
        parentList = new ArrayList<>();
        parentList.add("我的关注");
        parentList.add("互相关注");
        parentList.add("我的粉丝");
        parentList.add("对话场景");
        //粉丝列表集合
        final ArrayList<ContactInfo> myFans = new ArrayList<>();
        //关注列表集合
        final ArrayList<ContactInfo> myFocus = new ArrayList<>();
        //对话场景列表集合
        ArrayList<ContactInfo> conversationScene = new ArrayList<>();
        //互相关注列表集合
        ArrayList<ContactInfo> eachFocus = new ArrayList<>();
        //获取本地所以的群
        List<EMGroup> allGroups = EMClient.getInstance().groupManager().getAllGroups();

        for (int i = 0; i < allGroups.size(); i++) {
            String groupId = allGroups.get(i).getGroupId();
            LogUtils.d("我加入的群组的id" + groupId);
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setName(groupId);
            conversationScene.add(contactInfo);
        }
        map.put("对话场景", conversationScene);

        //联系人
        try {
            List<String> contactsList = EMClient.getInstance().contactManager().getAllContactsFromServer();

            for (int i = 0; i < contactsList.size(); i++) {
                String contact = contactsList.get(i);
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.setName(contact);
                eachFocus.add(contactInfo);
            }
            LogUtils.d("获取联系人成功");
        } catch (HyphenateException e) {
            e.printStackTrace();
            LogUtils.e("获取联系人失败");
        }

        map.put("互相关注", eachFocus);

        //获取我的关注列表
        SCHttpUtils.post()
                .url(HttpApi.FOCUS_MY_FOCUS)
                .addParams("characterId", currentUser)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("获取关注列表失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("获取我的关注的" + response);
                        Gson gson = new Gson();
                        FansListInfo fansListInfo = gson.fromJson(response, FansListInfo.class);
                        if (fansListInfo == null) {
                            LogUtils.d("json解析失败");
                            return;
                        }
                        String code = fansListInfo.getCode();
                        LogUtils.d("返回码 + " + code);
                        LogUtils.d("返回的消息：" + fansListInfo.getMessage());
                        List<FansInfo> fansInfos = fansListInfo.getFansInfos();

                        LogUtils.d("关注数据集合 ： " + fansInfos.size());
                        for (int i = 0; i < fansInfos.size(); i++) {
                            FansInfo fansInfo = fansInfos.get(i);
                            LogUtils.d("关注环信id = " + fansInfo.getKey().getFunsId());
                            ContactInfo contactInfo = new ContactInfo();
                            contactInfo.setName(fansInfo.getKey().getFunsId() + "");
                            myFocus.add(contactInfo);
                        }
                        map.put("我的关注", myFocus);
                        mContactAdapter.notifyDataSetChanged();
                    }
                });

        map.put("我的关注", myFocus);

        //获取粉丝列表
        SCHttpUtils.post()
                .url(HttpApi.FOCUS_FANS)
                .addParams("characterId", currentUser)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("获取我的粉丝列表失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("获取我的粉丝列表成功" + response);
                        Gson gson = new Gson();
                        FansListInfo fansListInfo = gson.fromJson(response, FansListInfo.class);
                        List<FansInfo> fansInfos = fansListInfo.getFansInfos();
                        for (int i = 0; i < fansInfos.size(); i++) {
                            FansInfo fansInfo = fansInfos.get(i);
                            int fansId = fansInfo.getKey().getFunsId();
                            ContactInfo contactInfo = new ContactInfo();
                            contactInfo.setName("" + fansId);
                            myFans.add(contactInfo);
                        }
                        map.put("我的粉丝", myFans);
                        mContactAdapter.notifyDataSetChanged();
                    }
                });
        map.put("我的粉丝", myFans);

    }

    private void initToolBar() {
        mTbContact.setBtnEnabled(true, false);
        mTbContact.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
