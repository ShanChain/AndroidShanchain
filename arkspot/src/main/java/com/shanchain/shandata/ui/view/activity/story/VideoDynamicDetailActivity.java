package com.shanchain.shandata.ui.view.activity.story;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.DynamicCommentAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.BdCommentBean;
import com.shanchain.shandata.ui.model.DynamicVideoBean;
import com.shanchain.shandata.ui.presenter.DynamicVideoPresenter;
import com.shanchain.shandata.ui.presenter.impl.DynamicVideoPresenterImpl;
import com.shanchain.shandata.ui.view.activity.story.stroyView.DynamicVideoView;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import de.hdodenhof.circleimageview.CircleImageView;

public class VideoDynamicDetailActivity extends BaseActivity implements DynamicVideoView, ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {


    @Bind(R.id.tb_dynamic_video)
    ArthurToolBar mTbDynamicVideo;
    @Bind(R.id.rv_video_dynamic)
    RecyclerView mRvVideoDynamic;
    private View mHeadView;
    private TextView mTvTitle;
    private TextView mTvContent;
    private JZVideoPlayerStandard mPlayer;
    private CircleImageView mAvatar;
    private ImageView mIvMore;
    private TextView mTvName;
    private TextView mTvTime;
    private TextView mTvFrom;
    private TextView mTvForward;
    private TextView mTvComment;
    private TextView mTvLike;
    private List<BdCommentBean> datas = new ArrayList<>();
    private DynamicCommentAdapter mAdapter;
    private DynamicVideoPresenter mPresenter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_video_dynamic_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        initToolBar();
        initRecyclerView();
        initData();
    }

    private void initData() {
        String id = "9";
        mPresenter = new DynamicVideoPresenterImpl(this);
        mPresenter.initData(id);
    }

    private void initRecyclerView() {
        initHeadView();
        mRvVideoDynamic.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DynamicCommentAdapter(R.layout.item_dynamic_comment, datas);
        mRvVideoDynamic.addItemDecoration(new RecyclerViewDivider(this));
        mAdapter.setEnableLoadMore(true);
        mRvVideoDynamic.setAdapter(mAdapter);
        mAdapter.setHeaderView(mHeadView);
        mAdapter.setOnLoadMoreListener(this, mRvVideoDynamic);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    private void initHeadView() {
        mHeadView = View.inflate(mContext, R.layout.head_dynamic_video, null);
        mTvTitle = (TextView) mHeadView.findViewById(R.id.tv_head_video_title);
        mTvContent = (TextView) mHeadView.findViewById(R.id.tv_head_video_content);
        mPlayer = (JZVideoPlayerStandard) mHeadView.findViewById(R.id.player_video_dynamic);
        mAvatar = (CircleImageView) mHeadView.findViewById(R.id.iv_item_story_avatar);
        mIvMore = (ImageView) mHeadView.findViewById(R.id.iv_item_story_more);
        mTvName = (TextView) mHeadView.findViewById(R.id.tv_item_story_name);
        mTvTime = (TextView) mHeadView.findViewById(R.id.tv_item_story_time);
        mTvFrom = (TextView) mHeadView.findViewById(R.id.tv_item_story_from);
        mTvForward = (TextView) mHeadView.findViewById(R.id.tv_item_story_forwarding);
        mTvComment = (TextView) mHeadView.findViewById(R.id.tv_item_story_comment);
        mTvLike = (TextView) mHeadView.findViewById(R.id.tv_item_story_like);
        mTvForward.setVisibility(View.GONE);
        mIvMore.setVisibility(View.GONE);
        mTvLike.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
        mTvComment.setOnClickListener(this);
        mPlayer.backButton.setVisibility(View.GONE);
        mPlayer.batteryTimeLayout.setVisibility(View.GONE);
    }

    private void initToolBar() {
        mTbDynamicVideo.setOnLeftClickListener(this);
        mTbDynamicVideo.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void initVideoSuc(DynamicVideoBean videoBean) {
        if (videoBean == null){
            return;
        }
        mTvTitle.setText(videoBean.getTitle());
        mTvContent.setText(videoBean.getIntro());
        mPlayer.setUp(videoBean.getUrl(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
        GlideUtils.load(mContext, videoBean.getBackground(), mPlayer.thumbImageView, 0);
        String time = DateUtils.formatFriendly(new Date(videoBean.getCreateTime()));
        mTvTime.setText(time);
        int characterId = videoBean.getCharacterId();
        mPresenter.getCharacterInfo(characterId);
    }

    @Override
    public void initFavSuc(boolean isFav) {
        Drawable like_def = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
        Drawable like_selected = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);
        like_def.setBounds(0, 0, like_def.getMinimumWidth(), like_def.getMinimumHeight());
        like_selected.setBounds(0, 0, like_selected.getMinimumWidth(), like_selected.getMinimumHeight());
        mTvLike.setCompoundDrawables(isFav ? like_selected : like_def, null, null, null);
        mTvLike.setCompoundDrawablePadding(DensityUtils.dip2px(this, 10));
    }

    @Override
    public void initCharacterSuc(String headImg, String name) {
        if (TextUtils.isEmpty(headImg)||TextUtils.isEmpty(name)){
            return;
        }

        GlideUtils.load(mContext, headImg, mAvatar, 0);
        mTvName.setText(name);
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_item_story_avatar:

                break;
            case R.id.tv_item_story_like:

                break;
            case R.id.tv_item_story_comment:

                break;
            default:

                break;
        }
    }
}
