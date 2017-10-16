package com.shanchain.arkspot.ui.view.fragment.view;

import com.shanchain.arkspot.ui.model.StoryListDataBean;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/13.
 */

public interface AttentionView {

    void initSuccess(List<StoryListDataBean> data);

    void initError(Exception e);

}
