package com.example.newsclient.presenter;

import com.android.volley.toolbox.ImageLoader;
import com.example.newsclient.Model.bean.ImageJsonBean;
import com.example.newsclient.Model.impl.ImageClassifyModelImpl;
import com.example.newsclient.Model.model.ImageClassifyModel;
import com.example.newsclient.view.impl.IImageClassifyViewImpl;

import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-05.
 */
public class ImageClassifyPresenter extends BasePresenter<IImageClassifyViewImpl, ImageClassifyModelImpl> {
    @Override
    protected ImageClassifyModelImpl instanceModel() {
        return new ImageClassifyModel();
    }


    public void getImageData(Map<String, String> params) {
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
                    getView().onLoadData(imageJsonBean);

                }
            }
        });
    }

    public void getBitmap(String url, ImageLoader.ImageListener l) {
        getModel().onLoadBitmap(url, l);

    }
}

