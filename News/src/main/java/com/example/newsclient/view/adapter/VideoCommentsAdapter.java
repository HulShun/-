package com.example.newsclient.view.adapter;

import android.view.View;
import android.widget.ImageView;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.bean.SysParam;
import com.example.newsclient.Model.bean.video.Commentsv2JsonBean;
import com.example.newsclient.Model.bean.video.VideoCommentUserBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.utils.YouKuUtil;
import com.example.newsclient.R;
import com.example.newsclient.view.viewholder.VideoCommentsViewHolder;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-05-18.
 */
public class VideoCommentsAdapter extends BaseRecyclerViewAdapter<Commentsv2JsonBean.CommentBean, VideoCommentsViewHolder> {
    @Override
    public int getFooterLayoutId() {
        return R.layout.fragment_rv_footer;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.fragment_comments_item;
    }

    @Override
    protected VideoCommentsViewHolder onCreateMyViewHolder(View view, int viewType) {
        return new VideoCommentsViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(VideoCommentsViewHolder holder, int position) {
        Commentsv2JsonBean.CommentBean bean = getData().get(position);

        holder.userNameView.setText(bean.getUser().getName());
        holder.contentView.setText(bean.getContent());
        holder.timeText.setText(bean.getPublished());

        setUpImageView(bean.getUser().getId(), holder.userImg);
    }

    private void setUpImageView(String id, final ImageView userImg) {
        final String q = "personid:" + id;
        final String fd = "thumburl";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Configuration.YOUKU_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        final ApiService service = retrofit.create(ApiService.class);
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                YouKuUtil util = YouKuUtil.getInstance();
                SysParam sysParam = util.getParam();
                sysParam.setAction("youku.content.performer.singal.detail.get");
                sysParam.setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));

                Map<String, String> map = new HashMap<String, String>();
                map.put("action", sysParam.getAction());
                map.put("client_id", sysParam.getClient_id());
                map.put("timestamp", sysParam.getTimestamp());
                map.put("version", sysParam.getVersion());
                map.put("q", q);
                map.put("fd", fd);
                String sign;
                try {
                    sign = util.signApiRequest(map, Configuration.YOUKU_SECRET);
                    sysParam.setSign(sign);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                String srt = gson.toJson(sysParam);
                subscriber.onNext(srt);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<VideoCommentUserBean>>() {
                    @Override
                    public Observable<VideoCommentUserBean> call(String s) {
                        return service.loadCommentUser(s, q, fd);
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoCommentUserBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("comments", "获取评论图片错误：" + e.getMessage());
                    }

                    @Override
                    public void onNext(VideoCommentUserBean videoCommentUserBean) {
                        Picasso.with(getContext())
                                .load(videoCommentUserBean.getAvatar())
                                .error(R.drawable.comments_default_img)
                                .placeholder(R.drawable.comments_default_img)
                                .tag(videoCommentUserBean.getAvatar())
                                .into(userImg);
                    }
                });
    }

    @Override
    protected List<Commentsv2JsonBean.CommentBean> initdatas() {
        return new ArrayList<>();
    }
}
