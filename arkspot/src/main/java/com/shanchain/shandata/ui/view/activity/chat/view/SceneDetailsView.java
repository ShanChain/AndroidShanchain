package com.shanchain.shandata.ui.view.activity.chat.view;

import com.shanchain.shandata.ui.model.BdGroupMemberInfo;
import com.shanchain.shandata.ui.model.NoticeBean;
import com.shanchain.shandata.ui.model.SceneDetailData;

import java.util.List;

/**
 * Created by zhoujian on 2017/11/20.
 */

public interface SceneDetailsView {
    void initGroupInfoSuc(SceneDetailData sceneDetailInfo, List<BdGroupMemberInfo> bdInfos);

    void initUserInfo(List<BdGroupMemberInfo> bdInfos);

    void initNoticeSuc(NoticeBean noticeBean);

    void leaveGroupSuc(boolean result);
}
