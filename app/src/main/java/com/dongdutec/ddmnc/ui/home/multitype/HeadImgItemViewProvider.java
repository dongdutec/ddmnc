package com.dongdutec.ddmnc.ui.home.multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.ui.home.multitype.model.HeadImg;

import me.drakeet.multitype.ItemViewProvider;


public class HeadImgItemViewProvider extends ItemViewProvider<HeadImg, HeadImgItemViewProvider.ViewHolder> {
    private Context context;

    public HeadImgItemViewProvider(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.main_headimg, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final HeadImg headImg) {
        Glide.with(context).load(headImg.getImgUrl())
                .placeholder(R.mipmap.lb)
                .into(holder.headimg);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private ImageView headimg;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.headimg = (ImageView) itemView.findViewById(R.id.headimg);
        }
    }
}