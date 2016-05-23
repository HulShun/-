package com.example.newsclient.presenter;

import com.android.volley.toolbox.ImageLoader;
import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.NewsListBean;
import com.example.newsclient.Model.impl.NewsFragmentModelImpl;
import com.example.newsclient.Model.model.NewsFragmentModel;
import com.example.newsclient.view.impl.IFragmentViewImpl;

import java.util.List;
import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016-04-11.
 */
public class NewsClassfyPresenter extends BasePresenter<IFragmentViewImpl, NewsFragmentModelImpl> {


    public NewsClassfyPresenter() {

    }

    @Override
    protected NewsFragmentModelImpl instanceModel() {
        return new NewsFragmentModel();
    }

    public void getNewsList(final int mode, final Map<String, String> params) {
        final int page = Integer.parseInt(params.get("page"));

        if (mode == ModelMode.INTERNET && !getView().checkNetWork()) {
            getView().onComplete();
            //  getView().onLoadMoreComplete();
            getView().showNoNetWork();
            return;
        }

        getModel().loadNewsList(mode, params, new Observer<NewsListBean>() {
                    @Override
                    public void onCompleted() {
                        if (getView().isVisiable()) {
                            getView().onComplete();
                            getView().showSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView().isVisiable()) {
                            getView().onComplete();
                            //  getView().onLoadMoreComplete();
                            getView().showFaild(e.getMessage());

                            LogUtil.e(LogUtil.TAG_DB, e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(NewsListBean newsListBean) {
                        if (getView().isVisiable()) {
                            List list = newsListBean.getRetData().getData();
                            if ((list == null || list.size() == 0) && mode == ModelMode.LOCAL) {
                                getNewsList(ModelMode.INTERNET, params);
                            } else {
                                //给UI刷新数据
                                if (page == 1) {
                                    getView().onRefresh(newsListBean);
                                } else {
                                    getView().onLoadMore(newsListBean);
                                }

                                if (newsListBean.getErrMsg() != null) {
                                    //缓存到数据库
                                    getModel().toDataBase(params, newsListBean);
                                }
                            }
                        }
                    }
                }
        );
    }

    public void getBitmap(String uri, ImageLoader.ImageListener listener) {
        getModel().getBitmap(uri, listener);
    }
}
