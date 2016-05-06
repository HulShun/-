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
import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.NewsBean;
import com.example.newsclient.Model.bean.NewsListBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.NewsListPresenter;
import com.example.newsclient.view.activity.ArticleActivity;
import com.example.newsclient.view.adapter.NewsAdapter;
import com.example.newsclient.view.impl.IFragmentViewImpl;
import com.example.newsclient.view.impl.OnItemClickListener;
import com.example.newsclient.view.viewholder.NewsViewHolder;
import com.example.newsclient.widget.AutoRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-04-11.
 */
public class NewsFragment extends BaseFragment<NewsListPresenter> implements IFragmentViewImpl {


    private String mKeyWord;
    private int nextPage;

    @Bind(R.id.news_rc)
    AutoRecyclerView newsRc;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsAdapter mAdapter;
    private NewsListBean newsList;

    /**
     * 第一次刷新标记
     */
    private boolean isFirstRefresh = true;


    @Bind(R.id.fragment_refresh)
    SwipeRefreshLayout fragmentRefresh;


    @Override
    protected NewsListPresenter getPresenter() {
        if (mPresenter == null) {
            NewsListPresenter presenter = new NewsListPresenter();
            return presenter;
        } else {
            return mPresenter;
        }
    }

    @Override
    protected void initLoading() {
        Bundle bundle = getArguments();
        mKeyWord = bundle.getString(Configuration.KEYWORD);

        getLoadingView().setOnBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void getMore() {
        if (newsList != null && newsList.getRetData().getHas_more() == 0) {
            Toast.makeText(NewsFragment.this.getContext(), "已全部加载...", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("keyword", mKeyWord);
            map.put("page", String.valueOf(nextPage));
            map.put("count", "20");
            getPresenter().getNewsList(ModelMode.LOCAL, map);
        }

    }

    private void initRecyclerView() {
        newsRc.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(NewsFragment.this.getContext());
        newsRc.setItemAnimator(new DefaultItemAnimator());
        newsRc.setLayoutManager(mLayoutManager);
        if (mAdapter == null && mPresenter != null) {
            mAdapter = new NewsAdapter(mPresenter);
            mAdapter.setFooterShow(true);
            mAdapter.setOnFooterListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.setFooterText("正在加载中...");
                    getMore();
                }
            });
            mAdapter.setOnItemClickListenner(new OnItemClickListener<NewsViewHolder>() {
                @Override
                public void onClick(NewsViewHolder viewHolder, int position) {
                    NewsBean bean = (NewsBean) viewHolder.itemView.getTag();
                    Intent intent = new Intent();
                    intent.setClass(NewsFragment.this.getContext(), ArticleActivity.class);
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
                mAdapter.setFooterText("正在加载中...");
                getMore();
            }

            @Override
            protected void pauseLoadImg() {
                if (mAdapter != null) {
                    mAdapter.pauseLoading(true);
                }
            }

            @Override
            protected void resumeLoadImg(int firstPosition, int lastPositon) {
                if (mAdapter != null) {
                    mAdapter.pauseLoading(false);
                    mAdapter.executeTask(firstPosition, lastPositon);
                }
            }
        });
    }


    private void updateDatas(int mode) {
        Map<String, String> map = new HashMap<>();
        map.put("keyword", mKeyWord);
        LogUtil.d(LogUtil.TAG_DB, "当前页面：" + mKeyWord);
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
        mPresenter.getNewsList(mode, map);
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
    public void onRefreshOrLoadMore(NewsListBean datas) {
        if (getLoadingView().isloading()) {
            getLoadingView().showSuccess();
        }
        newsList = datas;
        List<NewsBean> list = datas.getRetData().getData();
        LogUtil.d(LogUtil.TAG_DB, "当前页数：" + nextPage);
        LogUtil.d(LogUtil.TAG_DB, "是否有下一页：" + datas.getRetData().getHas_more());
        if (datas.getRetData().getHas_more() == 0) {
            //最后加载完数据，隐藏加载更多标识
            mAdapter.setFooterShow(false);
        }
        mAdapter.addData(list);
        nextPage++;
    }

    @Override
    public void onRefreshComplete() {
        if (newsRc != null) {
            newsRc.loadMoreCompleted();
        }
        if (fragmentRefresh != null) {
            fragmentRefresh.setRefreshing(false);
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
    public void showSuccess() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
