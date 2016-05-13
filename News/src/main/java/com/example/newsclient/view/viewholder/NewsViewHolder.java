package com.example.newsclient.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsclient.R;
import com.example.newsclient.view.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Administrator on 2016-04-12.
 */
public class NewsViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public TextView timeView;
    public ImageView imageView;
    public Button loadmoreBtn;
    public int viewtpye;

    public NewsViewHolder(View itemView, int viewtype) {
        super(itemView);
        if (viewtype == BaseRecyclerViewAdapter.VIEWTYPE_ITEM) {
            titleView = (TextView) itemView.findViewById(R.id.fragment_news_item_tv);
            imageView = (ImageView) itemView.findViewById(R.id.fragment_news_item_iv);
            timeView = (TextView) itemView.findViewById(R.id.fragment_news_item_date);
        } else {
            loadmoreBtn = (Button) itemView.findViewById(R.id.footer_btn);
        }

        this.viewtpye = viewtype;
    }
}
