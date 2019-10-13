package com.dongdutec.ddmnc.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;

public class ScanResultActiviity extends BaseActivity {

    private String sacnStr;
    private TextView sacntxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result_activiity);

        Intent intent = getIntent();
        sacnStr = intent.getStringExtra("sacnStr");

        sacntxt = findViewById(R.id.sacntxt);
        sacntxt.setText("禁止访问外部链接：" + sacnStr);

    }
}
