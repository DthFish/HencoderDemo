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
 * Date  2018/7/16.
 */

public class SportView extends View {

    private static final float PADDING = Utils.dpToPx(40);
    private static final float STROKE_WIDTH = Utils.dpToPx(20);
    private static final float FONT_SIZE = Utils.dpToPx(80);

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float percent = 0.75f;
    private String contentText = "sunny";
    private Paint.FontMetrics metrics = new Paint.FontMetrics();

    public SportView(Context context) {
        super(context);
    }

    public SportView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SportView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {

        paint.setStrokeCap(Paint.Cap.ROUND);

        paint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        float radius = Math.min(getWidth() / 2, getHeight() / 2) - PADDING;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
        paint.setColor(Color.BLUE);
        canvas.drawArc(getWidth() / 2 - radius, getHeight() / 2 - radius,
                getWidth() / 2 + radius, getHeight() / 2 + radius, -90, getAngle(), false, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(FONT_SIZE);
        paint.setColor(Color.MAGENTA);
        paint.getFontMetrics(metrics);
        canvas.drawText(contentText, getWidth() / 2, getHeight() / 2 - (metrics.ascent + metrics.descent) / 2, paint);

    }

    public void setPercent(float percent) {
        this.percent = percent;
        invalidate();
    }

    private float getAngle() {

        return percent * 360;
    }
}
