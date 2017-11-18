package com.shanchain.shandata.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/9.
 */

public class UpLoadImgBean {


    /**
     * accessKeyId : STS.DNC1ZBBrzH59ZLiyyo4fyAVi8
     * accessKeySecret : 2M5a3AJ3KFMC9T3E1BYheC9NpQESNmeVW6wGWADKaNWc
     * bucket : shanchain-seller
     * endPoint : oss-cn-hongkong.aliyuncs.com
     * expiration : 2017-10-09T09:28:09Z
     * host : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com
     * securityToken : CAISqAN1q6Ft5B2yfSjIpY32euD2r61b//fSWGrYnXk6OOlVrpPC2jz2IHxJfHBsCewWt/8/mG1V7v8dlqB6T55OSAmcNZIoP23lGpviMeT7oMWQweEuDvTHcDHhi3eZsebWZ+LmNjm0GJOEYEzFkSle2KbzcS7YMXWuLZyOj+xlDLEQRRLqQjdaI91UKwB+yqodLmCDDeuxFRToj2HMbjJvoREupW575Liy5cee5xHC7jj90fRHg4XqPqCtddV3Xud4SMzn9eZxbLbkzSpM6gBD7rtLlKhD8Dul2daGGAt14g6aFODW/9ZzN3VLBM4AFrVDseL3mNBhp+XXjP6X8RtWOvxPWCmtEuLWycDfSuSyLYR7J/SpIHTE1NuPP5jz9hgpZXYWLkQIGa4oIWQiDgc3GHOIaP28+FnMaQeqSq7Ay6wy1Zdv7S2xrIHaeAbfEu7EiXpFYsBjVS5yaU5Kh16GW7QdbglBf2lgA7uoVohpZHhc0/i04FWLCn08nigM76GjOquP5bpsYIH+T49A1pEGeJNFvm0lQlL6UbuyjV0Oc2hoUQuxC0xIuE/UGoABQHK/haXd1D1XmDQsaoOBTyrcrz+tSUuEFELaS1srwmw2cYd1uvtbqBYJpXaQcXlVwM4WbFBGiaSOpZ0gjqpqsrOoFRLs2Euzu1SyzSSCj4CnXPVXafHgvrM6ERbhL7y2ZZTHTjwCQeWKE6GuBkPmrK2p7sjDktxmRCC28M8gFek=
     * status : 200
     * uuidList : ["7a979b339c594f8398e08ac533e1542b","af1e4d1e64b24af1b5da35ae36b6b100","72c120142d8748d7936a9a8e4ce629cc"]
     */

    private String accessKeyId;
    private String accessKeySecret;
    private String bucket;
    private String endPoint;
    private String expiration;
    private String host;
    private String securityToken;
    private String status;
    private List<String> uuidList;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getUuidList() {
        return uuidList;
    }

    public void setUuidList(List<String> uuidList) {
        this.uuidList = uuidList;
    }

    @Override
    public String toString() {
        return "UpLoadImgBean{" +
                "accessKeyId='" + accessKeyId + '\n' +
                ", accessKeySecret='" + accessKeySecret + '\n' +
                ", bucket='" + bucket + '\n' +
                ", endPoint='" + endPoint + '\n' +
                ", expiration='" + expiration + '\n' +
                ", host='" + host + '\n' +
                ", securityToken='" + securityToken + '\n' +
                ", status='" + status + '\n' +
                ", uuidList=" + uuidList +
                '}';
    }
}
