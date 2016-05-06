package com.example.newsclient.view.adapter;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.newsclient.R;
import com.example.newsclient.presenter.ImageClassifyPresenter;
import com.example.newsclient.view.fragment.ImageClassifyFragment;
import com.example.newsclient.view.viewholder.ImageClassifyViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-05-05.
 */
public class ImageClassifyAdapter extends BaseRecyclerViewAdapter<ImageClassifyFragment.ItemBean, ImageClassifyViewHolder> {

    private List<ImageClassifyFragment.ItemBean> mImageBeans;
    private ImageClassifyPresenter mPresenter;


    public ImageClassifyAdapter(ImageClassifyPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public int getFooterLayoutId() {
        return R.layout.fragment_rv_footer;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.fragment_imageclassify_item;
    }

    @Override
    protected ImageClassifyViewHolder onCreateMyViewHolder(View view, int viewType) {
        return new ImageClassifyViewHolder(view, viewType);
    }

    @Override
    protected void onBindFooterViewHolder(ImageClassifyViewHolder holder, int position) {
        // holder.loadmoreBtn.setOnClickListener(getOnClickListener());
    }

    @Override
    protected void onBindItemViewHolder(final ImageClassifyViewHolder holder, int position) {
        holder.textView.setText(getData().get(position).name);
        final String s = getData().get(position).img_url;
        if (!TextUtils.isEmpty(s)) {
            holder.imageView.setTag(s);
        }
        holder.imageView.setImageBitmap(getLoadingBitmap());

        addTask(new Runnable() {
            @Override
            public void run() {
                mPresenter.getBitmap(s, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        if (s != null && s.equals(holder.imageView.getTag())) {
                            //加载图片后渐变显示
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
    protected List<ImageClassifyFragment.ItemBean> initdatas() {
        return new ArrayList<>();
    }
}
