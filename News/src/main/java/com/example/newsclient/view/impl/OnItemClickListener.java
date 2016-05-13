package com.example.newsclient.view.impl;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2016-05-04.
 */
public interface OnItemClickListener<VH extends RecyclerView.ViewHolder> {
    void onClick(VH viewHolder, int position);

}
