package com.example.newsclient.Model.model;

import com.example.newsclient.Model.bean.VideoListBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.VideosModelImpl;

import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-05-04.
 */
public class VideosModel implements VideosModelImpl {
    @Override
    public void getVideos(Map<String, String> params, Observer<VideoListBean> os) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://openapi.youku.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        service.loadVideos(params.get("opensysparams"), params.get("q"), params.get("fd"), params.get("cl"))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(os);
    }


}
