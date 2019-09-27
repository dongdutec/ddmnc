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
        return CommonUrlTest_HAO + "gtl_war_exploded/api/shopAdvert/getShopClassify.do";
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

    /**
     * 更改收藏状态
     * id, state
     *
     * @return
     */
    public static String updataStartState() {
        return CommonUrlTest_HAO + "gtl_war_exploded/api/customer/updateCollect.do";
    }

    /**
     * 修改昵称
     * 传参
     * token   
     * customerName   string  昵称
     *
     * @return
     */
    public static String changeNickname() {
        return CommonUrl + "gtl_war_exploded/api/customer/modifyCustomerInfo.do";
    }

    /**
     * 修改用户头像
     * 传参
     * token
     * headImg  string  用户头像
     *
     * @return
     */
    public static String changeImage() {
        return CommonUrl + "gtl_war_exploded/api/common/modifyCustomerHead.do";
    }

    /**
     * 上传文件
     * file
     *
     * @return
     */
    public static String uploadImage() {
        return CommonUrl + "gtl_war_exploded/api/common/uploadImage.do";
    }

    /**
     * 浏览历史
     *
     * @return
     */
    public static String getHistory() {
        return CommonUrlTest_HAO + "gtl_war_exploded/api/customer/searchHistory.do";
    }

    /**
     * 我的页面数据
     *
     * @return
     */
    public static String getMyData() {
        return CommonUrlTest_HAO + "gtl_war_exploded/api/customer/searchMy.do";
    }

    /**
     * 我的消费（商家/个人）
     *
     *
     * 返参
     * id createTime image money
     * state 0待记账1已记账 2取消记账
     *
     * @return
     */
    public static String getMyXiaofei() {
        return CommonUrlTest_HAO + "gtl_war_exploded/api/customer/searchConConsume.do";
    }

    /**
     * 商家确认记账返回积分
     * String id, String mp ,state(1 记账 2 取消记账)
     *
     * 返参 直接返回    成功/失败 （无需解析）
     *
     * @return
     */
    public static String storeGetJifen() {
        return CommonUrlTest_HAO + "gtl_war_exploded/api/customer/udpateConConsume.do";
    }
}
