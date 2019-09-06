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

import java.util.List;

import cn.jiguang.imui.model.DefaultUser;
import cn.jiguang.imui.view.CircleImageView;
import cn.jiguang.imui.view.RoundImageView;

/**
 * Created by WealChen
 * Date : 2019/7/10
 * Describe :客服列表
 */
public class ClientListAdapter extends BaseQuickAdapter<DefaultUser,BaseViewHolder> {
    public ClientListAdapter(int layoutResId, @Nullable List<DefaultUser> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, final DefaultUser item) {
        helper.setText(R.id.tv_user_name,item.getDisplayName());
    }

}
