package com.shanchain.shandata.ui.model;

/**
 * Created by WealChen
 * Date : 2019/10/19
 * Describe :
 */
public class CheckInBean {
    int userId;
    int continuousDays;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContinuousDays() {
        return continuousDays;
    }

    public void setContinuousDays(int continuousDays) {
        this.continuousDays = continuousDays;
    }
}
