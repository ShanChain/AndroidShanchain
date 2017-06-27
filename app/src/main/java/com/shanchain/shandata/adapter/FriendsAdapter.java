package com.shanchain.shandata.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.FriendsInfo;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/6.
 */

public class FriendsAdapter extends BaseCommonAdapter<FriendsInfo> {

    private List<FriendsInfo> mDatas;

    public FriendsAdapter(Context context, int layoutId, List<FriendsInfo> datas) {
        super(context, layoutId, datas);
        mDatas = datas;
    }

    @Override
    public void bindDatas(ViewHolder holder, FriendsInfo friendsInfo, int position) {
        Glide.with(mContext).load(R.drawable.photo2)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_friends_avatar));
        holder.setText(R.id.tv_item_friends_name, friendsInfo.getName());
        TextView tvFriendsFocus =
                holder.getView(R.id.tv_item_friends_focus);
        if (friendsInfo.isShanChainer()) {

            holder.setVisible(R.id.tv_item_friends_nickName, true);
            holder.setText(R.id.tv_item_friends_nickName, friendsInfo.getNickName());
            holder.setText(R.id.tv_item_friends_head, "他们也在玩善数者,赶紧关注吧");

            if (position != 1 ){
                holder.setVisible(R.id.tv_item_friends_head,false);
            }else {
                holder.setVisible(R.id.tv_item_friends_head,true);
            }

            if (friendsInfo.isFocus()) {
                Drawable drawable = mContext.getResources()
                        .getDrawable(R.mipmap.friends_btn_already_added_defult);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvFriendsFocus.setText("已关注");
                tvFriendsFocus.setTextColor(Color.parseColor("#d2d2d2"));
                tvFriendsFocus.setCompoundDrawables(null, drawable, null, null);
            } else {
                Drawable drawable = mContext.getResources()
                        .getDrawable(R.mipmap.friends_btn_add_to_selected);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvFriendsFocus.setTextColor(mContext.getResources().getColor(R.color.colorActive));
                tvFriendsFocus.setText("加关注");
                tvFriendsFocus.setCompoundDrawables(null, drawable, null, null);
            }

        } else {

            TextView tvNickName = holder.getView(R.id.tv_item_friends_nickName);
            tvNickName.setVisibility(View.INVISIBLE);
            holder.setText(R.id.tv_item_friends_head, "他们还没有善数者,赶紧发出邀请");
            if (!mDatas.get(position-2).isShanChainer()){
                holder.setVisible(R.id.tv_item_friends_head,false);
            }else {
                holder.setVisible(R.id.tv_item_friends_head,true);
            }

            Drawable drawable = mContext.getResources()
                    .getDrawable(R.mipmap.friends_btn_invite_selected);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvFriendsFocus.setTextColor(mContext.getResources().getColor(R.color.colorActive));
            tvFriendsFocus.setText("邀请");
            tvFriendsFocus.setCompoundDrawables(null, drawable, null, null);
        }


    }
}
