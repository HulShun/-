package com.example.newsclient.view.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.impl.RecommendJsonVideoBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.VideoBriefPresenter;
import com.example.newsclient.view.adapter.RecommendVideoAapter;
import com.example.newsclient.view.impl.IVideoBriefViewImpl;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-05-13.
 */
public class VideoBriefFramgent extends BaseFragment<VideoBriefPresenter> implements IVideoBriefViewImpl {


    @Override
    protected VideoBriefPresenter initPresenter() {
        return new VideoBriefPresenter();
    }


    @Override
    protected void initLoading() {
        vid = (String) getArguments().get("vid");
        getPresenter().loadVideoData(vid);
        getPresenter().loadRecommendVieoData(vid);
    }


    @Bind(R.id.brief_text)
    TextView briefText;
    @Bind(R.id.brief_text_nolimit)
    TextView briefText_nolimit;
    @Bind(R.id.brief_tv_btn)
    Button briefMoreText_Btn;
    private boolean isTextOn;

    @Bind(R.id.brief_more_btn)
    Button briefMoreBtn;
    @Bind(R.id.brief_more_rv)
    RecyclerView briefMoreRv;


    private String vid;
    private VideoItemBean videoInform;
    private RecommendVideoAapter mAapter;

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

        briefMoreText_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当前已经显示了全文,就回到收缩状态
                if (isTextOn) {
                    briefText.setVisibility(View.VISIBLE);
                    briefText_nolimit.setVisibility(View.INVISIBLE);
                    briefMoreText_Btn.setText(R.string.brief_text_on);
                } else {
                    //设置成展示全文的状态
                    briefText.setVisibility(View.INVISIBLE);
                    briefText_nolimit.setVisibility(View.VISIBLE);
                    briefMoreText_Btn.setText(R.string.brief_text_off);
                }
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        briefMoreRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        briefMoreRv.setItemAnimator(new DefaultItemAnimator());
        mAapter = new RecommendVideoAapter();
        mAapter.setFooterShow(false);
        briefMoreRv.setAdapter(mAapter);
    }

    /**
     * @param shortHeight
     * @param longHeight
     * @return true表示文字过长
     */
    private boolean mesureTextDescription(int shortHeight, int longHeight) {
        briefText.setVisibility(View.VISIBLE);
        briefText_nolimit.setVisibility(View.GONE);
        isTextOn = false;
        //文字内容没有超过最大
        if (shortHeight >= longHeight) {
            briefMoreText_Btn.setVisibility(View.GONE);
            return false;
        } else {
            briefMoreText_Btn.setText(R.string.brief_text_on);
            briefMoreText_Btn.setVisibility(View.VISIBLE);
            return true;
        }
    }

    @Override
    public void onVideoDataResult(VideoItemBean data) {
        videoInform = data;
    }

    @Override
    public void onRecommendVideoResult(RecommendJsonVideoBean data) {
        if (getLoadingView().isloading()) {
            getLoadingView().showSuccess();
        }
        //简介
        String description = videoInform.getDescription();
        if (description != null) {
            String title = getContext().getResources().getString(R.string.video_brief_title);
            briefText.setText(title +
                    "\r\n" +
                    "\t\t" +
                    description);
            briefText_nolimit.setText(title + description);
            mesureTextDescription(briefText.getHeight(), briefText_nolimit.getHeight());
        }
        //推荐视频
        if (mAapter.getData() != null) {
            mAapter.getData().clear();
        }
        mAapter.addData(data.getVideos());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
