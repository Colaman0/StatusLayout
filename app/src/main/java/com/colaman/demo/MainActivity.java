package com.colaman.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.colaman.statuslayout.StatusLayout;
import com.example.gg.statuslayout.R;

public class MainActivity extends AppCompatActivity {
    public static final String LOADING = "loading";
    public static final String ERROR = "error";
    public static final String EMPTY = "empty";
    private MyClickListener mMyClickListener;
    private StatusLayout mStatusLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMyClickListener = new MyClickListener();
        findViewById(R.id.btn_loading).setOnClickListener(mMyClickListener);
        findViewById(R.id.btn_normal).setOnClickListener(mMyClickListener);
        findViewById(R.id.btn_empty).setOnClickListener(mMyClickListener);
        findViewById(R.id.btn_error).setOnClickListener(mMyClickListener);
        mStatusLayout = findViewById(R.id.status_layout);
        initStatusLayout();
    }

    private void initStatusLayout() {
        mStatusLayout.defaultInit(this, R.layout.include_normal)
                .add(LOADING, R.layout.include_loading, false)
                .add(EMPTY, R.layout.include_empty, false)
                .add(ERROR, R.layout.include_error, false)
                .needAnimation(true);
    }

    private class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_loading:
                    mStatusLayout.switchLayout(LOADING);
                    break;
                case R.id.btn_normal:
                    mStatusLayout.switchLayout(StatusLayout.STATUS_NORMAL);
                    break;
                case R.id.btn_empty:
                    mStatusLayout.switchLayout(EMPTY);
                    break;
                case R.id.btn_error:
                    mStatusLayout.switchLayout(ERROR);
                    break;
                default:
                    break;
            }
        }
    }
}
