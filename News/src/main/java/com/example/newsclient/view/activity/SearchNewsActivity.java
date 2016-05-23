package com.example.newsclient.view.activity;

import android.app.SearchManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.TextView;

import com.example.newsclient.Model.bean.NewsListBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.NewsClassfyPresenter;
import com.example.newsclient.view.impl.IFragmentViewImpl;
import com.example.newsclient.widget.AutoRecyclerView;

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

    private String type;
    private int nowpage;

    @Override
    protected int getToolBarId() {
        return R.id.activity_search_toolbar;
    }

    @Override
    protected void init() {
        type = getIntent().getStringExtra(SearchManager.QUERY);


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
    public void onRefreshOrLoadMore(NewsListBean datas) {

    }

    @Override
    public void onRefreshComplete() {

    }

}
