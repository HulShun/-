package com.example.newsclient.presenter;

import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.impl.RecommendJsonVideoBean;
import com.example.newsclient.Model.impl.VideoBriefModelImpl;
import com.example.newsclient.Model.model.VideoBriefModel;
import com.example.newsclient.view.impl.IVideoBriefViewImpl;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-17.
 */
public class VideoBriefPresenter extends BasePresenter<IVideoBriefViewImpl, VideoBriefModelImpl> {
    @Override
    protected VideoBriefModelImpl instanceModel() {
        return new VideoBriefModel();
    }


    public void loadVideoData(String vid) {

        getModel().loadVideoData(vid, new Observer<VideoItemBean>() {
            @Override
            public void onCompleted() {
                if (getView().isVisiable()) {
                    getView().showSuccess();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (getView().isVisiable()) {
                    getView().showFaild(e.getMessage());
                    LogUtil.d("video", "video获取失败：" + e.getMessage());
                }
            }

            @Override
            public void onNext(VideoItemBean videoItemBean) {
                if (getView().isVisiable()) {
                    getView().onVideoDataResult(videoItemBean);
                }
            }
        });
    }

    public void loadRecommendVieoData(String vid) {
        getModel().loadRecommendVideoData(vid, new Observer<RecommendJsonVideoBean>() {
            @Override
            public void onCompleted() {
                if (getView().isVisiable()) {
                    getView().showSuccess();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (getView().isVisiable()) {
                    getView().showFaild(e.getMessage());
                    LogUtil.d("video", "video获取失败：" + e.getMessage());
                }
            }

            @Override
            public void onNext(RecommendJsonVideoBean recommendJsonVideoBean) {
                if (getView().isVisiable()) {
                    getView().onRecommendVideoResult(recommendJsonVideoBean);
                }
            }
        });
    }
}
