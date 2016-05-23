package com.example.newsclient.Model.model;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.video.Commentsv2JsonBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.CommentsModelImpl;

import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-05-16.
 */
public class CommentsModel implements CommentsModelImpl {
    @Override
    public void loadCommentsFromNet(final Map<String, String> params, Observer<Commentsv2JsonBean> observer) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Configuration.YOUKU_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        final ApiService service = retrofit.create(ApiService.class);

        String vid = params.get("vid");
        String page = String.valueOf(params.get("page"));
        service.loadVideoComments(Configuration.YOUKU_CLIENT_ID, vid, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);


      /*  Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                YouKuUtil util = YouKuUtil.getInstance();
                SysParam sysparam = util.getParam();
                sysparam.setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));
                sysparam.setAction("youku.content.comment.bycategory.get");

                Map<String, String> map = new HashMap<String, String>();
                map.put("action", sysparam.getAction());
                map.put("timestamp", sysparam.getTimestamp());
                map.put("version", sysparam.getVersion());
                map.put("client_id", sysparam.getClient_id());

                map.put("vid", params.get("vid"));
                //map.put("pg", String.valueOf(params.get("page")));

                String result = null;
                try {
                    result = util.signApiRequest(map, Configuration.YOUKU_SECRET);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sysparam.setSign(result);
                Gson gson = new Gson();
                String json = gson.toJson(sysparam);
                subscriber.onNext(json);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<CommentsJsonBean>>() {
                    @Override
                    public Observable<CommentsJsonBean> call(String s) {
                        return service.loadVideoComments(s, params.get("vid"), params.get("pg"));
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);*/
    }
}
