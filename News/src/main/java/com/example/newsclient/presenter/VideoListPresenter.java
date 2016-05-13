package com.example.newsclient.presenter;

import com.example.newsclient.Model.bean.video.VideoListBean;
import com.example.newsclient.Model.impl.VideosModelImpl;
import com.example.newsclient.Model.model.VideosModel;
import com.example.newsclient.view.impl.IVideoViewImpl;

import java.util.Map;

/**
 * Created by Administrator on 2016-05-04.
 */
public class VideoListPresenter extends BasePresenter<IVideoViewImpl, VideosModelImpl> {

    public VideoListPresenter() {
    }

    @Override
    protected VideosModelImpl instanceModel() {
        return new VideosModel();
    }


    public void getVideos(Map<String, String> params) {
        if (getView().checkNetWork()) {
            getView().onRefreshComplete();
            getView().showNoNetWork();
            return;
        }

        getModel().getVideos(params, new rx.Observer<VideoListBean>() {
            @Override
            public void onCompleted() {
                if (getView().isVisiable()) {
                    getView().showSuccess();
                    getView().onRefreshComplete();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (getView().isVisiable()) {
                    getView().onRefreshComplete();
                    getView().showFaild(e.getMessage());
                }
            }

            @Override
            public void onNext(VideoListBean videoListBean) {
                if (getView().isVisiable()) {
                    getView().updateDatas(videoListBean);
                    getView().onRefreshComplete();
                    getView().showSuccess();
                }
            }
        });
    }
}
