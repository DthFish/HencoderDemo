package com.dthfish.hencoderdemo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import com.dthfish.hencoderdemo.R;
import com.dthfish.hencoderdemo.Utils;

/**
 * Description
 * Author DthFish
 * Date  2018/7/27.
 */
public class ScalableImageView extends View implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, Runnable, ScaleGestureDetector.OnScaleGestureListener {
    public static final float IMAGE_SIZE = Utils.dpToPx(400);
    private Bitmap bitmap;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float bitmapHeight;
    private float bitmapWidth;
    private float smallScale = 0;
    private float bigScale = 0;
    private float currentScale = 0;
    private float savedCurrentScale = 0;

    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private boolean isBig;
    private ObjectAnimator animator;
    private float touchOffsetX;
    private float touchOffsetY;
    private OverScroller scroller;


    public ScalableImageView(Context context) {
        super(context);
        init(context, null);
    }

    public ScalableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        bitmap = Utils.getAvatar(getResources(), R.drawable.beijing, IMAGE_SIZE);
        gestureDetector = new GestureDetector(context, this);
        gestureDetector.setOnDoubleTapListener(this);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
        scroller = new OverScroller(context);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        bitmapWidth = IMAGE_SIZE;
        bitmapHeight = bitmap.getHeight();

        if (bitmapWidth / bitmapHeight > getWidth() / getHeight()) {
            smallScale = getWidth() / bitmapWidth;
            bigScale = getHeight() / bitmapHeight;
        } else {
            bigScale = getWidth() / bitmapWidth;
            smallScale = getHeight() / bitmapHeight;
        }
        currentScale = smallScale;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float tempScale = currentScale;
        float faction = (currentScale - smallScale) / (bigScale - smallScale);
        canvas.translate(touchOffsetX * faction, touchOffsetY * faction);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.scale(tempScale, tempScale, 0, 0);
        canvas.drawBitmap(bitmap, -bitmapWidth / 2, -bitmapHeight / 2, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getPointerCount() > 1) {
            return scaleGestureDetector.onTouchEvent(event);
        }
        return gestureDetector.onTouchEvent(event);
    }

    @SuppressWarnings("unused")
    public float getCurrentScale() {
        return currentScale;
    }

    @SuppressWarnings("unused")
    public void setCurrentScale(float currentScale) {
        float fixedCurrentScale = currentScale;
        if (currentScale < smallScale) {
            fixedCurrentScale = smallScale;
            isBig = false;
        } else if (currentScale > bigScale) {
            fixedCurrentScale = bigScale;
            isBig = true;
        }
        this.currentScale = fixedCurrentScale;
        invalidate();
    }

    public ObjectAnimator getAnimator() {
        if (animator == null) {
            animator = ObjectAnimator.ofFloat(this, "currentScale", smallScale, bigScale);
            animator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation, boolean isReverse) {
                    if (isReverse) {
                        touchOffsetX = touchOffsetY = 0;
                    }
                }
            });
        }
        return animator;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        isBig = !isBig;
        if (isBig) {
            touchOffsetX = getWidth() / 2 - e.getX();
            touchOffsetY = getHeight() / 2 - e.getY();
            rectifyOffset();
            getAnimator().start();
        } else {
            getAnimator().reverse();
        }
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {

        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (isBig) {
            touchOffsetX -= distanceX;
            touchOffsetY -= distanceY;

            rectifyOffset();
            invalidate();
        }
        return false;
    }

    private void rectifyOffset() {
        touchOffsetX = Math.min(touchOffsetX, (bigScale * bitmapWidth - getWidth()) / 2);
        touchOffsetX = Math.max(touchOffsetX, -(bigScale * bitmapWidth - getWidth()) / 2);
        touchOffsetY = Math.min(touchOffsetY, (bigScale * bitmapHeight - getHeight()) / 2);
        touchOffsetY = Math.max(touchOffsetY, -(bigScale * bitmapHeight - getHeight()) / 2);
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        scroller.fling((int) touchOffsetX, (int) touchOffsetY, (int) velocityX, (int) velocityY,
                (int) (getWidth() - bitmapWidth * bigScale) / 2, (int) (bitmapWidth * bigScale - getWidth()) / 2,
                (int) (getHeight() - bitmapHeight * bigScale) / 2, (int) (bitmapHeight * bigScale - getHeight()) / 2);
        postOnAnimation(this);
        return false;
    }

    @Override
    public void run() {
        if (scroller.computeScrollOffset()) {
            touchOffsetX = scroller.getCurrX();
            touchOffsetY = scroller.getCurrY();
            invalidate();
            postOnAnimation(this);
        }
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        setCurrentScale(savedCurrentScale * detector.getScaleFactor());
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        savedCurrentScale = currentScale;
        touchOffsetX = getWidth() / 2 - detector.getFocusX();
        touchOffsetY = getHeight() / 2 - detector.getFocusY();
        rectifyOffset();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }
}
