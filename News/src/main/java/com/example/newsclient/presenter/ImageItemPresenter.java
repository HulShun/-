package com.example.newsclient.presenter;

import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.bean.image.ImageContentBean;
import com.example.newsclient.Model.bean.image.ImageJsonBean;
import com.example.newsclient.Model.impl.ImageItemModelImpl;
import com.example.newsclient.Model.model.ImageItemModel;
import com.example.newsclient.view.impl.ImageItemViewImpl;

import java.util.List;
import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016/5/22.
 */
public class ImageItemPresenter extends BasePresenter<ImageItemViewImpl, ImageItemModelImpl> {
    @Override
    protected ImageItemModelImpl instanceModel() {
        return new ImageItemModel();
    }


    public void getImages(Map<String, String> map) {
        final String itemid = map.get("itemid");

        getModel().getImageDatas(map, new Observer<ImageJsonBean>() {
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
                    LogUtil.d("image", "加载图片错误：" + e.getMessage());
                    getView().showFaild(e.getMessage());
                }
            }

            @Override
            public void onNext(ImageJsonBean imageJsonBean) {
                if (getView().isVisiable()) {
                    List<ImageContentBean> data = imageJsonBean.getShowapi_res_body().getPagebean().getContentlist();
                    for (ImageContentBean bean : data) {
                        if (bean.getItemId().equals(itemid)) {
                            getView().onImagesResult(bean);
                        }
                    }
                }

            }
        });
    }
}
