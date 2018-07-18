package com.dthfish.hencoderdemo.jbox2d;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.jbox2d.dynamics.Body;

/**
 * Description
 * Author DthFish
 * Date  2018/7/17.
 */
public class CircleColor extends BaseBody {

    float radius;

    public CircleColor(Body body, float radius, int color) {
        this.body = body;
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void drawSelf(Canvas canvas, Paint paint) {
        paint.setColor(color & 0x8CFFFFFF);
        float x = body.getPosition().x * Constant.RATE;
        float y = body.getPosition().y * Constant.RATE;
        canvas.drawCircle(x, y, radius, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawCircle(x, y, radius, paint);
        paint.reset();

    }
}
