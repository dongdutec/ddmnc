package com.dongdutec.ddmnc.ui.home.multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.ui.home.multitype.model.HotStore;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


public class HomeItemViewProvider extends ItemViewProvider<HotStore, HomeItemViewProvider.ViewHolder> {
    private Context context;

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
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final HotStore hotStore) {

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
                /*Intent intent = new Intent(context, BaseWebActivity.class);
                context.startActivity(intent);*/
            }
        });
        if (hotStore.getStarState() == 0) {//不显示
            holder.img_star.setVisibility(View.GONE);
        }
        if (hotStore.getStarState() == 1) {//已收藏
            holder.img_star.setVisibility(View.VISIBLE);
            holder.img_star.setImageResource(R.mipmap.star_check);
        }
        if (hotStore.getStarState() == 2) {//未收藏
            holder.img_star.setVisibility(View.VISIBLE);
            holder.img_star.setImageResource(R.mipmap.star_);
        }

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
}