package com.example.newsclient.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsclient.R;

/**
 * Created by Administrator on 2016-05-04.
 */
public class ImagesViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView textView;


    public ImagesViewHolder(View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.images_item_iv);
        textView = (TextView) itemView.findViewById(R.id.images_item_tv);

    }
}
