package cn.jiguang.imui.messages;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.jiguang.imui.messages.ChatEventMessage;

import cn.jiguang.imui.R;
import cn.jiguang.imui.messages.BaseMessageViewHolder;
import cn.jiguang.imui.messages.MessageListStyle;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.model.MyMessage;

public class CustomEvenMsgHolder
        extends BaseMessageViewHolder<MyMessage>
        implements MsgListAdapter.DefaultMessageViewHolder{

    private TextView evenMessageTitle;
    private TextView evenMessageContent;
    private TextView evenMessageLastTime;
    private Button buttonEventTask;
    private TextView evenMessageComment;
    private TextView evenMessageLike;


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
    public void onBind(MyMessage myMessage) {

        ChatEventMessage eventMessage = myMessage.getChatEventMessage();
//        evenMessageTitle.setText(eventMessage.getBounty());
//        evenMessageContent.setText(eventMessage.getIntro());
//        evenMessageLastTime.setText(eventMessage.getLimitedTime());
//        evenMessageComment.setText(eventMessage.getCommentCount());
//        evenMessageLike.setText(eventMessage.getSupportCount());
        //buttonEventTask.setOnClickListener();
    }

    @Override
    public void applyStyle(MessageListStyle style) {

    }
}
