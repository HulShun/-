package com.example.newsclient.presenter;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.bean.video.VideosInFormBean;
import com.example.newsclient.Model.impl.VideoClassifyModelImpl;
import com.example.newsclient.Model.model.VideoClassifyModel;
import com.example.newsclient.view.impl.IVideoClassifyViewIpml;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-09.
 */
public class VideoClassifyPresenter extends BasePresenter<IVideoClassifyViewIpml, VideoClassifyModelImpl> {
    @Override
    protected VideoClassifyModelImpl instanceModel() {
        return new VideoClassifyModel();
    }


    public void loadVideoTypeList(String label, String period, final int page) {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", Configuration.YOUKU_KEYWORD);
        map.put("category", label);
        map.put("period", "today");
        map.put("orderby", "view-count");
        map.put("page", String.valueOf(page));
        map.put("count", String.valueOf(20));

        if (period == null) {
            getModel().loadVideosInform(map, new Observer<VideosInFormBean>() {
                @Override
                public void onCompleted() {
                    if (getView().isVisiable()) {
                        getView().onCompleted();
                        getView().showSuccess();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (getView().isVisiable()) {
                        getView().onCompleted();
                        getView().showFaild(e.getMessage());
                        LogUtil.d("videos", "videos错误日志：" + e.getMessage());
                    }
                }

                @Override
                public void onNext(VideosInFormBean videosInFormBean) {
                    if (getView().isVisiable()) {
                        if (page == 1) {
                            getView().onUpdateVideos(videosInFormBean);
                        } else {
                            getView().onloadMoreVideos(videosInFormBean);
                        }

                    }
                }
            });
        }

    }
}
