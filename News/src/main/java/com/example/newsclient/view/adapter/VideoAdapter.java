package com.example.newsclient.view.adapter;

import android.view.View;

import com.example.newsclient.Model.bean.VideoBean;
import com.example.newsclient.R;
import com.example.newsclient.view.viewholder.VideoViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016-05-04.
 */
public class VideoAdapter extends BaseRecyclerViewAdapter<VideoBean, VideoViewHolder> {
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

    }

    @Override
    protected void onBindItemViewHolder(VideoViewHolder holder, int position) {

    }

    @Override
    protected List<VideoBean> initdatas() {
        return null;
    }


}
