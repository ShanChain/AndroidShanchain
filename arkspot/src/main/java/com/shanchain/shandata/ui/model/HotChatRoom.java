package com.shanchain.shandata.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

public class HotChatRoom implements Parcelable {

    /**
     * id : 1
     * roomId : 12892685
     * roomName : 东经106.58°，北纬29.56°
     * thumbnails : http://shanchain-web.oss-cn-beijing.aliyuncs.com/hot_chat_room/jiefangbei2.png
     * background : http://shanchain-web.oss-cn-beijing.aliyuncs.com/hot_chat_room/jiefangbei1.png
     * sortNo : 1
     */

    private int id;
    private String roomId;
    private String roomName;
    private String thumbnails;
    private String background;
    private String sortNo;

    public HotChatRoom(){

    }

    protected HotChatRoom(Parcel in) {
        id = in.readInt();
        roomId = in.readString();
        roomName = in.readString();
        thumbnails = in.readString();
        background = in.readString();
        sortNo = in.readString();
        userNum = in.readString();
    }

    public static final Creator<HotChatRoom> CREATOR = new Creator<HotChatRoom>() {
        @Override
        public HotChatRoom createFromParcel(Parcel in) {
            return new HotChatRoom(in);
        }

        @Override
        public HotChatRoom[] newArray(int size) {
            return new HotChatRoom[size];
        }
    };

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    private String userNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(roomId);
        dest.writeString(roomName);
        dest.writeString(thumbnails);
        dest.writeString(background);
        dest.writeString(sortNo);
        dest.writeString(userNum);
    }
}