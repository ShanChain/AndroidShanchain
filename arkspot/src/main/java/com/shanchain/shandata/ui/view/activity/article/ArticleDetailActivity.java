package com.shanchain.shandata.ui.view.activity.article;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.vod.common.utils.ToastUtil;
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
    @Bind(R.id.im_zan)
    ImageView imZan;
    private SqureDataEntity mSqureDataEntity;
    private ArticleDetailPresenter mPresenter;
    private String userId = SCCacheUtils.getCache("0", "curUser");
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
            String []attr = mSqureDataEntity.getListImg().split(",");
            gvPhotoAdapter.setPhotoList(Arrays.asList(attr));
            gvPhoto.setAdapter(gvPhotoAdapter);
        }
        if("0".equals(mSqureDataEntity.getIsPraise())){
            //未点赞
            imZan.setBackgroundResource(R.mipmap.dianzan);
        }else {
            imZan.setBackgroundResource(R.mipmap.dianzan_done);
        }
        if("0".equals(mSqureDataEntity.getIsAttention())){
            updateUserAttention(0);
        }else {
            updateUserAttention(1);
        }

        mPresenter.getAllArticleComment(mSqureDataEntity.getId(),0,1000);

    }
    //发表评论
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

    //关注，取消关注
    @OnClick(R.id.tv_attention)
    void attentionOperation(){
        if(Integer.parseInt(userId) == mSqureDataEntity.getUserId()){
            ToastUtil.showToast(ArticleDetailActivity.this, R.string.myself_attention);
            return;
        }
        if("0".equals(mSqureDataEntity.getIsAttention())){
            mPresenter.attentionUser(Integer.parseInt(userId),mSqureDataEntity.getUserId());
        }else {
            mPresenter.deleteAttentionUser(Integer.parseInt(userId),mSqureDataEntity.getUserId());
        }
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

    @Override
    public void setAttentionResponse(String response, int type) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            if(type==0){
                updateUserAttention(1);
                mSqureDataEntity.setIsAttention("1");
                ToastUtil.showToast(ArticleDetailActivity.this,getString(R.string.Concerned));
            }else {
                updateUserAttention(0);
                mSqureDataEntity.setIsAttention("0");
                ToastUtil.showToast(ArticleDetailActivity.this, R.string.unfollowed);
            }

        }else {
            ToastUtil.showToast(ArticleDetailActivity.this, R.string.operation_failed);
        }
    }

    private void updateUserAttention(int type) {
        if(type==0){
            tvAttention.setBackgroundResource(R.drawable.squra_attention_n_shape);
            tvAttention.setTextColor(getResources().getColor(R.color.login_marjar_color));
            tvAttention.setText(getResources().getString(R.string.attention));
        }else {
            tvAttention.setBackgroundResource(R.drawable.squra_attention_y_shape);
            tvAttention.setTextColor(getResources().getColor(R.color.white));
            tvAttention.setText(getResources().getString(R.string.Concerned));
        }
    }
}
