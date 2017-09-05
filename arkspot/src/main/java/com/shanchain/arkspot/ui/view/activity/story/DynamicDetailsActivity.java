package com.shanchain.arkspot.ui.view.activity.story;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.DynamicCommentAdapter;
import com.shanchain.arkspot.adapter.StoryItemNineAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.CommentInfo;
import com.shanchain.arkspot.widgets.dialog.CustomDialog;
import com.shanchain.arkspot.widgets.other.RecyclerViewDivider;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import utils.ToastUtils;

public class DynamicDetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, View.OnClickListener {

    @Bind(R.id.tb_dynamic_comment)
    ArthurToolBar mTbAddRole;
    @Bind(R.id.rv_dynamic_comment)
    RecyclerView mRvDynamicComment;
    private List<CommentInfo> datas;
    private DynamicCommentAdapter mDynamicCommentAdapter;
    private View mHeadView;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_dynamic_details;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initData() {
        datas = new ArrayList<>();

        for (int i = 0; i < 28; i ++) {
            CommentInfo commentInfo = new CommentInfo();
            if (i%3==0) {
                commentInfo.setComment("重要的不是这件事情的真相，而是你对这件事情的态度 而已重要的不是这件事情的真相，，而是你对这件事情的态度 而已重要的不是这件事情的真相，而是你对这件事情的态度 而已");
            }else {
                commentInfo.setComment("重要的不是这件事情的真相你对这件事情的态度....");
            }
            commentInfo.setCounts(i*2);
            commentInfo.setLike(i%3==0?true:false);
            commentInfo.setTime(i + "分钟前");
            datas.add(commentInfo);
        }

    }

    private void initRecyclerView() {
        initHeadView();
        mRvDynamicComment.setLayoutManager(new LinearLayoutManager(this));
        mDynamicCommentAdapter = new DynamicCommentAdapter(R.layout.item_dynamic_comment,datas);
        mRvDynamicComment.addItemDecoration(new RecyclerViewDivider(this));
        mRvDynamicComment.setAdapter(mDynamicCommentAdapter);
        mDynamicCommentAdapter.setHeaderView(mHeadView);
        mDynamicCommentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_item_comment_like:
                        boolean like = datas.get(position).isLike();
                        datas.get(position).setLike(!like);
                        mDynamicCommentAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }

    private void initHeadView() {
        mHeadView = View.inflate(this, R.layout.head_dynamic_comment,null);
        ImageView ivAvatar = (ImageView) mHeadView.findViewById(R.id.iv_item_story_avatar);
        ImageView ivMore = (ImageView) mHeadView.findViewById(R.id.iv_item_story_more);
        TextView tvName = (TextView) mHeadView.findViewById(R.id.tv_item_story_name);
        TextView tvTime = (TextView) mHeadView.findViewById(R.id.tv_item_story_time);
        TextView tvFrom = (TextView) mHeadView.findViewById(R.id.tv_item_story_from);
        TextView tvContent = (TextView) mHeadView.findViewById(R.id.tv_head_comment_content);
        TextView tvExpend = (TextView) mHeadView.findViewById(R.id.tv_head_comment_expend);

        NineGridImageView nineGridImageView = (NineGridImageView) mHeadView.findViewById(R.id.ngiv_item_story);
        TextView tvForwarding = (TextView) mHeadView.findViewById(R.id.tv_item_story_forwarding);
        TextView tvHeadLike = (TextView) mHeadView.findViewById(R.id.tv_item_story_like);
        TextView tvHeadComment = (TextView) mHeadView.findViewById(R.id.tv_item_story_comment);

        StoryItemNineAdapter ngivAdapter = new StoryItemNineAdapter();
        nineGridImageView.setAdapter(ngivAdapter);
        List<Integer> imgs = new ArrayList();
        imgs.add(R.drawable.photo_city);
        imgs.add(R.drawable.photo_bear);
        imgs.add(R.drawable.photo_yue);
        nineGridImageView.setImagesData(imgs);

        ivAvatar.setOnClickListener(this);
        tvForwarding.setOnClickListener(this);
        tvHeadComment.setOnClickListener(this);
        tvHeadLike.setOnClickListener(this);
        ivMore.setVisibility(View.GONE);

        //根据是否是长文来控制是否显示展开，是长文显示，不是则不显示
        tvExpend.setVisibility(View.GONE);
    }

    private void initToolBar() {
        mTbAddRole.setOnLeftClickListener(this);
        mTbAddRole.setOnRightClickListener(this);
    }


    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        report();
    }

    private void report() {
        final CustomDialog customDialog = new CustomDialog(mActivity, true, 1.0, R.layout.dialog_report,
                new int[]{R.id.tv_report_dialog_shielding, R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_report_dialog_shielding:
                        //屏蔽
                        showShieldingDialog();
                        customDialog.dismiss();
                        break;
                    case R.id.tv_report_dialog_report:
                        //举报
                        Intent reportIntent = new Intent(mActivity, ReportActivity.class);
                        startActivity(reportIntent);
                        customDialog.dismiss();
                        break;
                    case R.id.tv_report_dialog_cancel:
                        //取消
                        customDialog.dismiss();
                        break;
                }
            }
        });
        customDialog.show();
    }

    private void showShieldingDialog() {
        final CustomDialog shieldingDialog = new CustomDialog(mActivity, false, 1, R.layout.dialog_shielding, new int[]{R.id.tv_shielding_dialog_cancel, R.id.tv_shielding_dialog_sure});
        shieldingDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_shielding_dialog_cancel:
                        shieldingDialog.dismiss();
                        break;
                    case R.id.tv_shielding_dialog_sure:
                        //确定屏蔽，请求接口

                        shieldingDialog.dismiss();
                        break;
                }
            }
        });
        shieldingDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_item_story_avatar:
                ToastUtils.showToast(this,"头像");
                break;
            case R.id.tv_item_story_forwarding:
                ToastUtils.showToast(this,"转发");
                break;
            case R.id.tv_item_story_comment:
                ToastUtils.showToast(this,"评论");
                break;
            case R.id.tv_item_story_like:
                ToastUtils.showToast(this,"喜欢");
                break;
        }
    }
}