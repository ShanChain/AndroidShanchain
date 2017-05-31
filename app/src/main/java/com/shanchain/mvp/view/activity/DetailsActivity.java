package com.shanchain.mvp.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.R;
import com.shanchain.adapter.CommentsAdapter;
import com.shanchain.base.BaseActivity;
import com.shanchain.widgets.dialog.CustomDialog;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;

import butterknife.Bind;

public class DetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {


    ArthurToolBar mToolbarDetails;
    @Bind(R.id.xrl_details)
    XRecyclerView mXrlDetails;
    @Bind(R.id.activity_details)
    LinearLayout mActivityDetails;
    private ArrayList<String> mDatas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_details;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initDatas();
        initRecyclerView();
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i ++) {
            mDatas.add("" + i);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mXrlDetails.setLayoutManager(layoutManager);
        mXrlDetails.setPullRefreshEnabled(false);
        mXrlDetails.setLoadingMoreEnabled(true);
        mXrlDetails.addHeaderView(View.inflate(this,R.layout.details_header_comments,null));

        RecyclerView.Adapter adapter = new CommentsAdapter(this,R.layout.item_comments,mDatas);
        mXrlDetails.setAdapter(adapter);
    }

    private void initToolBar() {
        mToolbarDetails = (ArthurToolBar) findViewById(R.id.toolbar_details);
        mToolbarDetails.setEnabled(true);
        mToolbarDetails.setOnLeftClickListener(this);
        mToolbarDetails.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick( View v) {
        CustomDialog dialog = new CustomDialog(this,true,R.layout.pop_report,new int[]{R.id.btn_pop_report,R.id.btn_pop_cancle});
        dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()){
                    case R.id.btn_pop_report:
                        readyGo(ReportActivity.class);
                        break;
                    case R.id.btn_pop_cancle:
                        break;
                }
            }
        });
        dialog.show();
    }
}
