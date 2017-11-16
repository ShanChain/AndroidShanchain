package com.shanchain.data.common.net;

import com.shanchain.data.common.base.Constants;

/**
 * Created by zhoujian on 2017/9/18.
 */

public interface HttpApi {

    /** IM相关接口*/



    String BASE_URL_IM = Constants.SC_ENV_PRD ? Constants.SC_HOST_RELEASE : Constants.SC_HOST_TEST;

    /** 全局接口地址*/
    String BASE_URL = Constants.SC_ENV_PRD ? Constants.SC_HOST_RELEASE : Constants.SC_HOST_TEST;

    /**注册环信聊天用户*/
    String HX_USER_REGIST = BASE_URL_IM + "/hx/user/regist";

    /** 添加好友*/
    String HX_USER_ADD_FRIEND = BASE_URL_IM + "/hx/user/addFriend";

    /** 删除好友*/
    String HX_USER_DEL_FRIEND = BASE_URL_IM + "/hx/user/delFriend";

    /** 创建群组*/
    String HX_GROUP_CREATE = BASE_URL_IM + "/hx/group/create";

    /** 修改群信息*/
    String HX_GROUP_MODIFY = BASE_URL_IM + "/hx/group/modify";

    /** 删除群*/
    String HX_GROUP_DEL = BASE_URL_IM + "/hx/group/delete";

    /** 变换群主*/
    String HX_GROUP_OWNER = BASE_URL_IM + "/hx/group/owner";

    /** 批量添加群成员*/
    String HX_GROUP_ADD_MEMBERS = BASE_URL_IM + "/hx/group/addMembers";

    /** 批量移除群成员*/
    String HX_GROUP_REMOVE_MEMBERS = BASE_URL_IM + "/hx/group/rmMembers";

    /** 添加管理员*/
    String HX_GROUP_ADD_ADMIN = BASE_URL_IM + "/hx/group/addAdmin";

    /** 移除群管理*/
    String HX_GROUP_REMOVE_ADMIN = BASE_URL_IM + "/hx/group/rmAdmin";

    /** 获取我的关注列表*/
    String FOCUS_MY_FOCUS = BASE_URL + "/v1/focus/myFocus";

    /** 获取我的粉丝列表*/
    String FOCUS_FANS = BASE_URL + "/v1/focus/funs";

    /** 查找我关注的列表*/
    String FOCUS_QUERY = BASE_URL + "/v1/focus/query";


    /** 查看群信息*/
    String HX_GROUP_QUERY = BASE_URL_IM + "/hx/group/query";

    /** 获取群公告*/
    String HX_GROUP_GET_NOTICE = BASE_URL_IM + "/hx/group/notice/get";

    /** 通过环信id（username）获取用户详情*/
    String HX_USER_DETAIL = BASE_URL_IM + "/hx/user/detail";

    /** 骚杨服务器跟地址*/
    String url = "http://192.168.0.110:8080";



    /** 获取上传图片的信息*/
    String UP_LOAD_FILE = "http://115.29.176.143/oss/upload/app";

    /** 查询标签*/
    String TAG_QUERY = BASE_URL + "/v1/tag/query";

    /** 获取热门标签*/
    String TAG_QUERY_HOT = BASE_URL + "/v1/tag/query/hot";

    /** 获取我收藏的时空*/
    String SPACE_LIST_FAVORITE = BASE_URL + "/v1/space/list/favorite";

    /**查找时空--根据ID*/
    String SPACE_LIST = BASE_URL + "/v1/space/list/spaceId";

    /**发布动态*/
    String STORY_ADD = BASE_URL + "/v1/story/add";
    /**第三方登录*/
    String USER_THIRD_LOGIN = BASE_URL + "/v1/user/third_login";

    /**注册账号*/
    String USER_REGISTER = BASE_URL + "/v1/user/register";

    /**未登录状态获取短信验证码*/
    String SMS_UNLOGIN_VERIFYCODE = BASE_URL + "/v1/sms/unlogin/verifycode";

    /** 登录*/
    String USER_LOGIN = BASE_URL + "/v1/user/login";

    /** 重置密码*/
    String RESET_PWD = BASE_URL + "/v1/user/reset_password";

    /** 获取主页关注故事列表*/
    String STORY_FOCUS_GET = BASE_URL + "/v1/story/focus/get";

    /** 获取实时故事id列表*/
    String STORY_RECOMMEND_HOT = BASE_URL + "/v1/recommend/hot";

    /** 获取故事详情列表*/
    String STORY_RECOMMEND_DETAIL = BASE_URL + "/v1/recommend/detail";

    /**通过角色id查询角色详细信息*/
    String CHARACTER_QUERY = BASE_URL + "/v1/character/query";

    /** 通过世界id查找世界详情*/
    String SPACE_LIST_SPACEID = BASE_URL + "/v1/space/list/spaceId";

    /** 给微博点赞*/
    String STORY_SUPPORT_ADD = BASE_URL + "/v1/story/support/add";

    /** 一个空间内的联系人*/
    String SPACE_CONTACT_LIST = BASE_URL + "/v1/character/model/list/spaceId";

    /** 获取评论列表*/
    String COMMENT_QUERY = BASE_URL + "/v1/storyComment/query";

    /**获取角色简要信息*/
    String CHARACTER_BRIEF = BASE_URL + "/v1/character/query/brief";

    /** 查找角色--通过用户ID*/
    String CHARACTER_QUERY_USER_ID = BASE_URL + "/v1/character/query/userId";

    /**新建评论*/
    String STORY_COMMENT_ADD = BASE_URL + "/v1/storyComment/add";

    /**查找时空人物模型*/
    String CHARACTER_MODEL_QUERY_SPACEID = BASE_URL + "/v1/character/model/query/spaceId";
    
    /** 添加人物模型（添加人物）*/
    String CHARACTER_MODEL_CREATE = BASE_URL + "/v1/character/model/create";

    /** 创建时空*/
    String SPACE_CREAT = BASE_URL + "/v1/space/create";

    /** 收藏时空 */
    String SPACE_FAVORITE = BASE_URL + "/v1/space/favorite";

    /** 取消收藏时空*/
    String SPACE_UNFAVORITE = BASE_URL + "/v1/space/unFavorite";
    
    /** 切换角色*/
    String CHARACTER_CHANGE = BASE_URL + "/v1/character/change";

    /** 切换时空*/
    String SPACE_SWITCH = BASE_URL + "/v1/space/switch";

    /** 获取时空的话题列表*/
    String TOPIC_QUERY_SPACEID = BASE_URL +  "/v1/topic/query/spaceId";

    /** 举报故事*/
    String STORY_REPORT = BASE_URL + "/v1/story/report";

    /** 获取当前角色*/
    String CHARACTER_GET_CURRENT = BASE_URL + "/v1/character/get/current";

    /** 获取我的评论*/
    String COMMENT_LIST_MINE = BASE_URL + "/v1/storyComment/list/mine";

    /** 通过故事id列表获取故事*/
    String STORY_GET = BASE_URL + "/v1/story/get";

    /** 添加角色关注*/
    String FOCUS_FOCUS = BASE_URL + "/v1/focus/focus";
    
    /** 根据cahracterId查找环信联系人*/
    String HX_USER_LIST = BASE_URL_IM + "/hx/user/list";

    /** 查找我所在的群*/
    String HX_GROUP_MYGROUP = BASE_URL_IM + "/hx/group/myGroup";

    /**创建大戏*/
    String HX_DRAMA_CREATE = BASE_URL_IM + "/hx/game/create";

    /** 获取角色联系人列表（互相关注带字母）*/
    String FOCUS_CONTACTS = BASE_URL + "/v1/focus/contacts";
    
    /** 获取群成员*/
    String HX_GROUP_MEMBERS = BASE_URL_IM + "/hx/group/members";

    /** 创建话题*/
    String TOPIC_CREATE = BASE_URL + "/v1/topic/create";

    /** 给微博取消点赞*/
    String STORY_SUPPORT_CANCEL = BASE_URL + "/v1/story/support/remove";

    /** 获取关注列表*/
    String STORY_RECOMMEND_FOCUS = BASE_URL + "/v1/recommend/focus";

    /** 通过故事id获取单个故事内容*/
    String STORY_GET_BY_ID = BASE_URL + "/v1/story/get/id";
    
    /** 查找时空--根据id*/
    String SPACE_GET_ID = BASE_URL + "/v1/space/get/id";

    /** 推荐列表*/
    String RECOMMEND_RATE = BASE_URL + "/v1/recommend/rate";

    /** 获取所有时空*/
    String SPACE_LIST_ALL = BASE_URL + "/v1/space/list/all";
    
    /**判断角色是否关注-通过id */
    String FOCUS_IS_FAV = BASE_URL + "/v1/focus/isFav";
    
    /** 获取角色动态接口*/
    String DYNAMIC_CHARACTER = BASE_URL + "/v1/dynamic/character";

    /** 取消角色关注*/
    String FOCUS_UNFOCUS = BASE_URL + "/v1/focus/unfocus";
}
