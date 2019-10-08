package com.dongdutec.ddmnc.ui.login.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.http.RequestUrls;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class ForgetPWActivity extends BaseActivity {
    private ImageView back;
    private TextView bar_title;
    private View v_line;

    private EditText dt_phone;
    private ImageView img_cha;
    private EditText dt_yanzhengma;
    private TextView tv_yanzhengma;
    private EditText dt_password;
    private CheckBox ck_yanjing;
    private EditText dt_password_second;
    private CheckBox ck_yanjing_second;
    private TextView tv_queren;
    private TextView tv_tiaokuan;
    private CheckBox ck_tiaokuan;

    private int numCount = 60;

    private boolean canLogin_phone = false;
    private boolean canLogin_yanzhengma = false;
    private boolean canLogin_password = false;
    private boolean canLogin_password_second = false;
    private boolean canLogin_tiaokuan = false;

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
    private String TAG = ForgetPWActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        initView();
        init();
        bindView();
    }

    @Override
    protected void initView() {
        back = findViewById(R.id.bar_left_img);
        bar_title = findViewById(R.id.bar_title_text);
        v_line = findViewById(R.id.v_line);
        dt_phone = findViewById(R.id.dt_phone);
        img_cha = findViewById(R.id.img_cha);
        dt_yanzhengma = findViewById(R.id.dt_yanzhengma);
        tv_yanzhengma = findViewById(R.id.tv_yanzhengma);
        dt_password = findViewById(R.id.dt_password);
        ck_yanjing = findViewById(R.id.ck_yanjing);
        dt_password_second = findViewById(R.id.dt_password_second);
        ck_yanjing_second = findViewById(R.id.ck_yanjing_second);
        tv_queren = findViewById(R.id.tv_queren);
        tv_tiaokuan = findViewById(R.id.tv_tiaokuan);
        ck_tiaokuan = findViewById(R.id.ck_tiaokuan);

        back.setImageResource(R.mipmap.back_login);
        bar_title.setText("忘记密码");
        bar_title.setTextColor(getResources().getColor(R.color.theme_primary));
        v_line.setVisibility(View.INVISIBLE);

        img_cha.setVisibility(View.GONE);
        ck_yanjing.setVisibility(View.GONE);
        ck_yanjing_second.setVisibility(View.GONE);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void bindView() {
        //条款
        RxViewAction.clickNoDouble(tv_tiaokuan).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showTiaoKuanDialog();
            }
        });
        //返回
        RxViewAction.clickNoDouble(back).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        //输入手机号监听
        dt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    canLogin_phone = true;
                    img_cha.setVisibility(View.VISIBLE);
                } else {
                    canLogin_phone = false;
                    img_cha.setVisibility(View.GONE);
                }
                changeLoginBtnState();//修改登录按钮状态
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //手机号 × clear
        RxViewAction.clickNoDouble(img_cha).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dt_phone.setText("");
            }
        });
        //短信验证码输入监听
        dt_yanzhengma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    canLogin_yanzhengma = true;
                } else {
                    canLogin_yanzhengma = false;
                }
                changeLoginBtnState();//修改登录按钮状态
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //输入密码监听
        dt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    canLogin_password = true;
                    ck_yanjing.setVisibility(View.VISIBLE);
                } else {
                    canLogin_password = false;
                    ck_yanjing.setVisibility(View.GONE);
                }
                changeLoginBtnState();//修改登录按钮状态
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //密码小眼睛
        ck_yanjing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    dt_password.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    dt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        //再次输入密码监听
        dt_password_second.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    canLogin_password_second = true;
                    ck_yanjing_second.setVisibility(View.VISIBLE);
                } else {
                    canLogin_password_second = false;
                    ck_yanjing_second.setVisibility(View.GONE);
                }
                changeLoginBtnState();//修改登录按钮状态
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //再次密码小眼睛
        ck_yanjing_second.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    dt_password_second.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    dt_password_second.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        //服务条款
        ck_tiaokuan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    canLogin_tiaokuan = true;
                } else {
                    canLogin_tiaokuan = false;
                }
                changeLoginBtnState();//修改登录按钮状态
            }
        });

        //确认
        RxViewAction.clickNoDouble(tv_queren).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //判断输入规范
                if (dt_phone.getText().toString().length() != 11) {
                    Toast.makeText(ForgetPWActivity.this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dt_yanzhengma.getText().toString().length() < 6) {
                    Toast.makeText(ForgetPWActivity.this, "请输入正确的短信验证码!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dt_password.getText().toString().length() < 6) {
                    Toast.makeText(ForgetPWActivity.this, "密码长度至少为6位!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dt_password_second.getText().toString().length() < 6) {
                    Toast.makeText(ForgetPWActivity.this, "密码长度至少为6位!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!dt_password.getText().toString().equals(dt_password_second.getText().toString())) {
                    Toast.makeText(ForgetPWActivity.this, "两次密码输入不一致!", Toast.LENGTH_SHORT).show();
                    return;
                }


                tv_queren.setBackgroundResource(R.drawable.save_btn_gray1);
                tv_queren.setClickable(false);

                //判断通过
                //post
                RequestParams params = new RequestParams(RequestUrls.forgetpassword());
                params.setConnectTimeout(5000);
                params.addBodyParameter("phone", dt_phone.getText().toString());
                params.addBodyParameter("code", dt_yanzhengma.getText().toString());
                params.addBodyParameter("password", dt_password.getText().toString());
                params.addBodyParameter("repeatPassword", dt_password_second.getText().toString());
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "onSuccess: result = " + result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String msg = jsonObject.getString("msg");
                            int code = jsonObject.getInt("code");
                            Toast.makeText(ForgetPWActivity.this, msg, Toast.LENGTH_SHORT).show();

                            if (code == 0) {
                                finish();
                            } else {
                                tv_queren.setBackgroundResource(R.drawable.save_btn_blue);
                                tv_queren.setClickable(true);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(ForgetPWActivity.this, "网络异常!", Toast.LENGTH_SHORT).show();

                        tv_queren.setBackgroundResource(R.drawable.save_btn_blue);
                        tv_queren.setClickable(true);
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

        //发送验证码
        RxViewAction.clickNoDouble(tv_yanzhengma).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (dt_phone.getText().toString().length() != 11) {
                    Toast.makeText(ForgetPWActivity.this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mHandler.sendEmptyMessage(0);

                RequestParams params = new RequestParams(RequestUrls.getMsgCode());
                params.setConnectTimeout(5000);
                params.addBodyParameter("phone", dt_phone.getText().toString());
                params.addBodyParameter("smsType", "2");
                Log.e(TAG, "boomer call:  params.toString() = " + params.toString());
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            Log.e(TAG, "boomer onSuccess: result = " + result);
                            JSONObject object = new JSONObject(result);
                            String msg = object.getString("msg");
                            int code = object.getInt("code");
                            Toast.makeText(ForgetPWActivity.this, msg, Toast.LENGTH_SHORT).show();
                            if (code != 0) {
                                errorSengMsg();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(ForgetPWActivity.this, "发送失败,请重试!", Toast.LENGTH_SHORT).show();

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


        tv_queren.setClickable(false);
    }

    private void errorSengMsg() {
        mHandler.removeMessages(0);
        tv_yanzhengma.setText("重新发送");
        tv_yanzhengma.setClickable(true);
        tv_yanzhengma.setBackgroundResource(R.drawable.blue_coner);
        numCount = 60;
    }


    /**
     * 修改登录按钮显示状态
     */
    private void changeLoginBtnState() {
        tv_queren.setLinksClickable(canLogin_phone && canLogin_yanzhengma && canLogin_password && canLogin_password_second && canLogin_tiaokuan);
        if (canLogin_phone && canLogin_yanzhengma && canLogin_password && canLogin_password_second && canLogin_tiaokuan) {
            tv_queren.setBackgroundResource(R.drawable.save_btn_blue);
            tv_queren.setClickable(true);
        } else {
            tv_queren.setBackgroundResource(R.drawable.save_btn_gray1);
            tv_queren.setClickable(false);
        }
    }


    private void showTiaoKuanDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_web, null);
        WebView dialog_web = (WebView) dialogView.findViewById(R.id.dialog_web);
        dialog_web.loadUrl("http://47.75.47.121:8080/mnc/serviceInfo.html");
        dialog.setTitle("服务条款");
        dialog.setView(dialogView);
        dialog.show();
    }
}
