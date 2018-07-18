package com.dthfish.hencoderdemo.jbox2d;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.dthfish.hencoderdemo.R;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;

public class Box2DActivity extends AppCompatActivity {

    World mWorld;
    ArrayList<BaseBody> mBodies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (dm.widthPixels < dm.heightPixels) {
            Constant.SCREEN_WIDTH = dm.widthPixels;
            Constant.SCREEN_HEIGHT = dm.heightPixels;

        } else {
            Constant.SCREEN_HEIGHT = dm.widthPixels;
            Constant.SCREEN_WIDTH = dm.heightPixels;
        }

        Vec2 gravity = new Vec2(0.f, 10.f);
        mWorld = new World(gravity);
        mWorld.setAllowSleep(true);
        final int kd = 40;
        RectColor lRc = Box2DUtil.createBox(kd / 4, Constant.SCREEN_HEIGHT / 2, kd / 4, Constant.SCREEN_HEIGHT / 2,
                true, mWorld, 0xFFE6E4FF);
        mBodies.add(lRc);
        RectColor rRc = Box2DUtil.createBox(Constant.SCREEN_WIDTH - kd / 4, Constant.SCREEN_HEIGHT / 2, kd / 4, Constant.SCREEN_HEIGHT / 2,
                true, mWorld, 0xFFE6E4FF);
        mBodies.add(rRc);
        RectColor tRc = Box2DUtil.createBox(Constant.SCREEN_WIDTH / 2, kd / 4, Constant.SCREEN_WIDTH / 2, kd / 4,
                true, mWorld, 0xFFE6E4FF);
        mBodies.add(tRc);
        RectColor bRc = Box2DUtil.createBox(Constant.SCREEN_WIDTH / 2, Constant.SCREEN_HEIGHT - kd / 4, Constant.SCREEN_WIDTH / 2, kd / 4,
                true, mWorld, 0xFFE6E4FF);
        mBodies.add(bRc);
        final int bs = 20;
        final int bw = (Constant.SCREEN_WIDTH - 2 * kd - 11 * bs) / 18;
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < 9 - i; j++) {
                    RectColor rc = Box2DUtil.createBox(
                            kd / 2 + bs + bw / 2 + i * (kd + 5) / 2 + j * (kd + 5) + 3,
                            Constant.SCREEN_HEIGHT + bw - i * (bw + kd) / 2,
                            bw / 2,
                            kd / 2,
                            false, mWorld, 0xFF44E422);
                    mBodies.add(rc);
                }

                for (int j = 0; j < 9 - i; j++) {
                    RectColor rc = Box2DUtil.createBox(
                            3 * kd / 2 + bs - bw / 2 + i * (kd + 5) / 2 + j * (kd + 5) - 3,
                            Constant.SCREEN_HEIGHT + bw - i * (bw + kd) / 2,
                            bw / 2,
                            kd / 2,
                            false, mWorld, 0xFF44E422);
                    mBodies.add(rc);
                }
            } else {

                for (int j = 0; j < 10 - i; j++) {
                    RectColor rc = Box2DUtil.createBox(
                            kd / 2 + bs + kd / 2 + (i - 1) * (kd + 5) / 2 + j * (kd + 5),
                            Constant.SCREEN_HEIGHT - (kd - bw) / 2 - (i - 1) * (bw + kd) / 2,
                            kd / 2,
                            bw / 2,
                            false, mWorld, 0xFF44E422);
                    mBodies.add(rc);
                }

            }


        }
        for (int i = 0; i < 16; i++) {

            CircleColor ball = Box2DUtil.createCircle(Constant.SCREEN_WIDTH / 2 - 24, kd * 30, kd / 2, mWorld,
                    Color.RED);
            mBodies.add(ball);
            ball.body.setLinearVelocity(new Vec2(0, 50));
        }
        setContentView(new GameView(this, mWorld, mBodies));


    }
}
