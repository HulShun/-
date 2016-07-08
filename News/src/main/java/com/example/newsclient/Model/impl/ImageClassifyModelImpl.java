package com.example.newsclient.Model.impl;

import com.example.newsclient.Model.bean.image.ImageJsonBean;
import com.example.newsclient.Model.bean.image.ImageTypeBean;

import java.util.List;
import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-05.
 */
public interface ImageClassifyModelImpl extends BaseModelImpl {

    void getImageDatas(int mode, List<ImageTypeBean> typeBeans, Observer<ImageJsonBean> os);

    void saveIntoDB(ImageJsonBean jsonBean);

    void deleteDataInDB(Map<String, String> map);
}
