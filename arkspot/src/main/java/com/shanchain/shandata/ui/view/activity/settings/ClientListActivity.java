package com.shanchain.shandata.ui.view.activity.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ClientListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.SingerChatInfoActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.SingleChatActivity;

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

    private ClientListAdapter mListAdapter;
    private List<DefaultUser> mList = new ArrayList<>();
    private String clientAttr[] = new String[]{"业务客服","技术客服1(优先)","技术客服2"};//测试
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

        for (int i = 0; i <3; i++) {
            DefaultUser defaultUser = new DefaultUser(i,clientAttr[i],"");
            defaultUser.setHxUserId(clientName[i]);
            defaultUser.setSignature("");
            defaultUser.setUserType(1);
            mList.add(defaultUser);
        }
        mListAdapter.notifyDataSetChanged();

        mListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DefaultUser user = mListAdapter.getItem(position);
                Intent intent = new Intent(ClientListActivity.this, SingleChatActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("userInfo", user);
                intent.putExtras(bundle1);
                startActivity(intent);
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
