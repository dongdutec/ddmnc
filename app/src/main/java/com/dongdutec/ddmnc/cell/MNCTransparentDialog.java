package com.dongdutec.ddmnc.cell;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.dongdutec.ddmnc.R;

/**
 * @作者 董斌
 * eml.dongbinjava@163.com
 * @创建日期 2019/8/12 10:10
 */
public class MNCTransparentDialog extends Dialog {
    public MNCTransparentDialog(Context context) {
        super(context, R.style.myTransparentDialog);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
