package com.shanchain.shandata.ui.view.fragment.view;

import com.shanchain.shandata.ui.model.StoryBeanModel;

import java.util.List;

import cn.jiguang.imui.model.ChatEventMessage;
import cn.jiguang.imui.model.MyMessage;

public interface TaskView {

    void initTask(List<ChatEventMessage> list, boolean isSuccess);

    void initUserTaskList(List<ChatEventMessage> list, boolean isSuccess);

    void  addSuccess(boolean success);

    void releaseTaskView(boolean isSuccess);

    void supportSuccess(boolean isSuccess,int position);

    void supportCancelSuccess(boolean isSuccess,int position);

    void deleteTaskView(boolean isSuccess,int position);
}
