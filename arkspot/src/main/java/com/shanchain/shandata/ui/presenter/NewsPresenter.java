package com.shanchain.shandata.ui.presenter;

import com.hyphenate.chat.EMMessage;
import com.shanchain.shandata.ui.model.MessageHomeInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/11/24.
 */

public interface NewsPresenter {
    void initConversationInfo(List<MessageHomeInfo> sourceDatas);

    void initConversationCache(List<MessageHomeInfo> sourceDatas);

    void updateCache(EMMessage message);
}
