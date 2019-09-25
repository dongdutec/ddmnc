package com.dongdutec.ddmnc.http;

/**
 * @作者 董斌
 * eml.dongbinjava@163.com
 * @创建日期 2019/9/18 9:58
 */
public class RequestUrls {
    public static String CommonUrlRelease = "";//正式地址
    public static String CommonUrlTest = "http://192.168.0.104:8080/";//测试地址
    public static String CommonUrlTest_HAO = "http://192.168.0.154:8080/";//测试地址
    public static String CommonUrl = CommonUrlTest;//切换


    /**
     * 获取验证码
     *
     * @return
     */
    public static String getMsgCode() {
        return CommonUrl + "gtl_war_exploded/api/common/getSmsCode.do";
    }

    /**
     * 用户注册
     *
     * @return
     */
    public static String userRegister() {
        return CommonUrl + "gtl_war_exploded/api/common/regiestUser.do";
    }

    /**
     * 用户登录
     *
     * @return
     */
    public static String userLogin() {
        return CommonUrl + "gtl_war_exploded/api/common/login.do";
    }

    /**
     * 首页Banner图
     *
     * @return
     */
    public static String getHomeBanner() {
        return CommonUrl + "gtl_war_exploded/api/customer/selectSysBanner.do";
    }

    /**
     * 忘记密码
     *
     * @return
     */
    public static String forgetpassword() {
        return CommonUrl + "gtl_war_exploded/api/common/forgetPass.do";
    }

    /**
     * 自动登录
     *
     * @return
     */
    public static String judgelogin() {
        return CommonUrl + "gtl_war_exploded/api/shopAdvert/getShopClassify.do";
    }

    /**
     * 首页热门推荐
     *
     * @return
     */
    public static String homeHotList() {
        return CommonUrlTest_HAO + "gtl_war_exploded/api/common/getHot.do";
    }

    /**
     * 我的收藏列表
     *
     * @return
     */
    public static String getStarList() {
        return CommonUrlTest_HAO + "gtl_war_exploded/api/customer/searchCollect.do";
    }
}
