package com.dongdutec.ddmnc.ui.home.multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.ui.home.multitype.model.MidBanner;

import me.drakeet.multitype.ItemViewProvider;


public class MidBannerItemViewProvider extends ItemViewProvider<MidBanner, MidBannerItemViewProvider.ViewHolder> {
    private Context context;

    public MidBannerItemViewProvider(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.main_midimg, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final MidBanner midBanner) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private ImageView midimg;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.midimg = (ImageView) itemView.findViewById(R.id.midimg);
        }
    }
}