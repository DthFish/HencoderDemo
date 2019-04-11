package com.dthfish.hencoderdemo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dthfish.hencoderdemo.R;
import com.dthfish.hencoderdemo.Utils;

import java.util.LinkedList;

/**
 * Description
 * Author zhaolizhi
 * Date  2019/4/10.
 */
public class RippleButton extends FrameLayout implements View.OnClickListener {

    private LinkedList<FloatWrap> rippleQueue = new LinkedList<>();

    private Paint paint = new Paint();
    private OnClickListener outClickListener;
    private int centerX;
    private int centerY;
    private int radius;
    private ImageView imageView;
    // 波纹起始透明度
    private float rippleStartAlpha = 0.4f;
    // 这里的比例是图片里面的圆半径和圆边到空间边框的距离的比例
    private float spaceProportion = 0.2f;
    private float imageProportion = 1 - spaceProportion;

    public RippleButton(@NonNull Context context) {
        this(context, null);
    }

    public RippleButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        imageView = new ImageView(context);
        imageView.setBackgroundColor(Color.TRANSPARENT);
        imageView.setClickable(true);
        imageView.setImageResource(R.drawable.bg_ripple_selector);
        addView(imageView, new LayoutParams((int) Utils.dpToPx(200), (int) Utils.dpToPx(200)));
        setOnClickListener(null);
        setWillNotDraw(false);

        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#FFFF5A7B"));
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        outClickListener = l;
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final FloatWrap fraction = new FloatWrap(0f);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(500);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rippleQueue.poll();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationStart(Animator animation) {
                rippleQueue.offer(fraction);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fraction.fraction = animation.getAnimatedFraction();
                postInvalidateOnAnimation();

            }
        });
        valueAnimator.start();

        if (outClickListener != null) {
            outClickListener.onClick(v);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        radius = Math.min(w, h) / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (FloatWrap ripple : rippleQueue) {
            drawRipple(ripple, canvas, paint);
        }
    }

    private void drawRipple(FloatWrap ripple, Canvas canvas, Paint paint) {
        if (Math.abs(0 - ripple.fraction) > 0.01) {
            float strokeWidth = radius * spaceProportion * ripple.fraction;
            float tempRadius = radius * (imageProportion + spaceProportion * ripple.fraction);
            paint.setStrokeWidth(strokeWidth);
            paint.setAlpha((int) (255 * rippleStartAlpha * (1 - ripple.fraction)));
            canvas.drawCircle(centerX, centerY, tempRadius - strokeWidth / 2, paint);
        }
    }

    static class FloatWrap {
        float fraction;

        FloatWrap(float fraction) {
            this.fraction = fraction;
        }
    }

}
