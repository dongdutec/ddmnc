package com.dongdutec.ddmnc.cell.loading;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.dongdutec.ddmnc.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * 加载中Dialog
 *
 * @author hzb
 */
public class LoadingDialog extends AlertDialog {

    private AVLoadingIndicatorView avi;

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.setContentView(R.layout.dialog_loading);
        avi = this.findViewById(R.id.avi);
    }

    @Override
    public void show() {
        super.show();
        avi.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        avi.hide();
    }
}