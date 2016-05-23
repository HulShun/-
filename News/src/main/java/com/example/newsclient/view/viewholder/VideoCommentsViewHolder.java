package com.example.newsclient.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsclient.R;

/**
 * Created by Administrator on 2016-05-18.
 */
public class VideoCommentsViewHolder extends RecyclerView.ViewHolder {
    public ImageView userImg;
    public TextView userNameView;
    public CheckedTextView zanView, resposeView;
    public TextView contentView;
    public TextView timeText;

    public VideoCommentsViewHolder(View itemView) {
        super(itemView);
        userImg = (ImageView) itemView.findViewById(R.id.comment_item_userimg);
        userNameView = (TextView) itemView.findViewById(R.id.comment_item_user);
        //  zanView = (CheckedTextView) itemView.findViewById(R.id.comment_item_zan_count);
        // resposeView = (CheckedTextView) itemView.findViewById(R.id.comment_item_comment_count);
        contentView = (TextView) itemView.findViewById(R.id.comment_item_text);
        timeText = (TextView) itemView.findViewById(R.id.comments_item_timetext);
    }
}
