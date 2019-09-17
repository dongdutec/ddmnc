package com.dongdutec.ddmnc.ui.home.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseFragment;
import com.dongdutec.ddmnc.ui.home.multitype.HomeItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.model.HotStore;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class HomeFragment extends BaseFragment {

    private SmartRefreshLayout main_refresh;
    private RecyclerView main_rlv;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private List<HotStore> mHotStoreList = new ArrayList<>();

    private int total_all_page;
    private int mRows = 10;  // 设置默认一页加载10条数据
    private int current_page;
    private boolean isLoadMore = false;
    private boolean isLoadOver = false;
    private boolean isLoadMoreSingle = false;//上拉单次标志位
    private boolean isFirstLoad = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        init();
        bindView();
    }

    @Override
    protected void bindView() {

    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        main_rlv = getView().findViewById(R.id.main_rlv);
        main_refresh = getView().findViewById(R.id.main_refresh);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(HotStore.class, new HomeItemViewProvider(getContext()));
        main_rlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(main_rlv, multiTypeAdapter);

    }
}