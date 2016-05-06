package com.example.newsclient.presenter;

import com.example.newsclient.Model.bean.ImageMainTypeBean;
import com.example.newsclient.Model.bean.ImageTypeJsonBean;
import com.example.newsclient.Model.impl.MainViewModelImpl;
import com.example.newsclient.Model.model.MainViewModel;
import com.example.newsclient.view.impl.IMainViewImpl;

import java.util.List;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-05.
 */
public class MainViewPresenter extends BasePresenter<IMainViewImpl, MainViewModelImpl> {

    private List<ImageMainTypeBean> mImageTpyes;


    public MainViewPresenter() {
        //参数 1 ：表示不刷新ui
        getImagesTabsFromLocal(1);
    }

    @Override
    protected MainViewModelImpl instanceModel() {
        return new MainViewModel();
    }

    public void getImageTabsFromNet(String url) {
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
    public void getImagesTabsFromLocal(final int flag) {

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

    public List<ImageMainTypeBean> getmImageTpyes() {
        return mImageTpyes;
    }
}
