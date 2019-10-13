package com.dongdutec.ddmnc.ui.my.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.ui.my.multitype.DailiItemViewProvider;
import com.dongdutec.ddmnc.ui.my.multitype.model.Daili;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class DailiActivity extends BaseActivity {

    private SmartRefreshLayout main_refresh;
    private RecyclerView main_rlv;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private List<Daili> mHotStoreList = new ArrayList<>();

    private int total_all_page;
    private int mRows = 10;  // 设置默认一页加载10条数据
    private int current_page;
    private boolean isLoadMore = false;
    private boolean isLoadOver = false;
    private boolean isLoadMoreSingle = false;//上拉单次标志位
    private boolean isFirstLoad = true;

    private ImageView back;
    private TextView bar_title;
    private TextView tv_qu;
    private TextView tv_shouyi;
    private TextView tv_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daili);

        initView();
        init();
        bindView();
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
        //查看更多
        RxViewAction.clickNoDouble(tv_more).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                tv_more.setVisibility(View.GONE);
                main_refresh.setEnableLoadMore(true);
            }
        });
    }

    @Override
    protected void init() {
        //http

        Random ran = new Random();
        int radom = ran.nextInt(100);
        Daili hotStore;
        mHotStoreList.clear();
        for (int i = 0; i < 100; i++) {
            hotStore = new Daili();
            hotStore.setHeadImgUrl("www.xxxxxxxxxxxxx");
            hotStore.setUserId(radom + i);
            mHotStoreList.add(hotStore);
        }
        updataData();


    }


    private void updataData() {
        items.clear();
        if (mHotStoreList == null || mHotStoreList.size() == 0) {
//            items.add(new ItemNullBean("暂无数据")); TODO
            main_refresh.setEnableLoadMore(false);
        } else {
            for (int i = 0; i < mHotStoreList.size(); i++) {
                items.add(mHotStoreList.get(i));
            }
        }
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initView() {

        back = findViewById(R.id.bar_left_img);
        bar_title = findViewById(R.id.bar_title_text);
        tv_qu = findViewById(R.id.tv_qu);
        tv_shouyi = findViewById(R.id.tv_shouyi);
        tv_more = findViewById(R.id.tv_more);

        bar_title.setText("代理数据");

        main_rlv = findViewById(R.id.homebtn_rlv);
        main_refresh = findViewById(R.id.homebtn_refresh);


        LinearLayoutManager manager = new GridLayoutManager(getApplicationContext(), 6, GridLayoutManager.VERTICAL, false);
        main_rlv.setLayoutManager(manager);
        main_refresh.setEnableLoadMore(false);//查看更多后解锁加载更多
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(Daili.class, new DailiItemViewProvider(getApplicationContext()));
        main_rlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(main_rlv, multiTypeAdapter);

        main_refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                main_refresh.finishLoadMore(1000);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                init();
                main_refresh.finishRefresh(1000);
            }
        });

    }
}
