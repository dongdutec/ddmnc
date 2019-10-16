package com.dongdutec.ddmnc.ui.home.multitype.beans;

public class MidBannerBeans {
    private String img ;
    private String url;
    public MidBannerBeans() {
    }

    public MidBannerBeans(String img, String url) {
        this.img = img;
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
