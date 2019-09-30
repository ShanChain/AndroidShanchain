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
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.interfaces.IChatGroupMenberCallback;
import com.shanchain.shandata.ui.model.Members;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;

import java.util.List;

import cn.jiguang.imui.model.DefaultUser;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by WealChen
 * Date : 2019/7/10
 * Describe :ARS群组成员适配器
 */
public class GroupARSMenberAdapter extends BaseQuickAdapter<Members,BaseViewHolder> {
    private String photoUrlBase = "http://shanchain-picture.oss-cn-beijing.aliyuncs.com/";
    private IChatGroupMenberCallback mMenberCallback;
    public void setMenberCallback(IChatGroupMenberCallback callback){
        this.mMenberCallback = callback;
    }
    public GroupARSMenberAdapter(int layoutResId, @Nullable List<Members> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Members item) {
        LogUtils.d("----username:"+item.getUsername());
        final TextView focus = helper.getView(R.id.tv_item_contact_child_focus);
        ImageView checkBox = helper.getView(R.id.check_box);
        if(Integer.parseInt(SCCacheUtils.getCacheUserId())== item.getUserId()){
            focus.setVisibility(View.INVISIBLE);
        }else {
            focus.setVisibility(View.VISIBLE);
        }
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
        JMessageClient.getUserInfo(item.getUserName(), new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                    LogUtils.d("----username11, i: "+i+"---s: "+s);
                LogUtils.d("----username11:"+userInfo.toJson());
                    String name = userInfo.getNickname() != null ? userInfo.getNickname() : userInfo.getUserName();
                    helper.setText(R.id.tv_item_contact_child_name, TextUtils.isEmpty(name) ? "" + userInfo.getDisplayName() : name);
                    final CircleImageView circleImageView = helper.getView(R.id.iv_item_contact_child_avatar);
                    userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                        @Override
                        public void gotResult(int i, String s, Bitmap bitmap) {
                            Bitmap bitmap1 = bitmap;
                            if (bitmap != null) {
                                circleImageView.setImageBitmap(bitmap);
                            } else {
                                circleImageView.setBackground(mContext.getResources().getDrawable(R.mipmap.aurora_headicon_default));
                            }

                        }
                    });
            }
        });
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
