package com.dongdutec.ddmnc.utils.banner;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.cell.circleimageview.CircleImageView;

public class GlideImageLoader extends CircleImageViewImageLoader {
    @Override
    public void displayImage(Context context, Object path, CircleImageView imageView) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */

        //Glide 加载图片
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setRoundHeight(120);
        imageView.setRoundWidth(120);
        /*RoundedCorners roundedCorners = new RoundedCorners(120);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);*/
        Glide.with(context).load(path)
                .placeholder(R.mipmap.guanggao)
                .into(imageView);

    }
}