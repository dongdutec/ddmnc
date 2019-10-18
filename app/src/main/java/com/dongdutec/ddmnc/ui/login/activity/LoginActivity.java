package com.dongdutec.ddmnc.ui.login.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dongdutec.ddmnc.MainActivity;
import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.cell.MNCTransparentDialog;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.db.model.User;
import com.dongdutec.ddmnc.http.HtmlUrls;
import com.dongdutec.ddmnc.http.RequestUrls;
import com.dongdutec.ddmnc.utils.piccode.CodeUtils;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class LoginActivity extends BaseActivity {
    private ImageView back;
    private FrameLayout fl_invite;
    private EditText dt_invite;
    private ImageView img_cha_invite;
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
    private ImageView img_tp_yanzhengma;

    private boolean canLogin_phone = false;
    private boolean canLogin_password = false;
    private boolean canLogin_yanzhengma = false;
    private boolean canLogin_tiaokuan = false;
    private boolean canLogin_invite = true;
    private String TAG = LoginActivity.class.getSimpleName();

    private CodeUtils codeUtils;
    private long exitTime = 0;
    private String isMan = "";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
    private String phone;

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
        fl_invite = findViewById(R.id.fl_invite);
        dt_invite = findViewById(R.id.dt_invite);
        img_cha_invite = findViewById(R.id.img_cha_invite);
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
        img_tp_yanzhengma = findViewById(R.id.img_tp_yanzhengma);

        back.setVisibility(View.GONE);
        bar_title.setText("登录");
        bar_title.setTextColor(getResources().getColor(R.color.theme_primary));
        v_line.setVisibility(View.INVISIBLE);

        ck_yanjing.setVisibility(View.GONE);
        img_cha.setVisibility(View.GONE);
        img_cha_invite.setVisibility(View.GONE);


        getCode();
    }

    @Override
    protected void init() {

    }

    //获取验证码
    private void getCode() {
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        img_tp_yanzhengma.setImageBitmap(bitmap);
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
        //图片验证码
        RxViewAction.clickNoDouble(img_tp_yanzhengma).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                getCode();
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
                clearPrivate();
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
        //输入推荐人手机号监听
        dt_invite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    canLogin_invite = true;
                    img_cha.setVisibility(View.VISIBLE);
                } else {
                    canLogin_invite = false;
                    img_cha_invite.setVisibility(View.GONE);
                }
                changeLoginBtnState();//修改登录按钮状态
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //推荐人手机号 × clear
        RxViewAction.clickNoDouble(img_cha_invite).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dt_invite.setText("");
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
                    Toast.makeText(LoginActivity.this, "请输入正确的图片验证码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = codeUtils.getCode();
                code = code.toUpperCase();
                String etStr = dt_tp_yanzhengma.getText().toString();
                etStr = etStr.toUpperCase();
                Log.e(TAG, "call: etStr = " + etStr + " code = " + code);
                if (!etStr.equals(code)) {
                    Toast.makeText(LoginActivity.this, "请输入正确的图片验证码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((!canLogin_invite) && (dt_invite.getText().toString().length() != 11)) {
                    Toast.makeText(LoginActivity.this, "请输入正确的邀请人手机号!", Toast.LENGTH_SHORT).show();
                    return;
                }


                tv_login.setBackgroundResource(R.drawable.save_btn_gray1);
                tv_login.setClickable(false);

                //判断通过
                //post
                RequestParams params = new RequestParams(RequestUrls.userLogin());
                phone = dt_phone.getText().toString();
                params.addBodyParameter("phone", phone);
                params.addBodyParameter("password", dt_password.getText().toString());
                params.addBodyParameter("person", dt_invite.getText().toString());
                params.setConnectTimeout(5000);
                showLoadings();
                Log.e(TAG, "call: " + params.toString());
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "onSuccess:  result = " + result);
                        try {
                            JSONObject object = new JSONObject(result);
                            String msg = object.getString("msg");
                            int code = object.getInt("code");
                            if (code == 0) {
                                JSONObject data = object.getJSONObject("data");
                                String isMan = data.getString("isMan");//有无推荐人
                                if ("y".equals(isMan)) {
                                    canLogin_invite = true;
                                    fl_invite.setVisibility(View.GONE);
                                    //存储用户信息到本地数据库
                                    saveUserToDb(data);
                                    mHandler.sendEmptyMessageDelayed(0, 1000);
                                } else {
                                    hideLoadings();
                                    Toast.makeText(LoginActivity.this, "请补充推荐人信息!", Toast.LENGTH_SHORT).show();
                                    canLogin_invite = false;
                                    fl_invite.setVisibility(View.VISIBLE);
                                }

                            } else {
                                hideLoadings();
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {
                            hideLoadings();
                            Toast.makeText(LoginActivity.this, "系统异常!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        hideLoadings();
                        Toast.makeText(LoginActivity.this, "网络异常!", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        changeLoginBtnState();
                    }
                });

            }
        });


        tv_login.setClickable(false);
    }

    private void clearPrivate() {
        fl_invite.setVisibility(View.GONE);
        dt_invite.setText("");
        canLogin_invite = true;
    }

    private void showTiaoKuanDialog() {
        final MNCTransparentDialog mncTransDialog = new MNCTransparentDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_web_tiaokuan, null, false);
        final TextView tv_queren = (TextView) dialogView.findViewById(R.id.tv_right);
        final WebView web = (WebView) dialogView.findViewById(R.id.web);
        RxViewAction.clickNoDouble(tv_queren).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mncTransDialog.dismiss();
            }
        });
        web.loadUrl(HtmlUrls.getServiceInfo());
        mncTransDialog.show();
        Window window = mncTransDialog.getWindow();//对话框窗口
        window.setGravity(Gravity.CENTER);//设置对话框显示在屏幕中间
        window.setWindowAnimations(R.style.dialog_style);//添加动画
        window.setContentView(dialogView);


    }

    //保存用户到数据库
    private void saveUserToDb(JSONObject data) {

        User user = new User();
        try {
            user.setToken(data.getString("token"));
            user.setLeval(data.getString("act"));
            user.setPhone(phone);
            user.setIsLogin("1");
            DbConfig dbConfig = new DbConfig(getApplicationContext());
            DbManager db = dbConfig.getDbManager();


            db.saveOrUpdate(user);
        } catch (JSONException e) {
        } catch (DbException e) {

        }
    }


    /**
     * 修改登录按钮显示状态
     */
    private void changeLoginBtnState() {
        tv_login.setLinksClickable(canLogin_phone && canLogin_password && canLogin_yanzhengma && canLogin_tiaokuan && canLogin_invite);
        if (canLogin_phone && canLogin_password && canLogin_yanzhengma && canLogin_tiaokuan && canLogin_invite) {
            tv_login.setBackgroundResource(R.drawable.save_btn_blue);
            tv_login.setClickable(true);
        } else {
            tv_login.setBackgroundResource(R.drawable.save_btn_gray1);
            tv_login.setClickable(false);
        }
    }

    @Override
    public void onBackPressed() {
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
}
