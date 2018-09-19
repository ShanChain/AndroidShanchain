package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.BdMyCommentBean;
import com.shanchain.shandata.ui.model.ReleaseContentInfo;
import com.shanchain.shandata.utils.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by zhoujian on 2017/9/29.
 */

public class MyCommentsAdapter extends BaseQuickAdapter<BdMyCommentBean,BaseViewHolder> {


    public MyCommentsAdapter(@LayoutRes int layoutResId, @Nullable List<BdMyCommentBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, BdMyCommentBean item) {
        String headImg = item.getContactBean().getHeadImg();
        String name = item.getContactBean().getName();
        GlideUtils.load(mContext,headImg,(ImageView) helper.getView(R.id.iv_my_comments_head),0);
        helper.setText(R.id.tv_my_comments_name,name);
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
