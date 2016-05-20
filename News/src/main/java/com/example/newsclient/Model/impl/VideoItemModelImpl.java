package com.example.newsclient.Model.impl;

import android.graphics.Bitmap;

import com.example.newsclient.Model.bean.video.CommentsJsonBean;
import com.example.newsclient.Model.bean.video.VideoItemBean;

import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-11.
 */
public interface VideoItemModelImpl extends BaseModelImpl {
    void loadVideoItemData(String id, int mode, Observer<VideoItemBean> os);

    void loadComments(Map<String, String> map, Observer<CommentsJsonBean> os);

    void getImage(String thumbnail, Observer<Bitmap> observer);

    void saveVideoDataToDB(VideoItemBean videoItemBean);
}
