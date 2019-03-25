package com.dthfish.hencoderdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dthfish.hencoderdemo.R;
import com.dthfish.hencoderdemo.view.AnimationButton;

/**
 * Description
 * Author zhaolizhi
 * Date  2019/3/25.
 */
public class AnimationButtonActivity extends AppCompatActivity {
    private AnimationButton animationButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_button);
        animationButton = findViewById(R.id.btn);
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationButton.startAnimation();
            }
        });

        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationButton.reset();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        animationButton.resume();
    }
}
