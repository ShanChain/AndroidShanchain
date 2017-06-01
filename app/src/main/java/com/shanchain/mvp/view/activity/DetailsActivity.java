package com.shanchain.mvp.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.ninegridimageview.NineGridImageView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.R;
import com.shanchain.adapter.CommentsAdapter;
import com.shanchain.adapter.NineGridDetailsAdapter;
import com.shanchain.base.BaseActivity;
import com.shanchain.utils.ToastUtils;
import com.shanchain.widgets.dialog.CustomDialog;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class DetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {


    ArthurToolBar mToolbarDetails;
    @Bind(R.id.xrl_details)
    XRecyclerView mXrlDetails;
    @Bind(R.id.activity_details)
    LinearLayout mActivityDetails;

    @Bind(R.id.ll_details_comments)
    LinearLayout mLlDetailsComments;
    private ArrayList<String> mDatas;
    private View mHeader_info;
    private View mHeader_comments;
    private ArrayList<String> mImagesUrl;

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
        for (int i = 0; i < 20; i++) {
            mDatas.add("" + i);
        }

        mImagesUrl = new ArrayList<>();
        mImagesUrl.add("1");
        mImagesUrl.add("2");
        mImagesUrl.add("3");
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mXrlDetails.setLayoutManager(layoutManager);
        mXrlDetails.setPullRefreshEnabled(false);
        mXrlDetails.setLoadingMoreEnabled(true);
        //动态信息头布局
        mHeader_info = View.inflate(this, R.layout.details_header_info, null);
        initHeaderInfo();
        //评论信息头布局
        mHeader_comments = View.inflate(this, R.layout.details_header_comments, null);
        initHeaderComments();
        mXrlDetails.addHeaderView(mHeader_info);
        mXrlDetails.addHeaderView(mHeader_comments);
        //评论内容列表
        RecyclerView.Adapter adapter = new CommentsAdapter(this, R.layout.item_comments, mDatas);
        mXrlDetails.setAdapter(adapter);
    }


    private void initHeaderComments() {
        TextView tvHeaderComments = (TextView) mHeader_comments.findViewById(R.id.tv_header_comments);
        TextView tvHeaderPraise = (TextView) mHeader_comments.findViewById(R.id.tv_header_praise);
    }

    private void initHeaderInfo() {
        ImageView ivDetailsInfoAvatar = (ImageView) mHeader_info.findViewById(R.id.iv_details_info_avatar);
        TextView tvDetailsInfoName = (TextView) mHeader_info.findViewById(R.id.tv_details_info_name);
        TextView tvDetailsInfoTime = (TextView) mHeader_info.findViewById(R.id.tv_details_info_time);
        TextView tvDetailsInfoDes = (TextView) mHeader_info.findViewById(R.id.tv_details_info_des);
        NineGridImageView<String> nineGridImageView = (NineGridImageView<String>) mHeader_info.findViewById(R.id.ngiv_details_info_images);
        NineGridDetailsAdapter adapter = new NineGridDetailsAdapter();
        nineGridImageView.setAdapter(adapter);
        nineGridImageView.setImagesData(mImagesUrl);
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
    public void onRightClick(View v) {
        CustomDialog dialog = new CustomDialog(this, true, R.layout.pop_report, new int[]{R.id.btn_pop_report, R.id.btn_pop_cancle});
        dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
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




    @OnClick(R.id.ll_details_comments)
    public void onClick() {
        CustomDialog customDialog = new CustomDialog(this,true,R.layout.dialog_comments_input,new int[]{R.id.iv_dialog_publish});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                ToastUtils.showToast(DetailsActivity.this,"发表评论");
            }
        });
        customDialog.show();
    }
}
