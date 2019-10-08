package com.dongdutec.ddmnc.ui.my.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.dongdutec.ddmnc.eventbus.UserInfoEvent;
import com.dongdutec.ddmnc.http.RequestUrls;
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
    private EditText et_nickname;
    private File headFile;
    private String TAG = UserInfoActivity.class.getSimpleName();

    private final int CROP_WHDTH = 5;
    private final int CROP_HEIGHT = 4;
    private String headUrl = "";
    private String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Intent intent = getIntent();
        headUrl = intent.getStringExtra("headUrl");
        userName = intent.getStringExtra("userName");

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
        et_nickname = findViewById(R.id.et_nickname);

        bar_title_text.setText("个人信息");

        //设置用户数据
        CircleCrop transformation = new CircleCrop();
        RequestOptions requestOptions = RequestOptions.bitmapTransform(transformation);
        Glide.with(UserInfoActivity.this).load(headUrl)
                .placeholder(R.mipmap.touxiang)
                .apply(requestOptions)
                .into(userimg);
        et_nickname.setText(userName);
    }

    @Override
    protected void init() {

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

                //上传图片
                if (headFile != null) {
                    RequestParams params = new RequestParams(RequestUrls.uploadImage());
                    params.addBodyParameter("token", new DbConfig(UserInfoActivity.this).getToken());
                    params.addBodyParameter("file", headFile);
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
                                    //第二步（修改头像）
                                    postChangeImage();
                                } else {
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

                        }
                    });
                } else {
                    //第二步（修改头像）
                    postChangeImage();
                }

            }
        });
    }

    //第二步修改头像
    private void postChangeImage() {
        RequestParams params = new RequestParams(RequestUrls.changeImage());
        params.setConnectTimeout(5000);
        params.addBodyParameter("token", new DbConfig(UserInfoActivity.this).getToken());
        params.addBodyParameter("headImg", headUrl);
        Log.e(TAG, "postChangeNickname: params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: result2 = " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int code = object.getInt("code");
                    String msg = object.getString("msg");
                    if (code != 0) {
                        Toast.makeText(UserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                    } else {
                        //第三步（修改昵称）
                        postChangeNickname();
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

            }
        });
    }

    //第三步修改昵称
    private void postChangeNickname() {
        RequestParams params = new RequestParams(RequestUrls.changeNickname());
        params.setConnectTimeout(5000);
        params.addBodyParameter("token", new DbConfig(UserInfoActivity.this).getToken());
        params.addBodyParameter("customerName", et_nickname.getText().toString());
        Log.e(TAG, "postChangeNickname: params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: result3 = " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int code = object.getInt("code");
                    String msg = object.getString("msg");
                    Toast.makeText(UserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                    if (code == 0) {
                        EventBus.getDefault().post(new UserInfoEvent());
                        finish();
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
    }
}
