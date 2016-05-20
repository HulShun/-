package com.example.newsclient.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.example.newsclient.Model.LogUtil;

/**
 * Created by Administrator on 2016-04-19.
 */
public class AutoRecyclerView extends RecyclerView {
    private Context mContext;
    private static boolean isLoadingMore;


    public AutoRecyclerView(Context context) {
        super(context);
    }

    public AutoRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void loadMoreCompleted() {
        isLoadingMore = false;
    }

    public boolean getLoadingMore() {
        return isLoadingMore;
    }


    public static abstract class AutoLoadMoreListener extends OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof LinearLayoutManager) {
                int lastPositon = 0;
                lastPositon = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                int itemCount = recyclerView.getAdapter().getItemCount();
                //通过isLoadingMore来判断是否正在加载，避免重复加载
                if (!isLoadingMore && dy >= 0 && lastPositon == itemCount - 2) {
                    isLoadingMore = true;
                    LogUtil.d("footer", "loadMore...");
                    loadMore();
                }
            }
            super.onScrolled(recyclerView, dx, dy);
        }

        protected abstract void loadMore();

    }

}
