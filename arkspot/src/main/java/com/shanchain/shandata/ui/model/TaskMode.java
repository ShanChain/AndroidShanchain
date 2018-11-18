package com.shanchain.shandata.ui.model;

import java.util.List;

public class TaskMode {

    /**
     * content : [{"taskId":55,"verify":0,"bounty":0.01,"topping":0,"intro":"测试","roomId":"15198852","statusHistory":"[\"已发布\"]","createTime":1541853537000,"expiryTime":1541851200000,"status":5,"receiveCount":0,"commentCount":0,"supportCount":0,"characterId":126,"verifyTime":null,"unfinishedTime":null,"currency":"rmb","releaseHash":"0xS0VXAJ1az0gH","lastHash":null,"userId":63}]
     * totalPages : 1
     * last : true
     * totalElements : 1
     * number : 0
     * size : 10
     * sort : [{"direction":"DESC","property":"createTime","ignoreCase":false,"nullHandling":"NATIVE","ascending":false,"descending":true}]
     * first : true
     * numberOfElements : 1
     */

    private int totalPages;
    private boolean last;
    private int totalElements;
    private int number;
    private int size;
    private boolean first;
    private int numberOfElements;
    private List<ContentBean> content;
    private List<SortBean> sort;

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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public List<SortBean> getSort() {
        return sort;
    }

    public void setSort(List<SortBean> sort) {
        this.sort = sort;
    }

    public static class ContentBean {
        /**
         * taskId : 55
         * verify : 0
         * bounty : 0.01
         * topping : 0
         * intro : 测试
         * roomId : 15198852
         * statusHistory : ["已发布"]
         * createTime : 1541853537000
         * expiryTime : 1541851200000
         * status : 5
         * receiveCount : 0
         * commentCount : 0
         * supportCount : 0
         * characterId : 126
         * verifyTime : null
         * unfinishedTime : null
         * currency : rmb
         * releaseHash : 0xS0VXAJ1az0gH
         * lastHash : null
         * userId : 63
         */

        private int taskId;
        private int verify;
        private double bounty;
        private int topping;
        private String intro;
        private String roomId;
        private String statusHistory;
        private long createTime;
        private long expiryTime;
        private int status;
        private int receiveCount;
        private int commentCount;
        private int supportCount;
        private int characterId;
        private Object verifyTime;
        private Object unfinishedTime;
        private String currency;
        private String releaseHash;
        private Object lastHash;
        private int userId;

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public int getVerify() {
            return verify;
        }

        public void setVerify(int verify) {
            this.verify = verify;
        }

        public double getBounty() {
            return bounty;
        }

        public void setBounty(double bounty) {
            this.bounty = bounty;
        }

        public int getTopping() {
            return topping;
        }

        public void setTopping(int topping) {
            this.topping = topping;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getStatusHistory() {
            return statusHistory;
        }

        public void setStatusHistory(String statusHistory) {
            this.statusHistory = statusHistory;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getExpiryTime() {
            return expiryTime;
        }

        public void setExpiryTime(long expiryTime) {
            this.expiryTime = expiryTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getReceiveCount() {
            return receiveCount;
        }

        public void setReceiveCount(int receiveCount) {
            this.receiveCount = receiveCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getSupportCount() {
            return supportCount;
        }

        public void setSupportCount(int supportCount) {
            this.supportCount = supportCount;
        }

        public int getCharacterId() {
            return characterId;
        }

        public void setCharacterId(int characterId) {
            this.characterId = characterId;
        }

        public Object getVerifyTime() {
            return verifyTime;
        }

        public void setVerifyTime(Object verifyTime) {
            this.verifyTime = verifyTime;
        }

        public Object getUnfinishedTime() {
            return unfinishedTime;
        }

        public void setUnfinishedTime(Object unfinishedTime) {
            this.unfinishedTime = unfinishedTime;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getReleaseHash() {
            return releaseHash;
        }

        public void setReleaseHash(String releaseHash) {
            this.releaseHash = releaseHash;
        }

        public Object getLastHash() {
            return lastHash;
        }

        public void setLastHash(Object lastHash) {
            this.lastHash = lastHash;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }

    public static class SortBean {
        /**
         * direction : DESC
         * property : createTime
         * ignoreCase : false
         * nullHandling : NATIVE
         * ascending : false
         * descending : true
         */

        private String direction;
        private String property;
        private boolean ignoreCase;
        private String nullHandling;
        private boolean ascending;
        private boolean descending;

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public boolean isIgnoreCase() {
            return ignoreCase;
        }

        public void setIgnoreCase(boolean ignoreCase) {
            this.ignoreCase = ignoreCase;
        }

        public String getNullHandling() {
            return nullHandling;
        }

        public void setNullHandling(String nullHandling) {
            this.nullHandling = nullHandling;
        }

        public boolean isAscending() {
            return ascending;
        }

        public void setAscending(boolean ascending) {
            this.ascending = ascending;
        }

        public boolean isDescending() {
            return descending;
        }

        public void setDescending(boolean descending) {
            this.descending = descending;
        }
    }
}
