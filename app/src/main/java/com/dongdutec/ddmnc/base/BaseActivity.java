package com.dongdutec.ddmnc.base;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    private BaseApplication application;
    private BaseActivity oContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 23) {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
        //去除ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //app退出控制
        if (application == null) {
            // 得到Application对象
            application = (BaseApplication) getApplication();
        }
        oContext = this;// 把当前的上下文对象赋值给BaseActivity
        addActivity();// 调用添加方法

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

    /**
     * 透明状态栏,透明导航栏
     * 使用了SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION,表示会让应用的主体内容占用
     * 系统导航栏的空间
     * 然后又调用了setNavigationBarColor()方法将导航栏设置成透明色
     */
    public void setStatus(int colorRes) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        } else {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏字体深色
            window.setStatusBarColor(getResources().getColor(colorRes));
        }
    }


    // 添加Activity方法
    public void addActivity() {
        application.addActivity_(oContext);// 调用BaseApplication的添加Activity方法
    }

    //销毁当个Activity方法
    public void removeActivity() {
        application.removeActivity_(oContext);// 调用BaseApplication的销毁单个Activity方法
    }

    //销毁所有Activity方法
    public void removeALLActivity() {
        application.removeALLActivity_();// 调用BaseApplication的销毁所有Activity方法
    }

    /* 把Toast定义成一个方法*/
    public void showToast(String text) {
        Toast.makeText(oContext, text, Toast.LENGTH_SHORT).show();
    }

    /* 把Log定义成一个方法*/
    public void showLog(String text) {
        Toast.makeText(oContext, text, Toast.LENGTH_SHORT).show();
        Log.e("MNC", "show_Log: " + text);
    }

}
