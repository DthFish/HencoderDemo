package com.dthfish.hencoderdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import com.dthfish.hencoderdemo.R;

/**
 * Description ScaleAnimLayout 会设置 clickable 所以不要给里面的子 View 设置点击事件，不然还是别用了
 * Author DthFish
 * Date  2018/8/22.
 */
public class ScaleAnimLayout extends LinearLayout implements View.OnTouchListener {
    private ScaleAnimation scaleAnimation;
    private DecorateInterpolator startInterpolator;
    private Interpolator reverseInterpolator = new DecelerateInterpolator();
    private float startAnimationProgress = 0;
    private float scaleRatio = 1.2f;
    private int duration = 100;

    public ScaleAnimLayout(Context context) {
        this(context, null);
    }

    public ScaleAnimLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleAnimLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScaleAnimLayout);
        duration = ta.getInteger(R.styleable.ScaleAnimLayout_duration, 100);
        scaleRatio = ta.getFloat(R.styleable.ScaleAnimLayout_scaleRatio, 1.2f);
        ta.recycle();

        scaleAnimation = new ScaleAnimation(1, scaleRatio, 1, scaleRatio,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(duration);

        startInterpolator = new DecorateInterpolator(new AccelerateInterpolator());
        startInterpolator.setListener(new OnProgressListener() {
            @Override
            public void onProgress(float progress) {
                startAnimationProgress = progress;
            }
        });
        scaleAnimation.setInterpolator(startInterpolator);
        setClickable(true);
        setOnTouchListener(this);

    }

    public void setDuration(int duration) {
        this.duration = duration;
        scaleAnimation.setDuration(duration);
    }

    public void setScaleRatio(float scaleRatio) {
        this.scaleRatio = scaleRatio;
        scaleAnimation = new ScaleAnimation(1, scaleRatio, 1, scaleRatio,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setInterpolator(startInterpolator);
        scaleAnimation.setDuration(duration);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (scaleAnimation != null) {
                    clearAnimation();
                    startAnimation(scaleAnimation);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                clearAnimation();
                float currentScaleRatio = 1 + (scaleRatio - 1) * startAnimationProgress;
                int durationMillis = (int) (this.duration * startAnimationProgress);
                ScaleAnimation reverseAnimation = new ScaleAnimation(currentScaleRatio, 1, currentScaleRatio, 1,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                reverseAnimation.setDuration(durationMillis);
                reverseAnimation.setInterpolator(reverseInterpolator);
                startAnimation(reverseAnimation);

                break;
        }
        return false;
    }

    public interface OnProgressListener {

        void onProgress(float progress);
    }

    public static class DecorateInterpolator implements Interpolator {

        private final Interpolator origin;
        private OnProgressListener listener;

        public DecorateInterpolator(Interpolator origin) {
            this.origin = origin;
        }

        @Override
        public float getInterpolation(float input) {
            if (listener != null) {
                listener.onProgress(input);
            }

            return origin.getInterpolation(input);
        }

        public void setListener(OnProgressListener listener) {
            this.listener = listener;
        }
    }

}
