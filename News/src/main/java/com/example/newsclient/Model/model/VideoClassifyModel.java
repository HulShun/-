package com.example.newsclient.Model.model;

import com.example.newsclient.Model.bean.VideosInFormBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.VideoClassifyModelImpl;

import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-05-09.
 */
public class VideoClassifyModel implements VideoClassifyModelImpl {

    @Override
    public void loadVideosInform(Map<String, String> map, Observer<VideosInFormBean> os) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://openapi.youku.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        service.loadVideosInform(map.get("client_id"),
                map.get("category"),
                map.get("period"),
                map.get("orderby"),
                map.get("page"),
                map.get("count")
        ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(os);
    }
}
