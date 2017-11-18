package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/10/24.
 */

public class DataPriceBean {

    /**
     * id : 9228
     * goods_id : 3
     * goods_name : 猪肉
     * price : 2.00
     * is_sale : 0
     * unit_name : 元/斤
     */

    private String id;
    private String goods_id;
    private String goods_name;
    private String price;
    private String is_sale;
    private String unit_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIs_sale() {
        return is_sale;
    }

    public void setIs_sale(String is_sale) {
        this.is_sale = is_sale;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }
}
