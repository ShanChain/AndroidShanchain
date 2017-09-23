package com.shanchain.arkspot.ui.view.activity.chat.view;

import com.hyphenate.chat.EMMessage;
import com.shanchain.arkspot.ui.model.MsgInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/9/12.
 */

public interface ChatView {
    void onUpDate(List<MsgInfo> emMessageList);

    void onInit(List<MsgInfo> emMessageList);

    void onPullHistory(List<EMMessage> emMessages);
}
