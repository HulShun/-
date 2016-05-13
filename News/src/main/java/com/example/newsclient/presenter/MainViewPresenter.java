package com.example.newsclient.presenter;

import android.content.Context;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.ImageMainTypeBean;
import com.example.newsclient.Model.bean.ImageTypeJsonBean;
import com.example.newsclient.Model.bean.VideoTypeBean;
import com.example.newsclient.Model.impl.MainViewModelImpl;
import com.example.newsclient.Model.model.MainViewModel;
import com.example.newsclient.R;
import com.example.newsclient.view.impl.IMainViewImpl;

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
            getVideoTabsFromNet(Configuration.VIDEO_TYPE_URL);
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
}
