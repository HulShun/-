package com.example.newsclient.Model.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.bean.video.CommentsJsonBean;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.VideoItemModelImpl;
import com.squareup.okhttp.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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

    @Override
    public void getImage(String thumbnail, Observer<Bitmap> observer) {

    }

    @Override
    public void shareToWeibo(VideoItemBean videoData, Observer<Bitmap> observer) {

        //加载图片
        Retrofit retrofit = new Retrofit.Builder().baseUrl(videoData.getThumbnail())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        LogUtil.d("http_img", videoData.getThumbnail());
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


}
