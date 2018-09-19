package com.shanchain.shandata.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/11/6.
 */

public class ResponseStoryIdData {

    /**
     * content : [{"chain":{"count":1,"datailIds":[]},"detailId":"s127"},{"chain":{"count":1,"datailIds":[]},"detailId":"s125"},{"chain":{"count":1,"datailIds":[]},"detailId":"s128"},{"chain":{"count":1,"datailIds":[]},"detailId":"s126"}]
     * last : true
     * totalPages : 1
     * totalElements : 4
     * sort : [{"direction":"DESC","property":"createTime","ignoreCase":false,"nullHandling":"NATIVE","descending":true,"ascending":false}]
     * first : true
     * numberOfElements : 4
     * size : 100
     * number : 0
     */

    private boolean last;
    private int totalPages;
    private int totalElements;
    private boolean first;
    private int numberOfElements;
    private int size;
    private int number;
    private List<ResponseStoryIdBean> content;
    private List<ResponseSortBean> sort;

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

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<ResponseStoryIdBean> getContent() {
        return content;
    }

    public void setContent(List<ResponseStoryIdBean> content) {
        this.content = content;
    }

    public List<ResponseSortBean> getSort() {
        return sort;
    }

    public void setSort(List<ResponseSortBean> sort) {
        this.sort = sort;
    }
}
