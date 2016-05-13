package com.example.newsclient.view.fragment;

import android.view.View;

import com.example.newsclient.R;
import com.example.newsclient.presenter.BasePresenter;

/**
 * Created by Administrator on 2016-05-13.
 */
public class CommentsFramgent extends BaseFragment {
    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initLoading() {

    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_comments;
    }

    @Override
    int getLoadingLayouId() {
        return R.layout.loadinglayout;
    }
}
