package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.shandata.ui.presenter.MyGroupTeamPresenter;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.MyGroupTeamView;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.request.RequestCall;

import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/8/10
 * Describe :
 */
public class MyGroupTeamPresenterImpl implements MyGroupTeamPresenter {
    private MyGroupTeamView mTeamView;
    public MyGroupTeamPresenterImpl(MyGroupTeamView view){
        this.mTeamView = view;
    }
    @Override
    public void queryGroupTeam(String joinUserId, String createUser, int page, int size, final int pullType) {
        mTeamView.showProgressStart();
        GetBuilder getBuilder =SCHttpUtils.getAndToken();
        getBuilder.url(HttpApi.QUERY_MY_GROUPTEAM);
        if(!TextUtils.isEmpty(joinUserId)){
            getBuilder.addParams("joinUserId",joinUserId);
        }
        if(!TextUtils.isEmpty(createUser)){
            getBuilder.addParams("createUser",createUser);
        }
        getBuilder.addParams("pageNum",page+"");
        getBuilder.addParams("pageSize",size+"");
        RequestCall build = getBuilder.build();
        build.execute(new SCHttpStringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mTeamView.showProgressEnd();
            }

            @Override
            public void onResponse(String response, int id) {
                mTeamView.showProgressEnd();
                mTeamView.setQuearyMygoupTeamResponse(response,pullType);
            }
        });

    }
}
