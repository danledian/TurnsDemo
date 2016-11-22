package com.dld.turnsplayviewdemo;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by song on 2016/11/21.
 */

public class ImageLoader {

    public static void load(Context context, String url, ImageView imageView){
        Glide.with(context)
        .load(url)
        .error(R.mipmap.default_image)
        .placeholder(R.mipmap.default_image)
        .dontAnimate()
        .into(imageView);
    }
}
