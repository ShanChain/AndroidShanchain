package com.shanchain.arkspot.ui.view.activity.story.stroyView;

import com.shanchain.arkspot.ui.model.StoryBeanModel;

import java.util.List;

/**
 * Created by zhoujian on 2017/11/6.
 */

public interface TopicDetailView {


    void initSuccess(List<StoryBeanModel> list, boolean isLast);

    void supportSuccess(boolean suc, int position);

    void supportCancelSuccess(boolean suc, int position);
}
