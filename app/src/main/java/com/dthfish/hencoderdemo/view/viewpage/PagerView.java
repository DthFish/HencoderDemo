package com.dthfish.hencoderdemo.view.viewpage;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

/**
 * Description ViewGroup，类似 ViewPager 但是没有蛋疼的刷新机制
 * 目的是用于不用重构太多，来替换公司原先用 Visible 和 Gone 来实现的面板切换需求
 * ps:业务太多了，我不敢下手，算了我还是重新写个 ViewGroup 吧
 * Author DthFish
 * Date  2018/8/9.
 */
public class PagerView extends ViewGroup {
    private float downX;
    private float downY;
    private float downScrollX;
    private boolean scrolling;
    private float minVelocity;
    private float maxVelocity;
    private OverScroller overScroller;
    private ViewConfiguration viewConfiguration;
    private VelocityTracker velocityTracker = VelocityTracker.obtain();
    private OnPageChangedListener listener;
    private int currentPage = 0;

    public PagerView(Context context) {
        this(context, null);
    }

    public PagerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        overScroller = new OverScroller(context);
        viewConfiguration = ViewConfiguration.get(context);
        maxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        minVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        int childTop = 0;
        int childRight = getWidth();
        int childBottom = getHeight();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.layout(childLeft, childTop, childRight, childBottom);
            childLeft += getWidth();
            childRight += getWidth();
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear();
        }
        velocityTracker.addMovement(ev);

        boolean result = false;
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                downScrollX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = downX - ev.getX();
                if (!scrolling) {
                    if (Math.abs(dx) > viewConfiguration.getScaledPagingTouchSlop()) {
                        scrolling = true;
                        getParent().requestDisallowInterceptTouchEvent(true);
                        result = true;
                    }
                }
                break;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear();
        }
        velocityTracker.addMovement(event);

        int childCount = getChildCount();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                downScrollX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = downX - event.getX() + downScrollX;
                if (childCount > 1) {
                    if (dx > getWidth() * (childCount - 1)) {
                        dx = getWidth() * (childCount - 1);
                    } else if (dx < 0) {
                        dx = 0;
                    }
                    scrollTo((int) (dx), 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                scrolling = false;
                getParent().requestDisallowInterceptTouchEvent(false);
                if (childCount > 1) {
                    velocityTracker.computeCurrentVelocity(1000, maxVelocity);
                    float vx = velocityTracker.getXVelocity();
                    int scrollX = getScrollX();
                    int targetPage;
                    int page = scrollX / getWidth();
                    if (page + 1 >= childCount) {
                        page = childCount - 2;
                    }
                    if (Math.abs(vx) < minVelocity) {
                        targetPage = scrollX % getWidth() > getWidth() / 2 ? page + 1 : page;
                    } else {
                        targetPage = vx < 0 ? page + 1 : page;
                    }


                    int scrollDistance = targetPage * getWidth() - scrollX;
                    overScroller.startScroll(getScrollX(), 0, scrollDistance, 0);
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                break;
        }
        return true;
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
    }

    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
    }

    public void setCurrentPage(int page, boolean smooth) {
        int rectifyPage = page;
        int childCount = getChildCount();
        if (rectifyPage > childCount - 1) {
            rectifyPage = childCount - 1;
        }

        if (rectifyPage < 0) {
            rectifyPage = 0;
        }
        if (getChildCount() > 1) {
            if (currentPage != rectifyPage) {
                if (smooth) {
                    int scrollDistance = rectifyPage * getWidth() - getScrollX();
                    overScroller.startScroll(getScrollX(), 0, scrollDistance, 0);
                    invalidate();
                } else {
                    currentPage = rectifyPage;
                    scrollTo(currentPage * getWidth(), 0);
                    if (listener != null) {
                        listener.onPageChanged(rectifyPage);
                    }
                }
            }
        }

    }

    @Override
    public void computeScroll() {
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.getCurrX(), overScroller.getCurrY());
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            int i = getScrollX() / getWidth();
            int mod = getScrollX() % getWidth();
            if (i != currentPage && mod == 0) {
                currentPage = i;
                if (listener != null) {
                    listener.onPageChanged(i);
                }
            }
        }
    }

    public interface OnPageChangedListener {
        void onPageChanged(int page);
    }

    public void setOnPageChangedListener(OnPageChangedListener listener) {
        this.listener = listener;
    }
}
