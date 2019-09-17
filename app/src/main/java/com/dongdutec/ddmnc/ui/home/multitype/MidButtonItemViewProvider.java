package com.dongdutec.ddmnc.ui.home.multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.ui.home.multitype.model.MidButtons;

import me.drakeet.multitype.ItemViewProvider;


public class MidButtonItemViewProvider extends ItemViewProvider<MidButtons, MidButtonItemViewProvider.ViewHolder> {
    private Context context;

    public MidButtonItemViewProvider(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.main_midbuttons, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final MidButtons midButtons) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private LinearLayout ll_meishi;
        @NonNull
        private LinearLayout ll_xiuxian;
        @NonNull
        private LinearLayout ll_yule;
        @NonNull
        private LinearLayout ll_xuexi;
        @NonNull
        private LinearLayout ll_gouwu;
        @NonNull
        private LinearLayout ll_dianying;
        @NonNull
        private LinearLayout ll_jiudian;
        @NonNull
        private LinearLayout ll_gengduo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ll_meishi = (LinearLayout) itemView.findViewById(R.id.ll_meishi);
            this.ll_xiuxian = (LinearLayout) itemView.findViewById(R.id.ll_xiuxian);
            this.ll_yule = (LinearLayout) itemView.findViewById(R.id.ll_yule);
            this.ll_xuexi = (LinearLayout) itemView.findViewById(R.id.ll_xuexi);
            this.ll_gouwu = (LinearLayout) itemView.findViewById(R.id.ll_gouwu);
            this.ll_dianying = (LinearLayout) itemView.findViewById(R.id.ll_dianying);
            this.ll_jiudian = (LinearLayout) itemView.findViewById(R.id.ll_jiudian);
            this.ll_gengduo = (LinearLayout) itemView.findViewById(R.id.ll_gengduo);
        }
    }
}