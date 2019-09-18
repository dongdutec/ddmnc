package com.dongdutec.ddmnc.ui.home.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseFragment;
import com.dongdutec.ddmnc.ui.home.multitype.BigLineItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.HeadImgItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.HomeItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.MidBannerItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.MidButtonItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.MidRemenItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.model.BigLine;
import com.dongdutec.ddmnc.ui.home.multitype.model.HeadImg;
import com.dongdutec.ddmnc.ui.home.multitype.model.HotStore;
import com.dongdutec.ddmnc.ui.home.multitype.model.MidBanner;
import com.dongdutec.ddmnc.ui.home.multitype.model.MidButtons;
import com.dongdutec.ddmnc.ui.home.multitype.model.MidRemen;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
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
        //http

        HotStore hotStore;
        for (int i = 0; i < 32; i++) {
            hotStore = new HotStore();
            hotStore.setImageUrl("www.xxxxxxxxxxxxx");
            hotStore.setStoreName("东度科技青岛测试店" + i);
            hotStore.setLocationStr("青岛市·黄岛区·长江路·国贸大厦");
            hotStore.setCount(i);
            hotStore.setDistance(1);
            mHotStoreList.add(hotStore);
        }
        updataData();


    }


    private void updataData() {
        items.clear();
        items.add(new HeadImg());
        items.add(new MidButtons());
        items.add(new MidBanner());
        items.add(new BigLine());
        items.add(new MidRemen());
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
        main_rlv = getView().findViewById(R.id.main_rlv);
        main_refresh = getView().findViewById(R.id.main_refresh);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        main_rlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(HeadImg.class, new HeadImgItemViewProvider(getContext()));
        multiTypeAdapter.register(MidButtons.class, new MidButtonItemViewProvider(getContext()));
        multiTypeAdapter.register(MidBanner.class, new MidBannerItemViewProvider(getContext()));
        multiTypeAdapter.register(BigLine.class, new BigLineItemViewProvider(getContext()));
        multiTypeAdapter.register(MidRemen.class, new MidRemenItemViewProvider(getContext()));
        multiTypeAdapter.register(HotStore.class, new HomeItemViewProvider(getContext()));
        main_rlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(main_rlv, multiTypeAdapter);

    }
}