package com.dongdutec.ddmnc.ui.home.multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.ui.home.multitype.beans.MidBannerBeans;
import com.dongdutec.ddmnc.ui.home.multitype.model.MidBanner;
import com.dongdutec.ddmnc.utils.banner.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

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
        List<MidBannerBeans> midBannerBeansList = midBanner.getMidBannerBeansList();
        List<String> images = new ArrayList<>();
        final List<String> jumpUrls = new ArrayList<>();
        for (int i = 0; i < midBannerBeansList.size(); i++) {
            images.add(midBannerBeansList.get(i).getImg());
            jumpUrls.add(midBannerBeansList.get(i).getUrl());
        }

        //设置banner样式（默认为CIRCLE_INDICATOR圆点指示器）
        holder.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        holder.banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        holder.banner.setImages(images);
        //设置自动轮播，默认为true
        holder.banner.isAutoPlay(true);
        //设置轮播时间
        holder.banner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        holder.banner.setIndicatorGravity(BannerConfig.CENTER);
        holder.banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //通过jumpUrls.get(position)跳转WebView
                Toast.makeText(context, "测试position = " + position + " web = " + jumpUrls.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        //banner设置方法全部调用完毕时最后调用
        holder.banner.start();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private Banner banner;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.banner = (Banner) itemView.findViewById(R.id.banner);
        }
    }
}