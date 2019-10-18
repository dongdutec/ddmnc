package com.dongdutec.ddmnc.http;

public class RequestUrls {
    public static String CommonUrlRelease = "http://47.75.47.121:8080/gtl/";//正式地址
    public static String CommonUrlTest = "http://192.168.0.154:8080/gtl_war_exploded/";//测试地址
    public static String CommonUrlTest_GANG = "http://192.168.0.104:8080/";//测试地址刚
    public static String CommonUrlTest_HAO = "http://192.168.0.154:8080/";//测试地址浩
    public static String CommonUrl = CommonUrlRelease;//切换


    /**
     * 验证token
     * 传参 token
     *
     * @return
     */
    public static String getJudgeToken() {
        return CommonUrl + "api/customer/searchToken.do";
    }

    /**
     * 获取验证码
     * phone smsType(1注册 2忘记密码)
     *
     * @return
     */
    public static String getMsgCode() {
        return CommonUrl + "api/common/getSmsCode.do";
    }

    /**
     * 获取验证码 new
     * String phone, String token, String code, String  adress
     * state = 0 成功 state 1=失败
     *
     * @return
     */
    public static String getMsgCodeNew() {
        return CommonUrl + "api/customer/sendCode.do";
    }

    /**
     * 修改交易所地址
     * String phone, String token, String code, String  adress
     * state0成功  1系统异常 2token无效 3验证码失效
     *
     * @return
     */
    public static String getChangeJYSaddress() {
        return CommonUrl + "api/customer/updateWalletAddress.do";
    }

    /**
     * 修改用户信息token认证
     * 入参：token  url  name   alipay   bankCard  address
     * 返回json: state   0：成功  1：系统异常 2：token有误
     *
     * @return
     */
    public static String changeUserInfo() {
        return CommonUrl + "api/customer/updateInfo.do";
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
     * 我的收藏列表token认证
     * shopId
     *
     * @return
     */
    public static String getStarList() {
        return CommonUrl + "api/customer/searchCollect.do";
    }

    /**
     * 改变收藏状态token认证
     * 请求  state (1 收藏  2取消)  id   token
     * 返参
     *
     * @return
     */
    public static String changeStarState() {
        return CommonUrl + "api/customer/updateCollect.do";
    }


    /**
     * 上传文件token认证
     * file
     *
     * @return
     */
    public static String uploadImage() {
        return CommonUrl + "api/common/uploadImage.do";
    }

    /**
     * 浏览历史token认证
     * shopId
     *
     * @return
     */
    public static String getHistory() {
        return CommonUrl + "api/customer/searchHistory.do";
    }

    /**
     * 我的页面数据token认证
     * 特殊返参：isAdv//0 已过审  1 审核中  2 已下架  3 普通用户
     *
     * @return
     */
    public static String getMyData() {
        return CommonUrl + "api/customer/searchMy.do";
    }

    /**
     * 我的消费（商家/个人）token认证
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
     * 商家确认/取消记账返回积分token认证
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
     * 用户去评价token认证
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
     * 商家管理页面
     * 传参 token
     * <p>
     * 返参
     *
     * @return
     */
    public static String getStoreManage() {
        return CommonUrl + "api/customer/searchAdvUser.do";
    }
}
