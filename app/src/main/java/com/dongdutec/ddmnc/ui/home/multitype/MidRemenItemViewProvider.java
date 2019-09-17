package com.dongdutec.ddmnc.ui.home.multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.ui.home.multitype.model.MidRemen;

import me.drakeet.multitype.ItemViewProvider;


public class MidRemenItemViewProvider extends ItemViewProvider<MidRemen, MidRemenItemViewProvider.ViewHolder> {
    private Context context;

    public MidRemenItemViewProvider(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.main_midremen, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final MidRemen midRemen) {
//        Glide.with(context).load(headImg.getImgUrl()).into(holder.headimg);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private ImageView img_midremen;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.img_midremen = (ImageView) itemView.findViewById(R.id.img_midremen);
        }
    }
}