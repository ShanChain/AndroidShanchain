package com.shanchain.shandata.ui.presenter.impl;

import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.shandata.ui.presenter.CouponListPresenter;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.CounponListView;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/7/20
 * Describe :
 */
public class CouponListPresenterImpl implements CouponListPresenter {
    private CounponListView mCounponListView;

    public CouponListPresenterImpl(CounponListView counponListView){
        this.mCounponListView = counponListView;
    }
    @Override
    public void getCounponList(String subuserId, int page, int size,final int pullType) {
//        mCounponListView.showProgressStart();
        SCHttpUtils.getAndToken()
                .url(HttpApi.COUPONS_ALL_LIST)
                .addParams("pageNo", page + "")
                .addParams("pageSize", size + "")
                .addParams("subuserId", subuserId + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        mCounponListView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, final int id) {
//                        mCounponListView.showProgressEnd();
                        mCounponListView.setCounponList(response,pullType);
                    }
                });
    }

    @Override
    public void getMyGetCounponList(String subuserId, int page, int size,final int pullType) {
        mCounponListView.showProgressStart();
        SCHttpUtils.get()
                .url(HttpApi.COUPONS_RECEIVER_LIST)
                .addParams("pageNo", page + "")
                .addParams("pageSize", size + "")
                .addParams("subuserId", subuserId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mCounponListView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mCounponListView.showProgressEnd();
                        mCounponListView.setMyGetCounponList(response,pullType);
                    }
                });
    }

    @Override
    public void getMyCreateCounponList(String subuserId, int page, int size, final int pullType) {
        SCHttpUtils.get()
                .url(HttpApi.COUPONS_CREATE_LIST)
                .addParams("pageNo", page + "")
                .addParams("pageSize", size + "")
                .addParams("subuserId", subuserId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mCounponListView.setMyCreateCounponList(response,pullType);
                    }
                });
    }
}
