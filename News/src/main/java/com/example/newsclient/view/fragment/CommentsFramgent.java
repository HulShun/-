package com.example.newsclient.view.fragment;

import android.view.View;

import com.example.newsclient.Model.bean.video.Commentsv2JsonBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.CommentsPresenter;
import com.example.newsclient.view.impl.ICommentsViewImpl;

/**
 * Created by Administrator on 2016-05-13.
 */
public class CommentsFramgent extends BaseFragment<CommentsPresenter> implements ICommentsViewImpl {
    @Override
    protected CommentsPresenter initPresenter() {
        return new CommentsPresenter();
    }

    private String vid;
    private int nowPage;

    @Override
    protected void initLoading() {
        vid = (String) getArguments().get("vid");
        nowPage = 1;
        getPresenter().loadComments(vid, 0);
    }

    @Override
    protected void initViews(View view) {

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

    }
}
