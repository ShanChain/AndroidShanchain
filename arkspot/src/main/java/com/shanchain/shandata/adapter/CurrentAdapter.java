package com.shanchain.shandata.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RNPagesConstant;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.RNDetailExt;
import com.shanchain.shandata.ui.model.RNGDataBean;
import com.shanchain.shandata.ui.model.ReleaseContentInfo;
import com.shanchain.shandata.ui.model.SpanBean;
import com.shanchain.shandata.ui.model.StoryBeanModel;
import com.shanchain.shandata.ui.model.StoryInfo;
import com.shanchain.shandata.ui.model.StoryModel;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.model.StoryModelInfo;
import com.shanchain.shandata.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.shandata.ui.view.activity.story.TopicDetailsActivity;
import com.shanchain.shandata.utils.ClickableSpanNoUnderline;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.shandata.utils.SCLinkMovementMethod;
import com.shanchain.shandata.widgets.other.AutoHeightListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CurrentAdapter extends BaseMultiItemQuickAdapter<StoryBeanModel, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */

    private Drawable mDrawable;
    private final String mCacheSpaceId;

    public CurrentAdapter(List<StoryBeanModel> data) {
        super(data);
        addItemType(StoryBeanModel.type1, R.layout.item_story_type3);
        addItemType(StoryBeanModel.type2, R.layout.item_story_type2);
        addItemType(StoryBeanModel.type3, R.layout.item_story_type4);
        mCacheSpaceId = SCCacheUtils.getCacheSpaceId();
    }

    @Override
    protected void convert(BaseViewHolder holder, StoryBeanModel item) {
        StoryModel storyModel = item.getStoryModel();
        final StoryModelBean bean = storyModel.getModelInfo().getBean();
        String characterImg = bean.getCharacterImg();
        String characterName = bean.getCharacterName();
        boolean beFav = bean.isBeFav();

        holder.setText(R.id.tv_item_story_name, characterName);
        ImageView ivHeadImg = holder.getView(R.id.iv_item_story_avatar);

        GlideUtils.load(mContext, characterImg, ivHeadImg, 0);
        String time = DateUtils.formatFriendly(new Date(bean.getCreateTime()));
        holder.setText(R.id.tv_item_story_time, time);
        holder.setText(R.id.tv_item_story_comment, bean.getCommendCount() + "");
        holder.setText(R.id.tv_item_story_like, bean.getSupportCount() + "");
        if (TextUtils.equals(mCacheSpaceId, bean.getSpaceId() + "")) {
            holder.setVisible(R.id.tv_item_story_from, false);
        } else {
            holder.setVisible(R.id.tv_item_story_from, true);
            holder.setText(R.id.tv_item_story_from, "来自" + bean.getSpaceName());
        }
        TextView tvLike = holder.getView(R.id.tv_item_story_like);

        if (beFav) {
            mDrawable = mContext.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);
        } else {
            mDrawable = mContext.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
        }

        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
        tvLike.setCompoundDrawables(mDrawable, null, null, null);
        tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mContext, 10));

        holder.addOnClickListener(R.id.iv_item_story_avatar)
                .addOnClickListener(R.id.iv_item_story_more)
                .addOnClickListener(R.id.tv_item_story_forwarding)
                .addOnClickListener(R.id.tv_item_story_comment)
                .addOnClickListener(R.id.tv_item_story_like)
                .addOnClickListener(R.id.tv_item_story_floors);


        switch (holder.getItemViewType()) {
            case StoryInfo.type1:
                String content = "";
                List<String> imgs = new ArrayList<>();
                String intro = bean.getIntro();
                LogUtils.d("内容信息 = " + intro);
                List<SpanBean> spanBeanList = null;
                if (intro.contains("content")) {
                    ReleaseContentInfo contentInfo = JSONObject.parseObject(intro, ReleaseContentInfo.class);
                    content = contentInfo.getContent();
                    spanBeanList = contentInfo.getSpanBeanList();
                    //span处理
                    imgs = contentInfo.getImgs();
                } else {
                    content = intro;
                }

                final List<StoryModelInfo> storyChain = storyModel.getStoryChain();
                if (storyChain != null) {
                    LogUtils.d("故事连长度 = " + storyChain.size());
                }
                AutoHeightListView lv = holder.getView(R.id.lv_item_story);
                if (storyChain == null || storyChain.size() == 0) {
                    LogUtils.d("gone==========");
                    holder.setVisible(R.id.tv_item_story_floors, false);
                    holder.setVisible(R.id.lv_item_story, false);
                } else {
                    LogUtils.d("visible==========");
                    holder.setVisible(R.id.tv_item_story_floors, true);
                    holder.setVisible(R.id.lv_item_story, true);
                    StoryItemFloorsAdapter floorsAdapter = new StoryItemFloorsAdapter(storyChain);
                    lv.setAdapter(floorsAdapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(mContext, DynamicDetailsActivity.class);
                            StoryModelBean storyModelBean = storyChain.get(position).getBean();
                            intent.putExtra("story", storyModelBean);
                            mContext.startActivity(intent);
                        }
                    });
                }

                holder.setVisible(R.id.tv_item_story_forwarding, true);
                NineGridImageView nineGridImageView = holder.getView(R.id.ngiv_item_story);

                if (imgs.size() == 0) {
                    nineGridImageView.setVisibility(View.GONE);
                } else {
                    nineGridImageView.setVisibility(View.VISIBLE);
                    StoryItemNineAdapter adapter = new StoryItemNineAdapter();
                    nineGridImageView.setAdapter(adapter);
                    nineGridImageView.setImagesData(imgs);
                }

                holder.setText(R.id.tv_item_story_forwarding, bean.getTranspond() + "");

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
                                    detailExt.setModelId(clickData.getBeanId()+"");
                                    String json =JSONObject.toJSONString(detailExt);
                                    bundle.putString(NavigatorModule.REACT_PROPS, json);
                                    NavigatorModule.startReactPage(mContext, RNPagesConstant.RoleDetailScreen,bundle);
                                }else if (clickData.getType() == Constants.SPAN_TYPE_TOPIC){
                                    String cacheSpaceId = SCCacheUtils.getCacheSpaceId();
                                    int spaceId = clickData.getSpaceId();
                                    if (TextUtils.equals(cacheSpaceId,spaceId+"")){
                                        int beanId = clickData.getBeanId();
                                        Intent intent = new Intent(mContext, TopicDetailsActivity.class);
                                        intent.putExtra("topicId",beanId + "");
                                        intent.putExtra("from",1);
                                        mContext.startActivity(intent);
                                    }else {
                                        ToastUtils.showToast(mContext,"不可查看非当前世界的话题内容");
                                    }

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
                TextView tvContent = holder.getView(R.id.tv_item_story_content);
                tvContent.setMovementMethod(SCLinkMovementMethod.getInstance());
                tvContent.setText(spannableStringBuilder);
                break;
            case StoryInfo.type2:
                holder.setText(R.id.tv_item_story_title, bean.getTitle());
                String introLong = bean.getIntro();
                String replace = introLong.replace(bean.getTitle() + "\n", "");
                holder.setText(R.id.tv_item_story_content, replace);
                holder.setVisible(R.id.tv_item_story_forwarding, false);
                break;
            case StoryInfo.type3:
                String topicImg = bean.getBackground();
                String title = bean.getTitle();
                holder.setText(R.id.tv_story_topic_title, "#" + title + "#");
                if (TextUtils.isEmpty(topicImg)) {
                    holder.setVisible(R.id.iv_item_story_img, false);
                } else {
                    holder.setVisible(R.id.iv_item_story_img, true);
                    GlideUtils.load(mContext, topicImg, (ImageView) holder.getView(R.id.iv_item_story_img), 0);
                }
                holder.setVisible(R.id.tv_item_story_forwarding, false);
                holder.setText(R.id.tv_item_story_intro, bean.getIntro());
                holder.setVisible(R.id.ll_topic_function, false);

                break;
        }
    }

}
