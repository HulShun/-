package com.example.newsclient.view.adapter;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-08.
 */
public abstract class BaseRvAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int BASE_HEADER_TYPE = 200000;
    private final static int BASE_FOOTER_TYPE = 300000;

    private Context mContext;
    private SparseArrayCompat<View> headerViews;
    private SparseArrayCompat<View> footerViews;
    private List<T> mDatas;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(RecyclerView.ViewHolder holder, int position);
    }

    public BaseRvAdapter(Context context) {
        mContext = context;

        headerViews = new SparseArrayCompat<>();
        footerViews = new SparseArrayCompat<>();
        mDatas = new ArrayList<>();
    }

    public BaseRvAdapter(Context context, int[] headerLayouts, int[] footerLayout) {
        this(context);
        if (headerLayouts != null) {
            for (int i : headerLayouts) {
                addHeaderView(i);
            }
        }
        if (footerLayout != null) {
            for (int i : footerLayout) {
                addFooterView(i);
            }
        }
    }

    public BaseRvAdapter(Context context, List<View> headerViews, List<View> footerViews) {
        this(context);
        if (headerViews != null) {
            for (View v : headerViews) {
                addHeaderView(v);
            }
        }
        if (footerViews != null) {
            for (View v : footerViews) {
                addFooterView(v);
            }
        }

    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //头布局
        if (headerViews.get(viewType) != null) {
            return onCreateHeaderViewHolder(parent, headerViews.get(viewType), viewType - BASE_HEADER_TYPE);
        } else if (footerViews.get(viewType) != null) {
            return onCreateFooterViewHolder(parent, footerViews.get(viewType), viewType - BASE_FOOTER_TYPE);
        }

        return onCreateRealItemViewHolder(parent, viewType);
    }

    protected abstract RecyclerView.ViewHolder onCreateRealItemViewHolder(ViewGroup parent, int viewType);

    protected abstract RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, View view, int posInFooterList);

    protected abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, View view, int posInHeaderList);

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (isHeader(position)) {
            onBindHeaderViewHolder(holder, position);
        } else if (isFooter(position)) {
            onBindFooterViewHolder(holder, position);
        } else {
            onBindRealItemViewHolder(holder, position);
            if (onItemClickListener != null) {
                holder.itemView.setTag(getDatas().get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onClick(holder, position);
                    }
                });
            }
        }
    }

    protected abstract void onBindRealItemViewHolder(RecyclerView.ViewHolder holder, int position);


    protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getFooterCount() + getRealItemCount();
    }

    public int getHeaderCount() {
        return headerViews.size();
    }

    public int getFooterCount() {
        return footerViews.size();
    }

    public int getRealItemCount() {
        return mDatas.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            //将键值作为type
            return headerViews.keyAt(position);
        }
        if (isFooter(position)) {
            return footerViews.keyAt(position);
        }

        return getRealItemViewType(position - getHeaderCount());
    }

    private int getRealItemViewType(int position) {
        return 0;
    }


    public boolean isHeader(int position) {
        return position < getHeaderCount();
    }

    public boolean isFooter(int position) {
        return position >= getHeaderCount() + getItemCount();
    }

    protected void addFooterView(int layout) {
        View view = LayoutInflater.from(mContext).inflate(layout, null, false);
        addFooterView(view);
    }

    protected void addHeaderView(int layout) {
        View view = LayoutInflater.from(mContext).inflate(layout, null, false);
        addHeaderView(view);
    }

    protected void addHeaderView(View view) {
        if (view != null) {
            headerViews.put(headerViews.size() + BASE_HEADER_TYPE, view);
        }
    }

    protected void addFooterView(View view) {
        if (view != null) {
            footerViews.put(BASE_FOOTER_TYPE + footerViews.size(), view);
        }
    }

    /**
     * @param position HeaderView总数中的第几个
     * @return
     */
    public View getHeaderView(int position) {
        return headerViews.get(position + BASE_HEADER_TYPE);
    }

    /**
     * @param position FooterView总数中的第几个
     * @return
     */
    public View getFooterView(int position) {
        return footerViews.get(position + BASE_FOOTER_TYPE);
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void addDatas(List<T> datas) {
        if (datas == null) return;
        //除去footer
        int oldCount = getItemCount() - getFooterCount();
        notifyItemRangeChanged(oldCount, datas.size());
    }

    public void clearDatas() {
        mDatas.clear();
    }


    public void setOnItemClickListener(OnItemClickListener l) {
        onItemClickListener = l;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        //网格布局，设置header或者footer占满屏幕宽度
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isHeader(position) || isFooter(position)) {
                        //总行（列）数，footer或者header要占满一行（或者一列）
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
            //重新设置一次
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        //瀑布流布局，设置header或者footer占满屏幕宽度
        int position = holder.getLayoutPosition();
        if (isFooter(position) || isHeader(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
            }
        }
    }
}
