package cn.jiguang.imui.messages;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jiguang.imui.R;
import cn.jiguang.imui.model.ChatEventMessage;

public class CustomEvenMsgHolder
        extends BaseMessageViewHolder<ChatEventMessage>
        implements MsgListAdapter.DefaultMessageViewHolder,View.OnClickListener {

    private TextView evenMessageTitle;
    private TextView evenMessageContent;
    private TextView evenMessageLastTime;
    private Button buttonEventTask;
    private TextView evenMessageComment;
    private TextView evenMessageLike;
    private LinearLayout linearLayout;
//    private MsgListAdapter.OnBtnEventTaskClickListener onBtnEventTaskClickListener;
//    private MsgListAdapter.OnTvEventLikeClickListener onTvEventLikeClickListener;
//    private MsgListAdapter.OnTvEventCommentClickListener onTvEventCommentClickListener;

    private int btnTaskID = R.id.btn_event_task;
    private int tvCommentID = R.id.item_tv_comment;
    private int tvLikeID = R.id.item_tv_like;


    public CustomEvenMsgHolder(View itemView,boolean isSender) {
        super(itemView);
        evenMessageTitle = itemView.findViewById(R.id.even_message_bounty);
        evenMessageContent = itemView.findViewById(R.id.even_message_content);
        evenMessageLastTime = itemView.findViewById(R.id.even_message_last_time);
        buttonEventTask = itemView.findViewById(R.id.btn_event_task);
        evenMessageComment = itemView.findViewById(R.id.item_tv_comment);
        evenMessageLike = itemView.findViewById(R.id.item_tv_like);

    }

    @Override
    public void onBind(ChatEventMessage myMessage) {
        ChatEventMessage eventMessage = (ChatEventMessage) myMessage;
        if (eventMessage != null){
        evenMessageTitle.setText("赏金: "+eventMessage.getBounty()+" SEAT");
        evenMessageContent.setText(eventMessage.getIntro()+"");
        evenMessageLastTime.setText("完成时限："+eventMessage.getLimitedTime());
        evenMessageComment.setText(eventMessage.getCommentCount()+"");
        evenMessageLike.setText(eventMessage.getSupportCount()+"");

        buttonEventTask.setOnClickListener(this);
        evenMessageComment.setOnClickListener(this);
        evenMessageLike.setOnClickListener(this);
        }
    }

    @Override
    public void applyStyle(MessageListStyle style) {
//        linearLayout.setPadding(style.getEventPaddingLeft(), style.getEventPaddingTop(),
//                style.getEventPaddingRight(), style.getEventPaddingBottom());
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_event_task) {
           // Log.d("###############","点击领取任务按钮事件");
            if (onBtnEventTaskClickListener!=null){
                onBtnEventTaskClickListener.TaskEventMessageClick();
            }
        }else if (i == R.id.item_tv_like){
            if (onTvEventLikeClickListener!=null){
                onTvEventLikeClickListener.LikeEventMessageClick();
            }
        } else if (i == R.id.item_tv_comment){
            if (onTvEventCommentClickListener!=null){
                onTvEventCommentClickListener.CommentEventMessageClick();
            }
        }
    }
}
