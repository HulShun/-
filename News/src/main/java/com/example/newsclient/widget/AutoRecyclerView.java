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
                int lastPositon = 0, firstPosition = 0;
                lastPositon = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                int itemCount = recyclerView.getAdapter().getItemCount();
                //通过isLoadingMore来判断是否正在加载，避免重复加载
                if (!isLoadingMore && dy >= 0 && lastPositon == itemCount - 2) {
                    isLoadingMore = true;
                    LogUtil.d("footer", "loadMore...");
                    loadMore();
                } else if (dy <= 10) {
                    firstPosition = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
                    resumeLoadImg(firstPosition, lastPositon);
                } else {
                    pauseLoadImg();
                }
            }

            super.onScrolled(recyclerView, dx, dy);
        }


        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    int lastPositon = 0, firstPosition = 0;
                    LayoutManager manager = recyclerView.getLayoutManager();
                    if (manager instanceof LinearLayoutManager) {
                        lastPositon = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                        firstPosition = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
                    }
                    resumeLoadImg(firstPosition, lastPositon);
                    break;
                //正在滑动
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    pauseLoadImg();
                    break;
                //惯性滑动
                case RecyclerView.SCROLL_STATE_SETTLING:
                    pauseLoadImg();
                    break;
            }
            super.onScrollStateChanged(recyclerView, newState);
        }

        protected abstract void loadMore();

        protected abstract void pauseLoadImg();

        protected abstract void resumeLoadImg(int firstPosition, int lastPositon);


    }


}
