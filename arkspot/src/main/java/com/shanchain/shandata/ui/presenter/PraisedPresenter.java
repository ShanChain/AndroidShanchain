package com.shanchain.shandata.ui.presenter;

/**
 * Created by zhoujian on 2017/11/21.
 */

public interface PraisedPresenter {
    void initPraiseData(int page,int size);

    void supportCancel(int storyId, int position);

    void support(int storyId, int position);
}
