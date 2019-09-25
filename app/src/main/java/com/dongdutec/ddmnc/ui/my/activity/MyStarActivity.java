package com.dongdutec.ddmnc.ui.my.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.http.RequestUrls;
import com.dongdutec.ddmnc.ui.home.multitype.HomeItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.model.HotStore;
import com.dongdutec.ddmnc.utils.location.LocationUtils;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;
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
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class MyStarActivity extends BaseActivity {

    private SmartRefreshLayout main_refresh;
    private RecyclerView main_rlv;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private List<HotStore> mHotStoreList = new ArrayList<>();
    private ImageView bar_left_img;
    private TextView bar_title;
    private LinearLayout ll_state;
    private RelativeLayout rl_bar;

    private int total_all_page;
    private int mRows = 10;  // 设置默认一页加载10条数据
    private int current_page;
    private boolean isLoadMore = false;
    private boolean isLoadOver = false;
    private boolean isLoadMoreSingle = false;//上拉单次标志位
    private boolean isFirstLoad = true;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commen_list);

        initView();
        init();
        bindView();
    }

    @Override
    protected void bindView() {
        //返回
        RxViewAction.clickNoDouble(bar_left_img).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
    }

    @Override
    protected void init() {
        //获取收藏列表
        final RequestParams params = new RequestParams(RequestUrls.getStarList());
        params.setConnectTimeout(5000);
        params.addBodyParameter("token", new DbConfig(MyStarActivity.this).getToken());
        params.addBodyParameter("phone", "");
        Log.e("boomer", "onSuccess: params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e("boomer", "onSuccess: result = " + result);
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        HotStore hotStore = new HotStore();
                        hotStore.setImageUrl(object.getString("image"));
                        hotStore.setStoreName(object.getString("shopName"));
                        hotStore.setLocationStr(object.getString("address"));
                        hotStore.setCount(Integer.parseInt(object.getString("count")));
                        String advertLatitude = object.getString("latitude");
                        String advertLongitude = object.getString("longitude");
                        double longitude_store = Double.parseDouble(advertLongitude);
                        double latitude_store = Double.parseDouble(advertLatitude);
                        hotStore.setLantitude(latitude_store);
                        hotStore.setLongitude(longitude_store);
                        DbConfig dbConfig = new DbConfig(MyStarActivity.this);
                        double distance = LocationUtils.getDistance(dbConfig.getLongitude(), dbConfig.getLatitude(), longitude_store, latitude_store);
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

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


        /*// testhttp

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
            if (i % 2 == 0) {
                hotStore.setStarState(2);
            } else {
                hotStore.setStarState(1);
            }
            mHotStoreList.add(hotStore);
        }
        updataData();*/


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

        type = getIntent().getStringExtra("type");
        bar_title.setText(type);


        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        main_rlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(HotStore.class, new HomeItemViewProvider(getApplicationContext()));
        main_rlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(main_rlv, multiTypeAdapter);

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
