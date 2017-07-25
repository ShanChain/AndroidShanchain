package com.shanchain.shandata.mvp.model;

/**
 * Created by zhoujian on 2017/7/16.
 */

public class RecommendedCityInfo {
    private String cityName;
    private boolean isSeletcted;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isSeletcted() {
        return isSeletcted;
    }

    public void setSeletcted(boolean seletcted) {
        isSeletcted = seletcted;
    }
}
