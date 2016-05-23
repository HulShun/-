package com.example.newsclient.Model.model;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.image.ImageJsonBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.ImageListModelImpl;
import com.example.newsclient.Model.utils.RetrofitUtil;

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
public class ImageListModel implements ImageListModelImpl {

    @Override
    public void getImageDatas(Map<String, Integer> parmas, Observer<ImageJsonBean> os) {
        Integer type = parmas.get("type");
        Integer page = parmas.get("page");

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://apis.baidu.com/showapi_open_bus/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(RetrofitUtil.createClinet("apikey", Configuration.HTTP_ARG))
                .build();

        ApiService service = retrofit.create(ApiService.class);
        service.loadImages(String.valueOf(type), String.valueOf(page))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(os);
    }


}
