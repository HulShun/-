package com.example.newsclient.presenter;

import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.bean.video.Commentsv2JsonBean;
import com.example.newsclient.Model.impl.CommentsModelImpl;
import com.example.newsclient.Model.model.CommentsModel;
import com.example.newsclient.view.impl.ICommentsViewImpl;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-16.
 */
public class CommentsPresenter extends BasePresenter<ICommentsViewImpl, CommentsModelImpl> {
    @Override
    protected CommentsModelImpl instanceModel() {
        return new CommentsModel();
    }


    public void loadComments(String vid, int page) {
        Map<String, String> map = new HashMap<>();
        map.put("vid", vid);
        map.put("page", String.valueOf(page));
        getModel().loadCommentsFromNet(map, new Observer<Commentsv2JsonBean>() {
            @Override
            public void onCompleted() {
                if (getView().isVisiable()) {
                    getView().showSuccess();
                    getView().onCompleted();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (getView().isVisiable()) {
                    getView().showFaild(e.getMessage());
                    LogUtil.d("url_comments", "获取评论错误:" + e.getMessage());
                    getView().onCompleted();
                }
            }

            @Override
            public void onNext(Commentsv2JsonBean data) {
                if (getView().isVisiable()) {
                    if (data == null) {
                        getView().showFaild("获取失败");
                        return;
                    }
                    getView().onCommentsResulte(data);
                }
            }
        });
    }
}
