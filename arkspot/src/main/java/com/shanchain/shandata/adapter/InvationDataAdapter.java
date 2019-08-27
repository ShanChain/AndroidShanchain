package com.shanchain.shandata.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.GroupTeamBean;
import com.shanchain.shandata.ui.model.InvationRecordBean;

import java.util.List;

import cn.jiguang.imui.view.CircleImageView;
import cn.jiguang.imui.view.RoundImageView;

/**
 * Created by WealChen
 * Date : 2019/7/10
 * Describe :返佣邀请记录适配器
 */
public class InvationDataAdapter extends BaseQuickAdapter<InvationRecordBean,BaseViewHolder> {
    private int type;
    public void setType(int type){
        this.type = type;
    }
    public InvationDataAdapter(int layoutResId, @Nullable List<InvationRecordBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final InvationRecordBean item) {
        if(!TextUtils.isEmpty(item.getCreateTime())){
            helper.setText(R.id.tv_register_time,item.getCreateTime().split(" ")[0]);
        }
        helper.setText(R.id.tv_register_phone,item.getAcceptUserName());
        if(item.getIsActive() ==0){
            helper.setText(R.id.tv_state,"已激活");
        }else {
            helper.setText(R.id.tv_state,"未激活");
        }
    }

}
