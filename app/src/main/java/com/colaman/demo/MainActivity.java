package com.colaman.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
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
        mStatusLayout = StatusLayout.init(this, R.layout.activity_main);
        setContentView(mStatusLayout);
        initStatusLayout();
        new CountDownTimer(100000, 3000) {
            int num;

            @Override
            public void onTick(long millisUntilFinished) {
                num++;
                switch (num % 4) {
                    case 0:
                        mStatusLayout.showDefaultContent();
                        break;
                    case 1:
                        mStatusLayout.switchLayout(StatusLayout.LOADING);
                        break;
                    case 2:
                        mStatusLayout.switchLayout(StatusLayout.EMPTY);
                        break;
                    case 3:
                        mStatusLayout.switchLayout(StatusLayout.ERROR);
                        break;
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void initStatusLayout() {
        mStatusLayout
                .add(StatusLayout.LOADING, LayoutInflater.from(this).inflate(R.layout.include_loading, null))
                .add(StatusLayout.ERROR, LayoutInflater.from(this).inflate(R.layout.include_error, null), R.id.btn_retry)
                .setInAnimation(R.anim.anim_in_alpha)
                .setOutAnimation(R.anim.anim_out_alpha)
                .setDefaultAnimation()
                .setLayoutClickListener(new StatusLayout.OnLayoutClickListener() {
                    @Override
                    public void OnLayoutClick(View view, String status) {
                        switch (status) {
                            case LOADING:
                                Toast.makeText(MainActivity.this, LOADING, Toast.LENGTH_SHORT).show();
                                break;
                            case EMPTY:
                                Toast.makeText(MainActivity.this, EMPTY, Toast.LENGTH_SHORT).show();
                                break;
                            case ERROR:
                                mStatusLayout.showDefaultContent();
                                Toast.makeText(MainActivity.this, "显示content", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }
}
