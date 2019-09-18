package com.dongdutec.ddmnc.http;

/**
 * @作者 董斌
 * eml.dongbinjava@163.com
 * @创建日期 2019/9/18 9:58
 */
public class RequestUrls {
    public static String CommomRelease = "";//正式地址
    public static String CommomTest = "";//测试地址


    /**
     * 获取首页店铺列表
     *
     * @return
     */
    public static String getHomeList() {
        return CommomTest + "/homedata/storeList";
    }
}
