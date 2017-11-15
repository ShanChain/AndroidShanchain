package com.shanchain.arkspot.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shanchain.arkspot.ui.model.BdContactInfo;
import com.shanchain.arkspot.ui.model.ContactBean;
import com.shanchain.arkspot.ui.model.GroupInfo;
import com.shanchain.arkspot.ui.model.ResponseHxUerBean;
import com.shanchain.arkspot.ui.presenter.ContactPresenter;
import com.shanchain.arkspot.ui.view.activity.chat.view.ContactView;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/10/28.
 */

public class ContactPresenterImpl implements ContactPresenter {

    private ContactView mContactView;

    public ContactPresenterImpl(ContactView contactView) {
        mContactView = contactView;
    }

    @Override
    public void initContact() {
        String spaceId = SCCacheUtils.getCacheSpaceId();
        SCHttpUtils.postWithUserId()
                .url(HttpApi.FOCUS_QUERY)
                .addParams("type", "0")
                .addParams("spaceId",spaceId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取联系人失败");
                        e.printStackTrace();
                        initError();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取我的关注的" + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                List<ContactBean> contactBeanList = JSONObject.parseArray(data, ContactBean.class);
                                ArrayList<BdContactInfo> bdContactInfos = new ArrayList<>();
                                ArrayList<Integer> characterIds = new ArrayList<>();
                                for (int i = 0; i < contactBeanList.size(); i++) {
                                    characterIds.add(contactBeanList.get(i).getCharacterId());
                                    BdContactInfo bdContactInfo = new BdContactInfo();
                                    bdContactInfo.setContactBean(contactBeanList.get(i));
                                    bdContactInfo.setGroup(false);
                                    bdContactInfos.add(bdContactInfo);
                                }
                                getHxInfo(bdContactInfos, characterIds);
                            }else {
                                initError();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            initError();
                        }
                    }
                });

        SCHttpUtils.postWithChaId()
                .url(HttpApi.HX_GROUP_MYGROUP)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("查找我所在的群失败");
                        e.printStackTrace();
                        mContactView.initGroupSuccess(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("查找我所在的群信息 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                                String data = JSONObject.parseObject(response).getString("data");
                                List<GroupInfo> groupInfoList = JSONObject.parseArray(data, GroupInfo.class);
                                List<BdContactInfo> group = new ArrayList<>();

                                for (int i = 0; i < groupInfoList.size(); i++) {
                                    BdContactInfo bdContactInfo = new BdContactInfo();
                                    bdContactInfo.setGroup(true);
                                    bdContactInfo.setGroupInfo(groupInfoList.get(i));
                                    group.add(bdContactInfo);
                                }
                                mContactView.initGroupSuccess(group);
                            }else {
                                mContactView.initGroupSuccess(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mContactView.initGroupSuccess(null);
                        }
                    }
                });

    }

    private void initError() {
        mContactView.initContactSuccess(null, null, null);
    }

    private void getHxInfo(final ArrayList<BdContactInfo> bdContactInfos, ArrayList<Integer> characterIds) {

        String jArr = JSON.toJSONString(characterIds);

        SCHttpUtils.post()
                .url(HttpApi.HX_USER_LIST)
                .addParams("characterIds", jArr)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取环信联系人失败");
                        e.printStackTrace();
                        initError();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取环信联系人成功 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                                String data = JSONObject.parseObject(response).getString("data");
                                List<ResponseHxUerBean> hxUserList = JSONObject.parseArray(data, ResponseHxUerBean.class);
                                for (int i = 0; i < bdContactInfos.size(); i++) {
                                    BdContactInfo bdContactInfo = bdContactInfos.get(i);
                                    for (ResponseHxUerBean bean : hxUserList) {
                                        if (bdContactInfo.getContactBean().getCharacterId() == bean.getCharacterId()) {
                                            bdContactInfo.setHxUerBean(bean);
                                        }
                                    }
                                }
                                List<BdContactInfo> focus = new ArrayList<>();
                                List<BdContactInfo> fans = new ArrayList<>();
                                List<BdContactInfo> each = new ArrayList<>();
                                for (int i = 0; i < bdContactInfos.size(); i++) {
                                    BdContactInfo bdContactInfo = bdContactInfos.get(i);
                                    int type = bdContactInfo.getContactBean().getType();
                                    if (type == Constants.TYPE_CONTACT_FOCUS) {
                                        focus.add(bdContactInfo);
                                    } else if (type == Constants.TYPE_CONTACT_FUNS) {
                                        fans.add(bdContactInfo);
                                    } else if (type == Constants.TYPE_CONTACT_EACH) {
                                        each.add(bdContactInfo);
                                    }
                                }
                                mContactView.initContactSuccess(focus, fans, each);

                            }else {
                                initError();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            initError();
                        }
                    }
                });
    }

}
