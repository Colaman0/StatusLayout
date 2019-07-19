package com.colaman.demo;

import android.app.Application;
import android.os.AsyncTask;

import com.colaman.statuslayout.StatusConfig;
import com.colaman.statuslayout.StatusLayout;
import com.example.gg.statuslayout.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Create by kyle on 2018/10/23
 * Function :
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 设置全局属性
        StatusLayout.Companion.setGlobalData(Arrays.asList(
                new StatusConfig(MainActivity.EMPTY, R.layout.include_empty, 0),
                new StatusConfig(MainActivity.ERROR, R.layout.include_error, R.id.btn_retry)));
    }
}
