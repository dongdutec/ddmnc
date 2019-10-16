package com.dongdutec.ddmnc.http;


public class HtmlUrls {
    public static String HtmlUrlRelease = "http://47.75.47.121:8080/mnc/";//正式地址


    /**
     * 平台简介页面
     *
     * @return
     */
    public static String getPlatforms() {
        return HtmlUrlRelease + "platform.html";
    }

    /**
     * 浏览器页面
     * 需要传参：token
     *
     * @return
     */
    public static String getBrowsers() {
        return HtmlUrlRelease + "browser.html";
    }

    /**
     * 商家码页面
     * 需要传参：token,shopId,shopName,customerId
     *
     * @return
     */
    public static String getBusinessCode() {
        return HtmlUrlRelease + "businessCode.html";
    }

    /**
     * 更改手机号
     * 需要传参：旧手机号oldPhone
     *
     * @return
     */
    public static String getChangePhone() {
        return HtmlUrlRelease + "changePhone.html";
    }

    /**
     * 我的专属码
     * 需要传参：token
     *
     * @return
     */
    public static String getExclusives() {
        return HtmlUrlRelease + "exclusive.html";
    }

    /**
     * 消息
     * 需要传参：token
     *
     * @return
     */
    public static String getMessages() {
        return HtmlUrlRelease + "message.html";
    }

    /**
     * 钱包
     * 需要传参：token
     *
     * @return
     */
    public static String getPurse() {
        return HtmlUrlRelease + "purse.html";
    }

    /**
     * 服务条款
     *
     * @return
     */
    public static String getServiceInfo() {
        return HtmlUrlRelease + "serviceInfo.html";
    }

    /**
     * 商家入驻
     * 需要传参：token
     *
     * @return
     */
    public static String getSettled() {
        return HtmlUrlRelease + "settled.html";
    }

    /**
     * 商家详情
     * 需要传参：token shopId
     *
     * @return
     */
    public static String getStoreDetail() {
        return HtmlUrlRelease + "storeDetail.html";
    }


}
