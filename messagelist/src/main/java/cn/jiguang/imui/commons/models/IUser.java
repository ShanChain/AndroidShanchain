package cn.jiguang.imui.commons.models;


public interface IUser {

    /**
     * User id.
     * @return user id, unique
     */
    long getId();

    /**
     * Display name of user
     * @return display name
     */
    String getDisplayName();

    String getSignature();

    /**
     * Get user avatar file path.
     * @return avatar file path
     */
    String getAvatarFilePath();

}
