package com.dongdutec.ddmnc.ui.my.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.eventbus.MyXiaofeiToRefresh;
import com.dongdutec.ddmnc.http.RequestUrls;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class PingjiaActivity extends BaseActivity {
    private ImageView back;
    private TextView title;

    private ImageView image;
    private TextView tv_storename;
    private EditText et_content;
    private TextView tv_submit;
    private String imageUrl;
    private String storeName;
    private String shopId;
    private String id;
    private String state;
    private String isStore;
    private String TAG = PingjiaActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pingjia);

        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("imageUrl");
        storeName = intent.getStringExtra("storeName");
        shopId = intent.getStringExtra("shopId");
        id = intent.getStringExtra("id");
        state = intent.getStringExtra("state");
        isStore = intent.getStringExtra("isStore");

        initView();
        init();
        bindView();
    }

    @Override
    protected void initView() {
        back = findViewById(R.id.bar_left_img);
        title = findViewById(R.id.bar_title_text);
        image = findViewById(R.id.image);
        tv_storename = findViewById(R.id.tv_storename);
        et_content = findViewById(R.id.et_content);
        tv_submit = findViewById(R.id.tv_submit);

        title.setText("评价");
        RoundedCorners roundedCorners = new RoundedCorners(10);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(PingjiaActivity.this).load(imageUrl)
                .apply(options)
                .into(image);
        tv_storename.setText(storeName);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void bindView() {
        //返回
        RxViewAction.clickNoDouble(back).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        //提交
        RxViewAction.clickNoDouble(tv_submit).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (et_content.getText() == null || et_content.getText().length() == 0) {
                    Toast.makeText(PingjiaActivity.this, "请输入评价!", Toast.LENGTH_SHORT).show();
                    return;
                }
                judgeToken();
            }
        });
    }

    @Override
    protected void onJudgeResult() {
        //提交post
        RequestParams params = new RequestParams(RequestUrls.insertDiscuss());
        params.addBodyParameter("token", new DbConfig(PingjiaActivity.this).getToken());
        params.addBodyParameter("shopId", shopId);
        params.addBodyParameter("id", id);
        params.addBodyParameter("remark", et_content.getText().toString());
        params.setConnectTimeout(5000);
        showLoadings();
        Log.e(TAG, "call: params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: result = " + result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int states = jsonObject.getInt("state");
                    if (states == 0) {
                        finish();
                        //通知Fragment刷新
                        Toast.makeText(PingjiaActivity.this, "评价成功!", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new MyXiaofeiToRefresh(state, isStore));
                    } else {
                        Toast.makeText(PingjiaActivity.this, "系统错误!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PingjiaActivity.this, "网络异常!", Toast.LENGTH_SHORT).show();
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
}
