package com.shanchain.shandata.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RNPagesConstant;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.ContactBean;
import com.shanchain.shandata.ui.model.RNDetailExt;
import com.shanchain.shandata.ui.model.RNGDataBean;
import com.shanchain.shandata.ui.model.ReleaseContentInfo;
import com.shanchain.shandata.ui.model.SpanBean;
import com.shanchain.shandata.ui.model.StoryChainBean;
import com.shanchain.shandata.ui.model.StoryChainModel;
import com.shanchain.shandata.ui.view.activity.story.TopicDetailsActivity;
import com.shanchain.shandata.utils.ClickableSpanNoUnderline;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.shandata.utils.SCLinkMovementMethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhoujian on 2017/11/23.
 */

public class ChainAdapter extends BaseQuickAdapter<StoryChainModel,BaseViewHolder> {
    private Drawable mDrawable;
    public ChainAdapter(@LayoutRes int layoutResId, @Nullable List<StoryChainModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoryChainModel item) {

        helper.addOnClickListener(R.id.iv_item_story_avatar)
                .addOnClickListener(R.id.iv_item_story_more)
                .addOnClickListener(R.id.tv_item_story_forwarding)
                .addOnClickListener(R.id.tv_item_story_comment)
                .addOnClickListener(R.id.tv_item_story_like);

        StoryChainBean storyBean = item.getStoryBean();
        ContactBean characterBean = item.getCharacterBean();

        boolean beFav = item.isBeFav();
        long createTime = storyBean.getCreateTime();
        String time = DateUtils.formatFriendly(new Date(createTime));
        TextView tvLike = helper.getView(R.id.tv_item_story_like);
        helper.setText(R.id.tv_item_story_time, time);
        helper.setText(R.id.tv_item_story_comment, storyBean.getCommentCount() + "");
        helper.setText(R.id.tv_item_story_name,characterBean==null?"":characterBean.getName());
        helper.setText(R.id.tv_item_story_forwarding,storyBean.getTranspond() + "");
        helper.setText(R.id.tv_item_story_like,storyBean.getSupportCount() + "");
        if (beFav){
            mDrawable = mContext.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);
        }else {
            mDrawable = mContext.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
        }
        mDrawable.setBounds(0,0,mDrawable.getMinimumWidth(),mDrawable.getMinimumHeight());
        tvLike.setCompoundDrawables(mDrawable,null,null,null);
        tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mContext, 10));

        String intro = storyBean.getIntro();
        List<String> imgs = new ArrayList<>();
        String content = "";
        List<SpanBean> spanBeanList = null;
        if (intro.contains("content")){
            ReleaseContentInfo contentInfo = JSONObject.parseObject(intro,ReleaseContentInfo.class);
            content = contentInfo.getContent();
            imgs = contentInfo.getImgs();
            spanBeanList = contentInfo.getSpanBeanList();
        }else {
            content = intro;
        }
        ImageView ivAvatar = helper.getView(R.id.iv_item_story_avatar);
        GlideUtils.load(mContext,characterBean==null?"":characterBean.getHeadImg(),ivAvatar,0);



        NineGridImageView nineGridImageView = helper.getView(R.id.ngiv_story_chain);

        if (imgs.size() == 0) {
            nineGridImageView.setVisibility(View.GONE);
        } else {
            nineGridImageView.setVisibility(View.VISIBLE);
            StoryItemNineAdapter adapter = new StoryItemNineAdapter();
            nineGridImageView.setAdapter(adapter);
            nineGridImageView.setImagesData(imgs);
        }

        TextView tvContent = helper.getView(R.id.tv_item_story_chain_content);
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
                            String spaceId = clickData.getSpaceId() + "";
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
        tvContent.setMovementMethod(SCLinkMovementMethod.getInstance());
        tvContent.setText(spannableStringBuilder);
    }
}
