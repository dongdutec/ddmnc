package com.dongdutec.ddmnc.eventbus;


public class MyXiaofeiToRefresh {
    private String state;
    private String isStore;

    public MyXiaofeiToRefresh() {
    }

    public MyXiaofeiToRefresh(String state, String isStore) {
        this.state = state;
        this.isStore = isStore;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIsStore() {
        return isStore;
    }

    public void setIsStore(String isStore) {
        this.isStore = isStore;
    }
}
