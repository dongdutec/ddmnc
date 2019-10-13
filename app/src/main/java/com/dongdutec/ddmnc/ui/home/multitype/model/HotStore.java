package com.dongdutec.ddmnc.ui.home.multitype.model;

/**
 * @作者 董斌
 * eml.dongbinjava@163.com
 * @创建日期 2019/9/17 12:19
 */
public class HotStore {
    private String storeId;
    private String imageUrl;
    private String storeName;
    private String id;
    private String locationStr;
    private int count;
    private String starState; // 1 已收藏  2 未收藏  其它 不显示
    private double lantitude;
    private double longitude;
    private double distance;
    private boolean isFirst = false;

    public HotStore() {
    }

    public HotStore(String storeId, String imageUrl, String storeName, String id, String locationStr, int count, String starState, double lantitude, double longitude, double distance, boolean isFirst) {
        this.storeId = storeId;
        this.imageUrl = imageUrl;
        this.storeName = storeName;
        this.id = id;
        this.locationStr = locationStr;
        this.count = count;
        this.starState = starState;
        this.lantitude = lantitude;
        this.longitude = longitude;
        this.distance = distance;
        this.isFirst = isFirst;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLantitude() {
        return lantitude;
    }

    public void setLantitude(double lantitude) {
        this.lantitude = lantitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStarState() {
        return starState;
    }

    public void setStarState(String starState) {
        this.starState = starState;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
