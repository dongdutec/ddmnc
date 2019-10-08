package com.dongdutec.ddmnc.ui.my.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseFragment;
import com.dongdutec.ddmnc.cell.CustomScrollView;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.eventbus.UserInfoEvent;
import com.dongdutec.ddmnc.http.RequestUrls;
import com.dongdutec.ddmnc.ui.my.activity.DailiActivity;
import com.dongdutec.ddmnc.ui.my.activity.HistoryActivity;
import com.dongdutec.ddmnc.ui.my.activity.MyStarActivity;
import com.dongdutec.ddmnc.ui.my.activity.MyXiaofeiActivity;
import com.dongdutec.ddmnc.ui.my.activity.SettingActivity;
import com.dongdutec.ddmnc.ui.my.activity.UserInfoActivity;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;
import com.dongdutec.ddmnc.web.WebsActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class MyFragment extends BaseFragment {
    private TextView my_title;
    private ImageView my_back;
    private ImageView userimg;
    private TextView username;
    private TextView tv_mnc;
    private TextView tv_mp;
    private TextView tv_fans;
    private ImageView edit;
    private ImageView img_message;
    private LinearLayout ll_shezhi;
    private LinearLayout ll_dailishuju;
    private LinearLayout ll_wodeshoucang;
    private LinearLayout ll_liulanlishi;
    private LinearLayout ll_user_daijizhang;
    private LinearLayout ll_user_yijizhang;
    private LinearLayout ll_user_all;
    private LinearLayout ll_store_daijizhang;
    private LinearLayout ll_store_shangjiama;
    private LinearLayout ll_zhuanshuma;
    private LinearLayout ll_store_all;
    private LinearLayout ll_wssj_all;
    private TextView store;
    private LinearLayout ll_qianbao;
    private RelativeLayout rl_sjrz;
    private RefreshLayout myfg_refresh;
    private CustomScrollView scorll_my;
    private String TAG = MyFragment.class.getSimpleName();
    private String image;
    private String userName;
    private double mncCoin;
    private double mp;
    private int vermicelli;
    private boolean isStorFlag = false;
    private String shopId = "";

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
        tv_mnc = getView().findViewById(R.id.tv_mnc);
        tv_mp = getView().findViewById(R.id.tv_mp);
        tv_fans = getView().findViewById(R.id.tv_fans);
        edit = getView().findViewById(R.id.edit);
        img_message = getView().findViewById(R.id.img_message);
        ll_shezhi = getView().findViewById(R.id.ll_shezhi);
        ll_dailishuju = getView().findViewById(R.id.ll_dailishuju);
        ll_wodeshoucang = getView().findViewById(R.id.ll_wodeshoucang);
        ll_liulanlishi = getView().findViewById(R.id.ll_liulanlishi);
        ll_user_daijizhang = getView().findViewById(R.id.ll_user_daijizhang);
        ll_user_yijizhang = getView().findViewById(R.id.ll_user_yijizhang);
        ll_user_all = getView().findViewById(R.id.ll_user_all);
        ll_store_daijizhang = getView().findViewById(R.id.ll_store_daijizhang);
        ll_store_shangjiama = getView().findViewById(R.id.ll_store_shangjiama);
        ll_zhuanshuma = getView().findViewById(R.id.ll_zhuanshuma);
        ll_store_all = getView().findViewById(R.id.ll_store_all);
        ll_wssj_all = getView().findViewById(R.id.ll_wssj_all);
        store = getView().findViewById(R.id.store);
        ll_qianbao = getView().findViewById(R.id.ll_qianbao);
        rl_sjrz = getView().findViewById(R.id.rl_sjrz);
        myfg_refresh = getView().findViewById(R.id.myfg_refresh);
        scorll_my = getView().findViewById(R.id.scorll_my);

        my_title.setText("我的");
        my_back.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void init() {
        RequestParams params = new RequestParams(RequestUrls.getMyData());
        params.setConnectTimeout(5000);
        params.addBodyParameter("token", new DbConfig(getContext()).getToken());
        Log.e(TAG, "init:  params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: result = " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    image = jsonObject.getString("image");
                    userName = jsonObject.getString("userName");
                    mncCoin = jsonObject.getDouble("mncCoin");
                    mp = jsonObject.getDouble("mp");
                    vermicelli = jsonObject.getInt("vermicelli");
                    try {
                        shopId = jsonObject.getString("shopId");
                        isStorFlag = true;
                    } catch (Exception e) {
                        isStorFlag = false;
                    }

                    //设置我的信息
                    CircleCrop transformation = new CircleCrop();
                    RequestOptions requestOptions = RequestOptions.bitmapTransform(transformation);
                    Glide.with(getContext()).load(image)
                            .placeholder(R.mipmap.touxiang)
                            .apply(requestOptions)
                            .into(userimg);
                    username.setText(userName);
                    tv_mnc.setText(mncCoin + "");
                    tv_mp.setText(mp + "");
                    tv_fans.setText(vermicelli + "");
                    if (isStorFlag) {
                        ll_wssj_all.setVisibility(View.VISIBLE);
                    } else {
                        ll_wssj_all.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    protected void bindView() {
        //我的专属码
        RxViewAction.clickNoDouble(ll_zhuanshuma).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), WebsActivity.class);
                intent.putExtra("webUrl", "http://47.75.47.121:8080/mnc/exclusive.html?token=" + new DbConfig(getContext()).getToken());
                startActivity(intent);
            }
        });
        //商家入驻
        RxViewAction.clickNoDouble(rl_sjrz).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), WebsActivity.class);
                intent.putExtra("webUrl", "http://47.75.47.121:8080/mnc/settled.html?token=" + new DbConfig(getContext()).getToken());
                startActivity(intent);
            }
        });
        RxViewAction.clickNoDouble(store).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), WebsActivity.class);
                intent.putExtra("webUrl", "http://47.75.47.121:8080/mnc/settled.html?token=" + new DbConfig(getContext()).getToken());
                startActivity(intent);
            }
        });
        //我的钱包
        RxViewAction.clickNoDouble(ll_qianbao).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), WebsActivity.class);
                intent.putExtra("webUrl", "http://47.75.47.121:8080/mnc/purse.html");
                startActivity(intent);
            }
        });
        //消息
        RxViewAction.clickNoDouble(img_message).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), WebsActivity.class);
                intent.putExtra("webUrl", "http://47.75.47.121:8080/mnc/message.html?token=" + new DbConfig(getContext()).getToken());
                startActivity(intent);
            }
        });
        //商家码
        RxViewAction.clickNoDouble(ll_store_shangjiama).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), WebsActivity.class);
                intent.putExtra("webUrl", "http://47.75.47.121:8080/mnc/businessCode.html?token=" + new DbConfig(getContext()).getToken() + "&shopId=" + shopId + "&shopName=" + userName);
                startActivity(intent);
            }
        });
        //ScrollView
        scorll_my.setOnScrollViewListener(new CustomScrollView.OnScrollViewListener() {
            @Override
            public void onTop() {
                myfg_refresh.autoRefresh();
            }

            @Override
            public void onBottom() {

            }
        });
        //刷新
        myfg_refresh.setEnableLoadMore(false);
        myfg_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                init();
                myfg_refresh.finishRefresh();
            }
        });
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
        //我的收藏
        RxViewAction.clickNoDouble(ll_wodeshoucang).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), MyStarActivity.class);
                intent.putExtra("type", "我的收藏");
                startActivity(intent);
            }
        });
        //浏览历史
        RxViewAction.clickNoDouble(ll_liulanlishi).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), HistoryActivity.class);
                intent.putExtra("type", "浏览历史");
                startActivity(intent);
            }
        });
        //我的消费-待记账
        RxViewAction.clickNoDouble(ll_user_daijizhang).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), MyXiaofeiActivity.class);
                intent.putExtra("page", 1);
                intent.putExtra("isStore", 1);//1用户 2商家
                startActivity(intent);
            }
        });
        //我的消费-已记账
        RxViewAction.clickNoDouble(ll_user_yijizhang).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), MyXiaofeiActivity.class);
                intent.putExtra("page", 2);
                intent.putExtra("isStore", 1);//1用户 2商家
                startActivity(intent);
            }
        });
        //我的消费-查看全部
        RxViewAction.clickNoDouble(ll_user_all).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), MyXiaofeiActivity.class);
                intent.putExtra("page", 0);
                intent.putExtra("isStore", 1);//1用户 2商家
                startActivity(intent);
            }
        });
        //我是商家-待记账
        RxViewAction.clickNoDouble(ll_store_daijizhang).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), MyXiaofeiActivity.class);
                intent.putExtra("page", 1);
                intent.putExtra("isStore", 2);//1用户 2商家
                startActivity(intent);
            }
        });
        //我是商家-查看全部
        RxViewAction.clickNoDouble(ll_store_all).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), MyXiaofeiActivity.class);
                intent.putExtra("page", 0);
                intent.putExtra("isStore", 2);//1用户 2商家
                startActivity(intent);
            }
        });
    }

    private void userInfo() {
        Intent intent = new Intent(getContext(), UserInfoActivity.class);
        intent.putExtra("headUrl", image);
        intent.putExtra("userName", userName);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void saveEvent(UserInfoEvent event) {
        init();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}