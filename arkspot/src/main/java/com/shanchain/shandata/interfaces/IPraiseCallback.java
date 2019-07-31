package com.shanchain.shandata.interfaces;

import com.shanchain.shandata.ui.model.SqureDataEntity;

/**
 * Created by WealChen
 * Date : 2019/7/31
 * Describe :点赞回调
 */
public interface IPraiseCallback {
    void praiseToArticle(SqureDataEntity item);
}
