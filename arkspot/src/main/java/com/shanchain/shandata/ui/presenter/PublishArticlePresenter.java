package com.shanchain.shandata.ui.presenter;

import com.shanchain.shandata.ui.model.PhotoBean;

import java.util.List;

/**
 * Created by WealChen
 * Date : 2019/7/25
 * Describe :
 */
public interface PublishArticlePresenter {
    void addArticleNoPictrue(int userId, String title,String content,String listImg);
    void uploadPhotoListToServer(List<PhotoBean> mList);
}
