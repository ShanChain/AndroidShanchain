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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.DynamicCommentAdapter;
import com.shanchain.arkspot.adapter.StoryItemNineAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.BdCommentBean;
import com.shanchain.arkspot.ui.model.CharacterInfo;
import com.shanchain.arkspot.ui.model.CommentBean;
import com.shanchain.arkspot.ui.model.CommentData;
import com.shanchain.arkspot.ui.model.ContactBean;
import com.shanchain.arkspot.ui.model.NovelModel;
import com.shanchain.arkspot.ui.model.ReleaseContentInfo;
import com.shanchain.arkspot.ui.model.ResponseCommentInfo;
import com.shanchain.arkspot.ui.model.ResponseContactInfo;
import com.shanchain.arkspot.ui.model.StoryBeanModel;
import com.shanchain.arkspot.ui.model.StoryModelBean;
import com.shanchain.arkspot.ui.view.activity.mine.FriendHomeActivity;
import com.shanchain.arkspot.utils.DateUtils;
import com.shanchain.arkspot.widgets.dialog.CustomDialog;
import com.shanchain.arkspot.widgets.other.RecyclerViewDivider;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

public class NovelDetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, View.OnClickListener {

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


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_novel_details;
    }

    @Override
    protected void initViewsAndEvents() {

        StoryBeanModel beanModel = (StoryBeanModel) getIntent().getSerializableExtra("story");
        String rnExtra = getIntent().getStringExtra(NavigatorModule.REACT_EXTRA);
        if (beanModel == null) {
            if(!TextUtils.isEmpty(rnExtra)){
                JSONObject jsonObject= JSONObject.parseObject(rnExtra);
                JSONObject rnGData = jsonObject.getJSONObject("gData");
                JSONObject rnData = jsonObject.getJSONObject("data");
                mNovelModel = JSON.parseObject(rnData.getJSONObject("novel").toJSONString(),NovelModel.class);
                mCharacterInfo = JSON.parseObject(rnData.getJSONObject("character").toJSONString(),CharacterInfo.class);
                mStoryId = mNovelModel.getStoryId() + "";
                mCharacterId = mNovelModel.getCharacterId() + "";
            }else {
                finish();
                return;
            }

        } else {
            StoryModelBean storyBean = beanModel.getStoryModel().getModelInfo().getBean();
            mStoryId = beanModel.getStoryModel().getModelInfo().getStoryId();
            mCharacterId = storyBean.getCharacterId() + "";
            mNovelModel = storyBean.getNovelMovel();
            mCharacterInfo = storyBean.getCharacterInfo();
        }

        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initData() {
        datas.clear();
        SCHttpUtils.postWithChaId()
                .url(HttpApi.COMMENT_QUERY)
                .addParams("storyId", mStoryId)
                .addParams("page", "0")
                .addParams("size", "100")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取评论列表失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取评论列表成功 = " + response);
                        if (TextUtils.isEmpty(response)) {

                            return;
                        }
                        ResponseCommentInfo responseCommentInfo = new Gson().fromJson(response, ResponseCommentInfo.class);
                        if (!TextUtils.equals(responseCommentInfo.getCode(), NetErrCode.COMMON_SUC_CODE)) {
                            return;
                        }
                        CommentData data = responseCommentInfo.getData();
                        List<CommentBean> commentBeanList = data.getContent();
                        List<Integer> characterIds = new ArrayList<>();
                        LogUtils.i("评论数量 = " + commentBeanList.size());
                        List<BdCommentBean> bdCommentBeanList = new ArrayList<>();
                        for (int i = 0; i < commentBeanList.size(); i++) {
                            BdCommentBean bdCommentBean = new BdCommentBean();
                            CommentBean commentBean = commentBeanList.get(i);
                            int characterId = commentBean.getCharacterId();
                            characterIds.add(characterId);
                            bdCommentBean.setCharacterId(characterId);
                            bdCommentBean.setCommentBean(commentBean);
                            bdCommentBeanList.add(bdCommentBean);
                        }
                        obtainCharacterInfos(bdCommentBeanList, characterIds);
                    }
                });
    }

    private void obtainCharacterInfos(final List<BdCommentBean> commentBeanList, List<Integer> characterIds) {

        String jArr = JSON.toJSONString(characterIds);
        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_BRIEF)
                .addParams("dataArray", jArr)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取角色信息失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取角色信息 = " + response);
                        try {
                            ResponseContactInfo responseContactInfo = JSONObject.parseObject(response, ResponseContactInfo.class);
                            String code = responseContactInfo.getCode();
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                List<ContactBean> data = responseContactInfo.getData();
                                for (int i = 0; i < commentBeanList.size(); i++) {
                                    BdCommentBean bdCommentBean = commentBeanList.get(i);

                                    for (ContactBean contactBean : data) {
                                        if (bdCommentBean.getCharacterId() == contactBean.getCharacterId()) {
                                            bdCommentBean.setContactBean(contactBean);
                                        }
                                    }
                                }
                                datas.addAll(commentBeanList);
                                mDynamicCommentAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            LogUtils.i("获取角色信息失败");
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void initRecyclerView() {
        initHeadView();
        mRvDynamicComment.setLayoutManager(new LinearLayoutManager(this));
        mDynamicCommentAdapter = new DynamicCommentAdapter(R.layout.item_dynamic_comment, datas);
        mRvDynamicComment.addItemDecoration(new RecyclerViewDivider(this));
        mRvDynamicComment.setAdapter(mDynamicCommentAdapter);
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
        mHeadView = View.inflate(this, R.layout.head_dynamic_comment, null);
        ImageView ivAvatar = (ImageView) mHeadView.findViewById(R.id.iv_item_story_avatar);
        ImageView ivMore = (ImageView) mHeadView.findViewById(R.id.iv_item_story_more);
        TextView tvName = (TextView) mHeadView.findViewById(R.id.tv_item_story_name);
        TextView tvTime = (TextView) mHeadView.findViewById(R.id.tv_item_story_time);
        TextView tvContent = (TextView) mHeadView.findViewById(R.id.tv_head_comment_content);
        TextView tvExpend = (TextView) mHeadView.findViewById(R.id.tv_head_comment_expend);

        NineGridImageView nineGridImageView = (NineGridImageView) mHeadView.findViewById(R.id.ngiv_item_story);
        TextView tvForwarding = (TextView) mHeadView.findViewById(R.id.tv_item_story_forwarding);
        TextView tvHeadLike = (TextView) mHeadView.findViewById(R.id.tv_item_story_like);
        TextView tvHeadComment = (TextView) mHeadView.findViewById(R.id.tv_item_story_comment);
        String characterImg = "";
        String characterName = "";
        if(mCharacterInfo != null){
             characterImg = mCharacterInfo.getHeadImg();
             characterName = mCharacterInfo.getName();
        }

        GlideUtils.load(mContext, characterImg, ivAvatar, 0);
        tvName.setText(characterName);
        tvTime.setText(DateUtils.formatFriendly(new Date(mNovelModel.getCreateTime())));
        tvForwarding.setText(mNovelModel.getTranspond() + "");
        tvHeadLike.setText(mNovelModel.getSupportCount() + "");
        tvHeadComment.setText(mNovelModel.getCommendCount() + "");

        Drawable like_def = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
        Drawable like_selected = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);

        like_def.setBounds(0, 0, like_def.getMinimumWidth(), like_def.getMinimumHeight());
        like_selected.setBounds(0, 0, like_selected.getMinimumWidth(), like_selected.getMinimumHeight());

        tvHeadLike.setCompoundDrawables(isBeFav ? like_selected : like_def, null, null, null);
        tvHeadLike.setCompoundDrawablePadding(DensityUtils.dip2px(this, 10));

        String intro = mNovelModel.getIntro();
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
        tvHeadComment.setOnClickListener(this);
        tvHeadLike.setOnClickListener(this);
        ivMore.setVisibility(View.GONE);

        //根据是否是长文来控制是否显示展开，是长文显示，不是则不显示
        int type = mNovelModel.getType();
        if (type == 2) { //是长文
            tvExpend.setVisibility(View.VISIBLE);
        } else {
            tvExpend.setVisibility(View.GONE);
        }

        tvExpend.setOnClickListener(this);

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
                int characterId = mNovelModel.getCharacterId();
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
                ToastUtils.showToast(this, "喜欢");
                break;
            case R.id.tv_head_comment_expend:
                //跳到阅读模式

                break;
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
        ImageView mIvPopCommentAt = (ImageView) contentView.findViewById(R.id.iv_pop_comment_at);
        ImageView mIvPopCommentTopic = (ImageView) contentView.findViewById(R.id.iv_pop_comment_topic);
        ImageView mIvPopCommentFrame = (ImageView) contentView.findViewById(R.id.iv_pop_comment_frame);

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

        mIvPopCommentAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(NovelDetailsActivity.this, "艾特");
            }
        });

        mIvPopCommentFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(NovelDetailsActivity.this, "小尾巴");
            }
        });

        mIvPopCommentTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(NovelDetailsActivity.this, "话题");
            }
        });
        pop.setTouchable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPopBg)));
        pop.showAtLocation(mLlDynamicDetails, 0, 0, Gravity.BOTTOM);
    }

    private void addComment(String comment) {

        String dataString = "{\"content\": \"" + comment + "\",\"isAnon\":0}";

        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_COMMENT_ADD)
                .addParams("dataString", dataString)
                .addParams("storyId", mNovelModel.getStoryId() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("添加评论失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("添加评论成功 = " + response);
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            initData();
                        } else {
                            ToastUtils.showToast(mContext, "评论失败~");
                        }
                    }
                });
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

}
