package com.shanchain.shandata.ui.model;

public class HistoryMessage {

    /**
     * version : 1
     * targetType : chatroom
     * targetName : 20.04318544,110.31167518
     * msgId : 163612067
     * createTime : 1541917931104
     * msgType : text
     * noNotification : null
     * fromId : 1541754485526c85b76c9647a116406
     * fromName : 1541754485526c85b76c9647a116406
     * fromPlatform : i
     * msgBody : {"text":"测试","extras":null,"width":null,"height":null,"format":null,"fsize":null,"mediaCrc32":null,"mediaId":null,"responseCode":-1,"originalContent":null,"rateLimitReset":0,"rateLimitRemaining":0,"rateLimitQuota":0,"resultOK":false}
     * fromType : user
     * msgLevel : 0
     * msgCtime : 1541917931475
     * noOffline : null
     * targetId : 12826211
     * responseCode : -1
     * originalContent : null
     * rateLimitReset : 0
     * rateLimitRemaining : 0
     * rateLimitQuota : 0
     * resultOK : false
     */

    private int version;
    private String targetType;
    private String targetName;
    private int msgId;
    private long createTime;
    private String msgType;
    private Object noNotification;
    private String fromId;
    private String fromName;
    private String fromPlatform;
    private MsgBodyBean msgBody;
    private String fromType;
    private int msgLevel;
    private long msgCtime;
    private Object noOffline;
    private String targetId;
    private int responseCode;
    private Object originalContent;
    private int rateLimitReset;
    private int rateLimitRemaining;
    private int rateLimitQuota;
    private boolean resultOK;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public Object getNoNotification() {
        return noNotification;
    }

    public void setNoNotification(Object noNotification) {
        this.noNotification = noNotification;
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

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
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

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
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

    public int getRateLimitReset() {
        return rateLimitReset;
    }

    public void setRateLimitReset(int rateLimitReset) {
        this.rateLimitReset = rateLimitReset;
    }

    public int getRateLimitRemaining() {
        return rateLimitRemaining;
    }

    public void setRateLimitRemaining(int rateLimitRemaining) {
        this.rateLimitRemaining = rateLimitRemaining;
    }

    public int getRateLimitQuota() {
        return rateLimitQuota;
    }

    public void setRateLimitQuota(int rateLimitQuota) {
        this.rateLimitQuota = rateLimitQuota;
    }

    public boolean isResultOK() {
        return resultOK;
    }

    public void setResultOK(boolean resultOK) {
        this.resultOK = resultOK;
    }

    public static class MsgBodyBean {
        /**
         * text : 测试
         * extras : null
         * width : null
         * height : null
         * format : null
         * fsize : null
         * mediaCrc32 : null
         * mediaId : null
         * responseCode : -1
         * originalContent : null
         * rateLimitReset : 0
         * rateLimitRemaining : 0
         * rateLimitQuota : 0
         * resultOK : false
         */

        private String text;
        private Object extras;
        private Object width;
        private Object height;
        private Object format;
        private Object fsize;
        private Object mediaCrc32;
        private Object mediaId;
        private int responseCode;
        private Object originalContent;
        private int rateLimitReset;
        private int rateLimitRemaining;
        private int rateLimitQuota;
        private boolean resultOK;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Object getExtras() {
            return extras;
        }

        public void setExtras(Object extras) {
            this.extras = extras;
        }

        public Object getWidth() {
            return width;
        }

        public void setWidth(Object width) {
            this.width = width;
        }

        public Object getHeight() {
            return height;
        }

        public void setHeight(Object height) {
            this.height = height;
        }

        public Object getFormat() {
            return format;
        }

        public void setFormat(Object format) {
            this.format = format;
        }

        public Object getFsize() {
            return fsize;
        }

        public void setFsize(Object fsize) {
            this.fsize = fsize;
        }

        public Object getMediaCrc32() {
            return mediaCrc32;
        }

        public void setMediaCrc32(Object mediaCrc32) {
            this.mediaCrc32 = mediaCrc32;
        }

        public Object getMediaId() {
            return mediaId;
        }

        public void setMediaId(Object mediaId) {
            this.mediaId = mediaId;
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

        public int getRateLimitReset() {
            return rateLimitReset;
        }

        public void setRateLimitReset(int rateLimitReset) {
            this.rateLimitReset = rateLimitReset;
        }

        public int getRateLimitRemaining() {
            return rateLimitRemaining;
        }

        public void setRateLimitRemaining(int rateLimitRemaining) {
            this.rateLimitRemaining = rateLimitRemaining;
        }

        public int getRateLimitQuota() {
            return rateLimitQuota;
        }

        public void setRateLimitQuota(int rateLimitQuota) {
            this.rateLimitQuota = rateLimitQuota;
        }

        public boolean isResultOK() {
            return resultOK;
        }

        public void setResultOK(boolean resultOK) {
            this.resultOK = resultOK;
        }
    }
}
