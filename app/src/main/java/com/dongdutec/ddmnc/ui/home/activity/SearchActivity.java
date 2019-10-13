package com.dongdutec.ddmnc.ui.home.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.http.RequestUrls;
import com.dongdutec.ddmnc.ui.home.multitype.HomeItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.NullListItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.model.HotStore;
import com.dongdutec.ddmnc.ui.home.multitype.model.NullList;
import com.dongdutec.ddmnc.utils.location.LocationUtils;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;
import com.nex3z.flowlayout.FlowLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private ImageView iv_midsearch;
    private ImageView img_delete;
    private TextView tv_quxiao;
    private FlowLayout flow_search;

    private long exitTime = 0;

    private int mRows = 10;  // 设置默认一页加载10条数据
    private int current_page = 1;
    private boolean isFirstLoad = true;

    private Set<String> historySet;
    private SharedPreferences sf;
    private String TAG = SearchActivity.class.getSimpleName();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //请求搜索数据
                    mHotStoreList.clear();
                    initSearch();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        sf = getSharedPreferences("data", MODE_PRIVATE);


        initView();
        init();
        bindView();
    }

    @Override
    protected void bindView() {
        //上拉加载下拉刷新
        main_refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isFirstLoad = false;
                current_page++;
                initSearch();
                main_refresh.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mHotStoreList.clear();
                isFirstLoad = true;
                current_page = 1;
                initSearch();
                main_refresh.finishRefresh();
                main_refresh.setEnableLoadMore(true);
            }
        });
        //搜索文字监听
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence == null || charSequence.length() == 0) {
                    initHistory();
                    main_refresh.setVisibility(View.GONE);
                    ll_lishi.setVisibility(View.VISIBLE);
                    tv_midsearch.setVisibility(View.VISIBLE);
                    iv_midsearch.setVisibility(View.VISIBLE);
                } else {
                    main_refresh.setVisibility(View.VISIBLE);
                    ll_lishi.setVisibility(View.GONE);
                    tv_midsearch.setVisibility(View.GONE);
                    iv_midsearch.setVisibility(View.GONE);


                    if ((et_search.getText() != null) && (et_search.getText().length() != 0)) {

                        if (((System.currentTimeMillis() - exitTime) > 1000)) {
                            //停留时间大于1s后无需移除
                            //记录最后一次按键时间
                            exitTime = System.currentTimeMillis();
                        } else {
                            //停留时间小于1s后关闭之前搜索
                            mHandler.removeMessages(0);
                            //记录最后一次按键时间
                            exitTime = System.currentTimeMillis();
                        }

                        mHandler.sendEmptyMessageDelayed(0, 1000);
                    }

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

    }

    private void initSearch() {
        judgeToken();
    }

    @Override
    protected void onJudgeResult() {
        //获取搜索数据
        RequestParams params = new RequestParams(RequestUrls.homeSearch());
        params.setConnectTimeout(5000);
        showLoadings();
        params.addBodyParameter("classifyId", "");
        params.addBodyParameter("city", "");
        params.addBodyParameter("isNew", "");
        params.addBodyParameter("sale", "");
        params.addBodyParameter("shopName", et_search.getText().toString());
        params.addBodyParameter("page", current_page + "");
        params.addBodyParameter("rows", mRows + "");
        Log.e(TAG, "init: params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: search result = " + result);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    if (jsonArray.length() == 0) {//refresh
                        main_refresh.setEnableLoadMore(false);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        HotStore hotStore = new HotStore();
                        hotStore.setImageUrl(jsonObject.getString("advertImage"));
                        hotStore.setStoreName(jsonObject.getString("advertName"));
                        hotStore.setLocationStr(jsonObject.getString("advertAddress"));
                        hotStore.setCount(Integer.parseInt(jsonObject.getString("count")));
                        hotStore.setStoreId(jsonObject.getString("id"));
                        String advertLatitude = jsonObject.getString("advertLatitude");
                        String advertLongitude = jsonObject.getString("advertLongitude");
                        double longitude_store = Double.parseDouble(advertLongitude);
                        double latitude_store = Double.parseDouble(advertLatitude);
                        hotStore.setLantitude(latitude_store);
                        hotStore.setLongitude(longitude_store);
                        double distance = LocationUtils.getDistance(new DbConfig(SearchActivity.this).getLongitude(), new DbConfig(SearchActivity.this).getLatitude(), longitude_store, latitude_store);
                        hotStore.setDistance(distance);

                        mHotStoreList.add(hotStore);
                    }
                    updataData();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: ex.toString()" + ex.toString());
                updataData();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideLoadings();
            }
        });
    }

    private void initHistory() {
        //历史记录
        historySet = new HashSet<String>();
        historySet = sf.getStringSet("historySet", new HashSet<String>());
        Log.e(TAG, "initHistory: historySet.toString() = " + historySet.toString());
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
                        et_search.setText(history);
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
            items.add(new NullList());
            main_refresh.setEnableLoadMore(false);
        } else {
            for (int i = 0; i < mHotStoreList.size(); i++) {
                items.add(mHotStoreList.get(i));
            }
        }
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();
        hideLoadings();
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
        iv_midsearch = findViewById(R.id.iv_midsearch);
        img_delete = findViewById(R.id.img_delete);
        tv_quxiao = findViewById(R.id.tv_quxiao);
        flow_search = findViewById(R.id.flow_search);


        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        main_rlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(HotStore.class, new HomeItemViewProvider(SearchActivity.this, true));
        multiTypeAdapter.register(NullList.class, new NullListItemViewProvider(SearchActivity.this));
        main_rlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(main_rlv, multiTypeAdapter);

        main_refresh.setVisibility(View.GONE);

    }
}
