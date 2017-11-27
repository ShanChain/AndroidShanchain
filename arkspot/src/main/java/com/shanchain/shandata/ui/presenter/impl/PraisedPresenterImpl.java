package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.encryption.SCJsonUtils;
import com.shanchain.shandata.ui.model.ContactBean;
import com.shanchain.shandata.ui.model.IsFavBean;
import com.shanchain.shandata.ui.model.StoryContentBean;
import com.shanchain.shandata.ui.presenter.PraisedPresenter;
import com.shanchain.shandata.ui.view.activity.mine.view.PraisedView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/11/21.
 */

public class PraisedPresenterImpl implements PraisedPresenter {
    private PraisedView mView;
    private List<StoryContentBean> mContentBeanList = new ArrayList<>();

    public PraisedPresenterImpl(PraisedView view) {
        mView = view;
    }


    @Override
    public void initPraiseData(int page, int size) {
        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_SUPPORT_LIST)
                .addParams("page", "" + page)
                .addParams("size", "" + size)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取我赞过的失败");
                        e.printStackTrace();
                        mView.initPraisedSuc(null, false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到我赞过的数据 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                boolean last = SCJsonUtils.parseBoolean(data, "last");
                                String content = SCJsonUtils.parseString(data, "content");
                                mContentBeanList = JSONObject.parseArray(content, StoryContentBean.class);
                                if (mContentBeanList != null && mContentBeanList.size() > 0) {
                                    obtainCharacterInfo(last);
                                   // mView.initPraisedSuc(mContentBeanList, last);
                                } else {
                                    mView.initPraisedSuc(null, last);
                                }
                            } else {
                                mView.initPraisedSuc(null, false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initPraisedSuc(null,false);
                        }


                    }
                });
    }

    @Override
    public void supportCancel(int storyId,final int position) {
        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_SUPPORT_CANCEL)
                .addParams("storyId", storyId + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("取消点赞失败");
                        e.printStackTrace();
                        mView.supportCancelSuc(false, position);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("取消点赞成功 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                mView.supportCancelSuc(true, position);
                            } else {
                                mView.supportCancelSuc(false, position);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.supportCancelSuc(false, position);
                        }
                    }
                });
    }

    @Override
    public void support(int storyId,final int position) {
        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_SUPPORT_ADD)
                .addParams("storyId", storyId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("点赞失败");
                        e.printStackTrace();
                        mView.supportSuc(false, position);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("点赞结果 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");

                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                mView.supportSuc(true, position);
                            } else {
                                mView.supportSuc(false, position);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.supportSuc(false, position);
                        }
                    }
                });
    }


    private void obtainCharacterInfo(final boolean last) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < mContentBeanList.size(); i ++) {
            int characterId = mContentBeanList.get(i).getCharacterId();
            ids.add(characterId);
        }
        String jArr = JSONObject.toJSONString(ids);
        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_BRIEF)
                .addParams("dataArray",jArr)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取角色简要信息失败");
                        e.printStackTrace();
                        mView.initPraisedSuc(null,last);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到角色简要信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                                String data = SCJsonUtils.parseData(response);
                                List<ContactBean> contactBeanList = JSONObject.parseArray(data, ContactBean.class);
                                for (int i = 0; i < mContentBeanList.size(); i ++) {
                                    StoryContentBean storyContentBean = mContentBeanList.get(i);
                                    for (ContactBean bean : contactBeanList){
                                        if (bean.getCharacterId() == storyContentBean.getCharacterId()){
                                            storyContentBean.setContactBean(bean);
                                        }
                                    }
                                }
                                checkFav(last);

                            }else{
                                mView.initPraisedSuc(null,last);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initPraisedSuc(null,last);
                        }
                    }
                });
    }

    private void checkFav(final boolean last) {

        List<Integer> checkList = new ArrayList<>();
        for (int i = 0; i < mContentBeanList.size(); i ++) {
            int storyId = mContentBeanList.get(i).getStoryId();
            checkList.add(storyId);
        }
        String jArr = JSONObject.toJSONString(checkList);

        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_ISFAV_LIST)
                .addParams("checkIdList",jArr)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("判断故事是否点赞失败");
                        e.printStackTrace();
                        mView.initPraisedSuc(null,false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到故事点赞数据 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                                String data = SCJsonUtils.parseData(response);
                                List<IsFavBean> favBeanList = JSONObject.parseArray(data, IsFavBean.class);
                                for (int i = 0; i < mContentBeanList.size(); i ++) {
                                    int storyId = mContentBeanList.get(i).getStoryId();
                                    for (IsFavBean bean : favBeanList){
                                        if (storyId == bean.getStoryId()){
                                            mContentBeanList.get(i).setFav(bean.isCheck());
                                        }
                                    }
                                }
                                mView.initPraisedSuc(mContentBeanList,last);
                            }else{
                                mView.initPraisedSuc(mContentBeanList,last);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initPraisedSuc(mContentBeanList,last);
                        }
                    }
                });
    }
}
