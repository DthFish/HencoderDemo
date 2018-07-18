package com.dthfish.hencoderdemo.jbox2d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.jbox2d.dynamics.World;

import java.util.List;

/**
 * Description
 * Author DthFish
 * Date  2018/7/17.
 */
class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private final DrawThread mDrawThread;
    private final World mWorld;
    private final List<BaseBody> mBodies;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public GameView(Context context, World world, List<BaseBody> bodies) {
        super(context);
        mWorld = world;
        mBodies = bodies;
        getHolder().addCallback(this);

        mDrawThread = new DrawThread(this);
        mDrawThread.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null) {
            return;
        }

        canvas.drawARGB(255, 255, 255, 255);
        for (BaseBody body : mBodies) {
            body.drawSelf(canvas, mPaint);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        repaint();
    }

    @SuppressLint("WrongCall")
    private void repaint() {

        SurfaceHolder holder = getHolder();
        Canvas canvas = holder.lockCanvas();

        try {
            synchronized (holder) {
                onDraw(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    static class DrawThread extends Thread {


        private final GameView gv;

        public DrawThread(GameView gv) {
            this.gv = gv;
        }

        @Override
        public void run() {

            while (Constant.DRAW_THREAD_FLAG) {
                gv.mWorld.step(Constant.TIME_STEP, Constant.VELOCITY_ITERATIONS, Constant.POSITION_ITERATIONS);
                gv.repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
