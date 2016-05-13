package com.example.newsclient.Model.model;

import com.android.volley.toolbox.ImageLoader;
import com.example.newsclient.Configuration;
import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.image.ImageJsonBean;
import com.example.newsclient.Model.bean.image.ImageTypeBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.ImageClassifyModelImpl;
import com.example.newsclient.Model.utils.MyImageLoader;
import com.example.newsclient.Model.utils.RetrofitUtil;
import com.example.newsclient.dao.ImagesDao;

import java.util.List;
import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-05-05.
 */
public class ImageClassifyModel implements ImageClassifyModelImpl {

    @Override
    public void onLoadBitmap(String url, ImageLoader.ImageListener l) {
        MyImageLoader.getImageLoader().get(url, l, 500, 500);
    }


    @Override
    public void getImageDatas(int mode, final List<ImageTypeBean> typeBeans, Observer<ImageJsonBean> os) {
        String types[] = new String[typeBeans.size()];
        int i = 0;
        for (ImageTypeBean bean : typeBeans) {
            types[i] = String.valueOf(bean.getId());
            i++;
        }

        if (mode == ModelMode.INTERNET) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://apis.baidu.com/showapi_open_bus/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(RetrofitUtil.createClinet("apikey", Configuration.HTTP_ARG))
                    .build();
            final ApiService service = retrofit.create(ApiService.class);

            LogUtil.d("type", "需要网络获取type有：\"" + types.length + "\"个数据");
            Observable.from(types)
                    .flatMap(new Func1<String, Observable<ImageJsonBean>>() {
                        @Override
                        public Observable<ImageJsonBean> call(String s) {
                            LogUtil.d("type", "网络获取type为：\"" + s + "\"的数据");
                            return service.loadImages(s, String.valueOf(1));
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(os);

        } else {
            //本地数据查询
            Observable.from(types)
                    .map(new Func1<String, ImageJsonBean>() {
                        @Override
                        public ImageJsonBean call(String s) {
                            LogUtil.d("type", "本地获取type为：\"" + s + "\"的数据");
                            ImageJsonBean jsonBean = null;
                            ImagesDao dao = ImagesDao.getInstance();
                            ImageJsonBean.ShowapiResBodyBean.PagebeanBean pagebean = dao.queryImageContent(s);
                            if (pagebean != null) {
                                jsonBean = new ImageJsonBean();
                                ImageJsonBean.ShowapiResBodyBean body = new ImageJsonBean.ShowapiResBodyBean();
                                body.setPagebean(pagebean);
                                jsonBean.setShowapi_res_body(body);
                            }
                            return jsonBean;
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(os);
        }

    }

    @Override
    public void saveIntoDB(final ImageJsonBean jsonBean) {
        Observable.just(jsonBean)
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ImageJsonBean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d("tabs", jsonBean.getShowapi_res_body().getPagebean().getContentlist().get(0).getType() + "相册信息导入数据库成功。");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("tabs", jsonBean.getShowapi_res_body().getPagebean().getContentlist().get(0).getType() + "相册信息导入数据库失败：" + e.getMessage());
                    }

                    @Override
                    public void onNext(ImageJsonBean jsonBean) {
                        ImagesDao dao = ImagesDao.getInstance();
                        dao.insertImageInform(jsonBean);
                    }

                });
    }


    @Override
    public void deleteDataInDB(Map<String, String> map) {
        Observable.just(map)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Map<String, String>>() {
                    @Override
                    public void call(Map<String, String> map) {
                        ImagesDao dao = ImagesDao.getInstance();
                        int m = dao.deleteImage(map);
                        LogUtil.d("type", "删除了数据库中：" + map.get("type") + "的数据：" + m);
                    }
                });

    }
}
