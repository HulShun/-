package com.example.newsclient.view.impl;

import com.example.newsclient.Model.bean.NewsListBean;

/**
 * Created by Administrator on 2016-04-11.
 */
public interface IFragmentViewImpl extends IBaseViewImpl {

    void onRefresh(NewsListBean datas);

    void onLoadMore(NewsListBean datas);

    void onComplete();

    //  void onLoadMoreComplete();

    void showNoNetWork();


}
