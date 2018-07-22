package com.dthfish.hencoderdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dthfish.hencoderdemo.R;
import com.dthfish.hencoderdemo.view.MaterialEditText;

public class MaterialEditTextActivity extends AppCompatActivity {

    private MaterialEditText met;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_edit_text);
        met = findViewById(R.id.met);
        findViewById(R.id.btn_left_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int leftIcon = met.getLeftIcon();
                if (leftIcon == 0) {
                    met.setLeftIcon(R.drawable.huaji);
                } else {
                    met.setLeftIcon(0);
                }
            }
        });

        findViewById(R.id.btn_use_floating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean useFloatingLabel = met.isUseFloatingLabel();
                met.setUseFloatingLabel(!useFloatingLabel);
            }
        });
    }
}
