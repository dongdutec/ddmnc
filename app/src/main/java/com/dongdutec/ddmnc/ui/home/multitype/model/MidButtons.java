package com.dongdutec.ddmnc.ui.home.multitype.model;

import com.dongdutec.ddmnc.ui.home.multitype.beans.BtnsBean;

import java.util.List;

/**
 * @作者 董斌
 * eml.dongbinjava@163.com
 * @创建日期 2019/9/17 16:22
 */
public class MidButtons {
    private List<BtnsBean> btnsBeanList;

    public MidButtons() {
    }

    public MidButtons(List<BtnsBean> btnsBeanList) {
        this.btnsBeanList = btnsBeanList;
    }

    public List<BtnsBean> getBtnsBeanList() {
        return btnsBeanList;
    }

    public void setBtnsBeanList(List<BtnsBean> btnsBeanList) {
        this.btnsBeanList = btnsBeanList;
    }
}
