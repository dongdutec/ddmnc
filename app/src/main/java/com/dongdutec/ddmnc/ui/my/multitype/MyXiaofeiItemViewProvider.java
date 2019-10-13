package com.dongdutec.ddmnc.ui.my.multitype;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.cell.MNCTransparentDialog;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.db.model.User;
import com.dongdutec.ddmnc.eventbus.MyXiaofeiToRefresh;
import com.dongdutec.ddmnc.http.RequestUrls;
import com.dongdutec.ddmnc.ui.login.activity.LoginActivity;
import com.dongdutec.ddmnc.ui.my.activity.PingjiaActivity;
import com.dongdutec.ddmnc.ui.my.multitype.model.MyXiaofei;
import com.dongdutec.ddmnc.utils.common.UtilsMNC;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


public class MyXiaofeiItemViewProvider extends ItemViewProvider<MyXiaofei, MyXiaofeiItemViewProvider.ViewHolder> {
    private Context context;
    private MNCTransparentDialog mncTransDialog;

    public MyXiaofeiItemViewProvider(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_myxiaofei, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final MyXiaofei myXiaofei) {
        //去评价
        RxViewAction.clickNoDouble(holder.tv_pingjia).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(context, PingjiaActivity.class);
                intent.putExtra("imageUrl", myXiaofei.getImgUrl());
                intent.putExtra("storeName", myXiaofei.getStoreName());
                intent.putExtra("shopId", myXiaofei.getStoreId());
                intent.putExtra("id", myXiaofei.getOrderId());
                intent.putExtra("state", myXiaofei.getOrderState());
                intent.putExtra("isStore", myXiaofei.getIsStore());
                context.startActivity(intent);

            }
        });
        RoundedCorners roundedCorners = new RoundedCorners(10);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(context).load(myXiaofei.getImgUrl())
                .apply(options)
                .into(holder.img_xiaofei);
        String timestampToStringAll = new UtilsMNC().getTimestampToStringAll(myXiaofei.getTimes());
        holder.tv_time.setText(timestampToStringAll);
        if (myXiaofei.getOrderState() != null && myXiaofei.getOrderState().equals("0")) {//待记账
            holder.tv_state.setText("待记账");
        } else if (myXiaofei.getOrderState() != null && myXiaofei.getOrderState().equals("1")) {//已记账&&null
            holder.tv_state.setText("已记账");
        } else if (myXiaofei.getOrderState() != null && myXiaofei.getOrderState().equals("3")) {//已评价
            holder.tv_state.setText("已评价");
        } else {
            holder.tv_state.setText("已取消");
        }
        holder.tv_title.setText(myXiaofei.getTitle());
        holder.tv_price.setText("￥ " + myXiaofei.getPrice());
        String phone = myXiaofei.getPhone();


        //记账 取消记账 评价 显示控制
        if (myXiaofei.getIsStore() != null && myXiaofei.getIsStore().equals("2")) { //商家
            holder.tv_pingjia.setVisibility(View.GONE);
            if (myXiaofei.getOrderState() != null && myXiaofei.getOrderState().equals("0")) {//待记账
                holder.tv_queren.setVisibility(View.VISIBLE);
                holder.tv_quxaio.setVisibility(View.VISIBLE);
            } else {//已记账&&null
                holder.tv_queren.setVisibility(View.GONE);
                holder.tv_quxaio.setVisibility(View.GONE);
            }

        } else {//用户&&null
            holder.ll_has_jizhang.setVisibility(View.GONE);
            if (myXiaofei.getOrderState() != null && myXiaofei.getOrderState().equals("1")) {//1 记账 2 取消记账 0 待记账
                if ("0".equals(myXiaofei.getAppraise())) {// 0 未评价 1 已评价
                    holder.tv_pingjia.setVisibility(View.VISIBLE);
                    holder.tv_pingjia.setBackgroundResource(R.drawable.blue_stoke);
                    holder.tv_pingjia.setClickable(true);
                } else {
                    holder.tv_pingjia.setVisibility(View.VISIBLE);
                    holder.tv_pingjia.setBackgroundResource(R.drawable.gray_stoke);
                    holder.tv_pingjia.setTextColor(context.getResources().getColor(R.color.gray1));
                    holder.tv_pingjia.setText("已评价");
                    holder.tv_pingjia.setClickable(false);
                }
            } else {//待记账&&取消记账&&null
                holder.tv_pingjia.setVisibility(View.GONE);
            }
        }

        if (phone != null && phone.length() == 11) {
            String phone_Str = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
            holder.tv_phone.setText("来自： " + phone_Str);
        } else {
            holder.tv_phone.setText("来自： " + phone);
        }

        //取消记账
        RxViewAction.clickNoDouble(holder.tv_quxaio).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showNoDialog(myXiaofei);
            }
        });
        //确认记账
        RxViewAction.clickNoDouble(holder.tv_queren).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showYesDialog(myXiaofei);
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private TextView tv_time;
        @NonNull
        private TextView tv_state;
        @NonNull
        private ImageView img_xiaofei;
        @NonNull
        private TextView tv_title;
        @NonNull
        private TextView tv_price;
        @NonNull
        private TextView tv_phone;
        @NonNull
        private TextView tv_quxaio;
        @NonNull
        private TextView tv_queren;
        @NonNull
        private TextView tv_pingjia;
        @NonNull
        private LinearLayout ll_has_jizhang;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            this.tv_state = (TextView) itemView.findViewById(R.id.tv_state);
            this.img_xiaofei = (ImageView) itemView.findViewById(R.id.img_xiaofei);
            this.tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            this.tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            this.tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            this.tv_quxaio = (TextView) itemView.findViewById(R.id.tv_quxaio);
            this.tv_queren = (TextView) itemView.findViewById(R.id.tv_queren);
            this.tv_pingjia = (TextView) itemView.findViewById(R.id.tv_pingjia);
            this.ll_has_jizhang = (LinearLayout) itemView.findViewById(R.id.ll_has_jizhang);
        }
    }

    private void showYesDialog(final MyXiaofei myXiaofei) {
        mncTransDialog = new MNCTransparentDialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_querenjizhang_mp, null, false);
        final EditText et_mp = (EditText) dialogView.findViewById(R.id.et_mp);
        TextView tv_quxiao = (TextView) dialogView.findViewById(R.id.tv_left);
        final TextView tv_queren = (TextView) dialogView.findViewById(R.id.tv_right);
        tv_quxiao.setText("取消");
        tv_queren.setText("确认");
        //取消
        RxViewAction.clickNoDouble(tv_quxiao).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mncTransDialog.dismiss();
            }
        });
        //确认
        RxViewAction.clickNoDouble(tv_queren).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                RequestParams params = new RequestParams(RequestUrls.getJudgeToken());
                params.setConnectTimeout(5000);
                params.addBodyParameter("token", new DbConfig(context).getToken());
                Log.e("judgeToken", "judgeToken:  params.toString() = " + params.toString());
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("judgeToken", "onSuccess: " + result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int state = jsonObject.getInt("state");
                            if (state == 0) {

                                postingyes(et_mp, tv_queren, myXiaofei);

                            } else {
                                //退出登录
                                showTokenDownDialog();
                            }


                        } catch (JSONException e) {
                            Toast.makeText(context, "系统异常!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(context, "网络异常!", Toast.LENGTH_SHORT).show();
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


        mncTransDialog.show();
        Window window = mncTransDialog.getWindow();//对话框窗口
        window.setGravity(Gravity.CENTER);//设置对话框显示在屏幕中间
        window.setWindowAnimations(R.style.dialog_style);//添加动画
        window.setContentView(dialogView);
    }

    private void showTokenDownDialog() {
        final MNCTransparentDialog mncTransDialog = new MNCTransparentDialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_tokendown, null, false);
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
                DbConfig dbConfig = new DbConfig(context);
                User user = dbConfig.getUser();
                if (user != null) {
                    try {
                        user.setIsLogin("0");
                        dbConfig.getDbManager().saveOrUpdate(user);
                        context.startActivity(new Intent(context, LoginActivity.class));
                    } catch (DbException e) {
                        e.printStackTrace();
                        context.startActivity(new Intent(context, LoginActivity.class));
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

    private void postingyes(EditText et_mp, final TextView tv_queren, final MyXiaofei myXiaofei) {
        if (et_mp.getText() == null || et_mp.getText().length() == 0) {
            Toast.makeText(context, "请输入给用户返还的MP数量!", Toast.LENGTH_SHORT).show();
            return;
        }
        tv_queren.setClickable(false);
        RequestParams params = new RequestParams(RequestUrls.storeGetJifen());
        params.setConnectTimeout(5000);
//        ((BaseActivity)context).showLoadings();
        params.addBodyParameter("id", myXiaofei.getOrderId());
        params.addBodyParameter("mp", et_mp.getText().toString());
        params.addBodyParameter("token", new DbConfig(context).getToken());
        params.addBodyParameter("state", "1");//1 记账 2 取消记账 3 评价 0 待记账
        Log.e("Myxiaofei", "call: params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("Myxiaofei", "onSuccess: result = " + result);


                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int state = jsonObject.getInt("state");
                    if (state == 0) {
                        Toast.makeText(context, "记账返还成功!", Toast.LENGTH_SHORT).show();
                        mncTransDialog.dismiss();

                        //通知Fragment刷新
                        EventBus.getDefault().post(new MyXiaofeiToRefresh(myXiaofei.getOrderState(), myXiaofei.getIsStore()));
                    } else {
                        Toast.makeText(context, "系统错误!", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(context, "网络异常!", Toast.LENGTH_SHORT).show();
                tv_queren.setClickable(true);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
//                ((BaseActivity)context).hideLoadings();
            }
        });
    }

    private void showNoDialog(final MyXiaofei myXiaofei) {
        mncTransDialog = new MNCTransparentDialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_querenjizhang, null);
        TextView message_text = (TextView) dialogView.findViewById(R.id.message_text);
        TextView tv_quxiao = (TextView) dialogView.findViewById(R.id.tv_left);
        final TextView tv_queren = (TextView) dialogView.findViewById(R.id.tv_right);
        tv_quxiao.setText("取消");
        tv_queren.setText("确认");
        message_text.setText("取消记账？");
        //取消
        RxViewAction.clickNoDouble(tv_quxiao).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mncTransDialog.dismiss();
            }
        });
        //确认
        RxViewAction.clickNoDouble(tv_queren).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                RequestParams params = new RequestParams(RequestUrls.getJudgeToken());
                params.setConnectTimeout(5000);
                ((BaseActivity)context).showLoadings();
                params.addBodyParameter("token", new DbConfig(context).getToken());
                Log.e("judgeToken", "judgeToken:  params.toString() = " + params.toString());
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("judgeToken", "onSuccess: " + result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int state = jsonObject.getInt("state");
                            if (state == 0) {


                                postingno(tv_queren, myXiaofei);

                            } else {
                                //退出登录
                                showTokenDownDialog();
                            }


                        } catch (JSONException e) {
                            Toast.makeText(context, "系统异常!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(context, "网络异常!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        ((BaseActivity)context).hideLoadings();
                    }
                });
            }
        });


        mncTransDialog.show();
        Window window = mncTransDialog.getWindow();//对话框窗口
        window.setGravity(Gravity.CENTER);//设置对话框显示在屏幕中间
        window.setWindowAnimations(R.style.dialog_style);//添加动画
        window.setContentView(dialogView);
    }

    private void postingno(final TextView tv_queren, final MyXiaofei myXiaofei) {
        tv_queren.setClickable(false);
        RequestParams params = new RequestParams(RequestUrls.storeGetJifen());
        params.setConnectTimeout(5000);
        ((BaseActivity)context).showLoadings();
        params.addBodyParameter("id", myXiaofei.getOrderId());
        params.addBodyParameter("mp", "");
        params.addBodyParameter("state", "2");//1 记账 2 取消记账 3 评价 0 待记账
        Log.e("Myxiaofei", "call: params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("Myxiaofei", "onSuccess: result = " + result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int state = jsonObject.getInt("state");
                    if (state == 0) {
                        Toast.makeText(context, "取消成功!", Toast.LENGTH_SHORT).show();
                        mncTransDialog.dismiss();

                        //通知Fragment刷新
                        EventBus.getDefault().post(new MyXiaofeiToRefresh(myXiaofei.getOrderState(), myXiaofei.getIsStore()));
                    } else {
                        Toast.makeText(context, "系统错误!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(context, "网络异常!", Toast.LENGTH_SHORT).show();
                tv_queren.setClickable(true);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                ((BaseActivity)context).hideLoadings();
            }
        });
    }

}