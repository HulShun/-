package com.example.newsclient.Model.model;

import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.video.VideosInFormBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.VideoClassifyModelImpl;
import com.example.newsclient.dao.VideoDao;

import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-05-09.
 */
public class VideoClassifyModel implements VideoClassifyModelImpl {

    @Override
    public void loadVideosInform(final Map<String, String> map, int mode, Observer<VideosInFormBean> os) {
        //网络加载
        if (mode == ModelMode.INTERNET) {
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
                    .map(new Func1<VideosInFormBean, VideosInFormBean>() {
                        @Override
                        public VideosInFormBean call(VideosInFormBean videosInFormBean) {
                            VideoDao dao = VideoDao.getInstance();
                            String title = map.get("category");
                            dao.deleteVideosByTitle(title);
                            dao.insertVideos(title, videosInFormBean);
                            LogUtil.d("video", "videos插入数据库完成");
                            return videosInFormBean;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(os);
        } else {
            //本地加载
            Observable.just(map)
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<Map<String, String>, VideosInFormBean>() {
                        @Override
                        public VideosInFormBean call(Map<String, String> stringStringMap) {
                            VideoDao dao = VideoDao.getInstance();
                            VideosInFormBean data = dao.queryVideos(map.get("category"), map.get("page"));
                            return data;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(os);
        }
    }
}
