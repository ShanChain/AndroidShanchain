package com.shanchain.shandata.ui.presenter.impl;

import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.ui.presenter.SquarePresenter;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.SquareView;

import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/7/23
 * Describe :
 */
public class SquarePresenterImpl implements SquarePresenter {
    private SquareView mSquareView;
    public SquarePresenterImpl(SquareView view){
        this.mSquareView = view;
    }
    @Override
    public void getListData(String title, String userId,int page, int size, final int pullType) {
        mSquareView.showProgressStart();
        SCHttpUtils.post()
                .url(HttpApi.INOVATION_LIST)
                .addParams("currentPage", page + "")
                .addParams("pagesize", size + "")
                .addParams("userId",userId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("TaskPresenterImpl", "查询任务失败");
                        mSquareView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mSquareView.showProgressEnd();
                        mSquareView.setListDataResponse(response,pullType);
                    }
                });
    }

    @Override
    public void attentionUser(int userId, int attentionUserId) {
        mSquareView.showProgressStart();
        SCHttpUtils.post()
                .url(HttpApi.ADD_ATTENTION)
                .addParams("userId", userId + "")
                .addParams("attentionUserId", attentionUserId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mSquareView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mSquareView.showProgressEnd();
                        mSquareView.setAttentionResponse(response,0);
                    }
                });
    }

    @Override
    public void deleteAttentionUser(int userId, int attentionUserId) {
        mSquareView.showProgressStart();
        SCHttpUtils.post()
                .url(HttpApi.DELETE_ATTENTION)
                .addParams("userId", userId + "")
                .addParams("attentionUserId", attentionUserId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mSquareView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mSquareView.showProgressEnd();
                        mSquareView.setAttentionResponse(response,1);
                    }
                });
    }

    @Override
    public void addPraiseToArticle(int userId, int invitationId) {
        mSquareView.showProgressStart();
        SCHttpUtils.post()
                .url(HttpApi.ADD_PRAISE_TITLE)
                .addParams("userId", userId + "")
                .addParams("invitationId", invitationId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mSquareView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mSquareView.showProgressEnd();
                        mSquareView.setPraiseResponse(response,0);
                    }
                });
    }

    @Override
    public void deletePraiseToArticle(int userId, int invitationId) {
        mSquareView.showProgressStart();
        SCHttpUtils.post()
                .url(HttpApi.DELETE_PRAISE_TITLE)
                .addParams("userId", userId + "")
                .addParams("invitationId", invitationId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mSquareView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mSquareView.showProgressEnd();
                        mSquareView.setPraiseResponse(response,1);
                    }
                });
    }
}
