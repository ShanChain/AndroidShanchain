package com.shanchain.shandata.ui.view.fragment.view;

import com.shanchain.shandata.ui.model.NewsCharacterBean;
import com.shanchain.shandata.ui.model.NewsGroupBean;

import java.util.List;

/**
 * Created by zhoujian on 2017/11/24.
 */

public interface NewsView {
    void initGroupInfoSuc(List<NewsGroupBean> newsGroupBeanList);

    void initCharacterSuc(List<NewsCharacterBean> characterBeanList);
}
