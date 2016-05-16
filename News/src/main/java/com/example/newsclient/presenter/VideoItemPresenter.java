package com.example.newsclient.presenter;

import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.bean.video.CommentsJsonBean;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.impl.VideoItemModelImpl;
import com.example.newsclient.Model.model.VideoItemModel;
import com.example.newsclient.view.impl.IVideoItemViewImpl;

import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-11.
 */
public class VideoItemPresenter extends BasePresenter<IVideoItemViewImpl, VideoItemModelImpl> {
    @Override
    protected VideoItemModelImpl instanceModel() {
        return new VideoItemModel();
    }


    public void loadVideoData(Map<String, String> map) {
        getModel().loadVideoItemData(map.get("id"), new Observer<VideoItemBean>() {
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
                    getView().loadVideoItemInform(videoItemBean);
                }
            }
        });
    }

    public void loadCommentsData(Map<String, String> map) {

        getModel().loadComments(map, new Observer<CommentsJsonBean>() {
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
                    getView().onCompleted();
                }
            }

            @Override
            public void onNext(CommentsJsonBean commentsJsonBean) {
                if (getView().isVisiable()) {

                }
            }
        });
    }
}
