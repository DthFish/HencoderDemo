package com.dthfish.hencoderdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dthfish.hencoderdemo.Utils;

/**
 * Description
 * Author DthFish
 * Date  2018/7/16.
 */

public class DashView extends View {

    private static final float PADDING = Utils.dpToPx(40);
    private static final int ANGLE = 120;
    private static final int TOTAL_COUNT = 20;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path dash = new Path();
    private Path path = new Path();
    private PathMeasure pathMeasure = new PathMeasure();

    private PathDashPathEffect pathEffect;

    private float dashWidth = Utils.dpToPx(1);

    private float dashHeight = Utils.dpToPx(5);
    private float value = 2;

    public DashView(Context context) {
        super(context);
    }

    public DashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Utils.dpToPx(1));
        paint.setTextSize(Utils.dpToPx(15));
        dash.addRect(0, 0, dashWidth, dashHeight, Path.Direction.CCW);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float radius = Math.min(getWidth() / 2, getHeight() / 2) - PADDING;
        canvas.drawArc(getWidth() / 2 - radius, getHeight() / 2 - radius,
                getWidth() / 2 + radius, getHeight() / 2 + radius,
                (180 - ANGLE) / 2 + ANGLE, 360 - ANGLE, false, paint);

        path.addArc(getWidth() / 2 - radius, getHeight() / 2 - radius,
                getWidth() / 2 + radius, getHeight() / 2 + radius,
                (180 - ANGLE) / 2 + ANGLE, 360 - ANGLE);

        pathMeasure.setPath(path, false);
        if (pathEffect == null) {
            pathEffect = new PathDashPathEffect(dash, (pathMeasure.getLength() - dashWidth) / TOTAL_COUNT, 0, PathDashPathEffect.Style.ROTATE);
        }
        paint.setPathEffect(pathEffect);
        canvas.drawArc(getWidth() / 2 - radius, getHeight() / 2 - radius,
                getWidth() / 2 + radius, getHeight() / 2 + radius,
                (180 - ANGLE) / 2 + ANGLE, 360 - ANGLE, false, paint);
        paint.setPathEffect(null);


        float x = (float) Math.cos(getAngle()) * radius;
        float y = (float) Math.sin(getAngle()) * radius;
//        canvas.drawText("Angle:" + (90 + ANGLE / 2 + (360 - ANGLE) * value / TOTAL_COUNT) + "\n"
//                + ",x:" + x + ",y:" + y, 50, 100, paint);
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.drawLine(0, 0, x, y, paint);
        canvas.restore();

    }

    public void setValue(float value) {
        this.value = value;
        invalidate();
    }

    private float getAngle() {
        return (float) ((90 + ANGLE / 2 + (360 - ANGLE) * value / TOTAL_COUNT) / 180 * Math.PI);
    }
}
