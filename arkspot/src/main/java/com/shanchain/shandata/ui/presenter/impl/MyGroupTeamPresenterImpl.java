package com.shanchain.shandata.ui.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpPostBodyCallBack;
import com.shanchain.data.common.net.SCHttpPostBodyNewCallBack;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
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
    public void queryGroupTeam(String joinUserId, String createUser,String searchString, int page, int size, final int pullType,int state,String isCreate) {
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
        if(state>0){
            getBuilder.addParams("status",state+"");
        }
        if(!TextUtils.isEmpty(isCreate)){
            getBuilder.addParams("isCreate",isCreate);
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
                mTeamView.setCheckPassFaile();
            }
        });
    }

    @Override
    public void insertMiningRoomByOther(String userId, String diggingsId,String isPay) {
        SCHttpUtils.post()
                .url(HttpApi.ADD_MMINING_ROOM)
                .addParams("userId",userId)
                .addParams("diggingsId",diggingsId)
                .addParams("isPay",isPay)
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

    @Override
    public void deleteMiningRoomRecord(String id) {
        SCHttpUtils.post()
                .url(HttpApi.DELETE_MMINING_ROOM_RECORD)
                .addParams("id",id)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mTeamView.setdeleteDigiRoomIdResponse(response);
                    }
                });
    }

    @Override
    public void updateMiningRoomRecord(String id, String isPay) {
        LogUtils.d("--->>>"+id+"----"+isPay);
        SCHttpUtils.post()
                .url(HttpApi.UPDATE_MMINING_ROOM_RECORD)
                .addParams("id",id)
                .addParams("isPay",isPay)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mTeamView.setUpdateMiningRoomResponse(response);
                    }
                });
    }

    @Override
    public void checkUserHasWallet(Context context) {
        CustomDialog showPasswordDialog = new CustomDialog(context, true, 1.0,
                R.layout.dialog_bottom_wallet_password,
                new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
        SCHttpUtils.getAndToken()
                .url(HttpApi.WALLET_INFO)
                .addParams("characterId", SCCacheUtils.getCacheCharacterId())
                .addParams("userId", SCCacheUtils.getCacheUserId())
                .build()
                .execute(new SCHttpStringCallBack(context, showPasswordDialog) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mTeamView.setCheckUserHasWalletResponse(response);
                    }
                });
    }
}
