package com.dongdutec.ddmnc.ui.home.multitype;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.cell.MNCTransparentDialog;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.eventbus.MyStarToRefresh;
import com.dongdutec.ddmnc.http.RequestUrls;
import com.dongdutec.ddmnc.ui.home.activity.GaoDeMapActivity;
import com.dongdutec.ddmnc.ui.home.multitype.model.HotStore;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;
import com.dongdutec.ddmnc.web.WebsActivity;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashSet;
import java.util.Set;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

import static android.content.Context.MODE_PRIVATE;


public class HomeItemViewProvider extends ItemViewProvider<HotStore, HomeItemViewProvider.ViewHolder> {
    private Context context;
    private String starState;
    private MNCTransparentDialog mncTransDialog;
    private SharedPreferences sf;

    public HomeItemViewProvider(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.main_homelist, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final HotStore hotStore) {

        starState = hotStore.getStarState();

        RoundedCorners roundedCorners = new RoundedCorners(10);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(context).load(hotStore.getImageUrl())
                .apply(options)
                .into(holder.main_listimg);

        holder.main_storename.setText(hotStore.getStoreName());
        holder.tv_location.setText(hotStore.getLocationStr());
        holder.tv_count.setText("记账人数：" + hotStore.getCount());
        holder.tv_distance.setText("距当前：" + hotStore.getDistance() + "Km");
        if (hotStore.isFirst()) {
            holder.line.setVisibility(View.INVISIBLE);
        }
        //数据条目点击
        RxViewAction.clickNoDouble(holder.rl_views).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(context, WebsActivity.class);
                intent.putExtra("webUrl", "http://47.75.47.121:8080/mnc/storeDetail.html?shopId=" + hotStore.getStoreId() + "&token=" + new DbConfig(context).getToken());
                Log.e("biner", "call: " + "http://47.75.47.121:8080/mnc/storeDetail.html?shopId=" + hotStore.getStoreId() + "&token=" + new DbConfig(context).getToken());

                sf = context.getSharedPreferences("data", MODE_PRIVATE);
                //test存储
                SharedPreferences.Editor editor = sf.edit();
                Set<String> hashSet = sf.getStringSet("historySet", new HashSet<String>());
                if (!hashSet.contains(hotStore.getStoreName())) {
                    hashSet.add(hotStore.getStoreName());
                }
                editor.putStringSet("historySet", hashSet);
                editor.apply();

                context.startActivity(intent);
            }
        });
        if ("1".equals(starState)) {//已收藏
            holder.img_star.setVisibility(View.VISIBLE);
            holder.img_star.setImageResource(R.mipmap.star_check);
        } else if ("2".equals(starState)) {//未收藏
            holder.img_star.setVisibility(View.VISIBLE);
            holder.img_star.setImageResource(R.mipmap.star_);
        } else {
            holder.img_star.setVisibility(View.GONE);
        }

        //取消收藏和收藏
        RxViewAction.clickNoDouble(holder.img_star).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showStarDialog(hotStore, holder);
            }
        });
        //定位地图
        RxViewAction.clickNoDouble(holder.tv_location).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(context, GaoDeMapActivity.class);
                intent.putExtra("lattitude", hotStore.getLantitude());
                intent.putExtra("longitude", hotStore.getLongitude());
                intent.putExtra("storeName",hotStore.getStoreName());
                context.startActivity(intent);
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private ImageView main_listimg;
        @NonNull
        private TextView main_storename;
        @NonNull
        private TextView tv_location;
        @NonNull
        private TextView tv_count;
        @NonNull
        private TextView tv_distance;
        @NonNull
        private View line;
        @NonNull
        private RelativeLayout rl_views;
        @NonNull
        private ImageView img_star;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.main_listimg = (ImageView) itemView.findViewById(R.id.main_listimg);
            this.main_storename = (TextView) itemView.findViewById(R.id.main_storename);
            this.tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            this.tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            this.tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
            this.line = (View) itemView.findViewById(R.id.line);
            this.rl_views = (RelativeLayout) itemView.findViewById(R.id.rl_views);
            this.img_star = (ImageView) itemView.findViewById(R.id.img_star);
        }
    }


    private void showStarDialog(final HotStore hotStore, final ViewHolder holder) {
        mncTransDialog = new MNCTransparentDialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_querenjizhang, null, false);
        final EditText et_mp = (EditText) dialogView.findViewById(R.id.et_mp);
        TextView message_text = (TextView) dialogView.findViewById(R.id.message_text);
        TextView tv_quxiao = (TextView) dialogView.findViewById(R.id.tv_left);
        final TextView tv_queren = (TextView) dialogView.findViewById(R.id.tv_right);
        if ("1".equals(starState)) {
            message_text.setText("确认取消收藏该店铺吗?");
        } else {
            message_text.setText("确认收藏该店铺吗?");
        }
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
                tv_queren.setClickable(false);
                RequestParams params = new RequestParams(RequestUrls.changeStarState());
                params.setConnectTimeout(5000);
                params.addBodyParameter("id", hotStore.getStoreId() + "");
                params.addBodyParameter("token", new DbConfig(context).getToken());
                params.addBodyParameter("state", "1".equals(starState) ? "2" : "1");
                Log.e("HomeItemViewProvider", "call: params.toString() = " + params.toString());
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("HomeItemViewProvider", "onSuccess: result = " + result);

                        if ("取消成功".equals(result) || "收藏成功".equals(result)) {
                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                            mncTransDialog.dismiss();

                            //通知Fragment刷新
                            EventBus.getDefault().post(new MyStarToRefresh());
                            if ("1".equals(starState)) {
                                starState = "2";
                                holder.img_star.setImageResource(R.mipmap.star_);
                            } else {
                                starState = "1";
                                holder.img_star.setImageResource(R.mipmap.star_);
                            }
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
}