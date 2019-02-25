package com.shanchain.shandata.ui.view.activity.story;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RNPagesConstant;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.eventbus.SCBaseEvent;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.DynamicCommentAdapter;
import com.shanchain.shandata.adapter.StoryItemNineAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.event.DynamicCommentEvent;
import com.shanchain.shandata.event.ReleaseSucEvent;
import com.shanchain.shandata.ui.model.BdCommentBean;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.CommentBean;
import com.shanchain.shandata.ui.model.DynamicModel;
import com.shanchain.shandata.ui.model.RNDetailExt;
import com.shanchain.shandata.ui.model.RNGDataBean;
import com.shanchain.shandata.ui.model.ReleaseContentInfo;
import com.shanchain.shandata.ui.model.SpanBean;
import com.shanchain.shandata.ui.model.StoryDetailInfo;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.presenter.DynamicDetailsPresenter;
import com.shanchain.shandata.ui.presenter.impl.DynamicDetailsPresenterImpl;
import com.shanchain.shandata.ui.view.activity.mine.FriendHomeActivity;
import com.shanchain.shandata.ui.view.activity.story.stroyView.DynamicDetailView;
import com.shanchain.shandata.utils.ClickableSpanNoUnderline;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.shandata.utils.KeyboardUtils;
import com.shanchain.shandata.utils.SCLinkMovementMethod;
import com.shanchain.shandata.widgets.dialog.CommentDialog;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
//import me.shaohui.shareutil.share.ShareListener;
//import me.shaohui.shareutil.share.SharePlatform;

public class DynamicDetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, View.OnClickListener, DynamicCommentAdapter.IonSlidingViewClickListener, BaseQuickAdapter.RequestLoadMoreListener, DynamicDetailView {

    @Bind(R.id.tb_dynamic_comment)
    ArthurToolBar mTbAddRole;
    @Bind(R.id.rv_dynamic_comment)
    RecyclerView mRvDynamicComment;
    @Bind(R.id.tv_dynamic_details_comment)
    TextView mTvDynamicDetailsComment;
    @Bind(R.id.ll_dynamic_details)
    LinearLayout mLlDynamicDetails;
    private List<BdCommentBean> datas = new ArrayList<>();
    private DynamicCommentAdapter mDynamicCommentAdapter;
    private View mHeadView;
    private StoryModelBean mBean;
    private String mStoryId, mCommentId;
    private int mCharacterId;
    private boolean isBeFav;
    private DynamicModel mDynamicModel;
    private CharacterInfo mCharacterInfo;
    private TextView mTvHeadLike;
    private TextView mTvHeadComment;
    private int page = 0;
    private int size = 10;
    private DynamicDetailsPresenter mPresenter;
    private boolean isLoadMore = false;
    private int mSpaceId;
    private boolean onece;
    private DynamicCommentAdapter.IonSlidingViewClickListener mIonSlidingViewClickListener;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_dynamic_details;
    }

    @Override
    protected void initViewsAndEvents() {
        mBean = (StoryModelBean) getIntent().getSerializableExtra("story");
        String rnExtra = getIntent().getStringExtra(NavigatorModule.REACT_EXTRA);
        if (mBean == null) {
            if (!TextUtils.isEmpty(rnExtra)) {
                JSONObject jsonObject = JSONObject.parseObject(rnExtra);
                JSONObject rnGData = jsonObject.getJSONObject("gData");
                JSONObject rnData = jsonObject.getJSONObject("data");
                mDynamicModel = JSON.parseObject(rnData.getJSONObject("novel").toJSONString(), DynamicModel.class);
                mCharacterInfo = JSON.parseObject(rnData.getJSONObject("character").toJSONString(), CharacterInfo.class);
                mStoryId = mDynamicModel.getStoryId() + "";
                mCharacterId = mDynamicModel.getCharacterId();
                mSpaceId = mDynamicModel.getSpaceId();
            } else {
                finish();
                return;
            }

        } else {
            mCharacterId = mBean.getCharacterId();
            mStoryId = mBean.getDetailId().substring(1);
            mDynamicModel = mBean.getDynamicModel();
            mCharacterInfo = mBean.getCharacterInfo();
            mSpaceId = mBean.getSpaceId();
            isBeFav = mBean.isBeFav();
        }

        initToolBar();
        initData();
        initHeadView();
        initRecyclerView();
    }

    private void initData() {
        mPresenter = new DynamicDetailsPresenterImpl(this);

        mPresenter.initData(page, size, mStoryId);

    }

    private void initRecyclerView() {
//        initDeleteButton();
        mRvDynamicComment.setLayoutManager(new LinearLayoutManager(this));
        mDynamicCommentAdapter = new DynamicCommentAdapter(R.layout.item_dynamic_comment, datas, this);
        mRvDynamicComment.addItemDecoration(new RecyclerViewDivider(this));
        mDynamicCommentAdapter.setEnableLoadMore(true);
        mRvDynamicComment.setAdapter(mDynamicCommentAdapter);
        mDynamicCommentAdapter.setHeaderView(mHeadView);
        mDynamicCommentAdapter.setOnLoadMoreListener(this, mRvDynamicComment);
        mDynamicCommentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_item_comment_like:
                        BdCommentBean bdCommentBean = mDynamicCommentAdapter.getData().get(position);
                        int commentId = bdCommentBean.getCommentBean().getCommentId();
                        boolean mySupport = bdCommentBean.getCommentBean().isMySupport();
                        if (mySupport) {
                            mPresenter.supportCancelComment(commentId, position);
                        } else {
                            mPresenter.supportComment(commentId, position);
                        }
                        int headerLayoutCount = mDynamicCommentAdapter.getHeaderLayoutCount();

                        TextView tvLike = (TextView) mDynamicCommentAdapter.getViewByPosition(position + headerLayoutCount, R.id.tv_item_comment_like);
                        tvLike.setEnabled(false);
                        break;
                    case R.id.iv_item_dynamic_comment_avatar:
                        BdCommentBean bean = mDynamicCommentAdapter.getData().get(position);
                        int characterId = bean.getCharacterId();
                        Intent intent = new Intent(mContext, FriendHomeActivity.class);
                        Intent intentFriend = intent.putExtra("characterId", characterId);
                        startActivity(intentFriend);
                        break;
                    default:

                        break;
                }
            }
        });

    }


    private void initHeadView() {
        mHeadView = View.inflate(this, R.layout.head_dynamic_comment, null);
        ImageView ivAvatar = (ImageView) mHeadView.findViewById(R.id.iv_item_story_avatar);
        ImageView ivMore = (ImageView) mHeadView.findViewById(R.id.iv_item_story_more);
        TextView tvName = (TextView) mHeadView.findViewById(R.id.tv_item_story_name);
        TextView tvTime = (TextView) mHeadView.findViewById(R.id.tv_item_story_time);
        TextView tvContent = (TextView) mHeadView.findViewById(R.id.tv_head_comment_content);

        NineGridImageView nineGridImageView = (NineGridImageView) mHeadView.findViewById(R.id.ngiv_item_story);
        TextView tvForwarding = (TextView) mHeadView.findViewById(R.id.tv_item_story_forwarding);
        mTvHeadLike = (TextView) mHeadView.findViewById(R.id.tv_item_story_like);
        mTvHeadComment = (TextView) mHeadView.findViewById(R.id.tv_item_story_comment);
        String characterImg = mCharacterInfo.getHeadImg();
        String characterName = mCharacterInfo.getName();

        GlideUtils.load(mContext, characterImg, ivAvatar, 0);
        tvName.setText(characterName);
        tvTime.setText(DateUtils.formatFriendly(new Date(mDynamicModel.getCreateTime())));
        tvForwarding.setText(mDynamicModel.getTranspond() + "");
        if (mBean == null) {
            //从rn转过来的页面暂时不可转发
            tvForwarding.setVisibility(View.INVISIBLE);
        }
        mTvHeadLike.setText(mDynamicModel.getSupportCount() + "");
        mTvHeadComment.setText(mDynamicModel.getCommendCount() + "");
        boolean isFav = isBeFav;
        Drawable like_def = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
        Drawable like_selected = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);

        like_def.setBounds(0, 0, like_def.getMinimumWidth(), like_def.getMinimumHeight());
        like_selected.setBounds(0, 0, like_selected.getMinimumWidth(), like_selected.getMinimumHeight());

        mTvHeadLike.setCompoundDrawables(isFav ? like_selected : like_def, null, null, null);
        mTvHeadLike.setCompoundDrawablePadding(DensityUtils.dip2px(this, 10));

        String intro = mDynamicModel.getIntro();
        String content = "";
        List<String> imgList = new ArrayList<>();
        List<SpanBean> spanBeanList = null;
        if (intro.contains("content")) {
            ReleaseContentInfo contentInfo = new Gson().fromJson(intro, ReleaseContentInfo.class);
            content = contentInfo.getContent();
            imgList = contentInfo.getImgs();
            spanBeanList = contentInfo.getSpanBeanList();
        } else {
            content = intro;
        }

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);

        if (spanBeanList != null) {
            for (int i = 0; i < spanBeanList.size(); i++) {
                ClickableSpanNoUnderline clickSpan = new ClickableSpanNoUnderline(Color.parseColor("#3bbac8"), new ClickableSpanNoUnderline.OnClickListener() {
                    @Override
                    public void onClick(View widget, ClickableSpanNoUnderline span) {
                        //ToastUtils.showToast(mContext, span.getClickData().getStr());
                        SpanBean clickData = span.getClickData();
                        if (clickData.getType() == Constants.SPAN_TYPE_AT) {
                            Bundle bundle = new Bundle();
                            RNDetailExt detailExt = new RNDetailExt();
                            RNGDataBean gDataBean = new RNGDataBean();
                            String uId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
                            String characterId = SCCacheUtils.getCache(uId, Constants.CACHE_CHARACTER_ID);
                            String spaceId = SCCacheUtils.getCache(uId, Constants.CACHE_SPACE_ID);
                            String token = SCCacheUtils.getCache(uId, Constants.CACHE_TOKEN);
                            gDataBean.setCharacterId(characterId);
                            gDataBean.setSpaceId(spaceId);
                            gDataBean.setToken(token);
                            gDataBean.setUserId(uId);
                            detailExt.setgData(gDataBean);
                            detailExt.setModelId(clickData.getBeanId() + "");
                            String json = JSONObject.toJSONString(detailExt);
                            bundle.putString(NavigatorModule.REACT_PROPS, json);
                            NavigatorModule.startReactPage(mContext, RNPagesConstant.RoleDetailScreen, bundle);
                        } else if (clickData.getType() == Constants.SPAN_TYPE_TOPIC) {
                            Intent intent = new Intent(mContext, TopicDetailsActivity.class);
                            intent.putExtra("from", 1);
                            intent.putExtra("topicId", clickData.getBeanId() + "");
                            startActivity(intent);
                        }
                    }
                });
                String str = spanBeanList.get(i).getStr();
                if (spanBeanList.get(i).getType() == Constants.SPAN_TYPE_AT) {
                    String temp = "@" + str;
                    int indexAt = content.indexOf(temp);
                    if (indexAt == -1) {
                        return;
                    }
                    spannableStringBuilder.setSpan(clickSpan, indexAt, indexAt + temp.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                } else if (spanBeanList.get(i).getType() == Constants.SPAN_TYPE_TOPIC) {
                    String temp = "#" + str + "#";
                    int indexTopic = content.indexOf(temp);
                    if (indexTopic == -1) {
                        return;
                    }
                    spannableStringBuilder.setSpan(clickSpan, indexTopic, indexTopic + temp.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                clickSpan.setClickData(spanBeanList.get(i));
            }

        }
        tvContent.setMovementMethod(SCLinkMovementMethod.getInstance());
        tvContent.setText(spannableStringBuilder);

        if (imgList.size() == 0) {
            nineGridImageView.setVisibility(View.GONE);
        } else {
            nineGridImageView.setVisibility(View.VISIBLE);
            StoryItemNineAdapter adapter = new StoryItemNineAdapter();
            nineGridImageView.setAdapter(adapter);
            nineGridImageView.setImagesData(imgList);
        }

        ivAvatar.setOnClickListener(this);
        tvForwarding.setOnClickListener(this);
        mTvHeadComment.setOnClickListener(this);
        mTvHeadLike.setOnClickListener(this);
        ivMore.setVisibility(View.GONE);

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
        final CustomDialog customDialog = new CustomDialog(mActivity, true, 1.0, R.layout.dialog_share_report,
                new int[]{R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel, R.id.tv_dialog_share});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_report_dialog_report:
                        //举报
                        Intent reportIntent = new Intent(mActivity, ReportActivity.class);
                        reportIntent.putExtra("storyId", mStoryId);
                        reportIntent.putExtra("characterId", mCharacterId + "");
                        startActivity(reportIntent);
                        customDialog.dismiss();
                        break;
                    case R.id.tv_report_dialog_cancel:
                        //取消
                        customDialog.dismiss();
                        break;
                    case R.id.tv_dialog_share:
                        showShare();
//                        ToastUtils.showToast(mContext,"暂不支持分享");
                        customDialog.dismiss();
                        break;

                }
            }
        });
        customDialog.show();
    }

    private void showShare() {
        final CustomDialog shareDialog = new CustomDialog(mContext, true, 1.0, R.layout.dialog_share, new int[]{
                R.id.iv_share_weichat, R.id.iv_share_circle, R.id.iv_share_qq,
                R.id.iv_share_qzone, R.id.iv_share_weibo, R.id.tv_dialog_share_cancel
        });

        shareDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {

                //obtainShareInfo(view);

                switch (view.getId()) {
                    case R.id.iv_share_weichat:
//                        postShareEvent(SharePlatform.WX);
                        // ShareUtil.shareMedia(mContext, SharePlatform.WX, "千千世界", "在这里，可以体验即兴表演的乐趣，快来加入吧！", "http://www.qianqianshijie.com", "http://p0.so.qhimgs1.com/t0143a3d87b8a734f6a.jpg", new SCShareListener());
                        break;
                    case R.id.iv_share_circle:
//                        postShareEvent(SharePlatform.WX_TIMELINE);
                        //ShareUtil.shareMedia(mContext, SharePlatform.WX_TIMELINE, "千千世界", "在这里，可以体验即兴表演的乐趣，快来加入吧", "http://www.qianqianshijie.com", "http://p0.so.qhimgs1.com/t0143a3d87b8a734f6a.jpg", new SCShareListener());
                        break;
                    case R.id.iv_share_qq:
//                        postShareEvent(SharePlatform.QQ);
                        //ShareUtil.shareMedia(mContext, SharePlatform.QQ, "千千世界", "在这里，可以体验即兴表演的乐趣，快来加入吧", "http://www.qianqianshijie.com", "http://p0.so.qhimgs1.com/t0143a3d87b8a734f6a.jpg", new SCShareListener());
                        break;
                    case R.id.iv_share_qzone:
//                        postShareEvent(SharePlatform.QZONE);
                        //ShareUtil.shareMedia(mContext, SharePlatform.QZONE, "千千世界", "在这里，可以体验即兴表演的乐趣，快来加入吧", "http://www.qianqianshijie.com", "http://p0.so.qhimgs1.com/t0143a3d87b8a734f6a.jpg", new SCShareListener());
                        break;
                    case R.id.iv_share_weibo:
//                        postShareEvent(SharePlatform.WEIBO);
                        //ShareUtil.shareMedia(mContext, SharePlatform.WEIBO, "千千世界", "在这里，可以体验即兴表演的乐趣，快来加入吧", "http://www.qianqianshijie.com", "http://p0.so.qhimgs1.com/t0143a3d87b8a734f6a.jpg", new SCShareListener());
                        break;
                    case R.id.tv_dialog_share_cancel:
                        shareDialog.dismiss();
                        break;
                }
            }
        });

        shareDialog.show();

    }

    private void postShareEvent(int shareType) {
        String storyId = mStoryId;
//        JSONObject obj = new JSONObject();
//        obj.put("shareType",shareType);
//        obj.put("id",storyId);
//        obj.put("type",Constants.SHARE_ID_TYPE_STORY);
        ReleaseSucEvent releaseSucEvent = new ReleaseSucEvent(true);
        EventBus.getDefault().post(new SCBaseEvent(EventConstant.EVENT_MODULE_ARKSPOT, EventConstant.EVENT_KEY_SHARE_WEB, releaseSucEvent, null));
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onDeleteBtnCilck(View view, final int position) {
        final CustomDialog customDialog = new CustomDialog(this, false, 1.0, R.layout.common_dialog_comment_delete, new int[]{R.id.tv_dialog_delete_cancel, R.id.tv_dialog_delete_sure});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_dialog_delete_cancel:
                        customDialog.dismiss();
                        break;
                    case R.id.tv_dialog_delete_sure:
                        CommentBean commentBean = mDynamicCommentAdapter.getData().get(position).getCommentBean();
                        mPresenter.deleteComment(String.valueOf(commentBean.getCommentId()), position);
                        mDynamicCommentAdapter.notifyDataSetChanged();
                        mDynamicCommentAdapter.closeMenu();
                        customDialog.dismiss();
                        break;
                }
            }
        });
        customDialog.show();

    }


//    private class SCShareListener extends ShareListener {
//
//        @Override
//        public void shareSuccess() {
//            LogUtils.i("分享成功");
//        }
//
//        @Override
//        public void shareFailure(Exception e) {
//            LogUtils.i("分享失败");
//            e.printStackTrace();
//        }
//
//        @Override
//        public void shareCancel() {
//            LogUtils.i("分享取消");
//        }
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_item_story_avatar:
                int characterId = mCharacterInfo.getCharacterId();
                Intent intentAvatar = new Intent(mContext, FriendHomeActivity.class);
                intentAvatar.putExtra("characterId", characterId);
                startActivity(intentAvatar);
                break;
            case R.id.tv_item_story_forwarding:
                int spaceId = mCharacterInfo.getSpaceId();
                String cacheSpaceId = SCCacheUtils.getCacheSpaceId();

                if (TextUtils.equals(cacheSpaceId, spaceId + "")) {
                    Intent intent = new Intent(mActivity, ForwardingActivity.class);
                    intent.putExtra("forward", mBean);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(mContext, "不同世界不能进行转发操作");
                }
                break;
            case R.id.tv_item_story_comment:

                String comSpaceId = SCCacheUtils.getCacheSpaceId();
                if (TextUtils.equals(comSpaceId, mSpaceId + "")) {
                    showPop();
                } else {
                    ToastUtils.showToast(mContext, "不同世界不能进行评论");
                }

                break;
            case R.id.tv_item_story_like:
                clickLike();
                break;
        }
    }

    /**
     * 描述：点赞当前故事
     */
    private void clickLike() {
        boolean fav = isBeFav;
        mTvHeadLike.setEnabled(false);
        if (fav) {
            //已经点赞
            mPresenter.supportCancel(mStoryId);
        } else {
            //未点赞
            mPresenter.support(mStoryId);
        }
    }

    @OnClick(R.id.tv_dynamic_details_comment)
    public void onClick() {
        String comSpaceId = SCCacheUtils.getCacheSpaceId();
        if (TextUtils.equals(comSpaceId, mSpaceId + "")) {
            showPop();
        } else {
            ToastUtils.showToast(mContext, "不同世界不能进行评论");
        }

    }

    private void showPop() {
        FragmentManager manager = getSupportFragmentManager();
        CommentDialog dialog = new CommentDialog();
        dialog.show(manager, "tag");
        dialog.setOnSendClickListener(new CommentDialog.OnSendClickListener() {
            @Override
            public void onSendClick(View v, String msg) {
                mPresenter.addComment(msg, mStoryId);
                mDynamicCommentAdapter.notifyDataSetChanged();
            }
        });
        KeyboardUtils.showSoftInput(this);
    }


    /**
     * 描述：加载更多评论信息
     */
    @Override
    public void onLoadMoreRequested() {
        LogUtils.i("load more ......");
        page++;
        isLoadMore = true;
        initData();
    }

    @Override
    public void commentSuccess(List<BdCommentBean> list, boolean isLast) {
        if (list == null) {
            if (isLast) {
                mDynamicCommentAdapter.loadMoreEnd();
            } else {
                mDynamicCommentAdapter.loadMoreFail();
            }
        } else {
            if (isLoadMore) {
                if (isLast) {
                    mDynamicCommentAdapter.loadMoreEnd();
                } else {
                    mDynamicCommentAdapter.loadMoreComplete();
                }
                mDynamicCommentAdapter.addData(list);
            } else {
                mDynamicCommentAdapter.setNewData(list);
                mDynamicCommentAdapter.disableLoadMoreIfNotFullPage(mRvDynamicComment);
            }
            mDynamicCommentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addSuccess(boolean success) {
        if (success) {
            //添加评论成功
            page = 0;
            isLoadMore = false;
            mPresenter.initData(page, size, mStoryId);
            EventBus.getDefault().postSticky(new DynamicCommentEvent(true));
        } else {
            //添加评论失败

        }

    }

    @Override
    public void deleteSuccess(boolean success, int position) {

        if (success) {
            //删除评论
            String storyId = mStoryId;
            page = 0;
            isLoadMore = false;
            mPresenter.initData(page, size, mStoryId);
//            initRecyclerView();
            EventBus.getDefault().postSticky(new DynamicCommentEvent(false));

        } else {

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DynamicCommentEvent event) {
        mTvHeadComment.setText(event.getCommentCount() + "");
    }

    @Override
    public void supportSuc(boolean suc) {
        mTvHeadLike.setEnabled(true);
        if (suc) {
            isBeFav = true;
            int supportCount = mDynamicModel.getSupportCount();
            Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvHeadLike.setCompoundDrawables(drawable, null, null, null);
            mTvHeadLike.setCompoundDrawablePadding(DensityUtils.dip2px(mActivity, 10));
            mTvHeadLike.setText(supportCount + 1 + "");
            mDynamicModel.setSupportCount(supportCount + 1);
        } else {

        }
    }

    @Override
    public void supportCancelSuc(boolean suc) {
        mTvHeadLike.setEnabled(true);
        if (suc) {
            isBeFav = false;
            int supportCount = mDynamicModel.getSupportCount();
            Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvHeadLike.setCompoundDrawables(drawable, null, null, null);
            mTvHeadLike.setCompoundDrawablePadding(DensityUtils.dip2px(mActivity, 10));
            mTvHeadLike.setText(supportCount - 1 + "");
            mDynamicModel.setSupportCount(supportCount - 1);
        } else {

        }
    }

    @Override
    public void initNovelSuc(StoryDetailInfo storyDetailInfo) {

    }

    @Override
    public void commentSupportSuc(boolean suc, int position) {
        if (suc) {
            CommentBean commentBean = mDynamicCommentAdapter.getData().get(position).getCommentBean();
            int supportCount = commentBean.getSupportCount();
            commentBean.setMySupport(true);
            int headerLayoutCount = mDynamicCommentAdapter.getHeaderLayoutCount();
            TextView tvLike = (TextView) mDynamicCommentAdapter.getViewByPosition(position + headerLayoutCount, R.id.tv_item_comment_like);
            tvLike.setEnabled(true);
            Drawable drawable = getResources().getDrawable(R.mipmap.abs_dynamic_btn_like_selected);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(null, null, drawable, null);
            tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mContext, 5));
            tvLike.setText(supportCount + 1 + "");
            commentBean.setSupportCount(supportCount + 1);
        } else {

        }
    }

    @Override
    public void commentSupportCancelSuc(boolean suc, int position) {
        if (suc) {
            CommentBean commentBean = mDynamicCommentAdapter.getData().get(position).getCommentBean();
            int supportCount = commentBean.getSupportCount();
            commentBean.setMySupport(false);
            int headerLayoutCount = mDynamicCommentAdapter.getHeaderLayoutCount();
            TextView tvLike = (TextView) mDynamicCommentAdapter.getViewByPosition(position + headerLayoutCount, R.id.tv_item_comment_like);
            tvLike.setEnabled(true);
            Drawable drawable = getResources().getDrawable(R.mipmap.abs_dynamic_btn_like_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(null, null, drawable, null);
            tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mContext, 5));
            if (supportCount - 1 <= 0) {
                tvLike.setText("0");
                commentBean.setSupportCount(0);
            } else {
                tvLike.setText(supportCount - 1 + "");
                commentBean.setSupportCount(supportCount - 1);
            }

        } else {

        }
    }
}
