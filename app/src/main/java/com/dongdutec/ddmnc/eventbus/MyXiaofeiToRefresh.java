package com.dongdutec.ddmnc.eventbus;

/**
 * @作者 董斌
 * eml.dongbinjava@163.com
 * @创建日期 2019/9/27 11:39
 */
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
