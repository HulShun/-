package com.example.newsclient.presenter;

import com.example.newsclient.Model.LogUtil;
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


    public void getImageDatas(final int mode, Map<String, Integer> params) {
        final int nowpage = params.get("page");

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
                    LogUtil.d("image", "加载图片出错：" + e.getMessage());
                    getView().showFaild(e.getMessage());
                }
            }

            @Override
            public void onNext(ImageJsonBean imageJsonBean) {
                if (getView().isVisiable()) {
                    if (nowpage == 1) {
                        getView().onRefreshed(imageJsonBean);
                    } else {
                        getView().onLoadMore(imageJsonBean);
                    }

                }
            }
        });
    }


}
