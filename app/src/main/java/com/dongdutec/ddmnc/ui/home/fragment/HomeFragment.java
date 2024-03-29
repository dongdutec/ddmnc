package com.dongdutec.ddmnc.ui.home.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseFragment;
import com.dongdutec.ddmnc.citypicker.CityPickerActivity;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.db.model.User;
import com.dongdutec.ddmnc.http.RequestUrls;
import com.dongdutec.ddmnc.ui.home.activity.ScanActivity;
import com.dongdutec.ddmnc.ui.home.activity.SearchActivity;
import com.dongdutec.ddmnc.ui.home.multitype.BigLineItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.HeadImgItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.HomeItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.MidBannerItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.MidButtonItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.MidRemenItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.NullListItemViewProvider;
import com.dongdutec.ddmnc.ui.home.multitype.beans.BtnsBean;
import com.dongdutec.ddmnc.ui.home.multitype.beans.MidBannerBeans;
import com.dongdutec.ddmnc.ui.home.multitype.beans.TopBannerBeans;
import com.dongdutec.ddmnc.ui.home.multitype.model.BigLine;
import com.dongdutec.ddmnc.ui.home.multitype.model.HeadImg;
import com.dongdutec.ddmnc.ui.home.multitype.model.HotStore;
import com.dongdutec.ddmnc.ui.home.multitype.model.MidBanner;
import com.dongdutec.ddmnc.ui.home.multitype.model.MidButtons;
import com.dongdutec.ddmnc.ui.home.multitype.model.MidRemen;
import com.dongdutec.ddmnc.ui.home.multitype.model.NullList;
import com.dongdutec.ddmnc.utils.location.LocationUtils;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;
import com.google.zxing.integration.android.IntentIntegrator;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;
import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class HomeFragment extends BaseFragment {

    private SmartRefreshLayout main_refresh;
    private RecyclerView main_rlv;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private List<HotStore> mHotStoreList = new ArrayList<>();
    private List<BtnsBean> mBtnsBeanList = new ArrayList<>();

    private List<MidBannerBeans> mMidBannerBeansList = new ArrayList<>();
    private TopBannerBeans topBannerBeans = new TopBannerBeans();


    private LinearLayout ll_souyisou;
    private ImageView img_saoyisao;
    private TextView tv_city;

    private static final int REQUEST_CODE_PICK_CITY = 9912;
    private String TAG = HomeFragment.class.getSimpleName();

    private double mLatitude = 36.263682;
    private double mLongitude = 120.307086;

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {

                    mLatitude = amapLocation.getLatitude();
                    mLongitude = amapLocation.getLongitude();

                    //存储User 定位城市数据
                    DbConfig dbConfig = new DbConfig(getContext());
                    User user = dbConfig.getUser();
                    try {
                        user.setCity(amapLocation.getCity().replace("市", ""));
                        dbConfig.getDbManager().saveOrUpdate(user);
                        tv_city.setText(amapLocation.getCity().replace("市", ""));

                    } catch (DbException e) {
                        e.printStackTrace();
                    }


                    saveUserLocationToDb(mLatitude, mLongitude);
                    //获取热门推荐数据
                    getHotData();

                } else {

                    //获取热门推荐数据（定位失败）下拉刷新重新定位
                    Toast.makeText(getContext(), "定位信息异常，请刷新重试!", Toast.LENGTH_SHORT).show();
                    getHotData();

                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            } else {
                //获取热门推荐数据（定位失败）下拉刷新重新定位
                Toast.makeText(getContext(), "定位信息异常，请刷新重试!", Toast.LENGTH_SHORT).show();
                getHotData();
            }
        }
    };

    private void saveUserLocationToDb(double latitude, double longitude) {
        User user = new DbConfig(getContext()).getUser();
        try {
            user.setLatitude(latitude);
            user.setLongitude(longitude);
            DbConfig dbConfig = new DbConfig(getContext());
            DbManager db = dbConfig.getDbManager();


            db.saveOrUpdate(user);
        } catch (DbException e) {

        }
    }

    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startLocaion();
        initView();
        init();
        bindView();
    }

    @Override
    protected void bindView() {
        //下拉刷新
        main_refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mBtnsBeanList.clear();
                startLocaion();
                init();

                refreshLayout.finishRefresh();
            }
        });
        //搜一搜
        RxViewAction.clickNoDouble(ll_souyisou).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        //扫一扫
        RxViewAction.clickNoDouble(img_saoyisao).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                String leval = new DbConfig(getContext()).getLeval();
                if (!"y".equals(leval)) {
                    showMessageDialog(getContext().getString(R.string.low_leval));
                    return;
                }

                /*以下是启动我们自定义的扫描*/
                IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
                intentIntegrator.setBeepEnabled(true);
                /*设置启动我们自定义的扫描，若不设置，将启动默认*/
                intentIntegrator.setCaptureActivity(ScanActivity.class);
                intentIntegrator.initiateScan();
            }
        });
        //定位
        RxViewAction.clickNoDouble(tv_city).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivityForResult(new Intent(getActivity(), CityPickerActivity.class),
                        REQUEST_CODE_PICK_CITY);
            }
        });
    }

    @Override
    protected void init() {
        //获取8个按钮数据
        RequestParams requestParams = new RequestParams(RequestUrls.getHomeBtns());
        requestParams.setConnectTimeout(5000);
        showLoadings();
        Log.e(TAG, "init: requestParams.toString() = " + requestParams.toString());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: btns result = " + result);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        BtnsBean btnsBean = new BtnsBean();
                        btnsBean.setClassifyImg(jsonObject.getString("classifyImg"));
                        btnsBean.setClassifyName(jsonObject.getString("classifyName"));
                        btnsBean.setId(jsonObject.getString("id"));
                        mBtnsBeanList.add(btnsBean);
                    }
                    updataData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getContext(), "网络异常!", Toast.LENGTH_SHORT).show();
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

        //首页Banner图
        RequestParams params = new RequestParams(RequestUrls.getHomeBannerNew());
        params.setConnectTimeout(5000);
        Log.e(TAG, "init: params.toString() = " + params.toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: banner result = " + result);
                try {
                    JSONArray data = new JSONArray(result);
                    mMidBannerBeansList.clear();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject beans = (JSONObject) data.get(i);
                        if ("1".equals(beans.getString("type"))) {//type 1 置顶图 0 轮播图
                            topBannerBeans = new TopBannerBeans(beans.getString("image"), beans.getString("url"));
                        } else {//type 1 置顶图 0 轮播图
                            MidBannerBeans midBannerBeans = new MidBannerBeans(beans.getString("image"), beans.getString("url"));
                            mMidBannerBeansList.add(midBannerBeans);
                        }
                    }
                    updataData();

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "系统异常!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getContext(), "网络异常!", Toast.LENGTH_SHORT).show();
                updataData();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

        //获取热门推荐数据 在定位回调之后


    }

    /**
     * //首页热门推荐
     */
    private void getHotData() {
        RequestParams requestParams = new RequestParams(RequestUrls.homeHotList());
        requestParams.setConnectTimeout(5000);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: boomer hot result = " + result);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    mHotStoreList.clear();
                    Log.e(TAG, "boomer onSuccess:  jsonArray.length() = " + jsonArray.length());
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
                        double distance = LocationUtils.getDistance(mLongitude, mLatitude, longitude_store, latitude_store);
                        hotStore.setDistance(distance);

                        mHotStoreList.add(hotStore);

                    }
                    updataData();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "boomer2 onSuccess:  e.toString() = " + e.toString());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
        items.add(new HeadImg(topBannerBeans.getImg(), topBannerBeans.getUrl()));
        items.add(new MidButtons(mBtnsBeanList));
        items.add(new MidBanner(mMidBannerBeansList));
        items.add(new BigLine());
        items.add(new MidRemen());
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
    }

    @Override
    protected void initView() {
        main_rlv = getView().findViewById(R.id.main_rlv);
        main_refresh = getView().findViewById(R.id.main_refresh);
        ll_souyisou = getView().findViewById(R.id.ll_souyisou);
        img_saoyisao = getView().findViewById(R.id.img_saoyisao);
        tv_city = getView().findViewById(R.id.tv_city);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        main_rlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(HeadImg.class, new HeadImgItemViewProvider(getContext()));
        multiTypeAdapter.register(MidButtons.class, new MidButtonItemViewProvider(getContext()));
        multiTypeAdapter.register(MidBanner.class, new MidBannerItemViewProvider(getContext()));
        multiTypeAdapter.register(BigLine.class, new BigLineItemViewProvider(getContext()));
        multiTypeAdapter.register(MidRemen.class, new MidRemenItemViewProvider(getContext()));
        multiTypeAdapter.register(HotStore.class, new HomeItemViewProvider(getContext()));
        multiTypeAdapter.register(NullList.class, new NullListItemViewProvider(getContext()));
        main_rlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(main_rlv, multiTypeAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK) {
            if (data != null) {
                String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                tv_city.setText(city);

                //存储User 定位城市数据
                DbConfig dbConfig = new DbConfig(getContext());
                User user = dbConfig.getUser();
                try {
                    user.setCity(city);
                    dbConfig.getDbManager().saveOrUpdate(user);

                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        } else {

        }
    }


    //获取经纬度

    public void startLocaion() {

        mLocationClient = new AMapLocationClient(getContext());
        mLocationClient.setLocationListener(mLocationListener);

        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
}