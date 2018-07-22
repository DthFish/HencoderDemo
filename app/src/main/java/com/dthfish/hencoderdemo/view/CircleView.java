package com.dthfish.hencoderdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dthfish.hencoderdemo.Utils;

/**
 * Description
 * Author DthFish
 * Date  2018/7/22.
 */
public class CircleView extends View {

    public static final float PADDING = Utils.dpToPx(10);
    public static final float RADIUS = Utils.dpToPx(30);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = (int) ((PADDING + RADIUS) * 2);
        setMeasuredDimension(resolveSizeAndState(size, widthMeasureSpec, 0),
                resolveSizeAndState(size, heightMeasureSpec, 0));
    }

    {
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.LTGRAY);
        canvas.drawCircle(PADDING + RADIUS, PADDING + RADIUS, RADIUS, paint);
    }
}
