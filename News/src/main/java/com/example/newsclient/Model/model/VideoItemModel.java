package com.example.newsclient.Model.model;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.SysParam;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.VideoItemModelImpl;
import com.example.newsclient.Model.utils.YouKuUtil;
import com.google.gson.Gson;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-05-11.
 */
public class VideoItemModel implements VideoItemModelImpl {
    @Override
    public void loadVideoItemData(String id, Observer<VideoItemBean> os) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.youku.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        service.loadVideoItem(Configuration.YOUKU_KEYWORD
                , id, "show")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(os);
    }

    @Override
    public void loadVideoM8u3(final String id, Observer<String> os) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://openapi.youku.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        final ApiService service = retrofit.create(ApiService.class);


        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                SysParam param = YouKuUtil.getInstance().getParam();
                Gson gson = new Gson();
                String s = gson.toJson(param);
                subscriber.onNext(s);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return service.loadVideoM8u3(s, id);
                    }
                }).subscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(os);
    }
}
