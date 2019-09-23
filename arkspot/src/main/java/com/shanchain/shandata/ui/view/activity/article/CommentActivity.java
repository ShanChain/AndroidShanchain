package com.shanchain.shandata.ui.view.activity.article;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CommetSecondListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.interfaces.IReplyCommentCallback;
import com.shanchain.shandata.ui.model.CommentEntity;
import com.shanchain.shandata.ui.presenter.CommentPresenter;
import com.shanchain.shandata.ui.presenter.impl.CommentPresenterImpl;
import com.shanchain.shandata.ui.view.activity.article.view.CommentView;
import com.shanchain.shandata.utils.TimeUtils;
import com.shanchain.shandata.widgets.CustomListView;
import com.shanchain.shandata.widgets.SwipeFinishLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jiguang.imui.view.CircleImageView;

/**
 * Created by WealChen
 * Date : 2019/9/20
 * Describe :
 */
public class CommentActivity extends SwipeFinishActivity implements CommentView{
    @Bind(R.id.iv_user_head1)
    CircleImageView ivUserHead1;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_delete)
    TextView tvDelete;
    @Bind(R.id.tv_attention)
    TextView tvAttention;
    @Bind(R.id.tv_comment)
    TextView tvComment;
    @Bind(R.id.tv_content_num)
    TextView tvContentNum;
    @Bind(R.id.cl_content_list)
    CustomListView clContentList;
    @Bind(R.id.et_content_input)
    EditText etContentInput;
    @Bind(R.id.tv_send)
    TextView tvSend;
    @Bind(R.id.im_back)
    ImageView imBack;
    @Bind(R.id.tv_reply_num)
    TextView tvReplyNum;

    private CommentEntity commentEntity;
    private CommentPresenter mCommentPresenter;
    private String userId = SCCacheUtils.getCache("0", "curUser");
    private List<CommentEntity> mList = new ArrayList<>();
    private CommetSecondListAdapter mListAdapter;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        setSlideFinishFlags(SwipeFinishLayout.FLAG_SCROLL_DOWN_FINISH);
        commentEntity = (CommentEntity) getIntent().getSerializableExtra("info");
        mCommentPresenter = new CommentPresenterImpl(this);
        mListAdapter = new CommetSecondListAdapter(this);
        initData();
    }

    private void initData(){
        if(commentEntity==null)return;
        Glide.with(this).load(commentEntity.getSendHeadIcon())
                .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                        .error(R.drawable.aurora_headicon_default)).into(ivUserHead1);
        tvNickname.setText(commentEntity.getSendNickName());
        tvTime.setText(TimeUtils.friendlyTime1(this,new Date(commentEntity.getCreateTime())));
        tvComment.setText(commentEntity.getContent());
        if("0".equals(commentEntity.getIsAttention())){
            //未关注
            tvAttention.setBackgroundResource(R.drawable.squra_attention_n_shape);
            tvAttention.setTextColor(this.getResources().getColor(R.color.login_marjar_color));
            tvAttention.setText(this.getResources().getString(R.string.attention));
        }else {
            tvAttention.setBackgroundResource(R.drawable.squra_attent_shape);
            tvAttention.setTextColor(this.getResources().getColor(R.color.white));
            tvAttention.setText(this.getResources().getString(R.string.Concerned));
        }
        getCommentList();
        initListener();
    }

    //获取所有评论列表
    private void getCommentList(){
        mCommentPresenter.getCommentSecontList(commentEntity.getInvitationId(),commentEntity.getId(),0,1000);
    }

    private void initListener(){
        mListAdapter.setIReplyCommentCallback(new IReplyCommentCallback() {
            @Override
            public void replyComment(CommentEntity commentEntity) {
                etContentInput.setHint("回复: "+commentEntity.getSendUserId());
                hideKeyboard(etContentInput,true);
            }
        });

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(velocityY) < 100) {
                    // System.out.println("手指移动的太慢了");
                    return true;
                }

                // 手势向下 down
                if ((e2.getRawY() - e1.getRawY()) > 200) {
                    finish();//在此处控制关闭
                    return true;
                }
                // 手势向上 up
                if ((e1.getRawY() - e2.getRawY()) > 200) {
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    //发表评论
    @OnClick(R.id.tv_send)
    void addComment(){
        String comment = etContentInput.getText().toString().trim();
        if(TextUtils.isEmpty(comment)){
            ToastUtils.showToast(CommentActivity.this, getResources().getString(R.string.enter_comment));
            return;
        }
        mCommentPresenter.addCommentUser(Integer.parseInt(userId),commentEntity.getSendUserId(),
                commentEntity.getInvitationId(),commentEntity.getId(),commentEntity.getId(),comment);
    }

    @OnClick(R.id.im_back)
    void finished(){
        this.finish();
    }

    /*@Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.out_to_down);
    }*/

    @Override
    public void showProgressStart() {
    }

    @Override
    public void showProgressEnd() {
    }

    @Override
    public void setCommentSencondListResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            String data = JSONObject.parseObject(response).getString("data");
            String listdata = JSONObject.parseObject(data).getString("content");
            if(TextUtils.isEmpty(listdata)){
                /*clContentList.setVisibility(View.GONE);
                llNotdata.setVisibility(View.VISIBLE);*/
                return;
            }
            mList.clear();
            mList = JSONObject.parseArray(listdata,CommentEntity.class);
            if(mList!=null && mList.size()>0){
                clContentList.setVisibility(View.VISIBLE);
                /*llNotdata.setVisibility(View.GONE);*/
                mListAdapter.setList(mList);
                clContentList.setAdapter(mListAdapter);
                mListAdapter.notifyDataSetChanged();
                tvReplyNum.setText(getString(R.string.reply_nums,mList.size()+""));
            }

        }
    }

    @Override
    public void addCommentResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            etContentInput.setText("");
            hideKeyboard(etContentInput,false);
            getCommentList();
        }
    }

    public void hideKeyboard(View view,boolean isOpen){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            if(!isOpen){
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
            }else {
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
