package com.dongdutec.ddmnc.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class HomeBtnMoreActivity extends BaseActivity {
    private TextView title;
    private ImageView back;
    private ImageView rightImage;

    private FlowLayout mFlowLayout;
    private List<String> btnStrList;

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
        mFlowLayout = findViewById(R.id.mFlowLayout);

        title.setText("更多分类");
        rightImage.setImageResource(R.mipmap.sure);
        RxViewAction.clickNoDouble(back).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        btnStrList = new ArrayList<>();
        btnStrList.add("娱乐");
        btnStrList.add("东度科技");
        btnStrList.add("东度");
        btnStrList.add("科技");
        btnStrList.add("新闻顶顶顶");
        btnStrList.add("要点的");
        btnStrList.add("今日头条");
        btnStrList.add("今日头条");
        btnStrList.add("玩");
        btnStrList.add("娱乐");
        btnStrList.add("东度科技");
        btnStrList.add("东度");
        btnStrList.add("科技");
        btnStrList.add("新闻顶顶顶");
        btnStrList.add("要点的");
        btnStrList.add("今日头条");
        for (int i = 0; i < btnStrList.size(); i++) {
            TextView textView = new TextView(this);//新建控件
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //设置控件的宽高        
            //layoutParams.setPadding(5, 5, 5, 5);//设置控件与上下左右的距离
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(50, 25, 50, 25);
            textView.setBackgroundResource(R.drawable.stroke_light);//设置背景色 
            textView.setTextColor(getResources().getColor(R.color.c5));//控件字体颜色
            textView.setLayoutParams(layoutParams);//上面设置控件的高宽后就落实
            textView.setText(btnStrList.get(i));//控件内容

            final int finalI = i;
            RxViewAction.clickNoDouble(textView).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    Toast.makeText(HomeBtnMoreActivity.this, btnStrList.get(finalI), Toast.LENGTH_SHORT).show();
                }
            });

            mFlowLayout.setChildSpacing(30);//子视图之间的水平间距
            mFlowLayout.setRowSpacing(30);//行之间的垂直间距
            mFlowLayout.addView(textView);
        }


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
