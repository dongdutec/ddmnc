package com.dongdutec.ddmnc.base;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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

public class BaseFragment extends Fragment {

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //loading
        loadingDialog = new LoadingDialog(getActivity(), R.style.myTransparentDialog); //设置AlertDialog背景透明
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

        showLog("showLoadingsFr");
        try{
            loadingDialog.show();
        }catch (Exception e){
            showLog(e.toString());
        }
    }

    public void hideLoadings() {

        showLog("hideLoadingsFr");
        try {
            loadingDialog.dismiss();
        }catch (Exception e){
            showLog(e.toString());
        }
    }

    public void hideLoadingsDelayed(long time) {

        showLog("hideLoadingsDelayedFr");
        mHandler.sendEmptyMessageDelayed(0, time);
    }

    protected void judgeToken() {
        RequestParams params = new RequestParams(RequestUrls.getJudgeToken());
        params.setConnectTimeout(5000);
        showLoadings();
        params.addBodyParameter("token", new DbConfig(getActivity()).getToken());
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
                    Toast.makeText(getActivity(), "系统异常!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(), "网络异常!", Toast.LENGTH_SHORT).show();
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
        final MNCTransparentDialog mncTransDialog = new MNCTransparentDialog(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_tokendown, null, false);
        TextView message_text = (TextView) dialogView.findViewById(R.id.message_text);
        message_text.setText(R.string.tokendown);
        final TextView tv_queren = (TextView) dialogView.findViewById(R.id.tv_right);
        final TextView tv_left = (TextView) dialogView.findViewById(R.id.tv_left);
        //确认
        RxViewAction.clickNoDouble(tv_queren).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mncTransDialog.dismiss();
            }
        });
        //取消
        RxViewAction.clickNoDouble(tv_left).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mncTransDialog.dismiss();
            }
        });
        //退出登录
        mncTransDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                DbConfig dbConfig = new DbConfig(getActivity());
                User user = dbConfig.getUser();
                if (user != null) {
                    try {
                        user.setIsLogin("0");
                        dbConfig.getDbManager().saveOrUpdate(user);
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    } catch (DbException e) {
                        e.printStackTrace();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
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


    /* 把Toast定义成一个方法*/
    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    /* 把Log定义成一个方法*/
    public void showLog(String text) {
        Log.e("MNC", "show_Log: " + text);
    }


    public void showMessageDialog(String msg) {
        final MNCTransparentDialog mncTransDialog = new MNCTransparentDialog(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_tokendown, null, false);
        TextView message_text = (TextView) dialogView.findViewById(R.id.message_text);
        message_text.setText(msg);
        final TextView tv_queren = (TextView) dialogView.findViewById(R.id.tv_right);
        final TextView tv_left = (TextView) dialogView.findViewById(R.id.tv_left);
        //确认
        RxViewAction.clickNoDouble(tv_queren).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mncTransDialog.dismiss();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
    }
}