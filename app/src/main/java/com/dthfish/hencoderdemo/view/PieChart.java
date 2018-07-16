package com.dthfish.hencoderdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dthfish.hencoderdemo.Utils;

/**
 * Description
 * Author DthFish
 * Date  2018/7/16.
 */

public class PieChart extends View {
    private static final float PADDING = Utils.dpToPx(40);
    private static final float OFFSET = Utils.dpToPx(10);

    private float[] parts = {50, 70, 100, 80, 60};
    private int[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.LTGRAY, Color.YELLOW};
    private int pullOutIndex = 3;
    private RectF rectF = new RectF();

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public PieChart(Context context) {
        super(context);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float radius = Math.min(getWidth() / 2, getHeight() / 2) - PADDING;
        rectF.set(getWidth() / 2 - radius, getHeight() / 2 - radius,
                getWidth() / 2 + radius, +getHeight() / 2 + radius);

        float usedAngle = 0;
        for (int i = 0; i < parts.length; i++) {
            paint.setColor(colors[i]);
            if (i == pullOutIndex) {
                double angle = (usedAngle + parts[i] / 2) / 180 * Math.PI;
                rectF.offset((float) Math.cos(angle) * OFFSET,
                        (float) Math.sin(angle) * OFFSET);
                canvas.drawArc(rectF, usedAngle, parts[i], true, paint);
                rectF.offset(-(float) Math.cos(angle) * OFFSET,
                        -(float) Math.sin(angle) * OFFSET);
            } else {
                canvas.drawArc(rectF, usedAngle, parts[i], true, paint);
            }
            usedAngle += parts[i];
        }

    }
}
