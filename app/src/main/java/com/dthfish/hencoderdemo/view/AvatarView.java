package com.dthfish.hencoderdemo.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dthfish.hencoderdemo.R;
import com.dthfish.hencoderdemo.Utils;

/**
 * Description
 * Author DthFish
 * Date  2018/7/16.
 */

public class AvatarView extends View {
    private static final float PADDING = Utils.dpToPx(15);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    public AvatarView(Context context) {
        super(context);
    }

    public AvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AvatarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float radius = Math.min(getWidth() / 2, getHeight() / 2) - PADDING;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);

        int save = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius - PADDING, paint);
        paint.setXfermode(xfermode);
        canvas.drawBitmap(Utils.getAvatar(getResources(), R.drawable.huaji, (int) radius * 2), getWidth() / 2 - radius, getHeight() / 2 - radius, paint);
        paint.setXfermode(null);

        canvas.restoreToCount(save);

    }

}
