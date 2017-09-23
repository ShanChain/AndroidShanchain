package com.shanchain.arkspot.http;

/**
 * Created by zhoujian on 2017/9/18.
 */

public interface HttpApi {

    //IM相关接口
    String BASE_URL_IM = "http://47.91.178.114:8090";

    String BASE_URL_ARTSPOT = "http://115.29.177.22:18480/mockjsdata/1";


    //注册环信聊天用户
    String HX_USER_REGIST = BASE_URL_IM + "/hx/user/regist";

    //添加好友
    String HX_USER_ADD_FRIEND = BASE_URL_IM + "/hx/user/addFriend";

    //删除好友
    String HX_USER_DEL_FRIEND = BASE_URL_IM + "/hx/user/delFriend";

    //创建群组
    String HX_GROUP_CREATE = BASE_URL_IM + "/hx/group/create";

    //修改群信息
    String HX_GROUP_MODIFY = BASE_URL_IM + "/hx/group/modify";

    //删除群
    String HX_GROUP_DEL = BASE_URL_IM + "/hx/group/delete";

    //变换群主
    String HX_GROUP_OWNER = BASE_URL_IM + "/hx/group/owner";

    //批量添加群成员
    String HX_GROUP_ADD_MEMBERS = BASE_URL_IM + "/hx/group/addMembers";

    //批量移除群成员
    String HX_GROUP_REMOVE_MEMBERS = BASE_URL_IM + "/hx/group/rmMembers";

    //添加管理员
    String HX_GROUP_ADD_ADMIN = BASE_URL_IM + "/hx/group/addAdmin";

    //移除群管理
    String HX_GROUP_REMOVE_ADMIN = BASE_URL_IM + "/hx/group/rmAdmin";

    //获取我的关注列表
    String FOCUS_MY_FOCUS = BASE_URL_ARTSPOT + "/v1/focus/myFocus";

    //获取我的粉丝列表
    String FOCUS_FANS = BASE_URL_ARTSPOT + "/v1/focus/funs";

    //查看群信息
    String HX_GROUP_QUARY = BASE_URL_IM + "/hx/group/query";

}
