package com.shanchain.shandata.adapter;

import android.content.Context;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.ChallengeOnGoingInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class ChallengeOnGoingAdapter extends BaseCommonAdapter<ChallengeOnGoingInfo> {

    public ChallengeOnGoingAdapter(Context context, int layoutId, List<ChallengeOnGoingInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, ChallengeOnGoingInfo challengeOnGoingInfo, int position) {
        holder.setText(R.id.tv_item_challenge_going_des, challengeOnGoingInfo.getDes());
        switch (challengeOnGoingInfo.getType()) {
            case 1:
                holder.setImageResource(R.id.iv_item_challenge_going_type,R.mipmap.home_icon_walkmore_default);
                holder.setText(R.id.tv_item_challenge_going_type,"多走走");
                break;
            case 2:
                holder.setImageResource(R.id.iv_item_challenge_going_type,R.mipmap.home_icon_happy_default);
                holder.setText(R.id.tv_item_challenge_going_type,"开心点");
                break;
            case 3:
                holder.setImageResource(R.id.iv_item_challenge_going_type,R.mipmap.home_icon_sleep_default);
                holder.setText(R.id.tv_item_challenge_going_type,"早点睡");
                break;
            case 4:
                holder.setImageResource(R.id.iv_item_challenge_going_type,R.mipmap.home_icon_focuson_default);
                holder.setText(R.id.tv_item_challenge_going_type,"别低头");
                break;
        }
    }
}
