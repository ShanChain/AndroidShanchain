package com.shanchain.shandata.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhoujian on 2017/10/16.
 */

public class TagDataBean implements Serializable{

    /**
     * content : [{"tagId":24,"tagName":"前轴心时代","rate":0},{"tagId":25,"tagName":"原创","rate":0},{"tagId":26,"tagName":"历史","rate":0},{"tagId":27,"tagName":"动漫","rate":0},{"tagId":28,"tagName":"游戏","rate":0},{"tagId":29,"tagName":"小说","rate":0},{"tagId":30,"tagName":"影视","rate":0},{"tagId":31,"tagName":"娱乐圈","rate":0},{"tagId":32,"tagName":"古风","rate":0},{"tagId":33,"tagName":"现代","rate":0},{"tagId":34,"tagName":"耽美","rate":0},{"tagId":35,"tagName":"百合","rate":0},{"tagId":36,"tagName":"商界","rate":0},{"tagId":37,"tagName":"体育","rate":0},{"tagId":38,"tagName":"校园","rate":0},{"tagId":39,"tagName":"玄幻","rate":0},{"tagId":40,"tagName":"奇幻","rate":0},{"tagId":41,"tagName":"武侠","rate":0},{"tagId":42,"tagName":"仙侠","rate":0},{"tagId":43,"tagName":"科幻","rate":0}]
     * last : false
     * totalPages : 3
     * totalElements : 47
     * sort : null
     * numberOfElements : 20
     * first : true
     * size : 20
     * number : 0
     */

    private boolean last;
    private int totalPages;
    private int totalElements;
    //public String sort;
    private int numberOfElements;
    private boolean first;
    private int size;
    private int number;
    private List<TagContentBean> content;

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<TagContentBean> getContent() {
        return content;
    }

    public void setContent(List<TagContentBean> content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "TagDataBean{" +
                "last=" + last +
                ", totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                ", numberOfElements=" + numberOfElements +
                ", first=" + first +
                ", size=" + size +
                ", number=" + number +
                ", content=" + content +
                '}';
    }
}
