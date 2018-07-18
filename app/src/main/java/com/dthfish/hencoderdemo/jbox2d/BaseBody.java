package com.dthfish.hencoderdemo.jbox2d;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.jbox2d.dynamics.Body;

/**
 * Description
 * Author DthFish
 * Date  2018/7/17.
 */
public abstract class BaseBody {
    Body body;
    int color;

    public abstract void drawSelf(Canvas canvas, Paint paint);
}
