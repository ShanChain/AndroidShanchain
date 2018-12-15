package com.shanchain.shandata.ui.model;

import android.graphics.drawable.Drawable;

public class App {

    private String appName;

    private String packageName;

    private String mainClassName;

    private Drawable appIcon;

    public App(String appName, String packageName, String mainClassName, Drawable appIcon) {
        this.appName = appName;
        this.packageName = packageName;
        this.mainClassName = mainClassName;
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getMainClassName() {
        return mainClassName;
    }

    public void setMainClassName(String mainClassName) {
        this.mainClassName = mainClassName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

}
