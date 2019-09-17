package com.dongdutec.ddmnc.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dongdutec.ddmnc.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 23) {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
        //去除ActionBar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_base);

    }

    protected void init() {

    }

    protected void bindData() {

    }

    protected void initData() {

    }

    protected void bindView() {

    }

    protected void initView() {

    }
}
