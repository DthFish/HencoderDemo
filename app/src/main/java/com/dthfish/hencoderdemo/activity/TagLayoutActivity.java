package com.dthfish.hencoderdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dthfish.hencoderdemo.R;
import com.dthfish.hencoderdemo.Utils;
import com.dthfish.hencoderdemo.view.TagLayout;

import java.util.Random;

public class TagLayoutActivity extends AppCompatActivity {

    private TagLayout mTl;
    private Random mRandom = new Random();
    public static final int MARGIN = (int) Utils.dpToPx(6);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_layout);
        mTl = findViewById(R.id.tl);
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = mRandom.nextInt(6) + 1;
                TextView tag = (TextView) getLayoutInflater().inflate(R.layout.view_tag, null, false);
                StringBuilder text = new StringBuilder();
                for (int j = 0; j < i; j++) {
                    text.append("哈");
                }
                tag.setText(text.toString());
                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                mTl.addView(tag, params);

            }
        });
        findViewById(R.id.btn_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childCount = mTl.getChildCount();
                if (childCount > 0) {
                    mTl.removeViewAt(childCount - 1);
                }
            }
        });
    }
}
