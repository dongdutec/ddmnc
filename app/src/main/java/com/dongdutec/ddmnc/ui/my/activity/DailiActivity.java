package com.dongdutec.ddmnc.ui.my.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import rx.functions.Action1;

public class DailiActivity extends BaseActivity {
    private ImageView back;
    private TextView bar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daili);

        initView();
        init();
        bindView();
    }

    @Override
    protected void initView() {
        back = findViewById(R.id.bar_left_img);
        bar_title = findViewById(R.id.bar_title_text);

        bar_title.setText("代理数据");
    }

    @Override
    protected void init() {

    }

    @Override
    protected void bindView() {
        RxViewAction.clickNoDouble(back).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
    }
}
