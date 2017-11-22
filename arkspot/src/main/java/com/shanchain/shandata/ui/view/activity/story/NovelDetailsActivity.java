package com.shanchain.shandata.ui.view.activity.story;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;

import com.shanchain.shandata.ui.model.StoryDetailInfo;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.DynamicCommentAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.BdCommentBean;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.NovelModel;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.presenter.impl.DynamicDetailsPresenterImpl;
import com.shanchain.shandata.ui.view.activity.mine.FriendHomeActivity;
import com.shanchain.shandata.ui.view.activity.story.stroyView.DynamicDetailView;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class NovelDetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, View.OnClickListener, DynamicDetailView, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tb_dynamic_comment)
    ArthurToolBar mTbAddRole;
    @Bind(R.id.rv_dynamic_comment)
    RecyclerView mRvDynamicComment;
    @Bind(R.id.tv_dynamic_details_comment)
    TextView mTvDynamicDetailsComment;
    @Bind(R.id.ll_dynamic_details)
    LinearLayout mLlDynamicDetails;
    private DynamicCommentAdapter mDynamicCommentAdapter;
    private List<BdCommentBean> datas = new ArrayList<>();
    private View mHeadView;
    private String mStoryId;
    private String mCharacterId;
    private NovelModel mNovelModel;
    private CharacterInfo mCharacterInfo;
    private boolean isBeFav = false;
    private DynamicDetailsPresenterImpl mPresenter;
    private int page = 0;
    private int size = 10;
    private boolean isLoadMore = false;
    private TextView mTvHeadLike;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_novel_details;
    }

    @Override
    protected void initViewsAndEvents() {

        StoryModelBean bean = (StoryModelBean) getIntent().getSerializableExtra("story");
        String rnExtra = getIntent().getStringExtra(NavigatorModule.REACT_EXTRA);
        if (bean == null) {
            if (!TextUtils.isEmpty(rnExtra)) {
                JSONObject jsonObject = JSONObject.parseObject(rnExtra);
                JSONObject rnGData = jsonObject.getJSONObject("gData");
                JSONObject rnData = jsonObject.getJSONObject("data");
                mNovelModel = JSON.parseObject(rnData.getJSONObject("novel").toJSONString(), NovelModel.class);
                mCharacterInfo = JSON.parseObject(rnData.getJSONObject("character").toJSONString(), CharacterInfo.class);
                mStoryId = mNovelModel.getStoryId() + "";
                mCharacterId = mNovelModel.getCharacterId() + "";
            } else {
                finish();
                return;
            }

        } else {
            mStoryId = bean.getDetailId().substring(1);
            mCharacterId = bean.getCharacterId() + "";
            mNovelModel = bean.getNovelMovel();
            mCharacterInfo = bean.getCharacterInfo();
        }

        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initData() {
        mPresenter = new DynamicDetailsPresenterImpl(this);
        mPresenter.initNovelInfo(mStoryId);
        mPresenter.initData(page,size,mStoryId);
    }

    private void initRecyclerView() {
        initHeadView();
        mRvDynamicComment.setLayoutManager(new LinearLayoutManager(this));
        mDynamicCommentAdapter = new DynamicCommentAdapter(R.layout.item_dynamic_comment, datas);
        mRvDynamicComment.addItemDecoration(new RecyclerViewDivider(this));
        mDynamicCommentAdapter.setEnableLoadMore(true);
        mRvDynamicComment.setAdapter(mDynamicCommentAdapter);
        mDynamicCommentAdapter.setOnLoadMoreListener(this,mRvDynamicComment);
        mDynamicCommentAdapter.setHeaderView(mHeadView);
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

        mHeadView = View.inflate(this, R.layout.head_novel_comment, null);
        ImageView ivAvatar = (ImageView) mHeadView.findViewById(R.id.iv_item_story_avatar);
        ImageView ivMore = (ImageView) mHeadView.findViewById(R.id.iv_item_story_more);
        TextView tvName = (TextView) mHeadView.findViewById(R.id.tv_item_story_name);
        TextView tvTime = (TextView) mHeadView.findViewById(R.id.tv_item_story_time);
        TextView tvContent = (TextView) mHeadView.findViewById(R.id.tv_head_comment_content);
        TextView tvTitle = (TextView) mHeadView.findViewById(R.id.tv_head_comment_title);
        TextView tvForwarding = (TextView) mHeadView.findViewById(R.id.tv_item_story_forwarding);
        mTvHeadLike = (TextView) mHeadView.findViewById(R.id.tv_item_story_like);
        TextView tvHeadComment = (TextView) mHeadView.findViewById(R.id.tv_item_story_comment);
        tvForwarding.setVisibility(View.GONE);
        String characterImg = "";
        String characterName = "";
        if (mCharacterInfo != null) {
            characterImg = mCharacterInfo.getHeadImg();
            characterName = mCharacterInfo.getName();
        }

        GlideUtils.load(mContext, characterImg, ivAvatar, 0);
        tvName.setText(characterName);
        tvTime.setText(DateUtils.formatFriendly(new Date(mNovelModel.getCreateTime())));
        mTvHeadLike.setText(mNovelModel.getSupportCount() + "");
        tvHeadComment.setText(mNovelModel.getCommendCount() + "");

        Drawable like_def = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
        Drawable like_selected = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);

        like_def.setBounds(0, 0, like_def.getMinimumWidth(), like_def.getMinimumHeight());
        like_selected.setBounds(0, 0, like_selected.getMinimumWidth(), like_selected.getMinimumHeight());
        tvTitle.setText(mNovelModel.getTitle());
        mTvHeadLike.setCompoundDrawables(isBeFav ? like_selected : like_def, null, null, null);
        mTvHeadLike.setCompoundDrawablePadding(DensityUtils.dip2px(this, 10));

        String intro = mNovelModel.getIntro();

        String replace = intro.replace(mNovelModel.getTitle() + "\n", "");

        tvContent.setText(replace);


        ivAvatar.setOnClickListener(this);
        tvForwarding.setOnClickListener(this);
        tvHeadComment.setOnClickListener(this);
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
        Intent intent = new Intent(mContext, ReadModelActivity.class);
        intent.putExtra("storyId", mStoryId);
        intent.putExtra("native",false);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_item_story_avatar:
                int characterId = mNovelModel.getCharacterId();
                Intent intentAvatar = new Intent(mContext, FriendHomeActivity.class);
                intentAvatar.putExtra("characterId", characterId);
                startActivity(intentAvatar);
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

    private void clickLike() {
        boolean fav = true;
        if (fav){
            //已经点赞
            mPresenter.supportCancel(mStoryId);
        }else {
            //未点赞
            mPresenter.support(mStoryId);
        }
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
                    ToastUtils.showToast(NovelDetailsActivity.this, "不能提交空评论哦~");
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
        mPresenter.addComment(comment,mStoryId);
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
            page = 0;
            isLoadMore = false;
            mPresenter.initData(page,size,mStoryId);
        }else {
            //添加评论失败

        }
    }

    @Override
    public void supportSuc(boolean suc) {

    }

    @Override
    public void supportCancelSuc(boolean suc) {

    }

    @Override
    public void initNovelSuc(StoryDetailInfo storyDetailInfo) {

    }

    @Override
    public void onLoadMoreRequested() {
        LogUtils.i("load more ......");
        page ++;
        isLoadMore = true;
        mPresenter.initData(page,size,mStoryId);
    }
}
