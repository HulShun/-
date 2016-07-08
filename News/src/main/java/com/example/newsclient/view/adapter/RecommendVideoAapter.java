package com.example.newsclient.view.adapter;

import android.view.View;

import com.example.newsclient.Model.bean.video.RecommendJsonVideoBean;
import com.example.newsclient.R;
import com.example.newsclient.view.viewholder.RecommendVideoViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-05-17.
 */
public class RecommendVideoAapter extends BaseRecyclerViewAdapter<RecommendJsonVideoBean.RecommendVideoBean, RecommendVideoViewHolder> {


    @Override
    public int getFooterLayoutId() {
        return 0;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.fragment_brief_item_video;
    }

    @Override
    protected RecommendVideoViewHolder onCreateMyViewHolder(View view, int viewType) {
        return new RecommendVideoViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(RecommendVideoViewHolder holder, int position) {
        holder.textView.setText(getData().get(position).getTitle());
        String img_utl = getData().get(position).getThumbnail();
        Picasso.with(getContext())
                .load(img_utl)
                .tag(img_utl)
                .error(R.drawable.ic_loading)
                .placeholder(R.drawable.ic_loading)
                .into(holder.imageView);
    }

    @Override
    protected List<RecommendJsonVideoBean.RecommendVideoBean> initdatas() {
        return new ArrayList<>();
    }
}
