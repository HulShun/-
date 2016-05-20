package com.example.newsclient.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.R;
import com.example.newsclient.view.impl.OnItemClickListener;
import com.example.newsclient.view.viewholder.FooterViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016-05-04.
 */
public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
    public static final int VIEWTYPE_ITEM = 1;
    public static final int VIEWTYPE_FOOTER = 2;


    private Context context;
    private boolean footershow;

    private List<T> datas;

    private View.OnClickListener listener;
    private RecyclerView.ViewHolder footerVH;

    private OnItemClickListener itemClickListener;

    public abstract int getFooterLayoutId();

    public abstract int getItemLayoutId();


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //一些需要context的初始化工作
        // if (imageLoader == null)
        if (context == null) {
            context = parent.getContext();
        }

        RecyclerView.ViewHolder holder = null;
        View view = null;
        if (viewType == VIEWTYPE_FOOTER) {
            view = LayoutInflater.from(parent.getContext()).inflate(getFooterLayoutId(), null);
            holder = new FooterViewHolder(view);
            footerVH = holder;
        } else if (viewType == VIEWTYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(), parent, false);
            holder = onCreateMyViewHolder(view, viewType);
        }

        return holder;

    }

    protected abstract VH onCreateMyViewHolder(View view, int viewType);

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == VIEWTYPE_ITEM) {
            onBindItemViewHolder((VH) holder, position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        holder.itemView.setTag(getData().get(position));
                        itemClickListener.onClick(holder, position);
                    }
                }
            });

        } else {
            ((FooterViewHolder) footerVH).loadingLayout.setOnClickListener(listener);
        }
    }

    protected abstract void onBindItemViewHolder(VH holder, int position);

    @Override
    public int getItemCount() {
        if (datas == null) {
            datas = initdatas();
        }
        if (datas.size() == 0) {
            return 0;
        }
        if (footershow) {
            return datas.size() + 1;
        } else {
            return datas.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == datas.size()) {
            return VIEWTYPE_FOOTER;
        } else {
            return VIEWTYPE_ITEM;
        }
    }

    public void addData(List<T> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        if (datas == null) {
            datas = initdatas();
        }
        int size = datas.size();
        datas.addAll(list);

        if (size == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(size, list.size());
        }

        //添加后就立即开启获取图片
        //  pauseLoading(false);
    }

    public void addData(T data) {
        if (data == null) {
            return;
        }
        if (datas == null) {
            datas = initdatas();
        }
        int size = datas.size();
        datas.add(data);
        if (footershow) {
            notifyItemRangeInserted(size - 1, 1);
        } else {
            notifyItemRangeInserted(size, 1);
        }
        //添加后就立即开启获取图片
        //  pauseLoading(false);
    }


    public void clearData() {
        if (datas == null) {
            datas = initdatas();
        }
        datas.clear();
        notifyDataSetChanged();
    }

    /**
     * 初始化数据容器
     */
    protected abstract List<T> initdatas();


    public void setOnItemClickListenner(OnItemClickListener l) {
        itemClickListener = l;
    }



    public void setOnFooterListener(View.OnClickListener l) {
        listener = l;
    }


    public void showFooterBtn() {
        if (footerVH != null) {
            // ((FooterViewHolder) footerVH).loadingLayout.setVisibility(View.GONE);
            ((FooterViewHolder) footerVH).textView.setText(R.string.footer_btn);
            ((FooterViewHolder) footerVH).imageView.setVisibility(View.GONE);
            notifyItemChanged(datas.size());
            LogUtil.d("footer", "showFooterBtn");
        }
    }

    public void showFooterLoading() {
        if (footerVH != null) {
            ((FooterViewHolder) footerVH).loadingLayout.setVisibility(View.VISIBLE);
            ((FooterViewHolder) footerVH).imageView.setImageResource(R.drawable.footer_loading);
            ((FooterViewHolder) footerVH).imageView.setVisibility(View.VISIBLE);
            ((FooterViewHolder) footerVH).textView.setText(R.string.footer_loadmore);
            notifyItemChanged(datas.size());
            LogUtil.d("footer", "showFooterLoading");
        }
    }

    public void setFooterShow(boolean b) {
        footershow = b;
        if (!b && datas != null) {
            if (!datas.isEmpty()) {
                notifyItemRemoved(datas.size() - 1);
            }
        }
    }


    public List<T> getData() {
        return datas;
    }


    public Context getContext() {
        return context;
    }



}
