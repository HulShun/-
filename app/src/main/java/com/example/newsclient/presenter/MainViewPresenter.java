package com.example.newsclient.presenter;

import com.example.newsclient.Model.bean.ImageMainTpyeBean;
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

    private List<ImageMainTpyeBean> mImageTpyes;

    @Override
    protected MainViewModelImpl instanceModel() {
        return new MainViewModel();
    }

    public void getImageTabs(String url) {
        if (!getView().checkNetWork()) {
            getView().onCompleted();
            getView().showNoNetWork();
            return;
        }

        getModel().getImageTabs(url, new Observer<ImageTypeJsonBean>() {
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
                    getView().onImageTabs(mImageTpyes);
                }
            }
        });
    }

    public List<ImageMainTpyeBean> getmImageTpyes() {
        return mImageTpyes;
    }
}
