package com.shanchain.shandata.ui.view.activity.mine.view;

import com.shanchain.shandata.ui.model.StoryDetailInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/11/25.
 */

public interface MyStoryView {
    void initStorySuc(List<StoryDetailInfo> storyDetailInfoList, boolean last);

    void supportSuc(boolean suc, int position);

    void supportCancelSuc(boolean suc, int position);
}
