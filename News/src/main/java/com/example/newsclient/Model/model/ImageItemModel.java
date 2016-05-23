package com.example.newsclient.Model.model;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.image.ImageJsonBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.ImageItemModelImpl;
import com.example.newsclient.Model.utils.RetrofitUtil;

import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/22.
 */
public class ImageItemModel implements ImageItemModelImpl {
    @Override
    public void getImageDatas(Map<String, String> parmas, Observer<ImageJsonBean> os) {
        String type = parmas.get("type");
        String page = parmas.get("page");

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://apis.baidu.com/showapi_open_bus/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(RetrofitUtil.createClinet("apikey", Configuration.HTTP_ARG))
                .build();

        ApiService service = retrofit.create(ApiService.class);
        service.loadImages(type, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(os);
    }
}
