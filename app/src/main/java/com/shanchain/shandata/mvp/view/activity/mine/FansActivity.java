package com.shanchain.shandata.mvp.view.activity.mine;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.FocusAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.FocusInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class FansActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarFans;
    @Bind(R.id.xrv_fans)
    XRecyclerView mXrvFans;
    private List<FocusInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_fans;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mXrvFans.setLayoutManager(layoutManager);
        mXrvFans.setLoadingMoreEnabled(false);
        mXrvFans.setPullRefreshEnabled(false);

        mXrvFans.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this,1),
                getResources().getColor(R.color.colorAddFriendDivider)));


        FocusAdapter adapter = new FocusAdapter(this,R.layout.item_focus,datas);
        mXrvFans.setAdapter(adapter);

    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 32; i ++) {
            FocusInfo focusInfo = new FocusInfo();
            focusInfo.setName("张小杨");
            focusInfo.setFocused(false);
            datas.add(focusInfo);
        }
    }

    private void initToolBar() {
        mToolbarFans = (ArthurToolBar) findViewById(R.id.toolbar_fans);
        mToolbarFans.setBtnVisibility(true,false);
        mToolbarFans.setBtnEnabled(true,false);
        mToolbarFans.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
