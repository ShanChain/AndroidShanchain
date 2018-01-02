package com.shanchain.shandata.ui.view.activity.story.stroyView;

import com.shanchain.shandata.ui.model.DynamicVideoBean;

/**
 * Created by zhoujian on 2018/1/2.
 */

public interface DynamicVideoView {
    void initVideoSuc(DynamicVideoBean videoBean);

    void initFavSuc(boolean isFav);

    void initCharacterSuc(String headImg, String name);
}
