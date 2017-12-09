package com.shanchain.shandata.ui.view.activity.mine.view;

import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.StoryBeanModel;

import java.util.List;

/**
 * Created by zhoujian on 2017/11/14.
 */

public interface FriendHomeView {

    void initFriendSuc(CharacterInfo characterInfo,boolean isFocus);

    void focusSuc(boolean focusSuc);

    void getStoryInfoSuc(List<StoryBeanModel> list, Boolean isLast);

    void getHxSuc(String hxUserName);

    void supportSuccess(boolean suc, int position);

    void supportCancelSuccess(boolean suc, int position);

    void focusCancelSuc(boolean suc);

    void initSpaceSuc(String name);
}
