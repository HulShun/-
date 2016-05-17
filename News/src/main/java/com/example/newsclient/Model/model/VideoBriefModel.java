package com.example.newsclient.Model.model;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.RecommendJsonVideoBean;
import com.example.newsclient.Model.impl.VideoBriefModelImpl;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-05-17.
 */
public class VideoBriefModel implements VideoBriefModelImpl {
    @Override
    public void loadVideoData(String vid, Observer<VideoItemBean> observer) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.youku.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        service.loadVideoItem(Configuration.YOUKU_CLIENT_ID
                , vid, "show")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void loadRecommendVideoData(String vid, Observer<RecommendJsonVideoBean> observer) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Configuration.YOUKU_API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        service.loadRecommendVideo(Configuration.YOUKU_CLIENT_ID, vid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


}
