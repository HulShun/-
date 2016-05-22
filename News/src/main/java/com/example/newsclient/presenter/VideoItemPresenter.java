package com.example.newsclient.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.impl.TencentBaseUIListenner;
import com.example.newsclient.Model.impl.VideoItemModelImpl;
import com.example.newsclient.Model.model.VideoItemModel;
import com.example.newsclient.R;
import com.example.newsclient.view.impl.IVideoItemViewImpl;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXVideoObject;
import com.tencent.tauth.Tencent;

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

    public void shareToWechat(final Context c, final IWXAPI mWechatAPI, final VideoItemBean videoData, final int flage) {
        //视频分享
        WXVideoObject wxVideoObject = new WXVideoObject();
        wxVideoObject.videoUrl = videoData.getLink();

        final WXMediaMessage mediaMessage = new WXMediaMessage(wxVideoObject);
        mediaMessage.title = videoData.getTitle();
        mediaMessage.description = videoData.getDescription();

        getModel().getImage(videoData.getThumbnail(), new Observer<Bitmap>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Bitmap thumb = BitmapFactory.decodeResource(c.getResources(), R.mipmap.ic_launcher);
                mediaMessage.setThumbImage(thumb);
                startShareWechat(mWechatAPI, videoData, flage, mediaMessage);
            }

            @Override
            public void onNext(Bitmap bitmap) {
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(c.getResources(), R.mipmap.ic_launcher);
                }
                mediaMessage.setThumbImage(bitmap);
                startShareWechat(mWechatAPI, videoData, flage, mediaMessage);
            }
        });

    }

    private void startShareWechat(IWXAPI mWechatAPI, VideoItemBean videoData, int flage, WXMediaMessage mediaMessage) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = videoData.getTitle() + System.currentTimeMillis();
        req.message = mediaMessage;
        //分享到聊天窗口或者是朋友圈
        req.scene = flage;
        mWechatAPI.sendReq(req);
    }

    public void prepareShareToWeibo(final Activity activity, final IWeiboShareAPI api, VideoItemBean videoData) {
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

        getModel().getImage(videoData.getThumbnail(), new Observer<Bitmap>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
                videoObject.setThumbImage(bitmap);
                if (getView().isVisiable()) {
                    shareToWeibo(activity, api, videoObject);
                }
            }

            @Override
            public void onNext(Bitmap bitmap) {
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
                }
                videoObject.setThumbImage(bitmap);
                if (getView().isVisiable()) {
                    shareToWeibo(activity, api, videoObject);
                }
            }
        });
    }

    public void shareToQQ(Activity activity, VideoItemBean videoData) {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, videoData.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, videoData.getDescription());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, videoData.getLink());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, videoData.getThumbnail());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getResources().getString(R.string.app_name));
        Tencent tencent = Tencent.createInstance(com.example.newsclient.Configuration.TENCENT_APPID, activity);
        tencent.shareToQQ(activity, params, new TencentBaseUIListenner());
    }


    private void shareToWeibo(Activity activity, IWeiboShareAPI api, VideoObject videoObject) {
        WeiboMultiMessage multiMessage = new WeiboMultiMessage();

        multiMessage.mediaObject = videoObject;
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = multiMessage;
        //查看日志
        //  com.sina.weibo.sdk.utils.LogUtil.sIsLogEnable = true;
        api.sendRequest(activity, request);
    }

}
