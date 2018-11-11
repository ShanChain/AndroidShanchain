package com.shanchain.shandata.ui.view.activity.tasklist;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.utils.KeyboardUtils;
import com.shanchain.shandata.widgets.dialog.CommentDialog;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class TaskDetailActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener, ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_task_comment)
    ArthurToolBar tbTaskComment;
    @Bind(R.id.rv_task_comment)
    RecyclerView rvTaskComment;
    @Bind(R.id.tv_task_details_comment)
    TextView tvTaskDetailsComment;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_task_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TaskDetailActivity.this,LinearLayoutManager.VERTICAL,false);
        rvTaskComment.setLayoutManager(linearLayoutManager);

    }

    private void initData() {

    }

    private void initToolBar() {
        tbTaskComment.isShowChatRoom(false);//不在导航栏显示聊天室信息
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tbTaskComment.getTitleView().setLayoutParams(layoutParams);
        tbTaskComment.setBackgroundColor(getResources().getColor(R.color.colorHomeBtn));
    }


    @OnClick(R.id.tv_task_details_comment)
    public void onClick() {
        showPop();
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }

    private void showPop() {
        FragmentManager manager = getSupportFragmentManager();
        CommentDialog dialog = new CommentDialog();
        dialog.show(manager, "tag");
        dialog.setOnSendClickListener(new CommentDialog.OnSendClickListener() {
            @Override
            public void onSendClick(View v, String msg) {
//                mPresenter.addComment(msg, mStoryId);
//                mDynamicCommentAdapter.notifyDataSetChanged();
            }
        });
        KeyboardUtils.showSoftInput(this);
    }
}
