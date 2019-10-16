package com.dongdutec.ddmnc.ui.my.multitype.model;


public class Daili {
    private int userId;
    private String headImgUrl;

    public Daili() {
    }

    public Daili(int userId, String headImgUrl) {
        this.userId = userId;
        this.headImgUrl = headImgUrl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
}
