package com.dongdutec.ddmnc.ui.login.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import rx.functions.Action1;

public class RegisterActivity extends BaseActivity {
    private ImageView back;
    private TextView bar_title;
    private View v_line;

    private EditText dt_phone;
    private ImageView img_cha;
    private EditText dt_password;
    private CheckBox ck_yanjing;
    private EditText dt_tp_tp_yanzhengma;
    private EditText dt_yanzhengma;
    private TextView tv_yanzhengma;
    private EditText dt_tuijianren;
    private ImageView img_cha_tuijainren;
    private TextView tv_register;
    private CheckBox ck_tiaokuan;

    private boolean canLogin_phone = false;
    private boolean canLogin_password = false;
    private boolean canLogin_tpyanzhengma = false;
    private boolean canLogin_yanzhengma = false;
    private boolean canLogin_tuijianren = false;
    private boolean canLogin_tiaokuan = false;

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
        setContentView(R.layout.activity_register);

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
        dt_password = findViewById(R.id.dt_password);
        ck_yanjing = findViewById(R.id.ck_yanjing);
        dt_tp_tp_yanzhengma = findViewById(R.id.dt_tp_tp_yanzhengma);
        dt_yanzhengma = findViewById(R.id.dt_yanzhengma);
        tv_yanzhengma = findViewById(R.id.tv_yanzhengma);
        dt_tuijianren = findViewById(R.id.dt_tuijianren);
        img_cha_tuijainren = findViewById(R.id.img_cha_tuijainren);
        tv_register = findViewById(R.id.tv_register);
        ck_tiaokuan = findViewById(R.id.ck_tiaokuan);

        back.setImageResource(R.mipmap.back_login);
        bar_title.setText("注册");
        bar_title.setTextColor(getResources().getColor(R.color.theme_primary));
        v_line.setVisibility(View.INVISIBLE);

        ck_yanjing.setVisibility(View.GONE);
        img_cha.setVisibility(View.GONE);
        img_cha_tuijainren.setVisibility(View.GONE);
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
        //图片验证码输入监听
        dt_tp_tp_yanzhengma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    canLogin_tpyanzhengma = true;
                } else {
                    canLogin_tpyanzhengma = false;
                }
                changeLoginBtnState();//修改登录按钮状态
            }

            @Override
            public void afterTextChanged(Editable editable) {

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

        //输入推荐人监听
        dt_tuijianren.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    canLogin_tuijianren = true;
                    img_cha_tuijainren.setVisibility(View.VISIBLE);
                } else {
                    canLogin_tuijianren = false;
                    img_cha_tuijainren.setVisibility(View.GONE);
                }
                changeLoginBtnState();//修改登录按钮状态
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //推荐人手机号 × clear
        RxViewAction.clickNoDouble(img_cha_tuijainren).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dt_tuijianren.setText("");
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
        //注册
        RxViewAction.clickNoDouble(tv_register).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //判断输入规范
                if (dt_phone.getText().toString().length() != 11) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dt_password.getText().toString().length() < 6) {
                    Toast.makeText(RegisterActivity.this, "密码长度至少为6位!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dt_tp_tp_yanzhengma.getText().toString().length() < 4) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的验证码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*if () { //TODO 校检图形验证码

                }*/

                if (dt_yanzhengma.getText().toString().length() < 6) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的短信验证码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*if () { //TODO 校检短信验证码 The Last!  post

                }*/

                /*if (dt_tuijianren.getText().toString().length() != 11) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的推荐人手机号!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (){//TODO 校检推荐人手机号   post

                }*/

                //判断通过
                //post
                Toast.makeText(RegisterActivity.this, "测试注册!", Toast.LENGTH_SHORT).show();

            }
        });

        //发送验证码
        RxViewAction.clickNoDouble(tv_yanzhengma).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (dt_phone.getText().toString().length() != 11) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dt_tp_tp_yanzhengma.getText().toString().length() < 4) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的验证码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*if () { //TODO 校检图形验证码

                }*/


                mHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 修改登录按钮显示状态
     */
    private void changeLoginBtnState() {
        tv_register.setLinksClickable(canLogin_phone && canLogin_password && canLogin_tpyanzhengma && canLogin_yanzhengma && canLogin_tuijianren && canLogin_tiaokuan);
        if (canLogin_phone && canLogin_password && canLogin_tpyanzhengma && canLogin_yanzhengma && canLogin_tuijianren && canLogin_tiaokuan) {
            tv_register.setBackgroundResource(R.drawable.save_btn_blue);
        } else {
            tv_register.setBackgroundResource(R.drawable.save_btn_gray1);
        }
    }
}
