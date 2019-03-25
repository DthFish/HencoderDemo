package com.dthfish.hencoderdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dthfish.hencoderdemo.R;

/**
 * Description
 * Author zhaolizhi
 * Date  2019/3/25.
 */
public class AnimationButton extends FrameLayout {

    private ImageView ivBack = null;
    private ImageView ivFront = null;
    private Bitmap backBitmap = null;
    private Bitmap frontBitmap = null;
    private Bitmap beforeBitmap = null;
    private boolean isAnimation = false;
    private boolean isReady = false;
    private boolean needResume = false;

    public AnimationButton(@NonNull Context context) {
        this(context, null);
    }

    public AnimationButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AnimationButton);
        int back = ta.getResourceId(R.styleable.AnimationButton_backgroundIcon, R.drawable.icon_back);
        int front = ta.getResourceId(R.styleable.AnimationButton_foregroundIcon, R.drawable.icon_front);

        ta.recycle();

        ivBack = new ImageView(context);
        backBitmap = BitmapFactory.decodeResource(context.getResources(), back);
        frontBitmap = BitmapFactory.decodeResource(context.getResources(), front);

        ivBack.setImageBitmap(backBitmap);
        ivFront = new ImageView(context);
        ivFront.setImageBitmap(frontBitmap);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.END;
        ivFront.setVisibility(View.GONE);
        addView(ivBack);
        addView(ivFront, lp);
        isReady = true;
    }

    public void startAnimation() {
        if (isAnimation || !isReady) {
            return;
        }
        final int length = backBitmap.getWidth() - frontBitmap.getWidth();

        beforeBitmap = Bitmap.createBitmap(backBitmap.getWidth(), backBitmap.getHeight(), backBitmap.getConfig());
        final Canvas canvas = new Canvas(beforeBitmap);
        canvas.drawBitmap(backBitmap, 0f, 0f, null);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        final RectF rect = new RectF(0f, 0f, frontBitmap.getWidth() * 0.5f, frontBitmap.getHeight());

        Animation animation = new TranslateAnimation(Animation.ABSOLUTE, -length, Animation.ABSOLUTE, 0f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ivFront.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimation = false;
                ivBack.setVisibility(View.GONE);
                beforeBitmap.recycle();
                beforeBitmap = null;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation.setDuration(500);
        animation.setFillBefore(true);
        animation.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                rect.offsetTo(length * input, 0f);
                canvas.drawRect(rect, paint);
                ivBack.setImageBitmap(beforeBitmap);
                return input;
            }
        });

        needResume = true;
        ivFront.startAnimation(animation);
    }

    /**
     * 如果在{@see startAnimation()}过程中，打开一个 Activity 再打开一个 Activity或者 dialog 就会出现
     * View 消失的问题，应该是 view 动画的原因
     */
    public void resume() {
        if (needResume && isReady) {
            isAnimation = false;
            ivBack.setVisibility(View.GONE);
            ivFront.setVisibility(View.VISIBLE);
            needResume = false;
        }
    }

    public void reset() {
        ivBack.setVisibility(View.VISIBLE);
        ivFront.setVisibility(View.GONE);
        ivBack.setImageBitmap(backBitmap);

    }
}
