package com.dongdutec.ddmnc.ui.login.activity;

import android.content.Intent;
import android.os.Bundle;
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

import com.dongdutec.ddmnc.MainActivity;
import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.db.model.User;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import rx.functions.Action1;

public class LoginActivity extends BaseActivity {
    private ImageView back;
    private TextView bar_title;
    private View v_line;
    private TextView _tv_goregister;
    private TextView _tv_forgetpw;
    private EditText dt_phone;
    private ImageView img_cha;
    private EditText dt_password;
    private CheckBox ck_yanjing;
    private EditText dt_tp_yanzhengma;
    private TextView tv_login;
    private CheckBox ck_tiaokuan;
    private TextView tv_tiaokuan;

    private boolean canLogin_phone = false;
    private boolean canLogin_password = false;
    private boolean canLogin_yanzhengma = false;
    private boolean canLogin_tiaokuan = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        init();
        bindView();
    }

    @Override
    protected void initView() {
        back = findViewById(R.id.bar_left_img);
        bar_title = findViewById(R.id.bar_title_text);
        v_line = findViewById(R.id.v_line);
        _tv_goregister = findViewById(R.id._tv_goregister);
        _tv_forgetpw = findViewById(R.id._tv_forgetpw);
        dt_phone = findViewById(R.id.dt_phone);
        img_cha = findViewById(R.id.img_cha);
        dt_password = findViewById(R.id.dt_password);
        ck_yanjing = findViewById(R.id.ck_yanjing);
        dt_tp_yanzhengma = findViewById(R.id.dt_tp_yanzhengma);
        tv_login = findViewById(R.id.tv_login);
        ck_tiaokuan = findViewById(R.id.ck_tiaokuan);
        tv_tiaokuan = findViewById(R.id.tv_tiaokuan);

        back.setImageResource(R.mipmap.back_login);
        bar_title.setText("登录");
        bar_title.setTextColor(getResources().getColor(R.color.theme_primary));
        v_line.setVisibility(View.INVISIBLE);

        ck_yanjing.setVisibility(View.GONE);
        img_cha.setVisibility(View.GONE);
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
        //立即注册
        RxViewAction.clickNoDouble(_tv_goregister).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        //忘记密码
        RxViewAction.clickNoDouble(_tv_forgetpw).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(LoginActivity.this, ForgetPWActivity.class);
                startActivity(intent);
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
        //验证码输入监听
        dt_tp_yanzhengma.addTextChangedListener(new TextWatcher() {
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
        //登录
        RxViewAction.clickNoDouble(tv_login).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //判断输入规范
                if (dt_phone.getText().toString().length() != 11) {
                    Toast.makeText(LoginActivity.this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dt_password.getText().toString().length() < 6) {
                    Toast.makeText(LoginActivity.this, "密码长度至少为6位!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dt_tp_yanzhengma.getText().toString().length() < 4) {
                    Toast.makeText(LoginActivity.this, "请输入正确的验证码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*if () { //TODO 校检图形验证码

                }*/

                //判断通过
                //post
                Toast.makeText(LoginActivity.this, "测试登录!", Toast.LENGTH_SHORT).show();
                saveUserToDbTest();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

            }
        });
    }

    //保存用户到数据库
    private void saveUserToDbTest() {

        User user = new User();
        try {
            user.setId(1);
            user.setToken("GYGS8UI78828237878DJKAJ");
            user.setUpdateTime("2019-09-23");
            user.setPhone("12345678910");
            user.setHeadimgurl("");
            user.setIsstore("1");
            user.setIsLogin("1");
            DbConfig dbConfig = new DbConfig(getApplicationContext());
            DbManager db = dbConfig.getDbManager();


            db.saveOrUpdate(user);
        } catch (DbException e) {

        }
    }



    /**
     * 修改登录按钮显示状态
     */
    private void changeLoginBtnState() {
        tv_login.setLinksClickable(canLogin_phone && canLogin_password && canLogin_yanzhengma && canLogin_tiaokuan);
        if (canLogin_phone && canLogin_password && canLogin_yanzhengma && canLogin_tiaokuan) {
            tv_login.setBackgroundResource(R.drawable.save_btn_blue);
        } else {
            tv_login.setBackgroundResource(R.drawable.save_btn_gray1);
        }
    }
}
