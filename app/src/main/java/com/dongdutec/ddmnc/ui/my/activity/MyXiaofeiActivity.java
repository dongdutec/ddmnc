package com.dongdutec.ddmnc.ui.my.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.ui.my.adapter.MyPagerAdapter;
import com.dongdutec.ddmnc.ui.my.fragment.MyXiaofeiFragment;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class MyXiaofeiActivity extends BaseActivity {

    private ImageView back;
    private TextView bar_title;
    private ImageView img_guanggao;
    private TabLayout tab_;
    private ViewPager pager_;
    private List<Fragment> fragments;
    private List<String> title_list;
    private MyPagerAdapter pagerAdapter;
    private int page;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_xiaofei);

        page = getIntent().getIntExtra("page", 0);

        initView();
        init();
        bindView();
    }

    @Override
    protected void initView() {
        back = findViewById(R.id.bar_left_img);
        bar_title = findViewById(R.id.bar_title_text);
        tab_ = findViewById(R.id.tab_);
        pager_ = findViewById(R.id.pager_);

        fragments = new ArrayList<>();
        fragments = getFragments();
        title_list = new ArrayList<>();
        title_list = getTitles();
        for (int i = 0; i < title_list.size(); i++) {
            TabLayout.Tab tab = tab_.newTab();
            tab.setText(title_list.get(i));
            tab_.addTab(tab);
        }
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), title_list, fragments);
        pager_.setAdapter(pagerAdapter);
        tab_.setupWithViewPager(pager_);

        pager_.setCurrentItem(page);


        bar_title.setText("我的消费");

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
    }

    private List<Fragment> getFragments() {

        ArrayList<Fragment> list_fg = new ArrayList<>();
        MyXiaofeiFragment fragment_all = new MyXiaofeiFragment();
        list_fg.add(fragment_all);

        MyXiaofeiFragment fragment_daijizhang = new MyXiaofeiFragment();
        list_fg.add(fragment_daijizhang);

        MyXiaofeiFragment fragment_yijizhang = new MyXiaofeiFragment();
        list_fg.add(fragment_yijizhang);

        return list_fg;
    }

    private List<String> getTitles() {
        List<String> stringList = new ArrayList<>();
        stringList.add("全部");
        stringList.add("待记账");
        stringList.add("已记账");
        return stringList;
    }
}
