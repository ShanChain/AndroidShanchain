package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.BdMyCommentBean;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.ReleaseContentInfo;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.GlideUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by zhoujian on 2017/9/29.
 */

public class MyCommentsAdapter extends BaseQuickAdapter<BdMyCommentBean,BaseViewHolder> {

    private final String mHeadImg;
    private final String mName;

    public MyCommentsAdapter(@LayoutRes int layoutResId, @Nullable List<BdMyCommentBean> data) {
        super(layoutResId, data);
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
        String characterInfo = SCCacheUtils.getCache(userId, Constants.CACHE_CHARACTER_INFO);
        CharacterInfo characterBean = JSONObject.parseObject(characterInfo, CharacterInfo.class);
        mHeadImg = characterBean.getHeadImg();
        mName = characterBean.getName();

    }

    @Override
    protected void convert(BaseViewHolder helper, BdMyCommentBean item) {
        GlideUtils.load(mContext,mHeadImg,(ImageView) helper.getView(R.id.iv_my_comments_head),0);
        helper.setText(R.id.tv_my_comments_name,mName);
        helper.setText(R.id.tv_my_comments_count,item.getCommentBean().getSupportCount()+"");
        String time = DateUtils.formatFriendly(new Date(item.getCommentBean().getCreateTime()));
        helper.setText(R.id.tv_my_comments_time,time);
        helper.setText(R.id.tv_my_comments_comment,item.getCommentBean().getContent());
        String intro = item.getStoryInfo().getIntro();
        String content = "";
        if (intro.contains("content")){
            ReleaseContentInfo releaseContentInfo = JSONObject.parseObject(intro, ReleaseContentInfo.class);
            content = releaseContentInfo.getContent();
        }else {
            content = intro;
        }
        helper.setText(R.id.tv_my_comments_content,content);
    }
}
