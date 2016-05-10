package com.example.newsclient.presenter;

import com.example.newsclient.Model.impl.VideoClassifyModelImpl;
import com.example.newsclient.Model.model.VideoClassifyModel;
import com.example.newsclient.view.impl.IVideoClassifyViewIpml;

/**
 * Created by Administrator on 2016-05-09.
 */
public class VideoClassifyPresenter extends BasePresenter<IVideoClassifyViewIpml, VideoClassifyModelImpl> {
    @Override
    protected VideoClassifyModelImpl instanceModel() {
        return new VideoClassifyModel();
    }


}
