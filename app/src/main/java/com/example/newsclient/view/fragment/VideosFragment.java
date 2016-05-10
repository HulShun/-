package com.example.newsclient.view.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.example.newsclient.Model.bean.VideoListBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.BasePresenter;
import com.example.newsclient.view.impl.IVideoViewImpl;
import com.example.newsclient.widget.AutoRecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 视频
 */
public class VideosFragment extends BaseFragment implements IVideoViewImpl {
    @Bind(R.id.fragment_video_rc)
    AutoRecyclerView fragmentVideoRc;
    @Bind(R.id.fragment_video_refresh)
    SwipeRefreshLayout fragmentVideoRefresh;


    @Override
    protected BasePresenter initPresenter() {
        return null;
    }



    @Override
    protected void initLoading() {

    }

    @Override
    protected void resume() {

    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);
        initRecyclerView();
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        fragmentVideoRc.setLayoutManager(gridLayoutManager);
        fragmentVideoRc.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_videos;
    }

    @Override
    int getLoadingLayouId() {
        return 0;
    }

    @Override
    public void updateDatas(VideoListBean videos) {

    }

    @Override
    public boolean checkNetWork() {
        return false;
    }



    @Override
    public void onRefreshComplete() {

    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
