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
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;

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
    private int type;
    public void setType(int type){
        this.type = type;
    }
    public GroupTeamAdapter(int layoutResId, @Nullable List<GroupTeamBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final GroupTeamBean item) {
//        LogUtils.d("------>>>GroupTeamAdapter"+item.toString());
        RoundImageView circleImageView = helper.getView(R.id.iv_avatar);
        Glide.with(mContext).load(item.getRoomImage())
                .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                        .error(R.drawable.aurora_headicon_default)).into(circleImageView);
        if(!TextUtils.isEmpty(item.getRoomName())){
            helper.setText(R.id.tv_title,item.getRoomName());
        }else {
            helper.setText(R.id.tv_title,"我的小分队");
        }
        helper.setText(R.id.tv_person_nums,item.getUserCount()+"");

        if(type ==2){
            helper.getView(R.id.ll_userList).setVisibility(View.VISIBLE);
            CircleImageView ci1 = helper.getView(R.id.iv_user_head1);
            CircleImageView ci2 = helper.getView(R.id.iv_user_head2);
            CircleImageView ci3 = helper.getView(R.id.iv_user_head3);
            if(item.gettDiggingJoinLogs().size()>=3){
                ci1.setVisibility(View.VISIBLE);
                ci2.setVisibility(View.VISIBLE);
                ci3.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(item.gettDiggingJoinLogs().get(0).getUserIcon())
                        .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                                .error(R.drawable.aurora_headicon_default)).into(ci1);
                Glide.with(mContext).load(item.gettDiggingJoinLogs().get(1).getUserIcon())
                        .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                                .error(R.drawable.aurora_headicon_default)).into(ci2);
                Glide.with(mContext).load(item.gettDiggingJoinLogs().get(2).getUserIcon())
                        .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                                .error(R.drawable.aurora_headicon_default)).into(ci3);
            }else if(item.gettDiggingJoinLogs().size()==2){
                ci1.setVisibility(View.VISIBLE);
                ci2.setVisibility(View.VISIBLE);
                ci3.setVisibility(View.GONE);
                Glide.with(mContext).load(item.gettDiggingJoinLogs().get(0).getUserIcon())
                        .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                                .error(R.drawable.aurora_headicon_default)).into(ci1);
                Glide.with(mContext).load(item.gettDiggingJoinLogs().get(1).getUserIcon())
                        .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                                .error(R.drawable.aurora_headicon_default)).into(ci2);
            }else {
                ci1.setVisibility(View.VISIBLE);
                ci2.setVisibility(View.GONE);
                ci3.setVisibility(View.GONE);
                Glide.with(mContext).load(item.gettDiggingJoinLogs().get(0).getUserIcon())
                        .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                                .error(R.drawable.aurora_headicon_default)).into(ci1);
            }
        }else {
            helper.getView(R.id.ll_userList).setVisibility(View.GONE);
        }

    }


    private void setUserIcon(BaseViewHolder helper, GroupTeamBean item){

    }
}
