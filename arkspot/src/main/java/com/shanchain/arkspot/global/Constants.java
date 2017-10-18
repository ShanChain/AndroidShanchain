package com.shanchain.arkspot.global;

/**
 * 全局常量
 */

public interface Constants {

        /*************消息类型 ************/
        //默认消息类型（闲聊）
        int ATTR_DEFAULT = 0;

        //对戏类型
        int ATTR_AGAINST = 1;

        //公告
        int ATTR_ANNOUNCEMENT = 2;

        //情景
        int ATTR_SCENE = 3;

        /*************消息扩展参数中的key值************/

        //消息扩展参数用户头像key值
        String MSG_HEAD_IMG = "headImg";

        //消息扩展参数群头像key值
        String MSG_GROUP_IMG = "groupImg";

        //消息扩展参数用户昵称key值
        String MSG_NICK_NAME = "nickName";

        //消息扩展参数消息类型key值
        String MSG_ATTR = "msgAttr";

        //消息扩展参数是否是群信息key值
        String MSG_IS_GROUP = "isGroup";


        /*************阿里云文件上传常量************/
        String OSS_ENDPOINT = "";
        String OSS_HOST = "";
        String OSS_BUCKET = "";


        /*************本地缓存常量************/
        String CACHE_USER_INFO = "userInfo";
        String CACHE_CUR_USER = "curUser";
        String CACHE_TOKEN = "token";
        String CACHE_SPACE_ID = "spaceId";
        String CACHE_CHARACTER_ID = "characterId";

}
