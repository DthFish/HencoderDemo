package com.dthfish.hencoderdemo.jbox2d;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.jbox2d.dynamics.Body;

/**
 * Description
 * Author DthFish
 * Date  2018/7/17.
 */
public class RectColor extends BaseBody {
    float halfWidth;
    float halfHeight;

    public RectColor(Body body, float halfWidth, float halfHeight, int color) {
        this.body = body;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
        this.color = color;
    }

    @Override
    public void drawSelf(Canvas canvas, Paint paint) {
        paint.setColor(color & 0x8CFFFFFF);
        float x = body.getPosition().x * Constant.RATE;
        float y = body.getPosition().y * Constant.RATE;
        canvas.drawRect(x - halfWidth, y - halfHeight,
                x + halfWidth, y + halfHeight, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawRect(x - halfWidth, y - halfHeight,
                x + halfWidth, y + halfHeight, paint);
        paint.reset();

    }
}
