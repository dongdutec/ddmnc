package com.dongdutec.ddmnc.base;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.cell.MNCTransparentDialog;
import com.dongdutec.ddmnc.cell.loading.LoadingDialog;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.db.model.User;
import com.dongdutec.ddmnc.http.RequestUrls;
import com.dongdutec.ddmnc.ui.login.activity.LoginActivity;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class BaseActivity extends AppCompatActivity {
    private BaseApplication application;
    private BaseActivity oContext;
    private LoadingDialog loadingDialog;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    loadingDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FileProvider
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.detectFileUriExposure();
        }
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 23) {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
        //去除ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //app退出控制
        if (application == null) {
            // 得到Application对象
            application = (BaseApplication) getApplication();
        }
        oContext = this;// 把当前的上下文对象赋值给BaseActivity
        addActivity();// 调用添加方法

        //loading
        loadingDialog = new LoadingDialog(this, R.style.myTransparentDialog); //设置AlertDialog背景透明
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);

    }

    protected void init() {

    }

    protected void bindData() {

    }

    protected void initData() {

    }

    protected void bindView() {

    }

    protected void initView() {

    }

    public void showLoadings() {

        showLog("showLoadingsAc");
        try{
            loadingDialog.show();
        }catch (Exception e){
            showLog(e.toString());
        }
    }

    public void hideLoadings() {

        showLog("hideLoadingsAc");
        try {
            loadingDialog.dismiss();
        }catch (Exception e){
            showLog(e.toString());
        }
    }

    public void hideLoadingsDelayed(long time) {

        showLog("hideLoadingsDelayedAc");
        mHandler.sendEmptyMessageDelayed(0, time);
    }

    protected void judgeToken() {
        RequestParams params = new RequestParams(RequestUrls.getJudgeToken());
        params.setConnectTimeout(5000);
        showLoadings();
        params.addBodyParameter("token", new DbConfig(this).getToken());
        Log.e("judgeToken", "judgeToken:  params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("judgeToken", "onSuccess: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int state = jsonObject.getInt("state");
                    if (state == 0) {
                        onJudgeResult();

                    } else {
                        //退出登录
                        showTokenDownDialog();
                    }


                } catch (JSONException e) {
                    Toast.makeText(BaseActivity.this, "系统异常!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(BaseActivity.this, "网络异常!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void showTokenDownDialog() {
        final MNCTransparentDialog mncTransDialog = new MNCTransparentDialog(BaseActivity.this);
        View dialogView = LayoutInflater.from(BaseActivity.this).inflate(R.layout.dialog_tokendown, null, false);
        TextView message_text = (TextView) dialogView.findViewById(R.id.message_text);
        message_text.setText(R.string.tokendown);
        final TextView tv_queren = (TextView) dialogView.findViewById(R.id.tv_right);
        //确认
        RxViewAction.clickNoDouble(tv_queren).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mncTransDialog.dismiss();
            }
        });
        //退出登录
        mncTransDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                DbConfig dbConfig = new DbConfig(BaseActivity.this);
                User user = dbConfig.getUser();
                if (user != null) {
                    try {
                        user.setIsLogin("0");
                        dbConfig.getDbManager().saveOrUpdate(user);
                        startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                    } catch (DbException e) {
                        e.printStackTrace();
                        startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                    }
                }
            }
        });
        mncTransDialog.show();
        Window window = mncTransDialog.getWindow();//对话框窗口
        window.setGravity(Gravity.CENTER);//设置对话框显示在屏幕中间
        window.setWindowAnimations(R.style.dialog_style);//添加动画
        window.setContentView(dialogView);

    }

    /**
     * token认证回调
     */
    protected void onJudgeResult() {

    }

    /**
     * 透明状态栏,透明导航栏
     * 使用了SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION,表示会让应用的主体内容占用
     * 系统导航栏的空间
     * 然后又调用了setNavigationBarColor()方法将导航栏设置成透明色
     */
    public void setStatus(int colorRes) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        } else {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏字体深色
            window.setStatusBarColor(getResources().getColor(colorRes));
        }
    }


    // 添加Activity方法
    public void addActivity() {
        application.addActivity_(oContext);// 调用BaseApplication的添加Activity方法
    }

    //销毁当个Activity方法
    public void removeActivity() {
        application.removeActivity_(oContext);// 调用BaseApplication的销毁单个Activity方法
    }

    //销毁所有Activity方法
    public void removeALLActivity() {
        application.removeALLActivity_();// 调用BaseApplication的销毁所有Activity方法
    }

    /* 把Toast定义成一个方法*/
    public void showToast(String text) {
        Toast.makeText(oContext, text, Toast.LENGTH_SHORT).show();
    }

    /* 把Log定义成一个方法*/
    public void showLog(String text) {
        Log.e("MNC", "show_Log: " + text);
    }


    public void showMessageDialog(String msg) {
        final MNCTransparentDialog mncTransDialog = new MNCTransparentDialog(BaseActivity.this);
        View dialogView = LayoutInflater.from(BaseActivity.this).inflate(R.layout.dialog_tokendown, null, false);
        TextView message_text = (TextView) dialogView.findViewById(R.id.message_text);
        message_text.setText(msg);
        final TextView tv_queren = (TextView) dialogView.findViewById(R.id.tv_right);
        //确认
        RxViewAction.clickNoDouble(tv_queren).subscribe(new Action1<Void>() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
    }
}
