package com.shanchain.shandata.ui.view.activity.article;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.shanchain.shandata.interfaces.ICommentPraiseCallback;
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

import java.util.ArrayList;
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
    private CommentEntity mCommentEntity;
    private ArticleDetailPresenter mPresenter;
    private String userId = SCCacheUtils.getCache("0", "curUser");
    private CommetListAdapter mAdapter;
    private List<CommentEntity> mList = new ArrayList<>();
    private String []attrPhotos;
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
        mAdapter = new CommetListAdapter(this);
        etContent.setExpandState(ExpandableTextView.STATE_EXPAND);
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
            attrPhotos = mSqureDataEntity.getListImg().split(",");
            gvPhotoAdapter.setPhotoList(Arrays.asList(attrPhotos));
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

        mPresenter.getAllArticleComment(Integer.parseInt(userId),mSqureDataEntity.getId(),0,1000);
        initListener();
    }
    //发表评论
    @OnClick(R.id.tv_send)
    void addComment(){
        String comment = etContentInput.getText().toString().trim();
        if(TextUtils.isEmpty(comment)){
            ToastUtils.showToast(ArticleDetailActivity.this, getResources().getString(R.string.enter_comment));
            return;
        }
        mPresenter.addComment(mSqureDataEntity.getId(),comment,Integer.parseInt(userId),
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

    //文章点赞
    @OnClick(R.id.im_zan)
    void praiseTitle(){
        if("0".equals(mSqureDataEntity.getIsPraise())){//未点赞
            mPresenter.addPraiseToArticle(Integer.parseInt(userId),mSqureDataEntity.getId());
        }else {
            mPresenter.deletePraiseToArticle(Integer.parseInt(userId),mSqureDataEntity.getId());
        }
    }

    private void initListener(){
        mAdapter.setICommentPraiseCallback(new ICommentPraiseCallback() {
            @Override
            public void praiseToUser(CommentEntity item) {
                mCommentEntity = item;
                if(Integer.parseInt(userId) == item.getSendUserId()){
                    ToastUtil.showToast(ArticleDetailActivity.this, R.string.myself_attention);
                    return;
                }
                if("0".equals(item.getIsAttention())){
                    mPresenter.addAttentToCommentUser(Integer.parseInt(userId),item.getSendUserId());
                }else {
                    mPresenter.deleteAttentionCommentUser(Integer.parseInt(userId),item.getSendUserId());
                }

            }
        });

        //点击图片查看大图
        gvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<String> list = Arrays.asList(attrPhotos);
                ArrayList<String> arrayList = new ArrayList<>(list);
                startActivity(new Intent(ArticleDetailActivity.this,PhotoPagerActivity.class)
                                        .putExtra("list", arrayList));
            }
        });
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
            mList.clear();
            mList = JSONObject.parseArray(listdata,CommentEntity.class);
            if(mList!=null && mList.size()>0){
                clContentList.setVisibility(View.VISIBLE);
                llNotdata.setVisibility(View.GONE);
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
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            etContentInput.setText("");
            hideKeyboard(etContentInput);
            mPresenter.getAllArticleComment(Integer.parseInt(userId),mSqureDataEntity.getId(),0,1000);

            mSqureDataEntity.setReviceCount(mSqureDataEntity.getReviceCount()+1);
            tvMessage.setText(mSqureDataEntity.getReviceCount()+"");
            tvContentNum.setText(getString(R.string.comment_nums,mSqureDataEntity.getReviceCount()+""));
        }else {
            ToastUtil.showToast(ArticleDetailActivity.this, R.string.operation_failed);
        }
    }

    public void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    @Override
    public void setAttentionResponse(String response, int type) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            updateUserCommentListFromDetail(type);
            if(type==0){
                updateUserAttention(1);
                mSqureDataEntity.setIsAttention("1");
                ToastUtil.showToast(ArticleDetailActivity.this,getString(R.string.Concerned));
            }else {
                updateUserAttention(0);
                mSqureDataEntity.setIsAttention("0");
                ToastUtil.showToast(ArticleDetailActivity.this, R.string.unfollowed);
            }
            //更新评论列表的关注状态


        }else {
            ToastUtil.showToast(ArticleDetailActivity.this, R.string.operation_failed);
        }
    }

    @Override
    public void setPraiseResponse(String response, int type) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            if(type==0){
                mSqureDataEntity.setIsPraise("1");
                mSqureDataEntity.setPraiseCount(mSqureDataEntity.getPraiseCount()+1);
                imZan.setBackgroundResource(R.mipmap.dianzan_done);
                ToastUtil.showToast(ArticleDetailActivity.this, R.string.liked);
            }else {
                mSqureDataEntity.setIsPraise("0");
                mSqureDataEntity.setPraiseCount(mSqureDataEntity.getPraiseCount()-1);
                imZan.setBackgroundResource(R.mipmap.dianzan);
                ToastUtil.showToast(ArticleDetailActivity.this, R.string.cancel_like);
            }
            tvConin.setText(mSqureDataEntity.getPraiseCount()+"");

        }else {
            ToastUtil.showToast(ArticleDetailActivity.this, R.string.operation_failed);
        }
    }

    @Override
    public void setAttentionCommentResponse(String response, int type) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            updateUserCommentList(type);
            updateUserAttentionData(type);
            if(type==0){
                ToastUtil.showToast(ArticleDetailActivity.this,getString(R.string.Concerned));
            }else {
                ToastUtil.showToast(ArticleDetailActivity.this, R.string.unfollowed);
            }
        }else {
            ToastUtil.showToast(ArticleDetailActivity.this, R.string.operation_failed);
        }
    }

    //详情关注用户后更新状态
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

    //评论列表关注成功后更新界面
    private void updateUserCommentList(int type){
        if(mCommentEntity == null || mList.size()==0)return;
        for (CommentEntity s:mList) {
            if(s.getSendUserId() == mCommentEntity.getSendUserId()){
                if(type==0){
                    s.setIsAttention("1");
                }else {
                    s.setIsAttention("0");
                }
                continue;
            }
        }
        mAdapter.notifyDataSetChanged();
    }
    //如果评论列表更新关注后更新详情用户的关注状态
    private void updateUserAttentionData(int type){
        if(mSqureDataEntity.getUserId() == mCommentEntity.getSendUserId()){
            if(type==0){
                updateUserAttention(1);
                mSqureDataEntity.setIsAttention("1");
            }else {
                updateUserAttention(0);
                mSqureDataEntity.setIsAttention("0");
            }
        }
    }
    //详情界面更新关注状态后更新评论列表用户的关注状态
    private void updateUserCommentListFromDetail(int type){
        if(mSqureDataEntity == null || mList.size()==0)return;
        for (CommentEntity s:mList) {
            if(s.getSendUserId() == mSqureDataEntity.getUserId()){
                if(type==0){
                    s.setIsAttention("1");
                }else {
                    s.setIsAttention("0");
                }
                continue;
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
