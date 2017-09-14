package com.shanchain.arkspot.ui.view.activity.story;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.TopicDetailsAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.StoryInfo;
import com.shanchain.arkspot.widgets.dialog.CustomDialog;
import com.shanchain.arkspot.widgets.other.RecyclerViewDivider;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import utils.DensityUtils;
import utils.ToastUtils;

public class TopicDetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    @Bind(R.id.tb_topic_details)
    ArthurToolBar mTbTopicDetails;
    @Bind(R.id.rv_topic_details)
    RecyclerView mRvTopicDetails;
    private List<StoryInfo> datas;
    private TopicDetailsAdapter mDetailsAdapter;
    private View mHeadView;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_topic_details;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        initHeadView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvTopicDetails.setLayoutManager(layoutManager);
        mDetailsAdapter = new TopicDetailsAdapter(R.layout.item_story_type3,datas);
        mRvTopicDetails.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this,5),
                getResources().getColor(R.color.colorAccent)));
        mRvTopicDetails.setAdapter(mDetailsAdapter);
        mDetailsAdapter.setHeaderView(mHeadView);

        mDetailsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(TopicDetailsActivity.this,DynamicDetailsActivity.class);
                startActivity(intent);
            }
        });

        mDetailsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_item_story_avatar:
                        //头像
                        ToastUtils.showToast(TopicDetailsActivity.this,"头像");
                        break;
                    case R.id.tv_item_story_forwarding:
                        //转发
                        ToastUtils.showToast(TopicDetailsActivity.this,"转发");
                        break;
                    case R.id.tv_item_story_comment:
                        //评论
                        ToastUtils.showToast(TopicDetailsActivity.this,"评论");
                        break;
                    case R.id.tv_item_story_like:
                        //喜欢
                        ToastUtils.showToast(TopicDetailsActivity.this,"喜欢");
                        break;
                    case R.id.iv_item_story_more:
                        //举报
                        report();
                        break;
                }
            }
        });

    }

    private void initHeadView() {
        mHeadView = View.inflate(this, R.layout.head_topic_details,null);
        ImageView ivHeadTopicImg = (ImageView) mHeadView.findViewById(R.id.iv_head_topic_img);
        TextView tvHeadTopic = (TextView) mHeadView.findViewById(R.id.tv_head_topic_topic);
        TextView tvHeadTopicDiscussRead = (TextView) mHeadView.findViewById(R.id.tv_head_topic_discuss_read);
        TextView tvHeadTopicDes = (TextView) mHeadView.findViewById(R.id.tv_head_topic_des);

        //设置数据


    }

    private void report() {
        final CustomDialog customDialog = new CustomDialog(mActivity, true, 1.0, R.layout.dialog_shielding_report,
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

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 4; i ++) {
            StoryInfo storyInfo = new StoryInfo();
            storyInfo.setTime(i * 12 +"");
            datas.add(storyInfo);
        }
    }

    private void initToolBar() {
        mTbTopicDetails.setBtnEnabled(true,false);
        mTbTopicDetails.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
