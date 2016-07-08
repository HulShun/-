package com.example.newsclient.Model.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.bean.TencentOpenBean;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.impl.ApiService;
import com.example.newsclient.Model.impl.TencentBaseUIListenner;
import com.example.newsclient.R;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.utils.Utility;
import com.squareup.okhttp.ResponseBody;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXVideoObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-07-06.
 */
public class LoginAndShareManager {
    private static LoginAndShareManager instance;

    /*QQ登陆*/
    private Tencent mTencent;
    private IUiListener mTencentListener;
    /*微博登陆*/
    private AuthInfo mWeiBoAuthInfo;
    /*SSO即客户端登陆模式*/
    private SsoHandler mWeiboSsonHandler;
    /*微博的token登录信息*/
    private Oauth2AccessToken weiboToken;
    private IWeiboShareAPI mWeiboShareAPI;
    private IWXAPI mWechatAPI;

    public static LoginAndShareManager getInstance() {
        if (instance == null) {
            synchronized (LoginAndShareManager.class) {
                if (instance == null) {
                    new LoginAndShareManager();
                }
            }
        }
        return instance;
    }

    private LoginAndShareManager() {
        instance = this;
    }


    public SsoHandler getWeiboLoginSsoHandler(Activity activity) {
        if (mWeiBoAuthInfo == null) {
            mWeiBoAuthInfo = new AuthInfo(activity,
                    Configuration.WEIBO_APPID,
                    Configuration.WEIBO_REDIRECT_URL,
                    "statuses_to_me_read");
        }
        mWeiboSsonHandler = new SsoHandler(activity, mWeiBoAuthInfo);
        return mWeiboSsonHandler;
    }

    public Tencent getQQLoginTencent(Context context) {
        //QQ登陆对象实例化
        if (mTencent == null) {
            mTencent = Tencent.createInstance(Configuration.TENCENT_APPID, context.getApplicationContext());
            TencentOpenBean data = TencentUtil.getToken(context);
            mTencent.setOpenId(data.getOpenid());
            mTencent.setAccessToken(data.getAccess_token(), String.valueOf(data.getExpires_in()));
        }
        return mTencent;
    }

    public IWXAPI getWechatShareApi(Context context) {
        if (mWechatAPI == null) {
            mWechatAPI = WXAPIFactory.createWXAPI(context, com.example.newsclient.Configuration.WECHAT_APPID, true);
            mWechatAPI.registerApp(com.example.newsclient.Configuration.WECHAT_APPID);
        }
        return mWechatAPI;
    }

    public IWeiboShareAPI getWeiboShareApi(Context context) {
        if (mWeiboShareAPI == null) {
            //初始化
            mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, com.example.newsclient.Configuration.WEIBO_APPID);
            mWeiboShareAPI.registerApp();
        }
        return mWeiboShareAPI;
    }

    public void shareToWeibo(final Activity activity, VideoItemBean videoData) {
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

        getImageFromNet(videoData.getThumbnail(), new Observer<Bitmap>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
                videoObject.setThumbImage(bitmap);
                if (!activity.isDestroyed()) {
                    startShareToWeibo(activity, videoObject);
                }
            }

            @Override
            public void onNext(Bitmap bitmap) {
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
                }
                if (!activity.isDestroyed()) {
                    videoObject.setThumbImage(bitmap);
                }
                startShareToWeibo(activity, videoObject);

            }
        });

    }

    /**
     * 分享到微博
     *
     * @param activity
     * @param videoObject
     */
    private void startShareToWeibo(Activity activity, VideoObject videoObject) {
        WeiboMultiMessage multiMessage = new WeiboMultiMessage();

        multiMessage.mediaObject = videoObject;
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = multiMessage;
        //查看日志
        //  com.sina.weibo.sdk.utils.LogUtil.sIsLogEnable = true;
        getWeiboShareApi(activity).sendRequest(activity, request);
    }

    private void getImageFromNet(String thumbnail, Observer<Bitmap> observer) {
        //加载图片
        Retrofit retrofit = new Retrofit.Builder().baseUrl(thumbnail)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        LogUtil.d("http_img", thumbnail);
        service.loadImage("")
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap call(ResponseBody responseBody) {
                        ByteArrayOutputStream os = null;
                        Bitmap bitmap = null;
                        try {
                            InputStream in = responseBody.byteStream();
                            bitmap = BitmapFactory.decodeStream(in);
                            //压缩图片
                            os = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
                            System.out.println("Weibo_th    size  " + os.toByteArray().length);
                            while (os.toByteArray().length > 32 * 1024) {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
                                System.out.println("Weibo_th    size  " + os.toByteArray().length);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (os != null) {
                                    os.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return bitmap;
                    }
                })
                .subscribe(observer);
    }


    /**
     * 分享到微信朋友圈或者好友
     *
     * @param activity
     * @param videoData VideoItemBean类型的数据
     * @param flage     分享到朋友圈还是给好友的flag
     */
    public void shareToWechat(final Activity activity, final VideoItemBean videoData, final int flage) {
        //视频分享
        WXVideoObject wxVideoObject = new WXVideoObject();
        wxVideoObject.videoUrl = videoData.getLink();

        final WXMediaMessage mediaMessage = new WXMediaMessage(wxVideoObject);
        mediaMessage.title = videoData.getTitle();
        mediaMessage.description = videoData.getDescription();
        getImageFromNet(videoData.getThumbnail(), new Observer<Bitmap>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Bitmap thumb = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
                mediaMessage.setThumbImage(thumb);
                if (!activity.isDestroyed()) {
                    startShareWechat(getWechatShareApi(activity), videoData, flage, mediaMessage);
                }

            }

            @Override
            public void onNext(Bitmap bitmap) {
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
                }
                if (!activity.isDestroyed()) {
                    mediaMessage.setThumbImage(bitmap);
                    startShareWechat(getWechatShareApi(activity), videoData, flage, mediaMessage);
                }
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

    /**
     * 分享到QQ
     *
     * @param activity
     * @param videoData
     */
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
}
