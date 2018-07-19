package com.dthfish.hencoderdemo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dthfish.hencoderdemo.R;
import com.dthfish.hencoderdemo.view.CameraView;

public class CameraActivity extends AppCompatActivity {

    private CameraView mCv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mCv = findViewById(R.id.cv);

        mCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mCv, "rotateY", 0, 45);
                objectAnimator.setDuration(800);

                final ObjectAnimator objectAnimator2 = ObjectAnimator.ofInt(mCv, "rotateZ", 0, 270);
                objectAnimator2.setDuration(4000);

                final ObjectAnimator objectAnimator3 = ObjectAnimator.ofInt(mCv, "rotateLeftY", 0, 45);
                objectAnimator3.setDuration(800);

                objectAnimator3.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mCv.reset();
                    }
                });

                objectAnimator2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        objectAnimator3.start();

                    }
                });

                objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        objectAnimator2.start();
                    }
                });
                objectAnimator.start();

            }
        });


    }
}
