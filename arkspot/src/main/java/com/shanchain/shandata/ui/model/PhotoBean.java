package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by WealChen
 * Date : 2019/7/24
 * Describe :
 */
public class PhotoBean implements Serializable {
    String url;
    String fileName;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
