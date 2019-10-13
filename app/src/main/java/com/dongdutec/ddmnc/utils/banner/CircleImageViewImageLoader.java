package com.dongdutec.ddmnc.utils.banner;

import android.content.Context;

import com.dongdutec.ddmnc.cell.circleimageview.CircleImageView;
import com.youth.banner.loader.ImageLoaderInterface;

public abstract class CircleImageViewImageLoader implements ImageLoaderInterface<CircleImageView> {

    @Override
    public CircleImageView createImageView(Context context) {
        CircleImageView circleImageView = new CircleImageView(context);
        return circleImageView;
    }

}