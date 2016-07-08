package com.example.newsclient.presenter;

import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.impl.VideoItemModelImpl;
import com.example.newsclient.Model.model.VideoItemModel;
import com.example.newsclient.view.impl.IVideoItemViewImpl;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-11.
 */
public class VideoItemPresenter extends BasePresenter<IVideoItemViewImpl, VideoItemModelImpl> {
    /*数据过期时间3分钟*/
    private static final int PAST_TIME = 3 * 60 * 60;

    @Override
    protected VideoItemModelImpl instanceModel() {
        return new VideoItemModel();
    }


    public void loadVideoData(final String id, final int mode) {
        if (mode == ModelMode.INTERNET && !getView().checkNetWork()) {
            getView().showNoNetWork();
            return;
        }

        getModel().loadVideoItemData(id, mode, new Observer<VideoItemBean>() {
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
                    //本地加载之后(在有网情况)，出现空数据，时间戳空，数据过期情况，就去网络加载
                    if (mode == ModelMode.LOCAL &&
                            getView().checkNetWork() &&
                            (videoItemBean == null ||
                                    videoItemBean.getUpdataTime() == null ||
                                    (System.currentTimeMillis() / 1000) - Long.valueOf(videoItemBean.getUpdataTime()) > PAST_TIME)) {
                        loadVideoData(id, ModelMode.INTERNET);
                    } else {
                        getView().loadVideoItemInform(videoItemBean);
                    }

                }
            }
        });


    }


}
