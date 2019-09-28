package com.shanchain.shandata.ui.presenter.impl;

import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.shandata.ui.model.PhotoBean;
import com.shanchain.shandata.ui.presenter.KYCPresenter;
import com.shanchain.shandata.ui.view.activity.settings.view.KYCView;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by WealChen
 * Date : 2019/9/26
 * Describe :
 */
public class KYCPresenterImpl implements KYCPresenter {
    private KYCView mKYCView;
    public KYCPresenterImpl(KYCView kycView){
        this.mKYCView = kycView;
    }
    @Override
    public void uploadPhotoListToServer(String urlPath) {
        OkHttpClient mOkHttpClient = OkHttpUtils.getInstance().getOkHttpClient();
        MultipartBody.Builder mbody=new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file = new File(urlPath);
        if(!file.exists()){
            return;
        }
        mbody.addFormDataPart("files",file.getName(), RequestBody.create(SCHttpUtils.FORM_DATA,file));
        RequestBody requestBody =mbody.build();
        Request request = new Request.Builder()
                .url(HttpApi.UPLOAD_IMAGE)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("Resonse IOException: ", e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("Resonse: ", result);
                mKYCView.setUploadImages(result);
            }
        });
    }

    @Override
    public void commitUserInfo(String userId, String realName, String idcardNo, String passportNo, String cardPhotoFront,
                               String cardPhotoBackground, String cardPhotoHand, String passportPhoto, String passportPhotoHand) {
        SCHttpUtils.post()
                .url(HttpApi.USER_PASSPORT_INFO)
                .addParams("userId", userId)
                .addParams("realName",realName)
                .addParams("idcardNo",idcardNo)
                .addParams("passportNo",passportNo)
                .addParams("cardPhotoFront",cardPhotoFront)
                .addParams("cardPhotoBackground",cardPhotoBackground)
                .addParams("cardPhotoHand",cardPhotoHand)
                .addParams("passportPhoto",passportPhoto)
                .addParams("passportPhotoHand",passportPhotoHand)
                .addParams("realStatus","0")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        mGroupMenberView.showProgressEnd();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mKYCView.setCommitPassportInfpResponse(response);
                    }

                });
    }

    @Override
    public void updateUserKYCInfo(String id, String userId, String realName, String idcardNo, String passportNo,
                                  String cardPhotoFront, String cardPhotoBackground, String cardPhotoHand,
                                  String passportPhoto, String passportPhotoHand) {
        SCHttpUtils.post()
                .url(HttpApi.UPDATE_USER_PASSPORT_INFO)
                .addParams("id",id)
                .addParams("userId", userId)
                .addParams("realName",realName)
                .addParams("idcardNo",idcardNo)
                .addParams("passportNo",passportNo)
                .addParams("cardPhotoFront",cardPhotoFront)
                .addParams("cardPhotoBackground",cardPhotoBackground)
                .addParams("cardPhotoHand",cardPhotoHand)
                .addParams("passportPhoto",passportPhoto)
                .addParams("passportPhotoHand",passportPhotoHand)
                .addParams("realStatus","0")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        mGroupMenberView.showProgressEnd();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mKYCView.setCommitPassportInfpResponse(response);
                    }

                });
    }

    @Override
    public void queryKycCertifitInfo(String userId) {
        SCHttpUtils.post()
                .url(HttpApi.USER_PASSPORT_QUERY)
                .addParams("userId", userId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        mGroupMenberView.showProgressEnd();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mKYCView.setQueryKycInfoResponse(response);
                    }

                });
    }
}
