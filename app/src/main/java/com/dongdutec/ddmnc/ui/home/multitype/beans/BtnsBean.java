package com.dongdutec.ddmnc.ui.home.multitype.beans;


public class BtnsBean {
    private String classifyImg;
    private String classifyName;
    private String id;

    public BtnsBean() {
    }

    public BtnsBean(String classifyImg, String classifyName, String id) {
        this.classifyImg = classifyImg;
        this.classifyName = classifyName;
        this.id = id;
    }

    public String getClassifyImg() {
        return classifyImg;
    }

    public void setClassifyImg(String classifyImg) {
        this.classifyImg = classifyImg;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
