package com.shanchain.arkspot.ui.view.activity.story.stroyView;

import com.shanchain.arkspot.ui.model.FavoriteSpaceBean;
import com.shanchain.arkspot.ui.model.SpaceBean;
import com.shanchain.arkspot.ui.model.TagContentBean;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/13.
 */

public interface StoryTitleView {

    void getTagSuccess(List<TagContentBean> tagList);

    void getSpaceListSuccess(List<SpaceBean> spaceBeanList );

    void getMyFavoriteSuccess(List<FavoriteSpaceBean> favoriteSpaceList);

}
