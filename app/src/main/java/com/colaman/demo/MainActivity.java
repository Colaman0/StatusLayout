package com.colaman.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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
    private StatusLayout mStatusLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initStatusLayout();
        findViewById(R.id.btn_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatusLayout.showDefaultContent();
            }
        });
        findViewById(R.id.btn_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatusLayout.switchLayout(ERROR);
            }
        });
        findViewById(R.id.btn_empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatusLayout.switchLayout(EMPTY);
            }
        });
        findViewById(R.id.btn_loading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatusLayout.switchLayout(LOADING);
            }
        });
    }

    private void initStatusLayout() {
        View errorView = LayoutInflater.from(this).inflate(R.layout.include_error, mStatusLayout,false);
        errorView.findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("statuslayout", "error click");
            }
        });
        mStatusLayout = findViewById(R.id.status_layout);
        mStatusLayout
                .add(LOADING, LayoutInflater.from(this).inflate(R.layout.include_loading, null))
                .add(ERROR, errorView)
                .setInAnimation(R.anim.anim_in_alpha)
                .setOutAnimation(R.anim.anim_out_alpha)
                .setDefaultAnimation()
                .setLayoutClickListener(new StatusLayout.OnLayoutClickListener() {
                    @Override
                    public void OnLayoutClick(View view, String status) {
                        Log.d("statuslayout", status+" =  click");
                    }
                });
    }
}
