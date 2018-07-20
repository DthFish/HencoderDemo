package com.dthfish.hencoderdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dthfish.hencoderdemo.R;
import com.dthfish.hencoderdemo.Utils;

/**
 * Description
 * Author DthFish
 * Date  2018/7/18.
 */
public class CameraView extends View {

    private final int IMAGE_SIZE = (int) Utils.dpToPx(150);
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Camera camera = new Camera();
    Bitmap bitmap = Utils.getAvatar(getResources(), R.drawable.huaji, IMAGE_SIZE);

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(Utils.dpToPx(20));
        camera.setLocation(0, 0, Utils.getLocationZ());
    }

    private int rotateY = 0;
    private int rotateZ = 0;
    private int rotateLeftY = 0;

    public int getRotateY() {
        return rotateY;
    }

    public void setRotateY(int rotateY) {
        this.rotateY = rotateY;
        invalidate();
    }

    public int getRotateZ() {
        return rotateZ;
    }

    public void setRotateZ(int rotateZ) {
        this.rotateZ = rotateZ;
        invalidate();
    }

    public int getRotateLeftY() {
        return rotateLeftY;
    }

    public void setRotateLeftY(int rotateLeftY) {
        this.rotateLeftY = rotateLeftY;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("Click me please!", getWidth() / 2, 50, paint);
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.rotate(-rotateZ);
        canvas.clipRect(0, -IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE);
        camera.save();
        camera.rotateY(-rotateY);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.rotate(rotateZ);
        canvas.drawBitmap(bitmap, -IMAGE_SIZE / 2, -IMAGE_SIZE / 2, paint);
        canvas.translate(-getWidth() / 2, -getHeight() / 2);
        canvas.restore();

        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.rotate(-rotateZ);
        canvas.clipRect(-IMAGE_SIZE, -IMAGE_SIZE, 0, IMAGE_SIZE);
        camera.save();
        camera.rotateY(rotateLeftY);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.rotate(rotateZ);
        canvas.drawBitmap(bitmap, -IMAGE_SIZE / 2, -IMAGE_SIZE / 2, paint);
        canvas.translate(-getWidth() / 2, -getHeight() / 2);
        canvas.restore();

    }

    public void reset() {
        rotateY = 0;
        rotateLeftY = 0;
        rotateZ = 0;
        invalidate();
    }
}
