package com.shanchain.shandata.ui.model;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "users", id = "_id")
public class UserEntry extends Model {

    @Column(name = "Username")
    public String username;

    @Column(name = "AppKey")
    public String appKey;

    @Column(name = "Avatar")
    public String avatar;

    public UserEntry() {
        super();
    }

    public UserEntry(String username, String avatar) {
        super();
        this.username = username;
        this.avatar = avatar;
    }

    public UserEntry(String username, String appKey, String avatar) {
        super();
        this.username = username;
        this.appKey = appKey;
        this.avatar = avatar;
    }

    public static UserEntry getUser(String username) {
        return new Select().from(UserEntry.class).where("Username = ?", username)
                .executeSingle();
    }

    public static UserEntry getUser(String username, String appKey) {
        return new Select().from(UserEntry.class).where("Username = ?", username)
                .where("AppKey = ?", appKey).executeSingle();
    }

    public List<FriendRecommendEntry> getRecommends() {
        return getMany(FriendRecommendEntry.class, "User");
    }

    public List<FriendEntry> getFriends() {
        return getMany(FriendEntry.class, "User");
    }

}
