package com.shanchain.arkspot.ui.view.fragment.view;

import com.shanchain.arkspot.ui.model.StoryInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/13.
 */

public interface AttentionView {

    void initSuccess(List<StoryInfo> storyInfoList);

    void initError(Exception e);

}
