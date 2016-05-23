package com.example.newsclient.view.impl;

import com.example.newsclient.Model.bean.video.Commentsv2JsonBean;

/**
 * Created by Administrator on 2016-05-16.
 */
public interface ICommentsViewImpl extends IBaseViewImpl {
    void onCommentsResulte(Commentsv2JsonBean data);
}
