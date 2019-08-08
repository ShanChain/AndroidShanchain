package com.shanchain.shandata.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.shandata.R;
import com.shanchain.shandata.interfaces.IAttentionCallback;
import com.shanchain.shandata.interfaces.IPraiseCallback;
import com.shanchain.shandata.ui.model.GroupTeamBean;
import com.shanchain.shandata.ui.model.SqureDataEntity;
import com.shanchain.shandata.utils.TimeUtils;

import java.util.Date;
import java.util.List;

import cn.jiguang.imui.view.CircleImageView;
import cn.jiguang.imui.view.RoundImageView;

/**
 * Created by WealChen
 * Date : 2019/7/10
 * Describe :矿区分队适配器
 */
public class GroupTeamAdapter extends BaseQuickAdapter<GroupTeamBean,BaseViewHolder> {

    public GroupTeamAdapter(int layoutResId, @Nullable List<GroupTeamBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final GroupTeamBean item) {
        RoundImageView circleImageView = helper.getView(R.id.iv_avatar);
        Glide.with(mContext).load(R.mipmap.ic_launcher)
                .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                        .error(R.drawable.aurora_headicon_default)).into(circleImageView);
        helper.setText(R.id.tv_title,"我的小分队");
        helper.setText(R.id.tv_person_nums,"3");


    }


}
