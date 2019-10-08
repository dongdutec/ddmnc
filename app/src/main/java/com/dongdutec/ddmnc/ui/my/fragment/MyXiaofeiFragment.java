package com.dongdutec.ddmnc.ui.my.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseFragment;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.eventbus.MyXiaofeiToRefresh;
import com.dongdutec.ddmnc.http.RequestUrls;
import com.dongdutec.ddmnc.ui.home.multitype.NullListItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.model.NullList;
import com.dongdutec.ddmnc.ui.my.multitype.MyXiaofeiItemViewProvider;
import com.dongdutec.ddmnc.ui.my.multitype.model.MyXiaofei;
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

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class MyXiaofeiFragment extends BaseFragment {

    private SmartRefreshLayout main_refresh;
    private RecyclerView main_rlv;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private List<MyXiaofei> mHotStoreList = new ArrayList<>();

    private int mRows = 10;  // 设置默认一页加载10条数据
    private int current_page = 1;
    private boolean isFirstLoad = true;
    private String TAG = MyXiaofeiFragment.class.getSimpleName();
    private String state;
    private String isStore;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myxiaofei, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        state = bundle.getString("state", "");
        isStore = bundle.getString("isStore", "1");

        initView();
        initCommon(1, mRows);
        bindView();
    }

    @Override
    protected void bindView() {
        main_refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isFirstLoad = false;
                initCommon(current_page++, mRows);
                main_refresh.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mHotStoreList.clear();
                isFirstLoad = true;
                current_page = 1;
                initCommon(1, mRows);
                main_refresh.finishRefresh();
                main_refresh.setEnableLoadMore(true);
            }
        });
    }


    private void initCommon(int page, int rows) {
        //我的消费数据
        RequestParams params = new RequestParams(RequestUrls.getMyXiaofei());
        params.addBodyParameter("token", new DbConfig(getContext()).getToken());
        params.addBodyParameter("state", state);//0待记账 1已记账
        params.addBodyParameter("type", isStore);//1用户 2商家
        params.addBodyParameter("page", current_page + "");
        params.addBodyParameter("rows", mRows + "");
        params.setConnectTimeout(5000);
        Log.e(TAG, "init:  params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: result = " + result);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    if (jsonArray.length() == 0) {
                        main_refresh.setEnableLoadMore(false);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        MyXiaofei myXiaofei = new MyXiaofei();
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        try {
                            JSONObject createTime = object.getJSONObject("createTime");
                            myXiaofei.setTimes(createTime.getLong("time"));
                        } catch (Exception e) {
                            myXiaofei.setTimes(0);
                        }
                        myXiaofei.setAppraise(object.getString("appraise"));
                        myXiaofei.setImgUrl(object.getString("image"));
                        myXiaofei.setTitle("扫码快速支付");
                        myXiaofei.setPrice(object.getDouble("money"));
                        myXiaofei.setPhone(object.getString("phone"));
                        myXiaofei.setOrderId(object.getString("id"));
                        myXiaofei.setOrderState(object.getString("state"));
                        myXiaofei.setStoreName(object.getString("shopName"));
                        myXiaofei.setCustomerId(object.getString("customerId"));
                        myXiaofei.setStoreId(object.getString("shopId"));
                        //1用户 2商家
                        myXiaofei.setIsStore(isStore);

                        mHotStoreList.add(myXiaofei);
                    }
                    updataData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: ex.toString() = " + ex.toString());
                updataData();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }


    private void updataData() {
        items.clear();
        if (((mHotStoreList == null || mHotStoreList.size() == 0) && isFirstLoad)) {
            items.add(new NullList());
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
        main_refresh = getView().findViewById(R.id.homebtn_refresh);
        main_rlv = getView().findViewById(R.id.homebtn_rlv);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        main_rlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(MyXiaofei.class, new MyXiaofeiItemViewProvider(getContext()));
        multiTypeAdapter.register(NullList.class, new NullListItemViewProvider(getContext()));
        main_rlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(main_rlv, multiTypeAdapter);

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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void myXiaofeiToRefreshEvent(MyXiaofeiToRefresh event) {
        if (isStore.equals(event.getIsStore())) {
            //刷新
            current_page = 1;
            mHotStoreList.clear();
            initCommon(1, mRows);
        }
    }
}