package com.dongdutec.ddmnc.ui.home.multitype.model;

/**
 * @作者 董斌
 * eml.dongbinjava@163.com
 * @创建日期 2019/9/17 12:19
 */
public class HotStore {
    private int storeId;
    private String imageUrl;
    private String storeName;
    private String locationStr;
    private int count;
    private int distance;

    public HotStore() {
    }

    public HotStore(int storeId, String imageUrl, String storeName, String locationStr, int count, int distance) {
        this.storeId = storeId;
        this.imageUrl = imageUrl;
        this.storeName = storeName;
        this.locationStr = locationStr;
        this.count = count;
        this.distance = distance;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLocationStr() {
        return locationStr;
    }

    public void setLocationStr(String locationStr) {
        this.locationStr = locationStr;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
