package com.dongdutec.ddmnc.ui.home.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.utils.guide.OpenLocalMapUtil;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import rx.functions.Action1;

public class RouteMapActivity extends BaseActivity {
    private ImageView bar_left_img;
    private TextView bar_title_text;

    private String startLat;
    private String startLong;
    private String endLat;
    private String endLong;
    private String startName;
    private String endName;
    private String city_;

    private static String APP_NAME = "MNC";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        bar_left_img = findViewById(R.id.bar_left_img);
        bar_title_text = findViewById(R.id.bar_title_text);
        bar_title_text.setText("路线规划");
        //返回
        RxViewAction.clickNoDouble(bar_left_img).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        startLat = bundle.getString("user_latitude");
        startLong = bundle.getString("user_longitude");
        endLat = bundle.getString("store_latitude");
        endLong = bundle.getString("store_longitude");
        endName = bundle.getString("storeName");
        startName = "我的位置";
        city_ = "北京";


        //高德地图
        if (!OpenLocalMapUtil.isGdMapInstalled()) {
            // 驾车路线规划
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.autonavi.minimap");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse("androidamap://route?sourceApplication=" + R.string.app_name
                    + "&sname=" + startName + "&dlat=" + endLat
                    + "&dlon=" + endLong
                    + "&dname=" + endName
                    + "&dev=0&m=0&t=0"));
            startActivity(intent);
            finish();
        } else if (!OpenLocalMapUtil.isBaiduMapInstalled()) {//百度地图
            double[] doubles = OpenLocalMapUtil.gaoDeToBaidu(Double.parseDouble(endLong), Double.parseDouble(endLat));
            // 驾车路线规划
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("baidumap://map/direction?origin=" + startName + "&destination=name:"
                    + endName
                    + "|latlng:" + doubles[1] + "," + doubles[0]
                    + "&mode=driving&sy=3&index=0&target=1"));
            startActivity(intent);
            finish();
        } else {
            openGaoDeWebMap();
            finish();
        }


    }


    /**
     * 打开浏览器进行百度地图导航
     */
    private void openBaiduWebMap(double slat, double slon, String sname, double dlat, double dlon, String dname, String city) {
        Uri mapUri = Uri.parse(OpenLocalMapUtil.getWebBaiduMapUri(String.valueOf(slat), String.valueOf(slon), sname,
                String.valueOf(dlat), String.valueOf(dlon),
                dname, city, APP_NAME));
        Intent loction = new Intent(Intent.ACTION_VIEW, mapUri);
        startActivity(loction);
    }

    /**
     * 打开浏览器进行高德地图导航
     */
    private void openGaoDeWebMap() {
        Intent intent = new Intent();
        intent.setData(Uri.parse("http://uri.amap.com/navigation?to=" + endLong + "," + endLat + "," + endName + "&mode=car&policy=1&src=mypage&coordinate=gaode&callnative=0"));
        startActivity(intent);

    }


}
