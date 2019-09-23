package com.shanchain.shandata.interfaces;

import com.shanchain.shandata.ui.model.CommentEntity;

/**
 * Created by WealChen
 * Date : 2019/9/21
 * Describe :
 */
public interface IReplyCommentCallback {
    void replyComment(CommentEntity commentEntity);
}
