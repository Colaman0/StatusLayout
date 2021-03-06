package com.colaman.demo

import android.app.Application
import android.os.AsyncTask

import com.colaman.statuslayout.StatusConfig
import com.colaman.statuslayout.StatusLayout
import com.example.gg.statuslayout.R

import java.util.ArrayList
import java.util.Arrays

/**
 * Create by kyle on 2018/10/23
 * Function :
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 设置全局属性
        StatusLayout.setGlobalData(
                StatusConfig(status = StatusLayout.STATUS_EMPTY, layoutRes = R.layout.include_empty))
//        // 设置全局动画
//        StatusLayout.setGlobalAnim(R.anim.anim_in_alpha, R.anim.anim_out_alpha)
    }
}
