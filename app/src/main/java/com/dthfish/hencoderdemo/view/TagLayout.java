package com.dthfish.hencoderdemo.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

/**
 * Description
 * Author DthFish
 * Date  2018/7/22.
 */
public class TagLayout extends ViewGroup {

    private Rect[] childrenRects;

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int childCount = getChildCount();
        if (childrenRects == null) {
            childrenRects = new Rect[childCount];
        } else if (childrenRects.length != childCount) {
            childrenRects = Arrays.copyOf(childrenRects, childCount);
        }
        int widthUsed = 0;
        int heightUsed = 0;
        int maxHeight = 0;
        int totalWidthUsed = 0;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            Rect childRect = childrenRects[i];
            // 推荐方案1：widthUsed 填 0，让子 view 放飞自我尽情测量，不需要考虑剩余的宽度，
            // 最大宽度可以为 TagLayout 的宽度减去 （getPaddingStart + getPaddingEnd）；对比方案2，不需要对 textview 单独处理
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);

            int tempWidth = child.getMeasuredWidth();
//            // 方案2：51行的位置 widthUsed 参数填 widthUsed 累加值
//            // 目前只支持单行文字，还需要对文字长度比 TagLayout 宽度长的情况进行处理
//            // {#tempWidth}这里主要是针对 TextView wrap_content 的时候会截断文字进行的处理
//            if (TextView.class.isAssignableFrom(child.getClass())) {
//                TextView textView = (TextView) child;
//                tempWidth = (int) (textView.getPaint().measureText(textView.getText().toString())
//                        + textView.getPaddingLeft() + textView.getPaddingRight());
//            }
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            // 如果要支持 margin，这里判断是否需要换行的时候要加上左右 margin，
            // 因为{ measureChildWithMargins } 里面的代码可以知道，子 view 的宽度是没有包含 margin 的
            if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED &&
                    ((tempWidth + widthUsed + layoutParams.leftMargin + layoutParams.rightMargin
                            + getPaddingStart() + getPaddingEnd() >
                            MeasureSpec.getSize(widthMeasureSpec)) ||
                            ((child.getMeasuredWidthAndState() & MEASURED_STATE_TOO_SMALL) != 0))) {

                widthUsed = 0;
                heightUsed += maxHeight;
                maxHeight = 0;
                measureChildWithMargins(child, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
            }


            if (childRect == null) {
                childRect = childrenRects[i] = new Rect();
            }
            childRect.set(widthUsed, heightUsed,
                    widthUsed + child.getMeasuredWidth(),
                    heightUsed + child.getMeasuredHeight());
            childRect.offset(layoutParams.leftMargin, layoutParams.topMargin);
            maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
            widthUsed += child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            totalWidthUsed = Math.max(totalWidthUsed, widthUsed);

        }
        int width = totalWidthUsed;
        int height = heightUsed + maxHeight;

        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0),
                resolveSizeAndState(height, heightMeasureSpec, 0));


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Rect childRect = childrenRects[i];
            child.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
    }

    @Override
    public MarginLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected MarginLayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected MarginLayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,
                MarginLayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }
}
