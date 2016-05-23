package com.example.newsclient.presenter;

import android.widget.Toast;

import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.image.ImageJsonBean;
import com.example.newsclient.Model.bean.image.ImageTypeBean;
import com.example.newsclient.Model.impl.ImageClassifyModelImpl;
import com.example.newsclient.Model.model.ImageClassifyModel;
import com.example.newsclient.MyApplication;
import com.example.newsclient.view.impl.IImageClassifyViewImpl;

import java.util.HashMap;
import java.util.List;
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

    public void getImageData(final int mode, final List<ImageTypeBean> typeBeans) {
        if (mode == ModelMode.LOCAL) {
            getImageDataFromLocal(typeBeans);
        } else {
            if (!getView().checkNetWork()) {
                Toast.makeText(MyApplication.getInstance(), "网络连接失败...", Toast.LENGTH_SHORT).show();
                return;
            }
            getImageDataFromNet(typeBeans);
        }

    }

    private void getImageDataFromNet(final List<ImageTypeBean> typeBeans) {
        getModel().getImageDatas(ModelMode.INTERNET, typeBeans, new Observer<ImageJsonBean>() {
                    @Override
                    public void onCompleted() {
                        if (getView().isVisiable()) {
                            getView().onCompleted();
                            getView().showSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("tabs", "网络相册信息加载失败：" + e.getMessage());
                        if (getView().isVisiable()) {
                            getView().onCompleted();
                            getView().showFaild(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(ImageJsonBean imageJsonBeen) {
                        if (getView().isVisiable()) {
                            LogUtil.d("type", "网络数据刷新ui");
                            getView().onLoadData(imageJsonBeen);
                        }
                        //先清空数据库中的数据
                        Map<String, String> map = new HashMap<>();
                        String itemId = imageJsonBeen.getShowapi_res_body().getPagebean().getContentlist().get(0).getItemId();
                        int type = imageJsonBeen.getShowapi_res_body().getPagebean().getContentlist().get(0).getType();
                        map.put("itemid", itemId);
                        map.put("type", String.valueOf(type));
                        getModel().deleteDataInDB(map);
                        //再往数据库中写入新的数据
                        getModel().saveIntoDB(imageJsonBeen);

                    }
                }


        );
    }

    private void getImageDataFromLocal(final List<ImageTypeBean> typeBeans) {
        final int size = typeBeans.size();
        final int[] count = {1};
        getModel().getImageDatas(ModelMode.LOCAL, typeBeans, new Observer<ImageJsonBean>() {
            @Override
            public void onCompleted() {
                if (getView().isVisiable()) {
                    getView().onCompleted();
                    getView().showSuccess();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ImageJsonBean imageJsonBeen) {
                if (imageJsonBeen == null) {
                    if (count[0] >= size) {
                        getImageDataFromNet(typeBeans);
                    } else {
                        count[0]++;
                    }
                } else {
                    if (getView().isVisiable()) {
                        LogUtil.d("type", "本地数据刷新ui");
                        getView().onLoadData(imageJsonBeen);
                    }
                }
            }
        });
    }

}

