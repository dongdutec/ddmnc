package com.dongdutec.ddmnc.ui.my.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.http.RequestUrls;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class StoreManageActivity extends BaseActivity {
    private ImageView bar_left_img;
    private TextView bar_title_text;
    private TextView tv_store_account;
    private TextView tv_store_password;
    private TextView tv_store_web;
    private String userName;
    private String passWord;
    private String webUrl = "https://www.mnctest.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manage);

        initView();
        init();
        bindView();
    }

    @Override
    protected void init() {
        RequestParams params = new RequestParams(RequestUrls.getStoreManage());
        params.addBodyParameter("token", new DbConfig(StoreManageActivity.this).getToken());
        params.setConnectTimeout(5000);
        showLoadings();
        showLog(params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showLog(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    userName = jsonObject.getString("userName");
                    passWord = jsonObject.getString("passWord");

                    tv_store_account.setText(userName);
                    tv_store_password.setText(passWord);
                    tv_store_web.setText(webUrl);


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
                hideLoadings();
            }
        });
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
    protected void initView() {
        bar_left_img = findViewById(R.id.bar_left_img);
        bar_title_text = findViewById(R.id.bar_title_text);
        tv_store_account = findViewById(R.id.tv_store_account);
        tv_store_password = findViewById(R.id.tv_store_password);
        tv_store_web = findViewById(R.id.tv_store_web);

        bar_title_text.setText("商家后台");
    }
}
