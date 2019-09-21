package com.dongdutec.ddmnc.ui.my.multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.ui.my.multitype.model.Daili;

import me.drakeet.multitype.ItemViewProvider;


public class DailiItemViewProvider extends ItemViewProvider<Daili, DailiItemViewProvider.ViewHolder> {
    private Context context;

    public DailiItemViewProvider(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.daili_headimg, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Daili daili) {
//        Glide.with(context).load(daili.getHeadImgUrl()).into(holder.img_head);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private ImageView img_head;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.img_head = (ImageView) itemView.findViewById(R.id.img_head);
        }
    }
}