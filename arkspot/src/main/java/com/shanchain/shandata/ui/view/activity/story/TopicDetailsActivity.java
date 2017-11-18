package com.shanchain.shandata.ui.view.activity.story;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TopicDetailsAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.RNDataBean;
import com.shanchain.shandata.ui.model.RNDetailExt;
import com.shanchain.shandata.ui.model.StoryBeanModel;
import com.shanchain.shandata.ui.model.StoryInfo;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.presenter.TopicDetailPresenter;
import com.shanchain.shandata.ui.presenter.impl.TopicDetailPresenterImpl;
import com.shanchain.shandata.ui.view.activity.story.stroyView.TopicDetailView;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class TopicDetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, TopicDetailView {


    @Bind(R.id.tb_topic_details)
    ArthurToolBar mTbTopicDetails;
    @Bind(R.id.rv_topic_details)
    RecyclerView mRvTopicDetails;
    private List<StoryInfo> datas = new ArrayList<>();
    private TopicDetailsAdapter mDetailsAdapter;
    private View mHeadView;
    private String mTopicId;
    private TopicDetailPresenter mDetailPresenter;
    private int mDiscussNum;
    private int mReadNum;
    private String mBackground;
    private String mIntro;
    private String mTitle;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_topic_details;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        int from = intent.getIntExtra("from", 0);
        if (from == 0) {     //RN页面传值
            String rnExtra = intent.getStringExtra(NavigatorModule.REACT_EXTRA);
            RNDetailExt rnDetailExt = JSONObject.parseObject(rnExtra, RNDetailExt.class);
            RNDataBean data = rnDetailExt.getData();
            mDiscussNum = data.getStoryNum();
            mTopicId = data.getTopicId() + "";
            mReadNum = data.getReadNum();
            mBackground = data.getBackground();
            mIntro = data.getIntro();
            mTitle = data.getTitle();
        } else if (from == 1) {   //主页点击过来
            StoryBeanModel beanModelTopic = (StoryBeanModel) intent.getSerializableExtra("topic");
            StoryModelBean storyModelBean = beanModelTopic.getStoryModel().getModelInfo().getBean();
            mTopicId = storyModelBean.getDetailId();
            mDiscussNum = storyModelBean.getCommendCount();
            mReadNum = storyModelBean.getSupportCount();
            mBackground = storyModelBean.getBackground();
            mIntro = storyModelBean.getIntro();
            mTitle = storyModelBean.getTitle();

        }
        mDetailPresenter = new TopicDetailPresenterImpl(this);
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        initHeadView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvTopicDetails.setLayoutManager(layoutManager);
        mDetailsAdapter = new TopicDetailsAdapter(R.layout.item_story_type3, datas);
        mRvTopicDetails.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this, 5),
                getResources().getColor(R.color.colorAccent)));
        mRvTopicDetails.setAdapter(mDetailsAdapter);
        mDetailsAdapter.setHeaderView(mHeadView);

        mDetailsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(TopicDetailsActivity.this, DynamicDetailsActivity.class);
                startActivity(intent);
            }
        });

        mDetailsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_item_story_avatar:
                        //头像
                        avatarClick(position);
                        break;
                    case R.id.tv_item_story_forwarding:
                        //转发
                        forwarding(position);
                        break;
                    case R.id.tv_item_story_comment:
                        //评论
                        comment(position);
                        break;
                    case R.id.tv_item_story_like:
                        //喜欢
                        like(position);
                        break;
                    case R.id.iv_item_story_more:
                        //举报
                        report(position);
                        break;
                }
            }
        });

    }

    private void like(int position) {
        ToastUtils.showToast(TopicDetailsActivity.this, "喜欢");
    }

    private void comment(int position) {
        ToastUtils.showToast(TopicDetailsActivity.this, "评论");
    }

    private void forwarding(int position) {
        ToastUtils.showToast(TopicDetailsActivity.this, "转发");
    }

    private void avatarClick(int position) {
        ToastUtils.showToast(TopicDetailsActivity.this, "头像");
    }

    private void initHeadView() {
        mHeadView = View.inflate(this, R.layout.head_topic_details, null);
        ImageView ivHeadTopicImg = (ImageView) mHeadView.findViewById(R.id.iv_head_topic_img);
        TextView tvHeadTopic = (TextView) mHeadView.findViewById(R.id.tv_head_topic_topic);
        TextView tvHeadTopicDiscussRead = (TextView) mHeadView.findViewById(R.id.tv_head_topic_discuss_read);
        TextView tvHeadTopicDes = (TextView) mHeadView.findViewById(R.id.tv_head_topic_des);

        //设置数据
        GlideUtils.load(mContext, mBackground, ivHeadTopicImg, 0);
        tvHeadTopic.setText("#" + mTitle + "#");
        tvHeadTopicDes.setText(mIntro);
        tvHeadTopicDiscussRead.setText(mDiscussNum + "讨论" + "  " + mReadNum + "阅读");

    }

    private void report(final int position) {
        final CustomDialog customDialog = new CustomDialog(mActivity, true, 1.0, R.layout.dialog_shielding_report,
                new int[]{ R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_report_dialog_report:
                        //举报
                        Intent reportIntent = new Intent(mActivity, ReportActivity.class);
                        reportIntent.putExtra("storyId",datas.get(position).getStoryListDataBean().getStoryId()+"");
                        reportIntent.putExtra("characterId",datas.get(position).getStoryListDataBean().getInfo().getCharacterId()+"");
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

    private void initData() {

        datas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            StoryInfo storyInfo = new StoryInfo();
            storyInfo.setTime(i * 12 + "");
            datas.add(storyInfo);
        }
    }


    private void initToolBar() {
        mTbTopicDetails.setBtnEnabled(true, false);
        mTbTopicDetails.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
