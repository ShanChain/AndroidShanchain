package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.shandata.ui.model.BdGroupMemberInfo;
import com.shanchain.shandata.ui.model.ContactBean;
import com.shanchain.shandata.ui.model.GroupOwnerBean;
import com.shanchain.shandata.ui.model.LeaveGroupResult;
import com.shanchain.shandata.ui.model.NoticeBean;
import com.shanchain.shandata.ui.model.ResponseGroupMemberBean;
import com.shanchain.shandata.ui.model.SceneDetailData;
import com.shanchain.shandata.ui.presenter.SceneDetailsPresenter;
import com.shanchain.shandata.ui.view.activity.chat.view.SceneDetailsView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/11/20.
 */

public class SceneDetailsPresenterImpl implements SceneDetailsPresenter {

    private SceneDetailsView mView;
    private List<BdGroupMemberInfo> mBdInfos;

    public SceneDetailsPresenterImpl(SceneDetailsView view) {
        mView = view;
    }


    @Override
    public void getGroupInfo(final String toChatName) {
        //自己服务器极客接口获取群信息
        SCHttpUtils.post()
                .url(HttpApi.HX_GROUP_QUERY)
                .addParams("groupId", toChatName)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取群信息失败");
                        e.printStackTrace();
                        mView.initGroupInfoSuc(null, null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到群信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                SceneDetailData sceneDetailInfo = JSONObject.parseObject(data, SceneDetailData.class);
                                obtainGroupMembers(toChatName, sceneDetailInfo);
                            } else {
                                mView.initGroupInfoSuc(null, null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initGroupInfoSuc(null, null);
                        }
                    }
                });
    }

    @Override
    public void getUserInfo(String toChatName) {
        List<String> ids = new ArrayList<>();

        String arr = JSONObject.toJSONString(ids);

        mBdInfos = new ArrayList<>();
        String myCharacterId = SCCacheUtils.getCacheCharacterId();
        ids.add(myCharacterId);
        BdGroupMemberInfo mInfo = new BdGroupMemberInfo();
        mInfo.setCharacterId(Integer.parseInt(myCharacterId));
    }

    @Override
    public void leaveGroup(String toChatName) {
        String myHxUserName = SCCacheUtils.getCacheHxUserName();
        List<String> arr = new ArrayList<>();
        arr.add(myHxUserName);
        String jArr = JSONObject.toJSONString(arr);
        SCHttpUtils.post()
                .url(HttpApi.HX_GROUP_REMOVE_MEMBERS)
                .addParams("groupId", toChatName)
//                .addParams("jArray", jArr)
                .addParams("username", myHxUserName)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("退群失败");
                        e.printStackTrace();
                        mView.leaveGroupSuc(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("退群结果 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                LeaveGroupResult resultsBean = JSONObject.parseObject(data, LeaveGroupResult.class);
                                boolean result = resultsBean.isResult();
                                    mView.leaveGroupSuc(result);
                            } else {
                                mView.leaveGroupSuc(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.leaveGroupSuc(false);
                        }
                    }
                });
    }

    private void obtainUsersInfo(List<String> ids) {
        String arr = JSONObject.toJSONString(ids);
        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_BRIEF)
                .addParams("dataArray", arr)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取角色简要信息失败");
                        e.printStackTrace();
                        mView.initUserInfo(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到角色简要信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                List<ContactBean> contactBeanList = JSONObject.parseArray(data, ContactBean.class);
                                for (int i = 0; i < mBdInfos.size(); i++) {
                                    BdGroupMemberInfo bdInfo = mBdInfos.get(i);
                                    for (ContactBean bean : contactBeanList) {
                                        if (bdInfo.getCharacterId() == bean.getCharacterId()) {
                                            bdInfo.setName(bean.getName());
                                            bdInfo.setHeadImg(bean.getHeadImg());
                                        }
                                    }
                                }

                                mView.initUserInfo(mBdInfos);

                            } else {
                                mView.initUserInfo(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initUserInfo(null);
                        }
                    }
                });

    }

    private void obtainGroupMembers(String toChatName, final SceneDetailData sceneDetailInfo) {
        SCHttpUtils.post()
                .url(HttpApi.HX_GROUP_MEMBERS)
                .addParams("groupId", toChatName)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取群成员失败");
                        e.printStackTrace();
                        mView.initGroupInfoSuc(sceneDetailInfo, null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取群成员 = " + response);
                            String code = SCJsonUtils.parseCode(response);

                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                List<ResponseGroupMemberBean> memberList = JSONObject.parseArray(data, ResponseGroupMemberBean.class);

                                mBdInfos = new ArrayList<>();
                                List<Integer> ids = new ArrayList<>();
                                GroupOwnerBean owner = sceneDetailInfo.getGroupOwner();
                                int ownerId = owner.getCharacterId();
                                BdGroupMemberInfo ownerInfo = new BdGroupMemberInfo();
                                ownerInfo.setCharacterId(ownerId);
                                ownerInfo.setHxUserName(owner.getHxUserName());
                                ownerInfo.setType(Constants.GROUP_OWNER);
                                mBdInfos.add(ownerInfo);
                                ids.add(ownerId);

                                for (int i = 0; i < memberList.size(); i++) {
                                    ResponseGroupMemberBean memberBean = memberList.get(i);
                                    ids.add(memberBean.getCharacterId());
                                    BdGroupMemberInfo memberInfo = new BdGroupMemberInfo();
                                    memberInfo.setCharacterId(memberBean.getCharacterId());
                                    memberInfo.setHxUserName(memberBean.getHxUserName());
                                    memberInfo.setType(memberBean.isAdmin() ? Constants.GROUP_ADMIN : Constants.GROUP_MEMBER);
                                    mBdInfos.add(memberInfo);
                                }

                                String idsArr = JSONObject.toJSONString(ids);

                                obtainCharacterBrief(sceneDetailInfo, idsArr);

                            } else {
                                mView.initGroupInfoSuc(sceneDetailInfo, null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initGroupInfoSuc(sceneDetailInfo, null);
                        }
                    }
                });

        SCHttpUtils.post()
                .url(HttpApi.HX_GROUP_GET_NOTICE)
                .addParams("groupId", toChatName)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取群公告失败");
                        e.printStackTrace();
                        mView.initNoticeSuc(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取群公告成功 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                List<NoticeBean> noticeBeenList = JSONObject.parseArray(data, NoticeBean.class);
                                if (noticeBeenList != null && noticeBeenList.size() > 0) {
                                    mView.initNoticeSuc(noticeBeenList.get(0));
                                } else {
                                    mView.initNoticeSuc(null);
                                }
                            } else {
                                mView.initNoticeSuc(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initNoticeSuc(null);
                        }


                    }
                });

    }

    private void obtainCharacterBrief(final SceneDetailData sceneDetailInfo, String arr) {
        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_BRIEF)
                .addParams("dataArray", arr)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取角色简要信息失败");
                        e.printStackTrace();
                        mView.initGroupInfoSuc(sceneDetailInfo, null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到角色简要信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                List<ContactBean> contactBeanList = JSONObject.parseArray(data, ContactBean.class);
                                for (int i = 0; i < mBdInfos.size(); i++) {
                                    BdGroupMemberInfo bdInfo = mBdInfos.get(i);
                                    for (ContactBean bean : contactBeanList) {
                                        if (bdInfo.getCharacterId() == bean.getCharacterId()) {
                                            bdInfo.setName(bean.getName());
                                            bdInfo.setHeadImg(bean.getHeadImg());
                                        }
                                    }
                                }
                                mView.initGroupInfoSuc(sceneDetailInfo, mBdInfos);
                            } else {
                                mView.initGroupInfoSuc(sceneDetailInfo, null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initGroupInfoSuc(sceneDetailInfo, null);
                        }
                    }
                });
    }
}
