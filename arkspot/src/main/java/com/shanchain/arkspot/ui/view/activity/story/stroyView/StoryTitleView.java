package com.shanchain.arkspot.ui.view.activity.story.stroyView;

import com.shanchain.arkspot.ui.model.SpaceInfo;
import com.shanchain.arkspot.ui.model.TagContentBean;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/13.
 */

public interface StoryTitleView {

    void getTagSuccess(List<TagContentBean> tagList);

    void getSpaceListSuccess(List<SpaceInfo> spaceInfoList,boolean isLast);

    void getMyFavoriteSuccess(List<SpaceInfo> favoriteSpaceList);

    void loadMoreResult();
}
