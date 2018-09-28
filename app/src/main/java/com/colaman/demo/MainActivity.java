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
                //add()方法第一个参数是布局对应的标记，第二个参数是布局资源，第三个参数是表示需不需要延迟加载，true:会用viewstub包装，false:默认的方法
                .add(LOADING, R.layout.include_loading, true)
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
