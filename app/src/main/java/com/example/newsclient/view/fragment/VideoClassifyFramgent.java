package com.example.newsclient.view.fragment;

import android.view.View;

import com.example.newsclient.R;
import com.example.newsclient.presenter.VideoClassifyPresenter;

/**
 * Created by Administrator on 2016-05-09.
 */
public class VideoClassifyFramgent extends BaseFragment<VideoClassifyPresenter> {


    @Override
    protected VideoClassifyPresenter initPresenter() {
        return new VideoClassifyPresenter();
    }

    @Override
    protected void initLoading() {
        //加载数据

    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_videos;
    }

    @Override
    int getLoadingLayouId() {
        return R.layout.loadinglayout;
    }


}
