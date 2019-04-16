package com.shanchain.shandata.widgets.other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.view.activity.jmessageui.FriendInfoActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.SingerChatInfoActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.SingleChatActivity;

import cn.jiguang.imui.model.DefaultUser;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by ${chenyn} on 2017/3/24.
 */

public class FriendInfoController implements View.OnClickListener {
    private FriendInfoActivity mContext;
    private UserInfo friendInfo;

    public FriendInfoController(FriendInfoView friendInfoView, FriendInfoActivity context) {
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goToChat:
//                mContext.startChatActivity();
                Intent intent = new Intent(mContext, SingleChatActivity.class);
                Bundle bundle1 = new Bundle();
                if (friendInfo != null) {
                    String avatar = friendInfo.getAvatarFile() != null ?
                            friendInfo.getAvatarFile().getAbsolutePath() : "";
                    DefaultUser userInfo = new DefaultUser(0, friendInfo.getDisplayName(), avatar);
                    userInfo.setHxUserId(friendInfo.getUserName() + "");
                    userInfo.setDisplayName(friendInfo.getDisplayName() + "");
                    userInfo.setSignature(friendInfo.getSignature() + "");
                    bundle1.putParcelable("userInfo", userInfo);
                    intent.putExtras(bundle1);
                }
                mContext.startActivity(intent);
                break;
            case R.id.iv_friendPhoto:
                mContext.startBrowserAvatar();
                break;
//            case R.id.jmui_commit_btn:
////                Intent intent = new Intent(mContext, FriendSettingActivity.class);
////                intent.putExtra("userName", friendInfo.getUserName());
////                intent.putExtra("noteName", friendInfo.getNotename());
////                mContext.startActivity(intent);
//                break;
            case R.id.return_btn:
                mContext.finish();
                break;
            default:
                break;
        }
    }

    public void setFriendInfo(UserInfo info) {
        friendInfo = info;
    }

}
