package com.shanchain.shandata.ui.view.activity.story.stroyView;


import com.shanchain.shandata.ui.model.StoryDetailInfo;
import com.shanchain.shandata.ui.model.BdCommentBean;


import java.util.List;

/**
 * Created by zhoujian on 2017/11/14.
 */

public interface DynamicDetailView {
    void commentSuccess(List<BdCommentBean> commentBeanList, boolean isLast);

    void addSuccess(boolean success);

    void deleteSuccess(boolean success,int position);

    void supportSuc(boolean suc);

    void supportCancelSuc(boolean suc);

    void initNovelSuc(StoryDetailInfo storyDetailInfo);

    void commentSupportSuc(boolean suc, int position);

    void commentSupportCancelSuc(boolean suc, int position);
}
