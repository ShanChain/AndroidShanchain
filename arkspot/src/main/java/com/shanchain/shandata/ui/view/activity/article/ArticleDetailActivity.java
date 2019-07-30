package com.shanchain.shandata.ui.view.activity.article;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CommetListAdapter;
import com.shanchain.shandata.adapter.GVPhotoAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.CommentEntity;
import com.shanchain.shandata.ui.model.SqureDataEntity;
import com.shanchain.shandata.ui.presenter.ArticleDetailPresenter;
import com.shanchain.shandata.ui.presenter.impl.ArticleDetailPresenterImpl;
import com.shanchain.shandata.ui.view.activity.article.view.ArticleDetailView;
import com.shanchain.shandata.utils.TimeUtils;
import com.shanchain.shandata.widgets.CustomGridView;
import com.shanchain.shandata.widgets.CustomListView;
import com.shanchain.shandata.widgets.ExpandableTextView;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.imui.view.CircleImageView;

/**
 * Created by WealChen
 * Date : 2019/7/29
 * Describe :
 */
public class ArticleDetailActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener,
        ArticleDetailView {
    @Bind(R.id.tb_task_comment)
    ArthurToolBar tbTaskComment;
    @Bind(R.id.iv_user_head)
    CircleImageView ivUserHead;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_attention)
    TextView tvAttention;
    @Bind(R.id.et_content)
    ExpandableTextView etContent;
    @Bind(R.id.gv_photo)
    CustomGridView gvPhoto;
    @Bind(R.id.tv_conin)
    TextView tvConin;
    @Bind(R.id.tv_message)
    TextView tvMessage;
    @Bind(R.id.tv_share)
    TextView tvShare;
    @Bind(R.id.tv_content_num)
    TextView tvContentNum;
    @Bind(R.id.cl_content_list)
    CustomListView clContentList;
    @Bind(R.id.et_content_input)
    EditText etContentInput;
    @Bind(R.id.tv_send)
    TextView tvSend;
    @Bind(R.id.ll_notdata)
    LinearLayout llNotdata;
    private SqureDataEntity mSqureDataEntity;
    private ArticleDetailPresenter mPresenter;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_article_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        tbTaskComment.setTitleText(getResources().getString(R.string.detail_1));
        tbTaskComment.setTitleTextColor(Color.BLACK);
        tbTaskComment.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        tbTaskComment.setOnLeftClickListener(this);

        mSqureDataEntity = (SqureDataEntity) getIntent().getSerializableExtra("info");
        mPresenter = new ArticleDetailPresenterImpl(this);
        initData();
    }
    //初始化数据
    private void initData(){
        if(mSqureDataEntity == null)return;
        Glide.with(mContext).load(mSqureDataEntity.getHeadIcon())
                .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                        .error(R.drawable.aurora_headicon_default)).into(ivUserHead);
        tvNickname.setText(mSqureDataEntity.getNickName());
        tvTime.setText(TimeUtils.friendlyTime1(new Date(mSqureDataEntity.getCreateTime())));
        etContent.setText(mSqureDataEntity.getContent());
        tvConin.setText(mSqureDataEntity.getPraiseCount()+"");
        tvMessage.setText(mSqureDataEntity.getReviceCount()+"");
        tvShare.setText(getString(R.string.share_1));
        tvContentNum.setText(getString(R.string.comment_nums,mSqureDataEntity.getReviceCount()+""));
        if(!TextUtils.isEmpty(mSqureDataEntity.getListImg())){
            GVPhotoAdapter gvPhotoAdapter = new GVPhotoAdapter(mContext);
            String []attr = mSqureDataEntity.getListImg().substring(1,mSqureDataEntity.getListImg().length()-1).split(",");
            gvPhotoAdapter.setPhotoList(Arrays.asList(attr));
            gvPhoto.setAdapter(gvPhotoAdapter);
        }

        mPresenter.getAllArticleComment(mSqureDataEntity.getId(),0,1000);

    }

    @OnClick(R.id.tv_send)
    void addComment(){
        String comment = etContentInput.getText().toString().trim();
        if(TextUtils.isEmpty(comment)){
            ToastUtils.showToast(ArticleDetailActivity.this, getResources().getString(R.string.enter_comment));
            return;
        }
        mPresenter.addComment(mSqureDataEntity.getId(),comment,Integer.parseInt(SCCacheUtils.getCache("0", "curUser")),
                mSqureDataEntity.getUserId());
    }


    @Override
    public void onLeftClick(View v) {
        finish();
    }



    @Override
    public void showProgressStart() {
        showLoadingDialog();
    }

    @Override
    public void showProgressEnd() {
        closeLoadingDialog();
    }

    @Override
    public void setCommentList(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            String data = JSONObject.parseObject(response).getString("data");
            String listdata = JSONObject.parseObject(data).getString("content");
            if(TextUtils.isEmpty(listdata)){
                clContentList.setVisibility(View.GONE);
                llNotdata.setVisibility(View.VISIBLE);
                return;
            }
            List<CommentEntity> mList = JSONObject.parseArray(listdata,CommentEntity.class);
            if(mList!=null && mList.size()>0){
                clContentList.setVisibility(View.VISIBLE);
                llNotdata.setVisibility(View.GONE);
                CommetListAdapter mAdapter = new CommetListAdapter(this);
                mAdapter.setList(mList);
                clContentList.setAdapter(mAdapter);
            }else {
                clContentList.setVisibility(View.GONE);
                llNotdata.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void addCommentResponse(String response) {

    }
}
