package com.dthfish.hencoderdemo.view;

import android.animation.FloatEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.dthfish.hencoderdemo.R;

/**
 * Description ScaleAnimLayout 会设置 clickable 所以不要给里面的子 View 设置点击事件，不然还是别用了
 * Author DthFish
 * Date  2018/8/22.
 */
public class TouchAnimLayout extends LinearLayout implements View.OnTouchListener {
    private DecorateInterpolator startInterpolator;
    private Interpolator reverseInterpolator = new DecelerateInterpolator();
    private float startAnimationProgress = 0;
    private int duration = 100;
    private ReverseEvaluator reverseEvaluator;
    private AnimationSet startAnimationSet;
    private float scaleRatio = 1f;
    private float rotateDegree = 0f;
    private float translationXRatio = 0f;
    private float translationYRatio = 0f;

    public TouchAnimLayout(Context context) {
        this(context, null);
    }

    public TouchAnimLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchAnimLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TouchAnimLayout);
        duration = ta.getInteger(R.styleable.TouchAnimLayout_duration, 100);
        scaleRatio = ta.getFloat(R.styleable.TouchAnimLayout_scaleRatio, 1f);
        rotateDegree = ta.getFloat(R.styleable.TouchAnimLayout_rotateDegree, 0f);
        translationXRatio = ta.getFloat(R.styleable.TouchAnimLayout_translationXRatio, 0f);
        translationYRatio = ta.getFloat(R.styleable.TouchAnimLayout_translationYRatio, 0f);
        ta.recycle();

        startAnimationSet = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1, scaleRatio, 1, scaleRatio,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        startAnimationSet.addAnimation(scaleAnimation);
        RotateAnimation rotateAnimation = new RotateAnimation(0, rotateDegree,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        startAnimationSet.addAnimation(rotateAnimation);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, translationXRatio,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, translationYRatio);
        startAnimationSet.addAnimation(translateAnimation);
        AccelerateInterpolator originInterpolator = new AccelerateInterpolator();
        reverseEvaluator = new ReverseEvaluator(originInterpolator);
        startInterpolator = new DecorateInterpolator(originInterpolator);
        startInterpolator.setListener(new OnProgressListener() {
            @Override
            public void onProgress(float progress) {
                startAnimationProgress = progress;
            }
        });
        startAnimationSet.setFillAfter(true);
        startAnimationSet.setDuration(duration);
        startAnimationSet.setInterpolator(startInterpolator);
        setClickable(true);
        setOnTouchListener(this);

    }

    public void setDuration(int duration) {
        this.duration = duration;
        startAnimationSet.setDuration(duration);
    }

    public void setReverseInterpolator(Interpolator reverseInterpolator) {
        this.reverseInterpolator = reverseInterpolator;
    }

    public void setStartInterpolator(Interpolator originInterpolator) {
        this.reverseEvaluator = new ReverseEvaluator(originInterpolator);
        this.startInterpolator = new DecorateInterpolator(originInterpolator);
        startAnimationSet.setInterpolator(startInterpolator);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (startAnimationSet != null) {
                    clearAnimation();
                    startAnimation(startAnimationSet);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                clearAnimation();

                AnimationSet reverseAnimationSet = new AnimationSet(true);

                Float currentScaleRatio = reverseEvaluator.evaluate(startAnimationProgress, 1, scaleRatio);
                int durationMillis = (int) (this.duration * startAnimationProgress);

                Float currentRotate = reverseEvaluator.evaluate(startAnimationProgress, 0, rotateDegree);

                Float currentX = reverseEvaluator.evaluate(startAnimationProgress, 0, translationXRatio);
                Float currentY = reverseEvaluator.evaluate(startAnimationProgress, 0, translationYRatio);

                RotateAnimation rotateAnimation = new RotateAnimation(currentRotate, 0,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                ScaleAnimation scaleAnimation = new ScaleAnimation(currentScaleRatio, 1, currentScaleRatio, 1,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, currentX,
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, currentY,
                        Animation.RELATIVE_TO_SELF, 0);

                reverseAnimationSet.addAnimation(rotateAnimation);
                reverseAnimationSet.addAnimation(scaleAnimation);
                reverseAnimationSet.addAnimation(translateAnimation);
                reverseAnimationSet.setDuration(durationMillis);
                reverseAnimationSet.setInterpolator(reverseInterpolator);
                startAnimation(reverseAnimationSet);

                break;
        }
        return false;
    }

    public interface OnProgressListener {

        void onProgress(float progress);
    }

    public static class ReverseEvaluator extends FloatEvaluator {
        private Interpolator interpolator;

        public ReverseEvaluator(Interpolator interpolator) {
            this.interpolator = interpolator;
        }

        @Override
        public Float evaluate(float fraction, Number startValue, Number endValue) {
            float factor = interpolator.getInterpolation(fraction);
            return super.evaluate(factor, startValue, endValue);
        }
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
