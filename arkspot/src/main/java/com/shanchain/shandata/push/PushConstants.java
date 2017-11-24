package com.shanchain.shandata.push;

/**
 * Created by flyye on 2017/11/17.
 * 友盟推送常量表
 */


public class PushConstants {

    //action_type
    public static final String ACTION_TYPE = "action_type";
    public static final String ACTION_TYPE_OPEN_PAGE = "open_page";
    public static final String ACTION_TYPE_RED_POINT = "red_point";
    public static final String ACTION_TYPE_CUSTOM = "custom_action";

    //page_name action_type为open_page时使用
    public static final String PAGE_NAME = "page_name";
    public static final String PAGE_STORY = "story_page";
    public static final String PAGE_CHAT = "chat_page";
    public static final String PAGE_MINE = "mine_page";
    public static final String PAGE_SPACE = "space_page";
    public static final String PAGE_SETTING = "setting_page";
    public static final String PAGE_ROLE = "role_page";
    public static final String PAGE_CHARACTER = "character_page";
    public static final String PAGE_CONTACT = "contact_page";
    public static final String PAGE_NOTIFICATION = "notification_page";

    //where action_type为red_point时使用
    public static final String WHERE_SETTING = "setting";
    public static final String WHERE_STORY_TAB = "story_tab";
    public static final String WHERE_MINE_TAB = "mine_tab";
    public static final String WHERE_SPACE_TAB = "space_tab";
    public static final String WHERE_CHAT_TAB = "chat_tab";
    public static final String WHERE_CHAT_FATE = "chat_fate";
    public static final String WHERE_SPACE_RULE = "space_rule";
    public static final String WHERE_NOTIFICATION = "notification";
    public static final String WHERE_CHAT_MENU = "chat_menu";
    public static final String WHERE_SPACE_MENU = "space_menu";


    //msg_key
    public static final String MSG_WORLDVIEW_ILLEGAL = "MSG_WORLDVIEW_ILLEGAL";//世界观因违规被暂停 消息编号：
    public static final String MSG_CHARACTER_BE_FOCUS = "MSG_CHARACTER_BE_FOCUS";//你的当前人物被@
    public static final String MSG_STORY_BE_COMMENT = "MSG_STORY_BE_COMMENT";//有人评论了我
    public static final String MSG_STORY_BE_PRAISE = "MSG_STORY_BE_PRAISE";//有人赞了我的故事
    public static final String MSG_COMMENT_BE_PRAISE = "MSG_COMMENT_BE_PRAISE";//有人赞了我的评论
    public static final String MSG_DYNAMIC_BE_FORWARD = "MSG_DYNAMIC_BE_FORWARD";//有人转发了我
    public static final String MSG_BE_REPORT = "MSG_BE_REPORT";//我被举报并处理
    public static final String MSG_REPORT_WILL_BE_DEAL_WITH = "MSG_REPORT_WILL_BE_DEAL_WITH";//待处理的举报
    public static final String MSG_FRIEND_RECOMMEND = "MSG_FRIEND_RECOMMEND";//好友推荐
    public static final String MSG_CHARACTER_BE_FOLLOW = "MSG_CHARACTER_BE_FOLLOW";//有人关注了你
    public static final String MSG_BE_FOCUS_AT_CHAT = "MSG_BE_FOCUS_AT_CHAT";//对话场景推荐
    public static final String MSG_APPLY_JOIN_GROUP = "MSG_APPLY_JOIN_GROUP";//有人申请加入场景
    public static final String MSG_APPLY_JOIN_GROUP_OK = "MSG_APPLY_JOIN_GROUP_OK";//申请加入场景通过
    public static final String MSG_BE_INVITATION_JOIN_GROUP = "MSG_BE_INVITATION_JOIN_GROUP";//被拉进对话场景
    public static final String MSG_CHARACTER_CHAT = "MSG_CHARACTER_CHAT";//单人对话消息
    public static final String MSG_GROUP_CHAT = "MSG_GROUP_CHAT";//多人对话消息
    public static final String MSG_BE_FOCUS_AT_GROUP = "MSG_BE_FOCUS_AT_GROUP";//场景中被@
    public static final String MSG_DRAMAS_BE_COMMING = "MSG_DRAMAS_BE_COMMING";//大戏预告
    public static final String MSG_DRAMAS_BE_CANCEL = "MSG_DRAMAS_BE_CANCEL";//大戏取消
    public static final String MSG_DRAMAS_WILL_START = "MSG_DRAMAS_WILL_START";//大戏即将开始
    public static final String MSG_DRAMAS_START = "MSG_DRAMAS_START";//大戏开始
    public static final String MSG_DRAMAS_SOMEONE_SIGN = "MSG_DRAMAS_SOMEONE_SIGN";//大戏中有人签到
    public static final String MSG_DRAMAS_SOMEONE_OUT = "MSG_DRAMAS_SOMEONE_OUT";//大戏中有人签退
    public static final String MSG_DRAMAS_OVER = "MSG_DRAMAS_OVER";//大戏结束
    public static final String MSG_DRAMAS_NEW_NOTICE = "MSG_DRAMAS_NEW_NOTICE";//场景新公告
    public static final String MSG_DRAMAS_CONETNT_UPDATE = "MSG_DRAMAS_CONETNT_UPDATE";//场景信息修改
    public static final String MSG_SOMEONT_JOIN_GROUP = "MSG_SOMEONT_JOIN_GROUP";//有人加入了场景
    public static final String MSG_SOMEONE_OUT_GROUP = "MSG_SOMEONE_OUT_GROUP";//有人离开了场景
    public static final String MSG_SOMEONE_BE_OUT_GROUP = "MSG_SOMEONE_BE_OUT_GROUP";//被请出对话场景
    public static final String MSG_GET_GROUP_MANAGER_RIGHT = "MSG_GET_GROUP_MANAGER_RIGHT";//获得场景管理权限
    public static final String MSG_LOST_GROUP_MANAGER_RIGHT = "MSG_LOST_GROUP_MANAGER_RIGHT";//失去场景管理权限
    public static final String MSG_APP_GUIDE_UPDATE = "MSG_APP_GUIDE_UPDATE";//欢迎和引导内容
    public static final String MSG_GET_SPACE_MANAGER_RIGHT = "MSG_GET_SPACE_MANAGER_RIGHT";//被添加为世界管理员
    public static final String MSG_LOST_SPACE_MANAGER_RIGHT = "MSG_LOST_SPACE_MANAGER_RIGHT";//不再是世界管理员
    public static final String MSG_WORLDVIEW_ = "MSG_WORLDVIEW_";//待审核的新人物
    public static final String MSG_NEW_MODEL_EXAMINE = "MSG_NEW_MODEL_EXAMINE";//新人物审核通过
    public static final String MSG_ACCOUT_BE_UPDATE = "MSG_ACCOUT_BE_UPDATE";//账号绑定信息修改
    public static final String MSG_GLOBLE_NOTICE = "MSG_GLOBLE_NOTICE";//官方手工发布通知
}
