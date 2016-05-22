package com.example.newsclient.presenter;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.ModelMode;
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
    /*数据过期时间三分钟*/
    private static final int PAST_TIEM = 3 * 60 * 60;

    @Override
    protected VideoClassifyModelImpl instanceModel() {
        return new VideoClassifyModel();
    }


    public void loadVideoTypeList(final String label, final String period, final int page, final int mode) {
        if (mode == ModelMode.INTERNET && !getView().checkNetWork()) {
            getView().showNoNetWork();
            return;
        }

        final Map<String, String> map = new HashMap<>();
        map.put("client_id", Configuration.YOUKU_CLIENT_ID);
        map.put("category", label);
        map.put("period", "today");
        map.put("orderby", "view-count");
        map.put("page", String.valueOf(page));
        map.put("count", String.valueOf(20));

        if (period == null) {
            Observer observser = new Observer<VideosInFormBean>() {
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
                        //如果数据空，或者缓存过期并且有网，重新网络加载
                        if (videosInFormBean != null) {
                            LogUtil.d("video", "时间戳：" + videosInFormBean.getUpdateTime());
                        }
                        //本地加载之后(在有网情况)，出现空数据，时间戳空，数据过期情况，就去网络加载
                        if (mode == ModelMode.LOCAL &&
                                getView().checkNetWork() &&
                                  (videosInFormBean == null ||
                                        videosInFormBean.getUpdateTime() == null ||
                                        (System.currentTimeMillis() / 1000 - Long.parseLong(videosInFormBean.getUpdateTime()) > PAST_TIEM))
                                ) {
                            loadVideoTypeList(label, period, page, ModelMode.INTERNET);
                        } else if (page == 1) {
                            getView().onUpdateVideos(videosInFormBean);
                        } else {
                            getView().onloadMoreVideos(videosInFormBean);
                        }

                    }
                }
            };


            getModel().loadVideosInform(map, mode, observser);
        }

    }
}
