package com.shanchain.shandata.ui.view.activity.chat;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ChatAtAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.GroupBriefBean;
import com.shanchain.shandata.widgets.listener.SCTextWatcher;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

public class ChatAtActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    private static final int RESULT_CODE = 100;
    @Bind(R.id.tb_chat_at)
    ArthurToolBar mTbChatAt;
    @Bind(R.id.et_chat_at)
    EditText mEtChatAt;
    @Bind(R.id.rv_chat_at)
    RecyclerView mRvChatAt;
    private List<GroupBriefBean> datas = new ArrayList<>();
    private List<GroupBriefBean> show = new ArrayList<>();
    private ChatAtAdapter mAdapter;
    private ArrayList<String> selected = new ArrayList<>();
    private String mGroupId;
    private ArrayList<String> mMembers;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_chat_at;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        mGroupId = intent.getStringExtra("groupId");
        mMembers = intent.getStringArrayListExtra("members");
        if (TextUtils.isEmpty(mGroupId) || mMembers == null) {
            LogUtils.i("=====群id为空=====");
            finish();
            return;
        }
        initToolBar();
        initRecyclerView();
        initData();
    }


    private void initRecyclerView() {
        mRvChatAt.setLayoutManager(new LinearLayoutManager(this));
        mRvChatAt.addItemDecoration(new RecyclerViewDivider(this));
        mAdapter = new ChatAtAdapter(R.layout.item_chat_at, show);
        mRvChatAt.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GroupBriefBean groupBriefBean = show.get(position);
                Intent intent = new Intent();
                intent.putExtra("at",groupBriefBean);
                setResult(RESULT_CODE,intent);
                finish();
            }
        });

        mEtChatAt.addTextChangedListener(new SCTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

    }

    private void initData() {
        String jArr = JSONObject.toJSONString(mMembers);
        SCHttpUtils.post()
                .url(HttpApi.HX_USER_USERNAME_LIST)
                .addParams("jArray", jArr)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("获取群成员信息失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到群成员信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                List<GroupBriefBean> groupList = JSONObject.parseArray(data, GroupBriefBean.class);
                                datas.addAll(groupList);
                                show.addAll(groupList);
                                mAdapter.notifyDataSetChanged();
                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                });


    }

    private void initToolBar() {
        mTbChatAt.setBtnEnabled(true, false);
        mTbChatAt.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
