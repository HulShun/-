package com.example.newsclient.view.adapter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.example.newsclient.Model.bean.image.ImageContentBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.ImageListPresenter;
import com.example.newsclient.view.viewholder.ImageViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-05-04.
 */
public class ImagesAdapter extends BaseRecyclerViewAdapter<ImageContentBean, ImageViewHolder> {

    private ImageListPresenter mPresenter;

    public ImagesAdapter(ImageListPresenter presenter) {
        mPresenter = presenter;
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
    protected ImageViewHolder onCreateMyViewHolder(View view, int viewType) {
        return new ImageViewHolder(view);
    }


    @Override
    protected void onBindItemViewHolder(final ImageViewHolder holder, int position) {
        holder.textView.setText(getData().get(position).getTitle());
        //用相册中第一张图片作为该相册的封面图
        final String s = getData().get(position).getList().get(0).getSmall();
        ImageView imageView = holder.imageView;
        if (!TextUtils.isEmpty(s)) {
            //imageView.setTag(s);
            Picasso.with(getContext())
                    .load(s)
                    .tag(s)
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_loading)
                    .config(Bitmap.Config.RGB_565)
                    .into(imageView);

        } else {
            imageView.setImageResource(R.drawable.ic_launcher);
        }
      /*  //默认加载图片
        imageView.setImageBitmap(getLoadingBitmap());

        addTask(new Runnable() {
            @Override
            public void run() {
                mPresenter.getBitmap(s, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        if (s != null && holder.imageView.getTag().equals(s)) {
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
        });*/
    }

    @Override
    protected List<ImageContentBean> initdatas() {
        return new ArrayList<>();
    }


}
