package com.example.newsclient.view.activity;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.example.newsclient.R;
import com.example.newsclient.presenter.ImageListPresenter;

/**
 * 相册中图片列表展示页面
 * Created by Administrator on 2016-05-05.
 */
public class ImageListActivity extends BaseActivity<ImageListPresenter> {

    int type;

    @Override
    protected int getToolBarId() {
        return R.id.imagelist_toolbar;
    }

    @Override
    public int getStatusBarColor() {
        return R.color.colorPrimary;
    }

    @Override
    protected void onCreateBeforView() {
        type = getIntent().getIntExtra("type", 1001);
    }

    @Override
    protected void init() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            String s = getIntent().getStringExtra("title");
            actionBar.setTitle(s);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_imagelist;
    }


    @Override
    protected ImageListPresenter initPresenter() {
        return new ImageListPresenter();
    }

    public int getType() {
        return type;
    }
}
