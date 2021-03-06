package com.example.newsclient.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.video.VideoTypeBean;
import com.example.newsclient.Model.bean.video.VideosInFormBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.VideoClassifyPresenter;
import com.example.newsclient.view.activity.VideoItemActivity;
import com.example.newsclient.view.adapter.VideoClassifyAdapter;
import com.example.newsclient.view.impl.IVideoClassifyViewIpml;
import com.example.newsclient.view.impl.OnItemClickListener;
import com.example.newsclient.view.viewholder.VideoViewHolder;
import com.example.newsclient.widget.AutoRecyclerView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-05-09.
 */
public class VideoClassifyFramgent extends BaseFragment<VideoClassifyPresenter> implements IVideoClassifyViewIpml {


    @Bind(R.id.fragment_video_rc)
    AutoRecyclerView fragmentVideoRc;
    @Bind(R.id.fragment_video_refresh)
    SwipeRefreshLayout fragmentVideoRefresh;

    private VideoTypeBean.VideoCategoriesBean mVideoTypeBean;
    private VideoClassifyAdapter mAdapter;
    private int nowPage;
    private boolean isloadingMore;

    @Override
    protected VideoClassifyPresenter initPresenter() {
        return new VideoClassifyPresenter();
    }

    @Override
    protected void initLoading() {

        getLoadingView().setOnBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().loadVideoTypeList(mVideoTypeBean.getLabel(), null, nowPage, ModelMode.LOCAL);
            }
        });

        //加载数据
        nowPage = 1;
        Bundle bundle = getArguments();
        mVideoTypeBean = (VideoTypeBean.VideoCategoriesBean) bundle.get("type");
        getPresenter().loadVideoTypeList(mVideoTypeBean.getLabel(), null, nowPage, ModelMode.LOCAL);


    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);
        initRecyclerView();
        fragmentVideoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                nowPage = 1;
                getPresenter().loadVideoTypeList(mVideoTypeBean.getLabel(), null, nowPage, ModelMode.INTERNET);
            }
        });
    }

    private void initRecyclerView() {
        fragmentVideoRc.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new VideoClassifyAdapter();
        mAdapter.setFooterShow(true);
        mAdapter.setOnFooterListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isloadingMore) {
                    isloadingMore = true;
                    mAdapter.showFooterLoading();
                    nowPage++;
                    getPresenter().loadVideoTypeList(mVideoTypeBean.getLabel(), null, nowPage, ModelMode.LOCAL);
                }
            }
        });
        mAdapter.setOnItemClickListenner(new OnItemClickListener() {
            @Override
            public void onClick(RecyclerView.ViewHolder viewHolder, int position) {
                //点击跳转
                VideoViewHolder holder = (VideoViewHolder) viewHolder;
                String id = (String) holder.title_textView.getTag();
                // Intent intent = new Intent(getContext(), VideoItemActivity.class);
                Intent intent = new Intent(getContext(), VideoItemActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("title", holder.title_textView.getText().toString());
                getContext().startActivity(intent);
            }
        });

        fragmentVideoRc.addOnScrollListener(
                new AutoRecyclerView.AutoLoadMoreListener() {
                    @Override
                    protected void loadMore() {
                        nowPage++;
                        getPresenter().loadVideoTypeList(mVideoTypeBean.getLabel(), null, nowPage, ModelMode.LOCAL);
                    }

                    @Override
                    protected void resumeLoadImage(LinearLayoutManager manager, int first, int last) {
                        Picasso.with(getContext())
                                .resumeTag(getContext().getApplicationContext());
                        /*int position = first;
                        while (position < last) {
                            LogUtil.d("image", "恢复加载图片");
                            View view = manager.findViewByPosition(position);
                            VideoViewHolder holder = (VideoViewHolder) view.getTag();
                            Picasso.with(getContext())
                                    .resumeTag(holder.imageView.getTag());
                            position++;
                        }*/
                    }

                    @Override
                    protected void pauseLoadImage() {
                        /*List<VideosInFormBean.VideosBean> datas = mAdapter.getData();
                        for (int i = 0; i < mAdapter.getData().size(); i++) {
                            Picasso.with(getContext())
                                    .pauseTag(datas.get(i).getThumbnail());
                            LogUtil.d("image", "暂停加载图片");
                        }*/
                        Picasso.with(getContext())
                                .pauseTag(getContext().getApplicationContext());
                    }
                });
        fragmentVideoRc.setAdapter(mAdapter);
    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_videos;
    }

    @Override
    int getLoadingLayouId() {
        return R.layout.loadinglayout;
    }


    @Override
    public void onUpdateVideos(VideosInFormBean data) {
        if (getLoadingView().isloading()) {
            getLoadingView().showSuccess();
        }
        mAdapter.clearData();
        mAdapter.addData(data.getVideos());
    }

    @Override
    public void onloadMoreVideos(VideosInFormBean date) {
        fragmentVideoRc.loadMoreCompleted();
        mAdapter.addData(date.getVideos());
    }

    @Override
    public void onCompleted() {
        isloadingMore = false;
        if (fragmentVideoRefresh != null) {
            fragmentVideoRefresh.setRefreshing(false);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showFaild(String msg) {
        super.showFaild(msg);
        if (!getLoadingView().isloading()) {
            mAdapter.showFooterBtn();
        }
    }
}
