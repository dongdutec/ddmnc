package com.dongdutec.ddmnc.http;

/**
 * @作者 董斌
 * eml.dongbinjava@163.com
 * @创建日期 2019/9/18 9:58
 */
public class RequestUrls {
    public static String CommonUrlRelease = "http://47.75.47.121:8080/gtl/";//正式地址
    public static String CommonUrlTest = "http://192.168.0.154:8080/gtl_war_exploded/";//测试地址
    public static String CommonUrlTest_GANG = "http://192.168.0.104:8080/";//测试地址刚
    public static String CommonUrlTest_HAO = "http://192.168.0.154:8080/";//测试地址浩
    public static String CommonUrl = CommonUrlRelease;//切换


    /**
     * 获取验证码
     *
     * @return
     */
    public static String getMsgCode() {
        return CommonUrl + "api/common/getSmsCode.do";
    }

    /**
     * 用户注册
     *
     * @return
     */
    public static String userRegister() {
        return CommonUrl + "api/common/regiestUser.do";
    }

    /**
     * 用户登录
     *
     * @return
     */
    public static String userLogin() {
        return CommonUrl + "api/common/login.do";
    }

    /**
     * 首页Banner图 已弃
     *
     * @return
     */
    public static String getHomeBanner() {
        return CommonUrl + "api/customer/selectSysBanner.do";
    }

    /**
     * 首页Banner图new
     * 返参 type 0 轮播图 1 置顶图
     *
     * @return
     */
    public static String getHomeBannerNew() {
        return CommonUrl + "/api/customer/searchPicture.do";
    }

    /**
     * 忘记密码
     *
     * @return
     */
    public static String forgetpassword() {
        return CommonUrl + "api/common/forgetPass.do";
    }

    /**
     * 自动登录
     *
     * @return
     */
    public static String judgelogin() {
        return CommonUrl + "api/shopAdvert/getShopClassify.do";
    }

    /**
     * 首页热门推荐
     *
     * @return
     */
    public static String homeHotList() {
        return CommonUrl + "api/common/getHot.do";
    }

    /**
     * 我的收藏列表
     *
     * @return
     */
    public static String getStarList() {
        return CommonUrl + "api/customer/searchCollect.do";
    }

    /**
     * 更改收藏状态
     * id, state
     *
     * @return
     */
    public static String updataStartState() {
        return CommonUrl + "api/customer/updateCollect.do";
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
        return CommonUrl + "api/customer/modifyCustomerInfo.do";
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
        return CommonUrl + "api/common/modifyCustomerHead.do";
    }

    /**
     * 上传文件
     * file
     *
     * @return
     */
    public static String uploadImage() {
        return CommonUrl + "api/common/uploadImage.do";
    }

    /**
     * 浏览历史
     *
     * @return
     */
    public static String getHistory() {
        return CommonUrl + "api/customer/searchHistory.do";
    }

    /**
     * 我的页面数据
     *
     * @return
     */
    public static String getMyData() {
        return CommonUrl + "api/customer/searchMy.do";
    }

    /**
     * 我的消费（商家/个人）
     * <p>
     * <p>
     * 返参
     * id createTime image money appraise (0 未评价 1 已评价)
     * state 0待记账1已记账 2取消记账
     *
     * @return
     */
    public static String getMyXiaofei() {
        return CommonUrl + "api/customer/searchConConsume.do";
    }

    /**
     * 商家确认记账返回积分
     * String id, String mp , String state(1 记账 2 取消记账)
     * <p>
     * 返参 直接返回    成功/失败 （无需解析）
     *
     * @return
     */
    public static String storeGetJifen() {
        return CommonUrl + "api/customer/udpateConConsume.do";
    }

    /**
     * 用户去评价
     * String token, String shopId , String remake ,String id
     * <p>
     * 返参 直接返回    成功/失败 （无需解析）
     *
     * @return
     */
    public static String insertDiscuss() {
        return CommonUrl + "api/customer/insertDiscuss.do";
    }

    /**
     * 首页8个按钮文本和更多按钮文本以及图片
     * 请求无参
     * <p>
     * 返参
     *
     * @return
     */
    public static String getHomeBtns() {
        return CommonUrl + "api/shopAdvert/searchClassify.do";
    }

    /**
     * 首页搜一搜/8个按钮内容排序
     * 请求  classifyId   city  isNew升序传""降序传"0"  sale升序传""降序传"0"  page  rows  shopName
     * <p>
     * 返参
     *
     * @return
     */
    public static String homeSearch() {
        return CommonUrl + "api/shopAdvert/searchAdvert.do";
    }

    /**
     * 改变收藏状态
     * 请求  state (1 收藏  2取消)  id   token
     * 返参
     *
     * @return
     */
    public static String changeStarState() {
        return CommonUrl + "api/customer/updateCollect.do";
    }
}
