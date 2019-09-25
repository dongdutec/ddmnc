package com.dongdutec.ddmnc.ui.home.multitype.beans;

/**
 * @作者 董斌
 * eml.dongbinjava@163.com
 * @创建日期 2019/9/24 10:21
 */
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
