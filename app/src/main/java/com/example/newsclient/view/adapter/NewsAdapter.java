package com.example.newsclient.view.adapter;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.newsclient.Model.bean.NewsBean;
import com.example.newsclient.Model.utils.TimeUtil;
import com.example.newsclient.R;
import com.example.newsclient.presenter.NewsListPresenter;
import com.example.newsclient.view.viewholder.NewsViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-05-04.
 */
public class NewsAdapter extends BaseRecyclerViewAdapter<NewsBean, NewsViewHolder> {
    private NewsListPresenter mPresenter;

    public NewsAdapter(NewsListPresenter presenter) {
        mPresenter = presenter;
    }

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
        return new NewsViewHolder(view, viewType);
    }

    @Override
    protected void onBindFooterViewHolder(NewsViewHolder holder, int position) {
        holder.loadmoreBtn.setOnClickListener(getOnClickListener());
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
            imageView.setTag(s);
        }
        //先设置默认显示图片
        imageView.setImageBitmap(getLoadingBitmap());

        addTask(new Runnable() {
            @Override
            public void run() {
                //获取图片并显示到imageview上
                mPresenter.getBitmap(s, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                        if (s != null && holder.imageView.getTag().equals(s)) {
                            getTdDrawableArray()[1] = new BitmapDrawable(getContext().getResources(), response.getBitmap());
                            TransitionDrawable td = new TransitionDrawable(getTdDrawableArray());
                            holder.imageView.setImageDrawable(td);
                            td.startTransition(300);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
            }

        });


    }

    @Override
    protected List<NewsBean> initdatas() {
        return new ArrayList<>();
    }


}
