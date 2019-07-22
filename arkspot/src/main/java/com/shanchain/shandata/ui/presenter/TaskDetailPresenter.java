package com.shanchain.shandata.ui.presenter;

/**
 * Created by WealChen
 * Date : 2019/7/20
 * Describe :社区帮列表接口
 */
public interface TaskDetailPresenter {
    void getTaskListData(String characterId,String roomId,int page,int size,int pullType);

}
