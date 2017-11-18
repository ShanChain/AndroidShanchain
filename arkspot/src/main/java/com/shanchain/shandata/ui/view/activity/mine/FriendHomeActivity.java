package com.shanchain.shandata.ui.view.activity.mine;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CurrentAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.StoryBeanModel;
import com.shanchain.shandata.ui.model.StoryInfo;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.presenter.FriendHomePresenter;
import com.shanchain.shandata.ui.presenter.impl.FriendHomePresenterImpl;
import com.shanchain.shandata.ui.view.activity.chat.ChatRoomActivity;
import com.shanchain.shandata.ui.view.activity.mine.view.FriendHomeView;
import com.shanchain.shandata.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.shandata.ui.view.activity.story.NovelDetailsActivity;
import com.shanchain.shandata.ui.view.activity.story.ReportActivity;
import com.shanchain.shandata.ui.view.activity.story.TopicDetailsActivity;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class FriendHomeActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, View.OnClickListener, FriendHomeView, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @Bind(R.id.tb_friend_home)
    ArthurToolBar mTbFriendHome;
    @Bind(R.id.rv_friend_home)
    RecyclerView mRvFriendHome;
    private View mHeadView;
    private List<StoryBeanModel> datas = new ArrayList<>();
    private CurrentAdapter mAdapter;
    private ImageView mIvBg;
    private ImageView mIvHead;
    private TextView mTvName;
    private Button mBtnFocus;
    private TextView mTvDes;
    private TextView mTvConversation;
    private int mCharacterId;
    private FriendHomePresenter mPresenter;
    private int page = 0;
    private int size = 10;
    private boolean isLoadMore = false;
    private LinearLayout mLlConversation;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_friend_home;
    }

    @Override
    protected void initViewsAndEvents() {
        mCharacterId = getIntent().getIntExtra("characterId", 0);

        String rnExtra = getIntent().getStringExtra(NavigatorModule.REACT_EXTRA);
        if(!getIntent().hasExtra("characterId")){
            JSONObject jsonObject = JSONObject.parseObject(rnExtra);
            JSONObject rnGData = jsonObject.getJSONObject("gData");
            JSONObject rnData = jsonObject.getJSONObject("data");
            mCharacterId = Integer.parseInt(rnData.getString("characterId"));
        }

        mPresenter = new FriendHomePresenterImpl(this);
        initToolBar();
        initData();
        initRecyclerView();

    }

    private void initData() {
        mPresenter.initCharacterInfo(mCharacterId);
        mPresenter.initStory(mCharacterId, page, size);
    }

    private void initRecyclerView() {
        initHeadView();
        mRvFriendHome.setLayoutManager(new LinearLayoutManager(this));
        mRvFriendHome.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(mContext, 5), getResources().getColor(R.color.colorDivider)));
        mAdapter = new CurrentAdapter(datas);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setHeaderView(mHeadView);
        mRvFriendHome.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this, mRvFriendHome);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(this).inflate(R.layout.head_friend_home, (ViewGroup) findViewById(android.R.id.content), false);
        mIvBg = (ImageView) mHeadView.findViewById(R.id.iv_friend_home_bg);
        mIvHead = (ImageView) mHeadView.findViewById(R.id.iv_friend_home_head);
        mTvName = (TextView) mHeadView.findViewById(R.id.tv_friend_home_name);
        mTvDes = (TextView) mHeadView.findViewById(R.id.tv_friend_home_des);
        mBtnFocus = (Button) mHeadView.findViewById(R.id.btn_friend_home_focus);
        mTvConversation = (TextView) mHeadView.findViewById(R.id.tv_friend_home_conversation);
        mLlConversation = (LinearLayout) mHeadView.findViewById(R.id.ll_conversation);
        mTvConversation.setOnClickListener(this);
        mBtnFocus.setOnClickListener(this);
    }

    private void initToolBar() {
        mTbFriendHome.setBtnEnabled(true, false);
        mTbFriendHome.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_friend_home_focus:
                //关注
                String btnTxt = mBtnFocus.getText().toString().trim();
                if (TextUtils.equals(btnTxt, "已关注")) {
                    mPresenter.focusCancel(mCharacterId);
                } else {
                    mPresenter.focus(mCharacterId);
                }
                break;
            case R.id.tv_friend_home_conversation:
                //发起对话
                mPresenter.obtainHxInfo(mCharacterId);
                break;
        }
    }


    @Override
    public void initFriendSuc(CharacterInfo friendInfo, boolean isFocus) {
        if (mHeadView != null && friendInfo != null) {
            int userId = friendInfo.getUserId();
            int spaceId = friendInfo.getSpaceId();
            String cacheUserId = SCCacheUtils.getCacheUserId();
            String cacheSpaceId = SCCacheUtils.getCacheSpaceId();

            if (TextUtils.equals(cacheUserId, userId + "")) {
                mBtnFocus.setVisibility(View.GONE);
                mLlConversation.setVisibility(View.GONE);
            } else {
                if (TextUtils.equals(cacheSpaceId, spaceId + "")) {
                    mBtnFocus.setVisibility(View.VISIBLE);
                    mLlConversation.setVisibility(View.VISIBLE);
                } else {
                    mBtnFocus.setVisibility(View.GONE);
                    mLlConversation.setVisibility(View.GONE);
                }
            }

            GlideUtils.load(mContext, friendInfo.getHeadImg(), mIvHead, 0);
            mTvName.setText(friendInfo.getName() + "(No." + friendInfo.getModelNo() + ")");
            mTvDes.setText(friendInfo.getSignature());
            mBtnFocus.setText(isFocus ? "已关注" : "关注TA");
        }
    }

    @Override
    public void focusSuc(boolean focusSuc) {
        if (focusSuc) {
            //关注成功
            mBtnFocus.setText("已关注");
        } else {
            ToastUtils.showToast(mContext, "关注失败");
        }
    }

    @Override
    public void focusCancelSuc(boolean suc) {
        if (suc) {
            mBtnFocus.setText("关注TA");
        } else {
            ToastUtils.showToast(mContext, "取消关注失败");
        }
    }

    @Override
    public void getStoryInfoSuc(List<StoryBeanModel> list, Boolean isLast) {
        if (list == null) {
            if (isLast) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreFail();
            }
        } else {
            if (isLoadMore) {
                mAdapter.addData(list);
            } else {
                mAdapter.setNewData(list);
                mAdapter.disableLoadMoreIfNotFullPage(mRvFriendHome);
            }
            mAdapter.notifyDataSetChanged();
            if (isLast) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreComplete();
            }
        }
    }

    @Override
    public void getHxSuc(String hxUserName) {
        if (TextUtils.isEmpty(hxUserName)) {
            ToastUtils.showToast(mContext, "当前用户异常");
        } else {
            Intent intent = new Intent(mContext, ChatRoomActivity.class);
            intent.putExtra("isGroup", false);
            intent.putExtra("toChatName", hxUserName);
            startActivity(intent);
        }
    }

    @Override
    public void supportSuccess(boolean suc, int position) {
        if (suc) {
            StoryModelBean bean = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean();
            int supportCount = bean.getSupportCount();
            bean.setBeFav(true);
            int headerLayoutCount = mAdapter.getHeaderLayoutCount();
            TextView tvLike = (TextView) mAdapter.getViewByPosition(position + headerLayoutCount, R.id.tv_item_story_like);
            Drawable drawable = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
            tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mContext, 10));
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
            Drawable drawable = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
            tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mContext, 10));
            tvLike.setText(supportCount - 1 + "");
            bean.setSupportCount(supportCount - 1);
        } else {

        }
    }


    @Override
    public void onLoadMoreRequested() {
        isLoadMore = true;
        page++;
        mPresenter.loadMore(mCharacterId, page, size);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        StoryBeanModel bean = mAdapter.getData().get(position);
        LogUtils.i("item  click = " + position + "; bean = " + bean);
        int itemType = bean.getItemType();
        switch (itemType) {
            case StoryInfo.type1:
                //类型1的条目点击事件 短故事
                Intent intentType1 = new Intent(mContext, DynamicDetailsActivity.class);
                StoryBeanModel beanModel = mAdapter.getData().get(position);
                StoryModelBean bean1 = beanModel.getStoryModel().getModelInfo().getBean();
                intentType1.putExtra("story", bean1);
                startActivity(intentType1);
                break;
            case StoryInfo.type2:
                //类型2的条目点击事件    长故事
                Intent intentType2 = new Intent(mContext, NovelDetailsActivity.class);
                StoryBeanModel beanModel2 = mAdapter.getData().get(position);
                StoryModelBean bean2 = beanModel2.getStoryModel().getModelInfo().getBean();
                intentType2.putExtra("story", bean2);
                startActivity(intentType2);
                break;
            case StoryInfo.type3:
                //类型3的条目点击事件    话题
                Intent intentType3 = new Intent(mContext, TopicDetailsActivity.class);
                intentType3.putExtra("from", 1);
                List<StoryBeanModel> data = mAdapter.getData();
                StoryBeanModel beanModelTopic = data.get(position);
                intentType3.putExtra("topic", beanModelTopic);
                startActivity(intentType3);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        LogUtils.i("点击的条目是  =  " + position);
        switch (view.getId()) {
            case R.id.iv_item_story_avatar:
                break;
            case R.id.iv_item_story_more:
                report(position);
                break;
            case R.id.tv_item_story_forwarding:
                clickForwarding(position);
                break;
            case R.id.tv_item_story_comment:
                clickComment(position);
                break;
            case R.id.tv_item_story_like:
                clickLike(position);
                break;
        }
    }

    private void clickLike(int position) {
        StoryModelBean bean = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean();
        String storyId = bean.getDetailId();
        boolean beFav = bean.isBeFav();
        if (beFav) {     //已经点赞
            mPresenter.storyCancelSupport(position, storyId.substring(1));
        } else {     //未点赞
            mPresenter.storySupport(position, storyId.substring(1));
        }
    }

    private void clickComment(int position) {
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

    private void clickForwarding(int position) {
        // TODO: 2017/11/14
    }

    private void report(final int position) {
        final CustomDialog customDialog = new CustomDialog(mContext, true, 1.0, R.layout.dialog_shielding_report,
                new int[]{R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_report_dialog_report:
                        //举报
                        Intent reportIntent = new Intent(mContext, ReportActivity.class);
                        String storyId = mAdapter.getData().get(position).getStoryModel().getModelInfo().getStoryId();
                        int characterId = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean().getCharacterId();
                        reportIntent.putExtra("storyId", storyId);
                        reportIntent.putExtra("characterId", characterId);
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

}
