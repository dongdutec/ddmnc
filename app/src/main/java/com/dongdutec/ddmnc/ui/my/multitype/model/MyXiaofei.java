package com.dongdutec.ddmnc.ui.my.multitype.model;


public class MyXiaofei {
    private String orderId;
    private long times;
    private String orderState;
    private String appraise;//是否评价标记
    private String imgUrl;
    private String title;
    private double price;
    private String phone;
    private String isStore = "1";//1用户 2商家
    private String storeName;
    private String storeId;
    private String customerId;

    public MyXiaofei() {
    }

    public MyXiaofei(String orderId, long times, String orderState, String appraise, String imgUrl, String title, double price, String phone, String isStore, String storeName, String storeId, String customerId) {
        this.orderId = orderId;
        this.times = times;
        this.orderState = orderState;
        this.appraise = appraise;
        this.imgUrl = imgUrl;
        this.title = title;
        this.price = price;
        this.phone = phone;
        this.isStore = isStore;
        this.storeName = storeName;
        this.storeId = storeId;
        this.customerId = customerId;
    }

    public String getAppraise() {
        return appraise;
    }

    public void setAppraise(String appraise) {
        this.appraise = appraise;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
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
