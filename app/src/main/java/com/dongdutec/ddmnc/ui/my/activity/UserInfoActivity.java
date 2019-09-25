package com.dongdutec.ddmnc.ui.my.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import rx.functions.Action1;

public class UserInfoActivity extends BaseActivity {
    private ImageView bar_left_img;
    private TextView bar_title_text;
    private ImageView userimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
        init();
        bindView();
    }

    @Override
    protected void initView() {
        bar_left_img = findViewById(R.id.bar_left_img);
        bar_title_text = findViewById(R.id.bar_title_text);
        userimg = findViewById(R.id.userimg);

        bar_title_text.setText("个人信息");
    }

    @Override
    protected void init() {

    }

    @Override
    protected void bindView() {
        //返回
        RxViewAction.clickNoDouble(bar_left_img).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        //修改头像
        RxViewAction.clickNoDouble(userimg).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
    }
}
