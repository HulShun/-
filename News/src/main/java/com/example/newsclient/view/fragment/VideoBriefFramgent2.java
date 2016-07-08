package com.example.newsclient.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.newsclient.Model.bean.video.RecommendJsonVideoBean;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.VideoBriefPresenter;
import com.example.newsclient.view.activity.VideoItemActivity;
import com.example.newsclient.view.activity.VideoRecommendActivity;
import com.example.newsclient.view.adapter.BaseRvAdapter;
import com.example.newsclient.view.adapter.VideoViewAdapter;
import com.example.newsclient.view.impl.IVideoBriefViewImpl;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-05-13.
 */
public class VideoBriefFramgent2 extends BaseFragment<VideoBriefPresenter> implements IVideoBriefViewImpl {

    @Override
    protected VideoBriefPresenter initPresenter() {
        return new VideoBriefPresenter();
    }


    @Override
    protected void initLoading() {
        vid = (String) getArguments().get("vid");
        getPresenter().loadVideoData(vid);
        //获取推荐视频
        getPresenter().loadRecommendVieoData(vid);
    }


    @Bind(R.id.brief_more_btn)
    Button briefMoreBtn;
    @Bind(R.id.brief_more_rv)
    RecyclerView briefMoreRv;


    private String vid;
    private VideoItemBean videoInform;
    private VideoViewAdapter mAdapter;

    @Override
    int getLayoutId() {
        return R.layout.fragment_brief;
    }

    @Override
    int getLoadingLayouId() {
        return R.layout.loadinglayout;
    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, rootView);

        initRecyclerView();
    }

    private void initRecyclerView() {
        briefMoreRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        briefMoreRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new VideoViewAdapter(this.getContext());

        mAdapter.setOnItemClickListener(new BaseRvAdapter.OnItemClickListener() {
            @Override
            public void onClick(RecyclerView.ViewHolder holder, int position) {
                RecommendJsonVideoBean.RecommendVideoBean data;
                data = (RecommendJsonVideoBean.RecommendVideoBean) holder.itemView.getTag();
                Activity activity = getActivity();
                Intent intent;
                //因为youku播放器需要将activity设置成singleTask，所以交替两个activity来触发推荐视频的播放
                if (activity instanceof VideoItemActivity) {
                    intent = new Intent(getContext(), VideoRecommendActivity.class);
                } else {
                    intent = new Intent(getContext(), VideoItemActivity.class);
                }
                intent.putExtra("id", data.getId());
                startActivity(intent);

                getActivity().finish();
            }
        });

        briefMoreRv.setAdapter(mAdapter);

    }


    /**
     * 当前要播放的视频的数据回调
     *
     * @param data
     */
    @Override
    public void onVideoDataResult(VideoItemBean data) {
        videoInform = data;
        mAdapter.setDataToHeader(videoInform);
    }

    /**
     * 推荐视频回调
     *
     * @param data
     */
    @Override
    public void onRecommendVideoResult(RecommendJsonVideoBean data) {
        if (getLoadingView().isloading()) {
            getLoadingView().showSuccess();
        }

        //推荐视频
        mAdapter.clearDatas();
        mAdapter.addDatas(data.getVideos());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
