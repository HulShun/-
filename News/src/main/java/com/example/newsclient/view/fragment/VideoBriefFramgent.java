package com.example.newsclient.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.newsclient.Model.bean.video.RecommendJsonVideoBean;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.VideoBriefPresenter;
import com.example.newsclient.view.activity.VideoItemActivity;
import com.example.newsclient.view.activity.VideoRecommendActivity;
import com.example.newsclient.view.adapter.RecommendVideoAapter;
import com.example.newsclient.view.impl.IVideoBriefViewImpl;
import com.example.newsclient.view.impl.OnItemClickListener;
import com.example.newsclient.widget.MyScrollLayout;

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
    @Bind(R.id.brief_scroll_layout)
    MyScrollLayout scrollLayout;

    private String vid;
    private VideoItemBean videoInform;
    private RecommendVideoAapter mAdapter;

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
                    isTextOn = false;
                    briefText.setVisibility(View.VISIBLE);
                    briefText_nolimit.setVisibility(View.GONE);
                    briefMoreText_Btn.setText(R.string.brief_text_on);
                    scrollLayout.notifyTextViewHeighChanged(0);

                } else {
                    isTextOn = true;
                    //设置成展示全文的状态
                    briefText.setVisibility(View.GONE);
                    briefText_nolimit.setVisibility(View.VISIBLE);
                    briefMoreText_Btn.setText(R.string.brief_text_off);
                    scrollLayout.notifyTextViewHeighChanged(1);
                }
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        briefMoreRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        briefMoreRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RecommendVideoAapter();
        mAdapter.setFooterShow(false);

        mAdapter.setOnItemClickListenner(new OnItemClickListener() {
            @Override
            public void onClick(RecyclerView.ViewHolder viewHolder, int position) {
                RecommendJsonVideoBean.RecommendVideoBean data;
                data = (RecommendJsonVideoBean.RecommendVideoBean) viewHolder.itemView.getTag();
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
     * @param shortHeight
     * @param longHeight
     * @return true表示文字过长
     */
    private boolean mesureTextDescription(int shortHeight, int longHeight) {
        briefText.setVisibility(View.VISIBLE);
        briefText_nolimit.setVisibility(View.GONE);
        isTextOn = false;
        boolean flag;
        //文字内容没有超过最大
       /* if (shortHeight >= longHeight) {
            briefMoreText_Btn.setVisibility(View.GONE);
            flag = false;
        } else {*/
        briefMoreText_Btn.setText(R.string.brief_text_on);
        briefMoreText_Btn.setVisibility(View.VISIBLE);
        flag = true;

        return flag;
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
        String title = getContext().getResources().getString(R.string.video_brief_title);
        if (!"".equals(description)) {
            briefText.setText(title +
                    "\r\n" +
                    "\t\t\t\t" +
                    description);
            briefText_nolimit.setText(title +
                    "\r\n" +
                    "\t\t\t\t"
                    + description);
        } else {
            briefText.setText(title + "无");
        }
        scrollLayout.notifyTextViewHeighChanged(0);
        //延迟，等待高度测量完成
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mesureTextDescription(briefText.getMeasuredHeight(), briefText_nolimit.getMeasuredHeight());
            }
        }, 500);
        //推荐视频
        mAdapter.clearData();
        mAdapter.addData(data.getVideos());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
