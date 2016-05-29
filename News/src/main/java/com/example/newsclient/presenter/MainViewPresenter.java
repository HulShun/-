package com.example.newsclient.presenter;

import android.app.Activity;
import android.content.Context;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.bean.QQUserInfro;
import com.example.newsclient.Model.bean.WeiboUserInfo;
import com.example.newsclient.Model.bean.image.ImageMainTypeBean;
import com.example.newsclient.Model.bean.image.ImageTypeJsonBean;
import com.example.newsclient.Model.bean.video.VideoTypeBean;
import com.example.newsclient.Model.impl.MainViewModelImpl;
import com.example.newsclient.Model.impl.MyWeiboAuthListener;
import com.example.newsclient.Model.impl.TencentBaseUIListenner;
import com.example.newsclient.Model.model.MainViewModel;
import com.example.newsclient.Model.utils.WeiboUtil;
import com.example.newsclient.R;
import com.example.newsclient.view.impl.IMainViewImpl;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-05.
 */
public class MainViewPresenter extends BasePresenter<IMainViewImpl, MainViewModelImpl> {

    private List<ImageMainTypeBean> mImageTpyes;
    private List<VideoTypeBean.VideoCategoriesBean> mVideoTypes;
    private List<String> mNewsTabs;



    public MainViewPresenter() {
        //参数 1 ：表示不刷新ui
        getImagesTabsFromLocal(1);
        getVideoTabsFromLocal(1);
    }

    @Override
    protected MainViewModelImpl instanceModel() {
        return new MainViewModel();
    }


    public void loadImageTpyes() {
        if (mImageTpyes == null) {
            getImageTabsFromNet(Configuration.IMAGE_TYPE_URL);
        } else {
            getView().onImageTabs(mImageTpyes);
        }

    }

    public void loadVideoTypes() {
        if (mVideoTypes == null) {
            getVideoTabsFromNet(Configuration.YOUKU_API_BASE_URL);
        } else {
            getView().onVideoTabs(mVideoTypes);
        }

    }


    public void loadNewsType(Context c) {
        if (mNewsTabs == null) {
            getNewsTabFromLocal(c);
        } else {
            getView().onNewsTabs(mNewsTabs);
        }
    }

    private void getNewsTabFromLocal(Context c) {
        String[] list = c.getResources().getStringArray(R.array.news_tab);
        mNewsTabs = Arrays.asList(list);
        getView().onNewsTabs(mNewsTabs);
    }


    private void getVideoTabsFromNet(String url) {
        if (!getView().checkNetWork()) {
            getView().onCompleted();
            getView().showNoNetWork();
            return;
        }
        //网络加载后的标签，直接存放到数据库中
        getModel().getVideoTabsFromNet(url);

        getVideoTabsFromLocal(0);
    }

    /**
     * @param flag 0，1： 0表示刷新ui，1表示不刷新ui
     */
    private void getVideoTabsFromLocal(final int flag) {
        getModel().getVideoTabsFromLocal(new Observer<VideoTypeBean>() {
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
                }
            }

            @Override
            public void onNext(VideoTypeBean videoTypeBean) {
                if (getView().isVisiable()) {
                    mVideoTypes = videoTypeBean.getCategories();
                  
                    if (flag == 0) {
                        getView().onVideoTabs(mVideoTypes);
                    }
                }
            }
        });
    }

    private void getImageTabsFromNet(String url) {
        if (!getView().checkNetWork()) {
            getView().onCompleted();
            getView().showNoNetWork();
            return;
        }
        getModel().getImageTabsFromNet(url);

        getImagesTabsFromLocal(0);
    }

    /**
     * @param flag 0，1： 0表示刷新ui，1表示不刷新ui
     */
    private void getImagesTabsFromLocal(final int flag) {

        getModel().getImageTabsFromLocal(new Observer<ImageTypeJsonBean>() {
            @Override
            public void onCompleted() {
                if (getView() != null) {
                    getView().showSuccess();
                    getView().onCompleted();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null) {
                    getView().showFaild(e.getMessage());
                    getView().onCompleted();
                }
            }

            @Override
            public void onNext(ImageTypeJsonBean imageTypeJsonBean) {
                if (getView() != null) {
                    mImageTpyes = imageTypeJsonBean.getShowapi_res_body().getList();

                    if (flag == 0) {
                        getView().onImageTabs(mImageTpyes);
                    }
                }
            }
        });
    }


    public void qqlogin(final Tencent mTencent, final Activity context) {
        //QQ登陆回调
        IUiListener mTencentListener = new TencentBaseUIListenner() {
            @Override
            protected void doComplete(JSONObject values) {
                super.doComplete(values);
                String s = values.toString();
                //保存用户信息
                getModel().saveQQInfo(values, context);
                //获取用户信息
                getQQUserInfo(mTencent, context);

            }
        };

        mTencent.login(context, "all", mTencentListener);
    }


    public void getQQUserInfo(Tencent mTencent, final Activity context) {
        UserInfo userInfo = new UserInfo(context, mTencent.getQQToken());
        getModel().getQQUserInfo(userInfo, new Observer<QQUserInfro>() {
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
                    LogUtil.d(MainViewPresenter.class.getSimpleName(), e.getMessage());
                    getView().showFaild(e.getMessage());
                    getView().onCompleted();
                }
            }

            @Override
            public void onNext(QQUserInfro qqUserInfro) {
                if (getView().isVisiable()) {
                    getView().onQQUserInfoResult(qqUserInfro);
                }
            }
        });

    }

    public void weiboLogin(SsoHandler mWeiboSsonHandler, final Context context) {
        mWeiboSsonHandler.authorize(new MyWeiboAuthListener() {
            @Override
            protected void doComplete(Oauth2AccessToken oauth2AccessToken) {
                WeiboUtil.saveToken(context, oauth2AccessToken);
                //去获取用户信息
                getWeiboUserInfo(oauth2AccessToken);
            }
        });
    }


    public void getWeiboUserInfo(final Oauth2AccessToken weiboToken) {

        String token = weiboToken.getToken();
        String uid = weiboToken.getUid();

        getModel().getWeiboUserInfo(token, uid, new Observer<WeiboUserInfo>() {
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
            public void onNext(WeiboUserInfo weiboUserInfo) {
                if (getView().isVisiable()) {
                    getView().onWeiboUserInfoResult(weiboUserInfo);
                }
            }
        });
    }
}
