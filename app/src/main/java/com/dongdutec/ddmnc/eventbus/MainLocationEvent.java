package com.dongdutec.ddmnc.eventbus;

/**
 * @作者 董斌
 * eml.dongbinjava@163.com
 * @创建日期 2019/9/21 17:49
 */
public class MainLocationEvent {
    private String city;

    public MainLocationEvent() {
    }

    public MainLocationEvent(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
