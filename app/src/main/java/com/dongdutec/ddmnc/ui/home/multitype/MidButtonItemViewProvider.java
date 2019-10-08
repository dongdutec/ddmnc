package com.dongdutec.ddmnc.ui.home.multitype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.ui.home.activity.HomeBtnListActivity;
import com.dongdutec.ddmnc.ui.home.activity.HomeBtnMoreActivity;
import com.dongdutec.ddmnc.ui.home.multitype.beans.BtnsBean;
import com.dongdutec.ddmnc.ui.home.multitype.model.MidButtons;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


public class MidButtonItemViewProvider extends ItemViewProvider<MidButtons, MidButtonItemViewProvider.ViewHolder> {
    private Context context;
    private List<BtnsBean> mBeanList = new ArrayList<>();

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
        mBeanList = midButtons.getBtnsBeanList();
        Log.e("HomeFragment", "onBindViewHolder: mBeanList.size() = " + mBeanList.size());
        Log.e("HomeFragment", "onBindViewHolder: mBeanList.toString() = " + mBeanList.toString());

        //8个按钮图片以及文字 TODO 赶时间后续修改
        for (int i = 0; i < mBeanList.size(); i++) {
            if (i == 0) {
                CircleCrop transformation = new CircleCrop();
                RequestOptions requestOptions = RequestOptions.bitmapTransform(transformation);
                Glide.with(context).load(mBeanList.get(i).getClassifyImg())
                        .placeholder(R.mipmap.ms)
                        .apply(requestOptions)
                        .into(holder.img_meishi);
                holder.tv_meishi.setText(mBeanList.get(i).getClassifyName());
                final int finalI = i;
                RxViewAction.clickNoDouble(holder.ll_meishi).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(context, HomeBtnListActivity.class);
                        intent.putExtra("type", mBeanList.get(finalI).getClassifyName());
                        intent.putExtra("classifyId", mBeanList.get(finalI).getId());
                        context.startActivity(intent);
                    }
                });
            }
            if (i == 1) {
                CircleCrop transformation = new CircleCrop();
                RequestOptions requestOptions = RequestOptions.bitmapTransform(transformation);
                Glide.with(context).load(mBeanList.get(i).getClassifyImg())
                        .placeholder(R.mipmap.xx)
                        .apply(requestOptions)
                        .into(holder.img_xiuxian);
                holder.tv_xiuxian.setText(mBeanList.get(i).getClassifyName());
                final int finalI = i;
                RxViewAction.clickNoDouble(holder.ll_xiuxian).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(context, HomeBtnListActivity.class);
                        intent.putExtra("type", mBeanList.get(finalI).getClassifyName());
                        intent.putExtra("classifyId", mBeanList.get(finalI).getId());
                        context.startActivity(intent);
                    }
                });
            }
            if (i == 2) {
                CircleCrop transformation = new CircleCrop();
                RequestOptions requestOptions = RequestOptions.bitmapTransform(transformation);
                Glide.with(context).load(mBeanList.get(i).getClassifyImg())
                        .placeholder(R.mipmap.yule)
                        .apply(requestOptions)
                        .into(holder.img_yule);
                holder.tv_yule.setText(mBeanList.get(i).getClassifyName());
                final int finalI = i;
                RxViewAction.clickNoDouble(holder.ll_yule).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(context, HomeBtnListActivity.class);
                        intent.putExtra("type", mBeanList.get(finalI).getClassifyName());
                        intent.putExtra("classifyId", mBeanList.get(finalI).getId());
                        context.startActivity(intent);
                    }
                });
            }
            if (i == 3) {
                CircleCrop transformation = new CircleCrop();
                RequestOptions requestOptions = RequestOptions.bitmapTransform(transformation);
                Glide.with(context).load(mBeanList.get(i).getClassifyImg())
                        .placeholder(R.mipmap.xuexi)
                        .apply(requestOptions)
                        .into(holder.img_xuexi);
                holder.tv_xuexi.setText(mBeanList.get(i).getClassifyName());
                final int finalI = i;
                RxViewAction.clickNoDouble(holder.ll_xuexi).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(context, HomeBtnListActivity.class);
                        intent.putExtra("type", mBeanList.get(finalI).getClassifyName());
                        intent.putExtra("classifyId", mBeanList.get(finalI).getId());
                        context.startActivity(intent);
                    }
                });
            }
            if (i == 4) {
                CircleCrop transformation = new CircleCrop();
                RequestOptions requestOptions = RequestOptions.bitmapTransform(transformation);
                Glide.with(context).load(mBeanList.get(i).getClassifyImg())
                        .placeholder(R.mipmap.gouwu)
                        .apply(requestOptions)
                        .into(holder.img_gouwu);
                holder.tv_gouwu.setText(mBeanList.get(i).getClassifyName());
                final int finalI = i;
                RxViewAction.clickNoDouble(holder.ll_gouwu).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(context, HomeBtnListActivity.class);
                        intent.putExtra("type", mBeanList.get(finalI).getClassifyName());
                        intent.putExtra("classifyId", mBeanList.get(finalI).getId());
                        context.startActivity(intent);
                    }
                });
            }
            if (i == 5) {
                CircleCrop transformation = new CircleCrop();
                RequestOptions requestOptions = RequestOptions.bitmapTransform(transformation);
                Glide.with(context).load(mBeanList.get(i).getClassifyImg())
                        .placeholder(R.mipmap.dianying)
                        .apply(requestOptions)
                        .into(holder.img_dianying);
                holder.tv_dianying.setText(mBeanList.get(i).getClassifyName());
                final int finalI = i;
                RxViewAction.clickNoDouble(holder.ll_dianying).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(context, HomeBtnListActivity.class);
                        intent.putExtra("type", mBeanList.get(finalI).getClassifyName());
                        intent.putExtra("classifyId", mBeanList.get(finalI).getId());
                        context.startActivity(intent);
                    }
                });
            }
            if (i == 6) {
                CircleCrop transformation = new CircleCrop();
                RequestOptions requestOptions = RequestOptions.bitmapTransform(transformation);
                Glide.with(context).load(mBeanList.get(i).getClassifyImg())
                        .placeholder(R.mipmap.jiudian)
                        .apply(requestOptions)
                        .into(holder.img_jiudian);
                holder.tv_jiudian.setText(mBeanList.get(i).getClassifyName());
                final int finalI = i;
                RxViewAction.clickNoDouble(holder.ll_jiudian).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(context, HomeBtnListActivity.class);
                        intent.putExtra("type", mBeanList.get(finalI).getClassifyName());
                        intent.putExtra("classifyId", mBeanList.get(finalI).getId());
                        context.startActivity(intent);
                    }
                });
            }
            if (i == 7) {
                CircleCrop transformation = new CircleCrop();
                RequestOptions requestOptions = RequestOptions.bitmapTransform(transformation);
                Glide.with(context).load(mBeanList.get(i).getClassifyImg())
                        .placeholder(R.mipmap.qita)
                        .apply(requestOptions)
                        .into(holder.img_gengduo);
                holder.tv_gengduo.setText(mBeanList.get(i).getClassifyName());
                RxViewAction.clickNoDouble(holder.ll_gengduo).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(context, HomeBtnMoreActivity.class);
                        List<String> classifyIdList = new ArrayList<String>();
                        List<String> classifyNameList = new ArrayList<String>();
                        for (int j = 0; j < mBeanList.size(); j++) {
                            if (j > 7) {
                                classifyIdList.add(mBeanList.get(j).getId());
                                classifyNameList.add(mBeanList.get(j).getClassifyName());
                            }
                        }

                        Bundle extras = new Bundle();
                        extras.putStringArrayList("classifyIdList", (ArrayList<String>) classifyIdList);
                        extras.putStringArrayList("classifyNameList", (ArrayList<String>) classifyNameList);
                        intent.putExtras(extras);
                        context.startActivity(intent);
                    }
                });
            }

        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private LinearLayout ll_meishi;
        @NonNull
        private ImageView img_meishi;
        @NonNull
        private TextView tv_meishi;
        @NonNull
        private LinearLayout ll_xiuxian;
        @NonNull
        private ImageView img_xiuxian;
        @NonNull
        private TextView tv_xiuxian;
        @NonNull
        private LinearLayout ll_yule;
        @NonNull
        private ImageView img_yule;
        @NonNull
        private TextView tv_yule;
        @NonNull
        private LinearLayout ll_xuexi;
        @NonNull
        private ImageView img_xuexi;
        @NonNull
        private TextView tv_xuexi;
        @NonNull
        private LinearLayout ll_gouwu;
        @NonNull
        private ImageView img_gouwu;
        @NonNull
        private TextView tv_gouwu;
        @NonNull
        private LinearLayout ll_dianying;
        @NonNull
        private ImageView img_dianying;
        @NonNull
        private TextView tv_dianying;
        @NonNull
        private LinearLayout ll_jiudian;
        @NonNull
        private ImageView img_jiudian;
        @NonNull
        private TextView tv_jiudian;
        @NonNull
        private LinearLayout ll_gengduo;
        @NonNull
        private ImageView img_gengduo;
        @NonNull
        private TextView tv_gengduo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ll_meishi = (LinearLayout) itemView.findViewById(R.id.ll_meishi);
            this.img_meishi = (ImageView) itemView.findViewById(R.id.img_meishi);
            this.tv_meishi = (TextView) itemView.findViewById(R.id.tv_meishi);

            this.ll_xiuxian = (LinearLayout) itemView.findViewById(R.id.ll_xiuxian);
            this.img_xiuxian = (ImageView) itemView.findViewById(R.id.img_xiuxian);
            this.tv_xiuxian = (TextView) itemView.findViewById(R.id.tv_xiuxian);

            this.ll_yule = (LinearLayout) itemView.findViewById(R.id.ll_yule);
            this.img_yule = (ImageView) itemView.findViewById(R.id.img_yule);
            this.tv_yule = (TextView) itemView.findViewById(R.id.tv_yule);

            this.ll_xuexi = (LinearLayout) itemView.findViewById(R.id.ll_xuexi);
            this.img_xuexi = (ImageView) itemView.findViewById(R.id.img_xuexi);
            this.tv_xuexi = (TextView) itemView.findViewById(R.id.tv_xuexi);

            this.ll_gouwu = (LinearLayout) itemView.findViewById(R.id.ll_gouwu);
            this.img_gouwu = (ImageView) itemView.findViewById(R.id.img_gouwu);
            this.tv_gouwu = (TextView) itemView.findViewById(R.id.tv_gouwu);

            this.ll_dianying = (LinearLayout) itemView.findViewById(R.id.ll_dianying);
            this.img_dianying = (ImageView) itemView.findViewById(R.id.img_dianying);
            this.tv_dianying = (TextView) itemView.findViewById(R.id.tv_dianying);

            this.ll_jiudian = (LinearLayout) itemView.findViewById(R.id.ll_jiudian);
            this.img_jiudian = (ImageView) itemView.findViewById(R.id.img_jiudian);
            this.tv_jiudian = (TextView) itemView.findViewById(R.id.tv_jiudian);

            this.ll_gengduo = (LinearLayout) itemView.findViewById(R.id.ll_gengduo);
            this.img_gengduo = (ImageView) itemView.findViewById(R.id.img_gengduo);
            this.tv_gengduo = (TextView) itemView.findViewById(R.id.tv_gengduo);
        }
    }
}