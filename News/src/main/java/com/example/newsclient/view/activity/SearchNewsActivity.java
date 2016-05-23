package com.example.newsclient.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.NewsBean;
import com.example.newsclient.Model.bean.NewsListBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.NewsClassfyPresenter;
import com.example.newsclient.view.adapter.NewsAdapter;
import com.example.newsclient.view.impl.IFragmentViewImpl;
import com.example.newsclient.view.impl.OnItemClickListener;
import com.example.newsclient.widget.AutoRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2016-05-23.
 */
public class SearchNewsActivity extends BaseActivity<NewsClassfyPresenter> implements IFragmentViewImpl {

    @Bind(R.id.activity_search_title)
    TextView activitySearchTitle;
    @Bind(R.id.activity_search_rc)
    AutoRecyclerView activitySearchRc;
    @Bind(R.id.activity_search_refresh)
    SwipeRefreshLayout activitySearchRefresh;

    private NewsAdapter mAdapter;

    private String key;
    private int nowpage;

    @Override
    protected int getToolBarId() {
        return R.id.activity_search_toolbar;
    }

    @Override
    public int getStatusBarColor() {
        return R.color.colorPrimary;
    }

    @Override
    protected void init() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        key = getIntent().getStringExtra(Configuration.KEYWORD);
        activitySearchTitle.setText("搜索:\"" + key + "\"");
        nowpage = 1;
        getDatas(key, nowpage);
    }

    private void getDatas(String key, int nowpage) {
        Map<String, String> map = new HashMap<>();
        map.put(Configuration.KEYWORD, key);
        map.put("page", String.valueOf(nowpage));
        map.put("count", String.valueOf(20));
        getPresenter().getNewsList(ModelMode.INTERNET, map);
    }

    private void initRecyclerView() {
        mAdapter = new NewsAdapter();
        mAdapter.setFooterShow(true);
        mAdapter.setOnFooterListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.showFooterLoading();
                nowpage++;
                getDatas(key, nowpage);
            }
        });
        mAdapter.setOnItemClickListenner(new OnItemClickListener() {
            @Override
            public void onClick(RecyclerView.ViewHolder viewHolder, int position) {
                NewsBean bean = (NewsBean) viewHolder.itemView.getTag();
                Intent intent = new Intent();
                intent.setClass(SearchNewsActivity.this, ArticleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("key", key);
                bundle.putString("title", bean.getTitle());
                bundle.putString("url", bean.getUrl());
                intent.putExtra("article", bundle);
                startActivity(intent);
            }
        });
        activitySearchRc.setLayoutManager(new LinearLayoutManager(this));
        activitySearchRc.setItemAnimator(new DefaultItemAnimator());

        activitySearchRc.setAdapter(mAdapter);
        activitySearchRc.addOnScrollListener(new AutoRecyclerView.AutoLoadMoreListener() {
            @Override
            protected void loadMore() {
                mAdapter.showFooterLoading();
                nowpage++;
                getDatas(key, nowpage);
            }

            @Override
            protected void pauseLoadImage() {
                Picasso.with(SearchNewsActivity.this)
                        .pauseTag(getApplicationContext());
            }

            @Override
            protected void resumeLoadImage(LinearLayoutManager manager, int first, int last) {
                Picasso.with(SearchNewsActivity.this)
                        .resumeTag(getApplicationContext());
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected NewsClassfyPresenter initPresenter() {
        return new NewsClassfyPresenter();
    }

    @Override
    public void onRefresh(NewsListBean datas) {
        if (datas.getRetData().getData() == null) {
            Toast.makeText(getApplicationContext(), "查找不到相关新闻.", Toast.LENGTH_SHORT).show();
        }
        if (mAdapter == null) {
            initRecyclerView();
        }
        mAdapter.clearData();
        mAdapter.addData(datas.getRetData().getData());
    }

    @Override
    public void onLoadMore(NewsListBean datas) {
        mAdapter.addData(datas.getRetData().getData());
    }

    @Override
    public void showFaild(String msg) {
        if (mAdapter != null) {
            mAdapter.showFooterBtn();
        }
    }

    @Override
    public void onComplete() {
        if (activitySearchRefresh != null) {
            activitySearchRefresh.setRefreshing(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SearchNewsActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
