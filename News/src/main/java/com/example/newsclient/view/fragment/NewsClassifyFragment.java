package com.example.newsclient.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.NewsBean;
import com.example.newsclient.Model.bean.NewsListBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.NewsClassfyPresenter;
import com.example.newsclient.view.activity.ArticleActivity;
import com.example.newsclient.view.adapter.NewsAdapter;
import com.example.newsclient.view.impl.IFragmentViewImpl;
import com.example.newsclient.view.impl.OnItemClickListener;
import com.example.newsclient.view.viewholder.NewsViewHolder;
import com.example.newsclient.widget.AutoRecyclerView;
import com.example.newsclient.widget.MyScrollLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-04-11.
 */
public class NewsClassifyFragment extends BaseFragment<NewsClassfyPresenter> implements IFragmentViewImpl {


    private String mKeyWord;
    private int nextPage;

    @Bind(R.id.news_rc)
    AutoRecyclerView newsRc;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsAdapter mAdapter;
    private NewsListBean newsList;

    @Bind(R.id.fragment_refresh)
    SwipeRefreshLayout fragmentRefresh;



    @Override
    protected NewsClassfyPresenter initPresenter() {
        return new NewsClassfyPresenter();
    }


    @Override
    protected void initLoading() {
        Bundle bundle = getArguments();
        mKeyWord = bundle.getString(Configuration.KEYWORD);

        getLoadingView().setOnBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoadingView().showLoading();
                updateDatas(ModelMode.LOCAL);
            }
        });

        //加载数据,先从本地加载数据
        updateDatas(ModelMode.LOCAL);
    }

    @Override
    protected void resume() {
        if (mAdapter != null && mAdapter.getItemCount() == 0) {
            updateDatas(ModelMode.LOCAL);
        }
    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);
        fragmentRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateDatas(ModelMode.INTERNET);
            }
        });

        initRecyclerView();

    }


    private void initRecyclerView() {
        newsRc.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(NewsClassifyFragment.this.getContext());
        newsRc.setItemAnimator(new DefaultItemAnimator());
        newsRc.setLayoutManager(mLayoutManager);
        if (mAdapter == null) {
            mAdapter = new NewsAdapter();
            mAdapter.setFooterShow(true);
            mAdapter.setOnFooterListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMore();
                }
            });
            mAdapter.setOnItemClickListenner(new OnItemClickListener<NewsViewHolder>() {
                @Override
                public void onClick(NewsViewHolder viewHolder, int position) {
                    NewsBean bean = (NewsBean) viewHolder.itemView.getTag();
                    Intent intent = new Intent();
                    intent.setClass(NewsClassifyFragment.this.getContext(), ArticleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("key", mKeyWord);
                    bundle.putString("title", bean.getTitle());
                    bundle.putString("url", bean.getUrl());
                    intent.putExtra("article", bundle);
                    startActivity(intent);
                }
            });
            newsRc.setAdapter(mAdapter);

        }

        newsRc.addOnScrollListener(new AutoRecyclerView.AutoLoadMoreListener() {
            @Override
            protected void loadMore() {
                mAdapter.showFooterLoading();
                getMore();
            }
        });
    }

    private void getMore() {

        if (newsList != null && newsList.getRetData().getHas_more() == 0) {
            Toast.makeText(NewsClassifyFragment.this.getContext(), "已全部加载...", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.showFooterLoading();
            Map<String, String> map = new HashMap<>();
            map.put("keyword", mKeyWord);
            map.put("page", String.valueOf(nextPage));
            map.put("count", "20");
            getPresenter().getNewsList(ModelMode.LOCAL, map);
        }

    }

    private void updateDatas(int mode) {
        Map<String, String> map = new HashMap<>();
        map.put(Configuration.KEYWORD, mKeyWord);
        nextPage = 1;
        map.put("page", String.valueOf(nextPage));
        map.put("count", "20");
        //如果是网络请求，就先清空当前数据库中的数据
        if (mode == ModelMode.INTERNET) {
            map.put("refresh", "refresh");
        }
        if (mAdapter != null) {
            mAdapter.clearData();
        }
        getPresenter().getNewsList(mode, map);
    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    int getLoadingLayouId() {
        return R.layout.loadinglayout;
    }


    @Override
    public void onRefresh(NewsListBean datas) {
        if (getLoadingView().isloading()) {
            getLoadingView().showSuccess();
        }
        newsList = datas;
        List<NewsBean> list = datas.getRetData().getData();
        if (datas.getRetData().getHas_more() == 0) {
            //最后加载完数据，隐藏加载更多标识
            mAdapter.setFooterShow(false);
        }
        mAdapter.addData(list);
        nextPage++;
    }

    @Override
    public void onLoadMore(NewsListBean datas) {
        mAdapter.addData(datas.getRetData().getData());
    }

    @Override
    public void onComplete() {
        if (newsRc != null) {
            newsRc.loadMoreCompleted();
        }
        if (fragmentRefresh != null) {
            fragmentRefresh.setRefreshing(false);
        }
    }


    @Override
    public void showFaild(String msg) {
        super.showFaild(msg);
        if (mAdapter != null) {
            mAdapter.showFooterBtn();
        }
    }

    @Override
    public void showNoNetWork() {
        super.showNoNetWork();
        if (fragmentRefresh == null) {
            fragmentRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_refresh);
        }
        fragmentRefresh.setRefreshing(false);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
