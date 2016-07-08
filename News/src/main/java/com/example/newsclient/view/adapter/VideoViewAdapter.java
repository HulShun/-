package com.example.newsclient.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newsclient.Model.bean.video.RecommendJsonVideoBean;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.R;
import com.example.newsclient.view.viewholder.FooterViewHolder;
import com.example.newsclient.view.viewholder.RecommendVideoViewHolder;
import com.example.newsclient.view.viewholder.VideoBriefViewHolder;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2016-07-08.
 */
public class VideoViewAdapter extends BaseRvAdapter<RecommendJsonVideoBean.RecommendVideoBean> {

    public VideoViewAdapter(Context context) {
        super(context);
        addHeaderView(R.layout.fragment_brief_item_brief);
        addHeaderView(R.layout.fragment_brief_item_subtitle);
        //addFooterView(R.layout.fragment_rv_footer);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateRealItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_brief_item_video, parent, false);
        return new RecommendVideoViewHolder(view);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, View view, int posInFooterList) {
        switch (posInFooterList) {
            case 0:
                return new FooterViewHolder(view);

            default:
                return null;
        }

    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, View view, int posInHeaderList) {
        switch (posInHeaderList) {
            case 0:
                return new VideoBriefViewHolder(view);
            case 1:
                return null;
            default:
                return null;
        }
    }

    @Override
    protected void onBindRealItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecommendVideoViewHolder) {
            RecommendVideoViewHolder _holder = (RecommendVideoViewHolder) holder;
            _holder.textView.setText(getDatas().get(position).getTitle());
            String img_utl = getDatas().get(position).getThumbnail();
            Picasso.with(getContext())
                    .load(img_utl)
                    .tag(img_utl)
                    .error(R.drawable.ic_loading)
                    .placeholder(R.drawable.ic_loading)
                    .into(_holder.imageView);
        }

    }


    private VideoItemBean videoInform;

    @Override
    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoBriefViewHolder) {
            if (videoInform != null) {
                final VideoBriefViewHolder _holder = (VideoBriefViewHolder) holder;
                //简介
                String description = videoInform.getDescription();
                String title = getContext().getResources().getString(R.string.video_brief_title);
                if (!"".equals(description)) {
                    _holder.brief_text.setText(title +
                            "\r\n" +
                            "\t\t\t\t" +
                            description);
                    _holder.brief_text_nolimit.setText(title +
                            "\r\n" +
                            "\t\t\t\t"
                            + description);
                } else {
                    _holder.brief_text.setText(title + "无");
                    _holder.brief_tv_btn.setVisibility(View.GONE);
                }

                _holder.brief_text_nolimit.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        mesureTextDescription(_holder);
                    }
                });

                _holder.brief_tv_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //当前已经显示了全文,就回到收缩状态
                        if (_holder.isTextOn) {
                            _holder.isTextOn = false;
                            _holder.brief_text.setVisibility(View.VISIBLE);
                            _holder.brief_text_nolimit.setVisibility(View.GONE);
                            _holder.brief_tv_btn.setText(R.string.brief_text_on);
                            // scrollLayout.notifyTextViewHeighChanged(0);
                        } else {
                            _holder.isTextOn = true;
                            //设置成展示全文的状态
                            _holder.brief_text.setVisibility(View.GONE);
                            _holder.brief_text_nolimit.setVisibility(View.VISIBLE);
                            _holder.brief_tv_btn.setText(R.string.brief_text_off);
                            // scrollLayout.notifyTextViewHeighChanged(1);
                        }
                    }
                });
            }

        }
    }

    /**
     * @return true表示文字过长
     */
    private boolean mesureTextDescription(VideoBriefViewHolder _holder) {
        _holder.brief_text.setVisibility(View.VISIBLE);
        _holder.brief_text_nolimit.setVisibility(View.GONE);
        _holder.isTextOn = false;
        boolean flag;
        //文字内容没有超过最大
        if (_holder.brief_text.getMeasuredHeight() >= _holder.brief_text_nolimit.getMeasuredHeight()) {
            _holder.brief_tv_btn.setVisibility(View.GONE);
            flag = false;
        } else {
            _holder.brief_tv_btn.setText(R.string.brief_text_on);
            _holder.brief_tv_btn.setVisibility(View.VISIBLE);
            flag = true;
        }

        return flag;
    }

    public void setDataToHeader(Object data) {
        if (data instanceof VideoItemBean) {
            videoInform = (VideoItemBean) data;
            notifyItemChanged(0);
        }
    }
}
