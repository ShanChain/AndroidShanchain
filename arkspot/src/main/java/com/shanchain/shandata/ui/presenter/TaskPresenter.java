package com.shanchain.shandata.ui.presenter;


/**
 * Created by zhoujian on 2017/11/24.
 */

public interface TaskPresenter {

    void initTask(String characterId, String roomId);    //初始化广场的任务列表

    void notFinishTask(int characterId, String squareId);    //获取广场未完成任务列表

    void releaseTask(String characterId, String roomId, String bounty, String dataString, String time);    //发布任务

    void deleteTask(int taskId);//    删除任务

    void receiveTask(int characterId, String squareId, int taskId);//    领取任务

    void confirmTaskProgress(int status, int characterId, int taskId);//    确认对方完成任务

    void taskProgressNotice(int targetId, int taskId, int type);//    催促完成任务通知

    void queryUserReleaseTask(int characterId);//    查询角色发布任务

    void queryAllTaskByUser(int characterId);//    查询用户的全部任务

    void queryAllFinishTaskByUser(int characterId);//    查询用户完成的全部任务

    void cancelTask(String characterId, String taskId);//    取消发布的任务

    void addTaskComment(String characterId, String taskId, String dataString);//    发布评论
}
