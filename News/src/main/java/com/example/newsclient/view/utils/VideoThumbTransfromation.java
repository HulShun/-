package com.example.newsclient.view.utils;

import android.graphics.Bitmap;
import android.view.View;

import com.squareup.picasso.Transformation;

/**
 * Created by Administrator on 2016/5/21.
 */
public class VideoThumbTransfromation implements Transformation {
    int height, width;

    public VideoThumbTransfromation(View view) {
        height = view.getMeasuredHeight();
        width = view.getMeasuredWidth();
    }


    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(height, width);
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return "crop";
    }
}
