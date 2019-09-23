package com.shanchain.shandata.ui.view.activity.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ClientListAdapter;
import com.shanchain.shandata.adapter.QuestionListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.rn.activity.SCWebViewXYActivity;
import com.shanchain.shandata.ui.model.QuestionBean;
import com.shanchain.shandata.ui.view.activity.jmessageui.SingerChatInfoActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.SingleChatActivity;
import com.shanchain.shandata.ui.view.activity.mine.QuestionDetailActivity;
import com.shanchain.shandata.ui.view.activity.mine.ReturnInvationActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.jiguang.imui.model.DefaultUser;

/**
 * Created by WealChen
 * Date : 2019/9/6
 * Describe :
 */
public class ClientListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        ArthurToolBar.OnLeftClickListener {
    @Bind(R.id.tb_about)
    ArthurToolBar tbTaskComment;
    @Bind(R.id.recycler_view_coupon)
    RecyclerView recyclerViewCoupon;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.lv_queston)
    ListView lvQueston;

    private ClientListAdapter mListAdapter;
    private QuestionListAdapter mQuestionListAdapter;
    private List<QuestionBean> mQuestionBeanList = new ArrayList<>();
    private List<DefaultUser> mList = new ArrayList<>();
    /*private String clientName [] = new String[]{"156505544974000163e0aa65869682","154233182630000163e0aa658298481",""};//测试*/
    private String clientName [] = new String[]{"154260669669500163e0aa658733721","156585492702200163e04797928698242","154279401865600163e0479798782"};//正式
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_client_service;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.login_marjar_color),
                getResources().getColor(R.color.register_marjar_color),getResources().getColor(R.color.google_yellow));
        mListAdapter = new ClientListAdapter(R.layout.activity_client_item,mList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCoupon.setLayoutManager(layoutManager);
        recyclerViewCoupon.setAdapter(mListAdapter);
        String clientAttr[] = new String[]{getResources().getString(R.string.bussiness_server),getResources().getString(R.string.technical_server_1),getResources().getString(R.string.technical_server_2)};//测试
        for (int i = 0; i <3; i++) {
            DefaultUser defaultUser = new DefaultUser(i,clientAttr[i],"");
            defaultUser.setHxUserId(clientName[i]);
            defaultUser.setSignature("");
            defaultUser.setUserType(1);
            defaultUser.setId(i+1);
            mList.add(defaultUser);
        }
        mListAdapter.notifyDataSetChanged();

        mListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DefaultUser user = mListAdapter.getItem(position);
                if(user.getUserType() == 1){
                    Intent intent = new Intent(ClientListActivity.this, SingleChatActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putParcelable("userInfo", user);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                }
            }
        });
        mQuestionListAdapter = new QuestionListAdapter(this);
        mQuestionBeanList.add(new QuestionBean(1,getString(R.string.question_1),"https://mp.weixin.qq.com/s/3jf9PIes_KHYNGyvHo96kA"));
        mQuestionBeanList.add(new QuestionBean(2,getString(R.string.question_2),""));
        mQuestionBeanList.add(new QuestionBean(3,getString(R.string.question_3),""));
        mQuestionBeanList.add(new QuestionBean(4,getString(R.string.question_4),""));
        mQuestionBeanList.add(new QuestionBean(5,getString(R.string.question_5),""));
        mQuestionBeanList.add(new QuestionBean(6,getString(R.string.question_6),""));
        mQuestionBeanList.add(new QuestionBean(7,getString(R.string.question_7),""));
        mQuestionBeanList.add(new QuestionBean(8,getString(R.string.question_8),""));
        mQuestionListAdapter.setList(mQuestionBeanList);
        lvQueston.setAdapter(mQuestionListAdapter);

        lvQueston.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionBean q = (QuestionBean) mQuestionListAdapter.getItem(position);
                if(q.getId()==1){
                    Intent intent = new Intent(ClientListActivity.this, SCWebViewXYActivity.class);
                    JSONObject obj = new JSONObject();
                    obj.put("title",q.getTitle());
                    obj.put("url", q.getUrl());
                    String webParams = obj.toJSONString();
                    intent.putExtra("webParams", webParams);
                    startActivity(intent);
                }else {
                    startActivity(new Intent(ClientListActivity.this, QuestionDetailActivity.class)
                    .putExtra("info",q));
                }
            }
        });


    }

    private void initToolBar() {
        tbTaskComment.setTitleText(getString(R.string.client_center));
        tbTaskComment.setTitleTextColor(Color.BLACK);
        tbTaskComment.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        tbTaskComment.setOnLeftClickListener(this);

    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
