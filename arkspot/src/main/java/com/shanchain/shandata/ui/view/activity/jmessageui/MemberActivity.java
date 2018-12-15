package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ContactAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.Members;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.ChatRoomManager;
import cn.jpush.im.android.api.JMessageClient;
import okhttp3.Call;

public class MemberActivity extends BaseActivity {


    @Bind(R.id.tb_main)
    ArthurToolBar tbMain;
    @Bind(R.id.rv_message_list)
    RecyclerView rvMessageList;

    private String roomID;
    private List chatRoomlist = new ArrayList();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_member;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        roomID = intent.getStringExtra("roomId");
        initToolBar();
        initData();
    }

    private void initData() {
        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHAT_ROOM_MEMBER)
                .addParams("roomId",roomID)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("获取聊天成员失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = com.alibaba.fastjson.JSONObject.parseObject(response).getString("code");

                        if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                            String data = com.alibaba.fastjson.JSONObject.parseObject(response).getString("data");
                            List<Members> membersList = JSONArray.parseArray(data,Members.class);
                            chatRoomlist.addAll(membersList);
                             LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MemberActivity.this, LinearLayoutManager.VERTICAL, false);
                             rvMessageList.setLayoutManager(linearLayoutManager);
                             rvMessageList.setAdapter(new BaseQuickAdapter<Members,BaseViewHolder>(R.layout.item_members_chat_room,chatRoomlist) {
                                @Override
                                protected void convert(BaseViewHolder helper, Members item) {
                                    helper.setText(R.id.tv_item_contact_child_name,item.getHxUserName());
                                    helper.setText(R.id.tv_item_contact_child_focus,"对话");
                                }

                            });
                        }
                    }
                });

    }

    private void initToolBar() {
        tbMain.setTitleTextColor(getResources().getColor(R.color.colorTextDefault));
        tbMain.isShowChatRoom(false);//不在导航栏显示聊天室信息
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tbMain.getTitleView().setLayoutParams(layoutParams);
        tbMain.setTitleText("群成员");
        tbMain.setBackgroundColor(getResources().getColor(R.color.white));
        tbMain.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        tbMain.setOnLeftClickListener(new ArthurToolBar.OnLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
