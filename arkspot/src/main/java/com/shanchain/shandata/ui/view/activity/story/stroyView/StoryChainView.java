package com.shanchain.shandata.ui.view.activity.story.stroyView;

import com.shanchain.shandata.ui.model.StoryChainModel;

import java.util.List;

/**
 * Created by zhoujian on 2017/11/17.
 */

public interface StoryChainView {

    void getStoryListSuc(List<StoryChainModel> modelBeanList, boolean isLast);

}
