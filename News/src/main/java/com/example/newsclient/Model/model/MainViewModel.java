package com.example.newsclient.Model.model;

import android.app.Activity;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.bean.QQUserInfro;
import com.example.newsclient.Model.bean.TencentOpenBean;
import com.example.newsclient.Model.bean.WeiboUserInfo;
import com.example.newsclient.Model.bean.image.ImageMainTypeBean;
import com.example.newsclient.Model.bean.image.ImageTypeJsonBean;
import com.example.newsclient.Model.bean.video.VideoTypeBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.MainViewModelImpl;
import com.example.newsclient.Model.utils.RetrofitUtil;
import com.example.newsclient.Model.utils.TencentUtil;
import com.example.newsclient.dao.ImagesDao;
import com.example.newsclient.dao.VideoTypeDao;
import com.example.newsclient.view.utils.AppUtil;
import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-05-05.
 */
public class MainViewModel implements MainViewModelImpl {
    @Override
    public void getImageTabsFromNet(String url) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://apis.baidu.com/showapi_open_bus/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(RetrofitUtil.createClinet("apikey", Configuration.HTTP_ARG))
                .build();
        ApiService service = retrofit.create(ApiService.class);
        service.loadImageTypes()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.immediate())
                .subscribe(new Observer<ImageTypeJsonBean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d("type", "type数据放入数据库成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("type", "type数据放入数据库失败");
                    }

                    @Override
                    public void onNext(ImageTypeJsonBean imageTypeJsonBean) {
                        //Type的dao包
                        ImagesDao typeDao = ImagesDao.getInstance();
                        typeDao.deleteAll();
                        //将数据放入数据库中
                        typeDao.insertMainType(imageTypeJsonBean.getShowapi_res_body().getList());
                    }
                });
    }

    @Override
    public void getImageTabsFromLocal(Observer<ImageTypeJsonBean> os) {
        Observable.create(new Observable.OnSubscribe<ImageTypeJsonBean>() {
            @Override
            public void call(Subscriber<? super ImageTypeJsonBean> subscriber) {
                //Type的dao包
                ImagesDao typeDao = ImagesDao.getInstance();
                //数据库中获取所有的type
                List<ImageMainTypeBean> maintypes = typeDao.queryMainType();
                //放到ImageTpyeJsonBean中
                ImageTypeJsonBean bean = new ImageTypeJsonBean();
                ImageTypeJsonBean.ShowapiResBodyBean body = new ImageTypeJsonBean.ShowapiResBodyBean();
                body.setList(maintypes);
                bean.setShowapi_res_body(body);

                subscriber.onNext(bean);
                subscriber.onCompleted();
            }
        }).observeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(os);
    }

    @Override
    public void getVideoTabsFromNet(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        service.loadVideoType()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.immediate())
                .subscribe(new Observer<VideoTypeBean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d("type", "videosTab数据放入数据库成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("type", "videosTab数据放入数据库失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(VideoTypeBean videoTypeBean) {
                        VideoTypeDao dao = VideoTypeDao.getInstance();
                        dao.deleteAll();
                        dao.insert(videoTypeBean.getCategories());
                    }
                });
    }

    @Override
    public void getVideoTabsFromLocal(Observer<VideoTypeBean> os) {
        Observable.create(new Observable.OnSubscribe<VideoTypeBean>() {
            @Override
            public void call(Subscriber<? super VideoTypeBean> subscriber) {
                VideoTypeDao dao = VideoTypeDao.getInstance();
                List<VideoTypeBean.VideoCategoriesBean> mainTypeBeans = dao.query();
                VideoTypeBean bean = new VideoTypeBean();
                bean.setCategories(mainTypeBeans);
                subscriber.onNext(bean);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(os);
    }

    @Override
    public void saveQQInfo(JSONObject values, final Activity context) {

        Observable.just(values.toString())
                .map(new Func1<String, TencentOpenBean>() {
                    @Override
                    public TencentOpenBean call(String s) {
                        Gson gson = new Gson();
                        TencentOpenBean data = gson.fromJson(s, TencentOpenBean.class);
                        return data;
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.immediate())
                .subscribe(new Action1<TencentOpenBean>() {
                    @Override
                    public void call(TencentOpenBean tencentOpenBean) {
                        TencentUtil.saveToken(context, tencentOpenBean);

                        AppUtil.getInstance().saveToShareRefence("login", "qq");
                    }
                });
    }


    @Override
    public void getQQUserInfo(final UserInfo userInfo, final Observer<QQUserInfro> observer) {
        //写一个用户信息储存本地数据库——————————先从本地取用户数据，再联网拿新的用户数据


        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Observable.just(o.toString())
                        .flatMap(new Func1<String, Observable<QQUserInfro>>() {
                            @Override
                            public Observable<QQUserInfro> call(String s) {
                                Gson gson = new Gson();
                                QQUserInfro infro = gson.fromJson(s, QQUserInfro.class);
                                if (infro.getRet() == -1) {
                                    return Observable.error(new Throwable(infro.getMsg()));
                                }
                                return Observable.just(infro);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer);
            }

            @Override
            public void onError(final UiError uiError) {
                Observable.just(1)
                        .flatMap(new Func1<Integer, Observable<QQUserInfro>>() {
                            @Override
                            public Observable<QQUserInfro> call(Integer integer) {
                                return Observable.error(new Throwable(uiError.errorMessage));
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void getWeiboUserInfo(String token, String uid, Observer<WeiboUserInfo> observer) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weibo.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit.create(ApiService.class)
                .loadWeiboUserInfo(token, uid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


}
