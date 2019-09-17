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

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private ImageView dianpu_rlv_img;
        @NonNull
        private TextView dianpu_tv_type;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            /*this.dianpu_rlv_img = (ImageView) itemView.findViewById(R.id.dianpu_rlv_img);*/
        }
    }
}