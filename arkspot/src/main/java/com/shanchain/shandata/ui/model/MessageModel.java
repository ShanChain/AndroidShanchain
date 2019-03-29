package com.shanchain.shandata.ui.model;

/**
 * Created by WealChen
 * Date : 2019/3/28
 * Describe :
 */
public class MessageModel {
    /**
     * version : 1
     * targetId : 15296728
     * fromId : 155293145900600163e0479799076126
     * fromName : 1015627273
     * fromType : user
     * fromPlatform : i
     * msgBody : {"text":"那区块链怎么用到果业呢？","extras":null,"width":null,"height":null,"format":null,"fsize":null,"mediaCrc32":null,"mediaId":null,"resultOK":false,"responseCode":-1,"originalContent":null,"rateLimitRemaining":0,"rateLimitReset":0,"rateLimitQuota":0}
     * msgLevel : 0
     * msgCtime : 1553222785613
     * noOffline : null
     * noNotification : null
     * targetType : chatroom
     * targetName : 29.56160760,106.58279282
     * msgId : 314305596
     * createTime : 1553222785466
     * msgType : text
     * resultOK : false
     * responseCode : -1
     * originalContent : null
     * rateLimitRemaining : 0
     * rateLimitReset : 0
     * rateLimitQuota : 0
     */

    private int version;
    private String targetId;
    private String fromId;
    private String fromName;
    private String fromType;
    private String fromPlatform;
    private MsgBodyBean msgBody;
    private int msgLevel;
    private long msgCtime;
    private Object noOffline;
    private Object noNotification;
    private String targetType;
    private String targetName;
    private int msgId;
    private long createTime;
    private String msgType;
    private boolean resultOK;
    private int responseCode;
    private Object originalContent;
    private int rateLimitRemaining;
    private int rateLimitReset;
    private int rateLimitQuota;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getFromPlatform() {
        return fromPlatform;
    }

    public void setFromPlatform(String fromPlatform) {
        this.fromPlatform = fromPlatform;
    }

    public MsgBodyBean getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(MsgBodyBean msgBody) {
        this.msgBody = msgBody;
    }

    public int getMsgLevel() {
        return msgLevel;
    }

    public void setMsgLevel(int msgLevel) {
        this.msgLevel = msgLevel;
    }

    public long getMsgCtime() {
        return msgCtime;
    }

    public void setMsgCtime(long msgCtime) {
        this.msgCtime = msgCtime;
    }

    public Object getNoOffline() {
        return noOffline;
    }

    public void setNoOffline(Object noOffline) {
        this.noOffline = noOffline;
    }

    public Object getNoNotification() {
        return noNotification;
    }

    public void setNoNotification(Object noNotification) {
        this.noNotification = noNotification;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public boolean isResultOK() {
        return resultOK;
    }

    public void setResultOK(boolean resultOK) {
        this.resultOK = resultOK;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Object getOriginalContent() {
        return originalContent;
    }

    public void setOriginalContent(Object originalContent) {
        this.originalContent = originalContent;
    }

    public int getRateLimitRemaining() {
        return rateLimitRemaining;
    }

    public void setRateLimitRemaining(int rateLimitRemaining) {
        this.rateLimitRemaining = rateLimitRemaining;
    }

    public int getRateLimitReset() {
        return rateLimitReset;
    }

    public void setRateLimitReset(int rateLimitReset) {
        this.rateLimitReset = rateLimitReset;
    }

    public int getRateLimitQuota() {
        return rateLimitQuota;
    }

    public void setRateLimitQuota(int rateLimitQuota) {
        this.rateLimitQuota = rateLimitQuota;
    }

    public static class MsgBodyBean {
        /**
         * text : 那区块链怎么用到果业呢？
         * extras : null
         * width : null
         * height : null
         * format : null
         * fsize : null
         * mediaCrc32 : null
         * mediaId : null
         * resultOK : false
         * responseCode : -1
         * originalContent : null
         * rateLimitRemaining : 0
         * rateLimitReset : 0
         * rateLimitQuota : 0
         */

        private String text;
        private String extras;
        private int width;
        private int height;
        private String format;
        private int fsize;
        private String mediaCrc32;
        private String mediaId;
        private boolean resultOK;
        private int responseCode;
        private String originalContent;
        private int rateLimitRemaining;
        private int rateLimitReset;
        private int rateLimitQuota;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getExtras() {
            return extras;
        }

        public void setExtras(String extras) {
            this.extras = extras;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public int getFsize() {
            return fsize;
        }

        public void setFsize(int fsize) {
            this.fsize = fsize;
        }

        public String getMediaCrc32() {
            return mediaCrc32;
        }

        public void setMediaCrc32(String mediaCrc32) {
            this.mediaCrc32 = mediaCrc32;
        }

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }

        public boolean isResultOK() {
            return resultOK;
        }

        public void setResultOK(boolean resultOK) {
            this.resultOK = resultOK;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }

        public Object getOriginalContent() {
            return originalContent;
        }

        public void setOriginalContent(String originalContent) {
            this.originalContent = originalContent;
        }

        public int getRateLimitRemaining() {
            return rateLimitRemaining;
        }

        public void setRateLimitRemaining(int rateLimitRemaining) {
            this.rateLimitRemaining = rateLimitRemaining;
        }

        public int getRateLimitReset() {
            return rateLimitReset;
        }

        public void setRateLimitReset(int rateLimitReset) {
            this.rateLimitReset = rateLimitReset;
        }

        public int getRateLimitQuota() {
            return rateLimitQuota;
        }

        public void setRateLimitQuota(int rateLimitQuota) {
            this.rateLimitQuota = rateLimitQuota;
        }
    }
}
