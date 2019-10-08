package com.dongdutec.ddmnc.ui.home.multitype.model;

/**
 * @作者 董斌
 * eml.dongbinjava@163.com
 * @创建日期 2019/9/17 16:22
 */
public class HeadImg {
    private String imgUrl;
    private String webUrl;

    public HeadImg() {
    }

    public HeadImg(String imgUrl, String webUrl) {
        this.imgUrl = imgUrl;
        this.webUrl = webUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
