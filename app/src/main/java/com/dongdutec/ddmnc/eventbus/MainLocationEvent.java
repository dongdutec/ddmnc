package com.dongdutec.ddmnc.eventbus;


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
