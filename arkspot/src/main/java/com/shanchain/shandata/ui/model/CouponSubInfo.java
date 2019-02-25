package com.shanchain.shandata.ui.model;

public class CouponSubInfo extends CouponInfo {

    /**
     * isNewRecord : false
     * id : 15465071703871785
     * subCoupId : 15465071703871785
     * userId : 3
     * subuserId : 3
     * tokenName : 马来
     * amount : 0
     * vendorUser : 100493
     * vendorSubuser : 143
     * price : 10.0
     * tokenStatus : 2
     * roomid : 111
     * getTime : 2019-01-03 17:19:30
     * useTime:
     * photoUrl : http://hihihihih
     * tokenSymbol : BBBAAAA
     * detail : 每次只能消费一张
     * getHash : 953061500d95e88d3c3a8707762b812a779f147114d07b7c3bbf792a1cd384d4
     * useHash : 7c69182c517b112a36c62d7d42d201deba880c1f382f97266ea9d4a0f9c9e454
     * couponsToken	 :     //核销凭证
     */

    private boolean isNewRecord;
    private String id;
    private String subCoupId;
    private int userId;
    private int subuserId;
    private String tokenName;
    private int amount;
    private int vendorUser;
    private int vendorSubuser;
    private String price;
    private int tokenStatus;
    private String roomid;
    private String getTime;
    private String useTime;
    private String photoUrl;
    private String tokenSymbol;
    private String detail;
    private String getHash;
    private String useHash;
    private String unusedNum;
    private String usedNum;
    private String couponsToken;

    public static final int CREATE_WAIT = 0;// 创建方待核销
    public static final int CREATE_INVALID = 1;// 创建方已失效
    public static final int RECEIVER = 10;// 领取方已领取
    public static final int RECEIVER_USE = 11;// 领取方已使用
    public static final int RECEIVER_UN_USE = 12;//
    public static final int RECEIVER_INVALID = 13;// 领取方已失效

    public String getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(String usedNum) {
        this.usedNum = usedNum;
    }

    public boolean isIsNewRecord() {
        return isNewRecord;
    }

    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubCoupId() {
        return subCoupId;
    }

    public void setSubCoupId(String subCoupId) {
        this.subCoupId = subCoupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSubuserId() {
        return subuserId;
    }

    public void setSubuserId(int subuserId) {
        this.subuserId = subuserId;
    }

    public String getTokenName() {
        return tokenName;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getCouponsToken() {
        return couponsToken;
    }

    public void setCouponsToken(String couponsToken) {
        this.couponsToken = couponsToken;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getVendorUser() {
        return vendorUser;
    }

    public void setVendorUser(int vendorUser) {
        this.vendorUser = vendorUser;
    }

    public int getVendorSubuser() {
        return vendorSubuser;
    }

    public void setVendorSubuser(int vendorSubuser) {
        this.vendorSubuser = vendorSubuser;
    }

    public String getUnusedNum() {
        return unusedNum;
    }

    public void setUnusedNum(String unusedNum) {
        this.unusedNum = unusedNum;
    }

    public int getTokenStatus() {
        return tokenStatus;
    }

    public void setTokenStatus(int tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getGetTime() {
        return getTime;
    }

    public void setGetTime(String getTime) {
        this.getTime = getTime;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getGetHash() {
        return getHash;
    }

    public void setGetHash(String getHash) {
        this.getHash = getHash;
    }

    public String getUseHash() {
        return useHash;
    }

    public void setUseHash(String useHash) {
        this.useHash = useHash;
    }


}
