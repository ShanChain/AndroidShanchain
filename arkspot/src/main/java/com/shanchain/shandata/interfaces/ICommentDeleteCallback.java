package com.shanchain.shandata.interfaces;

import com.shanchain.shandata.ui.model.CommentEntity;

/**
 * Created by WealChen
 * Date : 2019/9/2
 * Describe :
 */
public interface ICommentDeleteCallback {
    void deleteComment(CommentEntity commentEntity);
}
