package com.dongdutec.ddmnc.ui.home.multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.ui.home.multitype.model.HotStore;

import me.drakeet.multitype.ItemViewProvider;


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
//        Glide.with(context).load(hotStore.getImageUrl()).into(holder.main_listimg);
        holder.main_storename.setText(hotStore.getStoreName());
        holder.tv_location.setText(hotStore.getLocationStr());
        holder.tv_count.setText("记账人数：" + hotStore.getCount());
        holder.tv_distance.setText("距当前：" + hotStore.getCount() + "Km");
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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.main_listimg = (ImageView) itemView.findViewById(R.id.main_listimg);
            this.main_storename = (TextView) itemView.findViewById(R.id.main_storename);
            this.tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            this.tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            this.tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
        }
    }
}