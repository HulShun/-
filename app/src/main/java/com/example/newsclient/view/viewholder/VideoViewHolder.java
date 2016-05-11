package com.example.newsclient.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsclient.R;
import com.example.newsclient.view.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Administrator on 2016-05-04.
 */
public class VideoViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView title_textView, count_textView;


    public Button loadmoreBtn;

    public VideoViewHolder(View itemView, int viewType) {
        super(itemView);
        if (viewType == BaseRecyclerViewAdapter.VIEWTYPE_ITEM) {
            imageView = (ImageView) itemView.findViewById(R.id.video_item_iv);
            title_textView = (TextView) itemView.findViewById(R.id.video_item_tv_title);
            count_textView = (TextView) itemView.findViewById(R.id.video_item_tv_count);
        } else {
            loadmoreBtn = (Button) itemView.findViewById(R.id.footer_btn);
        }
    }
}
