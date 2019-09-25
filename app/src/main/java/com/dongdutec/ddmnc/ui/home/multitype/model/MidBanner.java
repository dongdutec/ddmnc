package com.dongdutec.ddmnc.ui.home.multitype.model;

import com.dongdutec.ddmnc.ui.home.multitype.beans.MidBannerBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 董斌
 * eml.dongbinjava@163.com
 * @创建日期 2019/9/17 16:22
 */
public class MidBanner {
    private List<MidBannerBeans> mMidBannerBeansList = new ArrayList<>();

    public MidBanner() {
    }

    public MidBanner(List<MidBannerBeans> midBannerBeansList) {
        mMidBannerBeansList = midBannerBeansList;
    }

    public List<MidBannerBeans> getMidBannerBeansList() {
        return mMidBannerBeansList;
    }

    public void setMidBannerBeansList(List<MidBannerBeans> midBannerBeansList) {
        mMidBannerBeansList = midBannerBeansList;
    }
}
