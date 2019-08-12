package com.shanchain.shandata.ui.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpPostBodyCallBack;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.shandata.ui.presenter.MyGroupTeamPresenter;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.MyGroupTeamView;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    @Override
    public void checkPasswordToServer(Context context, String file, String value) {
        mTeamView.showProgressStart();
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        RequestBody fileBody = MultipartBody.create(MEDIA_TYPE, file);
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder()
                .addFormDataPart("file", file, fileBody)
                .addFormDataPart("subuserId", "" + SCCacheUtils.getCacheCharacterId())
                .addFormDataPart("userId", "" + SCCacheUtils.getCacheUserId())
                .addFormDataPart("value", "" + value)//支付金额
                .setType(MultipartBody.FORM);
        RequestBody multiBody = multiBuilder.build();
        SCHttpUtils.postByBody(HttpApi.PAY_FOR_ARS + "?token=" + SCCacheUtils.getCacheToken(), multiBody, new SCHttpPostBodyCallBack(context, null) {
            @Override
            public void responseDoParse(String string) throws IOException {
                mTeamView.showProgressEnd();
                mTeamView.setCheckPasswResponse(string);
            }
        });
    }

    @Override
    public void insertMiningRoomByOther(String userId, String diggingsId) {
        SCHttpUtils.post()
                .url(HttpApi.ADD_MMINING_ROOM)
                .addParams("userId",userId)
                .addParams("diggingsId",diggingsId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mTeamView.setAddMinigRoomResponse(response);
                    }
                });
    }
}
