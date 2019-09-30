package com.shanchain.data.common.base;

import com.shanchain.common.BuildConfig;

/**
 * 全局常量
 */

public interface Constants {

    /*************消息类型 ************/
    /**
     * 默认消息类型（闲聊）
     */
    int ATTR_DEFAULT = 0;

    /**
     * 对戏类型
     */
    int ATTR_AGAINST = 1;

    /**
     * 公告
     */
    int ATTR_ANNOUNCEMENT = 2;

    /**
     * 情景
     */
    int ATTR_SCENE = 3;

    /*************消息扩展参数中的key值************/

    /**
     * 消息扩展参数用户头像key值
     */
    String MSG_HEAD_IMG = "headImg";

    /**
     * 消息扩展参数群头像key值
     */
    String MSG_GROUP_IMG = "groupImg";

    /**
     * 消息扩展参数用户昵称key值
     */
    String MSG_NICK_NAME = "nickName";

    /**
     * 消息扩展参数消息类型key值
     */
    String MSG_ATTR = "msgAttr";

    /**
     * 消息扩展参数是否是群信息key值
     */
    String MSG_IS_GROUP = "isGroup";

    /**
     * 消息扩展参数消息中@人的数组
     */
    String MSG_AT_LIST = "msgAtList";

    /**
     * 消息扩展参数中附带的当前角色id
     */
    String MSG_CHARACTER_ID = "characterId";

    /*************本地缓存常量************/
    String CACHE_USER_INFO = "userInfo";
    String CACHE_CUR_USER = "curUser";
    String CACHE_TOKEN = "token";
    String CACHE_SPACE_ID = "spaceId";
    String CACHE_CHARACTER_ID = "characterId";
    String CACHE_SPACE_INFO = "spaceInfo";
    String CACHE_CHARACTER_INFO = "characterInfo";
    String CACHE_HEAD_IMG = "headImg";
    String CACHE_AUTH_CODE = "authCode";
    String TEMPORARY_CODE = "temporaryCode";
    String CACHE_GDATA = "gData";
    String CACHE_SPACE_COLLECTION = "spaceCollection";
    String CACHE_HX_USER_NAME = "hxUserName";
    String CACHE_HX_PWD = "hxPwd";
    String CACHE_ROOM_ID = "roomID";
    String CACHE_DEVICE_TOKEN = "deviceToken";
    String CACHE_DEVICE_TOKEN_STATUS = "deviceTokenStatus";
    String CACHE_APP_ENV = "true";
    String CACHE_USER_MSG = "userMessage";
    String CACHE_USER_MSG_READ_STATUS = "cache_user_msg_read_status";
    String CACHE_USER_MSG_IS_RECEIVE = "isMsgReceive";
    String USER_DEVICE_TOKEN_STATUS = "deviceTokenStatus";


    /*************本地缓存常量************/
    int TYPE_STORY_SHORT = 1;
    int TYPE_STORY_LONG = 2;


    /**************好友类型**************/
    int TYPE_CONTACT_FOCUS = 1;
    int TYPE_CONTACT_FUNS = 2;
    int TYPE_CONTACT_EACH = 3;

    /*************SP中的key值***************/
    String SP_KEY_DRAFT = "draft";
    String SP_KEY_DEVICE_TOKEN_STATUS = "sp_key_device_token_status";
    String SP_KEY_DEVICE_TOKEN = "sp_key_device_token";
    /**
     * config.gradle中定义的常量
     */

    boolean SC_RN_DEBUG = Boolean.valueOf(BuildConfig.RN_DEBUG);
    boolean SC_ENV_PRD = Boolean.valueOf(BuildConfig.ENV_PRD);
    String SC_HOST_TEST = BuildConfig.HOST_TEST;
    String SC_HOST_RELEASE = BuildConfig.HOST_RELEASE;
    String SC_WALLET_RELEASE = BuildConfig.WALLET_RELEASE;
    String SC_WALLET_TEST = BuildConfig.WALLET_TEST;

    /**************群成员配置信息**************/
    int GROUP_OWNER = 0;
    int GROUP_ADMIN = 1;
    int GROUP_MEMBER = 2;

    /**************绑定内容**************/
    String BIND_MOBILE = "BIND_MOBILE";
    String RESET_PASSWORD = "RESET_PASSWORD";

    /**************举报类型**************/
    String REPORT_TYPE_TOPIC = "TOPIC";
    String REPORT_TYPE_STORY = "STORY";
    String REPORT_TYPE_USER = "USER";

    /**************span类型**************/
    int SPAN_TYPE_AT = 1;
    int SPAN_TYPE_TOPIC = 2;

    String SP_KEY_GUIDE = "sp_key_guide";
    String SP_KEY_GUIDE_VIEW = "sp_key_guide_view";//提示引导页
    String SP_KEY_BASE_PARA = "sp_key_base_para";//全局网络地址
    String SP_KEY_BASE_PARA_WALLET = "sp_key_base_para_wallet";//全局钱包地址
    String SP_KEY_BASE_PARA_MONEY = "sp_key_base_para_money";
    String SP_KEY_USER_ACCOUNT = "sp_key_user_account";//用户手机号
    String SP_KEY_NOT_ACOUNT = "sp_key_not_acount";//公告数量

    /**************上传的视频类型**************/
    String VIDEO_UPLOAD_DYNAMIC = "dynamic";
    String VIDEO_UPLOAD_NORMAL = "normal";

    /**************分享相关**************/
    String SHARE_ID_TYPE_STORY = "story";
    String SHARE_ID_TYPE_NOVEL = "novel";
    String SHARE_ID_TYPE_PLAY = "play";
    String SHARE_ID_TYPE_REHEARSE = "rehearse";
    String SHARE_ID_TYPE_SPACE = "space";

    String share_platform_type_weixin = "";

    //加载的数据数量
    int pageSize = 10;

    //下拉刷新
    int pullRefress = 1;
    int pillLoadmore = 2;

    String PAYFOR_MINING_MONEY = "100";

}
