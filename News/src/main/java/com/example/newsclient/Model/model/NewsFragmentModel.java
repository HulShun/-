package com.example.newsclient.Model.model;

import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.example.newsclient.Configuration;
import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.NewsBean;
import com.example.newsclient.Model.bean.NewsListBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.NewsFragmentModelImpl;
import com.example.newsclient.view.utils.AppUtil;
import com.example.newsclient.Model.utils.MyImageLoader;
import com.example.newsclient.Model.utils.RetrofitUtil;
import com.example.newsclient.dao.NewsDao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorFailedException;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-04-11.
 */
public class NewsFragmentModel implements NewsFragmentModelImpl {


    @Override
    public void loadNewsList(int mode, final Map<String, String> params, final Observer<NewsListBean> os) {
        if (mode == ModelMode.INTERNET) {
            //如果是刷新，就先清空数据库原来的数据
            String refresh = params.get("refresh");
            final String keyword = params.get("keyword");
            LogUtil.d(LogUtil.TAG_DB, "联网获取数据的keyWord：" + keyword);
            if ("refresh".equals(refresh)) {
                Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        NewsDao dao = NewsDao.getInstance();
                        subscriber.onNext(dao.deleteByKey(keyword));
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.immediate())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                LogUtil.d(LogUtil.TAG_DB, "删除数据库数据返回值：" + integer);
                            }
                        });
            }

            try {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(Configuration.HTTP_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .client(RetrofitUtil.createClinet("apikey", Configuration.HTTP_ARG))
                        .build();
                ApiService service = retrofit.create(ApiService.class);

                Observable<NewsListBean> ob = service.loadNewsList(keyword, params.get("page"), params.get("count"));
                ob.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(os);
            } catch (OnErrorFailedException e) {
                e.printStackTrace();
            }
        } else {
            //去数据库拿数据
            Observable.create(new Observable.OnSubscribe<NewsListBean>() {
                                  @Override
                                  public void call(Subscriber<? super NewsListBean> subscriber) {
                                      NewsDao dao = NewsDao.getInstance();
                                      NewsListBean list = dao.query(params);
                                      subscriber.onNext(list);
                                      subscriber.onCompleted();
                                  }
                              }
            ).subscribeOn(Schedulers.io())
                    .delay(300, TimeUnit.MILLISECONDS)   //为了让界面流畅点，等待页面显示完全再加载
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(os);
        }

    }

    @Override
    public void getBitmap(String url, ImageLoader.ImageListener listener) {
        ImageLoader loader = MyImageLoader.getImageLoader();
        if (url != null) {
            AppUtil util = AppUtil.getInstance();
            loader.get(url, listener, util.getScreenWidth(), util.getScreenHeight() / 2, ImageView.ScaleType.CENTER_INSIDE);
        }

    }

    @Override
    public void toDataBase(final Map<String, String> params, final NewsListBean body) {

        Observable.create(new Observable.OnSubscribe<List<NewsBean>>() {
            @Override
            public void call(Subscriber<? super List<NewsBean>> subscriber) {
                subscriber.onNext(body.getRetData().getData());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<List<NewsBean>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d(LogUtil.TAG_DB, "导入到数据库完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(LogUtil.TAG_DB, "导入到数据库错误" + e.getMessage());
                    }

                    @Override
                    public void onNext(List<NewsBean> newsBeen) {
                        NewsDao dao = NewsDao.getInstance();
                        Map temp = params;
                        temp.put("has_more", String.valueOf(body.getRetData().getHas_more()));
                        dao.insert(temp, newsBeen);
                    }
                });


    }


}
