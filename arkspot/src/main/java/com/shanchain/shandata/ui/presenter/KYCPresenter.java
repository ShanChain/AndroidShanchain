package com.shanchain.shandata.ui.presenter;

/**
 * Created by WealChen
 * Date : 2019/9/26
 * Describe :
 */
public interface KYCPresenter {
    void uploadPhotoListToServer(String urlPath);
    void commitUserInfo(String userId,String realName,String idcardNo,String passportNo,
                        String cardPhotoFront,String cardPhotoBackground,String cardPhotoHand,String passportPhoto,String passportPhotoHand);
    void updateUserKYCInfo(String id,String userId,String realName,String idcardNo,String passportNo,
                        String cardPhotoFront,String cardPhotoBackground,String cardPhotoHand,String passportPhoto,String passportPhotoHand);
    void queryKycCertifitInfo(String userId);
}
