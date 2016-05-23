package com.example.newsclient.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
            } else if (manager instanceof StaggeredGridLayoutManager) {
                int last[] = ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(null);
                int lastItem = recyclerView.getAdapter().getItemCount() - 2;
                if (!isLoadingMore && dy > 0) {
                    for (int i = 0; i < last.length; i++) {
                        if (last[i] == lastItem) {
                            isLoadingMore = true;
                            loadMore();
                        }

                    }


                }

            }
            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter == null) {
                return;
            }
            //滑动停止
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                LayoutManager manager = recyclerView.getLayoutManager();
                if (manager instanceof LinearLayoutManager) {
                    int first = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
                    int last = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                    resumeLoadImage((LinearLayoutManager) manager, first, last);
                }
            } else {
                pauseLoadImage();
            }


        }

        protected abstract void loadMore();

        protected void resumeLoadImage(LinearLayoutManager manager, int first, int last) {
        }

        protected void pauseLoadImage() {
        }
    }

}
