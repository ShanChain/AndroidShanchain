package com.shanchain.shandata.adapter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.shandata.R;
import com.shanchain.shandata.interfaces.IChatGroupMenberCallback;
import com.shanchain.shandata.ui.model.GroupTeamBean;
import com.shanchain.shandata.ui.model.Members;
import com.shanchain.shandata.ui.view.activity.jmessageui.SingerChatInfoActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.SingleChatActivity;

import java.util.List;

import cn.jiguang.imui.model.DefaultUser;
import cn.jiguang.imui.view.RoundImageView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by WealChen
 * Date : 2019/7/10
 * Describe :群组成员适配器
 */
public class GroupMenberAdapter extends BaseQuickAdapter<Members,BaseViewHolder> {
    private String photoUrlBase = "http://shanchain-picture.oss-cn-beijing.aliyuncs.com/";
    private IChatGroupMenberCallback mMenberCallback;
    public void setMenberCallback(IChatGroupMenberCallback callback){
        this.mMenberCallback = callback;
    }
    public GroupMenberAdapter(int layoutResId, @Nullable List<Members> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Members item) {
//        helper.setIsRecyclable(false);
        final TextView focus = helper.getView(R.id.tv_item_contact_child_focus);
        ImageView checkBox = helper.getView(R.id.check_box);
        //显示勾选按钮
        if (item.isEdite()) {
            helper.getView(R.id.check_box).setVisibility(View.VISIBLE);
            focus.setOnClickListener(null);
        } else {
            helper.getView(R.id.check_box).setVisibility(View.GONE);
        }
        if(item.isSelect()){
            checkBox.setImageResource(R.mipmap.ic_checked);
        }else {
            checkBox.setImageResource(R.mipmap.ic_uncheck);
        }
        CircleImageView circleImageView = helper.getView(R.id.iv_item_contact_child_avatar);
        Glide.with(mContext).load(item.getUserIcon())
                .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                        .error(R.drawable.aurora_headicon_default)).into(circleImageView);
        helper.setText(R.id.tv_item_contact_child_name, item.getUserName());
        helper.setText(R.id.tv_item_contact_child_focus, R.string.dialogue_1);
        focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMenberCallback!=null){
                    mMenberCallback.chatUser(item);
                }
            }
        });
    }

}
