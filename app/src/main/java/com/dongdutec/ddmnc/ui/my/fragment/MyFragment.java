package com.dongdutec.ddmnc.ui.my.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseFragment;
import com.dongdutec.ddmnc.ui.my.activity.DailiActivity;
import com.dongdutec.ddmnc.ui.my.activity.SettingActivity;
import com.dongdutec.ddmnc.ui.my.activity.UserInfoActivity;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import rx.functions.Action1;

public class MyFragment extends BaseFragment {
    private TextView my_title;
    private ImageView my_back;
    private ImageView userimg;
    private TextView username;
    private ImageView edit;
    private LinearLayout ll_shezhi;
    private LinearLayout ll_dailishuju;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        init();
        bindView();
    }

    @Override
    protected void initView() {
        my_title = getView().findViewById(R.id.bar_title_text);
        my_back = getView().findViewById(R.id.bar_left_img);
        userimg = getView().findViewById(R.id.userimg);
        username = getView().findViewById(R.id.username);
        edit = getView().findViewById(R.id.edit);
        ll_shezhi = getView().findViewById(R.id.ll_shezhi);
        ll_dailishuju = getView().findViewById(R.id.ll_dailishuju);

        my_title.setText("我的");
        my_back.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void bindView() {
        //个人信息
        RxViewAction.clickNoDouble(userimg).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                userInfo();
            }
        });
        RxViewAction.clickNoDouble(username).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                userInfo();
            }
        });
        RxViewAction.clickNoDouble(edit).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                userInfo();
            }
        });
        //设置
        RxViewAction.clickNoDouble(ll_shezhi).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
        //代理数据
        RxViewAction.clickNoDouble(ll_dailishuju).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), DailiActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userInfo() {
        Intent intent = new Intent(getContext(), UserInfoActivity.class);
        startActivity(intent);
    }
}