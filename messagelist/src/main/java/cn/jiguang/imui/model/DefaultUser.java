package cn.jiguang.imui.model;

import cn.jiguang.imui.commons.models.IUser;


public class DefaultUser implements IUser {

    private long id;
    private String displayName;
    private String avatar;

    public DefaultUser(long id, String displayName, String avatar) {
        this.id = id;
        this.displayName = displayName;
        this.avatar = avatar;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getAvatarFilePath() {
        return avatar;
    }
}
