package com.dongdutec.ddmnc.ui.home.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.ui.home.multitype.HomeItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.model.HotStore;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;
import com.nex3z.flowlayout.FlowLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class SearchActivity extends BaseActivity {
    private SmartRefreshLayout main_refresh;
    private RecyclerView main_rlv;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private List<HotStore> mHotStoreList = new ArrayList<>();
    private ImageView bar_left_img;
    private TextView bar_title;
    private LinearLayout ll_state;
    private RelativeLayout rl_bar;
    private EditText et_search;
    private LinearLayout ll_lishi;
    private TextView tv_midsearch;
    private ImageView img_delete;
    private TextView tv_quxiao;
    private FlowLayout flow_search;

    private int total_all_page;
    private int mRows = 10;  // 设置默认一页加载10条数据
    private int current_page;
    private boolean isLoadMore = false;
    private boolean isLoadOver = false;
    private boolean isLoadMoreSingle = false;//上拉单次标志位
    private boolean isFirstLoad = true;

    private Set<String> historySet = new HashSet<String>();
    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        sf = getSharedPreferences("data", MODE_PRIVATE);


        //test存储
        SharedPreferences.Editor editor = sf.edit();
        Set<String> hashSet = new HashSet<String>();
        hashSet.add("东度科技");
        hashSet.add("蓝海缘");
        hashSet.add("威海高区");
        editor.putStringSet("historySet", hashSet);
        editor.apply();


        initView();
        init();
        bindView();
    }

    @Override
    protected void bindView() {
        //搜索文字监听
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence == null || charSequence.length() == 0) {
                    main_refresh.setVisibility(View.GONE);
                    ll_lishi.setVisibility(View.VISIBLE);
                    tv_midsearch.setVisibility(View.VISIBLE);
                } else {
                    main_refresh.setVisibility(View.VISIBLE);
                    ll_lishi.setVisibility(View.GONE);
                    tv_midsearch.setVisibility(View.GONE);
                    //test
                    init();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //删除历史搜索
        RxViewAction.clickNoDouble(img_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //删除操作
                SharedPreferences.Editor editor = sf.edit();
                editor.remove("historySet");
                editor.apply();
                initHistory();
            }
        });
        //取消
        RxViewAction.clickNoDouble(tv_quxiao).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
    }

    @Override
    protected void init() {

        //历史记录
        initHistory();


        //http

        Random ran = new Random();
        int radom = ran.nextInt(100);
        HotStore hotStore;
        mHotStoreList.clear();
        for (int i = 0; i < 32; i++) {
            hotStore = new HotStore();
            hotStore.setImageUrl("www.xxxxxxxxxxxxx");
            hotStore.setStoreName("东度科技青岛测试店" + radom + i);
            hotStore.setLocationStr("青岛市·黄岛区·长江路·国贸大厦");
            hotStore.setCount(radom + i);
            hotStore.setDistance(radom + i);
            if (i == 0) {
                hotStore.setFirst(true);
            }
            mHotStoreList.add(hotStore);
        }
        updataData();


    }

    private void initHistory() {
        //历史记录
        historySet = sf.getStringSet("historySet", new HashSet<String>());
        flow_search.removeAllViews();
        if (historySet.size() > 0) {
            for (final String history : historySet) {
                TextView textView = new TextView(this);//新建控件
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //设置控件的宽高        
                //layoutParams.setPadding(5, 5, 5, 5);//设置控件与上下左右的距离
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(30, 10, 30, 10);
                textView.setBackgroundResource(R.drawable.c_coner);//设置背景色 
                textView.setTextColor(getResources().getColor(R.color.c5));//控件字体颜色
                textView.setTextSize(12);//控件字体大小
                textView.setLayoutParams(layoutParams);//上面设置控件的高宽后就落实
                textView.setText(history);//控件内容

                RxViewAction.clickNoDouble(textView).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Toast.makeText(SearchActivity.this, history, Toast.LENGTH_SHORT).show();
                    }
                });

                flow_search.setChildSpacing(10);//子视图之间的水平间距
                flow_search.setRowSpacing(10);//行之间的垂直间距
                flow_search.addView(textView);

            }
        }
    }


    private void updataData() {
        items.clear();
        if (mHotStoreList == null || mHotStoreList.size() == 0) {
//            items.add(new ItemNullBean("暂无数据")); TODO
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
        main_rlv = findViewById(R.id.homebtn_rlv);
        main_refresh = findViewById(R.id.homebtn_refresh);
        bar_left_img = findViewById(R.id.bar_left_img);
        bar_title = findViewById(R.id.bar_title_text);
        ll_state = findViewById(R.id.ll_state);
        rl_bar = findViewById(R.id.rl_bar);
        et_search = findViewById(R.id.et_search);
        ll_lishi = findViewById(R.id.ll_lishi);
        tv_midsearch = findViewById(R.id.tv_midsearch);
        img_delete = findViewById(R.id.img_delete);
        tv_quxiao = findViewById(R.id.tv_quxiao);
        flow_search = findViewById(R.id.flow_search);


        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        main_rlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(HotStore.class, new HomeItemViewProvider(getApplicationContext()));
        main_rlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(main_rlv, multiTypeAdapter);

        main_refresh.setVisibility(View.GONE);//test
        main_refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                init();
                main_refresh.finishRefresh(true);
            }
        });

    }
}
