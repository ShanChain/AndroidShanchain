package com.shanchain.shandata.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expiryTime0 = simpleDateFormat.format(item.getExpiryTime());
        holder.setTextView(R.id.even_message_last_time, "完成时限：" + expiryTime0 + "");
        holder.setTextView(R.id.even_message_content, item.getIntro() + "");
        holder.setTextView(R.id.even_message_bounty, "赏金： " + item.getBounty() + " SEAT");
        holder.setTextView(R.id.even_message_location, item.getRoomName() + "");

        if (viewType != 0) {
            holder.setImageURL(R.id.iv_item_story_avatar, item.getHeadImg());
            holder.setTextView(R.id.tv_item_story_name, item.getName());
        }

        this.chatEventMessage = list.get(holder.getLayoutPosition());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        LogUtils.d("复用问题", "" + holder.isRecyclable());
        holder.itemView.setTag(itemPosition);

        //itemLayout点击事件
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showToast(context, item.getIntro() + "" + itemPosition);
            }
        });
        setViewOnClick(holder, item, viewType, position);
    }
    private void setViewOnClick(final BaseViewHolder holder, ChatEventMessage itemData, int viewType, int position) {
        int characterId = itemData.getCharacterId();
        String character = SCCacheUtils.getCacheCharacterId();
        TextView taskReleaseTime = (TextView) holder.getViewId(R.id.task_release_time);
        TextView taskReceiveTime = (TextView) holder.getViewId(R.id.task_receive_time);
        TextView taskFinishTime = (TextView) holder.getViewId(R.id.task_finish_time);
        Button undone = (Button) holder.getViewId(R.id.item_task_undone);
        Button done = (Button) holder.getViewId(R.id.item_task_done);
        holder.setViewOnClick(R.id.item_task_urge, itemLayoutId, viewType, position, this);
        holder.setLayoutViewOnClick(itemLayoutId,viewType,itemData,this);

        //     * 点击确认完成
        switch (viewType) {
            case 1:
                holder.getViewId(R.id.btn_event_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgress();
                        SCHttpUtils.postWithUserId()
                                .url(HttpApi.TASK_DETAIL_ACCOMPLISH)
                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                .addParams("taskId", chatEventMessage.getTaskId() + "")
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
                                        closeProgress();
                                        notifyDataSetChanged();
                                    }
                                });
                    }
                });

//     * 点击取消任务
                holder.getViewId(R.id.item_task_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgress();
                        SCHttpUtils.postWithUserId()
                                .url(HttpApi.TASK_DETAIL_CANCEL)
                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                .addParams("taskId", chatEventMessage.getTaskId() + "")
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
                                        TextView release = holder.getViewId(R.id.task_release_time);
                                        TextView receive = holder.getViewId(R.id.task_receive_time);
                                        TextView finish = holder.getViewId(R.id.task_finish_time);

                                        notifyDataSetChanged();

                                    }
                                });
                    }
                });
                break;
            case 3:


//     * 点击已完成
                holder.getViewId(R.id.item_task_done).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgress();
                        SCHttpUtils.postWithUserId()
                                .url(HttpApi.TASK_DETAIL_DONE)
                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                .addParams("taskId", chatEventMessage.getTaskId() + "")
                                .addParams("userId", SCCacheUtils.getCacheUserId() + "")
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
//                                                        receive.setText("发布时间：");
                                        closeProgress();
                                        notifyDataSetChanged();

                                    }
                                });
                    }
                });

//     * 点击未完成
                holder.getViewId(R.id.item_task_undone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgress();
                        SCHttpUtils.postWithUserId()
                                .url(HttpApi.TASK_DETAIL_UNDONE)
                                .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                .addParams("taskId", chatEventMessage.getTaskId() + "")
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
                return 2;
            } else {
                return 1;
            }
        } else if (status == CANCEL || status == CONFIRM) {
            return 1;
        } else if (status == DONE || status == UNDONE) {
            return 2;
        }
//        else if (status==OVERTIME){
//            return 3;
//        }
        else {
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
                        .addParams("targetId", chatEventMessage.getTaskId() + "")
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
       /* switch (view.getId()) {
            case R.id.btn_event_task:
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(chatEventMessage, view, holder, position);
                }
                break;
            case R.id.item_task_urge:
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(chatEventMessage, view, holder, position);
                }
                break;
            case R.id.item_task_cancel:
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(chatEventMessage, view, holder, position);
                }
                break;
            case R.id.btn_event_confirm:
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(chatEventMessage, view, holder, position);
                }
                break;
            case R.id.item_task_done:
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(chatEventMessage, view, holder, position);
                }
                break;
            case R.id.item_task_undone:
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(chatEventMessage, view, holder, position);
                }
                break;
        }*/

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
