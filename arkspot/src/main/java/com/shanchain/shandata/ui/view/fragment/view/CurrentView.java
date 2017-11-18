package com.shanchain.shandata.ui.view.fragment.view;

import com.shanchain.shandata.ui.model.StoryBeanModel;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/17.
 */

public interface CurrentView {

    void initSuccess(List<StoryBeanModel> list,boolean isLast);

    void supportSuccess(boolean isSuccess,int position);

    void supportCancelSuccess(boolean isSuccess,int position);
}
