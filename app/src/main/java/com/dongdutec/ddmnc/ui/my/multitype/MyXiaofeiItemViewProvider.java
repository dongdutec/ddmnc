package com.dongdutec.ddmnc.ui.my.multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.ui.my.multitype.model.MyXiaofei;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


public class MyXiaofeiItemViewProvider extends ItemViewProvider<MyXiaofei, MyXiaofeiItemViewProvider.ViewHolder> {
    private Context context;

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
//        Glide.with(context).load(myXiaofei.getImgUrl()).into(holder.img_xiaofei);
        holder.tv_time.setText(myXiaofei.getTimes());
        holder.tv_state.setText(myXiaofei.getOrderState());
        holder.tv_title.setText(myXiaofei.getTitle());
        holder.tv_price.setText("￥ " + myXiaofei.getPrice());
        holder.tv_phone.setText("来自： " + myXiaofei.getPhone());

        //取消记账
        RxViewAction.clickNoDouble(holder.tv_quxaio).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showQuxiaoDialog(myXiaofei);
            }
        });
        //确认记账
        RxViewAction.clickNoDouble(holder.tv_queren).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showQuerenDialog(myXiaofei);
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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            this.tv_state = (TextView) itemView.findViewById(R.id.tv_state);
            this.img_xiaofei = (ImageView) itemView.findViewById(R.id.img_head);
            this.tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            this.tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            this.tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            this.tv_quxaio = (TextView) itemView.findViewById(R.id.tv_quxaio);
            this.tv_queren = (TextView) itemView.findViewById(R.id.tv_queren);
        }
    }


    private void showQuxiaoDialog(final MyXiaofei myXiaofei) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_querenjizhang, null);
        TextView message_text = (TextView) dialogView.findViewById(R.id.message_text);
        TextView tv_quxiao = (TextView) dialogView.findViewById(R.id.tv_left);
        TextView tv_queren = (TextView) dialogView.findViewById(R.id.tv_right);
        tv_quxiao.setText("取消");
        tv_queren.setText("确认");
        message_text.setText("取消记账？");
        //取消
        RxViewAction.clickNoDouble(tv_quxiao).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dialog.dismiss();
            }
        });
        //确认
        RxViewAction.clickNoDouble(tv_queren).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Toast.makeText(context, "取消成功!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.setView(dialogView);

        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.theme_primary));

    }

    private void showQuerenDialog(final MyXiaofei myXiaofei) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_querenjizhang, null);
        TextView message_text = (TextView) dialogView.findViewById(R.id.message_text);
        TextView tv_quxiao = (TextView) dialogView.findViewById(R.id.tv_left);
        TextView tv_queren = (TextView) dialogView.findViewById(R.id.tv_right);
        tv_quxiao.setText("取消");
        tv_queren.setText("确认");
        message_text.setText("确认记账？");
        //取消
        RxViewAction.clickNoDouble(tv_quxiao).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dialog.dismiss();
            }
        });
        //确认
        RxViewAction.clickNoDouble(tv_queren).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Toast.makeText(context, "记账成功!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.setView(dialogView);

        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.theme_primary));

    }
}