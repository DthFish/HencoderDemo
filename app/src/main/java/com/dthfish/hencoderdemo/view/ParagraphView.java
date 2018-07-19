package com.dthfish.hencoderdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.dthfish.hencoderdemo.R;
import com.dthfish.hencoderdemo.Utils;

/**
 * Description
 * Author DthFish
 * Date  2018/7/16.
 */

public class ParagraphView extends View {


    private static final int IMAGE_WIDTH = (int) Utils.dpToPx(150);
    private static final float IMAGE_OFFSET = Utils.dpToPx(80);
    private static final float FONT_SIZE = Utils.dpToPx(16);
    private String content = "Pellentesque in nisl sit amet lorem tincidunt facilisis ac ut ante. Integer justo felis, pharetra ac suscipit at, pulvinar vel tortor. Praesent lectus libero, condimentum in tellus sit amet, interdum efficitur lectus. Nullam sed ex mattis, consectetur nulla vel, fringilla nisl. Fusce orci ligula, commodo venenatis porta ut, egestas vitae ex. In hac habitasse platea dictumst. Sed posuere, massa eget ultrices feugiat, lectus nibh fringilla urna, sed placerat massa ante sit amet mauris. Aenean diam orci, accumsan eget blandit quis, imperdiet sit amet eros. Maecenas feugiat volutpat feugiat. In scelerisque ultricies dui a fermentum. In pretium neque eu quam ornare, ut eleifend nibh tincidunt. Sed ut diam viverra, sagittis purus fringilla, finibus erat. Integer sed arcu erat. Suspendisse imperdiet quam id eros dapibus convallis.";
    private TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private Paint.FontMetrics metrics = new Paint.FontMetrics();
    private StaticLayout staticLayout;

    private float[] measuredWidth = new float[1];

    public ParagraphView(Context context) {
        super(context);
    }

    public ParagraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ParagraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setTextSize(FONT_SIZE);
        paint.getFontMetrics(metrics);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        staticLayout = new StaticLayout(content, paint, getMeasuredWidth(), Layout.Alignment.ALIGN_NORMAL,
//                1, 0, false);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        staticLayout.draw(canvas);

        int start = 0;
        float offsetY = -metrics.top;
        float textWidth;
        if (offsetY > IMAGE_OFFSET && offsetY < IMAGE_OFFSET + IMAGE_WIDTH ||
                offsetY + metrics.bottom > IMAGE_OFFSET && offsetY + metrics.bottom < IMAGE_OFFSET + IMAGE_WIDTH) {
            textWidth = getWidth() - IMAGE_WIDTH;
        } else {
            textWidth = getWidth();
        }

        int count = paint.breakText(content, start, content.length(), true, textWidth, measuredWidth);
        while (count > 0) {
            canvas.drawText(content, start, start + count, 0, offsetY, paint);
            start += count;
            offsetY += paint.getFontSpacing();
            if (offsetY > IMAGE_OFFSET && offsetY < IMAGE_OFFSET + IMAGE_WIDTH ||
                    offsetY + metrics.bottom > IMAGE_OFFSET && offsetY + metrics.bottom < IMAGE_OFFSET + IMAGE_WIDTH) {
                textWidth = getWidth() - IMAGE_WIDTH;
            } else {
                textWidth = getWidth();
            }
            count = paint.breakText(content, start, content.length(), true, textWidth, measuredWidth);
        }
        canvas.drawBitmap(Utils.getAvatar(getResources(), R.drawable.huaji, IMAGE_WIDTH), getWidth() - IMAGE_WIDTH, IMAGE_OFFSET, paint);
    }

}
