package com.shanchain.shandata.ui.presenter;


import com.shanchain.shandata.ui.model.MessageHomeInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/11/24.
 */

public interface ChatRoomPresenter {
    void initConversationInfo(List<MessageHomeInfo> sourceDatas,long roomID);

    void initConversationCache(List<MessageHomeInfo> sourceDatas);

//    void updateCache(EMMessage message);
}
