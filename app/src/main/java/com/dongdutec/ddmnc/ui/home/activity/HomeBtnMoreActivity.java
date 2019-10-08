package com.dongdutec.ddmnc.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;
import com.nex3z.flowlayout.FlowLayout;

import java.util.List;

import rx.functions.Action1;

public class HomeBtnMoreActivity extends BaseActivity {
    private TextView title;
    private ImageView back;
    private ImageView rightImage;

    private FlowLayout mFlowLayout;
    private List<String> classifyIdList;
    private List<String> classifyNameList;
    private String TAG = HomeBtnMoreActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_btn_more);

        Bundle extras = getIntent().getExtras();
        classifyIdList = extras.getStringArrayList("classifyIdList");
        classifyNameList = extras.getStringArrayList("classifyNameList");

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


    }

    @Override
    protected void init() {
        //获取加载更多


        for (int i = 0; i < classifyNameList.size(); i++) {
            TextView textView = new TextView(HomeBtnMoreActivity.this);//新建控件
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //设置控件的宽高        
            //layoutParams.setPadding(5, 5, 5, 5);//设置控件与上下左右的距离
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(50, 25, 50, 25);
            textView.setBackgroundResource(R.drawable.stroke_light);//设置背景色 
            textView.setTextColor(getResources().getColor(R.color.c5));//控件字体颜色
            textView.setLayoutParams(layoutParams);//上面设置控件的高宽后就落实
            textView.setText(classifyNameList.get(i));//控件内容

            final int finalI1 = i;
            RxViewAction.clickNoDouble(textView).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    moreclick(finalI1);
                }
            });

            mFlowLayout.setChildSpacing(30);//子视图之间的水平间距
            mFlowLayout.setRowSpacing(30);//行之间的垂直间距
            mFlowLayout.addView(textView);
        }

    }

    @Override
    protected void bindView() {

    }

    public void moreclick(int i) {
        Intent intent = new Intent(HomeBtnMoreActivity.this, HomeBtnListActivity.class);
        intent.putExtra("type", classifyNameList.get(i));
        intent.putExtra("classifyId", classifyIdList.get(i));
        HomeBtnMoreActivity.this.startActivity(intent);
    }
}
