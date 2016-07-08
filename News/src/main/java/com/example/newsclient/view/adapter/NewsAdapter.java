package com.example.newsclient.view.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.example.newsclient.Model.bean.NewsBean;
import com.example.newsclient.Model.utils.TimeUtil;
import com.example.newsclient.R;
import com.example.newsclient.view.viewholder.NewsViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-05-04.
 */
public class NewsAdapter extends BaseRecyclerViewAdapter<NewsBean, NewsViewHolder> {

    @Override
    public int getFooterLayoutId() {
        return R.layout.fragment_rv_footer;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.fragment_news_item;
    }

    @Override
    protected NewsViewHolder onCreateMyViewHolder(View view, int viewType) {
        return new NewsViewHolder(view);
    }


    @Override
    protected void onBindItemViewHolder(final NewsViewHolder holder, final int position) {
        NewsBean bean = getData().get(position);
        holder.titleView.setText(bean.getTitle());
        String time = TimeUtil.getInstance().getTimeForNow(bean.getDatetime());
        holder.timeView.setText(time);
        final String s = bean.getImg_url();
        final ImageView imageView = holder.imageView;
        if (s != null) {
            //  imageView.setTag(s);
            Picasso.with(getContext().getApplicationContext())
                    .load(s)
                    .tag(s)
                    .placeholder(R.drawable.ic_loading)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.ic_loading)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_loading);
        }

    }

    @Override
    protected List<NewsBean> initdatas() {
        return new ArrayList<>();
    }


}
