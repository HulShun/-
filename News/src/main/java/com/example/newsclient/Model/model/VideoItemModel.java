package com.example.newsclient.Model.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.video.CommentsJsonBean;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.VideoItemModelImpl;
import com.example.newsclient.dao.VideoDao;
import com.squareup.okhttp.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
 * Created by Administrator on 2016-05-11.
 */
public class VideoItemModel implements VideoItemModelImpl {
    @Override
    public void loadVideoItemData(String id, int mode, Observer<VideoItemBean> os) {
        //网络获取
        if (mode == ModelMode.INTERNET) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://openapi.youku.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            ApiService service = retrofit.create(ApiService.class);
            service.loadVideoItem(Configuration.YOUKU_CLIENT_ID
                    , id, "show")
                    .subscribeOn(Schedulers.newThread())
                    .map(new Func1<VideoItemBean, VideoItemBean>() {
                        @Override
                        public VideoItemBean call(VideoItemBean videoItemBean) {
                            saveVideoDataToDB(videoItemBean);
                            return videoItemBean;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(os);
        } else {
            //本地获取
            Observable.just(id)
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<String, VideoItemBean>() {
                        @Override
                        public VideoItemBean call(String s) {
                            VideoDao dao = VideoDao.getInstance();
                            VideoItemBean data = dao.query(s);
                            return data;
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(os);
        }

    }

    @Override
    public void loadComments(Map<String, String> map, Observer<CommentsJsonBean> os) {

    }

    @Override
    public void getImage(String thumbnail, Observer<Bitmap> observer) {
        //加载图片
        Retrofit retrofit = new Retrofit.Builder().baseUrl(thumbnail)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        LogUtil.d("http_img", thumbnail);
        service.loadImage("")
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .map(new Func1<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap call(ResponseBody responseBody) {
                        ByteArrayOutputStream os = null;
                        Bitmap bitmap = null;
                        try {
                            InputStream in = responseBody.byteStream();
                            bitmap = BitmapFactory.decodeStream(in);
                            //压缩图片
                            os = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
                            System.out.println("Weibo_th    size  " + os.toByteArray().length);
                            while (os.toByteArray().length > 32 * 1024) {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
                                System.out.println("Weibo_th    size  " + os.toByteArray().length);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (os != null) {
                                    os.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return bitmap;
                    }
                })
                .subscribe(observer);
    }

    @Override
    public void saveVideoDataToDB(VideoItemBean videoItemBean) {
        VideoDao dao = VideoDao.getInstance();
        //先删除旧数据
        dao.deleteVideoDetailByKey(videoItemBean.getId());
        dao.insertVideoDetail(videoItemBean);
        LogUtil.d("video", "video数据保存到数据库完成");
    }


}
