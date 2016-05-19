package com.example.newsclient.view.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.bean.video.Commentsv2JsonBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.CommentsPresenter;
import com.example.newsclient.view.adapter.VideoCommentsAdapter;
import com.example.newsclient.view.impl.ICommentsViewImpl;
import com.example.newsclient.widget.AutoRecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-05-13.
 */
public class VideoCommentsFramgent extends BaseFragment<CommentsPresenter> implements ICommentsViewImpl {


    @Override
    protected CommentsPresenter initPresenter() {
        return new CommentsPresenter();
    }

    private String vid;
    private int nowPage;
    private boolean isloadingMore;
    private boolean isEnded;

    @Override
    protected void initLoading() {
        getLoadingView().setOnBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vid = (String) getArguments().get("vid");
                nowPage = 1;
                getPresenter().loadComments(vid, nowPage);
            }
        });
        vid = (String) getArguments().get("vid");
        nowPage = 1;
        getPresenter().loadComments(vid, nowPage);
    }


    @Bind(R.id.comments_rv)
    AutoRecyclerView commentsRv;

    private VideoCommentsAdapter mAdapter;

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);

        commentsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new VideoCommentsAdapter();
        mAdapter.setFooterShow(true);
        mAdapter.setOnFooterListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.showFooterLoading();
                //没有正在加载，并且还没有加载全部的页数
                if (!isloadingMore && !isEnded) {
                    nowPage++;
                    isloadingMore = true;
                    getPresenter().loadComments(vid, nowPage);
                }
            }
        });
        commentsRv.setAdapter(mAdapter);
        commentsRv.addOnScrollListener(new AutoRecyclerView.AutoLoadMoreListener() {
            @Override
            protected void loadMore() {
                LogUtil.d("comments", "评论数据加载中...");
                nowPage++;
                getPresenter().loadComments(vid, nowPage);

            }

            @Override
            protected void pauseLoadImg() {

            }

            @Override
            protected void resumeLoadImg(int firstPosition, int lastPositon) {

            }
        });

    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_comments;
    }

    @Override
    int getLoadingLayouId() {
        return R.layout.loadinglayout;
    }


    @Override
    public void onCommentsResulte(Commentsv2JsonBean data) {
        isloadingMore = false;
        if (getLoadingView().isloading()) {
            if (data.getComments() == null || data.getComments().isEmpty()) {
                getLoadingView().showMessage("没有评论内容.");
                return;
            } else {
                getLoadingView().showSuccess();
            }
        }

        //最后一页了
        if (data.getComments().size() == 0) {
            isEnded = true;
            mAdapter.setFooterShow(false);
            nowPage = data.getPage() - 1;
        }
        mAdapter.addData(data.getComments());
    }

    @Override
    public void showFaild(String msg) {
        super.showFaild(msg);
        if (mAdapter != null) {
            mAdapter.showFooterBtn();
        }
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        if (commentsRv != null) {
            commentsRv.loadMoreCompleted();
            LogUtil.d("comments", "评论数据加载完成...");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
