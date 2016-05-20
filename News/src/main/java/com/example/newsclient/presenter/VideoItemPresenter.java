package com.example.newsclient.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.impl.VideoItemModelImpl;
import com.example.newsclient.Model.model.VideoItemModel;
import com.example.newsclient.R;
import com.example.newsclient.view.activity.VideoItemActivity;
import com.example.newsclient.view.impl.IVideoItemViewImpl;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.mm.sdk.openapi.IWXAPI;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-11.
 */
public class VideoItemPresenter extends BasePresenter<IVideoItemViewImpl, VideoItemModelImpl> {
    @Override
    protected VideoItemModelImpl instanceModel() {
        return new VideoItemModel();
    }


    public void loadVideoData(String id) {
        getModel().loadVideoItemData(id, new Observer<VideoItemBean>() {
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

    public void shareToWechat(IWXAPI mWechatAPI, VideoItemBean videoData) {
        getModel().getImage(videoData.getThumbnail(), new Observer<Bitmap>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Bitmap bitmap) {

            }
        });

    }

    public void shareToWeibo(final VideoItemActivity videoItemActivity, VideoItemBean videoData) {
        final VideoObject videoObject = new VideoObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = videoData.getTitle();
        videoObject.description = videoData.getDescription();
        videoObject.defaultText = videoData.getDescription();
        videoObject.dataHdUrl = videoData.getLink();
        videoObject.dataUrl = videoData.getLink();
        videoObject.actionUrl = videoData.getLink();
        float d = Float.parseFloat(videoData.getDuration());
        videoObject.duration = (int) d;

        getModel().shareToWeibo(videoData, new Observer<Bitmap>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Bitmap bitmap = BitmapFactory.decodeResource(videoItemActivity.getResources(), R.mipmap.ic_launcher);
                videoObject.setThumbImage(bitmap);
                if (getView().isVisiable()) {
                    getView().readyShareToWeibo(videoObject);
                }
            }

            @Override
            public void onNext(Bitmap bitmap) {
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(videoItemActivity.getResources(), R.mipmap.ic_launcher);
                }
                videoObject.setThumbImage(bitmap);
                if (getView().isVisiable()) {
                    getView().readyShareToWeibo(videoObject);
                }
            }
        });
    }
}
