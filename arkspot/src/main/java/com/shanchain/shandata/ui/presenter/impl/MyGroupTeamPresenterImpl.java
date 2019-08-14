package com.shanchain.shandata.ui.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpPostBodyCallBack;
import com.shanchain.data.common.net.SCHttpPostBodyNewCallBack;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.ui.presenter.MyGroupTeamPresenter;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.MyGroupTeamView;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.shanchain.data.common.base.Constants.CACHE_TOKEN;

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
    public void queryGroupTeam(String joinUserId, String createUser,String searchString, int page, int size, final int pullType) {
        mTeamView.showProgressStart();
        GetBuilder getBuilder =SCHttpUtils.getAndToken();
        getBuilder.url(HttpApi.QUERY_MY_GROUPTEAM);
        if(!TextUtils.isEmpty(joinUserId)){
            getBuilder.addParams("joinUserId",joinUserId);
        }
        if(!TextUtils.isEmpty(createUser)){
            getBuilder.addParams("createUser",createUser);
        }
        if(!TextUtils.isEmpty(searchString)){
            getBuilder.addParams("searchString",searchString);
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
        LogUtils.d("Resonse file: ", file);
        final File f = new File(file);
        String userId = SCCacheUtils.getCache("0", "curUser");
        String token = SCCacheUtils.getCache(userId, CACHE_TOKEN);
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        RequestBody fileBody = MultipartBody.create(MEDIA_TYPE, f);
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder()
                .addFormDataPart("file", f.getName(), fileBody)
                .addFormDataPart("subuserId", "" + SCCacheUtils.getCacheCharacterId())
                .addFormDataPart("userId", "" + SCCacheUtils.getCacheUserId())
                .addFormDataPart("value", "" + value)//支付金额
                .addFormDataPart("token",token)
                .addFormDataPart("memo","payMining")//挖矿支付标识
                .setType(MultipartBody.FORM);
        RequestBody multiBody = multiBuilder.build();
        SCHttpUtils.postByBody(HttpApi.PAY_FOR_ARS + "?token=" + SCCacheUtils.getCacheToken(), multiBody, new SCHttpPostBodyNewCallBack(context, null) {
            @Override
            public void responseDoParse(String string) throws IOException {
                mTeamView.showProgressEnd();
                mTeamView.setCheckPasswResponse(string);
            }
            @Override
            public void responseDoFaile(String string) throws IOException {
                mTeamView.showProgressEnd();
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
