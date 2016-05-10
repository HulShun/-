package com.example.newsclient.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;

import com.example.newsclient.Model.bean.ImageJsonBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.ImageListPresenter;
import com.example.newsclient.view.impl.IImageListViewImpl;
import com.example.newsclient.widget.AutoRecyclerView;

import butterknife.Bind;

/**
 * 相册中图片列表展示页面
 * Created by Administrator on 2016-05-05.
 */
public class ImageListActivity extends BaseActivity<ImageListPresenter> implements IImageListViewImpl {
    @Bind(R.id.imagelist_toolbar)
    Toolbar imagelistToolbar;
    @Bind(R.id.imagelist_rc)
    AutoRecyclerView imagelistRc;
    @Bind(R.id.imagelist_refresh)
    SwipeRefreshLayout imagelistRefresh;

    @Override
    protected int getToolBarId() {
        return R.id.imagelist_toolbar;
    }

    @Override
    protected void init() {
        Bundle bundle = getIntent().getBundleExtra("data");
        ImageJsonBean data = bundle.getParcelable("data");
        initRecyclerView(data);
    }

    private void initRecyclerView(ImageJsonBean data) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_imagelist;
    }



    @Override
    protected ImageListPresenter initPresenter() {
        return new ImageListPresenter();
    }

    @Override
    public void onRefreshed(ImageJsonBean data) {

    }

    @Override
    public void onLoadMore(ImageJsonBean data) {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void showNoNetWork() {

    }

    @Override
    public boolean isVisiable() {
        return false;
    }

    @Override
    public boolean checkNetWork() {
        return false;
    }

    @Override
    public void showSuccess() {

    }

    @Override
    public void showFaild(String msg) {

    }

}
