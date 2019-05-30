package com.shanchain.shandata.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.HotChatRoom;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;

import java.util.List;

import cn.jiguang.imui.view.RoundImageView;
import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/4/9
 * Describe :
 */
public class ARSGamneRoomAdapter extends BaseMultiItemQuickAdapter<HotChatRoom, BaseViewHolder> {
    private Context mContext;
    private List<HotChatRoom> mHotChatRooms;

    public ARSGamneRoomAdapter(@Nullable Context context, List<HotChatRoom> data) {
        super(data);
        this.mContext = context;
        addItemType(0, R.layout.item_hot_chat_room);
//        addItemType(1, R.layout.item_hot_chat_room_type_one);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void convert(BaseViewHolder helper, final HotChatRoom item) {
        if (item.getType().equals("ars")) {
            RoundImageView roundImageView = helper.getView(R.id.item_round_view);
            RoundImageView avatar = helper.getView(R.id.iv_item_msg_home_avatar);
            TextView roomName = helper.getView(R.id.tv_item_room_name);
            TextView roomNum = helper.getView(R.id.tv_item_member_num);
            TextView visitTime = helper.getView(R.id.tv_item_visit_time);
            TextView btnJoin = helper.getView(R.id.bt_item_join);

            roomName.setText(item.getRoomName());
            roomNum.setText(item.getUserNum());
            RequestOptions options = new RequestOptions();
//        options.placeholder(R.mipmap.empty_foot_print);
            Glide.with(mContext).load(item.getBackground()).apply(options).into(roundImageView);
            Glide.with(mContext).load(item.getThumbnails()).apply(options).into(avatar);
            if (item.isLitUp()) {
                btnJoin.setBackground(mContext.getDrawable(R.drawable.shape_bg_msg_send_pressed));
                helper.getView(R.id.relative_hot_room).setForeground(null);
            } else {
                btnJoin.setBackground(mContext.getDrawable(R.drawable.shape_bg_msg_send_default));
                helper.getView(R.id.relative_hot_room).setForeground(mContext.getDrawable(R.color.colorArs));
            }
        }
    }

    private void UpdateChatRoomNum(HotChatRoom item) {
        //更新首页热门元社区列表
        SCHttpUtils.post()
                .url(HttpApi.CHAT_ROOM_LIST_UPDATE)
                .addParams("roomId", item.getRoomId() + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d(TAG, "网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d(TAG, "刷新首页列表数据成功");
                    }
                });
    }

    private void isInBlacklist(String roomID, final HotChatRoom item) {
        //判断是否在群黑名单里
        SCHttpUtils.get()
                .url(HttpApi.IS_BLACK_MEMBER)
                .addParams("roomId", roomID)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = SCJsonUtils.parseCode(response);
                        if (NetErrCode.COMMON_SUC_CODE.equals(code) || NetErrCode.SUC_CODE.equals(code)) {
                            String data = SCJsonUtils.parseData(response);
                            boolean isBlackMember = Boolean.valueOf(data);
                            if (!isBlackMember) {
                                Intent intent = new Intent(mContext, MessageListActivity.class);
                                intent.putExtra("roomId", item.getRoomId());
                                intent.putExtra("roomName", item.getRoomName());
                                intent.putExtra("hotChatRoom", item);
                                intent.putExtra("isHotChatRoom", true);
//                                intent.putExtra("isInCharRoom", isIn);
                                mContext.startActivity(intent);
                            } else {
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showToast(mContext, "您已被该聊天室管理员删除");
                                    }
                                });
                            }

                        }
                    }
                });
    }

}
