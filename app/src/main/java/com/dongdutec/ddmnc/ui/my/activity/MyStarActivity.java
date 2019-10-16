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
import android.widget.Toast;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.eventbus.MyStarToRefresh;
import com.dongdutec.ddmnc.http.RequestUrls;
import com.dongdutec.ddmnc.ui.home.multitype.HomeItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.NullListLongItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.model.HotStore;
import com.dongdutec.ddmnc.ui.home.multitype.model.NullList;
import com.dongdutec.ddmnc.utils.location.LocationUtils;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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

    private int mRows = 10;  // 设置默认一页加载10条数据
    private int current_page = 1;
    private boolean isFirstLoad = true;
    private String type;
    private String TAG = MyStarActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commen_list);

        EventBus.getDefault().register(this);

        initView();
        initCommon();
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
                initCommon();
                main_refresh.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mHotStoreList.clear();
                isFirstLoad = true;
                current_page = 1;
                initCommon();
                main_refresh.finishRefresh();
                main_refresh.setEnableLoadMore(true);
            }
        });
        //返回
        RxViewAction.clickNoDouble(bar_left_img).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
    }

    private void initCommon() {
        judgeToken();
    }

    @Override
    protected void onJudgeResult() {
        //获取收藏列表
        final RequestParams params = new RequestParams(RequestUrls.getStarList());
        params.setConnectTimeout(5000);
        showLoadings();
        params.addBodyParameter("token", new DbConfig(MyStarActivity.this).getToken());
        params.addBodyParameter("page", current_page + "");
        params.addBodyParameter("rows", mRows + "");
        Log.e(TAG, "onSuccess: params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: result = " + result);
                    JSONArray jsonArray = new JSONArray(result);
                    if (jsonArray.length() == 0) {//refresh
                        main_refresh.setEnableLoadMore(false);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        HotStore hotStore = new HotStore();
                        hotStore.setImageUrl(object.getString("image"));
                        hotStore.setStoreName(object.getString("shopName"));
                        hotStore.setLocationStr(object.getString("address"));
                        hotStore.setStoreId(object.getString("shopId"));
                        hotStore.setId(object.getString("id"));
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
                        hotStore.setStarState(object.getString("state"));

                        mHotStoreList.add(hotStore);
                    }

                    updataData();

                } catch (JSONException e) {
                    Toast.makeText(MyStarActivity.this, "系统异常!", Toast.LENGTH_SHORT).show();
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
                hideLoadingsDelayed(500);
            }
        });
    }

    private void updataData() {
        items.clear();
        if ((mHotStoreList == null || mHotStoreList.size() == 0) && isFirstLoad) {
            items.add(new NullList());
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
        multiTypeAdapter.register(HotStore.class, new HomeItemViewProvider(MyStarActivity.this));
        multiTypeAdapter.register(NullList.class, new NullListLongItemViewProvider(getApplicationContext()));
        main_rlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(main_rlv, multiTypeAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void myStarToRefreshEvent(MyStarToRefresh event) {
        mHotStoreList.clear();
        initCommon();
    }
}
