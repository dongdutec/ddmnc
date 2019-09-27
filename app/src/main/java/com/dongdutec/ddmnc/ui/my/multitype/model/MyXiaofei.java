package com.dongdutec.ddmnc.ui.my.multitype.model;

/**
 * @作者 董斌
 * eml.dongbinjava@163.com
 * @创建日期 2019/9/20 15:25
 */
public class MyXiaofei {
    private String orderId;
    private String times;
    private String orderState;
    private String imgUrl;
    private String title;
    private double price;
    private String phone;
    private String isStore = "1";//1用户 2商家

    public MyXiaofei() {
    }

    public MyXiaofei(String orderId, String times, String orderState, String imgUrl, String title, double price, String phone, String isStore) {
        this.orderId = orderId;
        this.times = times;
        this.orderState = orderState;
        this.imgUrl = imgUrl;
        this.title = title;
        this.price = price;
        this.phone = phone;
        this.isStore = isStore;
    }

    public String getIsStore() {
        return isStore;
    }

    public void setIsStore(String isStore) {
        this.isStore = isStore;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
