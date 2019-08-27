package com.shanchain.shandata.ui.presenter.impl;

import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.shandata.ui.presenter.ReturnInvationPresenter;
import com.shanchain.shandata.ui.view.activity.mine.view.ReturnInvationView;

import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/8/23
 * Describe :
 */
public class ReturnInvationPresenterImpl implements ReturnInvationPresenter {
    private ReturnInvationView mInvationView;

    public ReturnInvationPresenterImpl(ReturnInvationView view){
        this.mInvationView = view;
    }
    @Override
    public void getInvationDataFromUser(String userId) {
        mInvationView.showProgressStart();
        SCHttpUtils.getNoToken()
                .url(HttpApi.GET_INVATION_USER_DATA+"/"+userId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mInvationView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mInvationView.showProgressEnd();
                        mInvationView.setInvationDataResponse(response);
                    }
                });
    }

    @Override
    public void queryUserInvationRecord(String userId, int page, int size, final int pullType) {
        SCHttpUtils.get()
                .url(HttpApi.GET_INVATION_USER_LIST)
                .addParams("userId", userId)
                .addParams("pageNum",page+"")
                .addParams("pageSize",size+"")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mInvationView.setQuearyInvatRecordResponse(response,pullType);
                    }
                });
    }
}
