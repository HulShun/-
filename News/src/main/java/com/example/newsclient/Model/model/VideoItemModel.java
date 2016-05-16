package com.example.newsclient.Model.model;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.video.CommentsJsonBean;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.VideoItemModelImpl;

import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
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
        service.loadVideoItem(Configuration.YOUKU_CLIENT_ID
                , id, "show")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(os);
    }

    @Override
    public void loadComments(Map<String, String> map, Observer<CommentsJsonBean> os) {

    }


}
