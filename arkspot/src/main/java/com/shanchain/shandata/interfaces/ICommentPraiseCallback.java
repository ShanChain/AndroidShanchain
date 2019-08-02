package com.shanchain.shandata.interfaces;

import com.shanchain.shandata.ui.model.CommentEntity;

/**
 * Created by WealChen
 * Date : 2019/8/1
 * Describe :
 */
public interface ICommentPraiseCallback {
    void praiseToUser(CommentEntity item);
}
