package com.dongdutec.ddmnc.ui.my.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.cell.MNCTransparentDialog;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.http.RequestUrls;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class ChangeJYSaddressActivity extends BaseActivity {
    private ImageView bar_left_img;
    private TextView bar_title_text;


    private EditText et_newaddress;
    private TextView tv_phone;
    private EditText dt_yanzhengma;
    private TextView tv_yanzhengma;
    private TextView save;

    private String TAG = ChangeJYSaddressActivity.class.getSimpleName();
    private int numCount = 60;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    numCount--;

                    if (numCount <= 0) {
                        tv_yanzhengma.setText("重新发送");
                        tv_yanzhengma.setClickable(true);
                        tv_yanzhengma.setBackgroundResource(R.drawable.blue_coner);
                        numCount = 60;
                    } else {
                        tv_yanzhengma.setText(numCount + "s后重新发送");
                        tv_yanzhengma.setClickable(false);
                        tv_yanzhengma.setBackgroundResource(R.drawable.gray_coner);
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_jysaddress);


        initView();
        init();
        bindView();
    }

    @Override
    protected void initView() {
        bar_left_img = findViewById(R.id.bar_left_img);
        bar_title_text = findViewById(R.id.bar_title_text);


        et_newaddress = findViewById(R.id.et_newaddress);
        tv_phone = findViewById(R.id.tv_phone);
        dt_yanzhengma = findViewById(R.id.dt_yanzhengma);
        tv_yanzhengma = findViewById(R.id.tv_yanzhengma);
        save = findViewById(R.id.save);


        bar_title_text.setText("个人信息");
        tv_phone.setText(new DbConfig(ChangeJYSaddressActivity.this).getPhone());
    }

    @Override
    protected void init() {

    }

    private void errorSengMsg() {
        mHandler.removeMessages(0);
        tv_yanzhengma.setText("重新发送");
        tv_yanzhengma.setClickable(true);
        tv_yanzhengma.setBackgroundResource(R.drawable.blue_coner);
        numCount = 60;
    }

    protected void showSubmitDialog() {
        final MNCTransparentDialog mncTransDialog = new MNCTransparentDialog(ChangeJYSaddressActivity.this);
        View dialogView = LayoutInflater.from(ChangeJYSaddressActivity.this).inflate(R.layout.dialog_tokendown, null, false);
        TextView message_text = (TextView) dialogView.findViewById(R.id.message_text);
        message_text.setText(R.string.message_changeaddress);
        final TextView tv_queren = (TextView) dialogView.findViewById(R.id.tv_right);
        final TextView tv_left = (TextView) dialogView.findViewById(R.id.tv_left);
        //确认
        RxViewAction.clickNoDouble(tv_queren).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mncTransDialog.dismiss();
                submitPost();
            }
        });
        //取消
        RxViewAction.clickNoDouble(tv_left).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mncTransDialog.dismiss();
            }
        });
        mncTransDialog.show();
        Window window = mncTransDialog.getWindow();//对话框窗口
        window.setGravity(Gravity.CENTER);//设置对话框显示在屏幕中间
        window.setWindowAnimations(R.style.dialog_style);//添加动画
        window.setContentView(dialogView);

    }

    private void submitPost() {
        save.setBackgroundResource(R.drawable.save_btn_gray1);
        save.setClickable(false);

        RequestParams params = new RequestParams(RequestUrls.getChangeJYSaddress());
        showLoadings();
        params.setConnectTimeout(5000);
        params.addBodyParameter("phone", tv_phone.getText().toString());
        params.addBodyParameter("code", dt_yanzhengma.getText().toString());
        params.addBodyParameter("adress", et_newaddress.getText().toString());
        params.addBodyParameter("token", new DbConfig(ChangeJYSaddressActivity.this).getToken());
        Log.e(TAG, "call: " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String state = jsonObject.getString("state");
                    if ("0".equals(state)) {
                        showToast("交易所地址修改成功!");
                        Intent data = new Intent();
                        data.putExtra("addressStr", et_newaddress.getText().toString());
                        ChangeJYSaddressActivity.this.setResult(1278, data);
                        finish();
                    } else if ("3".equals(state)) {
                        showToast("验证码失效!");
                    } else if ("2".equals(state)) {
                        showTokenDownDialog();
                    } else {
                        showToast("系统异常!");
                    }
                    save.setBackgroundResource(R.drawable.save_btn_blue);
                    save.setClickable(true);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ChangeJYSaddressActivity.this, "请求超时,请重试!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideLoadings();
                save.setBackgroundResource(R.drawable.save_btn_blue);
                save.setClickable(true);
            }
        });
    }

    @Override
    protected void bindView() {
        //提交
        RxViewAction.clickNoDouble(save).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (et_newaddress.getText() == null || et_newaddress.getText().length() == 0) {
                    Toast.makeText(ChangeJYSaddressActivity.this, "请输入新的交易所密码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dt_yanzhengma.getText().toString().length() < 6) {
                    Toast.makeText(ChangeJYSaddressActivity.this, "请输入正确的短信验证码!", Toast.LENGTH_SHORT).show();
                    return;
                }

                showSubmitDialog();

            }
        });
        //获取验证码
        RxViewAction.clickNoDouble(tv_yanzhengma).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mHandler.sendEmptyMessage(0);

                RequestParams params = new RequestParams(RequestUrls.getMsgCodeNew());
                params.setConnectTimeout(5000);
                params.addBodyParameter("phone", tv_phone.getText().toString());
                Log.e(TAG, "boomer call:  params.toString() = " + params.toString());
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            Log.e(TAG, "boomer onSuccess: result = " + result);
                            JSONObject object = new JSONObject(result);
                            String state = object.getString("state");
                            if (!"0".equals(state)) {
                                Toast.makeText(ChangeJYSaddressActivity.this, "短信发送失败!", Toast.LENGTH_SHORT).show();
                                errorSengMsg();
                            } else {
                                Toast.makeText(ChangeJYSaddressActivity.this, "短信发送成功!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangeJYSaddressActivity.this, "发送失败,请重试!", Toast.LENGTH_SHORT).show();
                            errorSengMsg();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(ChangeJYSaddressActivity.this, "发送失败,请重试!", Toast.LENGTH_SHORT).show();

                        errorSengMsg();
                        Log.e(TAG, "boomer onError:  ex.toString() = " + ex.toString());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });


        //返回
        RxViewAction.clickNoDouble(bar_left_img).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
    }
}
