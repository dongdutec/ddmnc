package com.dongdutec.ddmnc.ui.my.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import rx.functions.Action1;

public class SettingActivity extends BaseActivity {
    private ImageView back;
    private TextView bar_title;
    private RelativeLayout rl_password;
    private TextView tv_version;
    private TextView tv_callphone;

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
        tv_version = findViewById(R.id.tv_version);
        tv_callphone = findViewById(R.id.tv_callphone);

        bar_title.setText("设置");
        //设置版本
        String versionName = null;
        try {
            versionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            tv_version.setText("V" + versionName + " ");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_callphone.setText("0532-1234567");
    }

    @Override
    protected void init() {

    }

    @Override
    protected void bindView() {
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

            }
        });
    }
}
