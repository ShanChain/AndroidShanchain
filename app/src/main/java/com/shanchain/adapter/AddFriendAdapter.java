package com.shanchain.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.R;
import com.shanchain.base.BaseCommonAdapter;
import com.shanchain.mvp.model.InterestedPersonInfo;
import com.shanchain.utils.GlideCircleTransform;
import com.shanchain.utils.ToastUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * Created by zhoujian on 2017/6/6.
 */

public class AddFriendAdapter extends BaseCommonAdapter<InterestedPersonInfo> {

    public AddFriendAdapter(Context context, int layoutId, List<InterestedPersonInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, final InterestedPersonInfo interestedPersonInfo, int position) {
        Glide.with(mContext).load(R.drawable.photo5)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_interested_avatarUrl));
        holder.setText(R.id.tv_item_insterested_nickName,interestedPersonInfo.getNickName());
        holder.setText(R.id.tv_item_insterested_signature,interestedPersonInfo.getSignature());
        holder.setText(R.id.tv_item_insterested_reason,interestedPersonInfo.getReason());
        holder.setOnClickListener(R.id.tv_item_insterested_focus, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(mContext,"加关注" + interestedPersonInfo.getNickName());
            }
        });
    }
}
