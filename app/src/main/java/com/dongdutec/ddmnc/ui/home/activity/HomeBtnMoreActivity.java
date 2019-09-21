package com.dongdutec.ddmnc.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import rx.functions.Action1;

public class HomeBtnMoreActivity extends BaseActivity {
    private TextView title;
    private ImageView back;
    private ImageView rightImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_btn_more);

        initView();
        init();
        bindView();
    }

    @Override
    protected void initView() {
        title = findViewById(R.id.bar_title_text);
        back = findViewById(R.id.bar_left_img);
        rightImage = findViewById(R.id.bar_right_img);

        title.setText("更多分类");
        rightImage.setImageResource(R.mipmap.sure);
        RxViewAction.clickNoDouble(back).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });


    }

    @Override
    protected void init() {

    }

    @Override
    protected void bindView() {

    }

    public void moreclick(View view) {
        Intent intent = new Intent(getApplicationContext(), HomeBtnListActivity.class);
        intent.putExtra("type", "娱乐");
        startActivity(intent);
    }
}
