package com.shanchain.arkspot.ui.view.activity.story.stroyView;

import com.shanchain.arkspot.ui.model.StoryModelBean;

import java.util.List;

/**
 * Created by zhoujian on 2017/11/17.
 */

public interface StoryChainView {

    void getStoryListSuc(List<StoryModelBean> modelBeanList, boolean isLast);

}
