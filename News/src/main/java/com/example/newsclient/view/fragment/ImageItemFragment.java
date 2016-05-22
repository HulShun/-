package com.example.newsclient.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.newsclient.R;
import com.example.newsclient.presenter.BasePresenter;
import com.example.newsclient.view.impl.MulitPointTouchListener;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2016/5/22.
 */
public class ImageItemFragment extends BaseFragment {
    private ImageView imageView;
    private String img_url;


    @Override
    protected BasePresenter initPresenter() {
        return null;
    }


    @Override
    protected void initLoading() {
        Bundle bundle = getArguments();
        img_url = bundle.getString("url");
        getLoadingView().showSuccess();
    }

    @Override
    protected void initViews(View view) {
        imageView = (ImageView) view.findViewById(R.id.fragmentitem_iv);
        imageView.setOnTouchListener(new MulitPointTouchListener());
        Picasso.with(getContext())
                .load(img_url)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_loading)
                .into(imageView);
    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_imageitem;
    }

    @Override
    int getLoadingLayouId() {
        return R.layout.loadinglayout;
    }


}
