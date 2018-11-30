package cn.jiguang.imui.model;

import android.os.Parcel;
import android.os.Parcelable;

import cn.jiguang.imui.commons.models.IUser;


public class DefaultUser implements IUser,Parcelable {

    private long id;
    private String displayName;
    private String avatar;
    private String signature;
    private String hxUserId;

    public DefaultUser(long id, String displayName, String avatar) {
        this.id = id;
        this.displayName = displayName;
        this.avatar = avatar;
    }

    protected DefaultUser(Parcel in) {
        id = in.readLong();
        displayName = in.readString();
        avatar = in.readString();
        signature = in.readString();
        hxUserId = in.readString();
    }

    public static final Creator<DefaultUser> CREATOR = new Creator<DefaultUser>() {
        @Override
        public DefaultUser createFromParcel(Parcel in) {
            return new DefaultUser(in);
        }

        @Override
        public DefaultUser[] newArray(int size) {
            return new DefaultUser[size];
        }
    };

    public void setId(long id) {
        this.id = id;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    @Override
    public long getId() {
        return id;
    }

    public String getHxUserId() {
        return hxUserId;
    }

    public void setHxUserId(String hxUserId) {
        this.hxUserId = hxUserId;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String getSignature() {
        return signature;
    }

    @Override
    public String getAvatarFilePath() {
        return avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(displayName);
        dest.writeString(avatar);
        dest.writeString(signature);
        dest.writeString(hxUserId);
    }
}
