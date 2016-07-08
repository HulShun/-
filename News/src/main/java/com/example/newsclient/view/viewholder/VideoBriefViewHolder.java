package com.example.newsclient.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.newsclient.R;

/**
 * Created by Administrator on 2016-07-08.
 */
public class VideoBriefViewHolder extends RecyclerView.ViewHolder {
    public TextView brief_text;
    public TextView brief_text_nolimit;
    public Button brief_tv_btn;

    public boolean isTextOn;

    public VideoBriefViewHolder(View itemView) {
        super(itemView);
        brief_text = (TextView) itemView.findViewById(R.id.brief_text);
        brief_text_nolimit = (TextView) itemView.findViewById(R.id.brief_text_nolimit);
        brief_tv_btn = (Button) itemView.findViewById(R.id.brief_tv_btn);
    }
}
