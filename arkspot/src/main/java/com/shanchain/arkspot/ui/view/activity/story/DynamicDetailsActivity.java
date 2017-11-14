package com.shanchain.arkspot.ui.view.activity.story;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.DynamicCommentAdapter;
import com.shanchain.arkspot.adapter.StoryItemNineAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.BdCommentBean;
import com.shanchain.arkspot.ui.model.ReleaseContentInfo;
import com.shanchain.arkspot.ui.model.StoryBeanModel;
import com.shanchain.arkspot.ui.model.StoryModelBean;
import com.shanchain.arkspot.ui.presenter.DynamicDetailsPresenter;
import com.shanchain.arkspot.ui.presenter.impl.DynamicDetailsPresenterImpl;
import com.shanchain.arkspot.ui.view.activity.mine.FriendHomeActivity;
import com.shanchain.arkspot.ui.view.activity.story.stroyView.DynamicDetailView;
import com.shanchain.arkspot.utils.DateUtils;
import com.shanchain.arkspot.widgets.dialog.CustomDialog;
import com.shanchain.arkspot.widgets.other.RecyclerViewDivider;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class DynamicDetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener, DynamicDetailView {

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
    private StoryBeanModel mBeanModel;
    private StoryModelBean mBean;
    private String mStoryId;
    private int mCharacterId;
    private TextView mTvHeadLike;
    private TextView mTvHeadComment;
    private int page = 0;
    private int size = 10;
    private DynamicDetailsPresenter mPresenter;
    private boolean isLoadMore = false;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_dynamic_details;
    }

    @Override
    protected void initViewsAndEvents() {

        mBeanModel = (StoryBeanModel) getIntent().getSerializableExtra("story");
        if (mBeanModel == null) {
            finish();
            return;
        } else {
            mBean = mBeanModel.getStoryModel().getModelInfo().getBean();
            mStoryId = mBeanModel.getStoryModel().getModelInfo().getStoryId();
            mCharacterId = mBean.getCharacterId();
        }
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initData() {
        mPresenter = new DynamicDetailsPresenterImpl(this);
        String storyId = mBean.getDetailId().substring(1);
        mPresenter.initData(page,size,storyId);

    }

    private void initRecyclerView() {
        initHeadView();
        mRvDynamicComment.setLayoutManager(new LinearLayoutManager(this));
        mDynamicCommentAdapter = new DynamicCommentAdapter(R.layout.item_dynamic_comment, datas);
        mRvDynamicComment.addItemDecoration(new RecyclerViewDivider(this));
        mDynamicCommentAdapter.setEnableLoadMore(true);
        mRvDynamicComment.setAdapter(mDynamicCommentAdapter);
        mDynamicCommentAdapter.setHeaderView(mHeadView);
        mDynamicCommentAdapter.setOnLoadMoreListener(this,mRvDynamicComment);
        mDynamicCommentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_item_comment_like:
                        mDynamicCommentAdapter.notifyDataSetChanged();
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
        String characterImg = mBeanModel.getStoryModel().getModelInfo().getBean().getCharacterImg();
        String characterName = mBeanModel.getStoryModel().getModelInfo().getBean().getCharacterName();

        GlideUtils.load(mContext, characterImg, ivAvatar, 0);
        tvName.setText(characterName);
        tvTime.setText(DateUtils.formatFriendly(new Date(mBean.getCreateTime())));
        tvForwarding.setText(mBean.getTranspond() + "");
        mTvHeadLike.setText(mBean.getSupportCount() + "");
        mTvHeadComment.setText(mBean.getCommendCount() + "");
        boolean isFav = mBean.isBeFav();
        Drawable like_def = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
        Drawable like_selected = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);

        like_def.setBounds(0, 0, like_def.getMinimumWidth(), like_def.getMinimumHeight());
        like_selected.setBounds(0, 0, like_selected.getMinimumWidth(), like_selected.getMinimumHeight());

        mTvHeadLike.setCompoundDrawables(isFav ? like_selected : like_def, null, null, null);
        mTvHeadLike.setCompoundDrawablePadding(DensityUtils.dip2px(this, 10));

        String intro = mBean.getIntro();
        String content = "";
        List<String> imgList = new ArrayList<>();
        if (intro.contains("content")) {
            ReleaseContentInfo contentInfo = new Gson().fromJson(intro, ReleaseContentInfo.class);
            content = contentInfo.getContent();
            imgList = contentInfo.getImgs();
        } else {
            content = intro;
        }

        tvContent.setText(content);
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
        final CustomDialog customDialog = new CustomDialog(mActivity, true, 1.0, R.layout.dialog_shielding_report,
                new int[]{ R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_report_dialog_report:
                        //举报
                        Intent reportIntent = new Intent(mActivity, ReportActivity.class);
                        reportIntent.putExtra("storyId",mStoryId);
                        reportIntent.putExtra("characterId",mCharacterId+"");
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_item_story_avatar:
                int characterId = mBean.getCharacterId();
                Intent intentAvatar = new Intent(mContext, FriendHomeActivity.class);
                intentAvatar.putExtra("characterId", characterId);
                startActivity(intentAvatar);
                break;
            case R.id.tv_item_story_forwarding:

                break;
            case R.id.tv_item_story_comment:
                showPop();
                popupInputMethodWindow();
                break;
            case R.id.tv_item_story_like:
                clickLike();
                break;
        }
    }

    /**
     *  描述：点赞当前故事
     */
    private void clickLike() {
        boolean fav = mBean.isBeFav();
        if (fav){
            //已经点赞
            cancelSupport();
        }else {
            //未点赞
            support();
        }
    }

    private void cancelSupport() {
        String storyId = mBean.getDetailId().substring(1);
        mPresenter.supportCancel(storyId);

    }

    private void support(){
        String storyId = mBean.getDetailId().substring(1);
        mPresenter.support(storyId);
    }

    @OnClick(R.id.tv_dynamic_details_comment)
    public void onClick() {
        showPop();
        popupInputMethodWindow();
    }

    private void showPop() {
        View contentView = View.inflate(this, R.layout.pop_comment, null);
        TextView mTvPopCommentOutside = (TextView) contentView.findViewById(R.id.tv_pop_comment_outside);
        final EditText mEtPopComment = (EditText) contentView.findViewById(R.id.et_pop_comment);
        TextView mTvPopCommentSend = (TextView) contentView.findViewById(R.id.tv_pop_comment_send);
        final PopupWindow pop = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        mTvPopCommentOutside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        mTvPopCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = mEtPopComment.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    ToastUtils.showToast(DynamicDetailsActivity.this, "不能提交空评论哦~");
                    return;
                }
                addComment(comment);
                pop.dismiss();
            }
        });
        pop.setTouchable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPopBg)));
        pop.showAtLocation(mLlDynamicDetails, 0, 0, Gravity.BOTTOM);
    }

    private void addComment(String comment) {
        String storyId = mBean.getDetailId().substring(1);
        mPresenter.addComment(comment,storyId);

    }

    private void popupInputMethodWindow() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    /**
     *  描述：加载更多评论信息
     */
    @Override
    public void onLoadMoreRequested() {
        LogUtils.i("load more ......");
        page ++;
        isLoadMore = true;
        initData();
    }

    @Override
    public void commentSuccess(List<BdCommentBean> list, boolean isLast) {
        if (list == null){
            if (isLast){
                mDynamicCommentAdapter.loadMoreEnd();
            }else {
                mDynamicCommentAdapter.loadMoreFail();
            }
        }else {
            if (isLoadMore){
                if (isLast){
                    mDynamicCommentAdapter.loadMoreEnd();
                }else {
                    mDynamicCommentAdapter.loadMoreComplete();
                }
                mDynamicCommentAdapter.addData(list);
            }else {
                mDynamicCommentAdapter.setNewData(list);
                mDynamicCommentAdapter.disableLoadMoreIfNotFullPage(mRvDynamicComment);
            }
            mDynamicCommentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addSuccess(boolean success) {
        if (success){
            //添加评论成功
            String storyId = mBean.getDetailId().substring(1);
            page = 0;
            isLoadMore = false;
            mPresenter.initData(page,size,storyId);
        }else {
            //添加评论失败

        }

    }

    @Override
    public void supportSuc(boolean suc) {
        if (suc){
            mBean.setBeFav(true);
            int supportCount = mBean.getSupportCount();
            Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvHeadLike.setCompoundDrawables(drawable, null, null, null);
            mTvHeadLike.setCompoundDrawablePadding(DensityUtils.dip2px(mActivity, 10));
            mTvHeadLike.setText(supportCount + 1 + "");
            mBean.setSupportCount(supportCount + 1);
        }else {

        }
    }

    @Override
    public void supportCancelSuc(boolean suc) {
        if (suc){
            mBean.setBeFav(false);
            int supportCount = mBean.getSupportCount();
            Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvHeadLike.setCompoundDrawables(drawable, null, null, null);
            mTvHeadLike.setCompoundDrawablePadding(DensityUtils.dip2px(mActivity, 10));
            mTvHeadLike.setText(supportCount - 1 + "");
            mBean.setSupportCount(supportCount - 1);
        }else {

        }
    }
}
