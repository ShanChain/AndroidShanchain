package com.shanchain.shandata.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhoujian on 2017/10/24.
 */

public class SpaceCharacterModelInfo implements Serializable{
    /**
     * content : [{"tagMap":[{"rate":0,"tagId":69,"tagName":"仙界"}],"characterNum":4,"createBy":1,"createTime":1505889439000,"disc":"  ","headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg","intro":"出自手游《王者荣耀》，作为一名炼金师，太乙真人大部分时间都宅在家里研究各种稀奇古怪的东西，现在是时候把这些发明拿到战场上试验一下威力了~","modelId":9,"name":"太乙真人","parentId":0,"spaceId":16,"status":1,"supportNum":677},{"tagMap":[{"rate":0,"tagId":69,"tagName":"仙界"}],"characterNum":4,"createBy":1,"createTime":1505889459000,"disc":"  ","headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg","intro":"长久以来，大陆一直流传着关于灭世魔神王的传说。他象征着绝对的黑暗，要将人间界带入永夜和毁灭之中，而他也许的大魔王的转身。","modelId":10,"name":"项羽","parentId":0,"spaceId":16,"status":1,"supportNum":0}]
     * first : true
     * last : false
     * number : 0
     * numberOfElements : 2
     * size : 2
     * sort : [{"ascending":true,"descending":false,"direction":"ASC","ignoreCase":false,"property":"createTime","undefinedHandling":"NATIVE"}]
     * totalElements : 19
     * totalPages : 10
     */

    private boolean first;
    private boolean last;
    private int number;
    private int numberOfElements;
    private int size;
    private int totalElements;
    private int totalPages;
    private List<SpaceCharacterBean> content;
    private List<ResponseSortBean> sort;

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<SpaceCharacterBean> getContent() {
        return content;
    }

    public void setContent(List<SpaceCharacterBean> content) {
        this.content = content;
    }

    public List<ResponseSortBean> getSort() {
        return sort;
    }

    public void setSort(List<ResponseSortBean> sort) {
        this.sort = sort;
    }
}
