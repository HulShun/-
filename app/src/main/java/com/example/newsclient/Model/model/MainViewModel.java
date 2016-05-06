package com.example.newsclient.Model.model;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.ImageTypeJsonBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.MainViewModelImpl;
import com.example.newsclient.Model.utils.RetrofitUtil;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-05-05.
 */
public class MainViewModel implements MainViewModelImpl {
    @Override
    public void getImageTabs(String url, Observer<ImageTypeJsonBean> os) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://apis.baidu.com/showapi_open_bus/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(RetrofitUtil.createClinet("apikey", Configuration.HTTP_ARG))
                .build();
        ApiService service = retrofit.create(ApiService.class);
        service.loadImageTypes()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(os);
    }
}
