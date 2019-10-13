package com.dongdutec.ddmnc.ui.my.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.db.model.User;
import com.dongdutec.ddmnc.http.HtmlUrls;
import com.dongdutec.ddmnc.ui.login.activity.LoginActivity;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;
import com.dongdutec.ddmnc.web.WebsActivity;

import org.xutils.ex.DbException;

import rx.functions.Action1;

public class SettingActivity extends BaseActivity {
    private ImageView back;
    private TextView bar_title;
    private RelativeLayout rl_password;
    private RelativeLayout rl_jianjie;
    private RelativeLayout rl_suggestion;
    private TextView tv_version;
    private TextView tv_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
        init();
        bindView();
    }

    @Override
    protected void initView() {
        back = findViewById(R.id.bar_left_img);
        bar_title = findViewById(R.id.bar_title_text);
        rl_password = findViewById(R.id.rl_password);
        rl_jianjie = findViewById(R.id.rl_jianjie);
        tv_version = findViewById(R.id.tv_version);
        rl_suggestion = findViewById(R.id.rl_suggestion);
        tv_exit = findViewById(R.id.tv_exit);

        bar_title.setText("设置");
        //设置版本
        String versionName = null;
        try {
            versionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            tv_version.setText("V" + versionName + " ");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void init() {

    }

    @Override
    protected void bindView() {
        //意见反馈
        RxViewAction.clickNoDouble(rl_suggestion).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Toast.makeText(SettingActivity.this, "暂未开放!", Toast.LENGTH_SHORT).show();
            }
        });
        //简介
        RxViewAction.clickNoDouble(rl_jianjie).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(SettingActivity.this, WebsActivity.class);
                intent.putExtra("webUrl", HtmlUrls.getPlatforms());
                intent.putExtra("title", "平台简介");
                startActivity(intent);
            }
        });
        //返回
        RxViewAction.clickNoDouble(back).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        //修改手机号
        RxViewAction.clickNoDouble(rl_password).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(SettingActivity.this, WebsActivity.class);
                intent.putExtra("title", "修改手机号");
                intent.putExtra("webUrl", HtmlUrls.getChangePhone()+"?oldPhone=" + new DbConfig(SettingActivity.this).getPhone());
                startActivity(intent);
            }
        });
        //退出登录
        RxViewAction.clickNoDouble(tv_exit).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                DbConfig dbConfig = new DbConfig(SettingActivity.this);
                User user = dbConfig.getUser();
                if (user != null) {
                    try {
                        user.setIsLogin("0");
                        dbConfig.getDbManager().saveOrUpdate(user);
                        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                        showToast("退出成功!");
                    } catch (DbException e) {
                        e.printStackTrace();
                        showToast("退出失败!");
                    }
                }
            }
        });
    }
}
