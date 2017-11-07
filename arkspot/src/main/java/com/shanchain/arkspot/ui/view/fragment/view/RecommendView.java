package com.shanchain.arkspot.ui.view.fragment.view;

import com.shanchain.arkspot.ui.model.StoryBeanModel;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/17.
 */

public interface RecommendView {
    void initSuccess(List<StoryBeanModel> list, boolean isLast);
}
