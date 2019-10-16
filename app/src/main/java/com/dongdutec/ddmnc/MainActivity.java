package com.dongdutec.ddmnc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.http.HtmlUrls;
import com.dongdutec.ddmnc.ui.home.fragment.HomeFragment;
import com.dongdutec.ddmnc.ui.my.fragment.MyFragment;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;
import com.dongdutec.ddmnc.web.WebsActivity;
import com.dongdutec.ddmnc.web.WebsFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE_PICK_CITY = 0;

    private TextView homeTv, browerTv, walletTv, myTv;
    private ImageView homeIv, browerIv, walletIv, myIv;
    private RelativeLayout homeRl, browerRl, walletRl, myRl;
    private List<TextView> textViews = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private List<ImageView> imageViews = new ArrayList<>();


    int[] unselectedMipmap = {R.mipmap.home, R.mipmap.browser, R.mipmap.wallet, R.mipmap.my};
    int[] selectedMipmap = {R.mipmap.home_check, R.mipmap.browser_check, R.mipmap.wallet_check, R.mipmap.my_check};
    private long exitTime = 0;
    private int currentPage = 0;//当前页
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        init();
        bindView();
    }

    @Override
    protected void initView() {
        homeTv = findViewById(R.id.tv_home);
        browerTv = findViewById(R.id.tv_browser);
        walletTv = findViewById(R.id.tv_wallet);
        myTv = findViewById(R.id.tv_my);
        homeIv = (ImageView) findViewById(R.id.img_home);
        browerIv = findViewById(R.id.img_browser);
        walletIv = findViewById(R.id.img_wallet);
        myIv = findViewById(R.id.img_my);
        homeRl = findViewById(R.id.rl_home);
        browerRl = findViewById(R.id.rl_browser);
        walletRl = findViewById(R.id.rl_wallet);
        myRl = findViewById(R.id.rl_my);
    }

    @Override
    protected void init() {
        fragments.add(new HomeFragment());
        WebsFragment fg_browser = new WebsFragment();
        Bundle bundle_browser = new Bundle();
        bundle_browser.putString("title", "浏览器");
        bundle_browser.putString("webUrl", HtmlUrls.getBrowsers() + "?token=" + new DbConfig(MainActivity.this).getToken());
        fg_browser.setArguments(bundle_browser);
        fragments.add(fg_browser);
        WebsFragment fg_wallet = new WebsFragment();
        Bundle bundle_wallet = new Bundle();
        bundle_wallet.putString("title", "钱包");
        bundle_wallet.putString("webUrl", HtmlUrls.getPurse() + "?token=" + new DbConfig(MainActivity.this).getToken() + "&act=" + new DbConfig(MainActivity.this).getLeval());
        fg_wallet.setArguments(bundle_wallet);
        fragments.add(fg_wallet);
        fragments.add(new MyFragment());

        imageViews.add(homeIv);
        imageViews.add(browerIv);
        imageViews.add(walletIv);
        imageViews.add(myIv);

        textViews.add(homeTv);
        textViews.add(browerTv);
        textViews.add(walletTv);
        textViews.add(myTv);


        showFragment(0);
        changeColor(0);
    }

    @Override
    protected void bindView() {

        RxViewAction.clickNoDouble(homeRl).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showFragment(0);
                changeColor(0);
            }
        });
        RxViewAction.clickNoDouble(browerRl).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showFragment(1);
                changeColor(1);
            }
        });
        RxViewAction.clickNoDouble(walletRl).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showFragment(2);
                changeColor(2);
            }
        });
        RxViewAction.clickNoDouble(myRl).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showFragment(3);
                changeColor(3);
            }
        });
    }

    //切换fragment性能优化,使每个fragment只实例化一次
    private void showFragment(int page) {
        currentPage = page;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // 想要显示一个fragment,先隐藏所有fragment，防止重叠
        hideFragments(ft);

        if (fragments.get(page).isAdded()) {

            ft.show(fragments.get(page));

        } else {

            ft.add(R.id.content, fragments.get(page));

        }

        ft.commitAllowingStateLoss();
    }

    // 当fragment已被实例化，相当于发生过切换，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {

        for (Fragment fm : fragments) {

            if (fm.isAdded()) {
                ft.hide(fm);
            }

        }

    }

    private void changeColor(int index) {

        for (int i = 0; i < imageViews.size(); i++) {
            ((ImageView) imageViews.get(i)).setImageResource(unselectedMipmap[i]);

        }

        imageViews.get(index).setImageResource(selectedMipmap[index]);


        for (int i = 0; i < textViews.size(); i++) {

            textViews.get(i).setTextColor(getResources().getColor(R.color.gray2));

        }

        textViews.get(index).setTextColor(getResources().getColor(R.color.c16));

    }

    @Override
    public void onBackPressed() {

        if (currentPage == 1) {//浏览器
            WebsFragment browserFragment = (WebsFragment) fragments.get(1);
            browserFragment.onBackPressed();
            if (!browserFragment.onBackPressed()) {//不可回退时
                backPressed();
            }
        } else if (currentPage == 2) {//钱包
            WebsFragment walletFragment = (WebsFragment) fragments.get(2);
            if (!walletFragment.onBackPressed()) {//不可回退时
                backPressed();
            }
        } else {
            backPressed();
        }
    }

    private void backPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            //记录最后一次按键时间
            exitTime = System.currentTimeMillis();
        } else {
            removeALLActivity();//移除所有Activity
            //终止虚拟机
            System.exit(0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.e(TAG, "onActivityResult:  result.getContents() = " + result.getContents());
                //跳转扫描结果操作页面
                String contents = result.getContents();
                String flag = "";
                String urls = "";
                try {
                    String substring = contents.substring(contents.length() - 5);
                    if ("MNCDD".equals(substring)) {
                        showMessageDialog(getString(R.string.saoma_mid));
                        return;
                    }
                    if (contents != null && contents.length() > 0) {
                        String[] arr = contents.split(",");
                        flag = arr[0];
                        urls = arr[1];
                    }
                    if (flag.equals("MNCDD")) {
                        Intent intent = new Intent(MainActivity.this, WebsActivity.class);
                        intent.putExtra("title", "记账");
                        intent.putExtra("webUrl", urls + "&token=" + new DbConfig(MainActivity.this).getToken());
                        startActivity(intent);
                    } else {
                        showMessageDialog(getString(R.string.saoma_no));
                    }
                } catch (Exception e) {
                    showMessageDialog(getString(R.string.saoma_no));
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


}
