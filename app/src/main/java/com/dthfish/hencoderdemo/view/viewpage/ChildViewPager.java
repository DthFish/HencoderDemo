package com.dthfish.hencoderdemo.view.viewpage;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Description 配合 PagerView 使用的 ViewPager 可以使 ViewPager 嵌套在 PagerView 里面并不影响两者翻页
 * Author DthFish
 * Date  2018/8/9.
 */
public class ChildViewPager extends ViewPager {

    private int startX;
    private int startY;

    public ChildViewPager(Context context) {
        super(context);
    }

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                int endX = (int) ev.getX();
                int endY = (int) ev.getY();

                if (Math.abs(endX - startX) > Math.abs(endY - startY) &&//左右滑动
                        ((endX > startX && getCurrentItem() == 0) ||
                                (endX < startX && getCurrentItem() == getAdapter().getCount() - 1))) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();

                break;

            case MotionEvent.ACTION_MOVE:

                int endX = (int) ev.getX();
                int endY = (int) ev.getY();

                if (Math.abs(endX - startX) > Math.abs(endY - startY)) {//左右滑动
                    if (endX > startX) {//右滑
                        if (getCurrentItem() == 0) {//第一个页面，需要父控件拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                            return false;
                        }
                    } else {//左滑
                        if (getCurrentItem() == getAdapter().getCount() - 1) {//最后一个页面，需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                            return false;
                        }
                    }

                }

                break;
        }

        boolean b = super.onTouchEvent(ev);
        getParent().requestDisallowInterceptTouchEvent(b);
        return b;
    }
}
	
