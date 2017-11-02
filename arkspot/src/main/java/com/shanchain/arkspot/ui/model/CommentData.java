package com.shanchain.arkspot.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/27.
 */

public class CommentData {


    /**
     * content : []
     * totalPages : 0
     * last : true
     * totalElements : 0
     * sort : [{"direction":"DESC","property":"createTime","ignoreCase":false,"nullHandling":"NATIVE","ascending":false,"descending":true}]
     * numberOfElements : 0
     * first : true
     * size : 10
     * number : 0
     */

    private int totalPages;
    private boolean last;
    private int totalElements;
    private int numberOfElements;
    private boolean first;
    private int size;
    private int number;
    private List<CommentBean> content;
    private List<ResponseSortBean> sort;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
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

    public List<CommentBean> getContent() {
        return content;
    }

    public void setContent(List<CommentBean> content) {
        this.content = content;
    }

    public List<ResponseSortBean> getSort() {
        return sort;
    }

    public void setSort(List<ResponseSortBean> sort) {
        this.sort = sort;
    }
}
