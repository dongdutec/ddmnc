package com.dongdutec.ddmnc.ui.my.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.eventbus.SearchMyToRefresh;
import com.dongdutec.ddmnc.http.RequestUrls;
import com.dongdutec.ddmnc.utils.bank.BankUtils;
import com.dongdutec.ddmnc.utils.file.UriTofilePath;
import com.dongdutec.ddmnc.utils.image.CropImgUtil;
import com.dongdutec.ddmnc.utils.image.ImageUtils;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import rx.functions.Action1;

public class UserInfoActivity extends BaseActivity {
    private ImageView bar_left_img;
    private TextView bar_title_text;
    private ImageView userimg;
    private Bitmap imgBitmap;
    private TextView save;
    private TextView change;
    private EditText et_nickname;
    private EditText et_jiaoyi;
    private EditText et_zhifubao;
    private EditText et_yinhangka;
    private File headFile;
    private String TAG = UserInfoActivity.class.getSimpleName();

    private final int CROP_WHDTH = 5;
    private final int CROP_HEIGHT = 4;
    private String headUrl = "";
    private String userName = "";
    private String jiaoyisuoStr = "";
    private String zhifubaoStr = "";
    private String yinhangkaStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Intent intent = getIntent();
        headUrl = intent.getStringExtra("headUrl");
        userName = intent.getStringExtra("userName");
        jiaoyisuoStr = intent.getStringExtra("adress");
        zhifubaoStr = intent.getStringExtra("alipay");
        yinhangkaStr = intent.getStringExtra("bankCard");

        initView();
        init();
        bindView();
    }

    @Override
    protected void initView() {
        bar_left_img = findViewById(R.id.bar_left_img);
        bar_title_text = findViewById(R.id.bar_title_text);
        userimg = findViewById(R.id.userimg);
        save = findViewById(R.id.save);
        change = findViewById(R.id.change);
        et_nickname = findViewById(R.id.et_nickname);
        et_jiaoyi = findViewById(R.id.et_jiaoyi);
        et_zhifubao = findViewById(R.id.et_zhifubao);
        et_yinhangka = findViewById(R.id.et_yinhangka);

        bar_title_text.setText("个人信息");

        //设置用户数据
        CircleCrop transformation = new CircleCrop();
        RequestOptions requestOptions = RequestOptions.bitmapTransform(transformation);
        Glide.with(UserInfoActivity.this).load(headUrl)
                .placeholder(R.mipmap.touxiang)
                .apply(requestOptions)
                .into(userimg);
        et_nickname.setText(userName);
        if ((jiaoyisuoStr != null && jiaoyisuoStr.length() > 0)) {
            et_jiaoyi.setText(jiaoyisuoStr);
            et_jiaoyi.setEnabled(false);
            change.setVisibility(View.VISIBLE);
        } else {
            et_jiaoyi.setEnabled(true);
            change.setVisibility(View.GONE);
        }
        et_zhifubao.setText(zhifubaoStr);
        et_yinhangka.setText(yinhangkaStr);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void bindView() {
        //更换
        RxViewAction.clickNoDouble(change).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(UserInfoActivity.this, ChangeJYSaddressActivity.class);
                startActivityForResult(intent, 1);

            }
        });
        //返回
        RxViewAction.clickNoDouble(bar_left_img).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        //修改头像
        RxViewAction.clickNoDouble(userimg).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //选择裁剪
                CropImgUtil.choicePhoto(UserInfoActivity.this);
            }
        });
        //保存
        RxViewAction.clickNoDouble(save).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (et_nickname.getText() == null || et_nickname.getText().toString().length() == 0) {
                    Toast.makeText(UserInfoActivity.this, "请输入昵称!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //验证支付宝账号
                if (et_zhifubao.getText() == null || et_zhifubao.getText().toString().length() == 0) {
                    showToast("请输入正确的支付宝账号!");
                    return;
                }

                //验证银行卡账号
                if (et_yinhangka.getText() == null || et_yinhangka.getText().toString().length() < 16) {
                    showToast("请输入正确的银行卡号!");
                    return;
                }
                if (!BankUtils.judgeBank(et_yinhangka.getText().toString())) {
                    showToast("请输入正确的银行卡号!");
                    return;
                }

                judgeToken();

            }
        });
    }

    @Override
    protected void onJudgeResult() {
        //上传图片
        if (headFile != null) {
            RequestParams params = new RequestParams(RequestUrls.uploadImage());
            params.addBodyParameter("token", new DbConfig(UserInfoActivity.this).getToken());
            params.addBodyParameter("file", headFile);
            params.setConnectTimeout(5000);
            Log.e(TAG, "call: params.toString() = " + params.toString());
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.e(TAG, "onSuccess: result1 = " + result);
                    try {
                        JSONObject object = new JSONObject(result);
                        int code = object.getInt("code");
                        String msg = object.getString("msg");
                        if (code == 0) {
                            JSONArray data = object.getJSONArray("data");
                            headUrl = (String) data.get(0);
                            //第二步（保存）
                            postSave();
                        } else {
                            hideLoadings();
                            Toast.makeText(UserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(UserInfoActivity.this, "系统异常!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    hideLoadings();
                }
            });
        } else {
            //第二步（修改头像）
            postSave();
        }
    }

    private void postSave() {
        RequestParams params = new RequestParams(RequestUrls.changeUserInfo());
        params.setConnectTimeout(5000);
        showLoadings();
        params.addBodyParameter("token", new DbConfig(UserInfoActivity.this).getToken());
        params.addBodyParameter("url", headUrl);
        params.addBodyParameter("name", et_nickname.getText().toString());
        params.addBodyParameter("address", et_jiaoyi.getText().toString());
        params.addBodyParameter("alipay", et_zhifubao.getText().toString());
        params.addBodyParameter("bankCard", et_yinhangka.getText().toString());
        Log.e(TAG, "postSave: " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: " + result);
                    JSONObject jsonObject = new JSONObject(result);
                    String state = jsonObject.getString("state");
                    if ("0".equals(state)) {
                        showToast(getResources().getString(R.string.userinfo_updata));
                        finish();
                    } else if ("1".equals(state)) {
                        showToast("系统异常!");
                    } else {
                        showTokenDownDialog();
                    }


                } catch (
                        JSONException e)

                {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CropImgUtil.TAKE_PHOTO://相机返回
                    //相机返回图片，调用裁剪的方法
                    CropImgUtil.startUCrop(UserInfoActivity.this, CropImgUtil.imageUri, CROP_WHDTH, CROP_HEIGHT);
                    break;
                case CropImgUtil.CHOOSE_PHOTO://相册返回
                    try {
                        if (data != null) {
                            Uri uri2 = data.getData();
                            //相册返回图片，调用裁剪的方法
                            CropImgUtil.startUCrop(UserInfoActivity.this, uri2, CROP_WHDTH, CROP_HEIGHT);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "图片选择失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case UCrop.REQUEST_CROP://裁剪返回
                    Uri resultUri = null;
                    resultUri = UCrop.getOutput(data);
                    try {
                        headFile = new Compressor(getApplicationContext()).compressToFile(new File(UriTofilePath.getFilePathByUri(UserInfoActivity.this, resultUri)));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //剪切返回，显示剪切的图片到布局
                    Glide.with(UserInfoActivity.this)
                            .load(resultUri)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .placeholder(R.mipmap.touxiang)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                            .skipMemoryCache(true)//跳过内存缓存
                            .into(userimg);
                    try {
                        imgBitmap = ImageUtils.getBitmapFormUri(getApplicationContext(), resultUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;

            }
        }
        if (resultCode == 1278) {
            et_jiaoyi.setText(data.getStringExtra("addressStr"));
            et_jiaoyi.setEnabled(false);
            change.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new SearchMyToRefresh());
    }
}
