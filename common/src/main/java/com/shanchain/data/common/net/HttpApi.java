package com.shanchain.data.common.net;

import com.shanchain.data.common.base.Constants;

/**
 * Created by zhoujian on 2017/9/18.
 */


public interface HttpApi {

    /**
     * IM相关接口
     */
    String BASE_URL_IM = Constants.SC_ENV_PRD ? Constants.SC_HOST_RELEASE : Constants.SC_HOST_TEST;
//    String BASE_URL_IM = "http://95.169.24.11:8082"; //本地测试环境IM相关接口
//    String BASE_URL_IM = "http://api.qianqianshijie.com"; //生产环境IM相关接口
//    String BASE_URL_IM = "http://47.100.20.170";


    /**
     * 全局接口地址
     */
    String BASE_URL = Constants.SC_ENV_PRD ? Constants.SC_HOST_RELEASE : Constants.SC_HOST_TEST;
//    String BASE_URL = "http://95.169.24.11:8081"; //本地测试环境全局接口地址
//    String BASE_URL = "http://api.qianqianshijie.com"; //生产环境全局接口地址
//    String BASE_URL = "http://47.100.20.170";

    /**
     * 注册环信聊天用户
     */
    String HX_USER_REGIST = BASE_URL_IM + "/hx/user/regist";

    //超级会员
    String SUPER_USER = BASE_URL_IM + "/web/api/sys/isSuper";

    /**
     * 添加好友
     */
    String HX_USER_ADD_FRIEND = BASE_URL_IM + "/hx/user/addFriend";

    /**
     * 删除好友
     */
    String HX_USER_DEL_FRIEND = BASE_URL_IM + "/hx/user/delFriend";

    /**
     * 创建群组
     */
    String HX_GROUP_CREATE = BASE_URL_IM + "/hx/group/create";

    /**
     * 修改群信息
     */
    String HX_GROUP_MODIFY = BASE_URL_IM + "/hx/group/modify";

    /**
     * 删除群
     */
    String HX_GROUP_DEL = BASE_URL_IM + "/hx/group/delete";

    /**
     * 变换群主
     */
    String HX_GROUP_OWNER = BASE_URL_IM + "/hx/group/owner";

    /**
     * 批量添加群成员
     */
    String HX_GROUP_ADD_MEMBERS = BASE_URL_IM + "/hx/group/addMembers";

    /**
     * 批量移除群成员
     */
    String HX_GROUP_REMOVE_MEMBERS = BASE_URL_IM + "/hx/group/rmMembers";


    /**
     * 查找我关注的列表
     */
    String FOCUS_QUERY = BASE_URL + "/v1/focus/query";


    /**
     * 查看群信息
     */
    String HX_GROUP_QUERY = BASE_URL_IM + "/hx/group/query";

    /**
     * 获取群公告
     */
    String HX_GROUP_GET_NOTICE = BASE_URL_IM + "/hx/group/notice/get";

    /**
     * 通过环信id（username）获取用户详情
     */
    String HX_USER_DETAIL = BASE_URL_IM + "/hx/user/detail";

    /**
     * 骚杨服务器跟地址
     */
    String url = "http://192.168.0.110:8080";


    /**
     * 获取上传图片的信息
     */
    String UP_LOAD_FILE = BASE_URL + "/oss/upload/app";
//    String UP_LOAD_FILE = "http://95.169.24.11:8083/oss/upload/app";


    /**
     * 查询标签
     */
    String TAG_QUERY = BASE_URL + "/v1/tag/query";

    /**
     * 获取热门标签
     */
    String TAG_QUERY_HOT = BASE_URL + "/v1/tag/query/hot";

    /**
     * 获取我收藏的时空
     */
    String SPACE_LIST_FAVORITE = BASE_URL + "/v1/space/list/favorite";

    /**
     * 查找时空--根据ID
     */
    String SPACE_LIST = BASE_URL + "/v1/space/list/spaceId";

    /**
     * 发布动态
     */
    String STORY_ADD = BASE_URL + "/v1/story/add";
    /**
     * 删除动态
     */
    String STORY_DELETE = BASE_URL + "/v1/story/delete";

    /**
     * 删除评论
     */
    String DELETE_MINE_COMMENT = BASE_URL + "/v1/storyComment/delete";

    /**
     * 第三方登录
     */
//    String USER_THIRD_LOGIN = BASE_URL + "/v1/user/third_login";
    String USER_THIRD_LOGIN = BASE_URL + "/v1/2.0/user/third_login";

    /**
     * 注册账号
     */
    String USER_REGISTER = BASE_URL + "/v1/user/register";

    /**
     * 未登录状态获取短信验证码
     */
    String SMS_UNLOGIN_VERIFYCODE = BASE_URL + "/v1/sms/unlogin/verifycode";

    /**
     * 获取验证码,三方账号绑定手机号的时候
     */
    String SMS_BIND_UNLOGIN_VERIFYCODE = BASE_URL + "/v1/2.0/sms/login/verifycode";

    /**
     * 短信验证码登录
     */
    String SMS_LOGIN = BASE_URL + "/v1/2.0/user/sms_login";

    /**
     * 登录
     */
    String USER_LOGIN = BASE_URL + "/v1/user/login";

    /**
     * 重置密码
     */
    String RESET_PWD = BASE_URL + "/v1/user/reset_password";

    /**
     * 获取主页关注故事列表
     */
    String STORY_FOCUS_GET = BASE_URL + "/v1/story/focus/get";

    /**
     * 获取实时故事id列表
     */
    String STORY_RECOMMEND_HOT = BASE_URL + "/v1/recommend/hot";

    /**
     * 获取故事详情列表
     */
    String STORY_RECOMMEND_DETAIL = BASE_URL + "/v1/recommend/detail";

    /**
     * 通过角色id查询角色详细信息
     */
    String CHARACTER_QUERY = BASE_URL + "/v1/character/query";

    /**
     * 通过世界id查找世界详情
     */
    String SPACE_LIST_SPACEID = BASE_URL + "/v1/space/list/spaceId";

    /**
     * 给微博点赞
     */
    String STORY_SUPPORT_ADD = BASE_URL + "/v1/story/support/add";

    /**
     * 一个空间内的联系人
     */
    String SPACE_CONTACT_LIST = BASE_URL + "/v1/character/model/list/spaceId";

    /**
     * 获取评论列表
     */
    String COMMENT_QUERY = BASE_URL + "/v1/storyComment/query";

    /**
     * 获取角色简要信息
     */
    String CHARACTER_BRIEF = BASE_URL + "/v1/character/query/brief";
//    String CHARACTER_BRIEF = "http://95.169.24.11:8081" + "/v1/character/query/brief";


    /**
     * 新建评论
     */
    String STORY_COMMENT_ADD = BASE_URL + "/v1/storyComment/add";

    /**
     * 查找时空人物模型
     */
    String CHARACTER_MODEL_QUERY_SPACEID = BASE_URL + "/v1/character/model/query/spaceId";

    /**
     * 添加人物模型（添加人物）
     */
    String CHARACTER_MODEL_CREATE = BASE_URL + "/v1/character/model/create";

    /**
     * 创建时空
     */
    String SPACE_CREAT = BASE_URL + "/v1/space/create";

    /**
     * 收藏时空
     */
    String SPACE_FAVORITE = BASE_URL + "/v1/space/favorite";

    /**
     * 取消收藏时空
     */
    String SPACE_UNFAVORITE = BASE_URL + "/v1/space/unFavorite";

    /**
     * 切换角色
     */
    String CHARACTER_CHANGE = BASE_URL + "/v1/character/change";

    /**
     * 获取时空的话题列表
     */
    String TOPIC_QUERY_SPACEID = BASE_URL + "/v1/topic/query/spaceId";

    /**
     * 获取当前角色
     */
    String CHARACTER_GET_CURRENT = BASE_URL + "/v1/character/get/current";

    /**
     * 获取我的评论
     */
    String COMMENT_LIST_MINE = BASE_URL + "/v1/storyComment/list/mine";

    /**
     * 通过故事id列表获取故事
     */
    String STORY_GET = BASE_URL + "/v1/story/get";

    /**
     * 添加角色关注
     */
    String FOCUS_FOCUS = BASE_URL + "/v1/focus/focus";

    /**
     * 根据cahracterId查找环信联系人
     */
    String HX_USER_LIST = BASE_URL_IM + "/hx/user/list";

    /**
     * 查找我所在的群
     */
    String HX_GROUP_MYGROUP = BASE_URL_IM + "/hx/group/myGroup";

    /**
     * 创建大戏
     */
    String HX_DRAMA_CREATE = BASE_URL_IM + "/hx/game/create";

    /**
     * 获取角色联系人列表（互相关注带字母）
     */
    String FOCUS_CONTACTS = BASE_URL + "/v1/focus/contacts";

    /**
     * 获取群成员
     */
    String HX_GROUP_MEMBERS = BASE_URL_IM + "/hx/group/members";

    /**
     * 创建话题
     */
    String TOPIC_CREATE = BASE_URL + "/v1/topic/create";

    /**
     * 给微博取消点赞
     */
    String STORY_SUPPORT_CANCEL = BASE_URL + "/v1/story/support/remove";

    /**
     * 获取关注列表
     */
    String STORY_RECOMMEND_FOCUS = BASE_URL + "/v1/recommend/focus";

    /**
     * 通过故事id获取单个故事内容
     */
    String STORY_GET_BY_ID = BASE_URL + "/v1/story/get/id";

    /**
     * 查找时空--根据id
     */
    String SPACE_GET_ID = BASE_URL + "/v1/space/get/id";

    /**
     * 推荐列表
     */
    String RECOMMEND_RATE = BASE_URL + "/v1/recommend/rate";

    /**
     * 获取所有时空
     */
    String SPACE_LIST_ALL = BASE_URL + "/v1/space/list/all";

    /**
     * 判断角色是否关注-通过id
     */
    String FOCUS_IS_FAV = BASE_URL + "/v1/focus/isFav";

    /**
     * 获取角色动态接口
     */
    String DYNAMIC_CHARACTER = BASE_URL + "/v1/dynamic/character";

    /**
     * 取消角色关注
     */
    String FOCUS_UNFOCUS = BASE_URL + "/v1/focus/unfocus";

    /**
     * 获取话题动态列表
     */
    String DYNAMIC_TOPIC = BASE_URL + "/v1/dynamic/topic";

    /**
     * 转发微博
     */
    String STORY_TRANSPOND = BASE_URL + "/v1/story/transpond";

    /**
     * 获取故事链
     */
    String STORY_CHAIN_ID = BASE_URL + "/v1/story/chain/id";

    /**
     * 判断是否收藏
     */
    String SPACE_IS_FAV = BASE_URL + "/v1/space/isFav";

    /**
     * 获取角色点赞列表
     */
    String STORY_SUPPORT_LIST = BASE_URL + "/v1/story/support/list";

    /**
     * 查询我的故事
     */
    String STORY_QUERY_MINE = BASE_URL + "/v1/story/query/mine";

    /**
     * 查找时空根据名字 模糊匹配
     */
    String SPACE_LIST_NAME = BASE_URL + "/v1/space/list/name";

    /**
     * 设置deviceToken
     */
    String SET_DEVICE_TOKEN = BASE_URL + "/v1/user/deviceToken";

    /**
     * 绑定其他账号
     */
    String BIND_OTHER_ACCOUNT = BASE_URL + "/v1/user/bind_other_account";

    /**
     * 获取最新apk信息
     */
    String OSS_APK_GET_LASTEST = BASE_URL + "/oss/apk/get/latest";

//    String OSS_APK_GET_LASTEST = "http://95.169.24.11:8083" + "/oss/apk/get/latest";

    /**
     * 判断故事是否点赞 -idList
     */
    String STORY_ISFAV_LIST = BASE_URL + "/v1/story/isFav/list";

    /**
     * 添加公告
     */
    String SPACE_ANNO_CREATE = BASE_URL + "/v1/spaceAnno/create";

    /**
     * 给评论点赞
     */
    String STORY_COMMENT_SUPPORT = BASE_URL + "/v1/storyComment/support/add";

    /**
     * 给评论取消点赞
     */
    String STORY_COMMENT_SUPPORT_CANCEL = BASE_URL + "/v1/storyComment/support/remove";

    /**
     * 根据环信用户名批量查询用户信息
     */
    String HX_USER_USERNAME_LIST = BASE_URL_IM + "/hx/user/hxUserName/list";
//    String HX_USER_USERNAME_LIST = BASE_URL_IM + "/hx/user/list";

    /**
     * 通过环信groupId批量查询群信息
     */
    String HX_GROUP_LIST = BASE_URL_IM + "/hx/group/list";

    /**
     * 判断故事是否点赞
     */
    String STORY_IS_FAV = BASE_URL + "/v1/story/isFav";

    /**
     * 通过Id查找话题
     */
    String TOPIC_QUERY = BASE_URL + "/v1/topic/query";

    /**
     * 获取话题-通过id
     */
    String TOPIC_QUERY_ID = BASE_URL + "/v1/topic/query/id";

    /**
     * 获取时空简要信息  通过id数组
     */
    String SPACE_QUERY_BRIEF = BASE_URL + "/v1/space/query/brief";

    /**
     * 获取时空简要信息  通过id
     */
    String SPACE_BRIEF_ID = BASE_URL + "/v1/space/query/brief/id";

    /**
     * 环信用户查询
     */
    String HX_USER_QUERY = BASE_URL_IM + "/hx/user/query";

    /**
     * 阿里云视频上传
     */
    String OSS_VIDEO_UPLOAD = BASE_URL + "/oss/video/uploadAuth/app";
//    String OSS_VIDEO_UPLOAD = "http://95.169.24.11:8083" + "/oss/video/uploadAuth/app";

    /**
     * 添加演绎动态
     */
    String PLAY_ADD = BASE_URL + "/v1/play/add";

    /**
     * 获取分享信息
     */
    String SHARE_SAVE = BASE_URL + "/v1/share/save";
    /**
     * 获取演绎详情
     */
    String PLAY_GET_ID = BASE_URL + "/v1/play/get/id";
    /*
     * 修改角色信息
     * */
    String MODIFY_CHARACTER = BASE_URL + "/v1/character/modify";
    /**
     * 附近房间信息
     */
    String CHAT_ROOM_COORDINATE = BASE_URL + "/v1/lbs/coordinate/infos";
//    String CHAT_ROOM_COORDINATE = "http://95.169.24.11:8081" + "/v1/lbs/coordinate/infos";
    /**
     * 获取聊天室信息
     */
    String CHAT_ROOM_INFO = BASE_URL + "/v1/lbs/coordinate/info";
    /* 获取绘制方块坐标 */
    String CUBE_INFO = BASE_URL + "/v1/lbs/hotChatRoom/coordinateInfo";
    //    String CHAT_ROOM_INFO = "http://95.169.24.11:8081" + "/v1/lbs/coordinate/info";
    /* 获取聊天室历史消息*/
    String CHAT_ROOM_HISTORY_MESSAGE = BASE_URL + "/jm/room/message";
    /* 获取聊天室成员*/
    String CHAT_ROOM_MEMBER = BASE_URL + "/jm/room/RoomMembers";
    /* 更新极光用户信息 */
    String CHAT_USER_UPDATE = BASE_URL + "/jm/user/update";
    /* 更新首页热门元社区列表 */
    String CHAT_ROOM_LIST_UPDATE = BASE_URL + "/v1/2.0/hotChatRoom/updateList";
    //    String CHAT_ROOM_HISTORY_MESSAGE = "http://95.169.24.11:8082" + "/jm/room/message";
//    String CHAT_ROOM_HISTORY_MESSAGE = "http://67.218.159.56:8082" + "/jm/room/message";
    /* 查询广场上的所有任务列表 */
    String GROUP_TASK_LIST = BASE_URL + "/v1/task/roomtask/list";
    /* 查询广场上的所有任务列表 */
    String ALL_TASK_LIST = BASE_URL + "/v1/task/totaltask/list";
    //    String GROUP_TASK_LIST = "http://95.169.24.11:8081" + "/v1/task/roomtask/list";
    /* 查询用户的所有任务列表 */
    String USER_TASK_LIST = BASE_URL + "/v1/task/individual/list";
    /* 我帮过的 */
    String MY_TASK_RECEIVE_LIST = BASE_URL + "/v1/task/receivetask/list";
    /* 帮过我的 */
    String MY_TASK_PUBLISH_LIST = BASE_URL + "/v1/task/publishtask/list";
    //    String USER_TASK_LIST = "http://95.169.24.11:8081" + "/v1/task/individual/list";
    /* 添加任务 */
    String CHAT_TASK_ADD = BASE_URL + "/v1/task/add";
    //    String CHAT_TASK_ADD = "http://95.169.24.11:8081" + "/v1/task/add";
    /* 领取任务 */
    String TASK_DETAIL_RECEIVE = BASE_URL + "/v1/task/receive";
    //    String TASK_DETAIL_RECEIVE = "http://95.169.24.11:8081" + "/v1/task/receive";
    /* 添加聊天室成员 */
    String CHAT_ROOM_MEMBERS = BASE_URL + "/jm/room/addMembers";
    //    String CHAT_ROOM_MEMBERS = "http://95.169.24.11:8082" + "/jm/room/addMembers";
    /* 添加任务评论 */
    String TASK_COMMENT_ADD = BASE_URL + "/v1/taskComment/add";
    //    String TASK_COMMENT_ADD = "http://95.169.24.11:8081" + "/v1/taskComment/add";
    /* 查询所有评论 */
    String TASK_COMMENT_QUERY = BASE_URL + "/v1/taskComment/query";
    //    String TASK_COMMENT_QUERY= "http://95.169.24.11:8081" + "/v1/taskComment/query";
    /* 查询所有领取的任务 */
//    String TASK_DETAIL_RECEIVE_LIST= "http://95.169.24.11:8081" + "/v1/task/receivetask/list";
    String TASK_DETAIL_RECEIVE_LIST = BASE_URL + "/v1/task/receivetask/list";
    /* 查询未领取的任务 */
    String TASK_DETAIL_UNACCALIMED_LIST = BASE_URL + "/v1/task/unaccalimed/list";
    /* 催促/完成的任务 */
    String TASK_DETAIL_URGE = BASE_URL + "/v1/task/urge";
    /* 发布者确认完成的任务 */
    String TASK_DETAIL_DONE = BASE_URL + "/v1/task/confirm/complete";
    /* 发布者确认未完成的任务 */
    String TASK_DETAIL_UNDONE = BASE_URL + "/v1/task/confirm/undone";
    /* 任务详情 */
    String TASK_DETAIL = BASE_URL + "/v1/task/detail";
    /* 取消任务 */
    String TASK_DETAIL_CANCEL = BASE_URL + "/v1/task/cancel";
    /* 领取方确认完成 */
    String TASK_DETAIL_ACCOMPLISH = BASE_URL + "/v1/task/receive/accomplish";
    /* 获取当前汇率 */
    String GET_SEAT_CURRENCY = BASE_URL + "/web/api/wallet/seat/currency";
    /* 用户反馈 */
    String USE_FEEDBACK = BASE_URL + "/v1/feedback/user";
    /* 举报用户 */
    String USE_REPORT_CREATE = BASE_URL + "/v1/user/report/create";
    /* 好友关注 */
    String USE_FOCUS = BASE_URL + "/v1/focus/focus";
    //官网app下载
    String APP_HOME = "https://marjar.app";
    //用户协议
    String USER_RULE = "http://h5.qianqianshijie.com/agreement";
    //隐私政策
    String PRIVACY_POLICY = "http://h5.qianqianshijie.com/privacy";
    /* 钱包地址 */
//    String SEAT_WALLET = "http://m.qianqianshijie.com/wallet";//测试
                String SEAT_WALLET = "http://h5.qianqianshijie.com/wallet";
    /* 获取钱包用户名密码 */
    String WALLET_GET_USE_PASSWORD = BASE_URL + "/wallet/api/wallet/2.0/hideInfo";
    /* 验证钱包密码 */
    String WALLET_CHECK_USE_PASSWORD = BASE_URL + "/wallet/api/wallet/2.0/checkInfo";
    /* 设置免密登录 */
    String WALLET_FREE_PASSWORD = BASE_URL + "/web/api/sys/needBindPwd";
    /* 用户已保存密码 */
    String WALLET_SAVE_PASSWORD = BASE_URL + "/wallet/api/wallet/2.0/savePwd?token=";
    /* 绑定手机设备号*/
    String WALLET_BIND_PHONE_IMEI = BASE_URL + "/wallet/api/wallet/2.0/bind?token=";
    //查询钱包信息
    String WALLET_INFO = BASE_URL + "/web/api/wallet/info";
    //进入聊天室支付金额
    String PAY_FOR_ARS = BASE_URL + "/wallet/api/wallet/2.0/payARS";
    /* 实名认证 */
    String VERIFIED = BASE_URL + "/v1/2.0/Verified";
    /* 是否实名 */
    String IS_REAL_NAME = BASE_URL + "/v1/2.0/Verified/isRealName";
    /* 修改绑定手机号 */
    String CHANGE_PHONE = BASE_URL + "/v1/2.0/user/change_phone";

    String RESET_PASSWORD = BASE_URL + "/v1/2.0/user/reset_password";
    /* 获取短信验证码 */
    String SETTING_GET_VERIFY_CODE = BASE_URL + "/v1/2.0/sms/setting/verifycode";
    /* 实名认证详情 */
    String VERIFIED_DETAILS = BASE_URL + "/v1/2.0/Verified/detail";
    /* 查询绑定第三方账号的信息*/
    String USER_BOUND = BASE_URL + "/v1/user/bound";
    /* 绑定/解绑第三方账号*/
    String BAND_THIRD_PLATFORM = BASE_URL + "/v1/2.0/user/bind_other_account";
    /* 跨年活动信息 */
    String LIGHT_ACTIVE = BASE_URL + "/v1/2.0/light/active";
    /* 活动闯关 */
    String LIGHT_RUSH = BASE_URL + "/v1/2.0/light/rush";
    /* 通用分享 */
    String SHARE_COMMON = BASE_URL + "/v1/2.0/share/commonShare";
    /* 分享元社区 */
    String SHARE_CHAT_ROOM = BASE_URL + "/v1/2.0/share/shareRoom";
    /* 热门元社区 */
    String HOT_CHAT_ROOM = BASE_URL + "/web/api/sys/hotChatRoom";
    //ARS聊天室
    String ARS_ROOM_LIST = BASE_URL + "/v1/2.0/arsRoomList";
    /* 搜索房间 */
    String SEARCH_ROOM = BASE_URL + "/v1/2.0/hotChatRoom/search";
    /* 是否为聊天室创建人员 */
    String ROOM_OWNER = BASE_URL + "/jm/room/ownerVerify";
    /* 删除聊天室成员 */
    String DELETE_ROOM_MEMBERS = BASE_URL + "/jm/room/rmMembers";
    /* 聊天室黑名单 */
    String ROOM_BLACK_LIST = BASE_URL + "/jm/room/blackMember/list";
    /* 判断是否聊天室黑名单成员 */
    String IS_BLACK_MEMBER = BASE_URL + "/jm/room/blackMember/authenticate";
    /* 添加社区 */
    String ADD_HOT_ROOM = BASE_URL + "/v1/2.0/hotChatRoom/add";
    /* 福包列表 */
    String RED_PAPER_LIST = BASE_URL + "/v1/2.0/redPaper/obtainList";
    /* 领取福包 */
    String RED_PAPER_RECEIVE = BASE_URL + "/v1/2.0/redPaper/appReceive";
    /* 卡劵接口 */
    /* 获取主页卡券列表 */
    String COUPONS_LIST = BASE_URL + "/wallet/api/coupons/vendor/list";
    /* 获取全部卡劵列表 */
    String COUPONS_ALL_LIST = BASE_URL + "/wallet/api/coupons/vendor/listAll";
    /* 创建卡券 */
    String COUPONS_CREATE = BASE_URL + "/wallet/api/coupons/vendor/create";
    /* 获取用户已发布的卡券列表 */
    String COUPONS_CREATE_LIST = BASE_URL + "/wallet/api/coupons/vendor/createList";
    /* 获取用户已领取的子卡券列表 和 获取子卡劵*/
    String COUPONS_RECEIVER_LIST = BASE_URL + "/wallet/api/coupons/client/getList";
    /* 创建方获取主卡劵详情 */
    String MY_CREATE_COUPONS_INFO = BASE_URL + "/wallet/api/coupons/vendor/Get";
    /* 子卡劵详情 */
    String SUB_COUPONS_INFO = BASE_URL + "/wallet/api/coupons/client/Get";
    /* 判断符号是否被占用 */
    String COUPON_CODE_CHECK = BASE_URL + "/wallet/api/coupons/vendor/check";
    /* 领取卡劵 */
    String COUPON_GET_COUPONS = BASE_URL + "/wallet/api/coupons/client/getCoupons";
    /* 创建卡劵详情 领取人员列表 */
    String COUPON_CLIENT_LIST = BASE_URL + "/wallet/api/coupons/vendor/clientList";
    /* 获取子卡劵的核销凭证 */
    String COUPON_CHECK_TOKEN = BASE_URL + "/wallet/api/coupons/token";
    /* 核销子卡劵 */
    String COUPON_CLIENT_USE = BASE_URL + "/wallet/api/coupons/client/useCoupons";
    //进入首层接口
    String ENTER_CHAT_ROOM = BASE_URL + "/v1/2.0/ars/enterfirstfloor";
    //进入下一层接口
    String ENTER_NEXT_CHAT_ROOM = BASE_URL + "/v1/2.0/ars/confirm";

}
