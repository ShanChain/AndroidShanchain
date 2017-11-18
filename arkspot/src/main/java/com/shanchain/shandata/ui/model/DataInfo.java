package com.shanchain.shandata.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/24.
 */

public class DataInfo {


    /**
     * title : 赵美红
     * photo : http://n.zhumei.net/data/upload/market_26/20170428/zmh.jpg
     * business_licence :
     * business_img : http://n.zhumei.net/data/upload/market_26/20170428/zmh_yyzz.jpg
     * scope : 鲜肉
     * star : 3
     * number : 26
     * price_config : 1
     * price_list : [{"id":"9228","goods_id":"3","goods_name":"猪肉","price":"2.00","is_sale":"0","unit_name":"元/斤"},{"id":"9229","goods_id":"4","goods_name":"三花肉","price":"2.00","is_sale":"0","unit_name":"元/斤"},{"id":"9230","goods_id":"22","goods_name":"后腿肉","price":"2.00","is_sale":"0","unit_name":"元/斤"},{"id":"9231","goods_id":"354","goods_name":"羊肉","price":"2.00","is_sale":"0","unit_name":"元/斤"}]
     * price_list_all : [{"id":"9228","goods_id":"3","goods_name":"猪肉","price":"2.00","is_sale":"0","unit_name":"元/斤"},{"id":"9229","goods_id":"4","goods_name":"三花肉","price":"2.00","is_sale":"0","unit_name":"元/斤"},{"id":"9230","goods_id":"22","goods_name":"后腿肉","price":"2.00","is_sale":"0","unit_name":"元/斤"},{"id":"9231","goods_id":"354","goods_name":"羊肉","price":"2.00","is_sale":"0","unit_name":"元/斤"}]
     * thumb : 13
     * qrcodeurl : http://nm.zhumei.net/index.php?g=Nmadmin&m=merchantCode&a=codeImg&mid=26&sid=2031
     * floor : 一层
     * ad_list : ["http://n.zhumei.net/public/images/z1.jpg","http://n.zhumei.net/public/images/z3.jpg"]
     * ad_right_list : []
     * ad_type : 0
     * right_title : 我，农贸市场经营户郑重承诺:
     * right_content : 本商位绝不出售假冒伪劣、过期变质的商品；食品安全可追溯，票据齐全、价格公道、计量准确；诚信经营，文明经商
     * right_name : 承诺人:
     * ercode_title : 点评/点赞/投诉
     * price_title : 今日菜价
     * stall_id : 2031
     * market_id : 26
     * merchant_id : 2011
     * movie_path : D:\phpStudy\WWW\src\download.mp4
     */

    private String title;
    private String photo;
    private String business_licence;
    private String business_img;
    private String scope;
    private String star;
    private String number;
    private int price_config;
    private int thumb;
    private String qrcodeurl;
    private String floor;
    private String ad_type;
    private String right_title;
    private String right_content;
    private String right_name;
    private String ercode_title;
    private String price_title;
    private String stall_id;
    private String market_id;
    private String merchant_id;
    private String movie_path;
    private List<DataPriceBean> price_list;
    private List<DataPriceBean> price_list_all;
    private List<String> ad_list;
    private List<?> ad_right_list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBusiness_licence() {
        return business_licence;
    }

    public void setBusiness_licence(String business_licence) {
        this.business_licence = business_licence;
    }

    public String getBusiness_img() {
        return business_img;
    }

    public void setBusiness_img(String business_img) {
        this.business_img = business_img;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getPrice_config() {
        return price_config;
    }

    public void setPrice_config(int price_config) {
        this.price_config = price_config;
    }

    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }

    public String getQrcodeurl() {
        return qrcodeurl;
    }

    public void setQrcodeurl(String qrcodeurl) {
        this.qrcodeurl = qrcodeurl;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public String getRight_title() {
        return right_title;
    }

    public void setRight_title(String right_title) {
        this.right_title = right_title;
    }

    public String getRight_content() {
        return right_content;
    }

    public void setRight_content(String right_content) {
        this.right_content = right_content;
    }

    public String getRight_name() {
        return right_name;
    }

    public void setRight_name(String right_name) {
        this.right_name = right_name;
    }

    public String getErcode_title() {
        return ercode_title;
    }

    public void setErcode_title(String ercode_title) {
        this.ercode_title = ercode_title;
    }

    public String getPrice_title() {
        return price_title;
    }

    public void setPrice_title(String price_title) {
        this.price_title = price_title;
    }

    public String getStall_id() {
        return stall_id;
    }

    public void setStall_id(String stall_id) {
        this.stall_id = stall_id;
    }

    public String getMarket_id() {
        return market_id;
    }

    public void setMarket_id(String market_id) {
        this.market_id = market_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMovie_path() {
        return movie_path;
    }

    public void setMovie_path(String movie_path) {
        this.movie_path = movie_path;
    }

    public List<DataPriceBean> getPrice_list() {
        return price_list;
    }

    public void setPrice_list(List<DataPriceBean> price_list) {
        this.price_list = price_list;
    }

    public List<DataPriceBean> getPrice_list_all() {
        return price_list_all;
    }

    public void setPrice_list_all(List<DataPriceBean> price_list_all) {
        this.price_list_all = price_list_all;
    }

    public List<String> getAd_list() {
        return ad_list;
    }

    public void setAd_list(List<String> ad_list) {
        this.ad_list = ad_list;
    }

    public List<?> getAd_right_list() {
        return ad_right_list;
    }

    public void setAd_right_list(List<?> ad_right_list) {
        this.ad_right_list = ad_right_list;
    }
}
