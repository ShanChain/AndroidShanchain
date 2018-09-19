package com.shanchain.shandata.ui.view.activity.story.stroyView;


import com.shanchain.shandata.ui.model.ResponseTopicContentBean;
import com.shanchain.shandata.ui.model.StoryBeanModel;

import java.util.List;

/**
 * Created by zhoujian on 2017/11/6.
 */

public interface TopicDetailView {

    void initTopicInfo(ResponseTopicContentBean topicInfo);

    void initSuccess(List<StoryBeanModel> list, boolean isLast);

    void supportSuccess(boolean suc, int position);

    void supportCancelSuccess(boolean suc, int position);
}
