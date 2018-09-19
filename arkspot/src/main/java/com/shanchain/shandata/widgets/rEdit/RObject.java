package com.shanchain.shandata.widgets.rEdit;

/**
 * Created by zhoujian on 2017/11/30.
 */

public class RObject {

    private String objectRule = "#";// 匹配规则
    private String objectText;// 高亮文本

    private String rulePrefix = "";
    private String ruleSuffix = "";

    public String getRulePrefix() {
        return rulePrefix;
    }

    public void setRulePrefix(String rulePrefix) {
        this.rulePrefix = rulePrefix;
    }

    public String getRuleSuffix() {
        return ruleSuffix;
    }

    public void setRuleSuffix(String ruleSuffix) {
        this.ruleSuffix = ruleSuffix;
    }

    public String getObjectRule() {
        return objectRule;
    }

    public void setObjectRule(String objectRule) {
        this.objectRule = objectRule;
    }

    public String getObjectText() {
        return objectText;
    }

    public void setObjectText(String objectText) {
        this.objectText = objectText;
    }

}
