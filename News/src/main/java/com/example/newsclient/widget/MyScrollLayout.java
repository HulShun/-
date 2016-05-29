package com.example.newsclient.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

import com.example.newsclient.R;

/**
 * Created by Administrator on 2016-05-17.
 */
public class MyScrollLayout extends LinearLayout {
    private static final int MOVE_INT = 50;   //下滑可以，切换滑动对象的临界值
    private View briefView_nolimit, briefView_limit;
    private Button tv_btn;

    private View moreBtn;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;

    private int topViewHeight;
    private boolean isTopHidden;   //可以上滑隐藏的部分是否已经隐藏
    private float mLastY;
    /*
   * 滑动助手类，
   * */
    private OverScroller mOverScroller;
    /*
  * 触摸屏跟踪事件,处理平滑移动
  * */
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;    //系统默认的滑动和点击的临界值
    private int mMaxVelocity, mMinVelocity;  //触控滑动速率
    private boolean mDragging;     //是否拖拽

    public MyScrollLayout(Context context) {
        super(context);
    }

    public MyScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mOverScroller = new OverScroller(context);
        mVelocityTracker = VelocityTracker.obtain();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();

    }

    public MyScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        briefView_limit = findViewById(R.id.brief_text);
        briefView_nolimit = findViewById(R.id.brief_text_nolimit);
        moreBtn = findViewById(R.id.brief_more_btn);
        tv_btn = (Button) findViewById(R.id.brief_tv_btn);
        relativeLayout = (RelativeLayout) findViewById(R.id.brief_rlayout);
        recyclerView = (RecyclerView) findViewById(R.id.brief_more_rv);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //recyclerview的最终显示高度
        ViewGroup.LayoutParams lp = recyclerView.getLayoutParams();
        lp.height = getMeasuredHeight()
                - relativeLayout.getHeight() - 120;
        recyclerView.setLayoutParams(lp);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int textHeight;
        textHeight = briefView_limit.getMeasuredHeight(); //要取得是整个view的高度，getHeight()是取在屏幕上看到的高度
        topViewHeight = textHeight + tv_btn.getMeasuredHeight();

    }

    public void notifyTextViewHeighChanged(final int flag) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flag == 0) {
                    topViewHeight = briefView_limit.getMeasuredHeight() + tv_btn.getMeasuredHeight();
                } else {
                    topViewHeight = briefView_nolimit.getMeasuredHeight() + tv_btn.getMeasuredHeight();
                }

            }
        }, 500);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float y = event.getY();  //触摸点的位置
        //向下滑动时候 dy为负值

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();  //停止滑动
                }
                mLastY = y;
                return true;

            case MotionEvent.ACTION_MOVE:
                float dy = mLastY - y; //滑动距离
                if (!mDragging && Math.abs(dy) > mTouchSlop) {
                    mDragging = true;
                }

                if (mDragging) {
                    scrollBy(0, (int) dy);  //滑动
                    if (dy > 0 && getScrollY() == topViewHeight) {    //向上滑动，并且getScrollY()--->布局在Y轴上相对于最上角被移动的距离
                        //子view从Down开始拦截动作
                        event.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(event);
                        isTopHidden = true;
                    }
                }


                if (isTopHidden && dy < 0) {
                    return false;
                }

                mLastY = y;
                break;

            //flying惯性
            case MotionEvent.ACTION_UP:
                mDragging = false;
                //初始化速率。每秒可移动1000点像素，最快为mMaxVelocity速率
                mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinVelocity) {
                    mOverScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, topViewHeight);
                    invalidate();   //屏幕刷新
                }

                mVelocityTracker.clear();
                break;

        }
        return false;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) y = 0;
        if (y > topViewHeight) y = topViewHeight;
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }

        isTopHidden = getScaleY() == topViewHeight;
    }

    @Override
    public void computeScroll() {
        //每一个位置的来刷新界面，实现滚动效果
        if (mOverScroller.computeScrollOffset()) {  //如果还在滚动，则会返回true
            scrollTo(0, mOverScroller.getCurrY());
            invalidate();
        }
    }

    /*
* 截获触摸事件，返回true表示当前view将处理这个事件，并中断事件传递。返回false表示将事件传给子view去处理
* 该方法会在onTouchEvent() 之前被调用
* */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int y = (int) ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = mLastY - y;

                if (Math.abs(dy) > mTouchSlop) {    //是滑动，不是点击
                    mDragging = true;
                    /*如果头部布局没有隐藏  或者 ListView在开始位置，头部布局隐藏，但是是向下滑动的 */
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int position = manager.findFirstVisibleItemPosition();
                    View v = manager.findViewByPosition(position);
                    boolean flag = false;
                    if (v != null) {
                        flag = (manager.findViewByPosition(position).getTop() <= MOVE_INT &&
                                position == 0 &&
                                isTopHidden &&
                                dy < 0);
                    }
                    if (!isTopHidden || flag) {
                        return true;   //由当前View来消费这个事件
                    }
                }
                break;
        }

        return false;
    }
}
