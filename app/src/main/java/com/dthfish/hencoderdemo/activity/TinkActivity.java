package com.dthfish.hencoderdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dthfish.hencoderdemo.R;
import com.dthfish.hencoderdemo.TinkManager;

public class TinkActivity extends AppCompatActivity {

    private EditText mTv;
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tink);
        mTv = findViewById(R.id.et);

        findViewById(R.id.btn_encrypt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String origin = mTv.getText().toString();
                if (!TextUtils.isEmpty(origin)) {
                    String text = TinkManager.getInstance(TinkActivity.this).encrypt(origin);
                    getSharedPreferences("Mine", MODE_PRIVATE).edit().putString("password", text).apply();
                    mTvResult.append("\n加密：" + origin + "\n结果：" + text);
                }

            }
        });

        findViewById(R.id.btn_decrypt).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String saved = getSharedPreferences("Mine", MODE_PRIVATE).getString("password", "");
                        if (!TextUtils.isEmpty(saved)) {
                            String text = TinkManager.getInstance(TinkActivity.this).decrypt(saved);
                            mTvResult.append("\n解密：" + saved + "\n结果：" + text);
                        }

                    }
                });

        mTvResult = findViewById(R.id.tv_encrypt_result);

        findViewById(R.id.btn_clean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvResult.setText("");
            }
        });


    }
}
