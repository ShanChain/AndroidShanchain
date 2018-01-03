package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.shandata.ui.model.DynamicVideoBean;
import com.shanchain.shandata.ui.presenter.DynamicVideoPresenter;
import com.shanchain.shandata.ui.view.activity.story.stroyView.DynamicVideoView;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by zhoujian on 2018/1/2.
 */

public class DynamicVideoPresenterImpl implements DynamicVideoPresenter {

    private DynamicVideoView mView;

    public DynamicVideoPresenterImpl(DynamicVideoView view) {
        mView = view;
    }

    @Override
    public void initData(String id) {
        SCHttpUtils.post()
                .url(HttpApi.PLAY_GET_ID)
                .addParams("playId", id)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("获取演绎详情失败");
                        e.printStackTrace();
                        mView.initVideoSuc(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到演绎详情信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                DynamicVideoBean videoBean = SCJsonUtils.parseObj(data, DynamicVideoBean.class);
                                mView.initVideoSuc(videoBean);
                            } else {
                                mView.initVideoSuc(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initVideoSuc(null);
                        }

                    }
                });

        SCHttpUtils.postWithUserId()
                .url(HttpApi.STORY_IS_FAV)
                .addParams("checkId", id)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("查询故事点赞失败");
                        e.printStackTrace();
                        mView.initFavSuc(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("查询故事点赞结果 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                if (TextUtils.equals("true", data)) {
                                    mView.initFavSuc(true);
                                } else {
                                    mView.initFavSuc(false);
                                }
                            } else {
                                mView.initFavSuc(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initFavSuc(false);
                        }
                    }
                });


    }

    @Override
    public void getCharacterInfo(int characterId) {

        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(characterId);
        String idArr = JSONObject.toJSONString(ids);

        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_BRIEF)
                .addParams("dataArray", idArr)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("获取角色信息失败");
                        e.printStackTrace();
                        mView.initCharacterSuc("","");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到角色信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                                String data = SCJsonUtils.parseData(response);
                                JSONArray jsonArray = JSONObject.parseArray(data);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String headImg = jsonObject.getString("headImg");
                                String name = jsonObject.getString("name");
                                mView.initCharacterSuc(headImg,name);
                            }else{
                                mView.initCharacterSuc("","");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initCharacterSuc("","");
                        }
                    }
                });
    }


}
