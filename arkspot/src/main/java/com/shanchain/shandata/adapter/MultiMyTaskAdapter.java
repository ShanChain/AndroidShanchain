package com.shanchain.shandata.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jiguang.imui.model.ChatEventMessage;
import okhttp3.Call;


public class MultiMyTaskAdapter extends CommonAdapter<ChatEventMessage> implements BaseViewHolder.OnItemClickListener, BaseViewHolder.OnLayoutViewClickListener, View.OnClickListener {
    private List<ChatEventMessage> list;
    private int[] itemLayoutId;
    private Context context;
    private ChatEventMessage chatEventMessage;
    private OnItemClickListener onItemClickListener;
    private OnClickListener onClickListener;
    private int position;
    private ChatEventMessage itemData;
    private BaseViewHolder holder;
    private ProgressDialog mDialog;
    private static final int UN_RECEIVE = 5;//我发布的：未领取
    private static final int RECEIVE = 10; //我领取的：正在完成/对方正在完成
    private static final int CONFIRM = 15; //我领取的：已确认完成/等待对方确认
    private static final int DONE = 20; //我发布的:去确认完成
    private static final int UNDONE = 21; //我发布的：确认对方任务未完成
    private static final int OVERTIME = 22; //任务超时
    private static final int CANCEL = 25; //我领取的：领取方任务取消
    //5未领取 10已领取/正在完成， //15领取方已确认完成
    //20发布方确认完成，21发布方确认任务未完成 22 任务超时 25领取方任务取消

    public MultiMyTaskAdapter(Context context, List list, int[] itemLayoutId) {
        super(context, list, itemLayoutId);
        this.context = context;
        this.list = list;
        this.itemLayoutId = itemLayoutId;
    }


    @Override
    public void setData(BaseViewHolder holder, final ChatEventMessage item, int viewType, final int itemPosition) {
//        this.position = holder.getLayoutPosition();
//        this.position = itemPosition;
        int characterId = item.getCharacterId();
        String character = SCCacheUtils.getCacheCharacterId();
        if (holder.itemView.getTag() != null) {
            this.position = (Integer) holder.itemView.getTag();
        }
        holder.setIsRecyclable(false);
        final Button undone = (Button) holder.getViewId(R.id.item_task_undone);
        final Button done = (Button) holder.getViewId(R.id.item_task_done);
        final Button confirm = (Button) holder.getViewId(R.id.btn_event_confirm);
        final Button cancel = (Button) holder.getViewId(R.id.item_task_cancel);
        final Button taskStatus = (Button) holder.getViewId(R.id.btn_event_task);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expiryTime0 = simpleDateFormat.format(item.getExpiryTime());
        holder.setTextView(R.id.even_message_last_time, "完成时限：" + expiryTime0 + "");
        holder.setTextView(R.id.even_message_content, item.getIntro() + "");
        holder.setTextView(R.id.even_message_bounty, "赏金：￥ " + item.getBounty());
        holder.setTextView(R.id.even_message_location, item.getRoomName() + "");
        holder.setTextView(R.id.task_release_time, "发布时间 " + simpleDateFormat.format(item.getCreateTime()));//发布时间
        if (viewType!=0||viewType!=7||viewType!=8){
            if (holder.getViewId(R.id.tv_item_story_time)!=null){
                holder.setTextView(R.id.tv_item_story_time,  DateUtils.formatFriendly(new Date(item.getCreateTime())) + "");
            }
        }
        //0我发布的，5:未领取 //1、我发布的，10.对方正在完成//2、我发布的，15:去确认
        // 3、我领取的，10:正在完成//4、我领取的，15:待赏主确认//5、我领取的,20发布方确认任务完成
        // 6、我领取的,21发布方确认任务未完成//7、22:过期的//8、25：任务已被取消

        if (viewType == 0) {
            holder.setTextView(R.id.task_release_time, "发布时间：" + simpleDateFormat.format(item.getCreateTime()));
        } else if (viewType == 1) {
            holder.setTextView(R.id.tv_item_story_name, item.getName() + " 领取的:");
            holder.setImageURL(R.id.iv_item_story_avatar, item.getHeadImg());
            holder.setTextView(R.id.even_message_location, item.getRoomName() + "");
            holder.setTextView(R.id.task_receive_time, "领取时间 " + simpleDateFormat.format(item.getReceiveTime()));
        } else if (viewType == 2) {
            holder.setImageURL(R.id.iv_item_story_avatar, item.getHeadImg());
            holder.setTextView(R.id.tv_item_story_name, item.getName() + " 领取的:");
            holder.setTextView(R.id.task_receive_time, "领取时间 " + simpleDateFormat.format(item.getReceiveTime()));
            holder.setTextView(R.id.task_finish_time, "完成时间 " + simpleDateFormat.format(item.getCompleteTime()));
        } else if (viewType == 3) {
            holder.setImageURL(R.id.iv_item_story_avatar, item.getHeadImg());
            holder.setTextView(R.id.tv_item_story_name, item.getName());
            holder.setTextView(R.id.tv_item_story_name, item.getName() + " 发布的:");
            holder.setTextView(R.id.task_receive_time, "领取时间 " + simpleDateFormat.format(item.getReceiveTime()));

        } else if (viewType == 4) {
            holder.setImageURL(R.id.iv_item_story_avatar, item.getHeadImg());
            holder.setTextView(R.id.tv_item_story_name, item.getName());
            holder.setTextView(R.id.tv_item_story_name, item.getName() + " 发布的:");
            holder.setTextView(R.id.task_receive_time, "领取时间 " + simpleDateFormat.format(item.getReceiveTime()));
            holder.setTextView(R.id.task_finish_time, "完成时间 " + simpleDateFormat.format(item.getCompleteTime()));
        } else if (viewType == 5) {
            holder.setImageURL(R.id.iv_item_story_avatar, item.getHeadImg());
            holder.setTextView(R.id.tv_item_story_name, item.getName());
            holder.setTextView(R.id.tv_item_story_name, item.getName() + " 发布的:");
            holder.setTextView(R.id.task_receive_time, "领取时间 " + simpleDateFormat.format(item.getReceiveTime()));
            holder.setTextView(R.id.task_finish_time, "完成时间 " + simpleDateFormat.format(item.getCompleteTime()));
            holder.setTextView(R.id.task_verify_time, "确认时间 " + simpleDateFormat.format(item.getVerifyTime()));
        } else if (viewType == 6) {
            holder.setImageURL(R.id.iv_item_story_avatar, item.getHeadImg());
            holder.setTextView(R.id.tv_item_story_name, item.getName());
            holder.setTextView(R.id.tv_item_story_name, item.getName() + " 发布的:");
            holder.setTextView(R.id.task_receive_time, "领取时间 " + simpleDateFormat.format(item.getReceiveTime()));
            holder.setTextView(R.id.task_finish_time, "完成时间 " + simpleDateFormat.format(item.getCompleteTime()));

        }

        this.chatEventMessage = item;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LogUtils.d("复用问题", "" + holder.isRecyclable());
        holder.itemView.setTag(itemPosition);
//        itemLayout点击事件
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showToast(context, item.getTaskId() + "" + item.getIntro() + itemPosition);
            }
        });
        setViewOnClick(holder, item, viewType, position);
    }

    private void setViewOnClick(final BaseViewHolder holder, final ChatEventMessage itemData, int viewType, int position) {
        int characterId = itemData.getCharacterId();
        String character = SCCacheUtils.getCacheCharacterId();
        final TextView taskReleaseTime = (TextView) holder.getViewId(R.id.task_release_time);
        TextView taskReceiveTime = (TextView) holder.getViewId(R.id.task_receive_time);
        final TextView taskFinishTime = (TextView) holder.getViewId(R.id.task_finish_time);
        TextView taskVerifyTime = (TextView) holder.getViewId(R.id.task_verify_time);
        final Button undone = (Button) holder.getViewId(R.id.item_task_undone);
        final Button done = (Button) holder.getViewId(R.id.item_task_done);
        final Button confirm = (Button) holder.getViewId(R.id.btn_event_confirm);
        final Button cancel = (Button) holder.getViewId(R.id.item_task_cancel);
        holder.setViewOnClick(R.id.item_task_urge, itemLayoutId, viewType, position, this);
        holder.setLayoutViewOnClick(itemLayoutId, viewType, itemData, this);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //     * 点击已完成
        switch (viewType) {
            case 3:
                holder.getViewId(R.id.btn_event_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgress();
//                        ToastUtils.showToast(context, itemData.getTaskId() + "" + itemData.getIntro());
                        String s = SCCacheUtils.getCacheCharacterId();
                        SCHttpUtils.postWithUserId()
                                .url(HttpApi.TASK_DETAIL_ACCOMPLISH)
                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                .addParams("taskId", itemData.getTaskId() + "")
                                .build()
                                .execute(new SCHttpStringCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        closeProgress();
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        TextView release = holder.getViewId(R.id.task_release_time);
                                        TextView receive = holder.getViewId(R.id.task_receive_time);
                                        TextView finish = holder.getViewId(R.id.task_finish_time);
                                        TextView confirmBtn = holder.getViewId(R.id.btn_event_confirm);
                                        TextView eventTask = holder.getViewId(R.id.btn_event_task);
                                        cancel.setVisibility(View.GONE);
                                        confirm.setVisibility(View.GONE);
                                        eventTask.setText("待赏主确认");
                                        eventTask.setTextColor(context.getResources().getColor(R.color.aurora_bg_edittext_default));
                                        eventTask.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_even_msg));
                                        closeProgress();
                                        notifyDataSetChanged();
                                    }
                                });
                    }
                });

                //     *点击取消任务
                holder.getViewId(R.id.item_task_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgress();
                        SCHttpUtils.postWithUserId()
                                .url(HttpApi.TASK_DETAIL_CANCEL)
                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                .addParams("taskId", itemData.getTaskId() + "")
                                .addParams("userId", SCCacheUtils.getCacheUserId() + "")
                                .build()
                                .execute(new SCHttpStringCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        closeProgress();
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        closeProgress();
                                        TextView eventTask = holder.getViewId(R.id.btn_event_task);
                                        cancel.setVisibility(View.GONE);
                                        eventTask.setText("任务取消");
                                        eventTask.setTextColor(context.getResources().getColor(R.color.aurora_bg_edittext_default));
                                        eventTask.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_even_msg));
                                        confirm.setText("已取消");
                                        confirm.setTextColor(context.getResources().getColor(R.color.aurora_bg_edittext_default));
                                        confirm.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_even_msg));
                                        confirm.setOnClickListener(null);
                                        notifyDataSetChanged();

                                    }
                                });
                    }
                });
                break;
            case 2:
                //   点击确认完成
                holder.getViewId(R.id.item_task_done).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ToastUtils.showToastLong(context, itemData.getTaskId() + "" + itemData.getIntro());
                        showProgress();
                        SCHttpUtils.postWithUserId()
                                .url(HttpApi.TASK_DETAIL_DONE)
                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                .addParams("taskId", itemData.getTaskId() + "")
                                .addParams("userId", SCCacheUtils.getCacheUserId() + "")
                                .build()
                                .execute(new SCHttpStringCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        closeProgress();
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        undone.setVisibility(View.GONE);
                                        done.setText("对方已收到赏金");
                                        done.setTextColor(context.getResources().getColor(R.color.aurora_bg_edittext_default));
                                        done.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_even_msg));
                                        done.setOnClickListener(null);
                                        closeProgress();
                                        notifyDataSetChanged();

                                    }
                                });
                    }
                });

                // 点击未完成
                holder.getViewId(R.id.item_task_undone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgress();
                        SCHttpUtils.postWithUserId()
                                .url(HttpApi.TASK_DETAIL_UNDONE)
                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                .addParams("taskId", itemData.getTaskId() + "")
                                .build()
                                .execute(new SCHttpStringCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        closeProgress();
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        undone.setVisibility(View.GONE);
                                        done.setText("赏金已退回您的账户");
                                        done.setTextColor(context.getResources().getColor(R.color.aurora_bg_edittext_default));
                                        done.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_even_msg));
                                        done.setOnClickListener(null);
                                        closeProgress();
                                        notifyDataSetChanged();
                                    }
                                });
                    }
                });
                break;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        int status = list.get(position).getStatus();
        int characterId = list.get(position).getCharacterId();
        String myCharacterId = SCCacheUtils.getCacheCharacterId();
        // 0
        if (status == RECEIVE) {
            if (myCharacterId.equals(String.valueOf(characterId))) {
                return 1;
            } else {
                return 3;
            }
        } else if (status == CONFIRM) {
            if (myCharacterId.equals(String.valueOf(characterId))) {
                return 2;
            } else {
                return 4;
            }
        } else if (status == CANCEL) {
            return 8;
        } else if (status == DONE) {
            return 5;
        } else if (status == UNDONE) {
            return 6;
        } else if (status == OVERTIME) {
            return 7;
        } else {
            return 0;
        }

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
//        onClickListener.OnClick();
        switch (v.getId()) {
            case R.id.item_task_urge:

                SCHttpUtils.postWithUserId()
                        .url(HttpApi.TASK_DETAIL_URGE)
                        .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                        .addParams("targetId", itemData.getTaskId() + "")
                        .build()
                        .execute(new SCHttpStringCallBack() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                TextView release = holder.getViewId(R.id.task_release_time);
                                TextView receive = holder.getViewId(R.id.task_receive_time);
                                TextView finish = holder.getViewId(R.id.task_finish_time);
                            }
                        });
                break;
        }
    }

    public void showProgress() {
        mDialog = new ProgressDialog(context);
        mDialog.setMax(100);
        mDialog.setMessage("数据请求中..");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public void closeProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void OnItemClick(View view) {
      switch (view.getId()) {
            case R.id.btn_event_task:
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(chatEventMessage, view, holder, position);
                }
                break;
        }

    }

    @Override
    public void OnLayoutViewClick(View view) {

        if (onClickListener != null) {
            onClickListener.OnClick(chatEventMessage, view, holder, position);
        }
    }


    public interface OnItemClickListener {
        void OnItemClick(ChatEventMessage item, View view, BaseViewHolder holder, int position);

    }


    public interface OnClickListener {

        void OnClick(ChatEventMessage item, View view, BaseViewHolder holder, int position);

    }
}
