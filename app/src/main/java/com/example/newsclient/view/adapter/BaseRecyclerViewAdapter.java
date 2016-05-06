package com.example.newsclient.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.example.newsclient.Model.utils.MyImageLoader;
import com.example.newsclient.R;
import com.example.newsclient.view.impl.OnItemClickListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016-05-04.
 */
public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
    public static final int VIEWTYPE_ITEM = 1;
    public static final int VIEWTYPE_FOOTER = 2;
    private String footerMessage = "正在加载中...";
    private ImageLoader imageLoader;
    private Drawable[] tdDrawableArray;
    private Context context;
    private boolean footershow;

    private List<T> datas;


    private View.OnClickListener listener;
    private RecyclerView.ViewHolder footerVH;

    private LinkedList<Runnable> mImgLoadQueue;
    private boolean pauseLoading;

    private OnItemClickListener itemClickListener;

    public abstract int getFooterLayoutId();

    public abstract int getItemLayoutId();


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //一些需要context的初始化工作
        if (imageLoader == null) {
            context = parent.getContext();
            MyImageLoader.newInstance().init(parent.getContext());
            imageLoader = MyImageLoader.getImageLoader();
            //图片渐变数组的初始化
            Drawable drawable;
            if (loadingBitamp == null) {
                loadingBitamp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_loading);
            }
            drawable = new BitmapDrawable(getContext().getResources(), loadingBitamp);
            tdDrawableArray = new Drawable[]{drawable, drawable};
        }

        RecyclerView.ViewHolder holder;
        View view = null;
        if (viewType == VIEWTYPE_FOOTER) {
            view = LayoutInflater.from(parent.getContext()).inflate(getFooterLayoutId(), null);
        }
        if (viewType == VIEWTYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(), parent, false);
        }
        holder = onCreateMyViewHolder(view, viewType);
        view.setTag(holder);

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
            if (!pauseLoading) {
                executeTask();
            }
        } else {
            onBindFooterViewHolder((VH) holder, position);
            footerVH = holder;
        }

    }

    protected abstract void onBindFooterViewHolder(VH holder, int position);

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
       /* if (footershow) {
            notifyItemRangeInserted(size - 1, list.size());
        } else {
            notifyItemRangeInserted(size, list.size());
        }*/
        if (size == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(size, list.size());
        }

        //添加后就立即开启获取图片
        pauseLoading(false);
    }

    public void addData(T data) {
        if (data == null) {
            return;
        }

        datas.add(data);
        if (footershow) {
            notifyItemRangeInserted(datas.size() - 1, 1);
        } else {
            notifyItemRangeInserted(datas.size(), 1);
        }
    }


    public void clearData() {
        datas.clear();
    }

    /**
     * 初始化数据容器
     */
    protected abstract List<T> initdatas();


    public void setOnItemClickListenner(OnItemClickListener l) {
        itemClickListener = l;
    }

    public void reomveAllData() {
        datas.clear();
    }

    public void pauseLoading(boolean b) {
        pauseLoading = b;
    }

    public boolean getPauseLoading() {
        return pauseLoading;
    }

    public void executeTask(int firstPosition, int lastPositon) {
        if (mImgLoadQueue == null) {
            return;
        }
        for (int i = lastPositon; i >= firstPosition; i--) {
            if (pauseLoading) {
                break;
            }
            if (!mImgLoadQueue.isEmpty()) {
                mImgLoadQueue.removeLast().run();
            }
        }
        //加载完当前页的图片就全部清空队列。
        mImgLoadQueue.clear();
    }

    public void executeTask() {
        if (mImgLoadQueue == null) {
            return;
        }
        for (Runnable r : mImgLoadQueue) {
            r.run();
        }
        mImgLoadQueue.clear();
    }

    protected void addTask(Runnable r) {
        if (mImgLoadQueue == null) {
            mImgLoadQueue = new LinkedList<>();
        }
        mImgLoadQueue.add(r);
    }

    public void setOnFooterListener(View.OnClickListener l) {
        listener = l;
    }

    public View.OnClickListener getOnClickListener() {
        return listener;
    }

    public void setFooterText(String s) {
        if (footerVH != null) {
            footerMessage = s;
            notifyItemChanged(datas.size());
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

    private static Bitmap loadingBitamp;

    protected Bitmap getLoadingBitmap() {
        return loadingBitamp;
    }

    public List<T> getData() {
        return datas;
    }


    public Context getContext() {
        return context;
    }

    /**
     * @return 图片渐变
     */
    public Drawable[] getTdDrawableArray() {
        return tdDrawableArray;
    }


}
