package com.colaman.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.colaman.statuslayout.StatusLayout;
import com.example.gg.statuslayout.R;

public class MainActivity extends AppCompatActivity {
    public static final String LOADING = "loading";
    public static final String ERROR = "error";
    public static final String EMPTY = "empty";
    public static final String NORMAL = "normal";
    private MyClickListener mMyClickListener;
    private StatusLayout mStatusLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMyClickListener = new MyClickListener();
        findViewById(R.id.btn_loading).setOnClickListener(mMyClickListener);
        findViewById(R.id.btn_empty).setOnClickListener(mMyClickListener);
        findViewById(R.id.btn_error).setOnClickListener(mMyClickListener);
        mStatusLayout = findViewById(R.id.status_layout);
        initStatusLayout();
    }

    private void initStatusLayout() {
        mStatusLayout
                .add(LOADING, R.layout.include_loading, R.id.tv_loading)
                .add(EMPTY, R.layout.include_empty)
                .add(ERROR, R.layout.include_error)
                .setInAnimation(R.anim.anim_in)
                .setOutAnimation(R.anim.anim_out)
                .setLayoutClickListener(new StatusLayout.OnLayoutClickListener() {
                    @Override
                    public void OnLayoutClick(View view, String status) {
                        switch (status) {
                            case LOADING:
                                ((TextView) view.findViewById(R.id.tv_loading)).setText("load complete");
                                break;
                            case EMPTY:
                                Toast.makeText(MainActivity.this, EMPTY, Toast.LENGTH_SHORT).show();
                                break;
                            case ERROR:
                                Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    private class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_loading:
                    mStatusLayout.switchLayout(LOADING);
                    break;
                case R.id.btn_normal:
                    mStatusLayout.showDefaultContent();
                    break;
                case R.id.btn_empty:
                    // 这里的tag是你在add的时候传的标记，如果要切换原本默认显示的内容(也就是defaultInit里的布局)则传入StatusLayout.STATUS_NORMAL
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
