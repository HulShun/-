package com.example.newsclient.view.adapter;

import android.view.View;

import com.example.newsclient.Model.bean.VideosInFormBean;
import com.example.newsclient.R;
import com.example.newsclient.view.viewholder.VideoViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-05-10.
 */
public class VideoClassifyAdapter extends BaseRecyclerViewAdapter<VideosInFormBean.VideosBean, VideoViewHolder> {
    @Override
    public int getFooterLayoutId() {
        return R.layout.fragment_rv_footer;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.fragment_videos_item;
    }

    @Override
    protected VideoViewHolder onCreateMyViewHolder(View view, int viewType) {
        return new VideoViewHolder(view, viewType);
    }

    @Override
    protected void onBindFooterViewHolder(VideoViewHolder holder, int position) {
        holder.loadmoreBtn.setOnClickListener(getOnClickListener());
    }

    @Override
    protected void onBindItemViewHolder(VideoViewHolder holder, int position) {
        holder.textView.setText(getData().get(position).getTitle());
        String img_url = getData().get(position).getBigThumbnail();
        Picasso.with(getContext())
                .load(img_url)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_loading)
                .tag(img_url)
                .into(holder.imageView);
    }

    @Override
    protected List<VideosInFormBean.VideosBean> initdatas() {
        return new ArrayList<>();
    }
}
