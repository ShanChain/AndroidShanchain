package com.shanchain.mvp.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.R;
import com.shanchain.adapter.CommentsAdapter;
import com.shanchain.adapter.NineGridDetailsAdapter;
import com.shanchain.base.BaseActivity;
import com.shanchain.mvp.model.CommentsInfo;
import com.shanchain.utils.GlideCircleTransform;
import com.shanchain.utils.ToastUtils;
import com.shanchain.widgets.dialog.CustomDialog;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;

public class DetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, View.OnClickListener {


    ArthurToolBar mToolbarDetails;
    @Bind(R.id.xrl_details)
    XRecyclerView mXrlDetails;
    @Bind(R.id.activity_details)
    LinearLayout mActivityDetails;

    @Bind(R.id.iv_details_comment)
    ImageView mIvDetailsComment;
    @Bind(R.id.et_details_content_comments)
    EditText mEtDetailsContentComments;
    private ArrayList<String> mDatas;
    private ArrayList<CommentsInfo> mCommentsInfos;
    private View mHeader_info;
    private ArrayList<String> mImagesUrl;

    private ImageView mIvDetailsInfoAvatar;
    private TextView mTvDetailsInfoName;
    private TextView mTvDetailsInfoTime;
    private TextView mTvDetailsInfoDes;
    private NineGridImageView mNgivDetailsInfoImages;
    private LinearLayout mLlDetailHeaderChallenge;
    private ImageView mIvDetailsHeaderIcon;
    private TextView mTvDetailsHeaderChallenge;
    private TextView mTvDetailsHeaderTimeAddr;
    private TextView mTvDetailsHeaderChallengeDes;
    private TextView mTvDetailsHeaderChallengeCall;
    private TextView mTvHeaderComments;
    private TextView mTvHeaderPraise;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_details;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initDatas();
        initRecyclerView();
        initListener();
    }

    private void initListener() {
        mIvDetailsInfoAvatar.setOnClickListener(this);
        mTvDetailsInfoName.setOnClickListener(this);
    }

    private void initDatas() {
        mCommentsInfos = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            CommentsInfo commentsInfo = new CommentsInfo();
            commentsInfo.setLike(new Random().nextInt(300));
            commentsInfo.setTime(new Random().nextInt(60) + "分钟前");
            mCommentsInfos.add(commentsInfo);
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
        mXrlDetails.addHeaderView(mHeader_info);
        //评论内容列表
        RecyclerView.Adapter adapter = new CommentsAdapter(this, R.layout.item_comments, mCommentsInfos);
        mXrlDetails.setAdapter(adapter);
    }



    private void initHeaderInfo() {


        mIvDetailsInfoAvatar = (ImageView) mHeader_info.findViewById(R.id.iv_details_info_avatar);
        mTvDetailsInfoName = (TextView) mHeader_info.findViewById(R.id.tv_details_info_name);
        mTvDetailsInfoTime = (TextView) mHeader_info.findViewById(R.id.tv_details_info_time);
        mTvDetailsInfoDes = (TextView) mHeader_info.findViewById(R.id.tv_details_info_des);
        mNgivDetailsInfoImages = (NineGridImageView) mHeader_info.findViewById(R.id.ngiv_details_info_images);
        mLlDetailHeaderChallenge = (LinearLayout) mHeader_info.findViewById(R.id.ll_detail_header_challenge);
        mIvDetailsHeaderIcon = (ImageView) mHeader_info.findViewById(R.id.iv_details_header_icon);
        mTvDetailsHeaderChallenge = (TextView) mHeader_info.findViewById(R.id.tv_details_header_challenge);
        mTvDetailsHeaderTimeAddr = (TextView) mHeader_info.findViewById(R.id.tv_details_header_time_addr);
        mTvDetailsHeaderChallengeDes = (TextView) mHeader_info.findViewById(R.id.tv_details_header_challenge_des);
        mTvDetailsHeaderChallengeCall = (TextView) mHeader_info.findViewById(R.id.tv_details_header_challenge_call);
        mTvHeaderComments = (TextView) mHeader_info.findViewById(R.id.tv_header_comments);
        mTvHeaderPraise = (TextView) mHeader_info.findViewById(R.id.tv_header_praise);


        Glide.with(this).load(R.mipmap.logo)
                .transform(new GlideCircleTransform(DetailsActivity.this))
                .into(mIvDetailsInfoAvatar);
        mTvDetailsInfoName.setText("张建");
        mTvDetailsInfoTime.setText("15分钟前");
        mTvDetailsInfoDes.setText("今天玩的很开心。#善数者正式发布#祝善数者越办越好");

        NineGridDetailsAdapter adapter = new NineGridDetailsAdapter();
        mNgivDetailsInfoImages.setAdapter(adapter);
        mNgivDetailsInfoImages.setImagesData(mImagesUrl);



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


    @OnClick(R.id.iv_details_comment)
    public void onClick() {
        String commentsDetails = mEtDetailsContentComments.getText().toString().trim();
        if (TextUtils.isEmpty(commentsDetails)){
            ToastUtils.showToast(DetailsActivity.this,"您还没写点什么呢");
        }else {

        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_details_info_avatar:
                readyGo(PersonalHomePagerActivity.class);
                break;
            case R.id.tv_details_info_name:
                readyGo(PersonalHomePagerActivity.class);
                break;
        }
    }
}
