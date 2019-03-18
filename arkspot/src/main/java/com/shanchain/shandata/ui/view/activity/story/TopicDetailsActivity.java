package com.shanchain.shandata.ui.view.activity.story;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CurrentAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.RNDataBean;
import com.shanchain.shandata.ui.model.RNDetailExt;
import com.shanchain.shandata.ui.model.ResponseTopicContentBean;
import com.shanchain.shandata.ui.model.StoryBeanModel;
import com.shanchain.shandata.ui.model.StoryInfo;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.presenter.TopicDetailPresenter;
import com.shanchain.shandata.ui.presenter.impl.TopicDetailPresenterImpl;
import com.shanchain.shandata.ui.view.activity.mine.FriendHomeActivity;
import com.shanchain.shandata.ui.view.activity.story.stroyView.TopicDetailView;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class TopicDetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, TopicDetailView, BaseQuickAdapter.RequestLoadMoreListener {


    @Bind(R.id.tb_topic_details)
    ArthurToolBar mTbTopicDetails;
    @Bind(R.id.rv_topic_details)
    RecyclerView mRvTopicDetails;
    private List<StoryBeanModel> datas = new ArrayList<>();
    private CurrentAdapter mAdapter;
    private View mHeadView;
    private String mTopicId;
    private TopicDetailPresenter mDetailPresenter;
    private int page = 0;
    private int size = 10;
    private boolean isFirstLoad = true;
    private ImageView mIvHeadTopicImg;
    private TextView mTvHeadTopic;
    private TextView mTvHeadTopicDiscussRead;
    private TextView mTvHeadTopicDes;

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
            mTopicId = data.getTopicId() + "";
        } else if (from == 1) {   //主页点击过来
            mTopicId= intent.getStringExtra("topicId");
        }

        if(TextUtils.isEmpty(mTopicId)){
            LogUtils.i("话题id为空  finish掉");
            return;
        }

        mDetailPresenter = new TopicDetailPresenterImpl(this);
        initToolBar();
        initRecyclerView();
        initData();
    }

    private void initRecyclerView() {
        initHeadView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvTopicDetails.setLayoutManager(layoutManager);
        mAdapter = new CurrentAdapter(datas);
        mAdapter.setEnableLoadMore(true);
        mRvTopicDetails.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this, 5),
                getResources().getColor(R.color.colorAccent)));
        mRvTopicDetails.setAdapter(mAdapter);
        mAdapter.setHeaderView(mHeadView);
        mAdapter.setOnLoadMoreListener(this, mRvTopicDetails);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                StoryModelBean bean = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean();
                int type = bean.getType();
                if (type == StoryInfo.type1) {
                    Intent intent = new Intent(TopicDetailsActivity.this, DynamicDetailsActivity.class);
                    intent.putExtra("story", bean);
                    startActivity(intent);
                } else if (type == StoryInfo.type2) {
                    Intent intent = new Intent(TopicDetailsActivity.this, NovelDetailsActivity.class);
                    intent.putExtra("story", bean);
                    startActivity(intent);
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
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
        StoryModelBean bean = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean();
        String storyId = bean.getDetailId();
        boolean beFav = bean.isBeFav();
        if (beFav) {     //已经点赞
            mDetailPresenter.storyCancelSupport(position, storyId.substring(1));
        } else {     //未点赞
            mDetailPresenter.storySupport(position, storyId.substring(1));
        }

    }

    private void comment(int position) {
        StoryBeanModel beanModel = mAdapter.getData().get(position);
        StoryModelBean bean = beanModel.getStoryModel().getModelInfo().getBean();
        int itemType = beanModel.getItemType();
        if (itemType == StoryInfo.type1) {   //普通动态
            Intent intent = new Intent(mContext, DynamicDetailsActivity.class);
            intent.putExtra("story", bean);
            startActivity(intent);
        } else if (itemType == StoryInfo.type2) { //小说
            Intent intentType2 = new Intent(mContext, NovelDetailsActivity.class);
            intentType2.putExtra("story", bean);
            startActivity(intentType2);
        }
    }

    private void forwarding(int position) {

        StoryModelBean bean = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean();
        int spaceId = bean.getSpaceId();
        String cacheSpaceId = SCCacheUtils.getCacheSpaceId();
        if (TextUtils.equals(cacheSpaceId, "" + spaceId)) {
            Intent intent = new Intent(mContext, ForwardingActivity.class);
            intent.putExtra("forward", bean);
            startActivity(intent);
        } else {
            ToastUtils.showToast(mContext, "不同世界不能进行转发操作");
        }

    }

    private void avatarClick(int position) {
        Intent intent = new Intent(mContext, FriendHomeActivity.class);
        int characterId = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean().getCharacterId();
        intent.putExtra("characterId", characterId);
        startActivity(intent);
    }

    private void initHeadView() {
        mHeadView = View.inflate(this, R.layout.head_topic_details, null);
        mIvHeadTopicImg = (ImageView) mHeadView.findViewById(R.id.iv_head_topic_img);
        mTvHeadTopic = (TextView) mHeadView.findViewById(R.id.tv_head_topic_topic);
        mTvHeadTopicDiscussRead = (TextView) mHeadView.findViewById(R.id.tv_head_topic_discuss_read);
        mTvHeadTopicDes = (TextView) mHeadView.findViewById(R.id.tv_head_topic_des);

    }

    private void report(final int position) {
        final CustomDialog customDialog = new CustomDialog(mActivity, true, 1.0, R.layout.dialog_shielding_report,
                new int[]{R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_report_dialog_report:
                        //举报
                        Intent reportIntent = new Intent(mActivity, ReportActivity.class);
                        String storyId = mAdapter.getData().get(position).getStoryModel().getModelInfo().getStoryId();
                        int characterId = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean().getCharacterId();
                        reportIntent.putExtra("storyId", storyId);
                        reportIntent.putExtra("characterId", "" + characterId);
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
        String topicId = "";

        if (mTopicId.startsWith("t")){
            topicId = mTopicId.substring(1);
        }else {
            topicId = mTopicId;
        }
        mDetailPresenter.initTopicInfo(topicId);
        mDetailPresenter.initStoryInfo(topicId, page, size);
    }


    private void initToolBar() {
        mTbTopicDetails.setBtnEnabled(true, false);
        mTbTopicDetails.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void initTopicInfo(ResponseTopicContentBean topicInfo) {
        if (topicInfo == null){
            return;
        }
        String background = topicInfo.getBackground();
        String title = topicInfo.getTitle();
        String intro = topicInfo.getIntro();
        int readNum = topicInfo.getReadNum();
        int discussNum = topicInfo.getStoryNum();
        GlideUtils.load(mContext, background, mIvHeadTopicImg, 0);
        mTvHeadTopic.setText("#" + title + "#");
        mTvHeadTopicDes.setText(intro);
        mTvHeadTopicDiscussRead.setText(discussNum + "讨论" + "  " + readNum + "阅读");
    }

    @Override
    public void initSuccess(List<StoryBeanModel> list, boolean isLast) {

        if (list == null) {
            if (isLast) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreFail();
            }
        } else {
            if (!isFirstLoad) {
                mAdapter.addData(list);
            } else {
                mAdapter.setNewData(list);
                mAdapter.disableLoadMoreIfNotFullPage(mRvTopicDetails);
            }
            mAdapter.notifyDataSetChanged();
            if (isLast) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreComplete();
            }

        }
        isFirstLoad = false;
    }

    @Override
    public void supportSuccess(boolean suc, int position) {
        if (suc) {
            StoryModelBean bean = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean();
            int supportCount = bean.getSupportCount();
            bean.setBeFav(true);
            int headerLayoutCount = mAdapter.getHeaderLayoutCount();
            TextView tvLike = (TextView) mAdapter.getViewByPosition(position + headerLayoutCount, R.id.tv_item_story_like);
            Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
            tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mActivity, 10));
            tvLike.setText(supportCount + 1 + "");
            bean.setSupportCount(supportCount + 1);
        } else {

        }
    }

    @Override
    public void supportCancelSuccess(boolean suc, int position) {
        if (suc) {
            StoryModelBean bean = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean();
            int supportCount = bean.getSupportCount();
            bean.setBeFav(false);
            int headerLayoutCount = mAdapter.getHeaderLayoutCount();
            TextView tvLike = (TextView) mAdapter.getViewByPosition(position + headerLayoutCount, R.id.tv_item_story_like);
            Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
            tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mActivity, 10));
            tvLike.setText(supportCount - 1 + "");
            bean.setSupportCount(supportCount - 1);
        } else {

        }
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        String topicId = "";

        if (mTopicId.startsWith("t")){
            topicId = mTopicId.substring(1);
        }else {
            topicId = mTopicId;
        }

        mDetailPresenter.loadMore(topicId, page, size);
    }
}
