package com.example.newsclient.presenter;

import com.android.volley.toolbox.ImageLoader;
import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.image.ImageJsonBean;
import com.example.newsclient.Model.impl.ImageListModelImpl;
import com.example.newsclient.Model.model.ImageListModel;
import com.example.newsclient.view.impl.IImageListViewImpl;

import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-04.
 */
public class ImageListPresenter extends BasePresenter<IImageListViewImpl, ImageListModelImpl> {
    @Override
    protected ImageListModelImpl instanceModel() {
        return new ImageListModel();
    }


    public void getImageDatas(final int mode, Map<String, String> params) {
        getModel().getImageDatas(params, new Observer<ImageJsonBean>() {
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
            public void onNext(ImageJsonBean imageJsonBean) {
                if (getView().isVisiable()) {
                    if (mode == ModelMode.REFRESH) {
                        getView().onRefreshed(imageJsonBean);
                    } else {
                        getView().onLoadMore(imageJsonBean);
                    }

                }
            }
        });
    }

    public void getBitmap(String url, ImageLoader.ImageListener l) {
        getModel().getBitmap(url, l);

    }
}
