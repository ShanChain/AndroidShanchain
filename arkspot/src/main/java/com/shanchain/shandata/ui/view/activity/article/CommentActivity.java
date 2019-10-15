package com.shanchain.shandata.ui.view.activity.article;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CommetSecondListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.interfaces.IReplyCommentCallback;
import com.shanchain.shandata.ui.model.CommentEntity;
import com.shanchain.shandata.ui.presenter.CommentPresenter;
import com.shanchain.shandata.ui.presenter.impl.CommentPresenterImpl;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.ui.view.activity.article.view.CommentView;
import com.shanchain.shandata.utils.EmojiUtils;
import com.shanchain.shandata.utils.TimeUtils;
import com.shanchain.shandata.widgets.CustomListView;
import com.shanchain.shandata.widgets.SoftKeyboardStateHelper;
import com.shanchain.shandata.widgets.SwipeFinishLayout;
import com.shanchain.shandata.widgets.TextEditTextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.imui.view.CircleImageView;

/**
 * Created by WealChen
 * Date : 2019/9/20
 * Describe :二级评论界面 */
public class CommentActivity extends BaseActivity implements CommentView/*, View.OnLayoutChangeListener*/ {
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
    ListView clContentList;
    @Bind(R.id.et_content_input)
    TextEditTextView etContentInput;
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
    private int enterType = 0;
    private CommentEntity currentComment;//点中的当前要回复的评论
    private int keyHeight = 0;
    private SoftKeyboardStateHelper mSoftKeyboardStateHelper;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_comment;
    }

    @Override
    protected void initViewsAndEvents() {
        mSoftKeyboardStateHelper = new SoftKeyboardStateHelper(findViewById(R.id.ll_rootview));
        commentEntity = (CommentEntity) getIntent().getSerializableExtra("info");
        mCommentPresenter = new CommentPresenterImpl(this);
        mListAdapter = new CommetSecondListAdapter(this);
        initData();
        keyHeight = mScreenHeight/3;
    }
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        setSlideFinishFlags(SwipeFinishLayout.FLAG_SCROLL_DOWN_FINISH);
        commentEntity = (CommentEntity) getIntent().getSerializableExtra("info");
        mCommentPresenter = new CommentPresenterImpl(this);
        mListAdapter = new CommetSecondListAdapter(this);
        initData();
    }*/

    private void initData(){
        if(commentEntity==null)return;
        Glide.with(this).load(commentEntity.getSendHeadIcon())
                .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                        .error(R.drawable.aurora_headicon_default)).into(ivUserHead1);
        tvNickname.setText(commentEntity.getSendNickName());
        tvTime.setText(TimeUtils.friendlyTime1(this,new Date(commentEntity.getCreateTime())));
        tvComment.setText(EmojiUtils.utf8ToString(commentEntity.getContent()));
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
                currentComment = commentEntity;
//                etContentInput.setHint("回复: "+commentEntity.getSendUserId());
                etContentInput.setFocusable(true);
                etContentInput.setFocusableInTouchMode(true);
                etContentInput.requestFocus();
                hideKeyboard(etContentInput,true);
                enterType = 1;
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

        etContentInput.setOnKeyBoardHideListener(new TextEditTextView.OnKeyBoardHideListener() {
            @Override
            public void onKeyHide(int keyCode, KeyEvent event) {
//                Toast.makeText(CommentActivity.this, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();
            }
        });

       /* mSoftKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                Toast.makeText(CommentActivity.this, "监听到软件盘打开...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSoftKeyboardClosed() {
                Toast.makeText(CommentActivity.this, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    //发表评论
    @OnClick(R.id.tv_send)
    void addComment(){
        String comment = etContentInput.getText().toString().trim();

        if(TextUtils.isEmpty(comment)){
            ToastUtils.showToast(CommentActivity.this, getResources().getString(R.string.enter_comment));
            return;
        }
        comment = EmojiUtils.stringToUtf8(comment);
        if(enterType ==0){
            mCommentPresenter.addCommentUser(Integer.parseInt(userId),commentEntity.getSendUserId(),
                    commentEntity.getInvitationId(),commentEntity.getId(),commentEntity.getId(),comment);
        }else {
            mCommentPresenter.addCommentUser(Integer.parseInt(userId),currentComment.getSendUserId(),
                    commentEntity.getInvitationId(),commentEntity.getId(),currentComment.getId(),comment);
        }

    }


    @OnClick(R.id.im_back)
    void finished(){
        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.out_to_down);
    }

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
                tvNotdata.setVisibility(View.VISIBLE);*/
                tvReplyNum.setText(getString(R.string.reply_nums,"0"));
                return;
            }
            mList.clear();
            mList = JSONObject.parseArray(listdata,CommentEntity.class);
            if(mList!=null && mList.size()>0){
                /*clContentList.setVisibility(View.VISIBLE);
                tvNotdata.setVisibility(View.GONE);*/
                mListAdapter.setList(mList);
                clContentList.setAdapter(mListAdapter);
                mListAdapter.notifyDataSetChanged();
                tvReplyNum.setText(getString(R.string.reply_nums,mList.size()+""));
            }else {
                /*clContentList.setVisibility(View.GONE);
                llNotdata.setVisibility(View.VISIBLE);*/
                tvReplyNum.setText(getString(R.string.reply_nums,"0"));
            }

        }
    }

    @Override
    public void addCommentResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            etContentInput.setText("");
            etContentInput.setHint(getString(R.string.write_y_op));
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

    /*@Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){

            Toast.makeText(this, "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();

        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)) {

            Toast.makeText(this, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();
        }
    }*/

    /*@Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }*/

}
