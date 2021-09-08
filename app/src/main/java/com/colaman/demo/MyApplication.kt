package com.colaman.demo

import android.app.Application

import com.colaman.statuslayout.StatusConfig
import com.colaman.statuslayout.StatusLayout
import com.example.gg.statuslayout.R

/**
 * Create by kyle on 2018/10/23
 * Function :
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 设置全局属性
        StatusLayout.setGlobalData(
            mutableListOf(
                Pair(StatusLayout.EMPTY, StatusConfig(contentLayoutRes = R.layout.include_empty))
            )
        )

//        // 设置全局动画
//        StatusLayout.setGlobalAnim(R.anim.anim_in_alpha, R.anim.anim_out_alpha)
    }
}
