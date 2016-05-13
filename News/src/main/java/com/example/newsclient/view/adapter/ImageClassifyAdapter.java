package com.example.newsclient.view.adapter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;

import com.example.newsclient.Model.bean.image.ImageJsonBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.ImageClassifyPresenter;
import com.example.newsclient.view.viewholder.ImageClassifyViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-05-05.
 */
public class ImageClassifyAdapter extends BaseRecyclerViewAdapter<ImageJsonBean, ImageClassifyViewHolder> {


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
        holder.textView.setText(getData().get(position).getShowapi_res_body().getPagebean().getContentlist().get(0).getTypeName());
        final String s = getData().get(position).getShowapi_res_body().getPagebean().getContentlist().get(0).getList().get(0).getMiddle();
        if (!TextUtils.isEmpty(s)) {
            holder.imageView.setTag(s);
        }
        Picasso.with(getContext())
                .load(s)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_loading)
                .into(holder.imageView);

    }


    @Override
    protected List<ImageJsonBean> initdatas() {
        return new ArrayList<>();
    }
}
