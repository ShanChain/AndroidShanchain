package com.shanchain.shandata.widgets.rEdit;

/**
 * Created by MryU93 on 2017/6/15.
 * Desc:
 */

public class InsertModel {
    private String insertRule;
    private String insertContent;
    private String insertColor;
    private int insertId;
    private String extra;

    public InsertModel(String insertRule, String insertContent, String insertColor, int insertId) {
        this.insertRule = insertRule;
        this.insertContent = insertContent;
        this.insertColor = insertColor;
        this.insertId = insertId;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getInsertId() {
        return insertId;
    }

    public void setInsertId(int insertId) {
        this.insertId = insertId;
    }

    public String getInsertRule() {
        return insertRule;
    }

    public void setInsertRule(String insertRule) {
        this.insertRule = insertRule;
    }

    public String getInsertContent() {
        return insertContent;
    }

    public void setInsertContent(String insertContent) {
        this.insertContent = insertContent;
    }

    public String getInsertColor() {
        return insertColor;
    }

    public void setInsertColor(String insertColor) {
        this.insertColor = insertColor;
    }

    @Override
    public String toString() {
        return "InsertModel{" +
                "insertRule='" + insertRule + '\'' +
                ", insertContent='" + insertContent + '\'' +
                ", insertColor='" + insertColor + '\'' +
                '}';
    }
}
