package com.shanchain.shandata.ui.view.activity.mine.view;

import com.shanchain.shandata.ui.model.StoryContentBean;

import java.util.List;

/**
 * Created by zhoujian on 2017/11/21.
 */

public interface PraisedView {
    void initPraisedSuc(List<StoryContentBean> contentBeanList, boolean last);

    void supportSuc(boolean suc, int position);

    void supportCancelSuc(boolean suc, int position);
}
