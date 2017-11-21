package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.encryption.SCJsonUtils;
import com.shanchain.shandata.ui.model.StoryContentBean;
import com.shanchain.shandata.ui.presenter.PraisedPresenter;
import com.shanchain.shandata.ui.view.activity.mine.view.PraisedView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/11/21.
 */

public class PraisedPresenterImpl implements PraisedPresenter {
    private PraisedView mView;

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
                        mView.initPraisedSuc(null,false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取到我赞过的数据 = " + response);
                        String code = SCJsonUtils.parseCode(response);
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            String data = SCJsonUtils.parseData(response);
                            boolean last = SCJsonUtils.parseBoolean(data, "last");
                            String content = SCJsonUtils.parseString(data, "content");
                            List<StoryContentBean> contentBeanList = JSONObject.parseArray(content, StoryContentBean.class);
                            if (contentBeanList != null && contentBeanList.size() > 0) {
                                mView.initPraisedSuc(contentBeanList,last);
                            } else {
                                mView.initPraisedSuc(null,last);
                            }
                        } else {
                            mView.initPraisedSuc(null,false);
                        }


                    }
                });
    }
}
