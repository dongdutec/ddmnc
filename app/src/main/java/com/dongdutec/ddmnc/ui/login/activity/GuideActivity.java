package com.dongdutec.ddmnc.ui.login.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class GuideActivity extends BaseActivity {
    private ViewPager viewPager;//需要ViewPaeger
    private PagerAdapter mAdapter;//需要PagerAdapter适配器
    private List<View> mViews = new ArrayList<>();//准备数据源
    private TextView bt_home;//在ViewPager的最后一个页面设置一个按钮，用于点击跳转到MainActivity
    private String TAG = GuideActivity.class.getSimpleName();
    private TextView tv_jupm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();//初始化view
    }

    protected void initView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tv_jupm = (TextView) findViewById(R.id.tv_jupm);

        RxViewAction.clickNoDouble(tv_jupm).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                clickCommitAndJump();
            }
        });

        LayoutInflater inflater = LayoutInflater.from(this);//将每个xml文件转化为View
        View guideOne = inflater.inflate(R.layout.guide_a_layout, null);//每个xml中就放置一个imageView
        View guideTwo = inflater.inflate(R.layout.guide_b_layout, null);
        View guideThree = inflater.inflate(R.layout.guide_c_layout, null);

        mViews.add(guideOne);//将view加入到list中
        mViews.add(guideTwo);
        mViews.add(guideThree);

        mAdapter = new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);//初始化适配器，将view加到container中
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View view = mViews.get(position);
                container.removeView(view);//将view从container中移除
            }

            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;//判断当前的view是我们需要的对象
            }
        };

        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    tv_jupm.setVisibility(View.GONE);
                    openAnimator();
                } else {
                    tv_jupm.setVisibility(View.VISIBLE);
                    closeAnimator();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bt_home = (TextView) guideThree.findViewById(R.id.to_Main);
        bt_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCommitAndJump();
            }
        });
    }

    /*
     * 进入主页出现动画
     * */
    private void openAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(bt_home, View.ALPHA, 1);
        animator.start();
    }

    /*
     * 进入主页隐藏动画
     * */
    private void closeAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(bt_home, View.ALPHA, 0);
        animator.start();
    }


    private void clickCommitAndJump() {
        //修改判断是否是第一次进入的标志位 TODO
        SharedPreferences sf = getSharedPreferences("data", MODE_PRIVATE);//判断是否是第一次进入
        SharedPreferences.Editor editor = sf.edit();
        editor.putBoolean("isFirstIn", false);
        editor.apply();
        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
