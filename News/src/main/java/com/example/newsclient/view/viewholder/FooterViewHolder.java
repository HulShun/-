package com.example.newsclient.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newsclient.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2016-05-16.
 */
public class FooterViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout loadingLayout;
    public GifImageView imageView;
    public TextView textView;
    // public Button button;


    public FooterViewHolder(View itemView) {
        super(itemView);
        loadingLayout = (LinearLayout) itemView.findViewById(R.id.footer_loadinglayout);
        //   button = (Button) itemView.findViewById(R.id.footer_btn);
        imageView = (GifImageView) itemView.findViewById(R.id.footer_imageview);
        textView = (TextView) itemView.findViewById(R.id.footer_textview);
    }
}
