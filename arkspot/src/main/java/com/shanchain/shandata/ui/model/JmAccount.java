package com.shanchain.shandata.ui.model;

import java.io.File;
import java.util.Map;

import cn.jpush.im.android.api.callback.DownloadAvatarCallback;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class JmAccount extends UserInfo {

    /**
     * username : 154282098521300163e0479798785
     * nickname : 我去⊙∀⊙！
     * avatar : qiniu/image/j/D658E06D5653DA0512644FD383404DDF.jpg
     * birthday : null
     * gender : 0
     * signature : 莫邪公交车
     * region : null
     * address : null
     * ctime : 2018-11-22 04:12:53
     * mtime : 2018-12-20 16:34:51
     * appkey : null
     * resultOK : true
     * responseCode : 200
     * originalContent : {"gender":0,"signature":"莫邪公交车","nickname":"我去⊙∀⊙！","ctime":"2018-11-22 04:12:53","avatar":"qiniu/image/j/D658E06D5653DA0512644FD383404DDF.jpg","mtime":"2018-12-20 16:34:51","username":"154282098521300163e0479798785"}
     * rateLimitRemaining : 1199
     * rateLimitQuota : 1200
     * rateLimitReset : 60
     */

    private String userName;
    private String nickName;
    private String noteName;
    private String avatar;
    private String birthday;
    private String noteText;
    private int gender;
    private String signature;
    private String region;
    private String address;
    private String ctime;
    private String mtime;
    private String appkey;
    private boolean resultOK;
    private int responseCode;
    private String originalContent;
    private int rateLimitRemaining;
    private int rateLimitQuota;
    private int rateLimitReset;

    @Override
    public String getNotename() {
        return null;
    }

    @Override
    public String getNoteText() {
        return null;
    }

    @Override
    public long getBirthday() {
        return 0;
    }

    @Override
    public File getAvatarFile() {
        return null;
    }

    @Override
    public void getAvatarFileAsync(DownloadAvatarCallback downloadAvatarCallback) {

    }

    @Override
    public void getAvatarBitmap(GetAvatarBitmapCallback getAvatarBitmapCallback) {

    }

    @Override
    public File getBigAvatarFile() {
        return null;
    }

    @Override
    public void getBigAvatarBitmap(GetAvatarBitmapCallback getAvatarBitmapCallback) {

    }

    @Override
    public int getBlacklist() {
        return 0;
    }

    @Override
    public int getNoDisturb() {
        return 0;
    }

    @Override
    public boolean isFriend() {
        return false;
    }

    @Override
    public String getAppKey() {
        return null;
    }

    @Override
    public void setUserExtras(Map<String, String> map) {

    }

    @Override
    public void setUserExtras(String s, String s1) {

    }

    @Override
    public void setBirthday(long l) {

    }

    @Override
    public void setNoDisturb(int i, BasicCallback basicCallback) {

    }

    @Override
    public void removeFromFriendList(BasicCallback basicCallback) {

    }

    @Override
    public void updateNoteName(String s, BasicCallback basicCallback) {

    }

    @Override
    public void updateNoteText(String s, BasicCallback basicCallback) {

    }

    @Override
    public String getDisplayName() {
        return null;
    }
}
