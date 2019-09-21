package com.dongdutec.ddmnc.ui.home.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.ui.home.multitype.HomeItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.model.HotStore;
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

public class HomeBtnListActivity extends BaseActivity {
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

    private LinearLayout ll_choosecity;
    private ImageView img_choosecity;
    private LinearLayout ll_first;
    private ImageView img_first;
    private LinearLayout ll_distance;
    private ImageView img_distance;
    private LinearLayout ll_price;
    private ImageView img_price;
    private LinearLayout ll_sellcount;
    private ImageView img_sellcount;
    private ArrayList<String> filterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_btn_list);

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
        //选择市区
        RxViewAction.clickNoDouble(ll_choosecity).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showFilterPop(0, ll_choosecity.getWidth(), img_choosecity);
            }
        });
        RxViewAction.clickNoDouble(ll_first).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showFilterPop(ll_choosecity.getWidth(), ll_first.getWidth(), img_first);
            }
        });
        //距离
        RxViewAction.clickNoDouble(ll_distance).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showFilterPop(ll_choosecity.getWidth() + ll_first.getWidth(), ll_distance.getWidth(), img_distance);
            }
        });
        //价格
        RxViewAction.clickNoDouble(ll_price).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showFilterPop(ll_choosecity.getWidth() + ll_first.getWidth() + ll_distance.getWidth(), ll_price.getWidth(), img_price);
            }
        });
        //销量
        RxViewAction.clickNoDouble(ll_sellcount).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showFilterPop(ll_choosecity.getWidth() + ll_first.getWidth() + ll_distance.getWidth() + ll_price.getWidth(), ll_sellcount.getWidth(), img_sellcount);
            }
        });

    }

    /**
     * 弹出筛选框
     */
    private void showFilterPop(int left_x, int width, final ImageView downImg) {
        filterData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            filterData.add("东度" + i);
        }
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupwindow, null);

        // 为了演示效果，简单的设置了一些数据，实际中大家自己设置数据即可，相信大家都会。
        ListView lsvMore = (ListView) view.findViewById(R.id.lsvMore);
        lsvMore.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.ll, filterData));

        // 创建PopupWindow对象，指定宽度和高度
        final PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setWidth(width);
        // 设置动画
//        window.setAnimationStyle(R.style.popup_window_anim);
        // 设置背景颜色
        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_coner));
        // 设置可以获取焦点
        window.setFocusable(true);
        // 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // 更新popupwindow的状态
        window.update();
        // 以下拉的方式显示，并且可以设置显示的位置
//        window.showAsDropDown(tvReport, 0, 20);
        window.showAtLocation(ll_choosecity, Gravity.LEFT | Gravity.TOP, left_x, ll_choosecity.getHeight() + ll_state.getHeight() + rl_bar.getHeight() + getStatusBarHeight());//这里的50是因为我底部按钮的高度是50
        downImg.setVisibility(View.INVISIBLE);
        lsvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                init();//test
                window.dismiss();
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                downImg.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 利用反射获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        } else {
            return 72;
        }
        return result;
    }

    @Override
    protected void init() {
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
        ll_choosecity = findViewById(R.id.ll_choosecity);
        img_choosecity = findViewById(R.id.img_choosecity);
        ll_first = findViewById(R.id.ll_first);
        img_first = findViewById(R.id.img_first);
        ll_distance = findViewById(R.id.ll_distance);
        img_distance = findViewById(R.id.img_distance);
        ll_price = findViewById(R.id.ll_price);
        img_price = findViewById(R.id.img_price);
        ll_sellcount = findViewById(R.id.ll_sellcount);
        img_sellcount = findViewById(R.id.img_sellcount);

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
