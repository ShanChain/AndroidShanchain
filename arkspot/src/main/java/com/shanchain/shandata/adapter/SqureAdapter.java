package com.shanchain.shandata.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.GridView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.PhoneFrontBean;
import com.shanchain.shandata.ui.model.SqureDataEntity;
import com.shanchain.shandata.utils.TimeUtils;
import com.shanchain.shandata.widgets.CustomGridView;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.jiguang.imui.view.CircleImageView;

/**
 * Created by WealChen
 * Date : 2019/7/10
 * Describe :
 */
public class SqureAdapter extends BaseQuickAdapter<SqureDataEntity,BaseViewHolder> {
    public SqureAdapter(int layoutResId, @Nullable List<SqureDataEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SqureDataEntity item) {
        CircleImageView circleImageView = helper.getView(R.id.iv_user_head);
        Glide.with(mContext).load(item.getHeadIcon())
                .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                        .error(R.drawable.aurora_headicon_default)).into(circleImageView);
        helper.setText(R.id.tv_nickname,item.getNickName());
        helper.setText(R.id.tv_time, TimeUtils.friendlyTime1(new Date(item.getCreateTime())));
        helper.setText(R.id.et_content,item.getContent());
        helper.setText(R.id.tv_conin,item.getPraiseCount()+"");
        helper.setText(R.id.tv_message,item.getReviceCount()+"");
        helper.setText(R.id.tv_share,item.getCollectCount()+"");
        CustomGridView gridView = helper.getView(R.id.gv_photo);
        if(!TextUtils.isEmpty(item.getListImg())){
            GVPhotoAdapter gvPhotoAdapter = new GVPhotoAdapter(mContext);
            String []attr = item.getListImg().substring(1,item.getListImg().length()-1).split(",");
            gvPhotoAdapter.setPhotoList(Arrays.asList(attr));
            gridView.setAdapter(gvPhotoAdapter);
        }


    }
}
